package com.mastere_project.vacances_tranquilles.service.impl;

import com.mastere_project.vacances_tranquilles.dto.ServiceDTO;
import com.mastere_project.vacances_tranquilles.entity.Service;
import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.exception.ServiceNotFoundException;
import com.mastere_project.vacances_tranquilles.mapper.ServiceMapper;
import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import com.mastere_project.vacances_tranquilles.repository.ServiceRepository;
import com.mastere_project.vacances_tranquilles.repository.UserRepository;
import com.mastere_project.vacances_tranquilles.util.jwt.SecurityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
}