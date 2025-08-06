package com.mastere_project.vacances_tranquilles.service.impl;

import com.mastere_project.vacances_tranquilles.dto.ServiceDTO;
import com.mastere_project.vacances_tranquilles.entity.Service;
import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.mapper.ServiceMapper;
import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import com.mastere_project.vacances_tranquilles.repository.ServiceRepository;
import com.mastere_project.vacances_tranquilles.repository.UserRepository;
import com.mastere_project.vacances_tranquilles.service.ServiceService;
import com.mastere_project.vacances_tranquilles.util.jwt.SecurityUtils;
import com.mastere_project.vacances_tranquilles.exception.ServiceNotFoundException;

import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;
    private final UserRepository userRepository;

    private static final String SERVICE_NOT_FOUND_MSG = "Service not found";

    /**
     * Constructeur avec injection de dépendances.
     *
     * @param serviceRepository repository pour les services
     * @param serviceMapper     mapper pour la conversion DTO/Entity
     * @param userRepository    repository pour les utilisateurs
     */
    public ServiceServiceImpl(ServiceRepository serviceRepository,
            ServiceMapper serviceMapper,
            UserRepository userRepository) {
        this.serviceRepository = serviceRepository;
        this.serviceMapper = serviceMapper;
        this.userRepository = userRepository;
    }

    /**
     * Crée un nouveau service pour le prestataire actuellement connecté.
     *
     * @param serviceDTO les informations du service à créer
     * @return le service créé
     * @throws ServiceNotFoundException si l'utilisateur connecté n'est pas trouvé
     * @throws AccessDeniedException    si l'utilisateur n'est pas un prestataire
     */
    @Override
    public ServiceDTO createService(ServiceDTO serviceDTO) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        User provider = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ServiceNotFoundException("Utilisateur non trouvé"));

        // Vérification du rôle : seuls les PROVIDER peuvent créer des services
        if (provider.getUserRole() != UserRole.PROVIDER) {
            throw new AccessDeniedException("Seuls les prestataires peuvent créer des services");
        }

        Service service = serviceMapper.toEntity(serviceDTO);
        service.setProvider(provider);
        Service saved = serviceRepository.save(service);
        return serviceMapper.toDto(saved);
    }

    /**
     * Supprime un service si l'utilisateur connecté en est le propriétaire.
     *
     * @param id identifiant du service à supprimer
     * @throws org.springframework.security.access.AccessDeniedException
     * si l'utilisateur n'est pas le propriétaire du service
     * @throws ServiceNotFoundException
     * si le service n'existe pas
     */
    @Override
    public void deleteService(Long id) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException(SERVICE_NOT_FOUND_MSG));
        Long currentUserId = SecurityUtils.getCurrentUserId();

        if (service.getProvider() == null || !service.getProvider().getId().equals(currentUserId)) {
            throw new AccessDeniedException("Vous n'êtes pas autorisé à modifier ce service.");
        }
        serviceRepository.deleteById(id);
    }

    /**
     * Récupère un service par son identifiant.
     *
     * @param id identifiant du service
     * @return le service correspondant
     * @throws ServiceNotFoundException si le service n'existe pas
     */
    @Override
    public ServiceDTO getServiceById(Long id) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException(SERVICE_NOT_FOUND_MSG));
        return serviceMapper.toDto(service);
    }

    /**
     * Récupère tous les services du prestataire connecté.
     *
     * @return liste des services du prestataire connecté
     */
    @Override
    public List<ServiceDTO> getMyServices() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        User provider = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ServiceNotFoundException("Utilisateur non trouvé"));
        UserRole currentRole = provider.getUserRole();
        if (currentRole != UserRole.PROVIDER) {
            throw new AccessDeniedException("Seuls les prestataires peuvent accéder à leurs services");
        }

        List<Service> services = serviceRepository.findByProviderId(currentUserId);
        List<ServiceDTO> dtoList = new ArrayList<>();
        for (Service service : services) {
            dtoList.add(serviceMapper.toDto(service));
        }
        return dtoList;
    }

    /**
     * Met à jour partiellement un service existant (seuls les champs non nuls du
     * DTO sont modifiés).
     *
     * @param id         identifiant du service à modifier
     * @param serviceDTO DTO contenant les champs à mettre à jour
     * @return le ServiceDTO mis à jour
     * @throws ServiceNotFoundException si le service n'existe pas
     */
    @Override
    public ServiceDTO partialUpdateService(Long id, ServiceDTO serviceDTO) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException(SERVICE_NOT_FOUND_MSG));

        Long currentUserId = SecurityUtils.getCurrentUserId();

        if (service.getProvider() == null || !service.getProvider().getId().equals(currentUserId)) {
            throw new AccessDeniedException("Vous n'êtes pas autorisé à modifier ce service.");
        }

        if (serviceDTO.getTitle() != null)
            service.setTitle(serviceDTO.getTitle());
        if (serviceDTO.getDescription() != null)
            service.setDescription(serviceDTO.getDescription());
        if (serviceDTO.getCategory() != null)
            service.setCategory(serviceDTO.getCategory());
        if (serviceDTO.getPrice() != null)
            service.setPrice(serviceDTO.getPrice());

        Service saved = serviceRepository.save(service);
        return serviceMapper.toDto(saved);
    }

    /**
     * Recherche les services disponibles selon les critères et la disponibilité
     * réelle des prestataires.
     *
     * @param category   Catégorie du service (obligatoire)
     * @param postalCode Code postal du prestataire (obligatoire)
     * @param date       Date souhaitée (obligatoire)
     * @param startTime  Heure de début souhaitée (obligatoire)
     * @param endTime    Heure de fin souhaitée (obligatoire)
     * @return Liste des services disponibles
     * @throws IllegalArgumentException si les paramètres sont invalides
     */
    @Override
    public List<ServiceDTO> searchAvailableServices(
            String category,
            String postalCode,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime) {
        // Validation des paramètres
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("La catégorie du service est obligatoire.");
        }
        if (postalCode == null || postalCode.isBlank()) {
            throw new IllegalArgumentException("Le code postal est obligatoire.");
        }
        if (date == null || startTime == null || endTime == null) {
            throw new IllegalArgumentException("La date et les horaires sont obligatoires.");
        }
        if (startTime.compareTo(endTime) >= 0) {
            throw new IllegalArgumentException("L'heure de début doit être antérieure à l'heure de fin.");
        }
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La date ne peut pas être dans le passé.");
        }

        List<Service> services = serviceRepository
                .findAvailableServices(
                        category, postalCode, date, startTime, endTime);

        List<ServiceDTO> dtoList = new ArrayList<>();
        for (Service service : services) {
            dtoList.add(serviceMapper.toDto(service));
        }
        return dtoList;
    }
}