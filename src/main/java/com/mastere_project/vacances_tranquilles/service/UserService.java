package com.mastere_project.vacances_tranquilles.service;

import com.mastere_project.vacances_tranquilles.dto.RegisterClientDTO;
import com.mastere_project.vacances_tranquilles.dto.RegisterProviderDTO;

public interface UserService {
    void registerClient(RegisterClientDTO registerClientDTO);
    void registerProvider(RegisterProviderDTO registerProviderDTO);
}
