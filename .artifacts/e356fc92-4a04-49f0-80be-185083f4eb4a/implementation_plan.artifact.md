# Implementation Plan: StockFlow CMP App Architecture & Login

Build a professional Compose Multiplatform (CMP) application for the StockFlow inventory system, following a robust layered architecture with Koin DI, Ktor networking, and a modern Teal-themed design.

## User Review Required

> [!IMPORTANT]
> - **Dependency Injection:** I'll be using **Koin** for DI as it's the standard for Compose Multiplatform.
> - **Network:** **Ktor** will be used for API calls with Kotlinx Serialization.
> - **Platform Storage:** **SecureStorage** will be implemented using platform-specific APIs (EncryptedSharedPreferences for Android, Keychain for iOS, and a file-based encrypted storage for Desktop).
> - **Navigation:** **Jetpack Navigation Compose** (Multiplatform version) will be used for screen transitions.

## Proposed Changes

### 1. Infrastructure & Dependencies

#### [MODIFY] [libs.versions.toml](file:///D:/Programming/CMP/StockFlow/gradle/libs.versions.toml)
- Add Koin, Ktor, Navigation, and Serialization versions and libraries.

#### [MODIFY] [build.gradle.kts](file:///D:/Programming/CMP/StockFlow/shared/build.gradle.kts)
- Apply Kotlin Serialization plugin.
- Add dependencies to `commonMain`, `androidMain`, `iosMain`, and `jvmMain`.

---

### 2. Design System Refinement

#### [MODIFY] [Color.kt](file:///D:/Programming/CMP/StockFlow/shared/src/commonMain/kotlin/org/sujan/stockflow/ui/theme/Color.kt)
- Update primary colors to **Teal** (#0D9488) as per the screenshots.
- Add status colors: `Pending` (Orange), `Approved` (Blue), `Shipped` (Green), `Rejected` (Red), `Admin` (Purple).

#### [MODIFY] [CustomButton.kt](file:///D:/Programming/CMP/StockFlow/shared/src/commonMain/kotlin/org/sujan/stockflow/ui/components/CustomButton.kt) & [CustomTextField.kt](file:///D:/Programming/CMP/StockFlow/shared/src/commonMain/kotlin/org/sujan/stockflow/ui/components/CustomTextField.kt)
- Adjust styling to match the sharp, clean cards and teal highlights in the screenshots.

---

### 3. Domain Layer

#### [NEW] Data Models
- Create `User`, `Role`, `Product`, `Order`, `OrderItem`, and `OrderStatus` in `domain/model/`.

#### [NEW] Repository Interfaces
- Define `AuthRepository`, `ProductRepository`, and `OrderRepository` in `domain/repository/`.

#### [NEW] Use Cases
- Implement `LoginUseCase` in `domain/usecase/`.

---

### 4. Data Layer

#### [NEW] Remote Data Sources
- `HttpClientFactory.kt`: Setup Ktor client with Logging and Serialization.
- `AuthApi.kt`: Ktor-based implementation for auth endpoints.
- `LoginRequestDto.kt` & `AuthResponseDto.kt`: DTOs matching the Spring Boot backend contracts.

#### [NEW] Local Storage
- `TokenStorage.kt`: Multiplatform interface for JWT persistence.
- Platform-specific `SecureStorage` implementations in `androidMain`, `iosMain`, and `desktopMain`.

#### [NEW] Repository Implementations
- `AuthRepositoryImpl.kt` mapping DTOs to Domain models.

---

### 5. Dependency Injection & Navigation

#### [NEW] DI Modules
- `NetworkModule.kt`, `RepositoryModule.kt`, `ViewModelModule.kt` in `di/`.

#### [NEW] Navigation
- `AppNavHost.kt`: Define routes for Login, Admin Dashboard, Manager Dashboard, and Staff Dashboard.

---

### 6. Presentation Layer: Login

#### [MODIFY] [LoginScreen.kt](file:///D:/Programming/CMP/StockFlow/shared/src/commonMain/kotlin/org/sujan/stockflow/presentation/auth/LoginScreen.kt)
- Update layout to match the provided screenshot:
    - Teal "Log in" button.
    - "Demo quick-login" section with Admin, Manager, and Staff role-specific buttons.
    - Clean card-based login form.

#### [NEW] [LoginViewModel.kt](file:///D:/Programming/CMP/StockFlow/shared/src/commonMain/kotlin/org/sujan/stockflow/presentation/auth/LoginViewModel.kt)
- Handle login state, API calls via UseCase, and navigation events.

---

## Verification Plan

### Automated Tests
- **Unit Tests:** Test `LoginUseCase` with a mocked repository.
- **DI Test:** Verify Koin module configuration.

### Manual Verification
- **Login Flow:** Verify that clicking "Log in" triggers the API call and navigates to the dashboard on success.
- **Quick Login:** Verify the role-specific buttons pre-fill or trigger direct logins as intended for the demo.
- **Visual Check:** Compare the rendered app on Android and Desktop against the provided Figma screenshots.
