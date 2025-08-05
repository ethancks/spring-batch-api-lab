# Spring Batch File Processor & RESTful API

This is a Spring Boot project developed for a technical assessment. It processes a transaction data file using Spring Batch and exposes a RESTful API to retrieve and update transaction records.

---

## ^_^ Features

- ✅ **Spring Batch Job** to read a `.txt` file and store transactions into a database
- ✅ **RESTful API** to:
  - Retrieve transaction records with pagination and filtering
  - Update transaction description with **concurrency control**
- ✅ **H2 In-Memory Database** for development/testing
- ✅ **Unit Tests** with JUnit and Mockito

---

## 📁 File Structure
src
└── main
├── java
│ └── com.job.lab.transaction
│ ├── batch # Spring Batch job configs
│ ├── controller # REST API controllers
│ ├── model # Entity classes
│ ├── repository # Spring Data JPA interfaces
│ ├── service # Business logic
│ └── config # App and batch configurations
└── resources
├── application.yml
└── data/input/Assessment_Data_Source.txt

---

## Tech Stack

- Java 17
- Spring Boot 3.x
- Spring Batch
- Spring Data JPA
- H2 Database
- Lombok
- JUnit 5 + Mockito

---

## How to Run

1. Clone the repo:
   ```bash
   git clone https://github.com/ethancks/spring-batch-api-lab.git
   cd spring-batch-api-lab.git
2. Open in your IDE
3. Run the Spring Boot application:
   ./mvnw spring-boot:run
4. Access H2 Console:
   http://localhost:8080/h2-console

---
## API Endpoints (to be added)
GET /transactions: Retrieve records (supports pagination and filtering)

PUT /transactions/{id}: Update description with concurrency control

---
## Diagrams
✅ Class Diagram — [link or embedded image]
✅ Activity Diagram — [link or embedded image]

---
## ✍️ Author
Ethan Chen

---
## 📄 License
This project is for educational and assessment purposes only.
