package com.mastere_project.vacances_tranquilles.repository;

import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository Spring Data JPA pour l'entité User.
 * Fournit des méthodes de recherche utilisateur par email.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Recherche un utilisateur par son email.
     * 
     * @param email l'email à rechercher
     * @return un Optional contenant l'utilisateur s'il existe
     */
    Optional<User> findByEmail(String email);

    /**
     * Vérifie l'existence d'un utilisateur par email.
     * 
     * @param email l'email à vérifier
     * @return true si un utilisateur existe avec cet email, false sinon
     */
    boolean existsByEmail(String email);

    /**
     * Récupère tous les utilisateurs ayant le rôle client.
     * 
     * @return la liste des clients
     */
    List<User> findByUserRole(UserRole userRole);

    /**
     * Récupère tous les clients.
     * 
     * @return la liste des clients
     */
    default List<User> findAllClients() {
        return findByUserRole(UserRole.CLIENT);
    }

    /**
     * Récupère tous les prestataires.
     * 
     * @return la liste des prestataires
     */
    default List<User> findAllProviders() {
        return findByUserRole(UserRole.PROVIDER);
    }

    /**
     * Récupère tous les clients non anonymisés.
     * 
     * @return la liste des clients actifs
     */
    default List<User> findAllActiveClients() {
        return findByUserRoleAndIsAnonymizedFalse(UserRole.CLIENT);
    }

    /**
     * Récupère tous les prestataires non anonymisés.
     * 
     * @return la liste des prestataires actifs
     */
    default List<User> findAllActiveProviders() {
        return findByUserRoleAndIsAnonymizedFalse(UserRole.PROVIDER);
    }

    /**
     * Récupère tous les utilisateurs ayant un rôle spécifique et non anonymisés.
     * 
     * @param userRole le rôle utilisateur
     * @return la liste des utilisateurs actifs
     */
    List<User> findByUserRoleAndIsAnonymizedFalse(UserRole userRole);
}
