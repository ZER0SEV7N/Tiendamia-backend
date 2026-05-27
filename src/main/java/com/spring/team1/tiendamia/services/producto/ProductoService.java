package com.spring.team1.tiendamia.services.producto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.team1.tiendamia.models.payload.producto.ProductoRequest;
import com.spring.team1.tiendamia.models.productos.Atributos;
import com.spring.team1.tiendamia.models.productos.Categorias;
import com.spring.team1.tiendamia.models.productos.Marcas;
import com.spring.team1.tiendamia.models.productos.Productos;
import com.spring.team1.tiendamia.models.productos.ValoresAtributos;
import com.spring.team1.tiendamia.models.productos.VariacionValores;
import com.spring.team1.tiendamia.models.productos.VariacionesProducto;
import com.spring.team1.tiendamia.repository.categoria.CategoriaRepository;
import com.spring.team1.tiendamia.repository.marca.MarcaRepository;
import com.spring.team1.tiendamia.repository.producto.AtributosRepository;
import com.spring.team1.tiendamia.repository.producto.ProductoRepository;
import com.spring.team1.tiendamia.repository.producto.ValoresAtributosRepository;
import com.spring.team1.tiendamia.repository.producto.VariacionProductoRepository;
import com.spring.team1.tiendamia.repository.producto.VariacionValoresRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductoService {
    @Autowired private ProductoRepository productoRepository; 
    @Autowired private VariacionProductoRepository variacionProducto;
    @Autowired private VariacionValoresRepository variacionValores;   
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
            VariacionesProducto variacion = new VariacionesProducto();
            variacion.setProducto(productoGuardado);
            variacion.setCodigo_inventario(VarReq.getCodigoInventario());
            variacion.setPrecio(VarReq.getPrecio());
            variacion.setStock(VarReq.getStock());
            variacion.setImagenUrl(VarReq.getImagenUrl());
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
                ValoresAtributos valorAtributo = valoresAtributosRepository
                        .findByValorIgnoreCaseAndAtributoId(CaracReq.getValorTexto(), atributo.getId())
                        .orElseGet(() -> {
                            ValoresAtributos nuevoValor = new ValoresAtributos();
                            nuevoValor.setValor(CaracReq.getValorTexto());
                            nuevoValor.setAtributo(atributo);
                            return valoresAtributosRepository.save(nuevoValor);
                        });
                // Asociar la variación con el valor del atributo
                VariacionValores variacionValor = new VariacionValores(variacion, valorAtributo);
                variacionValores.save(variacionValor);
            }
        }
        // Retornar el producto guardado con sus variaciones y características
        return "Producto creado exitosamente con ID: ";
    }
}
