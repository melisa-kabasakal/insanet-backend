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
- Sensitive configuration values are externalized using environment variables for security reasons.
- This project is part of a full-stack implementation including a React frontend

---

### Feel free to explore and contact for detailed demo or code explanations.

----

## Frontend Migration Note

The frontend of this project was originally developed with Vanilla JavaScript.  
It was later fully refactored and migrated to React by the same developer to improve scalability, state management, and user experience.  
The React version includes Context API, protected routes, and modular structure.

> The frontend repository is private due to business requirements but can be demonstrated upon request.

