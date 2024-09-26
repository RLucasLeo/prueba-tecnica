package prueba_tecnica.Services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import prueba_tecnica.Auth.AuthResponse;
import prueba_tecnica.Auth.LoginRequest;
import prueba_tecnica.Auth.PhoneRequest;
import prueba_tecnica.Auth.RegisterRequest;
import prueba_tecnica.Entity.Phone;
import prueba_tecnica.Entity.Role;
import prueba_tecnica.Entity.User;
import prueba_tecnica.Repository.UserRepository;
import prueba_tecnica.exception.ValidationUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest loginRequest) {

    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
    );

    User user = userRepository.findByEmail(loginRequest.getEmail())
        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

    String token = jwtService.getToken(user);

    // actualiza el último inicio de sesión
    user.setLastLogin(LocalDateTime.now());
    userRepository.save(user);

    return AuthResponse.builder()
        .id(user.getId())
        .created(user.getCreated())
        .lastLogin(user.getLastLogin())
        .token(token)
        .isActive(user.isActive())
        .name(user.getName())
        .email(user.getEmail())
        .password(user.getPassword())
        .phones(user.getPhones().stream().map(phone -> PhoneRequest.builder()
            .number(phone.getNumber())
            .cityCode(phone.getCityCode())
            .countryCode(phone.getCountryCode())
            .build()
        ).collect(Collectors.toList())) // Mapeo de los teléfonos a la respuesta
        .build();
}

    public AuthResponse register(RegisterRequest registerRequest) {     
        
        if (!ValidationUtils.isValidEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Email invalido");
        }
    
        if (!ValidationUtils.isValidPassword(registerRequest.getPassword())) {
            throw new IllegalArgumentException("La contraseña debe tener entre 8 y 12 caracteres, incluir una mayúscula y dos números");
        }

        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new DuplicateKeyException("Correo electronico ya registrado");
        }


          List<Phone> phones = registerRequest.getPhones().stream()
            .map(phoneRequest -> {
                Phone phone = Phone.builder()
                    .number(phoneRequest.getNumber())
                    .cityCode(phoneRequest.getCityCode())
                    .countryCode(phoneRequest.getCountryCode())
                    .build();
                return phone;
            }).collect(Collectors.toList());

            User user = User.builder()
            .email(registerRequest.getEmail())
            .password(passwordEncoder.encode(registerRequest.getPassword()))
            .name(registerRequest.getName())
            .rol(Role.USER)
            .phones(phones)
            .created(LocalDateTime.now())
            .lastLogin(LocalDateTime.now())
            .isActive(true)
            .build();

        phones.forEach(phone -> phone.setUser(user));

        try {
            userRepository.save(user);
            } catch (Exception e) {
                throw new RuntimeException("Error al registrar el usuario: " + e.getMessage());
            }


        return AuthResponse.builder()
                .id(user.getId())
                .created(user.getCreated())
                .lastLogin(user.getLastLogin())
                .token(jwtService.getToken(user))
                .isActive(user.isActive())
                .build();
    }
    
}
