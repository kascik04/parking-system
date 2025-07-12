import React, { useEffect, useState } from 'react';
import axios from 'axios';

function BlockList() {
  const [blocks, setBlocks] = useState([]);
  const [newBlock, setNewBlock] = useState({ name: '', totalSlots: '' });

  useEffect(() => {
    fetchBlocks();
  }, []);

  const fetchBlocks = () => {
    axios.get('http://localhost:8080/api/blocks')
      .then(res => setBlocks(res.data))
      .catch(err => console.error(err));
  };

  const handleDelete = (id) => {
    if (window.confirm('Are you sure you want to delete this block?')) {
      axios.delete(`http://localhost:8080/api/blocks/${id}`)
        .then(() => fetchBlocks())
        .catch(err => console.error(err));
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewBlock(prev => ({ ...prev, [name]: value }));
  };

  const handleCreate = () => {
    if (!newBlock.name || !newBlock.totalSlots) return;
    axios.post('http://localhost:8080/api/blocks', {
      name: newBlock.name,
      totalSlots: parseInt(newBlock.totalSlots, 10)
    })
    .then(() => {
      setNewBlock({ name: '', totalSlots: '' });
      fetchBlocks();
    })
    .catch(err => console.error(err));
  };

  return (
    <div className="bg-white shadow-md rounded-lg p-4">
      <h2 className="text-2xl font-semibold text-orange-500 mb-4">Parking Blocks</h2>

      <div className="flex gap-2 mb-4">
        <input
          type="text"
          name="name"
          placeholder="Block Name"
          value={newBlock.name}
          onChange={handleInputChange}
          className="border rounded px-2 py-1"
        />
        <input
          type="number"
          name="totalSlots"
          placeholder="Total Slots"
          value={newBlock.totalSlots}
          onChange={handleInputChange}
          className="border rounded px-2 py-1"
        />
        <button
          onClick={handleCreate}
          className="bg-orange-500 text-white px-3 py-1 rounded hover:bg-orange-600"
        >
          Add Block
        </button>
      </div>

      <table className="w-full text-left border-collapse">
        <thead>
          <tr>
            <th className="border-b py-2">ID</th>
            <th className="border-b py-2">Name</th>
            <th className="border-b py-2">Total Slots</th>
            <th className="border-b py-2">Actions</th>
          </tr>
        </thead>
        <tbody>
          {blocks.map(block => (
            <tr key={block.id} className="hover:bg-gray-100">
              <td className="py-2 border-b">{block.id}</td>
              <td className="py-2 border-b">{block.name}</td>
              <td className="py-2 border-b">{block.totalSlots}</td>
              <td className="py-2 border-b">
                <button 
                  className="text-sm text-white bg-orange-500 hover:bg-orange-600 px-3 py-1 rounded"
                  onClick={() => handleDelete(block.id)}
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

export default BlockList;
