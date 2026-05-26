package com.spring.team1.tiendamia.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.team1.tiendamia.Models.payload.categoria.CategoriaRequestDTO;
import com.spring.team1.tiendamia.Models.payload.categoria.CategoriaResponse;
import com.spring.team1.tiendamia.Models.payload.categoria.ListCategorias;
import com.spring.team1.tiendamia.Models.productos.Categorias;
import com.spring.team1.tiendamia.Util.ResponseApi;
import com.spring.team1.tiendamia.services.categoria.CategoriaService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/admin/categorias")
public class CategoriaController {
    @Autowired private CategoriaService categoriaService;

    @GetMapping("/")
    public ResponseEntity<ResponseApi<List<CategoriaResponse>>> allCategorias() {
        try {
            List<CategoriaResponse> categorias = categoriaService.getAllCategorias();
            return ResponseEntity.ok(new ResponseApi<>(true, "Categorías obtenidas correctamente", categorias));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseApi<>(false, "Error al obtener las categorías: " + e.getMessage(), null));
        }
    }

    @GetMapping("/list/padres")
    public ResponseEntity<ResponseApi<List<ListCategorias>>> allCategoriasPadre() {
        try {
            List<ListCategorias> categorias = categoriaService.getAllCategoriasPadre();
            return ResponseEntity.ok(new ResponseApi<>(true, "Categorías padre obtenidas correctamente", categorias));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseApi<>(false, "Error al obtener las categorías padre: " + e.getMessage(), null));
        }
    }

    @GetMapping("/list/hijas")
    public ResponseEntity<ResponseApi<List<ListCategorias>>> allCategoriasHija() {
        try {
            List<ListCategorias> categorias = categoriaService.getAllCategoriasHija();
            return ResponseEntity.ok(new ResponseApi<>(true, "Categorías hija obtenidas correctamente", categorias));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseApi<>(false, "Error al obtener las categorías hija: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseApi<Categorias>> getCategoriaById(@PathVariable Long id) {
        try {
            Categorias categoria = categoriaService.getCategoriaById(id);
            return ResponseEntity.ok(new ResponseApi<>(true, "Categoría obtenida correctamente", categoria));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseApi<>(false, "Error al obtener la categoría: " + e.getMessage(), null));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseApi<String>> createCategoria(@Valid @RequestBody CategoriaRequestDTO dto) {
        try {
            String message = categoriaService.createCategoria(dto);
            return ResponseEntity.ok(new ResponseApi<>(true, message, null));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(400).body(new ResponseApi<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseApi<>(false, "Error al crear la categoría: " + e.getMessage(), null));
        }
    }
    
    @PostMapping("/update/{id}")
    public ResponseEntity<ResponseApi<String>> updateCategoria(@PathVariable Long id, @Valid @RequestBody CategoriaRequestDTO dto) {
        try {
            String message = categoriaService.updateCategoria(id, dto);
            return ResponseEntity.ok(new ResponseApi<>(true, message, null));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(400).body(new ResponseApi<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseApi<>(false, "Error al actualizar la categoría: " + e.getMessage(), null));
        }
    }
}
