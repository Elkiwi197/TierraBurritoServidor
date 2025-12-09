### Etapa 1: build (con Maven)
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copia el pom y descarga dependencias antes (mejor caché)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia el código completo
COPY . .

# Compila
RUN mvn clean package -DskipTests


### Etapa 2: runtime (solo JDK)
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
