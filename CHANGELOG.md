# Journal des Versions - API Vacances Tranquilles

Toutes les modifications notables de ce projet sont documentées dans ce fichier.

Le format est basé sur [Keep a Changelog](https://keepachangelog.com/fr/1.0.0/),
et ce projet adhère au [Versionnage Sémantique](https://semver.org/lang/fr/).

## [Unreleased]
- Prochaines évolutions à venir.

---

## [0.8.0] - 2025-08-18

### Added
- **Système d'avis et notations :** Les utilisateurs "Particulier" peuvent désormais laisser un avis et une note sur une prestation terminée.

## [0.7.1] - 2025-08-15

### Changed
- **API de Réservation :** La réponse de l'API lors de la récupération d'une réservation a été enrichie avec les détails du service concerné pour éviter des appels supplémentaires depuis le front-end.

## [0.7.0] - 2025-08-12

### Added
- **Messagerie interne :** Implémentation des endpoints permettant la communication entre particuliers et prestataires après une réservation confirmée.

## [0.6.0] - 2025-08-11

### Added
- **Intégration du paiement :** Ajout des endpoints pour la création d'une session de paiement sécurisé via l'API Stripe en mode test.

## [0.5.0] - 2025-08-08

### Added
- **Module de Réservation :** Création des API pour le parcours de réservation complet, incluant la gestion des différents statuts de réservation (confirmée, annulée, etc.).

## [0.4.0] - 2025-08-05

### Added
- **Supervision de l'application :** Mise en place d'un endpoint `/actuator/prometheus` pour exposer les métriques de performance et de santé de l'API.

## [0.3.0] - 2025-08-01

### Added
- **Gestion des profils utilisateurs :** Implémentation des fonctionnalités permettant aux utilisateurs de consulter et mettre à jour leur profil.

### Changed
- **Refactoring des services :** Amélioration de la structure du code dans la couche service et augmentation de la couverture de test.

## [0.2.0] - 2025-07-28

### Added
- **Gestion des services :** API CRUD permettant aux prestataires de créer, lire, mettre à jour et supprimer les services qu'ils proposent.
- **Sécurité :** Renforcement de la sécurité des endpoints de service avec une gestion des rôles.

## [0.1.2] - 2025-07-26

### Fixed
- **Configuration CORS :** Correction de la configuration Cross-Origin Resource Sharing pour autoriser uniquement l'origine du front-end via une variable d'environnement.

## [0.1.1] - 2025-07-26

### Fixed
- **Intégration Continue (CI) :** Amélioration des rapports de couverture de code et de la configuration SonarCloud.

## [0.1.0] - 2025-07-25

### Added
- **MVP Initial :** Mise en place de la structure du projet Spring Boot.
- **Authentification JWT :** Endpoints d'inscription (`/register`) et de connexion (`/login`) avec génération de jetons JWT.
- **Modèle de Données :** Création des premières entités et DTOs.
- **Configuration CI/CD :** Mise en place du workflow d'intégration continue avec Maven sur GitHub Actions.

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