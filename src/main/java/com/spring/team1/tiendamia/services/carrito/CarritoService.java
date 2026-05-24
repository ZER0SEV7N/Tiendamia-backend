package com.spring.team1.tiendamia.services.carrito;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.team1.tiendamia.Models.Carrito.Carrito;
import com.spring.team1.tiendamia.Models.Carrito.CarritoDetalle;
import com.spring.team1.tiendamia.Models.Carrito.DTO.CarritoResponse;
import com.spring.team1.tiendamia.Models.Productos.Variaciones_Producto;
import com.spring.team1.tiendamia.Models.Usuario.Usuarios;
import com.spring.team1.tiendamia.repository.carrito.CarritoDetalleRepository;
import com.spring.team1.tiendamia.repository.carrito.CarritoRepository;
import com.spring.team1.tiendamia.repository.productos.VariacionesProductosRepository;
import com.spring.team1.tiendamia.repository.usuario.UsuariosRepository;

import jakarta.transaction.Transactional;

//Servicio para manejar la logica del carrito
@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private VariacionesProductosRepository variacionesRepository;

    @Autowired
    private CarritoDetalleRepository detalleRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    //Metodo para agregar un item al carrito
    @Transactional
    public CarritoDetalle agregarAlCarrito(Integer idUsuario, Integer idVariacion, Integer cantidad){
        Variaciones_Producto variante = variacionesRepository.findById(idVariacion).orElse(null);
        if(variante == null || cantidad <= 0) return null; 
    
        Carrito carrito = obtenerOCrearCarrito(idUsuario);
        CarritoDetalle itemExistente = detalleRepository.findByCarritoIdAndVariacionId(carrito.getId(), idVariacion);

        if(itemExistente != null){
            int nuevaCantidad = itemExistente.getCantidad() + cantidad;

            if(nuevaCantidad > variante.getStock()) return null;

            itemExistente.setCantidad(nuevaCantidad);
            itemExistente.setPrecio(variante.getPrecio() * nuevaCantidad);
            detalleRepository.save(itemExistente);
        } else {
            if(cantidad > variante.getStock()) return null;

            itemExistente = new CarritoDetalle();
            itemExistente.setCarrito(carrito);
            itemExistente.setVariacion(variante);
            itemExistente.setCantidad(cantidad);
            itemExistente.setPrecio(cantidad * variante.getPrecio());
            detalleRepository.save(itemExistente);
            carrito.getDetalles().add(itemExistente);
        }

        recalcularTodo(carrito);
        return itemExistente;
    }


    //Metodo para obtener el carrito de un usuario
    public Carrito obtenerCarritoPorUsuario(Integer idUsuario){
        return obtenerOCrearCarrito(idUsuario);
    }

    //Metodo para obtener el carrito usando el correo del usuario autenticado por JWT
    public Carrito obtenerCarritoPorCorreo(String correo){
        Usuarios usuario = usuariosRepository.findByCorreo(correo)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con correo: " + correo));

        return obtenerOCrearCarrito(usuario.getId());
    }

    //Metodo para eliminar un item del carrito
    @Transactional
    public boolean eliminarDelCarrito(Integer idDetalle){
        CarritoDetalle item = detalleRepository.findById(idDetalle).orElse(null);
        if(item == null) return false;
        
        Carrito carrito = item.getCarrito();
        carrito.getDetalles().remove(item);
        detalleRepository.delete(item);

        recalcularTodo(carrito);
        return true;
    }

    //Metodo para actualizar la cantidad de un item del carrito
    @Transactional
    public CarritoDetalle actualizarCantidad(Integer idDetalle, Integer nuevaCantidad){
        CarritoDetalle item = detalleRepository.findById(idDetalle).orElse(null);
        if(item == null || nuevaCantidad <= 0) return null;

        Variaciones_Producto variante = item.getVariacion();

        if(nuevaCantidad > variante.getStock()) return null;

        item.setCantidad(nuevaCantidad);
        item.setPrecio(nuevaCantidad * variante.getPrecio());
        detalleRepository.save(item);

        recalcularTodo(item.getCarrito());
        return item;
    }

    //Metodo para vaciar el carrito de un usuario
    @Transactional
    public boolean vaciarCarrito(Integer idUsuario){
        Carrito carrito = obtenerOCrearCarrito(idUsuario);
        if(carrito.getDetalles().isEmpty()) return false;

        carrito.getDetalles().clear();
        carrito.setTarifa(0.0);
        carrito.setEnvio(0.0);
        carrito.setTotal(0.0);
        carritoRepository.save(carrito);
        return true;
    }

    //Metodo para buscar el carrito de un usuario, o crear uno nuevo
    private Carrito obtenerOCrearCarrito(Integer idUsuario){
        List<Carrito> carrito = carritoRepository.findByUsuarioId(idUsuario);
        if(!carrito.isEmpty()) return carrito.get(0);
        
        Carrito nuevoCarrito = new Carrito();

        Usuarios usuario = usuariosRepository.findById(idUsuario)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));
        
        nuevoCarrito.setUsuario(usuario);
        nuevoCarrito.setTarifa(0.0);
        nuevoCarrito.setEnvio(0.0);
        nuevoCarrito.setTotal(0.0);
        return carritoRepository.save(nuevoCarrito);
    }

    private void recalcularTodo(Carrito carrito){
        double subtotal = 0.0;
        for(CarritoDetalle item : carrito.getDetalles())
            subtotal += item.getPrecio();
        
        double tarifa = subtotal * 0.10; //Tarifa fija del 10% sobre el subtotal

        //Envio es el 20% del subtotal, pero solo si el subtotal es mayor a 0
        double envio = 0.0;
        if (subtotal > 0.0) 
            envio = subtotal * 0.20;

        double total = subtotal + tarifa + envio;

        carrito.setTarifa(tarifa);
        carrito.setEnvio(envio);
        carrito.setTotal(total);
        carritoRepository.save(carrito);
    }

    //Metodo para enviar el dto de response
    public CarritoResponse convertirDto(Carrito carrito){
        CarritoResponse response = new CarritoResponse();
        response.setIdCarrito(carrito.getId());
        response.setEnvio(carrito.getEnvio());
        response.setTarifa(carrito.getTarifa());
        response.setTotal(carrito.getTotal());

        response.setItems(new ArrayList<>());

        for(CarritoDetalle detalle : carrito.getDetalles()){
            CarritoResponse.CarritoItemResponse item = new CarritoResponse.CarritoItemResponse();
            item.setIdVariante(detalle.getVariacion().getId());
            item.setVariacion(detalle.getVariacion().getProducto().getNombre() + " - " + detalle.getVariacion().getProducto().getDescripcion());
            item.setCantidad(detalle.getCantidad());
            item.setPrecio(detalle.getPrecio());
            response.getItems().add(item);
        }
        return response;
    }

}
