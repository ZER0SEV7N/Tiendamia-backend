package com.spring.team1.tiendamia.Controllers.carrito;

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

import com.spring.team1.tiendamia.Models.Carrito.Carrito;
import com.spring.team1.tiendamia.Models.Carrito.CarritoDetalle;
import com.spring.team1.tiendamia.Services.carrito.CarritoService;
import com.spring.team1.tiendamia.Models.Carrito.DTO.CarritoRequest;
import com.spring.team1.tiendamia.Models.Carrito.DTO.CarritoResponse;
import com.spring.team1.tiendamia.Models.payload.response;
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
    public ResponseEntity<response<CarritoResponse>> obtenerCarrito(Authentication authentication) {
        if (authentication == null || authentication.getName() == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new response<>(false, "No autenticado", null));

        try {
            String correo = authentication.getName();
            Carrito carrito = carritoService.obtenerCarritoPorCorreo(correo);
            CarritoResponse data = carritoService.convertirDto(carrito);

            return ResponseEntity.ok(new response<>(true, "Carrito obtenido correctamente", data));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new response<>(false, e.getMessage(), null));
        }
    }

    // Endpoint para agregar un item al carrito
    // POST: /api/carrito/agregar
    @PostMapping("/agregar")
    public ResponseEntity<response<CarritoResponse>> agregarAlCarrito(Authentication authentication,
            @RequestBody CarritoRequest request) {
        if (authentication == null || authentication.getName() == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new response<>(false, "No autenticado", null));

        try {
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
                        .body(new response<>(false, "No hay suficiente stock para uno o más items", data));

            return ResponseEntity.ok(new response<>(true, "Items agregados al carrito correctamente", data));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new response<>(false, "Error al agregar al carrito: " + e.getMessage(), null));
        }
    }

    // Endpoint para actualizar la cantidad de un item en el carrito
    // PATCH: /api/carrito/actualizar
    @PatchMapping("/actualizar")
    public ResponseEntity<response<CarritoResponse>> actualizarCantidad(Authentication authentication,
            @RequestBody CarritoRequest request) {
        if (authentication == null || authentication.getName() == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new response<>(false, "No autenticado", null));

        try {
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
                        .body(new response<>(false, "No hay suficiente stock para uno o más items", data));

            return ResponseEntity.ok(new response<>(true, "Cantidad actualizada correctamente", data));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new response<>(false, "Error al actualizar la cantidad: " + e.getMessage(), null));
        }
    }

    // Endpoint para eliminar un item del carrito
    // DELETE: /api/carrito/eliminar
    @DeleteMapping("/eliminar/{idDetalle}")
    public ResponseEntity<response<CarritoResponse>> eliminarDelCarrito(Authentication authentication,
            @PathVariable Integer idDetalle) {
        if (authentication == null || authentication.getName() == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new response<>(false, "No autenticado", null));

        try {
            String correo = authentication.getName();
            Carrito carrito = carritoService.obtenerCarritoPorCorreo(correo);

            boolean eliminado = carritoService.eliminarDelCarrito(idDetalle);

            if (!eliminado)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new response<>(false, "No se encontró el item en el carrito", null));

            carrito = carritoService.obtenerCarritoPorUsuario(carrito.getUsuario().getId());
            CarritoResponse data = carritoService.convertirDto(carrito);

            return ResponseEntity.ok(new response<>(true, "Item eliminado del carrito correctamente", data));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new response<>(false, "Error al eliminar del carrito: " + e.getMessage(), null));
        }
    }

    // Endpoint para vaciar el carrito de un usuario
    // DELETE: /api/carrito/vaciar
    @DeleteMapping("/vaciar")
    public ResponseEntity<response<String>> vaciarCarrito(Authentication authentication) {
        if (authentication == null || authentication.getName() == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new response<>(false, "No autenticado", null));

        try {
            String correo = authentication.getName();
            Carrito carrito = carritoService.obtenerCarritoPorCorreo(correo);

            boolean vaciado = carritoService.vaciarCarrito(carrito.getUsuario().getId());

            if (!vaciado)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new response<>(false, "El carrito ya está vacío", null));

            return ResponseEntity.ok(new response<>(true, "Carrito vaciado correctamente", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new response<>(false, "Error al vaciar el carrito: " + e.getMessage(), null));
        }
    }
}