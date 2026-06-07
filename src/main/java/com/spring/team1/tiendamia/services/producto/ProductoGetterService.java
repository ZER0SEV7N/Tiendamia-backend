package com.spring.team1.tiendamia.services.producto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.spring.team1.tiendamia.mapper.ProductoMapper;
import com.spring.team1.tiendamia.models.payload.producto.ProductoDetalleDTO;
import com.spring.team1.tiendamia.models.payload.producto.ProductoList;
import com.spring.team1.tiendamia.models.productos.Producto;
import com.spring.team1.tiendamia.repository.producto.ProductoRepository;

import org.springframework.transaction.annotation.Transactional;

//Servicio dedicado exclusivamente a la obtención de datos relacionados con productos.
//Este servicio se encarga de manejar toda la lógica relacionada con la consulta y transformación de datos
@Service
public class ProductoGetterService {

    @Autowired private ProductoRepository productoRepository;
    @Autowired private ProductoMapper productoMapper;

    //Métodos para obtener productos para el catálogo, detalles de productos, etc.
    @Transactional(readOnly = true)
    public List<ProductoList> obtenerProductosParaCatalogo() {
        return productoRepository.findAll().stream()
                .map(productoMapper::aProductoList)
                .collect(Collectors.toList());
    }

    //Método para obtener el detalle completo de un producto por su ID, 
    //incluyendo variaciones, galería de imágenes y atributos.
    @Transactional(readOnly = true)
    public ProductoDetalleDTO obtenerProductoDetalle(Integer id) {
        Producto prod = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        return productoMapper.aProductoDetalleDTO(prod);
    }

    //Método para obtener el detalle completo de un producto por su slug,
    //lo que permite acceder a la información del producto usando una URL amigable.
    @Transactional(readOnly = true)
    public ProductoDetalleDTO obtenerProductoPorSlug(String slug) {
        Producto prod = productoRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con slug: " + slug));
        return productoMapper.aProductoDetalleDTO(prod);
    }
}

