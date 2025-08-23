# Update manual — Vacances Tranquilles 

This document outlines the steps required to safely maintain and deploy the application.

--- 

## Git strategy

### Branches used 

| Branch        | Role                                           |   
|----------------|-----------------------------------------------|  
| `main`         | Stable code - automatic production deployment |  
| `staging`      | Validated code but in pre-production          |  
| `feature/*`    | New feature                                   |  
| `fix/*`        | Bug fix                                       |  


### Branches naming
- `feature/reservation-system`  
- `fix/login-error`  

---  

## Standard development workflow 

### 1. Switch to `staging`, update it, and create a new branch : 

``` 
git checkout staging 
git pull origin staging 
git checkout -b feature/ma-feature 
``` 

### 2. Develop the feature : 

- Implement the code 
- Write the associated unit tests 
- Document (JavaDoc, Typedoc) 

### 3. Commit : 

``` 
git add . 
git commit -m "add feature XYZ with unit tests and documentation" 
``` 

Commits must be professional, clear, in English, and follow the Conventional Commits specification.

### 4. Push the branch 

``` 
git push origin feature/ma-feature 
``` 

### 5. Check CI 

The GitHub Action is triggered automatically.

Validate that:
- Tests pass
- Code coverage (Codecov) is ≥ 80%
- Code analysis is satisfactory with SonarQube

### 6. Create a PR to `staging` 

- Wait for CI validation
- Assign a Reviewer
- Address the reviewer's feedback

The reviewer is responsible for reviewing the code, checking the tests, and ensuring compliance with best practices before approving the PR.

### 7. Update `staging` locally

``` 
git checkout staging  
git pull origin staging 
``` 

### 8. Update the version

Manually update the version number following semver.

``` 
git add . 
git commit -m "core(release): bump version to x.y.z" 
git push origin staging 
``` 

### 9. Create a PR from `staging` to `main` 

Verify that:
- Tests pass
- SonarQube check passes
- CI is validated

### 10. Deployment via Render

Deployment is automatically triggered if the version has changed (via the Render webhook).

--- 

## CI/CD GitHub Actions 

- Automatically triggered on `push`/`pull_request` 
- Test coverage ≥ 80% (Codecov)
- SonarQube analysis
- Automatic deployment if version has changed on main

--- 

## Versioning 

Version numbers are defined in:
- Backend : `pom.xml` 
- Frontend : `package.json` 

Follow the semver rules: https://semver.org/ 

- `fix`: bug fix 
- `minor`: new feature without breaking changes
- `major`: breaking change (avoid without explicit approval)

A version change is required to trigger the Render deployment.

--- 

## Running tests 

- Backend : `./mvnw test` (JUnit + MockMvc) 
- Frontend : `npm test` (Karma + Jasmine) 

--- 

## Documentation generation

- Backend : `./mvnw javadoc:javadoc` 
Generated in `/target/site/apidocs` 
- Frontend : `npm run docs` 
Generated in `/docs/typedoc` 

--- 

## Commit conventions

Commits must be professional, clear, in English, and follow the Conventional Commits convention.

Examples:
- feat: added reservation system 
- fix: fixed login error 

--- 

## GitHub release 

1. Go to the repository’s Releases tab

2. Click on "Draft a new release"

3. Fill in:
- Tag version: vX.Y.Z
- Target: main
- Title: Release vX.Y.Z
- Description: structured changelog (features, fixes, tests, security, used endpoints…)

4. Publish: Publish release

--- 

## Best practices

- Never commit .env files, secrets, or passwords
- Always run tests locally before merging
- Maintain test coverage ≥ 80%
- Make small, frequent, descriptive commits
- One Pull Request = one feature
- Follow commit conventions
- Wait for CI + review validation before merging into main

--- 

## Dependency updates

### Backend 
- Check dependencies in `pom.xml` 
- Update with:
``` 
./mvnw versions:display-dependency-updates 
./mvnw versions:use-latest-releases 
``` 

### Frontend 

- Check packages in `package.json` 
- Update with:
``` 
npm outdated 
npm update 
``` 

--- 

## Database migration

At this stage of the project, no structured database migration has been required.

--- 

## Regression testing

The existing unit tests (JUnit / Jasmine) provide a first level of regression testing.
End-to-end tests (Cypress or Postman) may be added later to strengthen coverage of critical scenarios.

--- 

## Updates

This guide is updated regularly to reflect project evolutions and DevOps best practices.
