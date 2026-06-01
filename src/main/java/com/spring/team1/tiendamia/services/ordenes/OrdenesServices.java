package com.spring.team1.tiendamia.services.ordenes;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.team1.tiendamia.models.carrito.Carrito;
import com.spring.team1.tiendamia.models.carrito.CarritoDetalle;
import com.spring.team1.tiendamia.models.ordenes.OrdenDetalle;
import com.spring.team1.tiendamia.models.ordenes.Ordenes;
import com.spring.team1.tiendamia.models.ordenes.DTO.CheckoutRequest;
import com.spring.team1.tiendamia.models.payload.orden.EstadoOrden;
import com.spring.team1.tiendamia.models.usuario.Usuarios;
import com.spring.team1.tiendamia.models.usuario.tarjetas.MarcasTarjeta;
import com.spring.team1.tiendamia.models.usuario.tarjetas.MetodosPago;
import com.spring.team1.tiendamia.models.usuario.ubicacion.DireccionesUsuarios;
import com.spring.team1.tiendamia.models.usuario.ubicacion.Distrito;
import com.spring.team1.tiendamia.models.productos.VariacionesProducto;
import com.spring.team1.tiendamia.repository.ordenes.*;
import com.spring.team1.tiendamia.repository.producto.variaciones.VariacionProductoRepository;
import com.spring.team1.tiendamia.repository.usuario.UsuariosRepository;
import com.spring.team1.tiendamia.repository.usuario.direcciones.DireccionesUsuariosRepository;
import com.spring.team1.tiendamia.repository.usuario.tarjetas.MarcasTarjetaRepository;
import com.spring.team1.tiendamia.repository.usuario.tarjetas.MetodosPagoRepository;
import com.spring.team1.tiendamia.services.carrito.CarritoService;

import jakarta.transaction.Transactional;

@Service
public class OrdenesServices {

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private DireccionesUsuariosRepository direccionesRepository;

    @Autowired
    private MarcasTarjetaRepository marcasTarjetaRepository;

    @Autowired
    private MetodosPagoRepository metodosPagoRepository;

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private VariacionProductoRepository variacionesRepository;

    @Autowired
    private OrdenRepository ordenRepository;
    
    @Autowired
    private OrdenDetalleRepository detalleRepository;

    @Transactional
    public Ordenes procesarCheckout(String correo, CheckoutRequest request) {
        // 1. Obtener usuario autenticado
        Usuarios usuario = usuariosRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Obtener el carrito actual y validar que no esté vacío
        Carrito carrito = carritoService.obtenerCarritoPorUsuario(usuario.getId());
        if (carrito.getDetalles() == null || carrito.getDetalles().isEmpty()) {
            throw new RuntimeException("El carrito está vacío, no se puede generar un pedido");
        }

        // 3. Validar stock en el último segundo antes de tocar la base de datos
        for (CarritoDetalle detalle : carrito.getDetalles()) {
            VariacionesProducto variante = detalle.getVariacion();
            if (variante.getStock() < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente de última hora para el producto: " + variante.getProducto().getNombre());
            }
        }

        // 4. Guardar la dirección de envío (Tabla: direcciones_usuario)
        DireccionesUsuarios nuevaDireccion = new DireccionesUsuarios();
        nuevaDireccion.setUsuario(usuario);
        nuevaDireccion.setDireccion(request.getDireccion().getDireccion());
        Distrito distrito = new Distrito();
        distrito.setNombre(request.getDireccion().getDistrito());
        nuevaDireccion.setReferencia(request.getDireccion().getReferencia());
        nuevaDireccion.setEs_principal(false);
        nuevaDireccion = direccionesRepository.save(nuevaDireccion);

        // 5. Registrar el método de pago simulado (Tabla: metodos_pago)
        MarcasTarjeta marca = marcasTarjetaRepository.findById(request.getPago().getMarca_tarjeta_id())
                .orElseThrow(() -> new RuntimeException("Marca de tarjeta inválida"));

        MetodosPago metodoPago = new MetodosPago();
        metodoPago.setUsuario(usuario);
        metodoPago.setPasarela(request.getPago().getPasarela());
        metodoPago.setCustomer_id(request.getPago().getCustomer_token());
        metodoPago.setUltimos_cuatro(request.getPago().getUltimos_cuatro());
        metodoPago.setMarca_tarjeta(marca);
        metodosPagoRepository.save(metodoPago);

        // 6. Crear la orden principal (Tabla: ordenes)
        Ordenes orden = new Ordenes();
        orden.setUsuario(usuario);
        orden.setDireccion(nuevaDireccion);
        orden.setTotal(carrito.getTotal()); // El total calculado en el backend del carrito
        orden.setEstado(EstadoOrden.PAGADO); // Al ser pasarela simulada exitosa, nace completada/pagada
        orden.setId_transaccion_pasarela(request.getPago().getCustomer_token());
        orden = ordenRepository.save(orden);

        // 7. Migrar detalles del carrito a detalles de orden y descontar stock
        orden.setDetalles(new ArrayList<>());
        for (CarritoDetalle detalle : carrito.getDetalles()) {
            VariacionesProducto variante = detalle.getVariacion();

            // REDUCIR STOCK
            variante.setStock(variante.getStock() - detalle.getCantidad());
            variacionesRepository.save(variante);

            // CREAR DETALLE DE ORDEN (Tabla: orden_detalles)
            OrdenDetalle ordenDetalle = new OrdenDetalle();
            ordenDetalle.setOrden(orden);
            ordenDetalle.setVariacion(variante);
            ordenDetalle.setCantidad(detalle.getCantidad());
            ordenDetalle.setPrecio_unitario(variante.getPrecio()); // Guardamos el precio del momento de compra
            detalleRepository.save(ordenDetalle);
            orden.getDetalles().add(ordenDetalle);
        }

        // 8. Vaciar el carrito de forma segura
        carritoService.vaciarCarrito(usuario.getId());

        return orden;
    }
}
