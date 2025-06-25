package com.mastere_project.vacances_tranquilles.mapper;

import com.mastere_project.vacances_tranquilles.dto.*;
import com.mastere_project.vacances_tranquilles.entity.User;

public interface UserMapper {
    User toUser(RegisterClientDTO dto);
    User toUser(RegisterProviderDTO dto);
    UserDTO toUserDTO(User user);
}
