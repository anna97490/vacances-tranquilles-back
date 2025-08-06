package com.mastere_project.vacances_tranquilles.mapper.impl;

import com.mastere_project.vacances_tranquilles.dto.ServiceDTO;
import com.mastere_project.vacances_tranquilles.entity.Service;
import com.mastere_project.vacances_tranquilles.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ServiceMapperImplTest {

    private ServiceMapperImpl mapper;

    @BeforeEach
    void setUp() {
        mapper = new ServiceMapperImpl();
    }

    @Test
    @DisplayName("toDto doit retourner null si Service est null")
    void toDto_ReturnsNull_WhenServiceIsNull() {
        assertNull(mapper.toDto(null));
    }

    @Test
    @DisplayName("toDto convertit tous les champs et providerId (provider non null)")
    void toDto_ReturnsDtoWithProviderId_WhenProviderIsNotNull() {
        Service service = new Service();
        service.setId(10L);
        service.setTitle("T");
        service.setDescription("Desc");
        service.setCategory("Cat");
        service.setPrice(BigDecimal.valueOf(20.00));

        User provider = new User();
        provider.setId(99L);
        service.setProvider(provider);

        ServiceDTO dto = mapper.toDto(service);

        assertEquals(service.getId(), dto.getId());
        assertEquals("T", dto.getTitle());
        assertEquals("Desc", dto.getDescription());
        assertEquals("Cat", dto.getCategory());
        assertEquals(BigDecimal.valueOf(20.00), dto.getPrice());
        assertEquals(99L, dto.getProviderId());
    }

    @Test
    @DisplayName("toDto gère le cas où le provider est null")
    void toDto_ReturnsDtoWithNullProviderId_WhenProviderIsNull() {
        Service service = new Service();
        service.setId(1L);
        ServiceDTO dto = mapper.toDto(service);
        assertNull(dto.getProviderId());
    }

    @Test
    @DisplayName("toEntity doit retourner null si DTO est null")
    void toEntity_ReturnsNull_WhenDtoIsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    @DisplayName("toEntity convertit tous les champs sans provider (géré par le service)")
    void toEntity_ReturnsServiceWithoutProvider_WhenProviderIdExists() {
        ServiceDTO dto = new ServiceDTO();
        dto.setId(11L);
        dto.setTitle("Serv");
        dto.setDescription("Desc");
        dto.setCategory("Cat");
        dto.setPrice(BigDecimal.valueOf(80.00));
        dto.setProviderId(123L);

        Service service = mapper.toEntity(dto);

        assertEquals(dto.getId(), service.getId());
        assertEquals("Serv", service.getTitle());
        assertEquals("Desc", service.getDescription());
        assertEquals("Cat", service.getCategory());
        assertEquals(BigDecimal.valueOf(80.00), service.getPrice());
        assertNull(service.getProvider()); // Le provider est géré par le service
    }

    @Test
    @DisplayName("toEntity gère le cas où providerId est null")
    void toEntity_ReturnsServiceWithNullProvider_WhenProviderIdIsNull() {
        ServiceDTO dto = new ServiceDTO();
        dto.setId(1L);
        dto.setProviderId(null);

        Service service = mapper.toEntity(dto);

        assertNull(service.getProvider());
    }
}