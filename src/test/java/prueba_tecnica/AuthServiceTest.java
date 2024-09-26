package prueba_tecnica;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import prueba_tecnica.Auth.AuthResponse;
import prueba_tecnica.Auth.LoginRequest;
import prueba_tecnica.Auth.PhoneRequest;
import prueba_tecnica.Auth.RegisterRequest;
import prueba_tecnica.Entity.Phone;
import prueba_tecnica.Entity.User;
import prueba_tecnica.Repository.UserRepository;
import prueba_tecnica.Services.AuthService;
import prueba_tecnica.Services.JwtService;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
     @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private LoginRequest loginRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        loginRequest = new LoginRequest("usuario@ejemplo.com", "Tucasa12");
    }

    @Test
    public void testLogin() {

        User user = new User();
        user.setEmail(loginRequest.getEmail());
        user.setPassword(loginRequest.getPassword());
        

        List<Phone> phones = new ArrayList<>();
        phones.add(new Phone(null, 123456789, 1, "25", user)); 
        user.setPhones(phones);


        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.getToken(user)).thenReturn("fake-jwt");


        AuthResponse response = authService.login(loginRequest);


        assertNotNull(response);
        assertEquals("usuario@ejemplo.com", response.getEmail());
        assertEquals("fake-jwt", response.getToken());
    }

    @Test
    public void testRegister() {

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("Password12");
        registerRequest.setName("Test User");

        List<PhoneRequest> phoneRequests = new ArrayList<>();
        PhoneRequest phoneRequest = new PhoneRequest();
        phoneRequest.setNumber(123456789);
        phoneRequest.setCityCode(1);
        phoneRequest.setCountryCode("25");
        phoneRequests.add(phoneRequest);
        registerRequest.setPhones(phoneRequests);


        User userMock = User.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .password("Password12")
                .name("Test User")
                .phones(new ArrayList<>())
                .created(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .isActive(true)
                .build();

        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(userMock); // Mockea el save para devolver el userMock


        AuthResponse response = authService.register(registerRequest);

        assertNotNull(userMock.getId()); // Verifica que el ID no sea nulo
        assertEquals("test@example.com", userMock.getEmail());
        assertEquals("Test User", userMock.getName());
        assertTrue(response.isActive());
    }

}
