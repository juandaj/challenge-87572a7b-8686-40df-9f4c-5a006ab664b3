[IntelliJ IDEA] Your PSReadLine module version (2.0.0) is outdated, which may cause the problem with black lines across the terminal screen:
https://learn.microsoft.com/windows/terminal/troubleshooting#black-lines-in-powershell-51-6x-70
Install the latest version by running: 'Install-Module PSReadLine -MinimumVersion 2.0.3 -Scope CurrentUser -Force'
After the installation, open a new terminal tab.

PS C:\Users\juan.aragon_pragma\Desktop\Assessment\2026\Estándares de Seguridad\challenge-87572a7b-8686-40df-9f4c-5a006ab664b3> $body = @{
>>   username = "challenge-user"
>>   password = "User12345!"
>> } | ConvertTo-Json
PS C:\Users\juan.aragon_pragma\Desktop\Assessment\2026\Estándares de Seguridad\challenge-87572a7b-8686-40df-9f4c-5a006ab664b3> 
PS C:\Users\juan.aragon_pragma\Desktop\Assessment\2026\Estándares de Seguridad\challenge-87572a7b-8686-40df-9f4c-5a006ab664b3> $response = Invoke-RestMethod `
>>   -Uri "http://localhost:8080/api/auth/login" `
>>   -Method Post `
>>   -ContentType "application/json" `
>>   -Body $body
PS C:\Users\juan.aragon_pragma\Desktop\Assessment\2026\Estándares de Seguridad\challenge-87572a7b-8686-40df-9f4c-5a006ab664b3> 
PS C:\Users\juan.aragon_pragma\Desktop\Assessment\2026\Estándares de Seguridad\challenge-87572a7b-8686-40df-9f4c-5a006ab664b3> $response

accessToken                                                                                                                                                                                                                        
-----------                                                                                                                                                                                                                        
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaGFsbGVuZ2UtdXNlciIsImlzcyI6InByYWdtYS1zZWN1cml0eS1jaGFsbGVuZ2UiLCJpYXQiOjE3ODgxMTk4NzEsImV4cCI6MTc4ODEyMDc3MSwianRpIjoiOTkzZjIzZGYtMGI3Ni00YjQwLWEzZTQtZDg0NDVhNDFiNmNjIiwicm9sZXMiOlsiUk9MRV9...


PS C:\Users\juan.aragon_pragma\Desktop\Assessment\2026\Estándares de Seguridad\challenge-87572a7b-8686-40df-9f4c-5a006ab664b3> $token = $response.accessToken
PS C:\Users\juan.aragon_pragma\Desktop\Assessment\2026\Estándares de Seguridad\challenge-87572a7b-8686-40df-9f4c-5a006ab664b3> Invoke-RestMethod `
>>   -Uri "http://localhost:8080/api/secured/hello" `                                                                                                                                                                               
>>   -Headers @{                                                                                                                                                                                                                    
>>     Authorization = "Bearer $token"                                                                                                                                                                                              
>>   }                                                                                                                                                                                                                              

message                  user          
-------                  ----          
Hello, secured endpoint! challenge-user


PS C:\Users\juan.aragon_pragma\Desktop\Assessment\2026\Estándares de Seguridad\challenge-87572a7b-8686-40df-9f4c-5a006ab664b3> Invoke-WebRequest `
>>   -Uri "http://localhost:8080/api/secured/admin" `                                                                                                                                                                               
>>   -Headers @{                                                                                                                                                                                                                    
>>     Authorization = "Bearer $token"                                                                                                                                                                                              
>>   }                                                                                                                                                                                                                              
Invoke-WebRequest : {"error":"forbidden"}
En línea: 1 Carácter: 1
+ Invoke-WebRequest `
+ ~~~~~~~~~~~~~~~~~~~
    + CategoryInfo          : InvalidOperation: (System.Net.HttpWebRequest:HttpWebRequest) [Invoke-WebRequest], WebException
    + FullyQualifiedErrorId : WebCmdletWebResponseException,Microsoft.PowerShell.Commands.InvokeWebRequestCommand
PS C:\Users\juan.aragon_pragma\Desktop\Assessment\2026\Estándares de Seguridad\challenge-87572a7b-8686-40df-9f4c-5a006ab664b3> $adminBody = @{
>>   username = "challenge-admin"                                                                                                                                                                                                   
>>   password = "Admin12345!"                                                                                                                                                                                                       
>> } | ConvertTo-Json                                                                                                                                                                                                               
PS C:\Users\juan.aragon_pragma\Desktop\Assessment\2026\Estándares de Seguridad\challenge-87572a7b-8686-40df-9f4c-5a006ab664b3> 
PS C:\Users\juan.aragon_pragma\Desktop\Assessment\2026\Estándares de Seguridad\challenge-87572a7b-8686-40df-9f4c-5a006ab664b3> $adminResponse = Invoke-RestMethod `
>>   -Uri "http://localhost:8080/api/auth/login" `                                                                                                                                                                                  
>>   -Method Post `                                                                                                                                                                                                                 
>>   -ContentType "application/json" `                                                                                                                                                                                              
>>   -Body $adminBody                                                                                                                                                                                                               
PS C:\Users\juan.aragon_pragma\Desktop\Assessment\2026\Estándares de Seguridad\challenge-87572a7b-8686-40df-9f4c-5a006ab664b3> 
PS C:\Users\juan.aragon_pragma\Desktop\Assessment\2026\Estándares de Seguridad\challenge-87572a7b-8686-40df-9f4c-5a006ab664b3> $adminToken = $adminResponse.accessToken
PS C:\Users\juan.aragon_pragma\Desktop\Assessment\2026\Estándares de Seguridad\challenge-87572a7b-8686-40df-9f4c-5a006ab664b3> Invoke-RestMethod `
>>   -Uri "http://localhost:8080/api/secured/admin" `                                                                                                                                                                               
>>   -Headers @{                                                                                                                                                                                                                    
>>     Authorization = "Bearer $adminToken"                                                                                                                                                                                         
>>   }                                                                                                                                                                                                                              

message                       user           
-------                       ----           
Administrative access granted challenge-admin


PS C:\Users\juan.aragon_pragma\Desktop\Assessment\2026\Estándares de Seguridad\challenge-87572a7b-8686-40df-9f4c-5a006ab664b3> 