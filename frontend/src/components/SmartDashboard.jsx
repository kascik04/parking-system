import React, { useState, useEffect } from 'react';
import CameraMonitor from './CameraMonitor';
import ParkingMap from './ParkingMap';

const SmartDashboard = () => {
  const [statistics, setStatistics] = useState({});
  const [recentActivity, setRecentActivity] = useState([]);
  const [alerts, setAlerts] = useState([]);
  const [isAutoMode, setIsAutoMode] = useState(true);

  // WebSocket cho real-time updates
  useEffect(() => {
    const ws = new WebSocket('ws://localhost:8080/parking-updates');
    
    ws.onmessage = (event) => {
      const data = JSON.parse(event.data);
      
      switch (data.type) {
        case 'statistics-updated':
          setStatistics(data.payload);
          break;
        case 'vehicle-checkin':
          setRecentActivity(prev => [data.payload, ...prev.slice(0, 9)]);
          break;
        case 'vehicle-checkout':
          setRecentActivity(prev => [data.payload, ...prev.slice(0, 9)]);
          break;
        case 'alert':
          setAlerts(prev => [data.payload, ...prev.slice(0, 4)]);
          break;
      }
    };

    return () => ws.close();
  }, []);

  // Auto trigger vehicle processing
  const handleLicensePlateDetected = async (licensePlate, laneType) => {
    if (!isAutoMode) return;

    try {
      const endpoint = laneType === 'IN' ? '/api/parking/smart-checkin' : '/api/parking/smart-checkout';
      
      const response = await fetch(endpoint, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ licensePlate })
      });

      const result = await response.json();
      
      if (result.success) {
        setAlerts(prev => [{
          type: 'success',
          message: `${laneType === 'IN' ? 'Xe vào' : 'Xe ra'} thành công: ${licensePlate}`,
          timestamp: new Date()
        }, ...prev]);
      } else {
        setAlerts(prev => [{
          type: 'error',
          message: result.message,
          timestamp: new Date()
        }, ...prev]);
      }
    } catch (error) {
      console.error('Processing failed:', error);
    }
  };

  return (
    <div className="smart-dashboard min-h-screen bg-gray-100 p-4">
      <div className="grid grid-cols-12 gap-4">
        
        {/* Header */}
        <div className="col-span-12 bg-white rounded-lg shadow p-4">
          <div className="flex justify-between items-center">
            <h1 className="text-2xl font-bold">Hệ thống gửi xe thông minh</h1>
            <div className="flex items-center space-x-4">
              <label className="flex items-center">
                <input
                  type="checkbox"
                  checked={isAutoMode}
                  onChange={(e) => setIsAutoMode(e.target.checked)}
                  className="mr-2"
                />
                Chế độ tự động
              </label>
              <div className="text-sm text-gray-500">
                {new Date().toLocaleString()}
              </div>
            </div>
          </div>
        </div>

        {/* Camera monitors */}
        <div className="col-span-6">
          <CameraMonitor 
            laneId={1} 
            laneType="IN" 
            onLicensePlateDetected={handleLicensePlateDetected}
          />
        </div>
        
        <div className="col-span-6">
          <CameraMonitor 
            laneId={2} 
            laneType="OUT" 
            onLicensePlateDetected={handleLicensePlateDetected}
          />
        </div>

        {/* Statistics */}
        <div className="col-span-12 bg-white rounded-lg shadow p-4">
          <h2 className="text-lg font-semibold mb-3">Thống kê theo thời gian thực</h2>
          <div className="grid grid-cols-2 md:grid-cols-6 gap-4">
            <div className="text-center">
              <div className="text-2xl font-bold text-blue-600">{statistics.totalSlots || 0}</div>
              <div className="text-sm text-gray-600">Tổng slot</div>
            </div>
            <div className="text-center">
              <div className="text-2xl font-bold text-green-600">{statistics.availableSlots || 0}</div>
              <div className="text-sm text-gray-600">Còn trống</div>
            </div>
            <div className="text-center">
              <div className="text-2xl font-bold text-red-600">{statistics.occupiedSlots || 0}</div>
              <div className="text-sm text-gray-600">Đã sử dụng</div>
            </div>
            <div className="text-center">
              <div className="text-2xl font-bold text-yellow-600">{statistics.motorbikes || 0}</div>
              <div className="text-sm text-gray-600">Xe máy</div>
            </div>
            <div className="text-center">
              <div className="text-2xl font-bold text-purple-600">{statistics.cars || 0}</div>
              <div className="text-sm text-gray-600">Ô tô</div>
            </div>
            <div className="text-center">
              <div className="text-2xl font-bold text-pink-600">{statistics.vips || 0}</div>
              <div className="text-sm text-gray-600">VIP</div>
            </div>
          </div>
        </div>

        {/* Recent activity */}
        <div className="col-span-6 bg-white rounded-lg shadow p-4">
          <h2 className="text-lg font-semibold mb-3">Hoạt động gần đây</h2>
          <div className="space-y-2 max-h-64 overflow-y-auto">
            {recentActivity.map((activity, index) => (
              <div key={index} className="flex justify-between items-center p-2 bg-gray-50 rounded text-sm">
                <span>{activity.licensePlate}</span>
                <span className={activity.type === 'IN' ? 'text-green-600' : 'text-blue-600'}>
                  {activity.type === 'IN' ? 'Vào' : 'Ra'}
                </span>
                <span className="text-gray-500 text-xs">
                  {new Date(activity.timestamp).toLocaleTimeString()}
                </span>
              </div>
            ))}
          </div>
        </div>

        {/* Alerts */}
        <div className="col-span-6 bg-white rounded-lg shadow p-4">
          <h2 className="text-lg font-semibold mb-3">Cảnh báo & Thông báo</h2>
          <div className="space-y-2 max-h-64 overflow-y-auto">
            {alerts.map((alert, index) => (
              <div key={index} className={`p-2 rounded text-sm ${
                alert.type === 'success' ? 'bg-green-100 text-green-700' :
                alert.type === 'error' ? 'bg-red-100 text-red-700' :
                'bg-yellow-100 text-yellow-700'
              }`}>
                <div>{alert.message}</div>
                <div className="text-xs opacity-70">
                  {new Date(alert.timestamp).toLocaleTimeString()}
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Parking map */}
        <div className="col-span-12">
          <ParkingMap />
        </div>
      </div>
    </div>
  );
};

export default SmartDashboard;