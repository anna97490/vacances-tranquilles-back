# Manuel de mise à jour — Vacances Tranquilles 

Ce document décrit les étapes pour maintenir et déployer en toute sécurité l’application. 

--- 

## Stratégie Git  

### Branches utilisées  

| Branche        | Rôle                                        |   
|----------------|---------------------------------------------|  
| `main`         | Code stable — déploiement prod automatique  |  
| `staging`      | Code validé mais en pré-prod                |  
| `feature/*`    | Nouvelle fonctionnalité                     |  
| `fix/*`        | Correction de bug                           |  


### Nommage des branches  
- `feature/reservation-system`  
- `fix/login-error`  

---  

## Workflow de développement standard 

### 1. Se placer sur `staging`, la mettre à jour et créer une branche : 

``` 
git checkout staging 
git pull origin staging 
git checkout -b feature/ma-feature 
``` 

### 2. Développer la feature : 

- Incrémenter le code 
- Écrire les tests unitaires associés 
- Documenter (JavaDoc, Typedoc) 

### 3. Commit : 

``` 
git add . 
git commit -m "add feature XYZ with unit tests and documentation" 
``` 

Les commits doivent être professionnels, clairs, en anglais et respecter la convention Conventional Commits.  

### 4. Pousser la branche 

``` 
git push origin feature/ma-feature 
``` 

### 5. Vérifier la CI 

L’action GitHub se déclenche automatiquement. 

Valider que :  
- Les tests passent  
- La couverture (Codecov) est ≥ 80 %  
- L'analyse de code est satisfaisante avec SonarQube  

### 6. Créer une PR vers `staging` 

- Attendre la validation CI 
- Désigner un Reviewer 
- Prendre en compte ses retours 

Le reviewer est chargé de relire le code, vérifier les tests, et s'assurer de la conformité aux bonnes pratiques avant validation de la PR. 

### 7. Mettre localement à jour `staging` 

``` 
git checkout staging  
git pull origin staging 
``` 

### 8. Mettre à jour la version : 

Modifier manuellement le numéro de version en respectant semver. 

``` 
git add . 
git commit -m "core(release): bump version to x.y.z" 
git push origin staging 
``` 

### 9. Créer une PR de `staging` vers `main` 

Vérifier :  
- Tests OK  
- SonarQube OK  
- CI validée  

### 10. Déploiement via Render 

Déploiement déclenché automatiquement si version changée (via le webhook Render). 

--- 

## CI/CD GitHub Actions 

- Lancement automatique sur `push`/`pull_request` 
- Couverture test ≥ 80% (Codecov) 
- Analyse SonarQube 
- Déploiement auto si version changée sur main 

--- 

## Versioning 

Numéro de version défini dans : 
- Backend : `pom.xml` 
- Frontend : `package.json` 

Respecter les règles semver : https://semver.org/ 

- `fix`: correction de bug 
- `minor`: nouvelle fonctionnalité sans rupture 
- `major`: changement incompatible (éviter sans validation) 

Un changement de version est obligatoire pour déclencher le déploiement Render. 

--- 

## Lancement des tests 

- Backend : `./mvnw test` (JUnit + MockMvc) 
- Frontend : `npm test` (Karma + Jasmine) 

--- 

## Génération de la documentation 

- Backend : `./mvnw javadoc:javadoc` 
Documentation générée dans `/target/site/apidocs` 
- Frontend : `npm run docs` 
Documentation générée dans `/docs/typedoc` 

--- 

## Conventions de commit 

Les commits doivent être professionnels, clairs, en anglais et respecter la convention Conventional Commits.  

Exemples de message : 
- feat: added reservation system 
- fix: fixed login error 

--- 

## Release GitHub 

1. Aller dans l’onglet "Releases" du dépôt 
2. Cliquer sur "Draft a new release" 
3. Remplir :  
- Tag version : vX.Y.Z  
- Target : main  
- Titre : Release vX.Y.Z  
- Description : changelog structuré (features, fixes, tests, sécurité, endpoints utilisés…)  

4. Publier : Publish release 

--- 

## Bonnes pratiques 

- Ne jamais committer de fichiers .env, secrets ou mots de passe  
- Toujours lancer les tests localement avant de merger  
- Maintenir une couverture de test ≥ 80 %  
- Faire des commits petits, fréquents et descriptifs  
- Une Pull Request = une fonctionnalité  
- Respecter les conventions de commit  
- Attendre la validation CI + review avant merge vers main  

--- 

## Mise à jour des dépendances 

### Backend 
- Vérifier les dépendances dans `pom.xml` 
- Mettre à jour avec : 
./mvnw versions:display-dependency-updates 
./mvnw versions:use-latest-releases 

### Frontend 

- Vérifier les packages dans `package.json` 
- Mettre à jour avec : 
npm outdated 
npm update 

--- 

## Migration de la base de données 

À ce stade du projet, aucune migration structurée de la base n’a été nécessaire.  

--- 

## Tests de non-régression 

Les tests unitaires mis en place (JUnit / Jasmine) assurent un premier niveau de non-régression.   
Des tests end-to-end (Cypress ou Postman) pourront être ajoutés pour renforcer la couverture des cas critiques. 

--- 

## Mise à jour 

Ce guide est mis à jour régulièrement pour refléter les évolutions du projet et les bonnes pratiques DevOps. 
