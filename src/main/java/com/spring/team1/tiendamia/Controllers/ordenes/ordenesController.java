package com.spring.team1.tiendamia.Controllers.ordenes;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.team1.tiendamia.Models.Ordenes.Ordenes;
import com.spring.team1.tiendamia.Models.Ordenes.DTO.CheckoutRequest;
import com.spring.team1.tiendamia.Models.payload.response;
import com.spring.team1.tiendamia.Services.ordenes.OrdenesServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/ordenes")
public class ordenesController {

    @Autowired
    private OrdenesServices ordenesServices;

    //Endpoint para procesar el checkout y crear la orden
    //Post: /api/ordenes/crear
    //El request body debe contener:
    //{
    //  "direccion": {
    //    "direccion": "Av. Siempre Viva 123",
    //    "distrito": "Springfield",
    //    "provincia": "Springfield",
    //    "departamento": "Springfield",
    //    "referencia": "Frente al parque"
    //  },
    //  "pago": {
    //    "pasarela": "Simulada",
    //    "customer_token": "cus_123456789",
    //    "ultimos_cuatro": "4242",
    //    "marca_tarjeta_id": 1
    //  },
    //  "total": 99.99
    //}
    @PostMapping("/crear")
    public ResponseEntity<response<Ordenes>> crearOrden(Authentication auth, @RequestBody CheckoutRequest request) {
        if(auth == null || !auth.isAuthenticated()) 
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new response<>(false, "Usuario no autenticado", null));
        
        try{ 
            String correo = auth.getName();
            Ordenes orden = ordenesServices.procesarCheckout(correo, request);
            return ResponseEntity.ok(new response<>(true, "Orden creada exitosamente", orden));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new response<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new response<>(false, "Error al crear la orden", null));
        }

    }
    

}
