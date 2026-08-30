# ADR-001 — MFA y autenticación reforzada para operaciones sensibles

- **Estado:** Aceptada para diseño de producción
- **Fecha:** 2026-08-30
- **Contexto:** plataforma de comercio electrónico
- **Referencia:** OWASP ASVS 5.0.0, especialmente V6 Authentication y V8 Authorization

## Contexto
Una plataforma de comercio electrónico debe equilibrar reducción de fraude y secuestro de cuenta con una experiencia de compra de baja fricción. ASVS 5.0 L2 exige MFA o mecanismos equivalentes con una postura explícitamente rigurosa. El proyecto demo implementa usuario/contraseña + JWT para demostrar el flujo técnico, pero eso no representa por sí solo el objetivo de producción L2.

## Fuerzas en tensión
- Reducir account takeover, credential stuffing y fraude.
- Evitar fricción innecesaria en navegación y tareas de bajo riesgo.
- Mantener una experiencia de compra competitiva.
- Controlar costos de soporte y recuperación de factores.
- Cumplir el nivel de aseguramiento decidido por la organización.

## Opciones evaluadas

### Opción A — Solo usuario y contraseña
**Ventajas:** implementación simple y menor fricción inicial.

**Desventajas:** una contraseña comprometida puede ser suficiente para tomar la cuenta; insuficiente como postura objetivo L2 sin una justificación fuerte y controles compensatorios.

### Opción B — MFA obligatorio en cada inicio de sesión
**Ventajas:** reduce significativamente el riesgo de uso de credenciales robadas.

**Desventajas:** más fricción, mayor abandono y más dependencia de mecanismos de recuperación.

### Opción C — MFA como base de producción + step-up adaptativo para acciones de alto riesgo
**Ventajas:** combina autenticación fuerte con controles contextuales; permite reforzar operaciones como cambio de contraseña, modificación de datos críticos o administración.

**Desventajas:** aumenta complejidad de arquitectura, telemetría y UX.

## Decisión
Adoptar **Opción C** en producción: autenticación multifactor mediante un Identity Provider compatible con OIDC y autenticación reforzada (step-up) para operaciones de mayor riesgo. Preferir factores resistentes al phishing cuando el perfil de riesgo lo justifique.

El código local del reto conserva un login simple únicamente como mecanismo autocontenido de demostración de JWT, RBAC y pruebas. No debe interpretarse como arquitectura final de identidad.

## Consecuencias positivas
- Menor riesgo de account takeover.
- Mejor alineación con ASVS L2.
- Controles más fuertes en funciones administrativas y transacciones sensibles.
- Posibilidad de centralizar lifecycle de identidad y auditoría en un IdP.

## Consecuencias negativas / costos
- Integración OIDC/MFA adicional.
- Gestión de recuperación de factores.
- Pruebas adicionales de UX, fraude y disponibilidad del IdP.
- Necesidad de definir cuándo se dispara step-up.

## Acciones
1. Seleccionar IdP corporativo/OIDC.
2. Definir operaciones de alto riesgo y reglas de step-up.
3. Instrumentar señales de riesgo y eventos de autenticación.
4. Definir recuperación segura de factores.
5. Validar los requisitos ASVS L2 aplicables antes de salida a producción.
