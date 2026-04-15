# Imagem base: Java JRE leve
FROM eclipse-temurin:21-jre-alpine

# Diretório de trabalho dentro do container
WORKDIR /app

# Diretório para logs
RUN mkdir -p /app/logs

# Copia o JAR gerado pelo Maven
COPY target/infra-agent-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar

# Variáveis de ambiente com valores padrão
ENV CHECK_INTERVAL_SECONDS=30
ENV CHECK_PORT=8080
ENV LOG_FILE_PATH=/app/logs/health.log

# verifica se o log foi atualizado nos últimos 2 min
HEALTHCHECK --interval=30s --timeout=5s --start-period=30s \
CMD test -f /app/logs/health.log && \
find /app/logs/health.log -mmin -2 | grep -q .

# Comando que será executado quando o container iniciar
ENTRYPOINT ["java", "-jar", "app.jar"]