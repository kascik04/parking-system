import React from 'react';
import BlockList from './components/BlockList';
import SlotList from './components/SlotList';
import LaneList from './components/LaneList';

function App() {
  return (
    <div className="min-h-screen bg-white text-gray-800 p-6">
      <div className="max-w-6xl mx-auto">
        <h1 className="text-4xl font-bold mb-8 text-center text-orange-500">
          Smart Parking System
        </h1>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div className="border rounded-lg p-4 shadow-md bg-white">
            <h2 className="text-2xl font-semibold mb-4 text-orange-500">Parking Blocks</h2>
            <BlockList />
          </div>
          <div className="border rounded-lg p-4 shadow-md bg-white">
            <h2 className="text-2xl font-semibold mb-4 text-orange-500">Parking Slots</h2>
            <SlotList />
          </div>
          <div className="md:col-span-2 border rounded-lg p-4 shadow-md bg-white">
            <h2 className="text-2xl font-semibold mb-4 text-orange-500">Lanes Management</h2>
            <LaneList />
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;