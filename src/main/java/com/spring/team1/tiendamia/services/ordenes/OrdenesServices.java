package com.spring.team1.tiendamia.Services.ordenes;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.team1.tiendamia.Models.Carrito.Carrito;
import com.spring.team1.tiendamia.Models.Carrito.CarritoDetalle;
import com.spring.team1.tiendamia.Models.Ordenes.OrdenDetalle;
import com.spring.team1.tiendamia.Models.Ordenes.Ordenes;
import com.spring.team1.tiendamia.Models.Ordenes.DTO.CheckoutRequest;
import com.spring.team1.tiendamia.Models.Productos.VariacionesProducto;
import com.spring.team1.tiendamia.Models.Usuario.DireccionesUsuarios;
import com.spring.team1.tiendamia.Models.Usuario.MarcasTarjeta;
import com.spring.team1.tiendamia.Models.Usuario.MetodosPago;
import com.spring.team1.tiendamia.Models.Usuario.Usuarios;
import com.spring.team1.tiendamia.Models.payload.EstadoOrden;
import com.spring.team1.tiendamia.Repository.ordenes.*;
import com.spring.team1.tiendamia.Repository.productos.VariacionesProductosRepository;
import com.spring.team1.tiendamia.Repository.usuario.DireccionesUsuariosRepository;
import com.spring.team1.tiendamia.Repository.usuario.MarcasTarjetaRepository;
import com.spring.team1.tiendamia.Repository.usuario.MetodosPagoRepository;
import com.spring.team1.tiendamia.Repository.usuario.UsuariosRepository;
import com.spring.team1.tiendamia.Services.carrito.CarritoService;

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
    private VariacionesProductosRepository variacionesRepository;

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
        nuevaDireccion.setDistrito(request.getDireccion().getDistrito());
        nuevaDireccion.setProvincia(request.getDireccion().getProvincia());
        nuevaDireccion.setDepartamento(request.getDireccion().getDepartamento());
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
