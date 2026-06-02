package com.spring.team1.tiendamia.controllers.cliente.producto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.team1.tiendamia.models.payload.producto.ProductoDetalleDTO;
import com.spring.team1.tiendamia.models.payload.producto.ProductoEditRequest;
import com.spring.team1.tiendamia.models.payload.producto.ProductoList;
import com.spring.team1.tiendamia.models.payload.producto.ProductoRequest;
import com.spring.team1.tiendamia.models.payload.producto.VariacionRequest;
import com.spring.team1.tiendamia.services.producto.ProductoSetterService;
import com.spring.team1.tiendamia.services.producto.ProductoGetterService;
import com.spring.team1.tiendamia.util.response;

import jakarta.validation.Valid;

//Controlador dedicado exclusivamente a manejar las solicitudes relacionadas con productos desde el frontend cliente.
//Este controlador se encarga de exponer endpoints para obtener el catálogo de productos, 
//detalles de productos, y también para que el panel de administración pueda crear y editar productos.
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    //Inyectamos nuestros dos nuevos servicios especializados
    @Autowired
    private ProductoGetterService lecturaService;

    @Autowired
    private ProductoSetterService escrituraService;

    //=========================================================================
    //ENDPOINTS DE LECTURA (Frontend Cliente)
    //=========================================================================

    //Endpoint Get: /api/productos/catalogo para obtener la lista de productos para el catálogo
    @GetMapping("/catalogo")
    public ResponseEntity<response<List<ProductoList>>> obtenerCatalogo() {
        List<ProductoList> catalogo = lecturaService.obtenerProductosParaCatalogo();
        return ResponseEntity.ok(new response<>(true, "Catálogo obtenido con éxito", catalogo));
    }

    //Endpoint Get: /api/productos/{id} para obtener el detalle completo de un producto por su ID
    @GetMapping("/{id}")
    public ResponseEntity<response<ProductoDetalleDTO>> getProductoById(@PathVariable Integer id) {
        ProductoDetalleDTO detalle = lecturaService.obtenerProductoDetalle(id);
        return ResponseEntity.ok(new response<>(true, "Producto obtenido con éxito", detalle));
    }

    //Endpoint Get: /api/productos/slug/{slug} para obtener el detalle completo de un producto por su slug
    @GetMapping("/slug/{slug}")
    public ResponseEntity<response<ProductoDetalleDTO>> getProductoBySlug(@PathVariable String slug) {
        ProductoDetalleDTO detalle = lecturaService.obtenerProductoPorSlug(slug);
        return ResponseEntity.ok(new response<>(true, "Producto obtenido con éxito", detalle));
    }

    //=========================================================================
    //ENDPOINTS DE ESCRITURA (Panel de Administración)
    //=========================================================================

    //Endpoint Post: /api/productos para crear un nuevo producto con sus variaciones
    @PostMapping
    public ResponseEntity<response<String>> crearProducto(@Valid @RequestBody ProductoRequest request) {
        String mensaje = escrituraService.crearProductoConVariacion(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new response<>(true, mensaje, null));
    }

    //Endpoint Post: /api/productos/{productoId}/variaciones para agregar una nueva variación a un producto existente
    @PostMapping("/{productoId}/variaciones")
    public ResponseEntity<response<String>> agregarVariacion(
            @PathVariable Integer productoId, 
            @Valid @RequestBody VariacionRequest request) {
        String mensaje = escrituraService.crearVariacionDelProducto(productoId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new response<>(true, mensaje, null));
    }

    //Endpoint Put: /api/productos/{id} para editar un producto existente (sin variaciones)
    @PutMapping("/{id}")
    public ResponseEntity<response<String>> editarProducto(
            @PathVariable Integer id, 
            @Valid @RequestBody ProductoEditRequest request) {
        String mensaje = escrituraService.editarProducto(id, request);
        return ResponseEntity.ok(new response<>(true, mensaje, null));
    }

    //Endpoint Put: /api/productos/variaciones/{codigoInventario} para editar una variación existente usando su código de inventario
    @PutMapping("/variaciones/{codigoInventario}")
    public ResponseEntity<response<String>> editarVariacion(
            @PathVariable String codigoInventario, 
            @Valid @RequestBody VariacionRequest request) {
        String mensaje = escrituraService.editarVariaciones(codigoInventario, request);
        return ResponseEntity.ok(new response<>(true, mensaje, null));
    }

    //Endpoint Patch: /api/productos/{id}/estado para cambiar el estado de un producto (activo/inactivo)
    @PatchMapping("/{id}/estado")
    public ResponseEntity<response<String>> cambiarEstadoProducto(@PathVariable Integer id) {
        String mensaje = escrituraService.estadoProducto(id);
        return ResponseEntity.ok(new response<>(true, mensaje, null));
    }
}