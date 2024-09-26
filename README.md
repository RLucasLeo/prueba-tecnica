# Documentación

La prueba técnica fue desarrollada utilizando Java 17, Spring Boot 3.3, JWT y MySQL.

Asegúrate de tener instalado Java 17, Apache Maven 3.9.x y MySQL 8.0.x.

## Cómo ejecutar la aplicación

1. Ubícate en la carpeta `demo` a través de la consola (Git Bash, PowerShell, etc.).
2. Ejecuta el comando `mvn spring-boot:run` para levantar el proyecto.
4. Se realizará una petición POST a `http://localhost:8080/sign-up` con el JSON obtenido en la documentación.
   Copia la respuesta del apartado "token".
5. Para realizar el inicio de sesión, se hará una solicitud POST a `http://localhost:8080/login`.
   Pega en la sección de Auth de Postman/Thunderclient, en la opción de Bearer, el contenido de token copiado previamente.
   Envía por cuerpo el JSON obtenido en la documentación.

## Ubicación y ejecución de los Tests a los servicios de sign-up y login

Los tests se hicieron utilizando JUnit y Mockito.

1. Para ejecutar los tests, debemos estar ubicados en la carpeta `demo` a través de la consola.
2. Ejecuta el comando `mvn test`.
   Esto nos retornará el resultado de la ejecución de los tests creados para login y sign-up.
3. Los tests se encuentran en el archivo `AuthServiceTest.java` ubicado en la carpeta `src/test/java/prueba_tecnica/`.
