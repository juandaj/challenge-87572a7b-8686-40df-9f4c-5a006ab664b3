# Entregable Fase 3 — Vista para negocio

## Decisión que negocio debe entender
La seguridad de acceso se diseña en capas: credenciales seguras, tokens de corta duración, permisos por rol y autenticación reforzada para acciones de mayor impacto.

## Riesgo de no hacerlo
Una cuenta robada podría permitir exposición de información, fraude, modificaciones administrativas o impacto reputacional. Autenticar a un usuario no implica automáticamente que deba tener permiso para todas las operaciones.

## Qué cambia para el usuario
- La navegación normal debe mantener baja fricción.
- Las acciones sensibles pueden solicitar una verificación adicional.
- Si un token es robado, su ventana de uso se limita al mantenerlo de corta duración.

## Qué cambia para la organización
- Las claves técnicas dejan de almacenarse en el código.
- Los accesos administrativos tienen permisos diferenciados.
- Existen pruebas que verifican controles de acceso básicos.
- Para producción se propone centralizar identidad y MFA en un proveedor especializado.

## Trade-off aceptado
Más seguridad puede agregar algunos pasos en acciones sensibles. Se acepta esa fricción porque el costo de fraude o toma de cuentas en esas operaciones es mayor que el costo de una verificación adicional.

## Decisión solicitada a negocio
Aprobar como objetivo de producción **ASVS L2 para los controles aplicables** y financiar/incluir en alcance la integración con un IdP que soporte MFA y step-up authentication.

## Indicadores sugeridos
- Tasa de account takeover/fraude.
- Tasa de éxito/fallo de MFA.
- Abandono asociado a step-up.
- Intentos de autenticación anómalos bloqueados.
- Hallazgos ASVS abiertos/cerrados por release.
