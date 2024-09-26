package prueba_tecnica.exception;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)//argumentos invalidos
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            Collections.singletonList(new ErrorDetail(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), ex.getMessage()))
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateKeyException.class)//email duplicado
public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(DuplicateKeyException ex) {
    ErrorResponse errorResponse = new ErrorResponse(
        Collections.singletonList(new ErrorDetail(LocalDateTime.now(), HttpStatus.CONFLICT.value(), ex.getMessage()))
    );

    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
}

    @ExceptionHandler(Exception.class) // excepciones no manjeadas
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            Collections.singletonList(new ErrorDetail(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "Ocurrió un error inesperado: " + ex.getMessage()))
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
class ErrorResponse {
    private List<ErrorDetail> error;

    public ErrorResponse(List<ErrorDetail> error) {
        this.error = error;
    }

    public List<ErrorDetail> getError() {
        return error;
    }
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class ErrorDetail {
    private LocalDateTime timestamp;
    private int codigo;
    private String detail;

}
