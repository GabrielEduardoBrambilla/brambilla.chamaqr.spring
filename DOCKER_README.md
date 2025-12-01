# Dockerization: Spring Boot Backend

This document explains the Docker setup for the Spring Boot Backend application running on Tomcat.

## 🐳 Dockerfile Overview (`Dockerfile.backend`)

The Dockerfile uses a **Multi-Stage Build** to compile the Java code and deploy it to a Tomcat server.

### Stage 1: Build
- **Base Image:** `maven:3.9-eclipse-temurin-17`
- **Action:** Compiles the Java source code and packages it into a `.war` file.

### Stage 2: Runtime
- **Base Image:** `tomcat:11-jre17-temurin`
- **Action:** Runs the compiled WAR file.
- **Configuration:**
  - **SSL:** Configured in `server.xml` to listen on port **8443**.
  - **Certificates:** Copies `ssl_certs/fullchain.pem` and `ssl_certs/wildcard.key` to Tomcat configuration.
  - **Environment:** Sets up connection variables for MySQL and Keycloak.

## 🛠️ Build & Run Commands

### 1. Build the Image
Run this from the `brambilla.chamaqr.spring` directory:

```bash
docker build -t spring-backend -f Dockerfile.backend .
```

### 2. Run the Container
**Important:** The backend listens on port **8443** (HTTPS), not 8080.

```bash
docker run -d \
  -p 8443:8443 \
  --name backend \
  spring-backend
```

## ⚙️ Environment Variables
The Dockerfile sets default values, but you can override them at runtime using `-e`:

- `MYSQL_HOST` (Default: `192.168.56.101`)
- `KEYCLOAK_URL` (Default: `https://192.168.56.104`)
- `KEYCLOAK_REALM` (Default: `chamadaqr`)

## 🔑 SSL Configuration
The build expects a directory `ssl_certs/` containing:
- `fullchain.pem`
- `wildcard.key`
