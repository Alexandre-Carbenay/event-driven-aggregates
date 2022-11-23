# Export environment variable for Linux docker-compose execution
gradle = ./gradlew
ifeq ($(OS),Windows_NT)
	gradle = gradlew
endif

build: ## Build the application
	$(gradle) bootJar && docker build -t library-loan:latest .

up: ## Start the demo environment
	$(LINUX_DOCKER_COMPOSE_FIX) docker-compose -f docker/docker-compose.yml -f docker/docker-compose.kafka.yml up -d

down: ## Stop the demo environment
	$(LINUX_DOCKER_COMPOSE_FIX) docker-compose -f docker/docker-compose.yml -f docker/docker-compose.kafka.yml down

kafka-up: ## Start the kafka dependencies
	$(LINUX_DOCKER_COMPOSE_FIX) docker-compose -f docker/docker-compose.kafka.yml up -d

kafka-down: ## Stop the kafka dependencies
	$(LINUX_DOCKER_COMPOSE_FIX) docker-compose -f docker/docker-compose.kafka.yml down

clean: ## Clean the project folder
	$(gradle) clean

help: ## This help dialog
	@echo "Usage: make [target]. Find the available targets below:"
	@echo "$$(grep -hE '^\S+:.*##' $(MAKEFILE_LIST) | sed 's/:.*##\s*/:/' | column -c2 -t -s :)"
