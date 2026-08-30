# Entrega final del reto

## Fase 0 — Proyecto funcional y corregido
Código reconstruido con Spring Boot, Spring Security y Java 21. Se sustituyó la configuración de seguridad obsoleta, se corrigió la inyección de dependencias y se implementó un flujo de autenticación/autorización demostrable.

## Fase 1 — Exploración ASVS
- `docs/01_ASVS_RESUMEN.md`
- `docs/02_MATRIZ_MEJORAS_ASVS.md`

Incluyen definición, estructura, niveles, uso del estándar, requisitos críticos y trazabilidad de cada corrección del código.

## Fase 2 — Decisión controvertida
- `docs/03_ADR-001_MFA_STEP_UP.md`

Decisión: objetivo de producción con MFA y autenticación reforzada (step-up) para operaciones sensibles, sustentada en seguridad, UX, fraude y costo operativo.

## Fase 3 — Comunicación
- `docs/04_VISTA_TECNICA.md`
- `docs/05_VISTA_NEGOCIO.md`

La primera muestra componentes, flujo y controles; la segunda traduce la decisión a impacto, riesgo, trade-off e inversión requerida.

## Material adicional para sustentación
- `docs/06_GUIA_ESTUDIO_CONCEPTOS.md`
- `docs/07_EJECUCION_Y_EVIDENCIAS.md`
- `SECURITY.md`
- `requests.http`

## Correcciones técnicas clave
1. secretos fuera del repositorio;
2. contraseñas fuera del YAML y BCrypt para la demo;
3. JWT corto y firmado con clave >= 256 bits;
4. validación de firma, issuer y expiración;
5. manejo explícito de JWT inválido;
6. autenticación stateless;
7. autorización RBAC USER/ADMIN;
8. 401 vs 403 correctos;
9. validación de requests;
10. pruebas automatizadas de controles de seguridad.
