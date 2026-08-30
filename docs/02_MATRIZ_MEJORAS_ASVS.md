# Matriz de mejoras y trazabilidad ASVS

| Problema original | Riesgo | Mejora aplicada | Concepto | ASVS relacionado |
|---|---|---|---|---|
| Secreto JWT embebido directamente en código | Exposición de credenciales y falsificación de tokens | `JWT_SECRET_BASE64` obligatorio por entorno; mínimo 256 bits | Secret management | V13.3.1, V13.3.2; V11.2.3 |
| Contraseña por defecto embebida en `application.yml` | Credencial predecible/default | Contraseñas obligatorias por entorno | Secure configuration | V6.3.2, V13.3.1 |
| Token de 10 días | Ventana de abuso muy amplia ante robo | 15 min por defecto; máximo 1 hora validado | Session/token lifetime | V9.2.1; V7 Session Management |
| `userDetailsService = null` | Fallo de autenticación / NPE | Inyección de dependencias real por constructor | Dependency Injection / testability | Secure coding; soporte a V6/V8 |
| Filtro parseaba JWT sin manejo de errores | Excepciones y comportamiento inseguro | Captura controlada de JWT inválido y respuesta 401 genérica | Fail securely | V9.1.1; V16 error handling |
| Configuración Spring Security antigua | API deprecada y difícil de mantener | `SecurityFilterChain` moderno, sin `WebSecurityConfigurerAdapter` | Security by framework defaults | V15 secure architecture |
| Estado de sesión no definido | Posible mezcla de sesión servidor + JWT | `SessionCreationPolicy.STATELESS` | Stateless authentication | V7 / V9 |
| Solo autenticación; sin privilegios diferenciados | Broken Access Control | Roles USER/ADMIN y `@PreAuthorize` | Authentication vs Authorization / RBAC | V8.2.1, V8.3.1 |
| JWT sin `issuer` | Token válido criptográficamente pero fuera de contexto | Se emite y exige `issuer` | Token context | V9.2.x |
| Algoritmo no claramente restringido por diseño | Algorithm confusion / aceptación no deseada | Emisión/verificación con HS256 y una sola familia simétrica | Algorithm allowlist | V9.1.2 |
| Login sin DTO validado | Entradas vacías o excesivas | Bean Validation `@NotBlank` + límites | Input validation | V2 / V4 |
| Errores podrían filtrar detalles | Information leakage | stack trace/message desactivados y 401/403 genéricos | Secure error handling | V13.4, V16 |
| No había pruebas de seguridad | Regresiones de controles | MockMvc: 200 público, 401 anónimo, 403 USER→ADMIN, 200 ADMIN, JWT inválido | Security verification | propósito central de ASVS |

## Nota sobre CSRF
CSRF se desactiva conscientemente porque la API usa un token Bearer enviado explícitamente en `Authorization` y no una cookie de sesión que el navegador adjunte automáticamente. Si el diseño cambiara a cookies de autenticación, esta decisión tendría que revisarse y habilitar controles CSRF adecuados.

## Nota sobre HS256
HS256 es apropiado para esta aplicación monolítica de demostración si el secreto tiene suficiente entropía y permanece exclusivamente en el servidor. En una arquitectura distribuida, suele ser preferible firma asimétrica (RS256/ES256/EdDSA, según estándares y soporte) para que los consumidores verifiquen con clave pública sin poseer la clave privada de firma.
