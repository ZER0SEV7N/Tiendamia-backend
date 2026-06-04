package com.spring.team1.tiendamia.controllers.cliente.carrito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.team1.tiendamia.models.carrito.Carrito;
import com.spring.team1.tiendamia.models.carrito.CarritoDetalle;
import com.spring.team1.tiendamia.models.carrito.DTO.CarritoRequest;
import com.spring.team1.tiendamia.models.carrito.DTO.CarritoResponse;
import com.spring.team1.tiendamia.services.carrito.CarritoService;
import com.spring.team1.tiendamia.util.Response;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//Controlador para manejar las operaciones del carrito de compras
@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    // Endpoint para obtener el carrito de un usuario autenticado
    // GET: /api/carrito
    @GetMapping
    public ResponseEntity<Response<CarritoResponse>> obtenerCarrito(Authentication authentication) {
        if (authentication == null || authentication.getName() == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Response<>(false, "No autenticado", null));

       
        String correo = authentication.getName();
        Carrito carrito = carritoService.obtenerCarritoPorCorreo(correo);
        CarritoResponse data = carritoService.convertirDto(carrito);

        return ResponseEntity.ok(new Response<>(true, "Carrito obtenido correctamente", data));
    }

    // Endpoint para agregar un item al carrito
    // POST: /api/carrito/agregar
    @PostMapping("/agregar")
    public ResponseEntity<Response<CarritoResponse>> agregarAlCarrito(Authentication authentication,
            @RequestBody CarritoRequest request) {
        if (authentication == null || authentication.getName() == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response<>(false, "No autenticado", null));

        
        String correo = authentication.getName();
        Carrito carrito = carritoService.obtenerCarritoPorCorreo(correo);
        Integer idUsuario = carrito.getUsuario().getId();
        boolean stockError = false;

        for (CarritoRequest.CarritoItemRequest item : request.getItems()) {
            CarritoDetalle detalle = carritoService.agregarAlCarrito(idUsuario, item.getIdVariante(),
                    item.getCantidad());
            if (detalle == null) {
                stockError = true;
                break;
            }
        }

        carrito = carritoService.obtenerCarritoPorUsuario(idUsuario);
        CarritoResponse data = carritoService.convertirDto(carrito);

        if (stockError)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(false, "No hay suficiente stock para uno o más items", data));

        return ResponseEntity.ok(new Response<>(true, "Items agregados al carrito correctamente", data));
    }

    // Endpoint para actualizar la cantidad de un item en el carrito
    // PATCH: /api/carrito/actualizar
    @PatchMapping("/actualizar")
    public ResponseEntity<Response<CarritoResponse>> actualizarCantidad(Authentication authentication,
            @RequestBody CarritoRequest request) {
        if (authentication == null || authentication.getName() == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Response<>(false, "No autenticado", null));

        
        String correo = authentication.getName();
        Carrito carrito = carritoService.obtenerCarritoPorCorreo(correo);
        Integer idUsuario = carrito.getUsuario().getId();
        boolean stockError = false;

        for (CarritoRequest.CarritoItemRequest item : request.getItems()) {
            CarritoDetalle detalle = carritoService.actualizarCantidad(item.getIdVariante(), item.getCantidad());
            if (detalle == null) {
                stockError = true;
                break;
            }
        }

        carrito = carritoService.obtenerCarritoPorUsuario(idUsuario);
        CarritoResponse data = carritoService.convertirDto(carrito);

        if (stockError)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(false, "No hay suficiente stock para uno o más items", data));

        return ResponseEntity.ok(new Response<>(true, "Cantidad actualizada correctamente", data));
    }

    // Endpoint para eliminar un item del carrito
    // DELETE: /api/carrito/eliminar
    @DeleteMapping("/eliminar/{idDetalle}")
    public ResponseEntity<Response<CarritoResponse>> eliminarDelCarrito(Authentication authentication,
            @PathVariable Integer idDetalle) {
        if (authentication == null || authentication.getName() == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Response<>(false, "No autenticado", null));

        
        String correo = authentication.getName();
        Carrito carrito = carritoService.obtenerCarritoPorCorreo(correo);

        boolean eliminado = carritoService.eliminarDelCarrito(idDetalle);

        if (!eliminado)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(false, "No se encontró el item en el carrito", null));

        carrito = carritoService.obtenerCarritoPorUsuario(carrito.getUsuario().getId());
        CarritoResponse data = carritoService.convertirDto(carrito);

        return ResponseEntity.ok(new Response<>(true, "Item eliminado del carrito correctamente", data));
    }

    // Endpoint para vaciar el carrito de un usuario
    // DELETE: /api/carrito/vaciar
    @DeleteMapping("/vaciar")
    public ResponseEntity<Response<String>> vaciarCarrito(Authentication authentication) {
        if (authentication == null || authentication.getName() == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Response<>(false, "No autenticado", null));

        String correo = authentication.getName();
        Carrito carrito = carritoService.obtenerCarritoPorCorreo(correo);

        boolean vaciado = carritoService.vaciarCarrito(carrito.getUsuario().getId());

        if (!vaciado)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(false, "El carrito ya está vacío", null));

        return ResponseEntity.ok(new Response<>(true, "Carrito vaciado correctamente", null));
    }
}