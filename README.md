## wall-social

A minimal social media app built using **Jetpack Compose** and **Firebase**, where users can sign in with Google, post short text updates, and view posts in real-time.

---

##  Features

- 🔐 Google Sign-In authentication
- 📝 Post short text updates
- 📆 Real-time syncing using Firestore
- 👤 Filter to show only logged-in user’s posts
- 🔄 Sort posts by timestamp (ascending)
- 🧩 Smooth UI with Compose animations
- ⚙️ Scalable MVVM architecture

---

##  Tech Used

- **Jetpack Compose** – Modern UI toolkit
- **Firebase Firestore** – Real-time NoSQL database
- **Firebase Auth (Google Sign-In)** – Secure user login
- **Hilt** – Dependency Injection
- **Kotlin Coroutines + Flow** – Reactive data handling
- **Jetpack Navigation** – Screen navigation


---

##  Unit Testing

- Unit test for AddPostUseCase

---

##  Architecture

- Follows **MVVM (Model-View-ViewModel)** pattern combined with a **UseCase** layer for clean separation of concerns. Each layer adheres to the **SOLID principles** for maintainability and scalability.

---

##  Layers Overview
- **Model**: Domain models
- **View**: Composables (UI screens & components)
- **ViewModel**: Handles state and events
- **UseCases**: Encapsulate individual pieces of business logic (Single Responsibility)
- **Repository**: Abstraction between domain and data sources (Dependency Inversion)

