# Music-Fan-Portal

**Music-Fan-Portal** is a web application developed by a 4-member team as an engineering project at Wrocław University of Science and Technology. It serves as a social platform for music fans, comprising four main modules: shop, chat, blog, and forum.

My contributions were centered around the forum backend and frontend, as well as the blog frontend and configuration.

## Technology Stack

### Deployment
- Docker

### Backend
- Java 11 (Eclipse Temurin)
- Spring Boot
- Spring Data
- Spring Cloud
- Spring Security
- Keycloak
- Stripe
- PostgreSQL
- RSQL
- Kafka

### Frontend
- TypeScript
- React
- React Router
- ChakraUI
- FramerMotion
- ChatEngine
- Sanity


## Architecture Overview

### Microservices Architecture

The system is designed with a microservices architecture, consisting of four main components responsible for:
- Managing products in the shop
- Handling orders
- Managing the forum
- Handling the notification system

These microservices register with the Eureka service, which is responsible for load balancing and service discovery. This setup allows multiple instances of the same microservice to run simultaneously, enhancing the application's performance.

### Spring Cloud Gateway

Access to the web interface is only possible through the native Spring Cloud Gateway solution. Most requests to the application, especially those modifying the state of databases, require an OAuth token generated by Keycloak, which stores user data and manages access.

### Frontend

The website is built using the React library, with components styled using the Chakra UI library. The blog is managed by Sanity headless CMS, a developer platform for structuring content in web applications. Sanity provides an API interface and supports technologies like JavaScript, REST, and GraphQL, allowing for structured information management with high data limits, enabling free operation.

### Chat

The chat functionality is powered by Chatengine, a free solution that supports direct and group communication among users.

### State Management and Storage

The internal state of the application is managed with Redux and session storage. Images within the application, including user-uploaded content, are stored in Firebase Cloud Storage. This technology allows for the storage and management of various user-generated multimedia content, ensuring efficiency, durability, and accessibility from anywhere.

### User Activity Analysis

User activity analysis and recommendation of products are handled by Recombee. Recombee uses real-time data to provide accurate content and product recommendations for each user, leveraging machine learning algorithms like collaborative filtering and text mining.

### Deployment

Microservices are deployed within a Docker network, orchestrated using a docker-compose file. The decision to replace a single large database with four smaller ones is justified by faster queries and improved performance. This approach also increases the independence of microservices, reducing interdependencies between application components.

### Database Design

All databases are relational, ensuring efficient memory management and avoiding undesirable data redundancy. Detailed searching, filtering, and sorting of products in the shop are achieved using high-level SQL queries. Relational databases also support concurrency, coordinating and processing multiple user operations simultaneously.

### Data Abstraction

Product attributes are divided into an abstract table containing common features for all categories and specific tables for different product types. This structure minimizes column repetition and allows for abstract queries that operate across all product types simultaneously.

## License

Unauthorized use or reproduction of any part of this project is strictly prohibited.
