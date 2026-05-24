package com.spring.team1.tiendamia.services.carrito;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.team1.tiendamia.Models.Carrito.Carrito;
import com.spring.team1.tiendamia.Models.Carrito.CarritoDetalle;
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

    @Transactional
    public CarritoDetalle agregarAlCarrito(Integer idUsuario, Integer idVariacion, Integer cantidad){
        Variaciones_Producto variante = variacionesRepository.findById(idVariacion).orElse(null);
        if(variante == null || cantidad <= 0 || variante.getStock() < cantidad)
            return null; 
    
        Carrito carrito = obtenerOCrearCarrito(idUsuario);

        CarritoDetalle itemExistente = detalleRepository.findByCarritoIdAndVariacionId(carrito.getId(), idVariacion);

        if(itemExistente != null){
            int nuevaCantidad = itemExistente.getCantidad() + cantidad;

            if(nuevaCantidad > (variante.getStock() + itemExistente.getCantidad()))
                return null;

            itemExistente.setCantidad(nuevaCantidad);
            itemExistente.setPrecio(variante.getPrecio() * nuevaCantidad);
            detalleRepository.save(itemExistente);
        } else {
            itemExistente = new CarritoDetalle();
            itemExistente.setCarrito(carrito);
            itemExistente.setVariacion(variante);
            itemExistente.setCantidad(cantidad);
            itemExistente.setPrecio(cantidad * variante.getPrecio());
            detalleRepository.save(itemExistente);
            carrito.getDetalles().add(itemExistente);
        }
        variante.setStock(variante.getStock() - cantidad);
        variacionesRepository.save(variante);

        recalcularTodo(carrito);
        return itemExistente;
    }

    //Metodo para obtener el carrito de un usuario
    public Carrito obtenerCarritoPorUsuario(Integer idUsuario){
        return obtenerOCrearCarrito(idUsuario);
    }

    //Metodo para eliminar un item del carrito
    @Transactional
    public boolean eliminarDelCarrito(Integer idDetalle){
        CarritoDetalle item = detalleRepository.findById(idDetalle).orElse(null);

        if(item == null ) return false;
        
        Variaciones_Producto variante = item.getVariacion();

        if(variante != null){
            variante.setStock(variante.getStock() + item.getCantidad());
            variacionesRepository.save(variante);
        }

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
        int diferencia = nuevaCantidad - item.getCantidad();

        if(diferencia > 0 && variante.getStock() < diferencia) return null;

        item.setCantidad(nuevaCantidad);
        item.setPrecio(nuevaCantidad * variante.getPrecio());
        detalleRepository.save(item);

        variante.setStock(variante.getStock() - diferencia);
        variacionesRepository.save(variante);

        recalcularTodo(item.getCarrito());
        return item;
    }

    //Metodo para vaciar el carrito de un usuario
    @Transactional
    public boolean vaciarCarrito(Integer idUsuario){
        Carrito carrito = obtenerOCrearCarrito(idUsuario);
        if(carrito.getDetalles().isEmpty()) return false;

        for(CarritoDetalle item : carrito.getDetalles()){
            Variaciones_Producto variante = item.getVariacion();
            if(variante != null){
                variante.setStock(variante.getStock() + item.getCantidad());
                variacionesRepository.save(variante);
            }
        }

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
        double tarifa = 0.0; //Equivale el 10% del total
        double envio = 0.0; //Equivale 
        double total = 0.0;
        for(CarritoDetalle item : carrito.getDetalles()){
            tarifa += item.getPrecio();
        }
        carrito.setTarifa(tarifa);
        carrito.setTotal(tarifa + carrito.getEnvio());
    }

}
