package com.spring.team1.tiendamia.services.producto.marca;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.team1.tiendamia.models.payload.marcas.MarcaRequestDTO;
import com.spring.team1.tiendamia.models.payload.marcas.MarcasDTO;
import com.spring.team1.tiendamia.models.productos.carateristicas.Marcas;
import com.spring.team1.tiendamia.repository.producto.marca.MarcaRepository;


@Service
public class MarcaService {
    @Autowired private MarcaRepository marcaRepository;

    public List<Marcas> getAllMarcas() {
        return marcaRepository.findAll();
    }

    public Marcas getMarcaById(Integer id) {
        return marcaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));
    }

    public List<MarcasDTO> getMarcasDTO() {
        return marcaRepository.findAll().stream().map(marca -> {
            MarcasDTO dto = new MarcasDTO();
            dto.setId(marca.getId());
            dto.setNombre(marca.getNombre());
            dto.setSlug(marca.getSlug());
            return dto;
        }).toList();
    }

    public String createMarca(MarcaRequestDTO dto) {

        if (marcaRepository.existsByNombreIgnoreCaseAndIdNot(dto.getNombre(), null)) {
            throw new IllegalStateException("Ya existe una marca con ese nombre");
        }
        
        Marcas marca = new Marcas();
        marca.setNombre(dto.getNombre());
        marca.setSlug(dto.getSlug());
        marca.setImagen_logo(dto.getImagen_logo());
        marca.setImagen_banner(dto.getImagen_banner());
        marca.setDescripcion(dto.getDescripcion());
        marca.setDestacada(dto.getDestacada());
        marca.setEstado(dto.getEstado());

        marcaRepository.save(marca);
        return "Marca creada correctamente";
    }

    public String updateMarca(Integer id, MarcaRequestDTO dto) {
    // 1. Verificar si la marca existe
    Marcas marca = marcaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Marca no encontrada"));

    // 2. Validar nombre único EXCLUYENDO el registro actual
    if (marcaRepository.existsByNombreIgnoreCaseAndIdNot(dto.getNombre(), id)) {
        throw new IllegalStateException("Ya existe otra marca con ese nombre");
    }

    // 3. Mapear los cambios
    marca.setNombre(dto.getNombre());
    marca.setSlug(dto.getSlug());
    marca.setImagen_logo(dto.getImagen_logo());
    marca.setImagen_banner(dto.getImagen_banner());
    marca.setDescripcion(dto.getDescripcion());
    marca.setDestacada(dto.getDestacada());
    marca.setEstado(dto.getEstado());

    // 4. Guardar
    marcaRepository.save(marca);
    return "Marca actualizada correctamente";
}
}
