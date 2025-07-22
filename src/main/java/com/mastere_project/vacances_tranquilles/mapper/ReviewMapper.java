package com.mastere_project.vacances_tranquilles.mapper;

import com.mastere_project.vacances_tranquilles.entity.Review;
import com.mastere_project.vacances_tranquilles.dto.ReviewDTO;

/**
 * Interface de mappage pour convertir les entités {@link Review} en {@link ReviewDTO} et inversement.
 */
public interface ReviewMapper {

    /**
     * Convertit une entité Review en ReviewDTO.
     *
     * @param review l'entité Review à convertir
     * @return l'objet DTO correspondant ou {@code null} si l'entrée est {@code null}
     */
    ReviewDTO toDTO(Review review);

    /**
     * Convertit un objet ReviewDTO en entité Review.
     *
     * @param dto l'objet DTO à convertir
     * @return l'entité correspondante ou null si l'entrée est null
     */
    Review toEntity(ReviewDTO dto);
}
