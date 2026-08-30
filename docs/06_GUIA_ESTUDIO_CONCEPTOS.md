# Guía rápida de conceptos para defender el reto

## 1. OWASP ASVS
**Idea clave:** estándar de requisitos verificables de seguridad de aplicaciones.

**Pregunta típica:** “¿Para qué sirve?”  
**Respuesta:** para convertir seguridad en requisitos concretos que desarrollo puede implementar y QA/seguridad puede verificar; también sirve para definir criterios de aceptación y niveles de aseguramiento.

## 2. Autenticación vs autorización
- **Autenticación:** ¿quién eres?
- **Autorización:** ¿qué puedes hacer?

Ejemplo: tener un JWT válido autentica a `challenge-user`; no lo autoriza a `/api/secured/admin`.

## 3. JWT
JWT es un token autocontenido con claims. **Firmado no significa cifrado.** Un atacante puede leer normalmente el payload de un JWT firmado; la firma protege integridad/autenticidad, no confidencialidad.

Validaciones relevantes:
- firma/MAC;
- algoritmo esperado;
- expiración (`exp`);
- emisor (`iss`);
- audiencia (`aud`) cuando aplica;
- propósito/tipo del token en sistemas más complejos.

## 4. HS256
Es HMAC-SHA-256: el mismo secreto sirve para firmar y validar. Es apropiado cuando un mismo backend controla ambas operaciones y el secreto está bien protegido.

En microservicios, la firma asimétrica puede ser preferible: el emisor conserva la clave privada y los consumidores solo necesitan clave pública.

## 5. Secret management
Un secreto no debe estar en Git, código, imagen Docker ni archivo de configuración versionado. Variables de entorno son una mejora para el reto; en producción se prefiere un gestor de secretos con control de acceso, auditoría y rotación.

## 6. Hash de contraseñas
Las contraseñas no se “encriptan para luego desencriptarlas”; normalmente se almacenan mediante un hash/KDF lento diseñado para passwords, como Argon2id, scrypt o BCrypt según la plataforma y política.

En el reto usamos BCrypt. El valor `12` es el costo: aumenta el trabajo necesario tanto para validar como para intentar fuerza bruta.

## 7. Stateless
Con `SessionCreationPolicy.STATELESS` el backend no conserva una sesión HTTP para recordar al usuario. Cada request autenticado debe presentar su token.

Ventaja: facilita escalado horizontal.  
Costo: revocar inmediatamente tokens autocontenidos es más complejo; por eso se usan expiraciones cortas y estrategias adicionales.

## 8. 401 vs 403
- **401 Unauthorized:** falta autenticación válida o el token es inválido.
- **403 Forbidden:** la identidad ya es válida, pero no tiene permiso.

Ejemplo del reto:
- sin JWT a `/hello` → 401;
- USER válido a `/admin` → 403.

## 9. RBAC
Role-Based Access Control asigna permisos según roles, por ejemplo USER y ADMIN. Es simple y útil, pero sistemas complejos pueden requerir ABAC/policies basadas en atributos del usuario, recurso y contexto.

## 10. Dependency Injection
El filtro original construía `TokenUtils` internamente y dejaba `UserDetailsService` en `null`. Ahora las dependencias llegan por constructor.

Beneficios: menor acoplamiento, pruebas más fáciles, configuración centralizada y lifecycle gestionado por Spring.

## 11. CSRF
CSRF aprovecha credenciales que el navegador envía automáticamente, típicamente cookies. En esta API el cliente envía explícitamente un Bearer token en el header `Authorization`, por lo que se desactiva CSRF. Si la autenticación se migrara a cookies, habría que reevaluarlo.

## 12. MFA y step-up authentication
MFA combina factores independientes. Step-up significa pedir autenticación más fuerte cuando una operación o contexto eleva el riesgo.

Ejemplo: consultar catálogo no necesita el mismo nivel que cambiar contraseña, registrar un medio de pago o ejecutar una función administrativa.

## 13. ASVS vs OWASP Top 10
- **Top 10:** sensibilización/priorización de grandes riesgos.
- **ASVS:** catálogo de requisitos verificables para construir y evaluar controles.

Regla mental: **Top 10 = qué puede salir mal; ASVS = qué debo verificar que esté bien.**

## 14. Errores comunes al aplicar ASVS
- Tratarlo como checklist ciego sin threat model ni alcance.
- Decir “cumplimos ASVS” sin indicar versión y nivel.
- Confiar solo en scanners automáticos.
- Confundir autenticación con autorización.
- Guardar secretos en código porque “el repositorio es privado”.
- Usar JWT sin validar contexto, expiración o algoritmo.
- Corregir controles sin crear pruebas que prevengan regresiones.
