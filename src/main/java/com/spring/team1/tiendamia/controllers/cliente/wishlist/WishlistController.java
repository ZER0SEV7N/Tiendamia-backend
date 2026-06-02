package com.spring.team1.tiendamia.controllers.cliente.wishlist;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.spring.team1.tiendamia.models.wishlist.DTO.WishlistItemResponse;
import com.spring.team1.tiendamia.services.wishlist.WishlistService;
import com.spring.team1.tiendamia.util.response;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    // GET /api/wishlist — obtener todos los favoritos del usuario
    @GetMapping
    public ResponseEntity<response<List<WishlistItemResponse>>> obtenerWishlist(Authentication authentication) {
        if (authentication == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new response<>(false, "No autenticado", null));

        List<WishlistItemResponse> items = wishlistService.obtenerWishlist(authentication.getName());
        return ResponseEntity.ok(new response<>(true, "Wishlist obtenida correctamente", items));
        
    }

    // POST /api/wishlist/agregar/{idProducto} — agregar un producto a favoritos
    @PostMapping("/agregar/{idProducto}")
    public ResponseEntity<response<WishlistItemResponse>> agregarProducto(
            Authentication authentication,
            @PathVariable Integer idProducto) {
        if (authentication == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new response<>(false, "No autenticado", null));
        
        WishlistItemResponse item = wishlistService.agregarProducto(authentication.getName(), idProducto);
        return ResponseEntity.ok(new response<>(true, "Producto agregado a favoritos", item));
       
    }

    // DELETE /api/wishlist/eliminar/{idWishlist} — eliminar un item de favoritos
    @DeleteMapping("/eliminar/{idWishlist}")
    public ResponseEntity<response<String>> eliminarItem(
            Authentication authentication,
            @PathVariable Integer idWishlist) {
        if (authentication == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new response<>(false, "No autenticado", null));
        
        wishlistService.eliminarItem(authentication.getName(), idWishlist);
        return ResponseEntity.ok(new response<>(true, "Producto eliminado de favoritos", null));
        
    }

    // GET /api/wishlist/check/{idProducto} — verifica si un producto esta en
    // favoritos con su id
    @GetMapping("/check/{idProducto}")
    public ResponseEntity<response<Boolean>> verificarEnWishlist(
            Authentication authentication,
            @PathVariable Integer idProducto) {
        if (authentication == null)
            return ResponseEntity.ok(new response<>(true, "No autenticado", false));
        
        boolean enWishlist = wishlistService.estaEnWishlist(authentication.getName(), idProducto);
        return ResponseEntity.ok(new response<>(true, "Verificación exitosa", enWishlist));
        
    }
}
