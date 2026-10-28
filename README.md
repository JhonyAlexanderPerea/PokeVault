# 📦 PokeVault

PokeVault is a modern, high-performance, and feature-rich Pokédex Android application built from the ground up using modern Android development best practices, **Jetpack Compose**, **Clean Architecture**, and **Dagger Hilt**.

---

## 🎯 Application Intent & Purpose

PokeVault is designed to offer a seamless, lightning-fast, and delightful Pokémon exploration experience. Whether you are a casual fan or a dedicated Pokémon enthusiast, PokeVault allows you to:
- **Explore** hundreds of Pokémon with infinite scrolling that never stutters or cuts.
- **Search & Filter** instantly by name, ID, or elemental type.
- **Inspect** detailed stats, physical attributes, and official high-res artwork.
- **Switch Languages** dynamically between Spanish and English for all Pokémon descriptions.

---

## 🛠️ Technical Stack & Architecture

PokeVault follows **Clean Architecture** principles and leverages the latest stable Android Jetpack libraries:

- **UI & Design:** 
  - [Jetpack Compose](https://developer.android.com/jetpack/compose) & [Material 3](https://m3.material.io/)
  - High refresh rate (120Hz/144Hz) scroll optimizations (`LazyColumn` stable item keys)
  - Custom vector adaptive app icon (Classic Pokéball design)
- **Dependency Injection:** 
  - [Dagger Hilt](https://dagger.dev/hilt/)
- **Asynchronous & State Management:** 
  - Kotlin Coroutines & `StateFlow`
  - Lifecycle `ViewModel` & `SavedStateHandle`
- **Networking:** 
  - [Retrofit](https://square.github.io/retrofit/) & [Gson](https://github.com/google/gson) consuming the [PokeAPI](https://pokeapi.co/)
- **Image Loading:** 
  - [Coil](https://coil-kt.github.io/coil/) with memory/disk caching and crossfade animations

---

## ✨ Key Features

1. **Butter-Smooth Infinite Scroll:** 
   - Pre-fetching and background pagination without bottom loading spinners or layout jumps, ensuring an iOS-like fluid scrolling experience.
2. **Robust Search & Filtering:** 
   - Partial and exact name search (e.g. searching "pika" matches Pikachu, Pichu, etc.), ID lookup, and an interactive dropdown type filter supporting Spanish type names (Fuego, Agua, Planta, etc.).
3. **Bilingual Descriptions (ES / EN):** 
   - Detail screen featuring an interactive language toggle (`ES` / `EN`) to instantly read Pokémon flavor texts in Spanish or English.
4. **Detailed Stats & Artwork:** 
   - High-resolution official artwork, physical attributes (height/weight), and visual progress bars for HP, Attack, Defense, and Speed.

---

## 🚀 Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/PokeVault.git
   ```
2. Open the project in [Android Studio](https://developer.android.com/studio).
3. Sync project with Gradle files.
4. Run the app on an Android emulator or physical device (API 28+).
