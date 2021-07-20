FROM openjdk:11-jre-slim

# define o diretorio de trabalho no container
WORKDIR /app

# copia de um diretorio local para container
# obs : o nome do jar pode mudar conforme desejar
COPY build/libs/*-all.jar /app/key-manager-rest-api.jar

# serve para documentar a porta de conexao ( não publica ela )
EXPOSE 8080

# comandos para ser executados no WORKDIR
CMD ["java", "-jar", "key-manager-rest-api.jar"]