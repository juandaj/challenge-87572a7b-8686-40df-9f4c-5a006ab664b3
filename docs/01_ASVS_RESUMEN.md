# Entregable Fase 1 — Resumen OWASP ASVS 5.0.0

## ¿Qué es OWASP ASVS?
OWASP Application Security Verification Standard (ASVS) es un estándar abierto que define requisitos verificables para diseñar, construir y evaluar controles técnicos de seguridad en aplicaciones y servicios web. Sirve como lista de requisitos, criterio de pruebas y lenguaje común entre desarrollo, arquitectura, seguridad, QA y negocio.

La versión usada en este reto es **OWASP ASVS 5.0.0**.

## Estructura general
ASVS 5.0 organiza aproximadamente 350 requisitos en 17 capítulos:

1. V1 Encoding and Sanitization
2. V2 Validation and Business Logic
3. V3 Web Frontend Security
4. V4 API and Web Service
5. V5 File Handling
6. V6 Authentication
7. V7 Session Management
8. V8 Authorization
9. V9 Self-contained Tokens
10. V10 OAuth and OIDC
11. V11 Cryptography
12. V12 Secure Communication
13. V13 Configuration
14. V14 Data Protection
15. V15 Secure Coding and Architecture
16. V16 Security Logging and Error Handling
17. V17 WebRTC

No todos los capítulos aplican a todos los sistemas. Para este reto, los de mayor relevancia son V6, V7, V8, V9, V11, V12, V13 y V16.

## Niveles de verificación
- **L1:** controles fundamentales; apropiado como base mínima.
- **L2:** mayor aseguramiento para aplicaciones que manejan información sensible o transacciones importantes; es el objetivo recomendado para muchas aplicaciones de negocio.
- **L3:** alto aseguramiento para sistemas con amenazas o consecuencias especialmente críticas.

El nivel no significa “cantidad de seguridad”; significa **profundidad y rigor de verificación**.

## ¿Cómo usar ASVS en un proyecto?
1. Determinar alcance, activos y nivel objetivo.
2. Filtrar capítulos/requisitos aplicables.
3. Convertir requisitos ASVS en criterios de aceptación técnicos.
4. Diseñar e implementar controles.
5. Crear pruebas automáticas y manuales que demuestren cumplimiento.
6. Registrar excepciones, riesgos aceptados y controles compensatorios.
7. Revalidar ante cambios relevantes de arquitectura o versión del estándar.

## Requisitos especialmente críticos para esta aplicación

### 1. Gestión segura de secretos — V13.3.1 (L2)
Las claves de firma y contraseñas no deben formar parte del código fuente ni de los artefactos de build. En producción deben residir en un secret manager o vault.

**Aplicación en este proyecto:** el secreto JWT y las contraseñas se reciben por variables de entorno; `.env` está ignorado por Git.

### 2. Integridad y validez de JWT — V9.1.1, V9.1.2, V9.1.3 y V9.2.1
Antes de confiar en un JWT se debe validar su firma/MAC, aceptar únicamente algoritmos esperados, obtener la clave de una fuente confiable y respetar el período de validez.

**Aplicación en este proyecto:** se usa una clave HMAC >= 256 bits, HS256 fijado por la aplicación, validación de firma, `issuer` y expiración.

### 3. Autorización explícita — V8.2.1 y V8.3.1
Autenticar no basta. Cada operación sensible debe comprobar permisos en un componente confiable del servidor.

**Aplicación en este proyecto:** `/api/secured/admin` exige `ROLE_ADMIN` mediante `@PreAuthorize` en backend.

### 4. Password hashing — V11.4.2 (L2)
Las contraseñas almacenadas deben usar una función de derivación/hash diseñada para passwords con costo computacional adecuado.

**Aplicación en este proyecto:** los usuarios demo se cargan en memoria y sus contraseñas son codificadas con BCrypt cost 12. En un sistema real se migraría a un IdP o repositorio persistente.

### 5. MFA para objetivo L2 — V6.3.3 (L2)
ASVS 5.0 adopta una postura fuerte: para L2 debe utilizarse MFA o una combinación aceptable de mecanismos; cualquier relajación requiere una justificación documentada y controles mitigantes.

**Aplicación en este reto:** se documenta como ADR la decisión de requerir MFA/step-up para operaciones sensibles en producción, dejando claro que el login local del proyecto es una simplificación didáctica.

## Error conceptual común
**OWASP Top 10 != OWASP ASVS.**
- Top 10: comunica familias frecuentes de riesgo.
- ASVS: define requisitos concretos y verificables que permiten diseñar y probar controles.
