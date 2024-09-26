package prueba_tecnica.Auth;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    
    private String name;

    @NotBlank(message = "El correo es obligatorio")
    @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$", message = "Formato de correo inválido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Pattern(regexp = "^(?=.*[A-Z])(?=(?:\\D*\\d){2}\\D*$)[a-zA-Z0-9]{8,12}$", 
    message = "Clave inválida: Debe tener una mayúscula, solo dos números, y de 8 a 12 caracteres")
    private String password;
    
    private List<PhoneRequest> phones;

    public List<PhoneRequest> getPhones() {
        return phones;}
    
    
}
