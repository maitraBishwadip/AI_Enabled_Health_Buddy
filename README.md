# 🏋️ Fitness Microservice Application

> An intelligent AI-powered fitness tracking microservice architecture that provides personalized activity recommendations using GPT-4

## 📋 Table of Contents

- [Overview](#overview)
- [System Architecture](#system-architecture)
- [Key Features](#key-features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Workflow Explanation](#workflow-explanation)
- [Microservices Overview](#microservices-overview)
- [API Endpoints](#api-endpoints)
- [Getting Started](#getting-started)
- [Service Communication](#service-communication)

---

## 🎯 Overview

This is a **scalable microservice-based fitness application** designed to track user activities and provide AI-generated personalized health recommendations. The architecture leverages Spring Cloud for service discovery, configuration management, and API gateway routing, combined with Apache Kafka for asynchronous event processing and OpenAI's GPT-4 for intelligent fitness recommendations.

**Key Goal**: Enable fitness enthusiasts to track activities and receive AI-powered recommendations on how to improve their health based on their fitness goals and activity patterns.

---

## 🏗️ System Architecture

![Architecture Diagram](Architecture highlevel plan.png)

### Architecture Overview

The system follows a **microservice architecture** with the following components:

### Core Components:

1. **API Gateway Service** - Entry point for all client requests
2. **User Service** - Manages user registration and profile
3. **Activity Service** - Handles activity tracking and logging
4. **AI Service** - Generates personalized recommendations using OpenAI
5. **Config Server** - Centralized configuration management
6. **Eureka Server** - Service discovery
7. **Kafka** - Asynchronous event streaming
8. **Databases** - MongoDB for data persistence
9. **Keycloak** - Authentication and authorization

---

## ⚡ Key Features

✅ **Centralized API Gateway** - Single entry point with Keycloak authentication
✅ **Service Discovery** - Eureka-based automatic service registration
✅ **Centralized Configuration** - Spring Cloud Config for environment-specific settings
✅ **AI-Powered Recommendations** - GPT-4 integration for personalized fitness advice
✅ **Asynchronous Processing** - Kafka for non-blocking activity processing
✅ **MVC Pattern** - Clean separation of concerns across all services
✅ **MongoDB** - Document-based NoSQL storage for flexibility
✅ **Scalable Design** - Microservices can be scaled independently

---

## 💻 Tech Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| **Framework** | Spring Boot | 4.0.x |
| **Cloud** | Spring Cloud | 2025.1.1 |
| **Java** | OpenJDK | 21 |
| **AI Integration** | Spring AI + OpenAI | 2.0.0-M3 |
| **Service Discovery** | Netflix Eureka | Latest |
| **API Gateway** | Spring Cloud Gateway | Latest |
| **Message Broker** | Apache Kafka | Latest |
| **Database** | MongoDB | Latest |
| **Authentication** | Keycloak | Latest |
| **Build Tool** | Maven | 3.x |

---

## 📁 Project Structure

```
FIitnessMicroservice/
├── APIGateWay/                 # API Gateway Service
│   ├── src/main/java/
│   ├── src/test/java/
│   └── pom.xml
│
├── userservice/                # User Management Service
│   ├── src/main/java/
│   │   ├── controllers/        # REST Controllers
│   │   ├── services/           # Business Logic
│   │   ├── dto/                # Data Transfer Objects
│   │   └── repository/         # Data Access Layer
│   └── pom.xml
│
├── demo/                       # Activity Service
│   ├── src/main/java/
│   │   ├── controller/         # REST Controllers
│   │   ├── services/           # Business Logic
│   │   ├── dto/                # Data Transfer Objects
│   │   ├── model/              # Entity Models
│   │   └── repository/         # Data Access Layer
│   └── pom.xml
│
├── AI-Service/                 # AI Recommendation Service
│   ├── src/main/java/
│   │   ├── controller/         # REST Controllers
│   │   ├── service/            # AI Logic & Kafka Subscriber
│   │   ├── model/              # Entity Models
│   │   └── repository/         # Data Access Layer
│   └── pom.xml
│
├── eureka/                     # Eureka Discovery Server
│   └── pom.xml
│
├── configServer/               # Spring Cloud Config Server
│   └── pom.xml
│
└── Architecture highlevel plan.png

```

---

## 🔄 Workflow Explanation

### Complete Request Flow:

```
┌─────────────────────────────────────────────────────────────────┐
│ 1. CLIENT REQUEST (Frontend/PostMan)                             │
│    • User makes HTTP request to track activity                  │
└─────────────────────────┬───────────────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────────────┐
│ 2. API GATEWAY SERVICE                                           │
│    • Receives all incoming requests                             │
│    • Routes to appropriate microservice                         │
│    • Handles authentication via Keycloak                        │
└─────────────────────────┬───────────────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────────────┐
│ 3. AUTHENTICATION (Keycloak)                                    │
│    • Validates user credentials                                 │
│    • Issues JWT tokens                                          │
│    • Creates new users if registering                           │
└─────────────────────────┬───────────────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────────────┐
│ 4. ACTIVITY SERVICE (Synchronous Call)                          │
│    • Receives activity tracking request                         │
│    • Makes synchronous REST call to User Service               │
│    • Validates user legitimacy                                  │
│    • Stores activity in MongoDB                                 │
└─────────────────────────┬───────────────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────────────┐
│ 5. KAFKA PUBLISHING (Asynchronous)                              │
│    • Activity Service publishes activity event to Kafka        │
│    • Kafka acts as event broker                                │
│    • Non-blocking operation                                    │
└─────────────────────────┬───────────────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────────────┐
│ 6. AI SERVICE CONSUMER (Kafka Subscriber)                       │
│    • Listens to activity events from Kafka                      │
│    • Extracts activity details                                  │
│    • Performs prompt engineering and optimization              │
└─────────────────────────┬───────────────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────────────┐
│ 7. AI PROCESSING                                                │
│    • Sends optimized prompt to OpenAI API                       │
│    • Generates personalized recommendations:                    │
│      - Duration analysis                                        │
│      - Heart rate assessment                                    │
│      - Health improvement insights                              │
│      - Goal-based suggestions                                   │
│      - Cautions and safety tips                                │
└─────────────────────────┬───────────────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────────────┐
│ 8. STORAGE & RESPONSE                                           │
│    • Stores recommendations in MongoDB                          │
│    • Returns response to AI Service                             │
│    • Data ready for frontend retrieval                          │
└─────────────────────────┬───────────────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────────────┐
│ 9. FRONTEND ACCESS (On Demand)                                  │
│    • Frontend requests recommendations via API                  │
│    • AI Service provides AI-generated insights                  │
└─────────────────────────────────────────────────────────────────┘
```

### Key Communication Patterns:

| Pattern | Used For | Details |
|---------|----------|---------|
| **Synchronous (REST)** | Activity → User Service | Validates user legitimacy immediately |
| **Asynchronous (Kafka)** | Activity → AI Service | Event-driven, non-blocking processing |
| **Service Discovery** | All Services | Eureka for dynamic service location |
| **Configuration** | All Services | Config Server for centralized config |

---

## 🎯 Microservices Overview

### 1. **API Gateway Service**
- **Purpose**: Entry point for all client requests
- **Responsibilities**:
  - Route requests to appropriate services
  - Authenticate via Keycloak
  - Load balancing
- **Technology**: Spring Cloud Gateway
- **Port**: Default 8080

### 2. **User Service**
- **Purpose**: Manage user registration and profile
- **Key Endpoints**:
  - `POST /api/users/register` - Register new user
  - `GET /api/users/{userId}` - Fetch user profile
  - `GET /api/users/{userId}/validate` - Validate user existence
- **Database**: MongoDB
- **Pattern**: MVC

### 3. **Activity Service**
- **Purpose**: Track and manage user activities
- **Key Features**:
  - Accepts activity submissions
  - Validates user via User Service (sync)
  - Publishes events to Kafka
  - Stores activities in MongoDB
- **Key Endpoints**:
  - `POST /activities` - Track new activity
- **Communication**:
  - Synchronous: Calls User Service
  - Asynchronous: Publishes to Kafka
- **Pattern**: MVC

### 4. **AI Service** (Recommendation Engine)
- **Purpose**: Generate AI-powered fitness recommendations
- **Key Features**:
  - Kafka subscriber for activity events
  - Prompt engineering for OpenAI
  - Stores recommendations in MongoDB
- **Key Endpoints**:
  - `GET /api/recommendations/user/{userId}` - Get user recommendations
  - `GET /api/recommendations/activity/{activityId}` - Get activity recommendations
  - `GET /api/recommendations/{recommendationId}` - Get specific recommendation
  - `GET /api/recommendations/user/{userId}/with-stats` - Get stats
- **External Integration**: OpenAI API (GPT-4)
- **Pattern**: MVC

### 5. **Eureka Discovery Server**
- **Purpose**: Service registration and discovery
- **Responsibilities**:
  - All services register themselves
  - Enables service-to-service discovery
  - Health monitoring

### 6. **Config Server**
- **Purpose**: Centralized configuration management
- **Responsibilities**:
  - Store environment-specific configs
  - Provides config to all services
  - Enables configuration updates without redeploy

---

## 📡 API Endpoints

### User Service
```
POST   /api/users/register          - Register new user
GET    /api/users/{userId}          - Get user profile
GET    /api/users/{userId}/validate - Validate user exists
```

### Activity Service
```
POST   /activities                  - Track new activity
```

### AI Service (Recommendations)
```
GET    /api/recommendations/user/{userId}              - Get user recommendations
GET    /api/recommendations/activity/{activityId}      - Get activity recommendations
GET    /api/recommendations/{recommendationId}         - Get specific recommendation
GET    /api/recommendations/user/{userId}/with-stats   - Get recommendations with stats
GET    /api/recommendations/health                     - Health check
```

---

## 🚀 Getting Started

### Prerequisites
- Java 21+
- Maven 3.6+
- Docker & Docker Compose (for Kafka, MongoDB, Keycloak)
- Git

### Installation Steps

1. **Clone the Repository**
```bash
git clone <repository-url>
cd FIitnessMicroservice
```

2. **Start Infrastructure Services**
```bash
# Start Kafka, MongoDB, Keycloak, Eureka using Docker
docker-compose up -d
```

3. **Build the Project**
```bash
mvn clean install
```

4. **Start Services in Order**

```bash
# 1. Start Eureka (Discovery Server)
cd eureka
mvn spring-boot:run

# 2. Start Config Server
cd ../configServer
mvn spring-boot:run

# 3. Start API Gateway
cd ../APIGateWay
mvn spring-boot:run

# 4. Start User Service
cd ../userservice/userservice
mvn spring-boot:run

# 5. Start Activity Service
cd ../demo/demo
mvn spring-boot:run

# 6. Start AI Service
cd ../AI-Service
mvn spring-boot:run
```

5. **Verify Services**
- Eureka Dashboard: `http://localhost:8761`
- API Gateway: `http://localhost:8080`

---

## 🔐 Service Communication

### Service Dependencies

```
Frontend
   ↓
API Gateway (Port 8080)
   ├─→ Keycloak (Authentication)
   ├─→ User Service (Port 8081)
   ├─→ Activity Service (Port 8082)
   └─→ AI Service (Port 8083)

Activity Service
   ├─→ User Service (Sync REST)
   └─→ Kafka Topic (Async Publish)

AI Service
   ├─→ Kafka Topic (Subscribe)
   └─→ OpenAI API (External)
```

### Message Flow (Kafka)

**Topic**: `activity-events`

**Message Structure**:
```json
{
  "userId": "user-123",
  "activityId": "activity-456",
  "activityType": "running",
  "duration": 30,
  "heartRate": 145,
  "caloriesBurned": 300,
  "timestamp": "2026-04-03T10:30:00Z"
}
```

---

## 📊 Database Schema

### User Service (MongoDB)
```json
{
  "_id": "ObjectId",
  "keycloakId": "string",
  "name": "string",
  "email": "string",
  "fitnessGoal": "string",
  "age": "number",
  "height": "number",
  "weight": "number"
}
```

### Activity Service (MongoDB)
```json
{
  "_id": "ObjectId",
  "userId": "string",
  "activityType": "string",
  "duration": "number",
  "heartRate": "number",
  "caloriesBurned": "number",
  "timestamp": "date",
  "metadata": "object"
}
```

### AI Service - Recommendations (MongoDB)
```json
{
  "_id": "ObjectId",
  "userId": "string",
  "activityId": "string",
  "recommendation": "string",
  "healthImprovement": "string",
  "suggestions": ["string"],
  "cautions": ["string"],
  "createdAt": "date"
}
```

---

## 🔑 Key Design Patterns

### 1. **MVC Pattern** (All Services)
- **Model**: Entity classes and DTOs
- **View**: REST responses
- **Controller**: REST endpoints

### 2. **Microservice Pattern**
- Each service is independently deployable
- Own database per service
- Communicate via REST and Kafka

### 3. **API Gateway Pattern**
- Single entry point
- Routing logic
- Authentication enforcement

### 4. **Event-Driven Architecture**
- Kafka for asynchronous processing
- Decouples Activity Service from AI Service
- Enables scalability

### 5. **Service Discovery Pattern**
- Eureka for dynamic service registration
- No hardcoded service URLs

### 6. **Externalized Configuration**
- Config Server manages properties
- Environment-specific configurations

---

## 🛡️ Security Features

✅ **Keycloak Integration** - OAuth 2.0 / OpenID Connect
✅ **JWT Tokens** - Secure inter-service communication
✅ **User Validation** - Activity Service validates users before processing
✅ **Service Discovery Protection** - Eureka authentication

---

## 📈 Scalability

This architecture supports:
- **Horizontal Scaling**: Each microservice can be scaled independently
- **Load Balancing**: API Gateway distributes requests
- **Event-Driven Processing**: Kafka handles spike in activities
- **Database Sharding**: MongoDB supports sharding for large datasets

---

## 🐛 Troubleshooting

### Services Not Registering with Eureka
- Ensure Eureka server is running
- Check `application.yaml` for Eureka URL configuration

### Kafka Not Processing Messages
- Verify Kafka is running
- Check AI Service logs for subscription errors
- Ensure topic `activity-events` exists

### OpenAI API Errors
- Verify API key in Config Server
- Check OpenAI account quota
- Review prompt engineering in AI Service

---

## 📚 Future Enhancements

- [ ] WebSocket support for real-time recommendations
- [ ] Caching layer (Redis) for frequently accessed recommendations
- [ ] Advanced analytics dashboard
- [ ] Mobile app integration
- [ ] Machine learning model for personalized insights
- [ ] Multi-language support for recommendations

---

## 👨‍💻 Developer Notes

### Local Development
```bash
# Build without running tests
mvn clean package -DskipTests

# Run with debug mode
mvn spring-boot:run -Dspring-boot.run.arguments="--debug"
```

### Common Issues
1. **Port conflicts**: Change ports in `application.yaml`
2. **MongoDB connection**: Ensure MongoDB is running and accessible
3. **Kafka connectivity**: Check broker configuration in services

---

## 📞 Support

For issues or questions, please refer to the individual service documentation:
- `AI-Service/DOCUMENTATION.md`
- `AI-Service/ARCHITECTURE.md`

---

## 📝 License

This project is part of the Fitness Microservice Suite.

---

**Created**: April 2026
**Last Updated**: April 3, 2026

