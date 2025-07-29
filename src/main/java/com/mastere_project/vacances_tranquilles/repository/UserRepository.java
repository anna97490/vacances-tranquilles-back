package com.mastere_project.vacances_tranquilles.repository;

import com.mastere_project.vacances_tranquilles.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
     * Recherche un utilisateur par son identifiant.
     *
     * @param id l'identifiant de l'utilisateur
     * @return un Optional contenant l'utilisateur s'il existe
     */
    Optional<User> findById(Long id);
}
