import React, { useState, useEffect, useRef } from 'react';

const CameraMonitor = ({ laneId, laneType, onLicensePlateDetected }) => {
  const videoRef = useRef(null);
  const canvasRef = useRef(null);
  const [isConnected, setIsConnected] = useState(false);
  const [lastDetection, setLastDetection] = useState(null);
  const [isProcessing, setIsProcessing] = useState(false);

  // Kết nối camera stream
  useEffect(() => {
    const connectCamera = async () => {
      try {
        // Mock camera stream - thực tế sẽ connect RTSP
        const stream = await navigator.mediaDevices.getUserMedia({ 
          video: { width: 640, height: 480 } 
        });
        
        if (videoRef.current) {
          videoRef.current.srcObject = stream;
          setIsConnected(true);
        }
      } catch (error) {
        console.error('Camera connection failed:', error);
        setIsConnected(false);
      }
    };

    connectCamera();
  }, [laneId]);

  // Auto capture và OCR mỗi 2 giây
  useEffect(() => {
    const interval = setInterval(() => {
      if (isConnected && !isProcessing) {
        captureAndProcess();
      }
    }, 2000);

    return () => clearInterval(interval);
  }, [isConnected, isProcessing]);

  const captureAndProcess = async () => {
    setIsProcessing(true);
    
    try {
      // Capture frame từ video
      const canvas = canvasRef.current;
      const video = videoRef.current;
      const ctx = canvas.getContext('2d');
      
      canvas.width = video.videoWidth;
      canvas.height = video.videoHeight;
      ctx.drawImage(video, 0, 0);
      
      const imageData = canvas.toDataURL('image/jpeg', 0.8);
      
      // Gửi lên backend để OCR
      const response = await fetch(`/api/camera/ocr`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          laneId: laneId,
          imageData: imageData
        })
      });
      
      const result = await response.json();
      
      if (result.success && result.data.confidence > 0.8) {
        setLastDetection({
          licensePlate: result.data.licensePlate,
          confidence: result.data.confidence,
          timestamp: new Date()
        });
        
        // Callback để trigger check-in/out
        onLicensePlateDetected(result.data.licensePlate, laneType);
      }
      
    } catch (error) {
      console.error('OCR processing failed:', error);
    } finally {
      setIsProcessing(false);
    }
  };

  return (
    <div className="camera-monitor bg-white p-4 rounded-lg shadow">
      <div className="flex justify-between items-center mb-2">
        <h3 className="font-semibold">
          Camera {laneType === 'IN' ? 'Vào' : 'Ra'} - Lane {laneId}
        </h3>
        <div className={`w-3 h-3 rounded-full ${isConnected ? 'bg-green-500' : 'bg-red-500'}`}></div>
      </div>
      
      <div className="relative">
        <video 
          ref={videoRef}
          autoPlay
          muted
          className="w-full h-48 bg-gray-200 rounded"
        />
        
        <canvas 
          ref={canvasRef}
          className="hidden"
        />
        
        {isProcessing && (
          <div className="absolute inset-0 bg-black bg-opacity-50 flex items-center justify-center">
            <div className="text-white">Đang xử lý...</div>
          </div>
        )}
      </div>
      
      {lastDetection && (
        <div className="mt-2 p-2 bg-blue-50 rounded">
          <div className="text-sm">
            <strong>Biển số:</strong> {lastDetection.licensePlate}
          </div>
          <div className="text-sm">
            <strong>Độ tin cậy:</strong> {(lastDetection.confidence * 100).toFixed(1)}%
          </div>
          <div className="text-xs text-gray-500">
            {lastDetection.timestamp.toLocaleTimeString()}
          </div>
        </div>
      )}
    </div>
  );
};

export default CameraMonitor;