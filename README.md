RAG Chat Microservice
A Spring Boot–based microservice that manages chat sessions and chat messages, designed for Retrieval-Augmented Generation (RAG)–style conversational systems.

The service supports both user-associated and user-agnostic chat sessions by allowing sessions to optionally include a user identifier, without enforcing user authentication. This enables flexible use cases such as anonymous chats, AI demos, internal system conversations, and integration with external identity providers.

🚀 Tech Stack
- Java 21
- Spring Boot 3.x
- Spring Web / Spring Data JPA
- MySQL (configurable)
- Docker & Docker Compose
- Swagger / OpenAPI (springdoc)
- API Key Security & Rate Limiting
📂 Project Structure
RAGChatMicroservice
├── src/main/java/com/example/RAGChatMicroservice
│   ├── controller        # REST controllers
│   ├── service           # Business logic
│   ├── repository        # JPA repositories
│   ├── entity            # JPA entities
│   ├── dto               # Request / Response DTOs
│   ├── security          # API key & rate limiting filters
│   ├── exception         # Global exception handling
│   ├── config            # Application & Swagger configs
│   ├── constants         # API path constants
│   ├── properties        # Custom property mappings
│   └── util              # Utility classes
├── docker-compose.yml
├── Dockerfile
├── .env
└── pom.xml
⚙️ Setup & Running Instructions
Prerequisites
- Java 21
- Maven 3.8+
- Docker & Docker Compose (optional)
Environment Configuration
Create a `.env` file based on `.env.example`:

DB_URL=jdbc:mysql://localhost:3306/rag_chat
DB_USERNAME=root
DB_PASSWORD=root
API_KEY=your-api-key
Run Locally
mvn clean install
mvn spring-boot:run

Application URL:
http://localhost:9091
🔑 User Handling
This service is designed to work in two modes:

1. User-Specific Mode
   - userId is provided in the request.
   - Sessions and messages are associated with a specific user.
   - Suitable for authenticated applications (e.g., dashboards, portals).

2. Generic / Anonymous Mode
   - userId is optional.
   - If omitted, sessions are created without user binding.
   - Suitable for:
     - AI demos
     - Internal tools
     - System-generated conversations
     - Anonymous chat use cases
📡 API Overview
Base URL:
/v1/vp/sessions
Chat Session APIs
- Create Chat Session (POST /v1/vp/sessions/create-session)
{
  "name": "My Chat Session",
  "userId": "opt1"    // optional
}
- Rename Chat Session (PUT /v1/vp/sessions/{sessionId}/rename)
- Get All Sessions (GET /v1/vp/sessions/get-all-sessions)
- Mark / Unmark Favorite Session (PUT /v1/vp/sessions/{sessionId}/favorite)
- Delete Session (DELETE /v1/vp/sessions/{sessionId})
Chat Message APIs
- Add Message to Session (POST /v1/vp/sessions/{sessionId}/add-messages)
{
  "sender": "AI",
  "content": "A single user can have multiple independent chat sessions.",
  "context": "session_2_chat",
  "userId": "opt1"
}
- Get Messages by Session (Paginated) (GET /v1/vp/sessions/{sessionId}/get-messages?page=0&size=5)
🔒 Security
- All APIs are secured using API Key Authentication.
- API key must be passed via header:
X-API-KEY: your-api-key
- Rate limiting is enforced using servlet filters.
📖 API Documentation (Swagger)

Interactive API documentation is available via Swagger UI and provides details of all 
request/response models and endpoints.

Swagger UI:
http://localhost:9091/swagger-ui/index.html


🩺 Health Check

The service exposes health check endpoints to support monitoring and container orchestration.

Health Endpoint:
GET /actuator/health

Example:
http://localhost:9091/actuator/health

This endpoint can be used by Docker, Kubernetes, or monitoring tools to verify service 
availability.


⚠️ Error Handling
- Centralized global exception handling.
- Consistent API response structure.
- Proper HTTP status codes (400, 404, 500, etc.).
🌟 Bonus Features
- Health check endpoints
- Swagger/OpenAPI documentation
- Dockerized database management tool (pgAdmin/Adminer)
- Basic unit tests for services/business logic
- CORS configuration for security
- Pagination support for chat messages

🚀 Quick Start

Follow the steps below to get the application running on your local machine.

Prerequisites
• Java 21

• Maven

• MySQL (running locally)

• Git

📦 Installation & Setup
Step 1: Clone the Repository
Clone the repository and move into the project directory:

git clone https://github.com/SmithIrfan/rag-chat-storage-microservice.git

cd RAGChatMicroservice

Step 2: Create Database
Create an empty database in MySQL.
Example:

CREATE DATABASE <your_db_name>;

⚠️ Only the database is created manually.
Tables will be created automatically by the application.

Step 3: Configure Environment Variables (.env)
Create a .env file in the project root directory.
Add ONLY sensitive values:

DB_URL=jdbc:mysql://localhost:3306/<your_db_name>

DB_USERNAME=your_db_username

DB_PASSWORD=your_db_password

API_KEY=your-api-key

Notes:
• Database name can be changed here

• .env is NOT responsible for table creation

Step 4: Update Application Properties (IMPORTANT)

Open application.properties 

Locate the following property:

spring.jpa.hibernate.ddl-auto=none

👉 Change it to:

spring.jpa.hibernate.ddl-auto=update

Why this change is required:
• Ensures tables are created automatically if they do not exist
• Updates schema when entity changes occur
• No manual SQL needed for table creation

Step 5: Run the Application
Start the Spring Boot application using Maven:

mvn spring-boot:run

📌Design Decisions Beyond Case Study Requirements

The original case study required storing chat sessions and messages. This implementation extends the design by making the userId field optional, allowing sessions to be either associated with an external user reference or remain user-agnostic.

This enables:

Sessions linked to an external user identifier managed by an upstream system.
Anonymous or system-initiated sessions for demos, internal tools, or AI-driven conversations.
This choice increases flexibility and makes the microservice usable across multiple business contexts without redesigning the API.
