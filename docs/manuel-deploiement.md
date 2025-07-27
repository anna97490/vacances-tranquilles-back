# Manuel de déploiement — Vacances Tranquilles

Ce guide décrit comment installer, configurer et déployer l’application **Vacances Tranquilles**, composée d’un backend Java (Spring Boot) et d’un frontend Angular.

---

## Prérequis techniques

- Java 17
- Node.js 18+
- Maven
- Angular CLI
- Docker + Docker Compose
- PostgreSQL (local ou distant via Render)
- Git

---

## Structure des repositories

- **Frontend** : https://github.com/anna97490/vacances-tranquilles-front
- **Backend** : https://github.com/anna97490/vacances-tranquilles-back

Organisez les deux projets dans un même dossier parent :

```
/vacances-tranquilles/
├── vacances-tranquilles-front/
└── vacances-tranquilles-back/
```

---

## Initialisation du projet local

```
git clone https://github.com/anna97490/vacances-tranquilles-front.git
git clone https://github.com/anna97490/vacances-tranquilles-back.git
```

Vérifiez la connexion aux dépôts :

```
git remote -v
```

---

## Lancer le projet en local

### Backend (Spring Boot)

#### PowerShell (Windows)

```powershell
$env:SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/vacancestranquillesdblocal"
$env:SPRING_DATASOURCE_USERNAME="admin"
$env:SPRING_DATASOURCE_PASSWORD="root"
$env:JWT_SECRET="N8w3#f5LxPpV2zX7!tQhZ9cR@rU6vYeW"
$env:ALLOWED_ORIGINS="http://localhost:8081"
mvn spring-boot:run
```

#### Mac/Linux (zsh)

```bash
export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/vacancestranquillesdblocal"
export SPRING_DATASOURCE_USERNAME="admin"
export SPRING_DATASOURCE_PASSWORD="root"
export JWT_SECRET="N8w3#f5LxPpV2zX7!tQhZ9cR@rU6vYeW"
mvn spring-boot:run
```

### Frontend (Angular)

```
cd vacances-tranquilles-front
npm install
npm start
```

### Services accessibles à :
- frontend : http://localhost:4200 
- backend : http://localhost:8080 

---

## Lancer avec Docker Compose

```
cd vacances-tranquilles-back
docker-compose build
docker-compose up
```

### Services accessibles à :
- frontend : http://localhost:4200 
- backend : http://localhost:8080 

---

## Visualisation base de données

Utilisez au choix : 
- DBeaver : https://dbeaver.io/download/
- pgAdmin : https://www.pgadmin.org/download/

Configurer l’accès à la base PostgreSQL locale ou distante selon vos besoins

---

## Déploiement Render (PaaS)

Les deux projets (frontend et backend) sont déployés automatiquement sur Render via des webhooks appelés par les GitHub Actions après une validation sur `main`.

### Backend

- **URL prod backend** : https://vacances-tranquilles-back.onrender.com
- **Type de service** : Web Service
- **Environment** : Java 17
- **Build command** : `./mvnw clean package -DskipTests`
- **Start command** : `java -jar target/*.jar`

**Variables Render à configurer dans l’interface Render :**

| Variable                   | Description                                  |
|----------------------------|----------------------------------------------|
| SPRING_DATASOURCE_URL      | URL de connexion PostgreSQL                  |
| SPRING_DATASOURCE_USERNAME | Identifiant DB                               |
| SPRING_DATASOURCE_PASSWORD | Mot de passe DB                              |
| JWT_SECRET                 | Clé de sécurité pour token JWT               |
| ALLOWED_ORIGINS            | Origines autorisées pour frontend (CORS)     |

**Déploiement déclenché automatiquement** via **GitHub Actions**, **lors d’un push sur `main`**, **si la version dans `pom.xml` a été mise à jour**.
Un **webhook Render** configuré dans l’interface Render est **automatiquement généré et intégré dans GitHub**.


### Frontend

- **URL frontend** : https://vacances-tranquilles-front.onrender.com
- **Environnement** : Node 20+
- **Déploiement Render** déclenché automatiquement via un webhook **lors d’un push sur `main`**


Le projet frontend **ne nécessite aucune variable d’environnement à configurer sur Render**. 

---

## Bonnes pratiques

- Ne jamais versionner `.env` ou données sensibles.
- Toujours vérifier les variables d’environnement avant le déploiement.
- Utiliser GitHub Actions pour automatiser les tests avant déploiement.
