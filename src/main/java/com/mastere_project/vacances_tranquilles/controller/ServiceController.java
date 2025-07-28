package com.mastere_project.vacances_tranquilles.controller;

import com.mastere_project.vacances_tranquilles.dto.ServiceDTO;
import com.mastere_project.vacances_tranquilles.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.time.LocalTime;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des services proposés par les prestataires.
 * Fournit des endpoints pour la création, la suppression, la récupération, la
 * modification partielle
 * et la recherche de services disponibles.
 */
@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService serviceService;

    /**
     * Crée un nouveau service.
     *
     * @param serviceDTO les informations du service à créer
     * @return le service créé
     */
    @PostMapping
    public ResponseEntity<ServiceDTO> createService(@RequestBody ServiceDTO serviceDTO) {
        return ResponseEntity.ok(serviceService.createService(serviceDTO));
    }

    /**
     * Supprime un service par son identifiant.
     *
     * @param id identifiant du service à supprimer
     * @return une réponse sans contenu si la suppression est réussie
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Récupère un service par son identifiant.
     *
     * @param id identifiant du service
     * @return le service correspondant
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServiceDTO> getService(@PathVariable Long id) {
        return ResponseEntity.ok(serviceService.getServiceById(id));
    }

    /**
     * Récupère tous les services du prestataire connecté.
     *
     * @return liste des services du prestataire connecté
     */
    @GetMapping("/my-services")
    public ResponseEntity<List<ServiceDTO>> getMyServices() {
        return ResponseEntity.ok(serviceService.getMyServices());
    }

    /**
     * Modifie partiellement un service existant. Seuls les champs non nuls du
     * ServiceDTO sont mis à jour.
     * 
     * @param id         identifiant du service à modifier
     * @param serviceDTO DTO contenant les champs à mettre à jour
     * @return le ServiceDTO mis à jour
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ServiceDTO> partialUpdateService(@PathVariable Long id, @RequestBody ServiceDTO serviceDTO) {
        return ResponseEntity.ok(serviceService.partialUpdateService(id, serviceDTO));
    }

    /**
     * Recherche les services disponibles selon les critères et la disponibilité
     * réelle des prestataires.
     *
     * @param category   Catégorie du service
     * @param postalCode Code postal du prestataire
     * @param date       Date souhaitée (obligatoire, format yyyy-MM-dd)
     * @param startTime  Heure de début souhaitée (obligatoire, format HH:mm)
     * @param endTime    Heure de fin souhaitée (obligatoire, format HH:mm)
     * @return Liste des services disponibles
     */
    @GetMapping("/search")
    public List<ServiceDTO> searchAvailableServices(
            @RequestParam String category,
            @RequestParam String postalCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {
        return serviceService.searchAvailableServices(category, postalCode, date, startTime, endTime);
    }
}
