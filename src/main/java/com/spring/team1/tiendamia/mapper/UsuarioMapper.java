package com.spring.team1.tiendamia.mapper;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.spring.team1.tiendamia.models.ordenes.Ordenes;
import com.spring.team1.tiendamia.models.ordenes.DTO.OrdenResponseDto;
import com.spring.team1.tiendamia.models.payload.usuario.UsuarioPerfilDto;
import com.spring.team1.tiendamia.models.usuario.Usuarios;
import com.spring.team1.tiendamia.models.usuario.ubicacion.DireccionesUsuarios;

//Componente para mapear entidades de usuario y direcciones a DTOs de perfil
@Component
public class UsuarioMapper {
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM, yyyy");

    //Metodo para convertir una entidad de usuario y sus direcciones a un DTO de perfil completo
    public UsuarioPerfilDto aUsuarioPerfilDTO(Usuarios usuario, List<DireccionesUsuarios> direcciones) {
        UsuarioPerfilDto dto = new UsuarioPerfilDto();
        dto.setId(usuario.getId());
        dto.setNombres(usuario.getNombres());
        dto.setApellidos(usuario.getApellidos());
        dto.setCorreo(usuario.getCorreo());
        dto.setTelefono(usuario.getTelefono());
        dto.setActivo(usuario.getEstado());
        dto.setRol(usuario.getRol() != null ? usuario.getRol().getNombre() : null);

        List<UsuarioPerfilDto.DireccionDto> dirDtos = direcciones.stream()
                .map(this::aDireccionDTO)
                .collect(Collectors.toList());
        dto.setDirecciones(dirDtos);

        return dto;
    }

    //Metodo auxiliar para convertir una entidad de dirección a su DTO correspondiente
    private UsuarioPerfilDto.DireccionDto aDireccionDTO(DireccionesUsuarios d) {
        UsuarioPerfilDto.DireccionDto dto = new UsuarioPerfilDto.DireccionDto();
        dto.setId(d.getId());
        dto.setDireccion(d.getDireccion());
        dto.setReferencia(d.getReferencia());
        dto.setEsPrincipal(Boolean.TRUE.equals(d.getEs_principal()));
        dto.setCreateAt(d.getCreateAt() != null ? d.getCreateAt().format(formatter) : null);
        //Mapear información de distrito, provincia y departamento si están disponibles
        if (d.getDistrito() != null) {
            dto.setDistrito(d.getDistrito().getNombre());
            if (d.getDistrito().getProvincia() != null) {
                dto.setProvincia(d.getDistrito().getProvincia().getNombre());
                if (d.getDistrito().getProvincia().getDepartamento() != null) {
                    dto.setDepartamento(d.getDistrito().getProvincia().getDepartamento().getNombre());
                }
            }
        }
        return dto;
    }

    //Mapeo de las ordenes del usuario
    public OrdenResponseDto aOrdenResponseDto(Ordenes orden) {
        OrdenResponseDto dto = new OrdenResponseDto();
        //Formatear el ID de la orden como "TM-000001-PE"
        dto.setId("TM-" + String.format("%06d", orden.getId()) + "-PE");
        dto.setFecha(orden.getCreateAt() != null ? orden.getCreateAt().format(formatter) : "Reciente");

        String estadoTexto = orden.getEstado() != null ? orden.getEstado().name().toLowerCase() : "pendiente";
        dto.setEstado(orden.getEstado() != null ? orden.getEstado().name() : "PENDIENTE");
        
        //Asignar colores según el estado de la orden
        if (estadoTexto.equals("entregada")) 
            dto.setEstadoColor("text-green-600 bg-green-50 border-green-200");
        else if (estadoTexto.equals("cancelada")) 
            dto.setEstadoColor("text-red-600 bg-red-50 border-red-200");
        else 
            dto.setEstadoColor("text-amber-600 bg-amber-50 border-amber-200");
        
        dto.setTotal("S/ " + String.format("%.2f", orden.getTotal()));

        //Mapear los productos de la orden a su DTO correspondiente
        List<OrdenResponseDto.ProductoDto> prodDtos = new ArrayList<>();
        if (orden.getDetalles() != null) {
            orden.getDetalles().forEach(d -> {
                OrdenResponseDto.ProductoDto pDto = new OrdenResponseDto.ProductoDto();
                if (d.getVariacion() != null && d.getVariacion().getProducto() != null) {
                    pDto.setNombre(d.getVariacion().getProducto().getNombre());
                    pDto.setTiendaOrigen("Tienda Oficial");
                    pDto.setImagen(d.getVariacion().getProducto().getImagen_url() != null ? "📦" : "🛍️");
                } else {
                    pDto.setNombre("Producto Variante Ref #" + d.getId());
                    pDto.setTiendaOrigen("Amazon EE.UU.");
                    pDto.setImagen("💻");
                }
                pDto.setCantidad(d.getCantidad());
                prodDtos.add(pDto);
            });
        }
        dto.setProductos(prodDtos);
        return dto;
    }
}
