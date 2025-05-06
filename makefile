# Variáveis
DOCKER_COMPOSE = docker-compose # Caminho para o docker-compose
DOCKER = docker
IMAGE_NAME = carometro-app # Nome da imagem Docker
CONTAINER_NAME = carometro-container # Nome do container
VOLUME_NAME = dados-sql # Nome do volume persistente do SQL Server

# Comando para compilar o projeto com Maven
build:
	@echo "Construindo imagem Docker..."
	${DOCKER} build -t ${IMAGE_NAME} .

# Comando para construir a imagem docker da aplicação
docker-build:
	@echo "Construindo imagem docker para a aplicação..."
	${DOCKER} build -t ${IMAGE_NAME} .

# Comando para iniciar os containers com Docker Compose
up:
	@echo "Iniciando aplicação com Docker Compose..."
	${DOCKER_COMPOSE} up --build -d 

# Comando para parar e remover os containers
down:
	@echo "Parando e removendo os containers, volumes e órfãos..."
	${DOCKER_COMPOSE} down --volumes --remove-orphans

# Comando para visualizar os logs
logs:
	@echo "Exibindo logs..."
	${DOCKER_COMPOSE} logs -f

# Comando para iniciar apenas o SQL Server
db-up:
	@echo "Iniciando container SQL Server"
	${DOCKER_COMPOSE} up -d sqlserver

# Comando para verificar o status dos containers
status:
	@echo "Verificando status dos containers..."
	${DOCKER_COMPOSE} ps

# Comando para rodar os testes unitários com Maven
test:
	@echo "Rodando testes unitários"
	mvn test

# Comando para limpar a build (artefatos do Maven)
clean:
	@echo "Limpando projeto..."
	mvn clean

# Comando para criar e subir o projeto completo (build + up)
all:
	@echo "Construindo o projeto completo..."
	${MAKE} build
	${MAKE} docker-build
	${MAKE} up

# Comando para remover volumes do Docker (se necessário)
remove-volumes:
	@echo "Removendo volumes persistentes..."
	${DOCKER} volume rm ${VOLUME_NAME}

# Comando para rebuildar a imagem (mesmo que a imagem já exista)
rebuild:
	@echo "Rebuildando a imagem Docker..."
	${DOCKER} build --no-cache -t ${IMAGE_NAME} .
