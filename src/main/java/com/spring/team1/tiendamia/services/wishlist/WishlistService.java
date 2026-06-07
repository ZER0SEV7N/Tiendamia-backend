package com.spring.team1.tiendamia.services.wishlist;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.team1.tiendamia.models.productos.Producto;
import com.spring.team1.tiendamia.models.productos.VariacionesProducto;
import com.spring.team1.tiendamia.models.usuario.Usuarios;
import com.spring.team1.tiendamia.models.wishlist.Wishlist;
import com.spring.team1.tiendamia.models.wishlist.DTO.WishlistItemResponse;
import com.spring.team1.tiendamia.repository.producto.ProductoRepository;
import com.spring.team1.tiendamia.repository.usuario.UsuariosRepository;
import com.spring.team1.tiendamia.repository.wishlist.WishlistRepository;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private ProductoRepository productoRepository;

    // Obtiene la wishlist completa del usuario como lista de DTOs
    public List<WishlistItemResponse> obtenerWishlist(String correo) {
        Usuarios usuario = obtenerUsuarioPorCorreo(correo);
        List<Wishlist> items = wishlistRepository.findByUsuarioId(usuario.getId());

        return items.stream()
                .map(item -> convertirDto(item))
                .collect(Collectors.toList());
    }

    // Agrega un producto a la wishlist. Si ya existe, no hace nada.
    public WishlistItemResponse agregarProducto(String correo, Integer idProducto) {
        Usuarios usuario = obtenerUsuarioPorCorreo(correo);

        // Si ya existe, devuelve el item existente sin duplicar
        Optional<Wishlist> existente = wishlistRepository.findByUsuarioIdAndProductoId(usuario.getId(), idProducto);
        if (existente.isPresent()) {
            return convertirDto(existente.get());
        }

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + idProducto));

        Wishlist nuevo = new Wishlist();
        nuevo.setUsuario(usuario);
        nuevo.setProducto(producto);

        return convertirDto(wishlistRepository.save(nuevo));
    }

    // Elimina un item de la wishlist por su ID
    public boolean eliminarItem(String correo, Integer idWishlist) {
        Usuarios usuario = obtenerUsuarioPorCorreo(correo);

        Wishlist item = wishlistRepository.findById(idWishlist)
                .orElseThrow(() -> new RuntimeException("Item no encontrado en wishlist"));

        // Verifica que el item pertenece al usuario autenticado
        if (!item.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("No tienes permiso para eliminar este item");
        }

        wishlistRepository.delete(item);
        return true;
    }

    // Verifica si un producto esta en la wishlist del usuario
    public boolean estaEnWishlist(String correo, Integer idProducto) {
        Usuarios usuario = obtenerUsuarioPorCorreo(correo);
        return wishlistRepository.existsByUsuarioIdAndProductoId(usuario.getId(), idProducto);
    }

    // Convierte una entidad Wishlist a su DTO de respuesta
    private WishlistItemResponse convertirDto(Wishlist item) {
        Producto producto = item.getProducto();
        List<VariacionesProducto> variaciones = producto.getVariaciones();

        // Precio minimo entre todas las variaciones
        double precioMin = variaciones.stream()
                .mapToDouble(v -> v.getPrecio() != null ? v.getPrecio() : 0.0)
                .min()
                .orElse(0.0);

        // En stock si al menos una variacion tiene stock > 0
        boolean enStock = variaciones.stream()
                .anyMatch(v -> v.getStock() != null && v.getStock() > 0);

        WishlistItemResponse dto = new WishlistItemResponse();
        dto.setIdWishlist(item.getId());
        dto.setIdProducto(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setImagenUrl(producto.getImagen_url());
        dto.setPrecio(precioMin);
        dto.setEnStock(enStock);

        return dto;
    }

    private Usuarios obtenerUsuarioPorCorreo(String correo) {
        return usuariosRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + correo));
    }
}
