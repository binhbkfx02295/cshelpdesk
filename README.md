# CS Help Desk 🎯

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

CS Help Desk is a comprehensive customer support system developed for Thiên An Phú Company, which specializes in distributing men's health supplements. The system automates ticket creation from Facebook messages, manages customer service operations, and provides performance analytics for support teams.

## 📋 Table of Contents

- [Features](#-features)
- [Technology Stack](#-technology-stack)
- [Prerequisites](#-prerequisites)
- [Installation](#-installation)
- [Configuration](#-configuration)
- [Running the Application](#-running-the-application)
- [Project Structure](#-project-structure)
- [API Documentation](#-api-documentation)
- [Key Modules](#-key-modules)
- [Development](#-development)
- [Testing](#-testing)
- [Deployment](#-deployment)
- [Contributing](#-contributing)
- [Authors](#-authors)
- [License](#-license)

## ✨ Features

- **Facebook Integration**: Webhook integration to receive messages and automatically create support tickets
- **User Management**: Manage Facebook users and internal staff members
- **Ticket Management**: 
  - Create, update, and track support tickets
  - Add notes and hashtags to tickets
  - Track ticket status and progress
- **Customer Satisfaction Tracking**: Monitor customer emotions and satisfaction levels
- **Role-Based Access Control**: Permission management based on user groups and roles
- **Dashboard Interface**: Real-time monitoring and coordination dashboard
- **Excel Export**: Export ticket data to Excel format for reporting
- **Real-time Updates**: WebSocket support for live updates
- **OpenAI Integration**: GPT-powered ticket analysis and assistance
- **Internationalization**: Multi-language support (Vietnamese/English)

## 🛠 Technology Stack

### Backend
- **Framework**: Spring Boot 3.4.4
- **Language**: Java 17
- **Security**: Spring Security
- **ORM**: Spring Data JPA with Hibernate
- **Database**: MySQL 8.0
- **API Documentation**: SpringDoc OpenAPI (Swagger)
- **WebSocket**: Spring WebSocket for real-time features

### Frontend
- **Template Engine**: Thymeleaf
- **CSS Framework**: Bootstrap 5
- **Styling**: SCSS
- **JavaScript**: jQuery
- **UI Components**: Custom single-page application components

### Additional Tools
- **Build Tool**: Maven
- **Development**: Spring DevTools, Lombok
- **Excel Processing**: Apache POI
- **Data Generation**: JavaFaker (for testing)
- **External APIs**: Facebook Graph API

### Deployment
- **Containerization**: Docker
- **Cloud Platform**: AWS (EC2, RDS)
- **CI/CD**: Jenkins
- **Orchestration**: Docker Compose

## 📦 Prerequisites

Before you begin, ensure you have the following installed:

- **Java Development Kit (JDK) 17** or higher
- **Maven 3.8+** (or use the included Maven Wrapper)
- **MySQL 8.0+** (or use Docker)
- **Docker & Docker Compose** (optional, for containerized deployment)
- **Git** for version control
- **Facebook Page Access Token** (for Facebook integration)

## 🚀 Installation

### 1. Clone the Repository

```bash
git clone https://github.com/binhbkfx02295/cshelpdesk.git
cd cshelpdesk
```

### 2. Set Up Database

**Option A: Using MySQL directly**
```sql
CREATE DATABASE cshelpdesk;
CREATE USER 'cshelpdesk_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON cshelpdesk.* TO 'cshelpdesk_user'@'localhost';
FLUSH PRIVILEGES;
```

**Option B: Using Docker**
```bash
docker run --name cshelpdesk-mysql \
  -e MYSQL_ROOT_PASSWORD=root_password \
  -e MYSQL_DATABASE=cshelpdesk \
  -e MYSQL_USER=cshelpdesk_user \
  -e MYSQL_PASSWORD=your_password \
  -p 3306:3306 \
  -d mysql:8.0
```

### 3. Install Dependencies

```bash
./mvnw clean install
```

## ⚙️ Configuration

### Application Properties

Create or update `src/main/resources/application.properties`:

> **Important**: The example below is for reference only. For development, create your own `application.properties` file with your actual credentials. Ensure `application.properties` is in `.gitignore` to prevent accidental credential commits. Consider creating an `application.properties.example` file with placeholder values for team reference.

```properties
# Application
spring.application.name=cshelpdesk

# Database Configuration
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/cshelpdesk?createDatabaseIfNotExist=true
spring.datasource.username=cshelpdesk_user
spring.datasource.password=your_password

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# Timezone
spring.jackson.time-zone=Asia/Ho_Chi_Minh
spring.jackson.serialization.write-dates-as-timestamps=true

# Facebook API Configuration
# WARNING: Never commit real credentials to version control!
# Use environment variables or secret management systems in production
facebook.api.app-id=YOUR_FACEBOOK_APP_ID
facebook.api.app-secret=YOUR_FACEBOOK_APP_SECRET
facebook.api.page-id=YOUR_FACEBOOK_PAGE_ID
facebook.api.default-short-token=YOUR_FACEBOOK_ACCESS_TOKEN
spring.application.facebook-page-api=YOUR_FACEBOOK_PAGE_ACCESS_TOKEN

# Thymeleaf
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
spring.thymeleaf.cache=false
spring.thymeleaf.encoding=UTF-8
spring.thymeleaf.mode=HTML

# Session
server.servlet.session.timeout=30m

# Internationalization
spring.messages.basename=messages
spring.messages.encoding=UTF-8
spring.mvc.locale=vi
spring.mvc.locale-resolver=fixed

# Logging
logging.level.root=INFO
logging.file.name=logs/app.log
logging.file.max-size=10MB
logging.file.total-size-cap=100MB
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
```

> **Security Note**: Never commit sensitive credentials to version control. Always use environment variables or secure secret management systems for production deployments.

### Environment Variables (Recommended for Production)

For production deployments, use environment variables:

```bash
export SPRING_DATASOURCE_URL=jdbc:mysql://your-db-host:3306/cshelpdesk
export SPRING_DATASOURCE_USERNAME=your_username
export SPRING_DATASOURCE_PASSWORD=your_password
export FACEBOOK_API_APP_ID=your_app_id
export FACEBOOK_API_APP_SECRET=your_app_secret
export FACEBOOK_API_PAGE_ID=your_page_id
export FACEBOOK_API_ACCESS_TOKEN=your_access_token
```

## 🏃 Running the Application

### Development Mode

**Using Maven Wrapper:**
```bash
./mvnw spring-boot:run
```

**Using Maven:**
```bash
mvn spring-boot:run
```

**Using JAR:**
```bash
./mvnw clean package
java -jar target/cshelpdesk-0.0.1-SNAPSHOT.jar
```

### Production Mode

**Using Docker Compose:**
```bash
docker-compose up --build -d
```

**Access the Application:**
- **Main Application**: http://localhost:8080
- **API Documentation**: http://localhost:8080/swagger-ui/index.html
- **Health Check**: http://localhost:8080/actuator/health

## 📂 Project Structure

```
cshelpdesk/
├── src/
│   ├── main/
│   │   ├── java/com/binhbkfx02295/cshelpdesk/
│   │   │   ├── CshelpdeskApplication.java          # Main application entry point
│   │   │   ├── ticket_management/                  # Ticket management module
│   │   │   │   ├── note/                          # Ticket notes
│   │   │   │   ├── progress_status/               # Ticket status tracking
│   │   │   │   └── tag/                           # Ticket hashtags/tags
│   │   │   ├── facebookuser/                       # Facebook user management
│   │   │   │   ├── entity/                        # User entities
│   │   │   │   ├── repository/                    # Data access layer
│   │   │   │   ├── service/                       # Business logic
│   │   │   │   ├── controller/                    # REST controllers
│   │   │   │   └── dto/                           # Data transfer objects
│   │   │   ├── employee_management/                # Employee management
│   │   │   │   └── employee/                      # Employee CRUD operations
│   │   │   ├── webhook/                            # Facebook webhook handlers
│   │   │   ├── openai/                             # OpenAI/GPT integration
│   │   │   │   ├── adapter/                       # GPT model adapters
│   │   │   │   ├── service/                       # AI services
│   │   │   │   └── common/                        # Prompt builders
│   │   │   └── infrastructure/                     # Infrastructure layer
│   │   │       ├── security/                      # Security configuration
│   │   │       │   ├── auth/                     # Authentication handlers
│   │   │       │   ├── config/                   # Security config
│   │   │       │   └── filter/                   # Security filters
│   │   │       ├── common/                        # Common utilities
│   │   │       │   ├── cache/                    # Master data caching
│   │   │       │   ├── seed/                     # Database seeders
│   │   │       │   └── config/                   # App configuration
│   │   │       ├── controller/                    # Main controllers
│   │   │       └── util/                          # Utility classes
│   │   └── resources/
│   │       ├── application.properties              # Application configuration
│   │       ├── messages.properties                 # Default messages
│   │       ├── messages_vi.properties              # Vietnamese translations
│   │       ├── static/                             # Static resources
│   │       │   ├── css/                           # Stylesheets
│   │       │   ├── js/                            # JavaScript files
│   │       │   ├── img/                           # Images
│   │       │   └── audio/                         # Audio files
│   │       └── templates/                          # Thymeleaf templates
│   └── test/                                       # Test files
├── .mvn/                                           # Maven wrapper files
├── mvnw                                            # Maven wrapper script (Unix)
├── mvnw.cmd                                        # Maven wrapper script (Windows)
├── pom.xml                                         # Maven project configuration
├── .gitignore                                      # Git ignore rules
└── README.md                                       # This file
```

## 📚 API Documentation

The application uses SpringDoc OpenAPI for API documentation.

**Access Swagger UI:**
```
http://localhost:8080/swagger-ui/index.html
```

**Access OpenAPI Specification:**
```
http://localhost:8080/v3/api-docs
```

### Key API Endpoints

- **Authentication**
  - `POST /login` - User login
  - `POST /logout` - User logout

- **Tickets**
  - `GET /api/tickets` - List all tickets
  - `POST /api/tickets` - Create a new ticket
  - `GET /api/tickets/{id}` - Get ticket details
  - `PUT /api/tickets/{id}` - Update ticket
  - `DELETE /api/tickets/{id}` - Delete ticket

- **Facebook Users**
  - `GET /api/facebook-users` - List Facebook users
  - `GET /api/facebook-users/{id}` - Get user details

- **Webhook**
  - `GET /webhook` - Facebook webhook verification
  - `POST /webhook` - Receive Facebook messages

## 🔑 Key Modules

### 1. Ticket Management
- Create and manage support tickets
- Track ticket status and progress
- Add notes and tags to tickets
- Monitor customer emotions and satisfaction

### 2. Facebook User Management
- Store and manage Facebook user information
- Link Facebook users to support tickets
- Track user interaction history

### 3. Employee Management
- Manage internal staff accounts
- Define user roles and permissions
- Track employee performance

### 4. Webhook Integration
- Receive Facebook messages in real-time
- Automatically create tickets from messages
- Send responses back to Facebook

### 5. Security
- Session-based authentication
- Role-based access control (RBAC)
- Secure API endpoints
- Password encryption

### 6. Master Data Cache
- Cache frequently accessed data
- Improve application performance
- Status, emotion, and satisfaction level lookups

## 💻 Development

### Code Style
- Follow Java coding conventions
- Use Lombok for reducing boilerplate code
- Maintain clean and readable code

### Hot Reload
The application includes Spring DevTools for automatic restart during development.

### Database Migrations
The application uses Hibernate with `ddl-auto=update` for automatic schema updates. For production, consider using a migration tool like Flyway or Liquibase.

## 🧪 Testing

### Run Tests

```bash
./mvnw test
```

### Test Coverage

```bash
./mvnw verify
```

The project includes:
- Unit tests with JUnit
- Integration tests
- Mock testing with Mockito

## 🚢 Deployment

### Docker Deployment

1. **Build the Docker image:**
```bash
docker build -t cshelpdesk:latest .
```

2. **Run with Docker Compose:**
```bash
docker-compose up -d
```

### AWS Deployment

The application is designed to run on AWS infrastructure:
- **EC2**: Application server
- **RDS**: MySQL database
- **Security Groups**: Network configuration

### Environment-Specific Configuration

Use Spring profiles for different environments:
- `application-dev.properties` - Development
- `application-staging.properties` - Staging
- `application-prod.properties` - Production

Run with specific profile:
```bash
java -jar target/cshelpdesk-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Code Standards
- Write clean, maintainable code
- Add appropriate comments
- Include unit tests for new features
- Update documentation as needed

## 👥 Authors

**Capstone Project – CS Help Desk Development Team**

- Project Supervisor: Thầy Phạm Đức Thắng
- Lead Developer: binhbkfx02295
- Organization: Thiên An Phú Company

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Facebook for the Graph API
- All contributors and team members
- Thiên An Phú Company for project support

## 📞 Support

For support and questions:
- Create an issue in the GitHub repository
- Contact the development team

---

**Built with ❤️ using Spring Boot**
