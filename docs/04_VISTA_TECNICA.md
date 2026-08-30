# Entregable Fase 3 — Vista técnica

## Objetivo de la vista
Que desarrollo, arquitectura, QA y seguridad sepan **dónde se aplican los controles**, cómo probarlos y qué debe sustituirse antes de producción.

```mermaid
flowchart LR
    C[Cliente] -->|POST /api/auth/login| A[AuthController]
    A --> AM[AuthenticationManager]
    AM --> UDS[UserDetailsService]
    UDS --> B[BCrypt]
    A -->|JWT HS256 corto| C

    C -->|Authorization: Bearer JWT| F[JwtAuthenticationFilter]
    F --> T[TokenUtils]
    T -->|firma + issuer + exp| F
    F --> SC[Spring SecurityContext]
    SC --> R{Autorización}
    R -->|USER o ADMIN| H[/api/secured/hello]
    R -->|solo ADMIN| AD[/api/secured/admin]

    ENV[Variables de entorno / Secret Manager] --> T
    ENV --> UDS
```

## Controles implementados
- API stateless: no se crea sesión HTTP de autenticación.
- JWT firmado con HS256 y clave >= 256 bits.
- Vida del access token: 900 s por defecto; configuración limitada a 60–3600 s.
- Validación de firma, `issuer` y expiración antes de autenticar.
- Contraseñas no incluidas en repositorio.
- BCrypt cost 12 para la demo en memoria.
- Autorización por roles en servidor con `@PreAuthorize`.
- Respuestas 401 y 403 diferenciadas sin detalles sensibles.
- Pruebas automatizadas de rutas públicas, autenticación y autorización.

## Responsabilidades antes de producción
1. Sustituir usuarios en memoria por IdP OIDC corporativo.
2. Implementar MFA/step-up según ADR-001.
3. Almacenar secretos en Vault/KMS/Secret Manager, con rotación.
4. Enforzar HTTPS/TLS desde edge hasta los componentes requeridos.
5. Implementar rate limiting/anti-automation para autenticación.
6. Centralizar auditoría y alertas de eventos de seguridad.
7. Evaluar audiencia (`aud`) y tipo de token en escenarios multi-servicio.

## Pruebas esperadas
- Sin token → `401` en `/api/secured/**`.
- JWT corrupto/expirado → `401`.
- USER → `200` en `/hello`, `403` en `/admin`.
- ADMIN → `200` en `/admin`.
- Endpoint público → `200` sin autenticación.
