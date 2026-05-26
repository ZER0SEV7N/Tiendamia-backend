package com.spring.team1.tiendamia.services.categoria;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.team1.tiendamia.models.payload.categoria.CategoriaRequestDTO;
import com.spring.team1.tiendamia.models.payload.categoria.CategoriaResponse;
import com.spring.team1.tiendamia.models.payload.categoria.ListCategorias;
import com.spring.team1.tiendamia.models.productos.Categorias;
import com.spring.team1.tiendamia.repository.categoria.CategoriaRepository;

@Service
public class CategoriaService {
    @Autowired CategoriaRepository categoriaRepository;

    public List<CategoriaResponse> getAllCategorias() {
        List<Categorias> allCategorias = categoriaRepository.findAll();
        
        return allCategorias.stream()
                .filter(c -> c.getCategoriaPadre() == null)
                .map(padre -> mapearAArbolDTO(padre))
                .collect(Collectors.toList());
    }

    private CategoriaResponse mapearAArbolDTO(Categorias categoria) {
        CategoriaResponse dto = new CategoriaResponse();
        dto.setId(categoria.getId());
        dto.setNombre(categoria.getNombre());
        dto.setSlug(categoria.getSlug());
        dto.setIdCategoriaPadre(categoria.getCategoriaPadre() != null ? categoria.getCategoriaPadre().getId() : null);

        if (categoria.getSubcategorias() != null && !categoria.getSubcategorias().isEmpty()) {
            List<CategoriaResponse> hijos = categoria.getSubcategorias().stream()
                    .map(hijo -> mapearAArbolDTO(hijo)) 
                    .collect(Collectors.toList());
            dto.setSubcategorias(hijos);
        } else {
            dto.setSubcategorias(null);
        }

        return dto;
    }

    public List<ListCategorias> getAllCategoriasPadre() {
        return categoriaRepository.findByCategoriaPadreIsNull().stream().map(categoria -> {
            ListCategorias dto = new ListCategorias();
            dto.setId(categoria.getId());
            dto.setNombre(categoria.getNombre());
            return dto;
        }).toList();
    }

    public List<ListCategorias> getAllCategoriasHija() {
        return categoriaRepository.findByCategoriasHijas().stream().map(categoria -> {
            ListCategorias dto = new ListCategorias();
            dto.setId(categoria.getId());
            dto.setNombre(categoria.getNombre());
            return dto;
        }).toList();
    }

    public Categorias getCategoriaById(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    public String createCategoria(CategoriaRequestDTO dto) {
        if (categoriaRepository.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new IllegalStateException("Ya existe una categoría con ese nombre");
        }

        Categorias categoria = new Categorias();
        categoria.setNombre(dto.getNombre());
        categoria.setSlug(dto.getSlug());
        if (dto.getId_categoria_padre() != null) {
            Categorias categoriaPadre = categoriaRepository.findById(dto.getId_categoria_padre())
                    .orElseThrow(() -> new RuntimeException("Categoría padre no encontrada"));
            categoria.setCategoriaPadre(categoriaPadre);
        }

        categoriaRepository.save(categoria);
        return "Categoría creada correctamente";
    }

    public String updateCategoria(Long id, CategoriaRequestDTO dto) {
        Categorias categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        if (!categoria.getNombre().equalsIgnoreCase(dto.getNombre()) &&
            categoriaRepository.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new IllegalStateException("Ya existe una categoría con ese nombre");
        }

        categoria.setNombre(dto.getNombre());
        categoria.setSlug(dto.getSlug());
        if (dto.getId_categoria_padre() != null) {
            Categorias categoriaPadre = categoriaRepository.findById(dto.getId_categoria_padre())
                    .orElseThrow(() -> new RuntimeException("Categoría padre no encontrada"));
            categoria.setCategoriaPadre(categoriaPadre);
        } else {
            categoria.setCategoriaPadre(null);
        }

        categoriaRepository.save(categoria);
        return "Categoría actualizada correctamente";
    }
}
