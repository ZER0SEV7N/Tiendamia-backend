package com.spring.team1.tiendamia.controllers.cliente;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.team1.tiendamia.models.payload.producto.ProductoList;
import com.spring.team1.tiendamia.services.producto.ProductoService;
import com.spring.team1.tiendamia.util.response;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    @Autowired ProductoService productoService;
    

    @GetMapping("/")
    public ResponseEntity<response<List<ProductoList>>> obtenerProductosParaCatalogo() {
        try {
            List<ProductoList> productos = productoService.obtenerProductosParaCatalogo();
            return ResponseEntity.ok(new response<>(true, "Productos obtenidos exitosamente", productos));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new response<>(false, "Error al obtener los productos: " + e.getMessage(), null));
        }
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<response<?>> obtenerDetalleProducto(@PathVariable Integer id) {
        try {
            var detalle = productoService.obtenerProductoDetalle(id);
            return ResponseEntity.ok(new response<>(true, "Detalle del producto obtenido exitosamente", detalle));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new response<>(false, "Error al obtener el detalle del producto: " + e.getMessage(), null));
        }
    }
}
