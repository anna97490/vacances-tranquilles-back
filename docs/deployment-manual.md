# Deployment manual — Vacances Tranquilles 

This guide explains how to install, configure, and deploy the **Vacances Tranquilles**, application, which consists of a Java (Spring Boot) backend and an Angular frontend.

--- 

## Technical prerequisites

- Java 17 
- Node.js 18+ 
- Maven 
- Angular CLI 
- Docker + Docker Compose 
- PostgreSQL (local or remote via Render) 
- Git 

--- 

## Repository structure

- **Frontend** : https://github.com/anna97490/vacances-tranquilles-front 
- **Backend** : https://github.com/anna97490/vacances-tranquilles-back 

Organize both projects under the same parent folder:

``` 
/vacances-tranquilles/ 
├── vacances-tranquilles-front/ 
└── vacances-tranquilles-back/ 
``` 

--- 

## Local project initialization

``` 
git clone https://github.com/anna97490/vacances-tranquilles-front.git 
git clone https://github.com/anna97490/vacances-tranquilles-back.git 
``` 

Check repository remotes:

``` 
git remote -v 
``` 

--- 

## Running the project locally

### Backend (Spring Boot) 

#### PowerShell (Windows) 

```powershell 
$env:SPRING_DATASOURCE_URL={{secret_datasource_url}} 
$env:SPRING_DATASOURCE_USERNAME={{secret_datasource_username}} 
$env:SPRING_DATASOURCE_PASSWORD={{secret_datasource_password}} 
$env:JWT_SECRET={{your_jwt_secret}} 
$env:ALLOWED_ORIGINS={{backend_url}} 
$env:STRIPE_API_KEY={{secret_stripe_api_key}} 
$env:FRONTEND_BASE_URL={{frontend_url}} 
$env:MONITORING_USERNAME={{secret_monitoring_username}} 
$env:MONITORING_PASSWORD={{secret_monitoring_password}} 
mvn spring-boot:run 
``` 

#### Mac/Linux (zsh) 

```bash 
export SPRING_DATASOURCE_URL={{secret_datasource_url}} 
export SPRING_DATASOURCE_USERNAME={{secret_datasource_username}} 
export SPRING_DATASOURCE_PASSWORD={{secret_datasource_password}} 
export JWT_SECRET={{your_jwt_secret}} 
export ALLOWED_ORIGINS={{frontend_url}} 
export STRIPE_API_KEY={{secret_stripe_api_key}} 
export FRONTEND_BASE_URL={{frontend_url}} 
export MONITORING_USERNAME={{secret_monitoring_username}} 
export MONITORING_PASSWORD={{secret_monitoring_password}} 
mvn spring-boot:run 
``` 

### Frontend (Angular) 

``` 
cd vacances-tranquilles-front 
npm install 
npm start 
``` 

### Services available at : 
- frontend : http://localhost:4200  
- backend : http://localhost:8080  

--- 

## Running with Docker Compose 

``` 
cd vacances-tranquilles-back 
docker-compose build 
docker-compose up 
``` 

### Services available at : 

- frontend : http://localhost:4200  
- backend : http://localhost:8080  

--- 

## Database Visualization

You can use either:
- DBeaver : https://dbeaver.io/download/ 
- pgAdmin : https://www.pgadmin.org/download/ 

Configure access to the PostgreSQL database (local or remote) according to your needs.

--- 

## Render deployment (PaaS) 

Both projects (frontend and backend) are automatically deployed on Render if the following conditions are met.

### Backend 

- **Production backend URL** : https://vacances-tranquilles-back.onrender.com 
- **Service type** : Web Service 
- **Environment** : Java 17 
- **Build command** : `./mvnw clean package -DskipTests` 
- **Start command** : `java -jar target/*.jar` 
  
**Render environment variables to configure in the Render dashboard :** 

| Variable                   | Description                                  | 
|----------------------------|----------------------------------------------| 
| SPRING_DATASOURCE_URL      | PostgreSQL connection URL                    | 
| SPRING_DATASOURCE_USERNAME | Database username                            | 
| SPRING_DATASOURCE_PASSWORD | Database password                            | 
| JWT_SECRET                 | Security key for JWT tokens                  | 
| ALLOWED_ORIGINS            | Allowed frontend origins (CORS)              | 
| STRIPE_API_KEY             | Stripe API secret key for online payments    | 
| FRONTEND_BASE_URL          | Frontend application URL                     | 
| MONITORING_USERNAME        | Username for monitoring interface access     | 
| MONITORING_PASSWORD        | Password for monitoring account              | 

**Deployment is automatically triggered** via **GitHub Actions**, **on push to `main`**, **if the version in `pom.xml` has been updated.**. 

### Frontend 

- **Frontend URL** : https://vacances-tranquilles-front.onrender.com 
- **Environement** : Node 20+ 
- **Deployment is automatically triggered** via **GitHub Actions**, **on push to `main`**, **if the version in `package.json` has been updated.**. 
 
The frontend project **does not require any environment variables to be configured in Render**.

--- 

## Best Practices

- Never commit .env files or sensitive data.
- Always check environment variables before deployment.
- Use GitHub Actions to automate tests before deployment.

--- 

## Updates

This guide is updated regularly to reflect project evolutions and DevOps best practices.
