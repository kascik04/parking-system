import React, { useState, useEffect } from 'react';

const ParkingMap = () => {
  const [blocks, setBlocks] = useState([]);
  const [selectedBlock, setSelectedBlock] = useState(null);
  const [slots, setSlots] = useState([]);

  useEffect(() => {
    fetchBlocks();
  }, []);

  const fetchBlocks = async () => {
    try {
      const response = await fetch('/api/blocks');
      const result = await response.json();
      setBlocks(result.data || []);
    } catch (error) {
      console.error('Error fetching blocks:', error);
    }
  };

  const fetchSlotsByBlock = async (blockId) => {
    try {
      const response = await fetch(`/api/slots/block/${blockId}`);
      const result = await response.json();
      setSlots(result.data || []);
    } catch (error) {
      console.error('Error fetching slots:', error);
    }
  };

  const handleBlockClick = (block) => {
    setSelectedBlock(block);
    fetchSlotsByBlock(block.id);
  };

  const getSlotColor = (slot) => {
    if (!slot.isActive) return 'bg-gray-400'; // Bảo trì
    if (slot.isOccupied) return 'bg-red-500';  // Đã có xe
    return 'bg-green-500'; // Trống
  };

  return (
    <div className="parking-map p-6">
      <h2 className="text-2xl font-bold mb-4">Sơ đồ bãi xe</h2>
      
      {/* Block overview */}
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-6">
        {blocks.map((block) => (
          <div
            key={block.id}
            onClick={() => handleBlockClick(block)}
            className={`cursor-pointer p-4 rounded-lg border-2 transition-all ${
              selectedBlock?.id === block.id 
                ? 'border-blue-500 bg-blue-50' 
                : 'border-gray-300 hover:border-gray-400'
            }`}
          >
            <h3 className="font-semibold">{block.name}</h3>
            <div className="text-sm text-gray-600">
              {block.occupiedSlots || 0}/{block.totalSlots} slots
            </div>
            <div className={`w-full h-2 mt-2 rounded ${
              (block.occupiedSlots / block.totalSlots) > 0.8 
                ? 'bg-red-200' 
                : 'bg-green-200'
            }`}>
              <div 
                className={`h-full rounded ${
                  (block.occupiedSlots / block.totalSlots) > 0.8 
                    ? 'bg-red-500' 
                    : 'bg-green-500'
                }`}
                style={{ 
                  width: `${(block.occupiedSlots / block.totalSlots) * 100}%` 
                }}
              ></div>
            </div>
          </div>
        ))}
      </div>

      {/* Detailed slot view */}
      {selectedBlock && (
        <div className="bg-white rounded-lg shadow p-6">
          <h3 className="text-xl font-semibold mb-4">
            Chi tiết Block: {selectedBlock.name}
          </h3>
          
          {/* Floor tabs */}
          <div className="flex space-x-2 mb-4">
            {[1, 2, 3, 4, 5].map((floor) => (
              <button
                key={floor}
                className="px-3 py-1 rounded bg-gray-200 hover:bg-gray-300"
              >
                Tầng {floor}
              </button>
            ))}
          </div>

          {/* Slot grid */}
          <div className="grid grid-cols-10 gap-2">
            {slots.map((slot) => (
              <div
                key={slot.id}
                className={`
                  w-8 h-12 rounded text-xs text-white flex items-center justify-center
                  ${getSlotColor(slot)}
                  cursor-pointer transition-all hover:scale-110
                `}
                title={`Slot ${slot.slotNumber} - ${slot.isOccupied ? 'Đã có xe' : 'Trống'}`}
              >
                {slot.slotNumber?.split('-')[1] || slot.id}
              </div>
            ))}
          </div>

          {/* Legend */}
          <div className="flex space-x-4 mt-4 text-sm">
            <div className="flex items-center">
              <div className="w-4 h-4 bg-green-500 rounded mr-2"></div>
              Trống
            </div>
            <div className="flex items-center">
              <div className="w-4 h-4 bg-red-500 rounded mr-2"></div>
              Đã có xe
            </div>
            <div className="flex items-center">
              <div className="w-4 h-4 bg-gray-400 rounded mr-2"></div>
              Bảo trì
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ParkingMap;