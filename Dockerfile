# Stage 1: build
FROM maven:3.8.6-openjdk-17 AS build
WORKDIR /app

# Copia pom e src
COPY pom.xml .
COPY src ./src

# Compila o jar
RUN mvn clean package -DskipTests

# Stage 2: runtime
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copia o jar gerado
COPY --from=build /app/target/*.jar app.jar

# Variáveis de ambiente para a conexão com o Postgres
ENV DB_HOST=db
ENV DB_PORT=5432
ENV DB_NAME1=orcamento
ENV DB_USER=postgres
ENV DB_PASSWORD=MeuP@inscape10

# Expõe a porta do Spring Boot
EXPOSE 8080

# Inicia a aplicação

ENTRYPOINT ["java", "-jar", "app.jar"]
