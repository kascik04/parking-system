import React, { useState, useEffect } from 'react';

const Dashboard = () => {
  const [statistics, setStatistics] = useState({
    totalSlots: 0,
    occupiedSlots: 0,
    availableSlots: 0,
    motorbikes: 0,
    cars: 0,
    vips: 0
  });

  const [checkInData, setCheckInData] = useState({
    licensePlate: '',
    cardNumber: '',
    vehicleType: 'Xe máy',
    imageIn: '',
    laneIn: 'LANE_IN_1'
  });

  const [checkOutData, setCheckOutData] = useState({
    licensePlate: '',
    imageOut: '',
    laneOut: 'LANE_OUT_1'
  });

  const [message, setMessage] = useState('');

  // Fetch statistics
  const fetchStatistics = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/parking/statistics');
      const result = await response.json();
      if (result.success) {
        setStatistics(result.data);
      }
    } catch (error) {
      console.error('Error fetching statistics:', error);
    }
  };

  // Check in vehicle
  const handleCheckIn = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/parking/checkin', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(checkInData)
      });
      const result = await response.json();
      setMessage(result.message);
      if (result.success) {
        setCheckInData({ ...checkInData, licensePlate: '', cardNumber: '' });
        fetchStatistics();
      }
    } catch (error) {
      setMessage('Lỗi kết nối: ' + error.message);
    }
  };

  // Check out vehicle
  const handleCheckOut = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/parking/checkout', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(checkOutData)
      });
      const result = await response.json();
      setMessage(result.message);
      if (result.success) {
        setCheckOutData({ ...checkOutData, licensePlate: '' });
        fetchStatistics();
      }
    } catch (error) {
      setMessage('Lỗi kết nối: ' + error.message);
    }
  };

  useEffect(() => {
    fetchStatistics();
    const interval = setInterval(fetchStatistics, 5000); // Auto refresh every 5s
    return () => clearInterval(interval);
  }, []);

  return (
    <div className="min-h-screen bg-gray-100 p-4 font-sans">
      <h1 className="text-3xl font-bold text-center text-orange-500 mb-6">KasickParking</h1>

      {/* Message Display */}
      {message && (
        <div className="bg-blue-100 border border-blue-400 text-blue-700 px-4 py-3 rounded mb-4">
          {message}
        </div>
      )}

      <div className="grid grid-cols-2 gap-4 mb-6">
        {/* Check In Section */}
        <div className="bg-white rounded shadow p-4">
          <h2 className="font-semibold mb-2 text-center text-green-600">XE VÀO</h2>
          <div className="bg-gray-200 h-32 flex items-center justify-center text-gray-500 mb-3">
            Camera vào
          </div>
          
          <div className="space-y-2">
            <input
              type="text"
              placeholder="Biển số xe"
              className="w-full p-2 border rounded"
              value={checkInData.licensePlate}
              onChange={(e) => setCheckInData({...checkInData, licensePlate: e.target.value})}
            />
            <input
              type="text"
              placeholder="Số thẻ"
              className="w-full p-2 border rounded"
              value={checkInData.cardNumber}
              onChange={(e) => setCheckInData({...checkInData, cardNumber: e.target.value})}
            />
            <select
              className="w-full p-2 border rounded"
              value={checkInData.vehicleType}
              onChange={(e) => setCheckInData({...checkInData, vehicleType: e.target.value})}
            >
              <option value="Xe máy">Xe máy</option>
              <option value="Ô tô">Ô tô</option>
              <option value="VIP">VIP</option>
            </select>
            <button
              onClick={handleCheckIn}
              className="w-full bg-green-500 text-white p-2 rounded hover:bg-green-600"
            >
              XE VÀO
            </button>
          </div>
        </div>

        {/* Check Out Section */}
        <div className="bg-white rounded shadow p-4">
          <h2 className="font-semibold mb-2 text-center text-red-600">XE RA</h2>
          <div className="bg-gray-200 h-32 flex items-center justify-center text-gray-500 mb-3">
            Camera ra
          </div>
          
          <div className="space-y-2">
            <input
              type="text"
              placeholder="Biển số xe"
              className="w-full p-2 border rounded"
              value={checkOutData.licensePlate}
              onChange={(e) => setCheckOutData({...checkOutData, licensePlate: e.target.value})}
            />
            <button
              onClick={handleCheckOut}
              className="w-full bg-red-500 text-white p-2 rounded hover:bg-red-600"
            >
              XE RA
            </button>
          </div>
        </div>
      </div>

      {/* Statistics */}
      <div className="bg-white rounded shadow p-6">
        <h2 className="text-xl font-semibold mb-4 text-center">THỐNG KÊ BÃI XE</h2>
        
        <div className="grid grid-cols-3 gap-4 mb-4">
          <div className="text-center">
            <div className="text-2xl font-bold text-blue-600">{statistics.totalSlots}</div>
            <div className="text-sm text-gray-600">Tổng chỗ</div>
          </div>
          <div className="text-center">
            <div className="text-2xl font-bold text-red-600">{statistics.occupiedSlots}</div>
            <div className="text-sm text-gray-600">Đã sử dụng</div>
          </div>
          <div className="text-center">
            <div className="text-2xl font-bold text-green-600">{statistics.availableSlots}</div>
            <div className="text-sm text-gray-600">Còn trống</div>
          </div>
        </div>

        <div className="grid grid-cols-3 gap-4">
          <div className="text-center bg-yellow-50 p-3 rounded">
            <div className="text-xl font-bold text-yellow-600">{statistics.motorbikes}</div>
            <div className="text-sm text-gray-600">Xe máy</div>
          </div>
          <div className="text-center bg-blue-50 p-3 rounded">
            <div className="text-xl font-bold text-blue-600">{statistics.cars}</div>
            <div className="text-sm text-gray-600">Ô tô</div>
          </div>
          <div className="text-center bg-purple-50 p-3 rounded">
            <div className="text-xl font-bold text-purple-600">{statistics.vips}</div>
            <div className="text-sm text-gray-600">VIP</div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
