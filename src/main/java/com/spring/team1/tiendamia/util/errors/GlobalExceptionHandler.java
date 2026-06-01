package com.spring.team1.tiendamia.util.errors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

import com.spring.team1.tiendamia.util.response;

@ControllerAdvice
public class GlobalExceptionHandler {

    //Manejo de Excepciones generales 
    @ExceptionHandler(Exception.class)
    public ResponseEntity<response<Object>> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new response<>(false, "Ocurrió un error inesperado: " + ex.getMessage(), null));
    }

    //Manejo de Excepciones específicas (ejemplo: RuntimeException)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<response<Object>> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new response<>(false, "Error: " + ex.getMessage(), null));
    }

    //Manejo de Excepciones de validación (ejemplo: IllegalArgumentException)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<response<Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new response<>(false, "Error de validación: " + ex.getMessage(), null));
    }

    //Manejo de Excepciones de autenticación (ejemplo: acceso no autorizado)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<response<Object>> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new response<>(false, "No tienes permiso para realizar esta accion", null));
    }
    
    //Manejo de Excepciones por MethodArgumentNotValidException (validación de campos en request)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<response<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        
        //Recorremos todos los campos que fallaron la validación
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        //Retornamos el mapa de errores en la parte de 'data' de tu response
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new response<>(false, "Hay campos inválidos o incompletos", errors));
    }
}
