# 📦 PokeVault

## 🇪🇸 Español

PokeVault es una aplicación Pokédex para Android moderna, de alto rendimiento y con múltiples funcionalidades, construida desde cero siguiendo las mejores prácticas del desarrollo Android moderno, **Jetpack Compose**, **Clean Architecture** y **Dagger Hilt**.

---

### 🎯 Intención y propósito de la aplicación

PokeVault está diseñada para ofrecer una experiencia de exploración Pokémon fluida, veloz y agradable. Ya seas un fan casual o un entusiasta dedicado de Pokémon, PokeVault te permite:
- **Explorar** cientos de Pokémon con desplazamiento infinito que nunca se traba ni se corta.
- **Buscar y filtrar** al instante por nombre, ID o tipo elemental.
- **Inspeccionar** estadísticas detalladas, atributos físicos y artwork oficial en alta resolución.
- **Cambiar de idioma** dinámicamente entre español e inglés para todas las descripciones de los Pokémon.

---

### 🛠️ Stack técnico y arquitectura

PokeVault sigue los principios de **Clean Architecture** y aprovecha las últimas librerías estables de Android Jetpack:

- **UI y diseño:**
  - [Jetpack Compose](https://developer.android.com/jetpack/compose) y [Material 3](https://m3.material.io/)
  - Optimizaciones de desplazamiento para pantallas de alta tasa de refresco (120Hz/144Hz) (claves de elemento estables en `LazyColumn`)
  - Icono de aplicación adaptativo vectorial personalizado (diseño de Pokéball clásica)
- **Inyección de dependencias:**
  - [Dagger Hilt](https://dagger.dev/hilt/)
- **Asincronía y gestión de estado:**
  - Kotlin Coroutines y `StateFlow`
  - `ViewModel` del ciclo de vida y `SavedStateHandle`
- **Redes:**
  - [Retrofit](https://square.github.io/retrofit/) y [Gson](https://github.com/google/gson) consumiendo la [PokeAPI](https://pokeapi.co/)
- **Carga de imágenes:**
  - [Coil](https://coil-kt.github.io/coil/) con caché en memoria/disco y animaciones crossfade

---

### 🔌 Descripción de la API

PokeVault consume la [PokeAPI](https://pokeapi.co/), una API REST pública y gratuita que expone información completa sobre el mundo Pokémon. La aplicación utiliza los siguientes endpoints, todos con el método HTTP **GET** y respuestas en formato **JSON** deserializadas con Gson:

- **`GET /pokemon`** — Lista paginada de Pokémon. Acepta los parámetros de consulta `limit` (cantidad de resultados) y `offset` (desplazamiento). Devuelve un objeto con una lista `results` de recursos (`name` y `url`).
- **`GET /pokemon/{idOrName}`** — Detalle de un Pokémon por ID o nombre. Sin parámetros de consulta. Devuelve los datos físicos, tipos, habilidades y estadísticas base del Pokémon.
- **`GET /type/{typeName}`** — Pokémon filtrados por tipo elemental (por ejemplo, `fire`, `water`, `grass`). El nombre del tipo se envía como parámetro de ruta. Devuelve una lista de Pokémon que pertenecen a ese tipo.
- **`GET /pokemon-species/{idOrName}`** — Información de la especie por ID o nombre. Sin parámetros de consulta. Devuelve, entre otros datos, las entradas de texto descriptivas (`flavor_text_entries`) en distintos idiomas.

Los parámetros se envían mediante `@Query` (paginación) y `@Path` (identificadores y nombres) en la interfaz Retrofit; la URL base es `https://pokeapi.co/api/v2/` y todas las respuestas se mapean a DTOs tipados.

---

### ✨ Funcionalidades principales

1. **Desplazamiento infinito totalmente fluido:**
   - Precarga y paginación en segundo plano sin indicadores de carga inferiores ni saltos de diseño, garantizando una experiencia de desplazamiento fluida al estilo iOS.
2. **Búsqueda y filtrado robustos:**
   - Búsqueda parcial y exacta por nombre (por ejemplo, buscar "pika" coincide con Pikachu, Pichu, etc.), búsqueda por ID y un filtro por tipo interactivo desplegable que admite nombres de tipos en español (Fuego, Agua, Planta, etc.).
3. **Descripciones bilingües (ES / EN):**
   - Pantalla de detalle con un conmutador de idioma interactivo (`ES` / `EN`) para leer al instante los textos de los Pokémon en español o inglés.
4. **Estadísticas y artwork detallados:**
   - Artwork oficial en alta resolución, atributos físicos (altura/peso) y barras de progreso visuales para HP, Ataque, Defensa y Velocidad.

---

### 🚀 Primeros pasos

1. Clona el repositorio:
   ```bash
   git clone https://github.com/your-username/PokeVault.git
   ```
2. Abre el proyecto en [Android Studio](https://developer.android.com/studio).
3. Sincroniza el proyecto con los archivos de Gradle.
4. Ejecuta la aplicación en un emulador o dispositivo físico Android (API 28+).

---

## 🇬🇧 English

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

## 🔌 API Description

PokeVault consumes the [PokeAPI](https://pokeapi.co/), a free public REST API that exposes complete information about the Pokémon world. The application uses the following endpoints, all with the **GET** HTTP method and **JSON** responses deserialized with Gson:

- **`GET /pokemon`** — Paginated Pokémon list. Accepts the query parameters `limit` (number of results) and `offset` (offset). Returns an object with a `results` list of resources (`name` and `url`).
- **`GET /pokemon/{idOrName}`** — Pokémon detail by ID or name. No query parameters. Returns the Pokémon's physical data, types, abilities, and base stats.
- **`GET /type/{typeName}`** — Pokémon filtered by elemental type (for example, `fire`, `water`, `grass`). The type name is sent as a path parameter. Returns a list of Pokémon belonging to that type.
- **`GET /pokemon-species/{idOrName}`** — Species information by ID or name. No query parameters. Returns, among other data, the descriptive flavor text entries (`flavor_text_entries`) in different languages.

Parameters are sent via `@Query` (pagination) and `@Path` (identifiers and names) in the Retrofit interface; the base URL is `https://pokeapi.co/api/v2/` and all responses are mapped to typed DTOs.

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
