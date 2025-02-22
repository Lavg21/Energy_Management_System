# Energy Management System

> A distributed system for managing smart energy metering devices, users, and real-time monitoring through microservices and WebSockets.

## 🚀 Overview

This project is an **Energy Management System** that enables administrators to manage users and devices while allowing clients to monitor their smart energy consumption. It consists of multiple microservices built with **Spring Boot**, a frontend developed in **Angular**, and a real-time messaging system powered by **RabbitMQ** and **WebSockets**.

## 🛠️ Features

✅ **User Authentication & Role-Based Access**  
✅ **CRUD Operations on Users & Devices**  
✅ **Microservices Communication via RabbitMQ**  
✅ **Smart Metering Device Simulator**  
✅ **Real-time Chat between Users & Admins**  
✅ **Deployment with Docker**  

## 📜 Table of Contents

- [Architecture](#architecture)
- [Installation](#installation)
- [Usage](#usage)
- [Technologies Used](#technologies-used)

## 🏗 Architecture

The system follows a **microservices architecture**, consisting of:

- **User Microservice**: Handles authentication, user management, and role-based access.
- **Device Microservice**: Manages devices and their associations with users.
- **Monitoring Microservice**: Receives energy consumption data from a simulator via RabbitMQ.
- **Chat Microservice**: Enables real-time communication between admins and clients using WebSockets.
- **Frontend**: Built with Angular for an intuitive UI.
- **Database**: MySQL with Hibernate ORM.
- **Dockerized Deployment**: Uses Docker Compose for seamless containerized execution.

## 💻 Installation

### Prerequisites
- Java 17+
- Node.js & Angular CLI
- Docker & Docker Compose
- RabbitMQ
- MySQL

## 🎮 Usage

### Administrator Actions
- Manage users and assign roles
- Add, edit, and delete devices
- View energy consumption statistics
- Chat with clients

### Client Actions
- View personal devices and their energy consumption
- Communicate with the administrator in real-time

## ⚡ Technologies Used

- **Backend**: Java, Spring Boot, Hibernate, JWT Authentication
- **Frontend**: Angular, TypeScript
- **Messaging**: RabbitMQ
- **Database**: MySQL
- **Deployment**: Docker, Docker Compose
- **Real-time Communication**: WebSockets
