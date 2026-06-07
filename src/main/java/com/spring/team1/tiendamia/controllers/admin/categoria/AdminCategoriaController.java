package com.spring.team1.tiendamia.controllers.admin.categoria;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.team1.tiendamia.models.payload.categoria.CategoriaRequestDTO;
import com.spring.team1.tiendamia.models.payload.categoria.CategoriaResponse;
import com.spring.team1.tiendamia.models.payload.categoria.ListCategorias;
import com.spring.team1.tiendamia.models.productos.carateristicas.Categorias;
import com.spring.team1.tiendamia.services.producto.categoria.CategoriaService;
import com.spring.team1.tiendamia.util.Response;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/admin/categorias")
public class AdminCategoriaController {
    @Autowired private CategoriaService categoriaService;

    @GetMapping("/")
    public ResponseEntity<Response<List<CategoriaResponse>>> allCategorias() {
        try {
            List<CategoriaResponse> categorias = categoriaService.getAllCategorias();
            return ResponseEntity.ok(new Response<>(true, "Categorías obtenidas correctamente", categorias));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Response<>(false, "Error al obtener las categorías: " + e.getMessage(), null));
        }
    }

    @GetMapping("/list/padres")
    public ResponseEntity<Response<List<ListCategorias>>> allCategoriasPadre() {
        try {
            List<ListCategorias> categorias = categoriaService.getAllCategoriasPadre();
            return ResponseEntity.ok(new Response<>(true, "Categorías padre obtenidas correctamente", categorias));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Response<>(false, "Error al obtener las categorías padre: " + e.getMessage(), null));
        }
    }

    @GetMapping("/list/hijas")
    public ResponseEntity<Response<List<ListCategorias>>> allCategoriasHija() {
        try {
            List<ListCategorias> categorias = categoriaService.getAllCategoriasHija();
            return ResponseEntity.ok(new Response<>(true, "Categorías hija obtenidas correctamente", categorias));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Response<>(false, "Error al obtener las categorías hija: " + e.getMessage(), null));
        }
    }

    @GetMapping("/list/nietas")
    public ResponseEntity<Response<List<ListCategorias>>> allCategoriasNieta() {
        try {
            List<ListCategorias> categorias = categoriaService.getAllCategoriasNietas();
            return ResponseEntity.ok(new Response<>(true, "Categorías nieta obtenidas correctamente", categorias));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Response<>(false, "Error al obtener las categorías nieta: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<Categorias>> getCategoriaById(@PathVariable Integer id) {
        try {
            Categorias categoria = categoriaService.getCategoriaById(id);
            return ResponseEntity.ok(new Response<>(true, "Categoría obtenida correctamente", categoria));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Response<>(false, "Error al obtener la categoría: " + e.getMessage(), null));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Response<String>> createCategoria(@Valid @RequestBody CategoriaRequestDTO dto) {
        try {
            String message = categoriaService.createCategoria(dto);
            return ResponseEntity.ok(new Response<>(true, message, null));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(400).body(new Response<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Response<>(false, "Error al crear la categoría: " + e.getMessage(), null));
        }
    }
    
    @PostMapping("/update/{id}")
    public ResponseEntity<Response<String>> updateCategoria(@PathVariable Integer id, @Valid @RequestBody CategoriaRequestDTO dto) {
        try {
            String message = categoriaService.updateCategoria(id, dto);
            return ResponseEntity.ok(new Response<>(true, message, null));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(400).body(new Response<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Response<>(false, "Error al actualizar la categoría: " + e.getMessage(), null));
        }
    }
}
