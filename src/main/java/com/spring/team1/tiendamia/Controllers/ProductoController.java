package com.spring.team1.tiendamia.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.team1.tiendamia.Models.payload.producto.ProductoRequest;
import com.spring.team1.tiendamia.Models.productos.Productos;
import com.spring.team1.tiendamia.Util.ResponseApi;
import com.spring.team1.tiendamia.services.producto.ProductoService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/admin/productos")
public class ProductoController {
    @Autowired ProductoService productoService;

    @PostMapping("/create")
    public ResponseEntity<ResponseApi<String>> crearProducto(@Valid@RequestBody ProductoRequest dto ) {
        try {
            String mensaje = productoService.crearProductoConVariacion(dto);
            return ResponseEntity.ok(new ResponseApi<>(true, mensaje, null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ResponseApi<>(false, "Error al crear el producto: " + e.getMessage(), null));
        }
    }
    
}
