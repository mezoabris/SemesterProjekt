# Semester Projekt

This is a Java application with a PostgreSQL database.

## Prerequisites

*   Docker
*   Docker Compose

## How to run

To start the application, run the following command:

```bash
docker compose up -d
```

This will start the application and the database in the background.

If you have made changes to the code, you will need to rebuild the Docker image. You can do this by running the following command:

```bash
docker compose up --build -d
```

This will rebuild the image and then start the application and the database in the background.

## How to stop

To stop the application, run the following command:

```bash
docker compose down
```

If you want to remove the database volume as well, run the following command:

```bash
docker compose down --volumes
```