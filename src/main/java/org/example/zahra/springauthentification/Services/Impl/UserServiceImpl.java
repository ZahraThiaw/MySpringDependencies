package org.example.zahra.springauthentification.Services.Impl;

import org.example.zahra.springauthentification.Datas.Entities.UserEntity;
import org.example.zahra.springauthentification.Datas.Repositories.UserRepository;
import org.example.zahra.springauthentification.Mappers.GenericMapper;
import org.example.zahra.springauthentification.Services.UserService;
import org.example.zahra.springauthentification.Web.Dtos.Responses.UserResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends BaseServiceImpl<UserEntity, Long> implements UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final GenericMapper<UserEntity, ?, UserResponseDTO> userMapper;

    public UserServiceImpl(UserRepository userRepository, JwtService jwtService, GenericMapper<UserEntity, ?, UserResponseDTO> userMapper) {
        super(userRepository);
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponseDTO getConnectedUserProfile() {
        String email = jwtService.extractUsernameFromToken(); // Récupérer l'e-mail depuis le JWT
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'e-mail : " + email));
        return userMapper.toResponseDto(user); // Mapper vers UserResponseDTO
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
