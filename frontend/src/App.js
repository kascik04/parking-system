import React, { useState, useEffect } from 'react';
import './App.css';

function App() {
  const [statistics, setStatistics] = useState({
    totalSlots: 0,
    occupiedSlots: 0,
    availableSlots: 0,
    motorbikes: 0,
    cars: 0,
    vips: 0
  });
  
  const [entryForm, setEntryForm] = useState({
    licensePlate: '',
    cardNumber: '',
    vehicleType: 'MOTORBIKE'
  });
  
  const [exitForm, setExitForm] = useState({
    licensePlate: ''
  });
  
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);

  // Fetch statistics from backend
  const fetchStatistics = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/parking/statistics');
      if (response.ok) {
        const data = await response.json();
        setStatistics(data);
        setMessage('');
      } else {
        setMessage('Không thể kết nối với server');
      }
    } catch (error) {
      setMessage('Lỗi kết nối: ' + error.message);
    }
  };

  // Handle vehicle entry
  const handleVehicleEntry = async (e) => {
    e.preventDefault();
    setLoading(true);
    
    try {
      const formData = new FormData();
      formData.append('licensePlate', entryForm.licensePlate);
      formData.append('cardNumber', entryForm.cardNumber);
      formData.append('vehicleType', entryForm.vehicleType);
      formData.append('laneInId', '1'); // Default lane
      
      const response = await fetch('http://localhost:8080/api/parking/checkin', {
        method: 'POST',
        body: formData
      });
      
      const data = await response.json();
      
      if (data.success) {
        setMessage(`✅ ${data.message}`);
        setEntryForm({ licensePlate: '', cardNumber: '', vehicleType: 'MOTORBIKE' });
        fetchStatistics(); // Refresh stats
      } else {
        setMessage(`❌ ${data.error}`);
      }
    } catch (error) {
      setMessage('❌ Lỗi kết nối: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  // Handle vehicle exit
  const handleVehicleExit = async (e) => {
    e.preventDefault();
    setLoading(true);
    
    try {
      const formData = new FormData();
      formData.append('licensePlate', exitForm.licensePlate);
      formData.append('laneOutId', '2'); // Default lane
      
      const response = await fetch('http://localhost:8080/api/parking/checkout', {
        method: 'POST',
        body: formData
      });
      
      const data = await response.json();
      
      if (data.success) {
        setMessage(`✅ ${data.message}. Phí đỗ xe: ${data.fee?.toLocaleString()} VNĐ`);
        setExitForm({ licensePlate: '' });
        fetchStatistics(); // Refresh stats
      } else {
        setMessage(`❌ ${data.error}`);
      }
    } catch (error) {
      setMessage('❌ Lỗi kết nối: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchStatistics();
    const interval = setInterval(fetchStatistics, 5000); // Auto refresh every 5s
    return () => clearInterval(interval);
  }, []);

  return (
    <div className="app">
      {/* Header */}
      <header className="app-header">
        <div className="container">
          <h1 className="app-title">🅿️ KasickParking</h1>
          <div className="header-status">
            <span className={`status-indicator ${message.includes('❌') ? 'error' : 'success'}`}>
              {message.includes('❌') ? '🔴' : '🟢'}
            </span>
            <span className="status-text">
              {message || 'Hệ thống hoạt động bình thường'}
            </span>
          </div>
        </div>
      </header>

      <div className="container">
        {/* Main Dashboard */}
        <div className="dashboard">
          {/* Entry Section */}
          <div className="section entry-section">
            <div className="section-header">
              <h2>🚗 XE VÀO</h2>
            </div>
            
            <div className="camera-preview">
              <div className="camera-placeholder">
                <span>📷 Camera vào</span>
              </div>
            </div>
            
            <form onSubmit={handleVehicleEntry} className="entry-form">
              <div className="form-group">
                <label>Biển số xe</label>
                <input
                  type="text"
                  value={entryForm.licensePlate}
                  onChange={(e) => setEntryForm({...entryForm, licensePlate: e.target.value})}
                  placeholder="VD: 29A-12345"
                  required
                />
              </div>
              
              <div className="form-group">
                <label>Số thẻ (tùy chọn)</label>
                <input
                  type="text"
                  value={entryForm.cardNumber}
                  onChange={(e) => setEntryForm({...entryForm, cardNumber: e.target.value})}
                  placeholder="Số thẻ thành viên"
                />
              </div>
              
              <div className="form-group">
                <label>Loại xe</label>
                <select
                  value={entryForm.vehicleType}
                  onChange={(e) => setEntryForm({...entryForm, vehicleType: e.target.value})}
                >
                  <option value="MOTORBIKE">🏍️ Xe máy</option>
                  <option value="CAR">🚗 Ô tô</option>
                  <option value="VIP">👑 VIP</option>
                  <option value="ELECTRIC">⚡ Xe điện</option>
                </select>
              </div>
              
              <button type="submit" className="btn btn-primary" disabled={loading}>
                {loading ? '⏳ Đang xử lý...' : '✅ XE VÀO'}
              </button>
            </form>
          </div>

          {/* Exit Section */}
          <div className="section exit-section">
            <div className="section-header">
              <h2>🚪 XE RA</h2>
            </div>
            
            <div className="camera-preview">
              <div className="camera-placeholder">
                <span>📷 Camera ra</span>
              </div>
            </div>
            
            <form onSubmit={handleVehicleExit} className="exit-form">
              <div className="form-group">
                <label>Biển số xe</label>
                <input
                  type="text"
                  value={exitForm.licensePlate}
                  onChange={(e) => setExitForm({...exitForm, licensePlate: e.target.value})}
                  placeholder="VD: 29A-12345"
                  required
                />
              </div>
              
              <button type="submit" className="btn btn-danger" disabled={loading}>
                {loading ? '⏳ Đang xử lý...' : '🚪 XE RA'}
              </button>
            </form>
          </div>
        </div>

        {/* Statistics */}
        <div className="statistics-section">
          <h2>📊 THỐNG KÊ BÃI XE</h2>
          
          <div className="stats-grid">
            <div className="stat-card total">
              <div className="stat-number">{statistics.totalSlots}</div>
              <div className="stat-label">Tổng chỗ</div>
              <div className="stat-icon">🅿️</div>
            </div>
            
            <div className="stat-card occupied">
              <div className="stat-number">{statistics.occupiedSlots}</div>
              <div className="stat-label">Đã sử dụng</div>
              <div className="stat-icon">🚗</div>
            </div>
            
            <div className="stat-card available">
              <div className="stat-number">{statistics.availableSlots}</div>
              <div className="stat-label">Còn trống</div>
              <div className="stat-icon">✅</div>
            </div>
          </div>
          
          <div className="vehicle-stats">
            <div className="vehicle-stat">
              <span className="vehicle-number">{statistics.motorbikes}</span>
              <span className="vehicle-label">🏍️ Xe máy</span>
            </div>
            
            <div className="vehicle-stat">
              <span className="vehicle-number">{statistics.cars}</span>
              <span className="vehicle-label">🚗 Ô tô</span>
            </div>
            
            <div className="vehicle-stat">
              <span className="vehicle-number">{statistics.vips}</span>
              <span className="vehicle-label">👑 VIP</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;
