# Slack Prototype - Real-Time Messaging System

A prototype for a **real-time messaging system**, inspired by Slack, built using **Kotlin**, **Spring WebFlux**, and **Reactive Programming**. This project demonstrates key concepts such as:

- Real-time WebSocket-based communication.
- Scalable pub/sub architecture with Redis.
- Durable message storage using MongoDB.
- Efficient membership and channel management with PostgreSQL.
- Adherence to Hexagonal Architecture principles for maintainable and extensible design.

---

## Features

- **Real-Time Messaging**:
  WebSocket-based real-time message broadcasting for channels and direct messages.

- **Message Persistence**:
  Store messages in MongoDB for retrieval and history.

- **Scalable Pub/Sub**:
  Redis used for channel-based messaging distribution.

- **Membership Management**:
  PostgreSQL for managing user memberships in channels with efficient querying.

- **Hexagonal Architecture**:
  Clean separation of concerns between application, domain, and infrastructure layers.

---

## Architecture Overview

### Layers and Responsibilities

1. **Application Layer**:
    - Manages the flow of data between the domain and infrastructure layers.
    - Handles WebSocket connections and orchestration of services.

2. **Domain Layer**:
    - Encapsulates business logic.
    - Defines abstractions for membership and message management.

3. **Infrastructure Layer**:
    - Handles persistence (MongoDB and PostgreSQL) and caching (Redis).
    - Implements low-level details like repositories and Redis pub/sub.

---

## Tech Stack

- **Language**: Kotlin
- **Web Framework**: Spring WebFlux
- **Database**: MongoDB (message storage), PostgreSQL (membership data)
- **Cache**: Redis (pub/sub for real-time messaging)
- **Reactive Programming**: Spring Data Reactive, Project Reactor
- **Docker**: Containerized environment for services
- **Asynchronous Handling**: Kotlin Coroutines for clean, non-blocking code.

---

## Asynchronous Programming with Kotlin Coroutines

This project extensively uses **Kotlin Coroutines** to handle asynchronous, non-blocking operations efficiently. Coroutines are a lightweight alternative to threads and integrate seamlessly with the **Reactor API** provided by Spring WebFlux.

### **Why Kotlin Coroutines?**

- **Non-Blocking**:
    - Coroutines allow writing asynchronous code in a sequential style without blocking threads.
    - Example: MongoDB queries, Redis operations, and WebSocket message broadcasting.

- **Lightweight**:
    - Unlike threads, coroutines consume fewer resources, enabling thousands of coroutines to run concurrently.

- **Integrated with Spring WebFlux**:
    - Kotlin Coroutines bridge the gap between the `Mono`/`Flux` APIs of Spring WebFlux and the suspendable functions of Kotlin.

---