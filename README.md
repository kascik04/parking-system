# Smart Parking System

A full-stack web application for managing a smart parking lot, built with **ReactJS** (frontend) and **Spring Boot** (backend). It helps organize parking blocks, slots, and vehicle lanes (IN/OUT) efficiently.

---

## Project Structure
```
parking-system/
│
├── frontend/ # ReactJS app
│ ├── src/
│ └── package.json
│
├── backend/ # Spring Boot app
│ ├── src/
│ └── pom.xml
```

---

## Frontend

- **Tech Stack**: React 19, TailwindCSS, Axios
- **Port**: `localhost:3000`
- **Main features**:
  - Manage parking blocks (add/delete)
  - Manage parking slots (assign block & floor)
  - Manage vehicle lanes (IN, OUT, BOTH)
  - UI: White background, orange action buttons

> To start frontend:
```bash
cd frontend
npm install
npm start
```
---

## Backend
- **Tech Stack**: Spring Boot, MySQL, JPA
- **Port**: `localhost:8080`
- **Database**: `parkingdb`
- **API base path**: `/api`

To start backend:
```
cd backend
# Run via Eclipse or using Maven
./mvnw spring-boot:run
```

# MySQL Database Configuration
```
spring.datasource.url=jdbc:mysql://localhost:3306/parkingdb
spring.datasource.username=root
spring.datasource.password=123456
```

# Hibernate Configuration
```
spring.jpa.hibernate.ddl-auto=update
spring.jpa.open-in-view=false
spring.jpa.show-sql=true
```

# Server Port
```
server.port=8080
```

# Logging
```
logging.level.org.springframework.security=DEBUG
```
