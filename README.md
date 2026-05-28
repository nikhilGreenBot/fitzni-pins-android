# ✨ FitzNi Pins

> **Named after my wife Katie Fitzsimmons + Nikhil(me) ** — a production-quality Android app for Disney trading pin collectors.

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0-purple?logo=kotlin)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-2024.06-green?logo=android)](https://developer.android.com/jetpack/compose)
[![Architecture](https://img.shields.io/badge/Architecture-Clean%20%2B%20UDF-blue)](https://developer.android.com/topic/architecture)
[![Hilt](https://img.shields.io/badge/DI-Hilt-orange)](https://dagger.dev/hilt/)
[![Room](https://img.shields.io/badge/DB-Room-teal)](https://developer.android.com/training/data-storage/room)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow)](LICENSE)

---

## 📱 What is FitzNi Pins?

FitzNi Pins is a **Disney trading pin collector app** built with modern Android practices. It helps collectors:

| Feature | Description |
|---------|-------------|
| 📦 **Catalog** | Manage a personal pin collection — photos, notes, wishlist, trade status |
| 🎉 **Pin-Tastic** | Browse official Disney Store drops (Pin-Tastic Tuesdays) with native UX |
| 🔍 **Identify** | Photograph unknown pins and get AI-assisted match candidates with confidence scores |
| 🏠 **Dashboard** | Next Pin-Tastic Tuesday countdown and quick navigation |

---

## 🏗️ Architecture

This app is a **showcase of production-quality Android architecture**, built following Google's recommended patterns:

```
┌─────────────────────────────────────────────────────────────┐
│  UI Layer (Jetpack Compose)                                  │
│  • Composables receive UiState, emit UiEvents               │
│  • collectAsStateWithLifecycle() for lifecycle-aware state   │
├─────────────────────────────────────────────────────────────┤
│  Presentation Layer (ViewModels)                             │
│  • StateFlow<UiState> + Channel<UiEffect>                   │
│  • SharingStarted.WhileSubscribed(5_000)                    │
│  • No Android framework in ViewModels                        │
├─────────────────────────────────────────────────────────────┤
│  Domain Layer (Pure Kotlin)                                  │
│  • Use Cases — one per user intent                          │
│  • Repository interfaces                                     │
│  • Sealed FitzNiResult<T> — no raw Throwable in UI         │
├─────────────────────────────────────────────────────────────┤
│  Data Layer                                                  │
│  • Room (local DB) + DataStore (prefs)                      │
│  • Retrofit + kotlinx.serialization (Phase B)               │
│  • DTOs → Domain models mapped at boundary                  │
└─────────────────────────────────────────────────────────────┘
```

### Why this matters for recruiters

- **No happy-path-only code** — every repository call returns `FitzNiResult<T>` (sealed), ViewModels map this to `UserFacingError` sealed class → explicit UI error states
- **Unidirectional data flow** — UiState in, UiEvent out; no side effects leaking into composables
- **Clean separation** — Composables are dumb; Domain has zero Android imports
- **Testable by design** — all use cases take repository interfaces; full Turbine + MockK unit tests

---

## 🛠️ Tech Stack

| Layer | Library | Notes |
|-------|---------|-------|
| Language | **Kotlin 2.0** (K2 compiler) | Latest stable |
| UI | **Jetpack Compose** + **Material 3** | Dynamic color, no XML screens |
| State | **Coroutines** + **StateFlow** | `viewModelScope`, structured concurrency |
| DI | **Hilt** | `@HiltViewModel`, `@Singleton` modules |
| Navigation | **Navigation Compose** | Typed routes, bottom nav, predictive back |
| Local DB | **Room** | Upserts, FTS search, schema export |
| Preferences | **DataStore** | Onboarding, sync timestamps |
| Networking | **Retrofit** + **OkHttp** | Phase B — backend integration |
| Serialization | **kotlinx.serialization** | No Gson |
| Images | **Coil 3** | Memory/disk cache, crossfade |
| Zoom | **Telephoto** | Pinch-to-zoom on product images |
| Splash | **AndroidX Core SplashScreen** | System splash + Compose animated logo |
| Logging | **Timber** | Debug only, no PII in release |
| Testing | **JUnit 5**, **Turbine**, **MockK** | StateFlow assertions, mock repositories |

---

## 🗂️ Package Structure

```
com.nikhilgreenbot.fitznipins
├── ui/
│   ├── screens/
│   │   ├── home/           # Dashboard with Pin-Tastic Tuesday countdown
│   │   ├── collection/     # Pin grid, search, CRUD
│   │   ├── pintastic/      # Official Disney drops, pinch-zoom, Custom Tab CTA
│   │   ├── identify/       # Camera → upload → confidence results
│   │   ├── profile/        # About, tech stack, settings
│   │   ├── splash/         # Animated FitzNi Pins logo
│   │   └── onboarding/     # 3-page animated walkthrough
│   ├── components/         # ErrorBanner, EmptyStateView, SkeletonBox
│   ├── theme/              # Material 3 + Disney-inspired color palette
│   └── navigation/         # NavGraph, Routes, BottomNavItems
├── presentation/
│   ├── collection/         # CollectionViewModel, UiState, UiEvent, UiEffect
│   ├── pintastic/          # PinTasticViewModel
│   ├── identify/           # IdentifyViewModel + IdentifyPhase
│   └── home/ profile/
├── domain/
│   ├── model/              # UserPin, OfficialProduct, IdentifyResult, FitzNiResult
│   ├── repository/         # PinRepository, CatalogRepository, IdentifyRepository
│   └── usecase/            # One use case per user intent (10 total)
└── data/
    ├── local/              # Room entities, DAOs, FitzNiDatabase
    ├── remote/             # DTOs, MockCatalogSeed (Phase B: Retrofit API)
    ├── repository/         # Concrete implementations (stale-while-revalidate)
    └── mapper/             # Entity ↔ Domain boundary mappers
```

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Jellyfish or later
- JDK 17+
- Android device / emulator (API 26+)

### Clone & Run

```bash
git clone https://github.com/nikhilGreenBot/fitzni-pins-android.git
cd fitzni-pins-android
./gradlew assembleDebug
```

Or open in Android Studio and press ▶️ Run.

### Build Variants
| Variant | Notes |
|---------|-------|
| `debug` | Timber logging, `.debug` suffix, no minification |
| `release` | R8 minification + shrinking enabled |

---

## 📐 State Management Pattern

Every screen follows this contract:

```kotlin
// UiState — immutable snapshot of what the screen shows
data class CollectionUiState(
    val isLoading: Boolean = false,
    val pins: List<UserPin> = emptyList(),
    val error: UserFacingError? = null,   // Sealed, not raw Throwable
)

// ViewModel exposes StateFlow — never mutable state to composables
val uiState: StateFlow<CollectionUiState>
    get() = _uiState.asStateFlow()

// Side effects travel through a Channel (consumed exactly once)
val effects: Flow<CollectionEffect>
```

---

## 🔒 Legal & Data Notes

- **No Disney scraping** — the Android client never scrapes Disney Store or PinPics
- Pin-Tastic catalog is served from **your own backend** (mock data in v1)
- **Custom Tab** integration for purchases → always the real Disney Store page
- PinPics identification is a planned **own-API** feature (see spec §10); no data replication
- All screens include appropriate disclaimers per spec requirements

---

## 🗺️ Roadmap

| Phase | Status | Scope |
|-------|--------|-------|
| **A** | ✅ Scaffolded | Navigation shell, Room CRUD, Splash + Onboarding |
| **B** | 🔜 Next | Backend API, real catalog refresh, Retrofit wiring |
| **C** | 🔜 Later | CameraX live preview, real identify upload, multipart |
| **D** | 🔜 Polish | Pagination, export, analytics hooks, performance pass |

---

## 🧪 Testing

```bash
# Unit tests (JUnit 5 + Turbine + MockK)
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest
```

Tests cover: ViewModel state transitions, Flow emissions via Turbine, `FitzNiResult` error paths.

---

## 👨‍💻 Author

**Nikhil** ([@nikhilGreenBot](https://github.com/nikhilGreenBot))

Built with ❤️ for wife Katie Fitzsimmons — the best Disney pin collector in the world.

---

*FitzNi Pins is not affiliated with The Walt Disney Company.*
*Product data and purchases are handled on shopDisney.com.*
