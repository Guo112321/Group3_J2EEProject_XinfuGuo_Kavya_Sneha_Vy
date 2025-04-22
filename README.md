# 🛒 The Convenience System
**J2EE Business Components - ITE-5432-IRA**

## 📌 Project Overview
**The Convenience System** is a smart hub solution focused on creating a streamlined and scalable convenience store experience.
This phase involves the development of a fully functional MVP featuring secure authentication, product management, user dashboards, and payment handling.

---

## 👥 Team Members
| Name        | Student ID   | Contribution                          |
|-------------|--------------|----------------------------------------|
| Sneha Jose  | N01635957    | Login, Signup, Home Pages             |
| Kavya Gali  | N01622088    | User Dashboard (Search & Display), MyCart |
| Xinfu Guo   | N01611988    | Payment Integration, Invoices Page    |
| Vy Ly       | N01600569    | Admin Dashboard                        |

---

## 🎯 Objective
To build a secure, scalable, and testable MVP with key frontend and backend features, providing a solid foundation for deployment and real-world testing.

---

## ✨ Features

### 1. User Authentication & Role-Based Access
- Secure registration and login via **Spring Security**
- Role-based access control: **Customer** and **Store Owner**

### 2. Store Inventory Management
- CRUD operations on products by store owners
- RESTful APIs using **Spring Boot**, **JPA**, and **MySQL**

### 3. Product Browsing & Search
- Search products by name, category, or price
- Implemented using **Spring MVC** and **JPA Queries**

### 4. Shopping Cart & Checkout
- Users can add products to the cart and proceed to payment
- Payment and invoice generation handled securely

### 5. Admin Dashboard
- Admin interface for managing users, roles, and system-level tasks

---

## 🔗 Application Routes

| Page           | Route                                                                         |
|----------------|-------------------------------------------------------------------------------|
| 🏠 Home Page    | [http://localhost:8081/](http://localhost:8081/)                              |
| 🔑 User Login   | [http://localhost:8081/user/login](http://localhost:8081/user/login)          |
| 📝 User Register| [http://localhost:8081/user/register](http://localhost:8081/user/register)    |
| 🛠️ Admin Login | Same as User Login – use admin credentials    (username-admin,password-admin) |

---


## 📦 Models Overview

### 🛍️ Product Model
| Field           | Data Type | Description                        |
|------------------|-----------|------------------------------------|
| `id`             | Long      | Primary key                        |
| `name`           | String    | Product name                       |
| `category`       | String    | Product category (e.g., beverages) |
| `price`          | double    | Must be a positive value           |
| `available`      | boolean   | Availability status                |
| `storeLocation`  | String    | Store’s location                   |

### 👤 User Model
| Field        | Data Type | Description                             |
|--------------|-----------|-----------------------------------------|
| `id`         | Long      | Primary key                             |
| `username`   | String    | Unique username                         |
| `password`   | String    | Encrypted password                      |
| `email`      | String    | User’s email                            |
| `enabled`    | boolean   | Indicates if the account is active      |
| `roles`      | Set<Role> | Assigned roles (Customer/Admin/Owner)   |

### 🛡️ Role Entity
| Field   | Data Type | Description            |
|---------|-----------|------------------------|
| `id`    | Long      | Primary key            |
| `name`  | String    | Role name (e.g., Admin)|

---

## 🛠️ Tech Stack

| Layer          | Technology                        |
|----------------|------------------------------------|
| Backend        | Spring Boot, Spring MVC, Spring Security, JPA |
| Frontend       | Thymeleaf                         |
| Database       | MySQL                             |
 
