# Build y Despliegue del Cliente Móvil

## 1. Requisitos

- Android Studio (recomendado, con SDK Platform 37).
- JDK 11+ (el módulo compila con `source/targetCompatibility = JavaVersion.VERSION_11`).
- Dispositivo o emulador con API 24+ (`minSdk = 24`, `targetSdk = 36`).
- Backend en ejecución y accesible desde el dispositivo (ver sección 3).
- Permiso `INTERNET` ya declarado en `AndroidManifest.xml`.

---

# 2. Compilación

Desde la raíz del repo, el módulo móvil es un proyecto Gradle independiente (`iubconsultas/settings.gradle.kts`, `rootProject.name = "iubconsultas"`):

```bash
cd iubconsultas
./gradlew assembleDebug
```

El APK queda en `iubconsultas/app/build/outputs/apk/debug/`. Para desarrollo, instalar directamente con *Run* desde Android Studio.

---

# 3. Conectar con el backend

La app no descubre el servidor: usa `BASE_URL` de `RetrofitClient.kt`. Elegir según el caso:

| Caso | Constante a usar en `BASE_URL` |
|------|-------------------------------|
| Emulador Android | `SERVER_EMULATOR` (`http://10.0.2.2:8080/`) |
| Dispositivo físico por ADB | `SERVER_ADB` (`http://localhost:8080/`) |
| Dispositivo físico en Wi-Fi | `SERVER_WIFI` (`http://192.168.1.6:8080/`, valor actual) |

Pasos:

1. Levantar el backend (`./gradlew bootRun` en la raíz, puerto 8080).
2. Verificar acceso desde el dispositivo (navegador a `http://<host>:8080/swagger-ui/index.html` o a la `BASE_URL` elegida).
3. Ajustar `BASE_URL` en `RetrofitClient.kt` si hace falta y recompilar.

---

# 4. Probar el flujo mínimo

1. Abrir la app (destino `login`).
2. Registrarse como Estudiante o Docente (`register` → vuelve a `login`).
3. Iniciar sesión y comprobar que navega al home del rol.
4. Como Estudiante: crear una solicitud en `consultations`.
5. Como Docente/Admin: cambiar su estado y verificar que el historial se refleja.
6. Forzar un 401 (p. ej. detener el backend con sesión iniciada) y comprobar que `SessionWatcher` redirige al login.

---

# 5. Limitaciones conocidas

- `android:usesCleartextTraffic="true"` permite HTTP solo para desarrollo local. En producción: servir la API por HTTPS y retirar ese atributo.
- La sesión vive solo en memoria (`SessionManager`): reiniciar la app obliga a nuevo login.
- `BASE_URL` está hardcodeada a una IP de Wi-Fi concreta (`192.168.1.6`); cambiar de red exige editar y recompilar (mejora: `BuildConfig` por entorno).
- El módulo no se dockeriza (es un APK, no un servicio): la dockerización del proyecto cubre API + cliente web Angular, no esta app.

---

# 6. Mejoras futuras

- Sabores `dev`/`prod` con URL y flags por entorno.
- Persistencia de sesión con DataStore + refresh silencioso.
- CI que compile `assembleDebug` y ejecute tests en cada PR.
