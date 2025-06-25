package com.mastere_project.vacances_tranquilles.service.impl;

import com.mastere_project.vacances_tranquilles.dto.RegisterClientDTO;
import com.mastere_project.vacances_tranquilles.dto.RegisterProviderDTO;
import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.exception.EmailAlreadyExistsException;
import com.mastere_project.vacances_tranquilles.exception.MissingFieldException;
import com.mastere_project.vacances_tranquilles.mapper.UserMapper;
import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import com.mastere_project.vacances_tranquilles.repository.UserRepository;
import com.mastere_project.vacances_tranquilles.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public void registerClient(RegisterClientDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException("Un compte avec cet email existe déjà.");
        }

        User user = userMapper.toUser(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void registerProvider(RegisterProviderDTO dto) {
        if (dto.getCompanyName() == null || dto.getCompanyName().isBlank()) {
            throw new MissingFieldException("Le nom de la société est obligatoire pour les prestataires.");
        }

        if (dto.getSiretSiren() == null || dto.getSiretSiren().isBlank()) {
            throw new MissingFieldException("Le numéro SIRET/SIREN est obligatoire pour les prestataires.");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException("Un compte avec cet email existe déjà.");
        }

        User user = userMapper.toUser(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
