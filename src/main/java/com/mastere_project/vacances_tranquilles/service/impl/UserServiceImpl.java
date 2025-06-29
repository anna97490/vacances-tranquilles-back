package com.mastere_project.vacances_tranquilles.service.impl;

import com.mastere_project.vacances_tranquilles.dto.*;
import com.mastere_project.vacances_tranquilles.entity.User;
import com.mastere_project.vacances_tranquilles.exception.EmailAlreadyExistsException;
import com.mastere_project.vacances_tranquilles.exception.EmailNotFoundException;
import com.mastere_project.vacances_tranquilles.exception.MissingFieldException;
import com.mastere_project.vacances_tranquilles.exception.WrongPasswordException;
import com.mastere_project.vacances_tranquilles.mapper.UserMapper;
import com.mastere_project.vacances_tranquilles.model.enums.UserRole;
import com.mastere_project.vacances_tranquilles.repository.UserRepository;
import com.mastere_project.vacances_tranquilles.service.UserService;
import com.mastere_project.vacances_tranquilles.util.jwt.JwtConfig;

import lombok.RequiredArgsConstructor;

import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtConfig jwt;

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

  
    @Override
    public LoginResponseDTO login(UserDTO userDTO) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(userDTO.getEmail());

            if (optionalUser.isEmpty()) {
                throw new EmailNotFoundException("Aucun compte trouvé pour l'email : " + userDTO.getEmail());
            }

            User user = optionalUser.get();

            if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
                throw new WrongPasswordException("Mot de passe incorrect pour l'email : " + userDTO.getEmail());
            }

            String token = jwt.generateToken(user.getEmail());

            return new LoginResponseDTO(token, user.getUserRole());

        } catch (EmailNotFoundException | WrongPasswordException e) {
            throw e; // On laisse les exceptions connues remonter pour être captées par le
                     // ControllerAdvice
        } catch (Exception e) {
            throw new RuntimeException("Erreur serveur inattendue lors de la tentative de connexion", e);
        }
    }
}
