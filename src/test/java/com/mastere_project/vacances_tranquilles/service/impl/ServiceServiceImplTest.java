package com.mastere_project.vacances_tranquilles.service.impl;

import com.mastere_project.vacances_tranquilles.dto.ServiceDTO;
import com.mastere_project.vacances_tranquilles.entity.Service;
import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.exception.ServiceNotFoundException;
import com.mastere_project.vacances_tranquilles.mapper.ServiceMapper;
import com.mastere_project.vacances_tranquilles.repository.ServiceRepository;
import com.mastere_project.vacances_tranquilles.repository.UserRepository;
import com.mastere_project.vacances_tranquilles.util.jwt.SecurityUtils;
import com.mastere_project.vacances_tranquilles.model.enums.UserRole;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.security.access.AccessDeniedException;

@ExtendWith(MockitoExtension.class)
class ServiceServiceImplTest {
    @Mock
    private ServiceRepository serviceRepository;
    @Mock
    private ServiceMapper serviceMapper;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private ServiceServiceImpl serviceService;

    MockedStatic<SecurityUtils> securityUtilsMock;

    @BeforeEach
    void setUp() {
        securityUtilsMock = Mockito.mockStatic(SecurityUtils.class);
    }

    @AfterEach
    void tearDown() {
        securityUtilsMock.close();
    }

    @Test
    void createService_providerIsCurrentUser() {
        ServiceDTO dto = new ServiceDTO();
        User provider = new User();
        provider.setId(1L);
        provider.setUserRole(UserRole.PROVIDER);
        Service entity = new Service();
        entity.setProvider(provider);
        Service saved = new Service();
        saved.setProvider(provider);
        ServiceDTO expectedDto = new ServiceDTO();

        when(SecurityUtils.getCurrentUserId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(provider));
        when(serviceMapper.toEntity(dto)).thenReturn(entity);
        when(serviceRepository.save(entity)).thenReturn(saved);
        when(serviceMapper.toDto(saved)).thenReturn(expectedDto);

        ServiceDTO result = serviceService.createService(dto);
        assertEquals(expectedDto, result);
        verify(serviceRepository).save(entity);
    }

    @Test
    void createService_userNotFound_throwsException() {
        when(SecurityUtils.getCurrentUserId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        ServiceDTO dto = new ServiceDTO();
        assertThrows(ServiceNotFoundException.class, () -> serviceService.createService(dto));
    }

    @Test
    void createService_userNotProvider_throwsAccessDenied() {
        User client = new User();
        client.setId(1L);
        client.setUserRole(UserRole.CLIENT);

        when(SecurityUtils.getCurrentUserId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(client));
        ServiceDTO dto = new ServiceDTO();

        assertThrows(AccessDeniedException.class, () -> serviceService.createService(dto));
    }

    @Test
    void partialUpdateService_updatesAllFields_whenFieldsAreNotNull() {
        // Préparation
        Service service = new Service();
        User provider = new User();
        provider.setId(1L);
        service.setProvider(provider);

        when(serviceRepository.findById(1L)).thenReturn(Optional.of(service));
        when(SecurityUtils.getCurrentUserId()).thenReturn(1L);

        ServiceDTO dto = new ServiceDTO();
        dto.setTitle("Nouveau titre");
        dto.setDescription("Nouvelle description");
        dto.setCategory("Nouvelle catégorie");
        dto.setPrice(BigDecimal.valueOf(99.99));

        Service savedService = new Service();
        savedService.setProvider(provider);
        when(serviceRepository.save(any(Service.class))).thenReturn(savedService);
        when(serviceMapper.toDto(savedService)).thenReturn(new ServiceDTO());

        // Exécution
        ServiceDTO result = serviceService.partialUpdateService(1L, dto);

        // Vérification
        verify(serviceRepository).findById(1L);
        verify(serviceRepository).save(service);
        assertNotNull(result);
        assertEquals(provider, service.getProvider());
        assertEquals("Nouveau titre", service.getTitle());
        assertEquals("Nouvelle description", service.getDescription());
        assertEquals("Nouvelle catégorie", service.getCategory());
        assertEquals(BigDecimal.valueOf(99.99), service.getPrice());
    }

    @Test
    void partialUpdateService_doesNotUpdateFields_whenDtoFieldsAreNull() {
        Service service = new Service();
        User provider = new User();
        provider.setId(1L);
        service.setProvider(provider);

        when(serviceRepository.findById(1L)).thenReturn(Optional.of(service));
        when(SecurityUtils.getCurrentUserId()).thenReturn(1L);

        ServiceDTO dto = new ServiceDTO(); // Tous les champs sont null

        when(serviceRepository.save(any(Service.class))).thenReturn(service);
        when(serviceMapper.toDto(service)).thenReturn(new ServiceDTO());

        serviceService.partialUpdateService(1L, dto);

        verify(serviceRepository).save(service);
        // Tous les champs de service restent null
        assertNull(service.getTitle());
        assertNull(service.getDescription());
        assertNull(service.getCategory());
        assertNull(service.getPrice());
    }

    @Test
    void searchAvailableServices_returnsList_whenParametersAreValid() {
        String category = "Entretien";
        String postalCode = "75001";
        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime start = LocalTime.of(10, 0);
        LocalTime end = LocalTime.of(12, 0);

        List<Service> services = List.of(new Service());
        when(serviceRepository.findAvailableServices(category, postalCode, date, start, end)).thenReturn(services);
        when(serviceMapper.toDto(any(Service.class))).thenReturn(new ServiceDTO());

        List<ServiceDTO> result = serviceService.searchAvailableServices(category, postalCode, date, start, end);

        assertEquals(1, result.size());
        verify(serviceRepository).findAvailableServices(category, postalCode, date, start, end);
    }

    @Test
    void searchAvailableServices_throwsException_whenStartAfterEnd() {
        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime start = LocalTime.of(14, 0);
        LocalTime end = LocalTime.of(12, 0); // start > end

        assertThrows(IllegalArgumentException.class,
                () -> serviceService.searchAvailableServices("Cat", "75001", date, start, end));
    }

    @Test
    void searchAvailableServices_throwsException_whenDateIsInPast() {
        LocalDate date = LocalDate.now().minusDays(1);
        assertThrows(IllegalArgumentException.class,
                () -> serviceService.searchAvailableServices("Cat", "75001", date, LocalTime.NOON, LocalTime.MIDNIGHT));
    }

    @Test
    void deleteService_notOwner_throwsAccesDenied() {
        Service service = new Service();
        User provider = new User();
        provider.setId(2L);
        service.setProvider(provider);
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(service));
        when(SecurityUtils.getCurrentUserId()).thenReturn(1L);
        assertThrows(AccessDeniedException.class, () -> serviceService.deleteService(1L));
    }

    @Test
    void deleteService_serviceNotFound_throwsException() {
        when(serviceRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ServiceNotFoundException.class, () -> serviceService.deleteService(1L));
    }

    @Test
    void getServiceById_serviceNotFound_throwsException() {
        when(serviceRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ServiceNotFoundException.class, () -> serviceService.getServiceById(1L));
    }

    @Test
    void getMyServices_shouldReturnMappedServiceDTOs() {
        // Arrange
        Long currentUserId = 1L;
        Service service1 = new Service();
        service1.setId(100L);
        Service service2 = new Service();
        service2.setId(101L);
        List<Service> services = List.of(service1, service2);

        User provider = new User();
        provider.setId(currentUserId);
        provider.setUserRole(UserRole.PROVIDER);

        ServiceDTO dto1 = new ServiceDTO();
        dto1.setId(100L);
        ServiceDTO dto2 = new ServiceDTO();
        dto2.setId(101L);

        when(SecurityUtils.getCurrentUserId()).thenReturn(currentUserId);
        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(provider));
        when(serviceRepository.findByProviderId(currentUserId)).thenReturn(services);
        when(serviceMapper.toDto(service1)).thenReturn(dto1);
        when(serviceMapper.toDto(service2)).thenReturn(dto2);

        // Act
        List<ServiceDTO> result = serviceService.getMyServices();

        // Assert
        assertEquals(2, result.size());
        assertEquals(dto1.getId(), result.get(0).getId());
        assertEquals(dto2.getId(), result.get(1).getId());
        verify(serviceRepository).findByProviderId(currentUserId);
        verify(serviceMapper).toDto(service1);
        verify(serviceMapper).toDto(service2);
    }

    @Test
    void partialUpdateService_notOwner_throwsAccesDenied() {
        Service service = new Service();
        User provider = new User();
        provider.setId(2L);
        service.setProvider(provider);
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(service));
        when(SecurityUtils.getCurrentUserId()).thenReturn(1L);
        ServiceDTO dto = new ServiceDTO();
        assertThrows(AccessDeniedException.class, () -> serviceService.partialUpdateService(1L, dto));
    }

    @Test
    void partialUpdateService_serviceNotFound_throwsException() {
        when(serviceRepository.findById(1L)).thenReturn(Optional.empty());
        ServiceDTO dto = new ServiceDTO();
        assertThrows(ServiceNotFoundException.class, () -> serviceService.partialUpdateService(1L, dto));
    }

    @Test
    void searchAvailableServices_throwsException_whenCategoryIsNull() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        assertThrows(IllegalArgumentException.class,
                () -> serviceService.searchAvailableServices(null, "75001", tomorrow, LocalTime.NOON,
                        LocalTime.MIDNIGHT));
    }

    @Test
    void searchAvailableServices_throwsException_whenCategoryIsBlank() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        assertThrows(IllegalArgumentException.class,
                () -> serviceService.searchAvailableServices("", "75001", tomorrow, LocalTime.NOON,
                        LocalTime.MIDNIGHT));
    }

    @Test
    void searchAvailableServices_throwsException_whenPostalCodeIsNull() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        assertThrows(IllegalArgumentException.class,
                () -> serviceService.searchAvailableServices("Cat", null, tomorrow, LocalTime.NOON,
                        LocalTime.MIDNIGHT));
    }

    @Test
    void searchAvailableServices_throwsException_whenDateIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> serviceService.searchAvailableServices("Cat", "75001", null, LocalTime.NOON, LocalTime.MIDNIGHT));
    }
}