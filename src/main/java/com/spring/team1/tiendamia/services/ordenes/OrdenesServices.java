package com.spring.team1.tiendamia.Services.ordenes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.team1.tiendamia.Models.Carrito.Carrito;
import com.spring.team1.tiendamia.Models.Carrito.CarritoDetalle;
import com.spring.team1.tiendamia.Models.Ordenes.Ordenes;
import com.spring.team1.tiendamia.Models.Productos.VariacionesProducto;
import com.spring.team1.tiendamia.Repository.ordenes.*;
import com.spring.team1.tiendamia.Repository.productos.VariacionesProductosRepository;
import com.spring.team1.tiendamia.Repository.usuario.MetodosPagoRepository;
import com.spring.team1.tiendamia.Repository.usuario.UsuariosRepository;
import com.spring.team1.tiendamia.Services.carrito.CarritoService;

import jakarta.transaction.Transactional;

@Service
public class OrdenesServices {

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private DireccionesRepository direccionesRepository;

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
        DireccionesUsuario nuevaDireccion = new DireccionesUsuario();
        nuevaDireccion.setUsuario(usuario);
        nuevaDireccion.setDireccion(request.getDireccion().getDireccion());
        nuevaDireccion.setDistrito(request.getDireccion().getDistrito());
        nuevaDireccion.setProvincia(request.getDireccion().getProvincia());
        nuevaDireccion.setDepartamento(request.getDireccion().getDepartamento());
        nuevaDireccion.setReferencia(request.getDireccion().getReferencia());
        nuevaDireccion.setEsPrincipal(false);
        nuevaDireccion = direccionesRepository.save(nuevaDireccion);

        // 5. Registrar el método de pago simulado (Tabla: metodos_pago)
        MarcasTarjeta marca = marcasTarjetaRepository.findById(request.getPago().getMarca_tarjeta_id())
                .orElseThrow(() -> new RuntimeException("Marca de tarjeta inválida"));

        MetodosPago metodoPago = new MetodosPago();
        metodoPago.setUsuario(usuario);
        metodoPago.setPasarela(request.getPago().getPasarela());
        metodoPago.setCustomerToken(request.getPago().getCustomer_token());
        metodoPago.setUltimosCuatro(request.getPago().getUltimos_cuatro());
        metodoPago.setMarcaTarjeta(marca);
        metodosPagoRepository.save(metodoPago);

        // 6. Crear la orden principal (Tabla: ordenes)
        Ordenes orden = new Ordenes();
        orden.setUsuario(usuario);
        orden.setDireccion(nuevaDireccion);
        orden.setTotal(carrito.getTotal()); // El total calculado en el backend del carrito
        orden.setEstado("completado"); // Al ser pasarela simulada exitosa, nace completada/pagada
        orden.setIdTransaccionPasarela(request.getPago().getCustomer_token());
        orden = ordenesRepository.save(orden);

        // 7. Migrar detalles del carrito a detalles de orden y descontar stock
        orden.setDetalles(new ArrayList<>());
        for (CarritoDetalle detalle : carrito.getDetalles()) {
            VariacionesProducto variante = detalle.getVariacion();

            // REDUCIR STOCK
            variante.setStock(variante.getStock() - detalle.getCantidad());
            variacionesRepository.save(variante);

            // CREAR DETALLE DE ORDEN (Tabla: orden_detalles)
            OrdenDetalles ordenDetalle = new OrdenDetalles();
            ordenDetalle.setOrden(orden);
            ordenDetalle.setVariacion(variante);
            ordenDetalle.setCantidad(detalle.getCantidad());
            ordenDetalle.setPrecioUnitario(variante.getPrecio()); // Guardamos el precio del momento de compra
            
            ordenDetallesRepository.save(ordenDetalle);
            orden.getDetalles().add(ordenDetalle);
        }

        // 8. Vaciar el carrito de forma segura
        carritoService.vaciarCarrito(usuario.getId());

        return orden;
    }
}
