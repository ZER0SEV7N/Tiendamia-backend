package com.spring.team1.tiendamia.controllers.producto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.team1.tiendamia.models.payload.producto.ProductoRequest;
import com.spring.team1.tiendamia.services.producto.ProductoService;
import com.spring.team1.tiendamia.util.response;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/admin/productos")
public class ProductoController {
    @Autowired ProductoService productoService;

    @PostMapping("/create")
    public ResponseEntity<response<String>> crearProducto(@Valid@RequestBody ProductoRequest dto ) {
        try {
            String mensaje = productoService.crearProductoConVariacion(dto);
            return ResponseEntity.ok(new response<>(true, mensaje, null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new response<>(false, "Error al crear el producto: " + e.getMessage(), null));
        }
    }
    
}
