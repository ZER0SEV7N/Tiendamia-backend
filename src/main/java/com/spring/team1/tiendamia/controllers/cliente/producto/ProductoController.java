package com.spring.team1.tiendamia.controllers.cliente.producto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.team1.tiendamia.models.payload.producto.ProductoDetalleDTO;
import com.spring.team1.tiendamia.models.payload.producto.ProductoList;
import com.spring.team1.tiendamia.services.producto.ProductoService;
import com.spring.team1.tiendamia.util.response;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    
    @Autowired ProductoService productoService;
    //Endpoint: GET /api/productos/ -> Lista de productos para mostrar en el catálogo
    @GetMapping("/")
    public ResponseEntity<response<List<ProductoList>>> obtenerProductosParaCatalogo() {
            List<ProductoList> productos = productoService.obtenerProductosParaCatalogo();
            return ResponseEntity.ok(new response<>(true, "Productos obtenidos exitosamente", productos));
    }

    //Endpoint: GET /api/productos/detalle/{id} -> Detalle completo del producto para mostrar en la página de detalle
    @GetMapping("/detalle/{id}")
    public ResponseEntity<response<?>> obtenerDetalleProducto(@PathVariable Integer id) { 
        var detalle = productoService.obtenerProductoDetalle(id);
        return ResponseEntity.ok(new response<>(true, "Detalle del producto obtenido exitosamente", detalle));   
    }

    //Endpoint: GET /api/productos/slug/{slug} -> Detalle completo del producto usando el slug
    @GetMapping("/slug/{slug}")
    public ResponseEntity<response<ProductoDetalleDTO>> obtenerDetalleProductoPorSlug(@PathVariable String slug) {
        ProductoDetalleDTO detalle = productoService.obtenerProductoPorSlug(slug);
        return ResponseEntity.ok(new response<>(true, "Detalle del producto obtenido exitosamente", detalle));
    }

}
