package com.spring.team1.tiendamia.mapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import com.spring.team1.tiendamia.models.productos.Producto;
import com.spring.team1.tiendamia.models.productos.VariacionesProducto;
import com.spring.team1.tiendamia.models.productos.carateristicas.ImagenesProducto;
import com.spring.team1.tiendamia.models.payload.producto.ProductoDetalleDTO;
import com.spring.team1.tiendamia.models.payload.producto.ProductoList;

//Mapper para convertir entidades de Producto a DTOs y viceversa.
//Actualmente está vacío, 
//pero se puede implementar métodos de mapeo aquí en el futuro para mantener el código limpio y organizado.
@Component
public class ProductoMapper {

    //Mapeo de catalogo 
    public ProductoList aProductoList(Producto prod){
        ProductoList dto = new ProductoList();
        dto.setId(prod.getId());
        dto.setNombre(prod.getNombre());
        dto.setSlug(prod.getSlug());
        dto.setImagenUrl(prod.getImagen_url());
        dto.setNombreCategoria(prod.getCategoria() != null ? prod.getCategoria().getNombre() : "Sin Categoria");
        dto.setNombreMarca(prod.getMarca() != null ? prod.getMarca().getNombre() : "Sin Marca");
        dto.setEstado(prod.getEstado());
        return dto;
    }

    //Mapeo del detalle del producto
    public ProductoDetalleDTO aProductoDetalleDTO(Producto prod){
        ProductoDetalleDTO dto = new ProductoDetalleDTO();
        dto.setId(prod.getId());
        dto.setNombre(prod.getNombre());
        dto.setSlug(prod.getSlug());
        dto.setImagenUrl(prod.getImagen_url());
        dto.setDescripcion(prod.getDescripcion());
        dto.setNombreCategoria(prod.getCategoria() != null ? prod.getCategoria().getNombre() : "Sin Categoria");
        dto.setNombreMarca(prod.getMarca() != null ? prod.getMarca().getNombre() : "Sin Marca");

        dto.setGaleria(mapearGaleria(prod));
        dto.setVariaciones(mapearVariaciones(prod));
        dto.setAtributos(mapearAtributos(prod));
        return dto;
    }

    //Mapear la Galeria de imagenes (Privado)
    private List<String> mapearGaleria(Producto prod){
        if(prod.getImagenes() == null) return new ArrayList<>();
        return prod.getImagenes().stream()
               .sorted(Comparator.comparing(ImagenesProducto::getId)) //Ordenar por ID para mantener un orden consistente
               .map(ImagenesProducto::getUrl)
               .collect(Collectors.toList());
    }

    //Mapear las variaciones del producto (Privado)
    private List<ProductoDetalleDTO.VariacionDTO> mapearVariaciones(Producto prod){
        if(prod.getVariaciones() == null) return new ArrayList<>();
        return prod.getVariaciones().stream().map(v -> {
            ProductoDetalleDTO.VariacionDTO vDto = new ProductoDetalleDTO.VariacionDTO();
            vDto.setId(v.getId());
            vDto.setCodigoInventario(v.getCodigoInventario());
            vDto.setPrecio(v.getPrecio());
            vDto.setStock(v.getStock());
            vDto.setImagenUrl(v.getImagenUrl());

            //Mapear las características de la variación a partir de sus valores de atributo
            List<ProductoDetalleDTO.CaracteristicaDTO> caracDTOs = v.getVariacionValores().stream().map(vv -> {
                ProductoDetalleDTO.CaracteristicaDTO cDto = new ProductoDetalleDTO.CaracteristicaDTO();
                cDto.setAtributoNombre(vv.getValorAtributo().getAtributo().getNombre());
                cDto.setValorTexto(vv.getValorAtributo().getValor());
                return cDto;
            }).collect(Collectors.toList());

            vDto.setCaracteristicas(caracDTOs);
            return vDto;
        }).collect(Collectors.toList());
    }

    //Mapear los atributos del producto (Privado)
    private List<ProductoDetalleDTO.AtributoOpcionesDTO> mapearAtributos(Producto prod) {
        Map<String, List<String>> atributosMap = new HashMap<>();
        
        //Si el producto tiene variaciones, 
        //recorremos cada variación y sus valores para construir un mapa de atributos y sus opciones
        if(prod.getVariaciones() != null) {
            
            //Recorremos cada variación del producto
            for (VariacionesProducto v : prod.getVariaciones()) {
                
                //Si la variación tiene valores de atributos, recorremos cada uno para llenar el map
                if (v.getVariacionValores() != null) {

                    //Recorremos cada valor de atributo de la variación
                    for (var vv : v.getVariacionValores()) {
                        
                        //Obtenemos el nombre del atributo y su valor, y los agregamos al mapa de atributos
                        String nombreAtributo = vv.getValorAtributo().getAtributo().getNombre();
                        String valor = vv.getValorAtributo().getValor();
                        atributosMap.computeIfAbsent(nombreAtributo, k -> new ArrayList<>()).add(valor);
                    }
                }
            }
        }

        //Convertimos el mapa de atributos a una lista de 
        //AtributoOpcionesDTO para incluir en el DTO de detalle del producto
        return atributosMap.entrySet().stream().map(entry -> {
            ProductoDetalleDTO.AtributoOpcionesDTO dto = new ProductoDetalleDTO.AtributoOpcionesDTO();
            dto.setNombre(entry.getKey());
            dto.setOpciones(entry.getValue());
            return dto;
        }).collect(Collectors.toList());
    }
}
