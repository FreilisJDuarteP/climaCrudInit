# Usa una imagen de OpenJDK 17 como base
FROM openjdk:17-jdk-slim

# Establece el directorio de trabajo
WORKDIR /app

# Copia el archivo JAR generado por Maven al contenedor
COPY target/app.jar app.jar

# Exponer el puerto en el que corre la app (ajustar al puerto de Spring Boot si es diferente)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
