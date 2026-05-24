package com.spring.team1.tiendamia.Services.ordenes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.team1.tiendamia.Models.Carrito.Carrito;
import com.spring.team1.tiendamia.Models.Carrito.CarritoDetalle;
import com.spring.team1.tiendamia.Models.ordenes.Ordenes;
import com.spring.team1.tiendamia.Models.productos.Variaciones_Producto;
import com.spring.team1.tiendamia.Repository.ordenes.*;
import com.spring.team1.tiendamia.Repository.productos.VariacionesProductosRepository;
import com.spring.team1.tiendamia.Services.carrito.CarritoService;

import jakarta.transaction.Transactional;

@Service
public class OrdenesServices {

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private VariacionesProductosRepository variacionesRepository;

    @Autowired
    private OrdenRepository ordenRepository;
    @Autowired
    private OrdenDetalleRepository detalleRepository;

    @Transactional
    public Ordenes finalizarCompra(Integer idUsuario, Integer idDireccion) {
        Carrito carrito = carritoService.obtenerCarritoPorUsuario(idUsuario);

        if (carrito.getDetalles().isEmpty())
            throw new RuntimeException("El carrito está vacío");

        for (CarritoDetalle item : carrito.getDetalles()) {
            Variaciones_Producto variante = item.getVariacion();
            if (variante.getStock() < item.getCantidad())
                throw new RuntimeException(
                        "No hay suficiente stock para el producto: " + variante.getProducto().getNombre());

        }

        // Procesar la orden (crear entidad Ordenes, guardar detalles, etc.)
        // SE OMITIÓ: La lógica de creación de la orden y sus detalles, así

        for (CarritoDetalle item : carrito.getDetalles()) {
            Variaciones_Producto variante = item.getVariacion();
            variante.setStock(variante.getStock() - item.getCantidad());
            variacionesRepository.save(variante);
        }

        Ordenes ordenGuardada = ordenRepository.save(new Ordenes(/* datos de la orden */));

        // Clear el carrito
        carritoService.vaciarCarrito(idUsuario);

        return ordenGuardada;
    }
}
