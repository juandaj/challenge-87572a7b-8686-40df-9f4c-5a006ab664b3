# Reto OWASP ASVS — Proyecto final corregido

Implementación final del reto **Estándares de seguridad**, acompañada de los entregables de las fases 1, 2 y 3.

## Qué contiene
- Aplicación Spring Boot 3 / Java 21.
- Spring Security moderno (`SecurityFilterChain`).
- Autenticación stateless con JWT.
- Secrets y passwords fuera del código.
- JWT corto con firma HS256, `issuer` y expiración validados.
- BCrypt para passwords demo.
- Autorización por roles USER/ADMIN.
- Manejo seguro de 401/403 y token inválido.
- Validación de input.
- Pruebas automatizadas de seguridad con MockMvc.
- Documentación ASVS, matriz de controles, ADR y vistas técnica/negocio.

## Estructura

```text
.
├── pom.xml
├── README.md
├── SECURITY.md
├── .env.example
├── requests.http
├── docs/
│   ├── 01_ASVS_RESUMEN.md
│   ├── 02_MATRIZ_MEJORAS_ASVS.md
│   ├── 03_ADR-001_MFA_STEP_UP.md
│   ├── 04_VISTA_TECNICA.md
│   ├── 05_VISTA_NEGOCIO.md
│   ├── 06_GUIA_ESTUDIO_CONCEPTOS.md
│   └── 07_EJECUCION_Y_EVIDENCIAS.md
└── src/
    ├── main/java/com/pragma/security/...
    └── test/java/com/pragma/security/SecurityApplicationTests.java
```

## Arranque rápido en IntelliJ
1. Abrir la carpeta como proyecto Maven.
2. Configurar JDK 21.
3. En **Run Configuration > Environment variables**, definir:
   - `JWT_SECRET_BASE64`
   - `APP_USER_PASSWORD`
   - `APP_ADMIN_PASSWORD`
4. Para generar `JWT_SECRET_BASE64`: `openssl rand -base64 32`.
5. Ejecutar `SecurityApplication`.
6. Ejecutar `SecurityApplicationTests` para evidencias.

Usuarios por defecto (solo nombres, no passwords):
- `challenge-user` → ROLE_USER
- `challenge-admin` → ROLE_USER + ROLE_ADMIN

## Flujo

```text
POST /api/auth/login
      |
      v
AuthenticationManager -> UserDetailsService -> BCrypt
      |
      v
JWT de corta duración
      |
      v
Authorization: Bearer <JWT>
      |
      v
JwtAuthenticationFilter
  - valida firma
  - valida issuer
  - valida exp
      |
      v
SecurityContext
      |
      +--> /api/secured/hello  USER/ADMIN
      +--> /api/secured/admin ADMIN
```

## Entregables del reto
- **Fase 0:** proyecto reconstruido y modernizado.
- **Fase 1:** `docs/01_ASVS_RESUMEN.md` y `docs/02_MATRIZ_MEJORAS_ASVS.md`.
- **Fase 2:** `docs/03_ADR-001_MFA_STEP_UP.md`.
- **Fase 3:** `docs/04_VISTA_TECNICA.md` y `docs/05_VISTA_NEGOCIO.md`.
- **Preparación para sustentación:** `docs/06_GUIA_ESTUDIO_CONCEPTOS.md`.
- **Evidencias y comandos:** `docs/07_EJECUCION_Y_EVIDENCIAS.md`.

## Alcance y producción
Este repositorio demuestra controles técnicos clave del reto. El usuario en memoria **no es** la recomendación de identidad para un e-commerce real. La decisión documentada para producción es integrar un Identity Provider OIDC, MFA/step-up, secret manager, TLS y controles anti-automatización.
