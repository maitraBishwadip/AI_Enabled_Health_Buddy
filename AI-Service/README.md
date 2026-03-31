# AI-Service Microservice - Quick Start Guide

## Overview

The **AI-Service** is a Spring Boot microservice that processes fitness activities from Kafka topics using OpenAI's GPT models and Spring AI to generate personalized fitness recommendations.

## 🚀 Quick Start

### Prerequisites
- Java 21+
- Maven 3.6+
- Docker (for Kafka & MongoDB)
- OpenAI API Key

### Setup

1. **Set environment variable for OpenAI API:**
   ```powershell
   $env:OPENAI_API_KEY = "your-api-key-here"
   ```

2. **Start dependencies (Docker):**
   ```bash
   # Kafka
   docker run -d -p 9092:9092 -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 confluentinc/cp-kafka:latest
   
   # MongoDB
   docker run -d -p 27017:27017 mongo:latest
   
   # Eureka Server (if not running)
   # Ensure it's on port 8761
   ```

3. **Build and run:**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **Verify:**
   ```bash
   curl http://localhost:8083/api/recommendations/health
   ```

## 📋 API Endpoints

### Get User Recommendations
```
GET /api/recommendations/user/{userId}
```

### Get Activity Recommendations
```
GET /api/recommendations/activity/{activityId}
```

### Get Specific Recommendation
```
GET /api/recommendations/{recommendationId}
```

### Health Check
```
GET /api/recommendations/health
```

## 🏗️ Architecture

```
Activities Service (Kafka Producer)
        ↓ (sends Activity events)
    Kafka Topic: activity-events
        ↓
AI-Service (Kafka Consumer)
        ↓ (processes activity)
    OpenAI API (ChatClient)
        ↓ (gets recommendations)
    Response Parser
        ↓ (extracts JSON)
    MongoDB Storage
        ↓
REST API Endpoints
```

## 📚 Key Features

- ✅ **Real-time Processing**: Kafka consumer listens for activity events
- ✅ **AI-Powered**: Uses OpenAI GPT models via Spring AI
- ✅ **Structured Responses**: Generates JSON recommendations with:
  - Overview of workout
  - Suggestions for improvement
  - Areas to focus on
  - Safety alerts
- ✅ **Persistent Storage**: Saves recommendations to MongoDB
- ✅ **REST API**: Easy access to recommendations
- ✅ **Scalable**: Eureka service discovery ready

## 🔧 Configuration

Edit `src/main/resources/application.yaml`:

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092  # Kafka connection
  data:
    mongodb:
      uri: mongodb://localhost:27017/aiservice  # MongoDB connection
  ai:
    openai:
      api-key: ${OPENAI_API_KEY}  # Environment variable
```

## 💡 How It Works

1. **Activities Service** publishes activity events to Kafka
2. **AI-Service** Kafka listener receives the activity
3. **OpenAI Service** builds a prompt with activity details
4. **ChatClient** (Spring AI) calls OpenAI API
5. **Response Parser** extracts JSON from the response
6. **Recommendation Service** saves to MongoDB
7. **Controller** provides REST API access

## 📝 Response Example

```json
{
  "status": "SUCCESS",
  "data": [
    {
      "id": "507f1f77bcf86cd799439011",
      "activityId": 123,
      "userId": 456,
      "recommendation": "Great cardio session! You maintained a steady pace for 30 minutes.",
      "suggestions": [
        "Increase intensity with interval training",
        "Add strength training 2x per week",
        "Track heart rate for better insights"
      ],
      "improvements": [
        "Work on running form",
        "Build aerobic endurance",
        "Reduce recovery time between sessions"
      ],
      "safety": [
        "Always warm up for 5 minutes",
        "Stay hydrated throughout"
      ],
      "createdAt": "2024-03-31T10:30:00"
    }
  ]
}
```

## 🐛 Troubleshooting

### Kafka Connection Error
```bash
# Check if Kafka is running
docker ps | grep kafka

# Restart Kafka
docker restart <kafka-container-id>
```

### OpenAI API Error
```powershell
# Verify API key is set
echo $env:OPENAI_API_KEY

# Restart application after setting API key
```

### MongoDB Connection Error
```bash
# Check if MongoDB is running
docker ps | grep mongo

# Start MongoDB
docker run -d -p 27017:27017 mongo:latest
```

## 📖 Full Documentation

See `DOCUMENTATION.md` for comprehensive guides on:
- Spring AI integration
- Kafka consumer patterns
- Prompt engineering techniques
- Development guidelines
- Performance optimization

## 📁 Project Structure

```
AI-Service/
├── src/
│   ├── main/
│   │   ├── java/com/Fitness/AI/Service/
│   │   │   ├── controller/         # REST endpoints
│   │   │   ├── service/            # Business logic
│   │   │   ├── model/              # Domain entities
│   │   │   ├── repository/         # Data access
│   │   │   └── config/             # Configurations
│   │   └── resources/
│   │       ├── application.yaml    # Configuration
│   │       └── promttemplates/
│   │           └── userpromt.st    # AI character definition
│   └── test/                       # Unit tests
├── pom.xml                         # Maven dependencies
└── README.md
```

## 🎯 Next Steps

1. **Set up Kafka topic:**
   ```bash
   # Create topic in Kafka
   kafka-topics --create --topic activity-events --bootstrap-server localhost:9092
   ```

2. **Start Activities Service** to produce events

3. **Verify recommendations** are being generated and stored

4. **Customize prompt** in `userpromt.st` for different AI behavior

5. **Scale horizontally** by running multiple AI-Service instances with the same consumer group

## 🔐 Security Notes

- **Never commit API keys** - use environment variables
- **Use HTTPS** in production
- **Implement authentication** for API endpoints
- **Enable Kafka SSL/SASL** for production
- **Secure MongoDB** with authentication

## 📞 Support

For detailed information:
- Check `DOCUMENTATION.md` for comprehensive guide
- Review application logs: `tail -f target/spring-boot.log`
- Enable debug logging in `application.yaml`

---

**Version:** 1.0.0  
**Last Updated:** March 31, 2026  
**Status:** Production Ready

