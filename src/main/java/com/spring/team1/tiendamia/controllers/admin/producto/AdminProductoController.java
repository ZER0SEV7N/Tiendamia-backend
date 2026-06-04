package com.spring.team1.tiendamia.controllers.admin.producto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.team1.tiendamia.models.payload.producto.ProductoEditRequest;
import com.spring.team1.tiendamia.models.payload.producto.ProductoRequest;
import com.spring.team1.tiendamia.models.payload.producto.VariacionRequest;
import com.spring.team1.tiendamia.services.producto.ProductoSetterService;
import com.spring.team1.tiendamia.util.Response;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/admin/productos")
public class AdminProductoController {
    @Autowired ProductoSetterService productoService;

    @PostMapping("/create")
    public ResponseEntity<Response<String>> crearProducto(@Valid @RequestBody ProductoRequest dto ) {
        try {
            String mensaje = productoService.crearProductoConVariacion(dto);
            return ResponseEntity.ok(new Response<>(true, mensaje, null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new Response<>(false, "Error al crear el producto: " + e.getMessage(), null));
        }
    }

    @PostMapping("/variacion/create/{productoId}")
    public ResponseEntity<Response<String>> crearVariacion(@PathVariable Integer productoId, @Valid @RequestBody VariacionRequest request) {
        try {
            String mensaje = productoService.crearVariacionDelProducto(productoId, request);
            return ResponseEntity.ok(new Response<>(true, mensaje, null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new Response<>(false, "Error al crear la variación: " + e.getMessage(), null));
        }
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Response<String>> actualizarProducto(@PathVariable Integer id, @Valid @RequestBody ProductoEditRequest dto) {
        try {
            String mensaje = productoService.editarProducto(id, dto);
            return ResponseEntity.ok(new Response<>(true, mensaje, null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new Response<>(false, "Error al actualizar el producto: " + e.getMessage(), null));
        }
    }
    
    @PostMapping("/variacion/update/{codigoInventario}")
    public ResponseEntity<Response<String>> actualizarVariacion(@PathVariable String codigoInventario, @Valid @RequestBody VariacionRequest request) {
        try {
            String mensaje = productoService.editarVariaciones(codigoInventario, request);
            return ResponseEntity.ok(new Response<>(true, mensaje, null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new Response<>(false, "Error al actualizar la variación: " + e.getMessage(), null));
        }
    }


    @PutMapping("/estado/{id}")
    public String cambiarEstado(@PathVariable Integer id) {
        try{
            var estado = productoService.estadoProducto(id);
            return estado;
        } catch (RuntimeException e) {
            return "Error al cambiar el estado del producto: " + e.getMessage();
        }
    }
}
