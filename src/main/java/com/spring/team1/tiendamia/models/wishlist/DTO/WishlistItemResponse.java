package com.spring.team1.tiendamia.models.wishlist.DTO;

import lombok.Data;

@Data
public class WishlistItemResponse {
    private Integer idWishlist;
    private Integer idProducto;
    private String nombre;
    private String imagenUrl;
    private Double precio;
    private Boolean enStock;
}
