# Ejecución y evidencias

## Entorno utilizado

- Windows 10/11
- IntelliJ IDEA Community Edition
- Java 21
- Maven gestionado desde IntelliJ
- PowerShell integrado en IntelliJ

## Configuración inicial

Abrir el proyecto en IntelliJ y esperar a que Maven cargue las dependencias definidas en `pom.xml`.

Validar que el proyecto esté usando Java 21:

Run
→ Edit Configurations
→ SecurityApplication

En `Build and run` debe aparecer Java 21.

## Variables obligatorias

La aplicación utiliza variables de entorno para evitar almacenar secretos y contraseñas directamente en el código fuente.

Variables requeridas:

- `JWT_SECRET_BASE64`
- `APP_USER_PASSWORD`
- `APP_ADMIN_PASSWORD`

### Generar la clave JWT en Windows

Desde la terminal PowerShell de IntelliJ:

```powershell
$bytes = New-Object byte[] 32
[Security.Cryptography.RandomNumberGenerator]::Fill($bytes)
[Convert]::ToBase64String($bytes)

-----------------


borrador

El comando devuelve un valor Base64 similar a:

CvUr2VYVeUVHW1cQ+PfbS/uvpyJyh25ifdsZ7saUqf4=

Este valor se utiliza como JWT_SECRET_BASE64.

Configurar variables en IntelliJ

Ir a:

Run
→ Edit Configurations
→ SecurityApplication

En Environment variables configurar:

JWT_SECRET_BASE64=<valor generado>
APP_USER_PASSWORD=User12345!
APP_ADMIN_PASSWORD=Admin12345!

También pueden ingresarse en una sola línea separadas por ;:

JWT_SECRET_BASE64=<valor generado>;APP_USER_PASSWORD=User12345!;APP_ADMIN_PASSWORD=Admin12345!

Guardar con:

Apply
→ OK

Las contraseñas anteriores son únicamente valores locales de prueba.
En ambientes reales deben gestionarse mediante mecanismos seguros de secretos.

Ejecutar la aplicación

Abrir:

src/main/java/com/pragma/security/SecurityApplication.java

Ejecutar mediante el botón verde ubicado junto al método main:

Run 'SecurityApplication'

La aplicación inició correctamente si la consola muestra mensajes similares a:

Tomcat started on port 8080 (http)
Started SecurityApplication

La aplicación queda disponible en:

http://localhost:8080
Pruebas manuales

Como IntelliJ IDEA Community no permite ejecutar directamente archivos .http, las pruebas se realizan desde PowerShell utilizando Invoke-RestMethod.

1. Endpoint público

Desde el navegador o PowerShell:

Invoke-RestMethod `
  -Uri "http://localhost:8080/api/public/health"

Resultado esperado:

200 OK

Este endpoint no requiere autenticación.

2. Login de usuario USER
$body = @{
  username = "challenge-user"
  password = "User12345!"
} | ConvertTo-Json

$response = Invoke-RestMethod `
  -Uri "http://localhost:8080/api/auth/login" `
  -Method Post `
  -ContentType "application/json" `
  -Body $body

$response

Resultado esperado:

Se recibe un JWT en la propiedad:

accessToken

Guardar el token:

$token = $response.accessToken
3. USER accede a endpoint protegido
Invoke-RestMethod `
  -Uri "http://localhost:8080/api/secured/hello" `
  -Headers @{
    Authorization = "Bearer $token"
  }

Resultado esperado:

200 OK

Ejemplo de respuesta:

message                  user
-------                  ----
Hello, secured endpoint! challenge-user

Esto demuestra que el JWT es válido y que el usuario se encuentra autenticado.

4. USER intenta acceder a endpoint ADMIN
Invoke-WebRequest `
  -Uri "http://localhost:8080/api/secured/admin" `
  -Headers @{
    Authorization = "Bearer $token"
  }

Resultado esperado:

403 Forbidden

Ejemplo:

{
  "error": "forbidden"
}

Este resultado es correcto.

El usuario se encuentra autenticado, pero no tiene el rol requerido para acceder al recurso administrativo.

5. Login de usuario ADMIN
$adminBody = @{
  username = "challenge-admin"
  password = "Admin12345!"
} | ConvertTo-Json

$adminResponse = Invoke-RestMethod `
  -Uri "http://localhost:8080/api/auth/login" `
  -Method Post `
  -ContentType "application/json" `
  -Body $adminBody

$adminToken = $adminResponse.accessToken
6. ADMIN accede al endpoint administrativo
Invoke-RestMethod `
  -Uri "http://localhost:8080/api/secured/admin" `
  -Headers @{
    Authorization = "Bearer $adminToken"
  }

Resultado esperado:

200 OK

Ejemplo:

message                       user
-------                       ----
Administrative access granted challenge-admin

Esto demuestra que la autorización basada en roles funciona correctamente.

Pruebas adicionales de seguridad
Endpoint protegido sin JWT
Invoke-WebRequest `
  -Uri "http://localhost:8080/api/secured/hello"

Resultado esperado:

401 Unauthorized
Login con contraseña incorrecta
$badBody = @{
  username = "challenge-user"
  password = "ClaveIncorrecta"
} | ConvertTo-Json

Invoke-WebRequest `
  -Uri "http://localhost:8080/api/auth/login" `
  -Method Post `
  -ContentType "application/json" `
  -Body $badBody

Resultado esperado:

401 Unauthorized
JWT inválido
Invoke-WebRequest `
  -Uri "http://localhost:8080/api/secured/hello" `
  -Headers @{
    Authorization = "Bearer token-invalido"
  }

Resultado esperado:

401 Unauthorized
Suite automatizada

La clase:

src/test/java/.../SecurityApplicationTests.java

incluye pruebas para validar:

endpoint público sin credenciales → 200;
endpoint protegido sin token → 401;
USER accede a /secured/hello → 200;
USER intenta acceder a /secured/admin → 403;
ADMIN accede a /secured/admin → 200;
JWT inválido → 401;
credenciales incorrectas → 401.
Ejecutar pruebas desde IntelliJ

Abrir:

src/test/java

Ubicar:

SecurityApplicationTests

Clic derecho:

Run 'SecurityApplicationTests'

La ejecución es satisfactoria cuando todos los tests aparecen en verde y no existen errores ni fallos.

Evidencias obtenidas

Durante la validación manual se comprobó:

Prueba	Resultado
Aplicación inicia correctamente	OK
Tomcat escucha en puerto 8080	OK
Login USER genera JWT	OK
USER accede a /secured/hello	200
USER intenta /secured/admin	403
Login ADMIN genera JWT	OK
ADMIN accede a /secured/admin	200

También se verificó durante el arranque:

Global AuthenticationManager configured with UserDetailsService

confirmando que Spring Security detecta y configura correctamente el servicio de usuarios.

Interpretación de resultados

Los resultados permiten verificar dos conceptos principales:

Autenticación
→ comprobar quién es el usuario
→ login + JWT

y:

Autorización
→ comprobar qué puede hacer ese usuario
→ roles USER y ADMIN

Por esta razón:

USER + /secured/hello
→ 200

pero:

USER + /secured/admin
→ 403

mientras que:

ADMIN + /secured/admin
→ 200

El código 401 representa un problema de autenticación, mientras que 403 indica que el usuario está correctamente autenticado pero no posee permisos suficientes.

Consideraciones de seguridad

Las variables sensibles no deben almacenarse directamente en el repositorio.

El proyecto incluye:

.env.example
.gitignore

pero no debe incluir un archivo .env con secretos reales.

La clave utilizada para firmar los JWT se proporciona externamente mediante:

JWT_SECRET_BASE64

y las contraseñas locales mediante:

APP_USER_PASSWORD
APP_ADMIN_PASSWORD

Esto evita dejar credenciales y secretos hardcodeados dentro del código fuente.


Y cambiaría especialmente la parte final anterior que decía:

> “no se afirma una ejecución…”

porque **ya no aplica a tu caso**. Tú sí ejecutaste la aplicación y ya validaste manualmente autenticación y autorización.

Tu estado real ahora es:

```text
Compilación           ✅
Spring Boot inicia    ✅
Tomcat 8080           ✅
Login USER            ✅
JWT                    ✅
Autorización USER     ✅
403 para ADMIN        ✅
Login ADMIN           ✅
Acceso ADMIN          ✅