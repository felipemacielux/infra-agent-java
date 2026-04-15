# Infra Agent – Java + Docker

Infra Agent é um agente de monitoramento simples escrito em Java, projetado para executar de forma contínua e coletar informações básicas de saúde do sistema.

O projeto foi desenvolvido com foco em boas práticas de infraestrutura e containerização.

## ✅ Funcionalidades

- Execução contínua (agent)
- Verificação de espaço em disco disponível
- Verificação de porta local
- Logs persistidos em arquivo
- Logs em STDOUT (compatível com Docker)
- Configuração via variáveis de ambiente
- Docker HEALTHCHECK

## 🧰 Tecnologias

- Java 21
- Maven
- Docker (imagem Alpine)
- Linux

## ⚙️ Configuração via Variáveis de Ambiente

| Variável | Descrição | Padrão |
|--------|----------|-------|
| CHECK_INTERVAL_SECONDS | Intervalo entre checks | 30 |
| CHECK_PORT | Porta monitorada | 8080 |
| LOG_FILE_PATH | Caminho do log | /app/logs/health.log |

## ▶️ Como executar com Docker

```bash
docker build -t infra-agent .
docker run \
--name infra_agent \
-v ~/infra_agent-logs:/app/logs \
infra_agent
