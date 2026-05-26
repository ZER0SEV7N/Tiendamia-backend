package com.spring.team1.tiendamia.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.team1.tiendamia.Models.payload.marcas.MarcaRequestDTO;
import com.spring.team1.tiendamia.Models.payload.marcas.MarcasDTO;
import com.spring.team1.tiendamia.Models.productos.Marcas;
import com.spring.team1.tiendamia.Util.ResponseApi;
import com.spring.team1.tiendamia.services.marca.MarcaService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("api/admin/marca")
public class MarcaController {
    @Autowired private MarcaService marcaService;

    @GetMapping("/")
    public ResponseEntity<ResponseApi<List<Marcas>>> all() {
        try {
            return ResponseEntity.ok(new ResponseApi<>(true, "Marcas obtenidas correctamente", marcaService.getAllMarcas()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ResponseApi<>(false, "Error al obtener las marcas", null));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseApi<List<MarcasDTO>>> getMethodName() {
        try {
            List<MarcasDTO> marcas = marcaService.getMarcasDTO();
            return ResponseEntity.ok(new ResponseApi<>(true, "Marcas obtenidas correctamente", marcas));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ResponseApi<>(false, "Error al obtener las marcas", null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseApi<Marcas>> getById(@PathVariable Long id) {
        try {
            Marcas marca = marcaService.getMarcaById(id);
            return ResponseEntity.ok(new ResponseApi<>(true, "Marca obtenida correctamente", marca));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ResponseApi<>(false, "Error al obtener la marca", null));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseApi<Marcas>> create(@Valid @RequestBody MarcaRequestDTO dto) {
        try {
            Marcas nuevaMarca = marcaService.createMarca(dto);
            return ResponseEntity.ok(new ResponseApi<>(true, "Marca creada correctamente", nuevaMarca));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ResponseApi<>(false, "Error al crear la marca", null));
        }
    }
    
}
