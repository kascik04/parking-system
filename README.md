# Smart Parking System

A comprehensive full-stack web application for managing smart parking operations, built with **React 19** frontend and **Spring Boot** backend. The system provides real-time vehicle tracking, automated entry/exit processing, and intelligent parking management.

---

## Features

### Core Functionality
- Automated vehicle entry and exit management
- Real-time parking statistics and monitoring
- Multi-vehicle type support (motorbike, car, VIP, electric)
- Parking block and slot management
- Vehicle lane configuration (IN/OUT/BOTH)
- Membership card integration
- Responsive dashboard interface

### Technical Features
- RESTful API architecture
- Real-time data synchronization
- Cross-platform compatibility
- Database persistence with MySQL
- Security framework ready
- CORS-enabled communication

---

## Technology Stack

**Frontend:**
- React 19 with modern hooks
- TailwindCSS for styling
- Axios for API communication
- Responsive design principles

**Backend:**
- Java 17 with Spring Boot 3.x
- Spring Security for authentication
- Spring Data JPA with Hibernate
- MySQL 8.0 database
- Maven build system

---

## Project Structure

```
parking-system/
│
├── frontend/                    # React application
│   ├── src/
│   │   ├── components/          # React components
│   │   ├── App.js              # Main application
│   │   └── index.js            # Entry point
│   └── package.json
│
├── backend/                     # Spring Boot application
│   ├── src/main/java/com/example/parkingsystem/
│   │   ├── config/             # Configuration classes
│   │   ├── controller/         # REST controllers
│   │   ├── entity/             # JPA entities
│   │   ├── repository/         # Data repositories
│   │   ├── service/            # Business logic
│   │   └── ParkingSystemApplication.java
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
│
└── README.md
```

---

## Prerequisites

- Java 17+
- Node.js 16+
- MySQL 8.0+
- Maven 3.6+

---

## Installation

### Database Setup
```sql
CREATE DATABASE parkingdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### Backend Setup
```bash
cd backend
./mvnw clean install
./mvnw spring-boot:run
```
Server runs on `http://localhost:8080`

### Frontend Setup
```bash
cd frontend
npm install
npm start
```
Application runs on `http://localhost:3000`

---

## Configuration

### Backend Configuration (`application.properties`)
```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/parkingdb
spring.datasource.username=root
spring.datasource.password=123456
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.open-in-view=false
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Server Configuration
server.port=8080

# CORS Configuration
cors.allowed-origins=http://localhost:3000

# Logging
logging.level.org.springframework.security=DEBUG
logging.level.com.example.parkingsystem=INFO
```

### Frontend Configuration
Frontend automatically connects to backend at `http://localhost:8080/api`

---

## API Documentation

### Base URL
```
http://localhost:8080/api
```

### Core Endpoints

#### Parking Management
```
GET    /parking/statistics          # Get parking statistics
POST   /parking/checkin            # Vehicle entry
POST   /parking/checkout           # Vehicle exit
GET    /parking/test               # Health check
```

#### Block Management
```
GET    /blocks                     # Get all blocks
POST   /blocks                     # Create block
PUT    /blocks/{id}                # Update block
DELETE /blocks/{id}                # Delete block
```

#### Slot Management
```
GET    /slots                      # Get all slots
POST   /slots                      # Create slot
PUT    /slots/{id}                 # Update slot
DELETE /slots/{id}                 # Delete slot
```

#### Lane Management
```
GET    /lanes                      # Get all lanes
POST   /lanes                      # Create lane
PUT    /lanes/{id}                 # Update lane
DELETE /lanes/{id}                 # Delete lane
```

---

## Database Schema

### Core Tables
- `parking_blocks` - Physical parking areas
- `parking_slots` - Individual parking spaces
- `vehicle_logs` - Entry/exit records
- `lanes` - Vehicle entry/exit lanes
- `cameras` - Surveillance equipment
- `barriers` - Gate control systems
- `users` - System users

---

## Development

### Running Development Environment
```bash
# Backend (Terminal 1)
cd backend
./mvnw spring-boot:run

# Frontend (Terminal 2)
cd frontend
npm start
```

### Development Tools
- Backend: Spring Boot DevTools for hot reload
- Frontend: React development server with hot refresh
- Database: MySQL Workbench or phpMyAdmin

---

## Deployment

### Production Build

#### Backend
```bash
cd backend
./mvnw clean package
java -jar target/parking-system-1.0.0.jar
```

#### Frontend
```bash
cd frontend
npm run build
```

### Docker Deployment
```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_DATABASE: parkingdb
    ports:
      - "3306:3306"
    
  backend:
    build: ./backend
    ports:
      - "8080:8080"
    depends_on:
      - mysql
      
  frontend:
    build: ./frontend
    ports:
      - "3000:3000"
    depends_on:
      - backend
```

---

## Troubleshooting

### Common Issues

**Database Connection Error**
- Verify MySQL service is running
- Check credentials in `application.properties`
- Ensure database exists

**CORS Error**
- Check CORS configuration in backend
- Verify frontend URL in allowed origins

**Port Already in Use**
- Change port in configuration files
- Kill existing processes using the ports

**Build Failures**
- Clear npm cache: `npm cache clean --force`
- Clean Maven: `./mvnw clean`
- Check Java and Node versions

---

## Contributing

1. Fork the repository
2. Create feature branch: `git checkout -b feature/new-feature`
3. Commit changes: `git commit -m 'Add new feature'`
4. Push to branch: `git push origin feature/new-feature`
5. Submit pull request

### Development Standards
- Follow existing code style
- Add tests for new functionality
- Update documentation
- Ensure all tests pass

---


## Credits

**Developer:** Trần Minh Khang - 2251010048 - Open University

**Repository:** https://github.com/kascik04/parking-system
