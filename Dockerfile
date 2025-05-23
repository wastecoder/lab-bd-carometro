### BUILD

# Usa maven e Java 17 para build
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Define o diretório de trabalho no container
WORKDIR /build

# Copia o código fonte para o container
COPY . .

# Realiza o build do projeto, ignorando os testes
RUN mvn clean package -DskipTests

### Runtime

# Imagem leve do Java 17 (JRE)
FROM eclipse-temurin:17-jre-alpine

# Define o diretório de trabalho para a aplicação
WORKDIR /app

# Copia o JAR gerado da fase de build para o diretório de execução
COPY --from=builder /build/target/*.jar app.jar

# Define o perfil ativo como "docker"
ENV SPRING_PROFILES_ACTIVE=docker

# Cria o diretório e define permissões
RUN mkdir -p /uploads && chmod -R 777 /uploads

# Expõe a porta onde a aplicação irá rodar
EXPOSE 8080

# Define o comando para rodar a aplicação
ENTRYPOINT [ "java", "-jar", "app.jar" ]
