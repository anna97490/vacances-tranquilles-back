# Changelog - Vacances Tranquilles API

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/).

## [Unreleased]
- 

---

## [0.8.0] - 2025-08-18

### Added
- **Review and Rating System:** "Client" users can now leave a review and a rating on a completed service.

## [0.7.1] - 2025-08-15

### Fixed
- **Reservation API:** The API response when fetching a reservation has been enriched with the details of the related service to avoid extra calls from the front-end.

## [0.7.0] - 2025-08-12

### Added
- **Internal Messaging:** Implemented endpoints for communication between clients and providers after a reservation is confirmed.

## [0.6.0] - 2025-08-11

### Added
- **Payment Integration:** Added endpoints for creating a secure payment session via the Stripe API in test mode.

## [0.5.0] - 2025-08-08

### Added
- **Reservation Module:** Created APIs for the complete booking flow, including management of different reservation statuses (confirmed, cancelled, etc.).

## [0.4.0] - 2025-08-05

### Added
- **Application Monitoring:** Set up an `/actuator/prometheus` endpoint to expose API health and performance metrics.

## [0.3.0] - 2025-08-01

### Added
- **User Profile Management:** Implemented features allowing users to view and update their profile.

### Changed
- **Service Refactoring:** Improved the code structure in the service layer and increased test coverage.

## [0.2.0] - 2025-07-28

### Added
- **Service Management:** CRUD API allowing providers to create, read, update, and delete the services they offer.
- **Security:** Enhanced the security of service endpoints with role-based management.

## [0.1.2] - 2025-07-26

### Fixed
- **CORS Configuration:** Fixed Cross-Origin Resource Sharing configuration to only allow the front-end origin via an environment variable.

## [0.1.1] - 2025-07-26

### Fixed
- **Continuous Integration (CI):** Improved code coverage reports and SonarCloud configuration.

## [0.1.0] - 2025-07-25

### Added
- **Initial MVP:** Set up the Spring Boot project structure.
- **JWT Authentication:** Register (`/register`) and login (`/login`) endpoints with JWT generation.
- **Data Model:** Created the initial entities and DTOs.
- **CI/CD Configuration:** Set up the continuous integration workflow with Maven on GitHub Actions.

[Unreleased]: https://github.com/anna97490/vacances-tranquilles-back/compare/v0.8.0...HEAD
[0.8.0]: https://github.com/anna97490/vacances-tranquilles-back/compare/v0.7.1...v0.8.0
[0.7.1]: https://github.com/anna97490/vacances-tranquilles-back/compare/v0.7.0...v0.7.1
[0.7.0]: https://github.com/anna97490/vacances-tranquilles-back/compare/v0.6.0...v0.7.0
[0.6.0]: https://github.com/anna97490/vacances-tranquilles-back/compare/v0.5.0...v0.6.0
[0.5.0]: https://github.com/anna97490/vacances-tranquilles-back/compare/v0.4.0...v0.5.0
[0.4.0]: https://github.com/anna97490/vacances-tranquilles-back/compare/v0.3.0...v0.4.0
[0.3.0]: https://github.com/anna97490/vacances-tranquilles-back/compare/v0.2.0...v0.3.0
[0.2.0]: https://github.com/anna97490/vacances-tranquilles-back/compare/v0.1.2...v0.2.0
[0.1.2]: https://github.com/anna97490/vacances-tranquilles-back/compare/v0.1.1...v0.1.2
[0.1.1]: https://github.com/anna97490/vacances-tranquilles-back/compare/v0.1.0...v0.1.1
[0.1.0]: https://github.com/anna97490/vacances-tranquilles-back/releases/tag/v0.1.0
