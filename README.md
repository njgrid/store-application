# Store Application 

This is a simple RESTful API for a store system that I built as a backend project. The idea was to cover all core e-commerce flows like user registration, login, product listing, cart management, and placing orders. Everything is built from scratch using Java and Spring Boot.

---

## What It Does ✅

- User can register and log in (passwords are stored securely)
- Products are listed with stock info and price
- Users can:
    - Add products to a cart
    - View and update their cart
    - Remove items if needed
- Checkout flow validates stock before creating the order
- Session-based cart – tied to login
- Also planned (optional):
    - Password reset
    - Cancel order
    - View all past orders

---

## Tech Stack 🛠️

- **Backend**: Java 17, Spring Boot
- **Database**: PostgreSQL
- **ORM**: Hibernate / JPA
- **Security**: Spring Security + BCrypt
- **Build Tool**: Gradle

---

## Running the Project 🚀

1. Clone this repo:
```bash
git clone https://github.com/your-username/store-app.git
cd store-app
