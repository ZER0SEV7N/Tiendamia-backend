package com.spring.team1.tiendamia.controllers.cliente.wishlist;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.spring.team1.tiendamia.models.wishlist.DTO.WishlistItemResponse;
import com.spring.team1.tiendamia.services.wishlist.WishlistService;
import com.spring.team1.tiendamia.util.Response;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    // GET /api/wishlist — obtener todos los favoritos del usuario
    @GetMapping
    public ResponseEntity<Response<List<WishlistItemResponse>>> obtenerWishlist(Authentication authentication) {
        if (authentication == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Response<>(false, "No autenticado", null));

        List<WishlistItemResponse> items = wishlistService.obtenerWishlist(authentication.getName());
        return ResponseEntity.ok(new Response<>(true, "Wishlist obtenida correctamente", items));
        
    }

    // POST /api/wishlist/agregar/{idProducto} — agregar un producto a favoritos
    @PostMapping("/agregar/{idProducto}")
    public ResponseEntity<Response<WishlistItemResponse>> agregarProducto(
            Authentication authentication,
            @PathVariable Integer idProducto) {
        if (authentication == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Response<>(false, "No autenticado", null));
        
        WishlistItemResponse item = wishlistService.agregarProducto(authentication.getName(), idProducto);
        return ResponseEntity.ok(new Response<>(true, "Producto agregado a favoritos", item));
       
    }

    // DELETE /api/wishlist/eliminar/{idWishlist} — eliminar un item de favoritos
    @DeleteMapping("/eliminar/{idWishlist}")
    public ResponseEntity<Response<String>> eliminarItem(
            Authentication authentication,
            @PathVariable Integer idWishlist) {
        if (authentication == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Response<>(false, "No autenticado", null));
        
        wishlistService.eliminarItem(authentication.getName(), idWishlist);
        return ResponseEntity.ok(new Response<>(true, "Producto eliminado de favoritos", null));
        
    }

    // GET /api/wishlist/check/{idProducto} — verifica si un producto esta en
    // favoritos con su id
    @GetMapping("/check/{idProducto}")
    public ResponseEntity<Response<Boolean>> verificarEnWishlist(
            Authentication authentication,
            @PathVariable Integer idProducto) {
        if (authentication == null)
            return ResponseEntity.ok(new Response<>(true, "No autenticado", false));
        
        boolean enWishlist = wishlistService.estaEnWishlist(authentication.getName(), idProducto);
        return ResponseEntity.ok(new Response<>(true, "Verificación exitosa", enWishlist));
        
    }
}
