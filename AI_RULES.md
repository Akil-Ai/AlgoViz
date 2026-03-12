# AI Rules & Tech Stack for AlgoViz

## Tech Stack
- **Language**: Kotlin 2.x with Coroutines and Flow for reactive programming.
- **UI Framework**: Jetpack Compose (Material 3) for all UI components and screens.
- **Architecture**: MVVM (Model-View-ViewModel) combined with Clean Architecture (Domain/Data/UI layers).
- **Dependency Injection**: Hilt (Dagger) for managing component lifecycles and dependency provisioning.
- **Backend (BaaS)**: Supabase for Authentication, PostgreSQL (Postgrest), Realtime subscriptions, and Object Storage.
- **Local Persistence**: Room Database for offline caching and progress tracking.
- **Navigation**: Jetpack Navigation Compose for type-safe routing between screens.
- **AI Integration**: Sarvam AI API for multilingual tutoring, voice (TTS/STT), and translation.
- **Code Execution**: Judge0 API for sandboxed execution of user-submitted code.
- **Image Loading**: Coil for efficient, asynchronous image loading.

## Library Usage Rules
- **UI Components**: Always use Material 3 components. Custom visualizations must be implemented using `androidx.compose.ui.graphics.Canvas` or Compose Animation APIs.
- **Networking**: Use the official Supabase Kotlin SDK for all backend interactions. Use Retrofit 2 with OkHttp 4 for external REST APIs like Sarvam AI and Judge0.
- **Serialization**: Use `kotlinx.serialization` for all JSON parsing and data transfer objects (DTOs).
- **Icons**: Use `androidx.compose.material.icons` (Material Icons) for all UI iconography.
- **Asynchronous Work**: Use Kotlin Coroutines for background tasks and `StateFlow`/`SharedFlow` for exposing data to the UI.
- **Dependency Injection**: All ViewModels, Repositories, and UseCases must be Hilt-injected. Avoid manual instantiation of these components.
- **Testing**: Use JUnit 5 for unit tests, Mockk for mocking dependencies, and Compose UI Test for integration/UI testing.
- **Error Handling**: Prefer `Result<T>` wrappers for repository and use case returns to handle success and failure states explicitly.