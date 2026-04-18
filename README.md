# Computer Inventory System

Web application for managing computers and their components.

---

## Functionality

### Authentication
- Registration and login
- Passwords are stored securely (hashed)
- Automatic login after registration

---

### User data
- Each user works only with their own data
- Computers and components are linked to a specific user

---

### Computers
- Add, edit, delete computers
- View list of computers

---

### Components
- Add, edit, delete components
- Assign components to computers
- Remove component from computer
- Transfer component between computers

---

### Search
Search components by:
- Model
- Inventory number
- Manufacturer
- Type
- Status (FREE / ASSIGNED / IN REPAIR)
- Computer

---

### File operations
- Export data to JSON file
- Import data from JSON file

---

## Technologies

- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL (Azure)
- Thymeleaf
- HTML / CSS / JavaScript

---

## Deployment

Application is available at:

https://computer-inventory-a6exevb3dde0htcn.italynorth-01.azurewebsites.net

---

## Notes

- Passwords are hashed using BCrypt
- Users cannot access other users' data
- Only free components can be assigned

---

## About file handling

In Java, operator overloading is not supported.  
Therefore, file operations are implemented using JSON serialization.

---

## Author

Olya Pliatsko