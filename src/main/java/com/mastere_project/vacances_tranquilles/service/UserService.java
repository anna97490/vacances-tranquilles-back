package com.mastere_project.vacances_tranquilles.service;

import com.mastere_project.vacances_tranquilles.dto.*;
public interface UserService {
    void registerClient(RegisterClientDTO registerClientDTO);
    void registerProvider(RegisterProviderDTO registerProviderDTO);
    LoginResponseDTO login(UserDTO userDTO);
}
