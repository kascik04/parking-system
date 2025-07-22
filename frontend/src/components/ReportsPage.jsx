import React, { useState, useEffect } from 'react';

const ReportsPage = () => {
  const [dailyReport, setDailyReport] = useState(null);
  const [currentVehicles, setCurrentVehicles] = useState([]);
  const [vehicleHistory, setVehicleHistory] = useState([]);
  const [selectedLicensePlate, setSelectedLicensePlate] = useState('');
  const [selectedDate, setSelectedDate] = useState(new Date().toISOString().split('T')[0]);

  const fetchDailyReport = async (date) => {
    try {
      const response = await fetch(`http://localhost:8080/api/reports/daily?date=${date}`);
      const result = await response.json();
      if (result.success) {
        setDailyReport(result.data);
      }
    } catch (error) {
      console.error('Error fetching daily report:', error);
    }
  };

  const fetchCurrentVehicles = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/reports/current-vehicles');
      const result = await response.json();
      if (result.success) {
        setCurrentVehicles(result.data);
      }
    } catch (error) {
      console.error('Error fetching current vehicles:', error);
    }
  };

  const fetchVehicleHistory = async (licensePlate) => {
    if (!licensePlate) return;
    try {
      const response = await fetch(`http://localhost:8080/api/reports/vehicle-history/${licensePlate}`);
      const result = await response.json();
      if (result.success) {
        setVehicleHistory(result.data);
      }
    } catch (error) {
      console.error('Error fetching vehicle history:', error);
    }
  };

  useEffect(() => {
    fetchDailyReport(selectedDate);
    fetchCurrentVehicles();
  }, [selectedDate]);

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND'
    }).format(amount);
  };

  const formatDateTime = (dateTime) => {
    return new Date(dateTime).toLocaleString('vi-VN');
  };

  return (
    <div className="p-6 bg-gray-100 min-h-screen">
      <h1 className="text-3xl font-bold mb-6 text-center">BÁO CÁO QUẢN LÝ</h1>

      {/* Daily Report Section */}
      <div className="bg-white rounded-lg shadow-md p-6 mb-6">
        <h2 className="text-xl font-semibold mb-4">Báo cáo theo ngày</h2>
        
        <div className="mb-4">
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Chọn ngày:
          </label>
          <input
            type="date"
            value={selectedDate}
            onChange={(e) => setSelectedDate(e.target.value)}
            className="border border-gray-300 rounded-md px-3 py-2"
          />
        </div>

        {dailyReport && (
          <div className="grid grid-cols-2 md:grid-cols-5 gap-4">
            <div className="bg-blue-50 p-4 rounded-lg text-center">
              <div className="text-2xl font-bold text-blue-600">{dailyReport.totalVehicles}</div>
              <div className="text-sm text-gray-600">Tổng xe ra vào</div>
            </div>
            <div className="bg-green-50 p-4 rounded-lg text-center">
              <div className="text-lg font-bold text-green-600">{formatCurrency(dailyReport.totalRevenue)}</div>
              <div className="text-sm text-gray-600">Doanh thu</div>
            </div>
            <div className="bg-yellow-50 p-4 rounded-lg text-center">
              <div className="text-xl font-bold text-yellow-600">{dailyReport.motorcycles}</div>
              <div className="text-sm text-gray-600">Xe máy</div>
            </div>
            <div className="bg-purple-50 p-4 rounded-lg text-center">
              <div className="text-xl font-bold text-purple-600">{dailyReport.cars}</div>
              <div className="text-sm text-gray-600">Ô tô</div>
            </div>
            <div className="bg-red-50 p-4 rounded-lg text-center">
              <div className="text-xl font-bold text-red-600">{dailyReport.vips}</div>
              <div className="text-sm text-gray-600">VIP</div>
            </div>
          </div>
        )}
      </div>

      {/* Current Vehicles Section */}
      <div className="bg-white rounded-lg shadow-md p-6 mb-6">
        <h2 className="text-xl font-semibold mb-4">Xe hiện tại trong bãi ({currentVehicles.length})</h2>
        
        <div className="overflow-x-auto">
          <table className="min-w-full table-auto">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-4 py-2 text-left">Biển số</th>
                <th className="px-4 py-2 text-left">Loại xe</th>
                <th className="px-4 py-2 text-left">Thời gian vào</th>
                <th className="px-4 py-2 text-left">Cổng vào</th>
                <th className="px-4 py-2 text-left">Thời gian đỗ</th>
              </tr>
            </thead>
            <tbody>
              {currentVehicles.map((vehicle) => (
                <tr key={vehicle.id} className="border-b hover:bg-gray-50">
                  <td className="px-4 py-2 font-semibold">{vehicle.licensePlate}</td>
                  <td className="px-4 py-2">{vehicle.vehicleType}</td>
                  <td className="px-4 py-2">{formatDateTime(vehicle.timeIn)}</td>
                  <td className="px-4 py-2">{vehicle.laneIn}</td>
                  <td className="px-4 py-2">
                    {Math.floor((new Date() - new Date(vehicle.timeIn)) / (1000 * 60))} phút
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Vehicle History Section */}
      <div className="bg-white rounded-lg shadow-md p-6">
        <h2 className="text-xl font-semibold mb-4">Lịch sử xe</h2>
        
        <div className="mb-4 flex gap-2">
          <input
            type="text"
            placeholder="Nhập biển số xe"
            value={selectedLicensePlate}
            onChange={(e) => setSelectedLicensePlate(e.target.value)}
            className="border border-gray-300 rounded-md px-3 py-2 flex-1"
          />
          <button
            onClick={() => fetchVehicleHistory(selectedLicensePlate)}
            className="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600"
          >
            Tìm kiếm
          </button>
        </div>

        {vehicleHistory.length > 0 && (
          <div className="overflow-x-auto">
            <table className="min-w-full table-auto">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-4 py-2 text-left">Thời gian vào</th>
                  <th className="px-4 py-2 text-left">Thời gian ra</th>
                  <th className="px-4 py-2 text-left">Loại xe</th>
                  <th className="px-4 py-2 text-left">Phí đỗ xe</th>
                  <th className="px-4 py-2 text-left">Cổng vào/ra</th>
                </tr>
              </thead>
              <tbody>
                {vehicleHistory.map((record) => (
                  <tr key={record.id} className="border-b hover:bg-gray-50">
                    <td className="px-4 py-2">{formatDateTime(record.timeIn)}</td>
                    <td className="px-4 py-2">
                      {record.timeOut ? formatDateTime(record.timeOut) : 'Chưa ra'}
                    </td>
                    <td className="px-4 py-2">{record.vehicleType}</td>
                    <td className="px-4 py-2">
                      {record.fee ? formatCurrency(record.fee) : 'Chưa tính'}
                    </td>
                    <td className="px-4 py-2">{record.laneIn} / {record.laneOut || 'N/A'}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
};

export default ReportsPage;
