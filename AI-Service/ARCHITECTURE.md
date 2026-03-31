# AI-Service Architecture & Visual Guide

## 🏗️ Complete System Architecture

```
┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
┃                                                                            ┃
┃                      MICROSERVICES ARCHITECTURE                           ┃
┃                                                                            ┃
┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛

┌─────────────────────────────────────────────────────────────────────────────┐
│                         EXTERNAL SERVICES                                   │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  ┌──────────────────────┐    ┌──────────────────┐    ┌─────────────────┐  │
│  │ Activities Service   │    │  Eureka Server   │    │  OpenAI API     │  │
│  │  (Port: 8082)        │    │  (Port: 8761)    │    │  (External)     │  │
│  │                      │    │                  │    │                 │  │
│  │ • Produces events    │    │ • Service        │    │ • GPT Models    │  │
│  │ • Activity CRUD      │    │   discovery      │    │ • Chat models   │  │
│  │ • Kafka producer     │    │ • Registration   │    │ • Completions   │  │
│  └──────┬───────────────┘    └──────────────────┘    └─────────────────┘  │
│         │                                                     ▲             │
│         │ Publishes Activities                               │             │
│         │                                                     │ HTTP API    │
└─────────┼──────────────────────────────────────────────────────┼───────────┘
          │                                                     │
          │ JSON Messages                                       │
          ▼                                                     │
┌──────────────────────────────────────────────────────────────┼────────────┐
│                          KAFKA                               │            │
├───────────────────────────────────────────────────────────────┼────────────┤
│                                                               │            │
│  ┌────────────────────────────────────────────────────────┐  │            │
│  │              activity-events Topic                    │  │            │
│  │  (Partitions: 1 | Replication Factor: 1)             │  │            │
│  ├────────────────────────────────────────────────────────┤  │            │
│  │                                                        │  │            │
│  │  [Activity Message 1]  [Activity Message 2]  [...]  │  │            │
│  │  {ActivityId: 123}     {ActivityId: 124}            │  │            │
│  │  {UserId: 456}         {UserId: 789}                │  │            │
│  │                                                        │  │            │
│  └────────────────────────────────────────────────────────┘  │            │
│         ▲                              ▼                     │            │
│         │                              │                     │            │
│   Producer                      Consumer Group:              │            │
│  (Activities           activity-processor-group             │            │
│   Service)             (AI-Service Instance)                │            │
│                                                              │            │
└──────────────────────────────────────────────────────────────┼────────────┘
                                                               │
          ┌────────────────────────────────────────────────────┘
          │
          ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                          AI-SERVICE (Port: 8083)                            │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  ┌─────────────────────────────────────────────────────────────────────┐  │
│  │                    ActivityMessageListener                          │  │
│  │  (Kafka Consumer - @KafkaListener)                                 │  │
│  │                                                                     │  │
│  │  ┌──────────────────────────────────────────────────────────────┐ │  │
│  │  │ 1. Receive Activity Message                                │ │  │
│  │  │ 2. Validate activity data                                 │ │  │
│  │  │ 3. Deserialize JSON → Activity object                    │ │  │
│  │  │ 4. Call OpenAIService.generateRecommendation()          │ │  │
│  │  │ 5. Call RecommendationService.saveRecommendation()     │ │  │
│  │  │ 6. Handle errors gracefully                            │ │  │
│  │  └──────────────────────────────────────────────────────────────┘ │  │
│  └──────────────┬────────────────────────────────┬────────────────────┘  │
│                 │                                │                       │
│                 ▼                                ▼                       │
│     ┌──────────────────────┐        ┌──────────────────────┐            │
│     │  OpenAIService       │        │ RecommendationService│            │
│     │                      │        │                      │            │
│     │ • Build prompt       │        │ • Save records       │            │
│     │ • Call OpenAI API    │        │ • Query by userId    │            │
│     │ • Parse JSON         │        │ • Query by activity  │            │
│     │ • Extract fields     │        │ • Handle errors      │            │
│     │ • Fallback handling  │        │                      │            │
│     └──────────┬───────────┘        └──────────┬───────────┘            │
│               │                                │                        │
│               │ Builds                         │ Calls Repository       │
│               │ structured prompt              │                        │
│               │                                ▼                        │
│               │                     ┌──────────────────────┐            │
│               │                     │ Recommendation       │            │
│               │                     │ Repository          │            │
│               │                     │ (MongoRepository)    │            │
│               │                     └──────────┬───────────┘            │
│               │                                │                        │
│               ▼ HTTP POST                       │                        │
│     ┌──────────────────────────────┐           │                        │
│     │   OpenAI ChatClient          │           │                        │
│     │ (Spring AI Configuration)    │           │                        │
│     │                              │           │                        │
│     │ • System Prompt (from file)  │           │                        │
│     │ • User Prompt (structured)   │           │                        │
│     │ • Model: GPT-4/3.5           │           │                        │
│     │ • Retry logic                │           │                        │
│     │ • Error handling             │           │                        │
│     └──────────┬───────────────────┘           │                        │
│               │                                │                        │
│               │ JSON Response                  │                        │
│               │                                │                        │
│               └────────────────────────────────┘                        │
│                                                                         │
│  ┌─────────────────────────────────────────────────────────────────┐  │
│  │          recommendationController (REST API)                    │  │
│  │  GET /api/recommendations/user/{userId}                        │  │
│  │  GET /api/recommendations/activity/{activityId}               │  │
│  │  GET /api/recommendations/{id}                                │  │
│  │  GET /api/recommendations/user/{userId}/with-stats           │  │
│  │  GET /api/recommendations/health                             │  │
│  └────────────────────────────┬─────────────────────────────────┘  │
│                               │                                     │
│                               │ JSON Response                       │
└───────────────────────────────┼─────────────────────────────────────┘
                                │
                                ▼
                    ┌───────────────────────┐
                    │   Client Applications │
                    │  (Web/Mobile/Desktop) │
                    │                       │
                    │ Fetch recommendations │
                    │ Display to users      │
                    │ Track progress        │
                    └───────────────────────┘
```

---

## 📊 Data Flow Diagram

```
STEP 1: Activity Generation (Activities Service)
┌─────────────────────────────────────────────────────┐
│  User completes a workout (Running, Cycling, etc.)  │
└──────────────────┬──────────────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────────────────┐
│  Activities Service creates Activity object:        │
│  {                                                  │
│    "ActivityId": 123,                              │
│    "userId": 456,                                  │
│    "type": "RUNNING",                              │
│    "duration": 30,                                 │
│    "caloriesBurned": 300,                          │
│    "startTime": "2024-03-31T10:00:00"             │
│  }                                                 │
└──────────────────┬──────────────────────────────────┘
                   │
                   ▼
STEP 2: Kafka Publishing
┌─────────────────────────────────────────────────────┐
│  Serialize to JSON                                  │
│  Send to Kafka topic: activity-events              │
│  Kafka broker stores in partition                  │
└──────────────────┬──────────────────────────────────┘
                   │
                   ▼
STEP 3: Kafka Consumption (AI-Service)
┌─────────────────────────────────────────────────────┐
│  Consumer group: activity-processor-group           │
│  ActivityMessageListener wakes up                   │
│  Deserializes JSON → Activity object                │
│  Validates data                                     │
└──────────────────┬──────────────────────────────────┘
                   │
                   ▼
STEP 4: AI Recommendation Generation
┌─────────────────────────────────────────────────────┐
│  Build structured prompt with activity details      │
│  Include system prompt (fitness coach character)    │
│  Request JSON format response from OpenAI          │
└──────────────────┬──────────────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────────────────┐
│  OpenAI API Call (ChatClient)                       │
│  Model: GPT-4 or GPT-3.5-turbo                     │
│  Returns JSON response:                             │
│  {                                                  │
│    "overview": "Great cardio session...",          │
│    "suggestions": [...],                           │
│    "improvements": [...],                          │
│    "safety_alerts": [...]                          │
│  }                                                 │
└──────────────────┬──────────────────────────────────┘
                   │
                   ▼
STEP 5: Response Parsing & Storage
┌─────────────────────────────────────────────────────┐
│  Parse JSON response                                │
│  Extract fields: overview, suggestions, etc.       │
│  Create Recommendation object                      │
│  Save to MongoDB collection: recommendations       │
└──────────────────┬──────────────────────────────────┘
                   │
                   ▼
STEP 6: API Retrieval
┌─────────────────────────────────────────────────────┐
│  Client calls REST API:                             │
│  GET /api/recommendations/user/456                 │
│                                                    │
│  Controller queries MongoDB                        │
│  Returns recommendations as JSON                   │
└──────────────────┬──────────────────────────────────┘
                   │
                   ▼
STEP 7: Client Presentation
┌─────────────────────────────────────────────────────┐
│  Client receives recommendations                    │
│  Displays to user:                                  │
│  - Workout overview                                 │
│  - Improvement suggestions                         │
│  - Safety tips                                      │
│  - Next steps                                       │
└─────────────────────────────────────────────────────┘
```

---

## 🔄 Kafka Message Flow

```
TIME ──────────────────────────────────────────────────────────────────>

Activities Service (Producer):
  │
  ├─ [Activity 1: Running] → Kafka Topic
  │                           │
  │                           ├─ [Partition 0]
  │                           │   ├─ Offset 0: Activity 1
  │                           │   ├─ Offset 1: Activity 2
  │                           │   └─ Offset 2: Activity 3
  │                           │
  │                           └─ Broker stores messages
  │
  ├─ [Activity 2: Cycling] → Kafka Topic
  │
  └─ [Activity 3: Swimming] → Kafka Topic

AI-Service (Consumer):
  Consumer Group: activity-processor-group
  
  Consumer Instance 1:
  │
  ├─ Reads Offset 0 ─ Deserialize ─ Validate ─ Process ─ Save to MongoDB
  │
  ├─ Reads Offset 1 ─ Deserialize ─ Validate ─ Process ─ Save to MongoDB
  │
  └─ Reads Offset 2 ─ Deserialize ─ Validate ─ Process ─ Save to MongoDB
  
  Offset Tracking:
  Kafka stores the consumer offset (e.g., Offset 2 processed)
  If consumer crashes, it resumes from Offset 3 on restart
```

---

## 🗄️ Database Schema

```
MongoDB Database: aiservice

Collection: recommendations
├─ _id: ObjectId (Primary Key)
├─ activityId: Long (Indexed)
├─ userId: Long (Indexed)
├─ recommendation: String (overview text)
├─ suggestions: Array<String>
│  ├─ "Suggestion 1"
│  ├─ "Suggestion 2"
│  └─ "Suggestion 3"
├─ improvements: Array<String>
│  ├─ "Improvement 1"
│  ├─ "Improvement 2"
│  └─ "Improvement 3"
├─ safety: Array<String>
│  ├─ "Safety alert 1"
│  └─ "Safety alert 2"
├─ createdAt: DateTime (Indexed)
└─ updatedAt: DateTime

Indexes:
1. { userId: 1 }
2. { activityId: 1 }
3. { userId: 1, createdAt: -1 }
```

---

## 🔧 Configuration & Environment

```
┌─────────────────────────────────────────────────────┐
│         Environment Variables                       │
├─────────────────────────────────────────────────────┤
│                                                     │
│  OPENAI_API_KEY                                    │
│  └─ Value: sk-xxxxxxxxxxxxxxxxxxxxxxxx...         │
│     Used: AiConfig loads into ChatClient          │
│                                                     │
└─────────────────────────────────────────────────────┘
                    │
                    ▼
┌─────────────────────────────────────────────────────┐
│         application.yaml                            │
├─────────────────────────────────────────────────────┤
│                                                     │
│  spring.ai.openai.api-key: ${OPENAI_API_KEY}     │
│  spring.kafka.bootstrap-servers: localhost:9092   │
│  spring.data.mongodb.uri: mongodb://...          │
│  server.port: 8083                                │
│  kafka.topic.name: activity-events               │
│                                                     │
└─────────────────────────────────────────────────────┘
                    │
                    ▼
┌─────────────────────────────────────────────────────┐
│         Spring Boot Application                     │
├─────────────────────────────────────────────────────┤
│                                                     │
│  ✓ Initializes ChatClient bean                    │
│  ✓ Configures Kafka consumer                      │
│  ✓ Connects to MongoDB                            │
│  ✓ Starts REST controller                         │
│  ✓ Registers with Eureka                          │
│                                                     │
└─────────────────────────────────────────────────────┘
```

---

## 🌐 Port Mappings

```
Service                Port    Access URL                      Purpose
─────────────────────────────────────────────────────────────────────
AI-Service             8083    http://localhost:8083           REST API
Kafka                  9092    localhost:9092                  Bootstrap
Zookeeper              2181    localhost:2181                  Cluster Mgmt
MongoDB                27017   localhost:27017                 Database
Mongo Express          8081    http://localhost:8081           MongoDB UI
Kafka UI               8080    http://localhost:8080           Kafka Monitoring
Eureka Server          8761    http://localhost:8761           Service Registry
Activities Service     8082    http://localhost:8082           External Service
```

---

## 💾 Deployment Topology

```
DEVELOPMENT ENVIRONMENT:
┌─────────────────────────────────────────────────┐
│           Local Machine                         │
├─────────────────────────────────────────────────┤
│                                                 │
│  ┌──────────────────────────────────────────┐  │
│  │         Docker (All Services)            │  │
│  │  ┌──────┐  ┌────────┐  ┌──────────────┐ │  │
│  │  │Kafka │  │MongoDB │  │Kafka UI/    │ │  │
│  │  │      │  │        │  │Mongo Exp    │ │  │
│  │  └──────┘  └────────┘  └──────────────┘ │  │
│  └──────────────────────────────────────────┘  │
│                                                 │
│  ┌──────────────────────────────────────────┐  │
│  │    AI-Service (Maven Spring-Boot)        │  │
│  │    Running in IDE or Terminal            │  │
│  └──────────────────────────────────────────┘  │
│                                                 │
└─────────────────────────────────────────────────┘

PRODUCTION ENVIRONMENT (Recommended):
┌──────────────────────────────────────────────────────────┐
│                Kubernetes Cluster                         │
├──────────────────────────────────────────────────────────┤
│                                                           │
│  ┌──────────────────────────────────────────────────┐   │
│  │             Kafka Cluster                        │   │
│  │  (Multiple Brokers, Zookeeper Quorum)          │   │
│  └──────────────────────────────────────────────────┘   │
│                                                           │
│  ┌──────────────────────────────────────────────────┐   │
│  │           MongoDB Replica Set                    │   │
│  │  (Primary + Secondaries)                        │   │
│  └──────────────────────────────────────────────────┘   │
│                                                           │
│  ┌──────────────────────────────────────────────────┐   │
│  │  AI-Service Instances (Scaled Horizontally)     │   │
│  │  ┌────────┐ ┌────────┐ ┌────────┐              │   │
│  │  │Pod 1   │ │Pod 2   │ │Pod N   │              │   │
│  │  │Instance│ │Instance│ │Instance│              │   │
│  │  └────────┘ └────────┘ └────────┘              │   │
│  └──────────────────────────────────────────────────┘   │
│                                                           │
│  ┌──────────────────────────────────────────────────┐   │
│  │          Load Balancer / API Gateway             │   │
│  └──────────────────────────────────────────────────┘   │
│                                                           │
└──────────────────────────────────────────────────────────┘
```

---

## 🔐 Security & Best Practices

```
┌─────────────────────────────────────────────────────┐
│            SECURITY LAYERS                          │
├─────────────────────────────────────────────────────┤
│                                                     │
│  1. Environment Variables                          │
│     └─ Never hardcode API keys                    │
│     └─ Use environment-specific configs           │
│                                                     │
│  2. Network Security                               │
│     └─ Enable Kafka SSL/SASL in production        │
│     └─ Use VPCs for MongoDB access                │
│     └─ Implement API authentication               │
│                                                     │
│  3. Database Security                              │
│     └─ MongoDB authentication enabled             │
│     └─ Implement access control lists             │
│     └─ Regular backups scheduled                  │
│                                                     │
│  4. Application Security                           │
│     └─ Input validation on all APIs               │
│     └─ Error handling without exposing details    │
│     └─ Logging for audit trails                   │
│                                                     │
│  5. Code Security                                  │
│     └─ Dependencies regularly updated             │
│     └─ CVE scanning enabled                       │
│     └─ Code review process                        │
│                                                     │
└─────────────────────────────────────────────────────┘
```

---

## 📈 Scalability Considerations

```
HORIZONTAL SCALING:
┌─────────────────────────────────────────────────────┐
│  Multiple AI-Service Instances                      │
│                                                     │
│  Instance 1 ──┐                                     │
│              │                                     │
│  Instance 2 ──┼─→ Kafka Consumer Group             │
│              │   (activity-processor-group)        │
│  Instance N ──┘                                     │
│                                                     │
│  Each instance processes different partitions      │
│  Automatic rebalancing on instance join/leave      │
└─────────────────────────────────────────────────────┘

VERTICAL SCALING:
┌─────────────────────────────────────────────────────┐
│  Single AI-Service with more resources:             │
│                                                     │
│  ✓ Increase JVM heap size (-Xmx)                   │
│  ✓ More Kafka consumer threads                     │
│  ✓ Better CPU for processing                       │
│  ✓ More MongoDB connections                        │
│                                                     │
│  Limitations:                                       │
│  ✗ Single point of failure                         │
│  ✗ Limited by single machine resources             │
│  ✗ Difficult to update/restart                     │
└─────────────────────────────────────────────────────┘

RECOMMENDED APPROACH:
Horizontal Scaling + Load Distribution
```

---

**Last Updated:** March 31, 2026  
**Architecture Version:** 1.0

