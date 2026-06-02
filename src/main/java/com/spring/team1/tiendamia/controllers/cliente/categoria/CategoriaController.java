package com.spring.team1.tiendamia.controllers.cliente.categoria;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.team1.tiendamia.models.payload.categoria.CategoriaResponse;
import com.spring.team1.tiendamia.services.producto.categoria.CategoriaService;
import com.spring.team1.tiendamia.util.response;


@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {
    @Autowired private CategoriaService categoriaService;

    @GetMapping("/")
    public ResponseEntity<response<List<CategoriaResponse>>> allCategorias() {
        try {
            List<CategoriaResponse> categorias = categoriaService.getAllCategorias();
            return ResponseEntity.ok(new response<>(true, "Categorías obtenidas correctamente", categorias));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new response<>(false, "Error al obtener las categorías: " + e.getMessage(), null));
        }
    }
}
