package com.spring.team1.tiendamia.services.marca;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.team1.tiendamia.models.payload.marcas.MarcaRequestDTO;
import com.spring.team1.tiendamia.models.payload.marcas.MarcasDTO;
import com.spring.team1.tiendamia.models.productos.Marcas;
import com.spring.team1.tiendamia.repository.marca.MarcaRepository;


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

        if (marcaRepository.existsByNombreIgnoreCase(dto.getNombre())) {
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
        Marcas marca = marcaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));

        if (marcaRepository.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new IllegalStateException("Ya existe una marca con ese nombre");
        }

        marca.setNombre(dto.getNombre());
        marca.setSlug(dto.getSlug());
        marca.setImagen_logo(dto.getImagen_logo());
        marca.setImagen_banner(dto.getImagen_banner());
        marca.setDescripcion(dto.getDescripcion());
        marca.setDestacada(dto.getDestacada());
        marca.setEstado(dto.getEstado());

        marcaRepository.save(marca);
        return "Marca actualizada correctamente";
    }
}
