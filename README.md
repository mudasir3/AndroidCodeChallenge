# 🎬 StarzPlay Video App Code Challenge

A multi-module Android application built with Kotlin and Jetpack Compose, designed to search and display TMDB media (movies and TV shows). The app showcases a clean architecture, immersive video playback, and robust unit testing.

## 📁 Project Structure
📦 starzplay_videoappcodechallenge
┣ 📂 app                → UI & presentation layer
┣ 📂 library            → Shared business logic, repository, and models
┣ build.gradle.kts      → Central build configuration
┣ settings.gradle.kts   → Module registration

This modular architecture ensures separation of concerns, reusability, and scalability.


## 🧱 Tech Stack
| Tool/Library                     | Usage                                 |
| -------------------------------- | ------------------------------------- |
| **Kotlin**                       | Programming language                  |
| **Jetpack Compose**              | Declarative UI                        |
| **Koin**                         | Dependency Injection                  |
| **Kotlinx Coroutines + Flow**    | Async data + reactive UI binding      |
| **TMDB API**                     | Media data source                     |
| **Coil**                         | Asynchronous image loading in Compose |
| **ExoPlayer (Media3)**           | Video playback                        |
| **Navigation Compose**           | Type-safe navigation                  |
| **Gson / Kotlinx Serialization** | Data serialization                    |
| **JUnit, Mockk**                 | Unit testing                          |


## 🧠 Architecture: MVI Pattern
Followed Model-View-Intent (MVI) using Jetpack Compose + Flow:

**Intent**: User-driven actions (e.g. search).

**Model**: ViewModel orchestrates logic & updates state.

**ViewState**: Represents UI state (loading, error, success).

**Effect**: One-time events (snackbar, navigation).

## 🖼 Screens Overview

**🔍 Main Screen**
- TextField for search input

- LazyColumn with horizontally scrollable groups (LazyRow) per media type

- Coil image loading

- Loading indicator

- Error message via one-time effects

**📄 Detail Screen (Optional/extendable)**
- Displays title, image, and metadata

- Navigation from item click

**▶️ Player Screen**
- Forces landscape orientation

- Hides system UI for fullscreen

- Shows image preview ,  loads video via ExoPlayer on play button click

- Video controls enabled


## ✅ Unit Testing
**Tools**:
- JUnit 4
- Mockk

**Covered**:
- MainViewModel tested for:

✅ Successful search (returns grouped results)

✅ Failed search (triggers error effect)


## 🧼 Best Practices Followed
- Multi-module architecture for better separation and testability

- Single source of truth for state management via StateFlow

- Immutable UI state and clear side effects

- Clean Compose UI design with previews and themes

- Dependency injection using Koin

- Proper test layering: domain + UI


## 👤 Author
Mudasir Mukhtar
Senior Android Engineer

### License
This project is licensed under the MIT License. See the LICENSE file for details.


