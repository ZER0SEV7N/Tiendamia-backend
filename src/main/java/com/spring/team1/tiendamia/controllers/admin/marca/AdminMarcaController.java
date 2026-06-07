package com.spring.team1.tiendamia.controllers.admin.marca;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.team1.tiendamia.models.payload.marcas.MarcaRequestDTO;
import com.spring.team1.tiendamia.models.payload.marcas.MarcasDTO;
import com.spring.team1.tiendamia.models.productos.carateristicas.Marcas;
import com.spring.team1.tiendamia.services.producto.marca.MarcaService;
import com.spring.team1.tiendamia.util.Response;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("api/admin/marca")
public class AdminMarcaController {
    @Autowired private MarcaService marcaService;

    @GetMapping("/")
    public ResponseEntity<Response<List<Marcas>>> all() {
        try {
            return ResponseEntity.ok(new Response<>(true, "Marcas obtenidas correctamente", marcaService.getAllMarcas()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new Response<>(false, "Error al obtener las marcas", null));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Response<List<MarcasDTO>>> getMethodName() {
        try {
            List<MarcasDTO> marcas = marcaService.getMarcasDTO();
            return ResponseEntity.ok(new Response<>(true, "Marcas obtenidas correctamente", marcas));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new Response<>(false, "Error al obtener las marcas", null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<Marcas>> getById(@PathVariable Integer id) {
        try {
            Marcas marca = marcaService.getMarcaById(id);
            return ResponseEntity.ok(new Response<>(true, "Marca obtenida correctamente", marca));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new Response<>(false, "Error al obtener la marca", null));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Response<String>> create(@Valid @RequestBody MarcaRequestDTO dto) {
        try {
            String mensaje = marcaService.createMarca(dto);
            return ResponseEntity.ok(new Response<>(true, mensaje, null));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new Response<>(false, e.getMessage(), null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new Response<>(false, "Error al crear la marca", null));
        }
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Response<String>> update(@PathVariable Integer id, @Valid @RequestBody MarcaRequestDTO dto) {
        try {
            String mensaje = marcaService.updateMarca(id, dto);
            return ResponseEntity.ok(new Response<>(true, mensaje, null));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new Response<>(false, e.getMessage(), null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new Response<>(false, "Error al actualizar la marca", null));
        }
    }
}
