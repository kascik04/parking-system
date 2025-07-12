import React, { useEffect, useState } from 'react';
import axios from 'axios';

function LaneList() {
  const [lanes, setLanes] = useState([]);
  const [type, setType] = useState('');
  const [description, setDescription] = useState('');
  const [editing, setEditing] = useState(null);

  const API_BASE = 'http://localhost:8080/api/lanes';

  const fetchLanes = async () => {
    try {
      const res = await axios.get(API_BASE);
      setLanes(res.data);
    } catch (err) {
      console.error(err);
      alert("Failed to fetch lanes");
    }
  };

  useEffect(() => { fetchLanes(); }, []);

  const handleSave = async () => {
    if (!type || !description || !['IN', 'OUT', 'BOTH'].includes(type.toUpperCase())) {
      alert("Please enter valid type (IN, OUT, BOTH) and a description.");
      return;
    }

    const data = { type: type.toUpperCase(), description };

    try {
      if (editing) {
        await axios.put(`${API_BASE}/${editing.id}`, data);
        alert("Lane updated successfully!");
      } else {
        await axios.post(API_BASE, data);
        alert("Lane added successfully!");
      }

      setType('');
      setDescription('');
      setEditing(null);
      fetchLanes();
    } catch (err) {
      console.error(err);
      alert("Failed to save lane");
    }
  };

  const handleEdit = (lane) => {
    setEditing(lane);
    setType(lane.type);
    setDescription(lane.description);
  };

  const handleDelete = async (id, type) => {
    if (type === 'IN' || type === 'OUT') {
      alert("Cannot delete default lanes IN or OUT");
      return;
    }

    if (!window.confirm("Are you sure you want to delete this lane?")) return;

    try {
      await axios.delete(`${API_BASE}/${id}`);
      alert("Lane deleted successfully!");
      fetchLanes();
    } catch (err) {
      console.error(err);
      alert("Failed to delete lane");
    }
  };

  return (
    <div className="bg-white p-4 rounded-xl shadow">
      <h2 className="text-xl font-bold mb-4 text-orange-600">Lane Management</h2>

      <div className="flex gap-2 mb-4 flex-wrap">
        <select
          className="border p-2 w-1/4"
          value={type}
          onChange={e => setType(e.target.value)}
        >
          <option value="">Select Type</option>
          <option value="IN">IN</option>
          <option value="OUT">OUT</option>
          <option value="BOTH">BOTH</option>
        </select>
        <input
          className="border p-2 w-1/2"
          placeholder="Description"
          value={description}
          onChange={e => setDescription(e.target.value)}
        />
        <button
          className="bg-orange-500 text-white px-4 py-2 rounded"
          onClick={handleSave}
        >
          {editing ? 'Update' : 'Add'}
        </button>
      </div>

      <table className="w-full text-sm text-left border">
        <thead>
          <tr className="bg-gray-200">
            <th className="p-2">Type</th>
            <th className="p-2">Description</th>
            <th className="p-2">Actions</th>
          </tr>
        </thead>
        <tbody>
          {lanes.map(lane => (
            <tr key={lane.id} className="border-t">
              <td className="p-2">{lane.type}</td>
              <td className="p-2">{lane.description}</td>
              <td className="p-2 flex gap-2">
                <button
                  className="text-blue-600"
                  onClick={() => handleEdit(lane)}
                >Edit</button>
                <button
                  className="text-red-500"
                  onClick={() => handleDelete(lane.id, lane.type)}
                >Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default LaneList;
