# IB2026DavidCV - Gestión de Energía Iberdrola ⚡

![Android](https://img.shields.io/badge/Platform-Android-3DDC84.svg?style=flat&logo=android)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-7F52FF.svg?style=flat&logo=kotlin)
![Compose](https://img.shields.io/badge/UI-Jetpack_Compose-4285F4.svg?style=flat&logo=jetpackcompose)

**IB2026DavidCV** es una aplicación Android moderna desarrollada para la gestión de servicios de energía (Luz y Gas), facturación electrónica y administración de perfiles de usuario. La aplicación sigue las mejores prácticas de arquitectura de Google y utiliza las tecnologías más recientes del ecosistema Android.

## 🚀 Funcionalidades Principales

- **Gestión de Contratos**: Visualización detallada de contratos de luz y gas.
- **Facturación Electrónica**: Listado histórico de facturas con estados de pago (Pagada, Pendiente, Tramitada, etc.).
- **Filtros Avanzados**: Filtrado dinámico de facturas por fecha, rango de precios y estado.
- **Perfil de Usuario**: Edición de perfil con persistencia en DataStore y gestión de imagen de perfil local.
- **Remote Config**: Activación dinámica de servicios (Luz/Gas) desde Firebase en tiempo real.
- **Feedback de Usuario**: Sistema de encuestas (Opinion Bottom Sheet) basado en contadores de uso.

## 🛠️ Stack Tecnológico

- **Lenguaje**: [Kotlin](https://kotlinlang.org/)
- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Arquitectura declarativa).
- **Inyección de Dependencias**: [Hilt](https://dagger.dev/hilt/) (Dagger).
- **Arquitectura**: MVVM (Model-View-ViewModel) con Clean Architecture (Usecases).
- **Base de Datos**: [Room](https://developer.android.com/training/data-storage/room) para persistencia offline de facturas y contratos.
- **Persistencia de Preferencias**: [DataStore Preferences](https://developer.android.com/topic/libraries/architecture/datastore).
- **Red/JSON**: Retrofit (preparado) y GSON para el parseo de datos.
- **Imagen**: [Coil](https://coil-kt.github.io/coil/) para la carga eficiente de imágenes.
- **Animaciones**: [Lottie](https://airbnb.io/lottie/) para micro-interacciones.
- **Firebase**: 
    - Analytics (Seguimiento de eventos de navegación).
    - Remote Config (Configuración dinámica).
    - Crashlytics (Reporte de errores).

## 🏗️ Patrones de Diseño & Arquitectura

- **Unidirectional Data Flow (UDF)**: El estado de la UI se gestiona mediante `StateFlow` en el ViewModel, asegurando una única fuente de verdad y recomposiciones eficientes.
- **Repository Pattern**: Abstracción de las fuentes de datos (Local vs Remote) permitiendo un fácil intercambio entre datos de red y caché de Room.
- **SOLID Principles**: Uso de Casos de Uso (Interactors) específicos para cada acción del dominio, garantizando el principio de responsabilidad única.
- **Clean Architecture**: Clara separación entre las capas de Data, Domain y UI.

## 🧪 Estrategia de Testing

El proyecto cuenta con una cobertura integral dividida en:

### 1. Tests Unitarios (`src/test`)
- **ViewModels**: Validación de lógica de estados, filtrado y edición (`DataStoreViewModel`, `BillListViewModel`, etc.).
- **Use Cases**: Testeo de la lógica de negocio de forma aislada.
- **Mappers**: Verificación de la transformación correcta entre Entidades de BD y Modelos de Dominio.

### 2. Tests Instrumentales (`src/androidTest`)
- **DAOs**: Pruebas de CRUD sobre la base de datos Room usando una instancia en memoria.
- **DataStore**: Verificación de la persistencia real de la cuenta de usuario.
- **UI/Compose**: Tests de componentes y pantallas (`UserAccountScreen`, `ContractListScreen`).
- **Navegación E2E**: Flujos completos (Ej: Editar perfil -> Guardar -> Verificar cambio).

## 📊 Analytics & Monitorización

La aplicación utiliza un sistema de trazas para analizar el comportamiento del usuario:
- **Navigation Events**: Registro de flujos entre pantallas (`screen_navigation`) con parámetros de origen y destino.
- **Interaction Events**: Seguimiento de clics críticos (`RefreshContracts`, `ModifyEmail`, `SaveProfile`).
- **Real-time Configuration**: Implementación de `ConfigUpdateListener` para reaccionar a cambios en Remote Config sin necesidad de reiniciar la App.
- **Health Check**: Monitorización proactiva de crashes y ANRs mediante Firebase Crashlytics.

## 📁 Estructura del Proyecto

```text
com.iberdrola.practicas2026.davidcv/
├── data/               # Implementación de Repositorios, DAOs, Entidades y Mappers.
├── domain/             # Modelos de dominio, Interfaces de Repositorio y Casos de Uso.
├── ui/                 # Pantallas (Compose), ViewModels, Temas y Navegación.
│   ├── base/           # Componentes comunes y base.
│   ├── navigation/     # Grafo de navegación y Wrapper.
│   └── screens/        # Pantallas específicas por funcionalidad.
└── di/                 # Módulos de inyección de dependencias de Hilt.
```

## 🔧 Configuración & Requisitos

### Requisitos del Entorno
- **JDK**: 17
- **Android Studio**: Ladybug (2024.2.1) o superior.
- **Gradle**: 8.13+
- **Min SDK**: 29 (Android 10)
- **Target SDK**: 36 (Android 15)

### Pasos para Ejecución
1. Clonar el repositorio.
2. Añadir el archivo `google-services.json` en la carpeta `/app`.
3. Sincronizar Gradle.
4. Ejecutar en un dispositivo o emulador.

---
**Autor**: David CV  
**Proyecto**: IB2026 Energy Management
