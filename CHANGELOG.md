# Changelog

Todos los cambios notables en este proyecto serán documentados en este archivo.

## [1.1.0]

### Añadido
- **Suite de Testing Integral**:
    - Tests Unitarios para todos los ViewModels (`DataStoreViewModel`, `BillListViewModel`, `BillViewModel`, `ContractListViewModel`).
    - Tests Unitarios para los 8 Casos de Uso del dominio.
    - Tests Unitarios para los Mappers de datos (`ContractMapper`, `BillMapper`).
    - Tests Instrumentales para DAOs de Room y DataStoreManager.
    - Tests de UI (Compose) para `UserAccountScreen` y `ContractListScreen`.
    - Tests de Navegación End-to-End (E2E) usando Hilt.
- **Real-time Remote Config**: Implementación de `ConfigUpdateListener` para actualizaciones instantáneas de la interfaz sin reinicio.
- **Analíticas Avanzadas**: Trazas de navegación con parámetros de origen/destino y seguimiento de clics.
- **Validación de Formularios**: Lógica de validación reactiva en tiempo real para la edición de perfil.

### Cambiado
- **Refactorización MVVM/UDF**: Migración de estados mutables locales a un flujo de datos unidireccional (UDF) centralizado en el ViewModel.
- **Modularización de UI**: Descomposición de la pantalla `EditProfileScreen` en componentes más pequeños y mantenibles (`ProfileImageHeader`, `EditFields`, `SaveButton`).
- **Arquitectura de Estado**: Refactorización de `UserAccountState` a un modelo de datos inmutable.

### Solucionado
- **Error de Dependencias**: Corregido el fallo de resolución de `com.google.firebase:firebase-config-ktx:23.0.1`.
- **Configuración de Hilt**: Solucionados los problemas de inyección de dependencias en el entorno de pruebas instrumentales.

---

