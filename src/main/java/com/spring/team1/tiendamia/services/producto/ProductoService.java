package com.spring.team1.tiendamia.services.producto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.team1.tiendamia.Models.payload.producto.ProductoRequest;
import com.spring.team1.tiendamia.Models.productos.Atributos;
import com.spring.team1.tiendamia.Models.productos.Categorias;
import com.spring.team1.tiendamia.Models.productos.Marcas;
import com.spring.team1.tiendamia.Models.productos.Productos;
import com.spring.team1.tiendamia.Models.productos.Valores_Atributos;
import com.spring.team1.tiendamia.Models.productos.Variacion_Valores;
import com.spring.team1.tiendamia.Models.productos.Variaciones_Producto;
import com.spring.team1.tiendamia.repository.categoria.CategoriaRepository;
import com.spring.team1.tiendamia.repository.marca.MarcaRepository;
import com.spring.team1.tiendamia.repository.producto.AtributosRepository;
import com.spring.team1.tiendamia.repository.producto.ProductoRepository;
import com.spring.team1.tiendamia.repository.producto.ValoresAtributosRepository;
import com.spring.team1.tiendamia.repository.producto.VariacionProducto;
import com.spring.team1.tiendamia.repository.producto.VariacionValores;

import jakarta.transaction.Transactional;

@Service
public class ProductoService {
    @Autowired private ProductoRepository productoRepository; 
    @Autowired private VariacionProducto variacionProducto;
    @Autowired private VariacionValores variacionValores;   
    @Autowired private AtributosRepository atributosRepository;
    @Autowired private ValoresAtributosRepository valoresAtributosRepository;
    @Autowired private CategoriaRepository categoriaRepository;
    @Autowired private MarcaRepository marcaRepository;

    @Transactional
    public String crearProductoConVariacion(ProductoRequest request) {
        // Validar categoría y marca
        Categorias categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        Marcas marca = marcaRepository.findById(request.getMarcaId())
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));

        // Crear producto
        Productos producto = new Productos();
        producto.setNombre(request.getNombre());
        producto.setSlug(request.getSlug());
        producto.setDescripcion(request.getDescripcion());
        producto.setImagen_url(request.getImagenUrl());
        producto.setCategoria(categoria);
        producto.setMarca(marca);
        // Guardar producto
        Productos productoGuardado = productoRepository.save(producto);
        // Crear variaciones
        for(ProductoRequest.VariacionInicialRequest VarReq : request.getVariaciones() ){
            Variaciones_Producto variacion = new Variaciones_Producto();
            variacion.setProducto(productoGuardado);
            variacion.setCodigo_inventario(VarReq.getCodigoInventario());
            variacion.setPrecio(VarReq.getPrecio());
            variacion.setStock(VarReq.getStock());
            variacion.setImagen_url(VarReq.getImagenUrl());
            // Guardar variación
            variacionProducto.save(variacion);

            // Crear o procesar valores de atributos para la variación
            for(ProductoRequest.CaracteristicaRequest CaracReq : VarReq.getCaracteristicas()){
                // Buscar o crear el atributo
                Atributos atributo = atributosRepository.findByNombreIgnoreCase(CaracReq.getAtributoNombre())
                        .orElseGet(() -> {
                            Atributos nuevoAtributo = new Atributos();
                            nuevoAtributo.setNombre(CaracReq.getAtributoNombre());
                            return atributosRepository.save(nuevoAtributo);
                        });
                // Buscar o crear el valor del atributo
                Valores_Atributos valorAtributo = valoresAtributosRepository
                        .findByValorIgnoreCaseAndAtributoId(CaracReq.getValorTexto(), atributo.getId())
                        .orElseGet(() -> {
                            Valores_Atributos nuevoValor = new Valores_Atributos();
                            nuevoValor.setValor(CaracReq.getValorTexto());
                            nuevoValor.setAtributo(atributo);
                            return valoresAtributosRepository.save(nuevoValor);
                        });
                // Asociar la variación con el valor del atributo
                Variacion_Valores variacionValor = new Variacion_Valores(variacion, valorAtributo);
                variacionValores.save(variacionValor);
            }
        }
        // Retornar el producto guardado con sus variaciones y características
        return "Producto creado exitosamente con ID: ";
    }
}
