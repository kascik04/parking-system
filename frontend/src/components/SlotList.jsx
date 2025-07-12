import React, { useEffect, useState } from 'react';
import axios from 'axios';

function SlotList() {
  const [slots, setSlots] = useState([]);
  const [newSlot, setNewSlot] = useState({ blockId: '', floor: '', isOccupied: false });

  const fetchSlots = () => {
    axios.get('http://localhost:8080/api/slots')
      .then(res => setSlots(res.data))
      .catch(err => {
        console.error(err);
        alert("Failed to fetch slots");
      });
  };

  useEffect(() => {
    fetchSlots();
  }, []);

  const handleDelete = (id) => {
    if (window.confirm('Are you sure you want to delete this slot?')) {
      axios.delete(`http://localhost:8080/api/slots/${id}`)
        .then(() => {
          setSlots(prev => prev.filter(slot => slot.id !== id));
          alert("Deleted successfully!");
        })
        .catch(err => {
          console.error(err);
          alert("Failed to delete slot");
        });
    }
  };

  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target;
    setNewSlot(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const handleAdd = () => {
    const { blockId, floor } = newSlot;

    if (!blockId || floor === '') {
      alert("Please enter valid blockId and floor.");
      return;
    }

    axios.post('http://localhost:8080/api/slots', {
      ...newSlot,
      floor: parseInt(newSlot.floor, 10)
    })
      .then(() => {
        fetchSlots();
        setNewSlot({ blockId: '', floor: '', isOccupied: false });
        alert("Slot added successfully!");
      })
      .catch(err => {
        console.error(err);
        alert("Failed to add slot");
      });
  };

  return (
    <div className="bg-white shadow-md rounded-lg p-4">
      <h2 className="text-2xl font-semibold text-orange-500 mb-4">Parking Slots</h2>

      <div className="mb-4 grid grid-cols-1 md:grid-cols-3 gap-4">
        <input
          type="text"
          name="blockId"
          placeholder="Block ID"
          value={newSlot.blockId}
          onChange={handleInputChange}
          className="border px-3 py-1 rounded"
        />
        <input
          type="number"
          name="floor"
          placeholder="Floor"
          value={newSlot.floor}
          onChange={handleInputChange}
          className="border px-3 py-1 rounded"
        />
        <label className="flex items-center space-x-2">
          <input
            type="checkbox"
            name="isOccupied"
            checked={newSlot.isOccupied}
            onChange={handleInputChange}
          />
          <span>Occupied</span>
        </label>
      </div>

      <button
        onClick={handleAdd}
        className="mb-6 bg-orange-500 text-white px-4 py-2 rounded hover:bg-orange-600"
      >
        Add Slot
      </button>

      <table className="w-full text-left border-collapse">
        <thead>
          <tr>
            <th className="border-b py-2">ID</th>
            <th className="border-b py-2">Block</th>
            <th className="border-b py-2">Floor</th>
            <th className="border-b py-2">Occupied</th>
            <th className="border-b py-2">Actions</th>
          </tr>
        </thead>
        <tbody>
          {slots.map(slot => (
            <tr key={slot.id} className="hover:bg-gray-100">
              <td className="py-2 border-b">{slot.id}</td>
              <td className="py-2 border-b">{slot.block?.name || `Block ID ${slot.blockId}`}</td>
              <td className="py-2 border-b">{slot.floor}</td>
              <td className="py-2 border-b">{slot.isOccupied ? 'Yes' : 'No'}</td>
              <td className="py-2 border-b space-x-2">
                <button
                  className="text-sm text-white bg-orange-500 hover:bg-orange-600 px-3 py-1 rounded"
                  onClick={() => handleDelete(slot.id)}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default SlotList;
