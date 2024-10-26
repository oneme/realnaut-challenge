# Proyecto de Spaceships

Este proyecto es una aplicación de gestión de naves espaciales construida con Spring Boot y Docker. Aquí te mostramos cómo ejecutar los tests y levantar los contenedores Docker.

## Prerrequisitos

Asegúrate de tener instalados los siguientes programas en tu máquina:

- Java 17
- Maven 3.8.4 o superior
- Docker y Docker Compose

## Ejecutar Tests

Para ejecutar los tests del proyecto, utiliza Maven con el siguiente comando:

```bash
mvn test
```

## Levantar los contenedores

Para levantar los contenedores, utiliza Docker Compose con el siguiente comando:

```bash
docker compose up --build
