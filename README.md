# Insanet Reverse Auction Backend (Demo)

This repository contains the backend structure of a reverse auction system developed with Java Spring Boot.  
The full version includes Redis-based OTP authentication, PostgreSQL data management, and secure JWT-based login.

> This is a **demo version**. Sensitive credentials (e.g., database, email, tokens) have been anonymized or removed for security reasons.  
> The full implementation is maintained in a private repository and available upon request.

## Technologies Used
- Java 17
- Spring Boot
- Spring Security & JWT
- Spring Data JPA
- Redis (for OTP management)
- PostgreSQL
- Maven

## Structure
- `/auth`: login, register, password change, OTP logic  
- `/auction`: reverse auction endpoints, admin approvals  
- `/file`: file upload configuration  
- `/profile`: user info & profile management  
- `/config`: JWT, CORS, Redis configs

## Features
- JWT authentication & role-based access  
- OTP generation and verification via Redis  
- File upload support (tax certificate, trade registry)  
- Auction request creation and approval logic  
- Contractor and admin separation  
- Spring validation and exception handling

## Testing
JUnit & Mockito test structure is included (in progress).

## Notes
- `application.properties` is anonymized and tracked via .gitignore  
- This project is part of a full-stack implementation including a React frontend

---

### Feel free to explore and contact for detailed demo or code explanations.
