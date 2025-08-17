package com.mastere_project.vacances_tranquilles.exception;

/**
 * Exception levée lorsqu'un avis (review) est introuvable pour un identifiant donné.
 * 
 * Cette erreur peut survenir si l'avis a été supprimé ou si l'identifiant est invalide.
 */
public class ReviewNotFoundException extends RuntimeException {

    /**
     * Construit une nouvelle exception ReviewNotFoundException avec le message spécifié.
     *
     * @param message le message expliquant l'absence de l'avis recherché
     */
    public ReviewNotFoundException(String message) {
        super(message);
    }
}
