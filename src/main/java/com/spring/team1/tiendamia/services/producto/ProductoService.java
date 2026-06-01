package com.spring.team1.tiendamia.services.producto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.team1.tiendamia.models.payload.producto.ProductoDetalleDTO;
import com.spring.team1.tiendamia.models.payload.producto.ProductoEditRequest;
import com.spring.team1.tiendamia.models.payload.producto.ProductoList;
import com.spring.team1.tiendamia.models.payload.producto.ProductoRequest;
import com.spring.team1.tiendamia.models.payload.producto.VariacionRequest;
import com.spring.team1.tiendamia.models.productos.Productos;
import com.spring.team1.tiendamia.models.productos.ValoresAtributos;
import com.spring.team1.tiendamia.models.productos.VariacionValores;
import com.spring.team1.tiendamia.models.productos.VariacionesProducto;
import com.spring.team1.tiendamia.models.productos.carateristicas.Atributos;
import com.spring.team1.tiendamia.models.productos.carateristicas.Categorias;
import com.spring.team1.tiendamia.models.productos.carateristicas.Marcas;
import com.spring.team1.tiendamia.repository.producto.ProductoRepository;
import com.spring.team1.tiendamia.repository.producto.categoria.CategoriaRepository;
import com.spring.team1.tiendamia.repository.producto.marca.MarcaRepository;
import com.spring.team1.tiendamia.repository.producto.variaciones.AtributosRepository;
import com.spring.team1.tiendamia.repository.producto.variaciones.ValoresAtributosRepository;
import com.spring.team1.tiendamia.repository.producto.variaciones.VariacionProductoRepository;
import com.spring.team1.tiendamia.repository.producto.variaciones.VariacionValoresRepository;

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

    public List<ProductoList> obtenerProductosParaCatalogo() {
        return productoRepository.findAll().stream().map(prod -> {
            ProductoList dto = new ProductoList();
            dto.setId(prod.getId());
            dto.setNombre(prod.getNombre());
            dto.setSlug(prod.getSlug());
            dto.setImagenUrl(prod.getImagen_url());
            dto.setDescripcion(prod.getDescripcion());
            dto.setNombreCategoria(prod.getCategoria() != null ? prod.getCategoria().getNombre() : "Sin Categoría");
            dto.setNombreMarca(prod.getMarca() != null ? prod.getMarca().getNombre() : "Sin Marca");
            dto.setEstado(prod.getEstado());
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public ProductoDetalleDTO obtenerProductoDetalle(Integer id) {
        Productos prod = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        ProductoDetalleDTO dto = new ProductoDetalleDTO();
        dto.setId(prod.getId());
        dto.setNombre(prod.getNombre());
        dto.setSlug(prod.getSlug());
        dto.setImagenUrl(prod.getImagen_url());
        dto.setDescripcion(prod.getDescripcion());
        dto.setNombreCategoria(prod.getCategoria() != null ? prod.getCategoria().getNombre() : "Sin Categoría");
        dto.setNombreMarca(prod.getMarca() != null ? prod.getMarca().getNombre() : "Sin Marca");

        List<ProductoDetalleDTO.VariacionDTO> listaVarDTO = prod.getVariaciones().stream().map(v -> {
            ProductoDetalleDTO.VariacionDTO vDto = new ProductoDetalleDTO.VariacionDTO();
            vDto.setId(v.getId());
            vDto.setCodigoInventario(v.getCodigoInventario());
            vDto.setPrecio(v.getPrecio());
            vDto.setStock(v.getStock());
            vDto.setImagenUrl(v.getImagenUrl());

            List<ProductoDetalleDTO.CaracteristicaDTO> caracDTOs = v.getVariacionValores().stream().map(vv -> {
                ProductoDetalleDTO.CaracteristicaDTO cDto = new ProductoDetalleDTO.CaracteristicaDTO();
                cDto.setAtributoNombre(vv.getValorAtributo().getAtributo().getNombre());
                cDto.setValorTexto(vv.getValorAtributo().getValor());
                return cDto;
            }).collect(Collectors.toList());

            vDto.setCaracteristicas(caracDTOs);
            return vDto;
        }).collect(Collectors.toList());

        dto.setVariaciones(listaVarDTO);
        return dto;
    }

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
            variacion.setCodigoInventario(VarReq.getCodigoInventario());
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

    @Transactional
    public String crearVariacionDelProducto(Integer productoId, VariacionRequest request) {
        // Verificar si el producto realmente existe
        Productos producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con el ID: " + productoId));

        // Validamos que no exista otra variación con el mismo código de inventario
        if (variacionProducto.findByCodigoInventario(request.getCodigoInventario()).isPresent()) {
            throw new RuntimeException("Ya existe una variación con el código de inventario: " + request.getCodigoInventario());
        }

        // Crear e insertar la nueva variación vinculada al producto encontrado
        VariacionesProducto nuevaVariacion = new VariacionesProducto();
        nuevaVariacion.setProducto(producto);
        nuevaVariacion.setCodigoInventario(request.getCodigoInventario());
        nuevaVariacion.setPrecio(request.getPrecio());
        nuevaVariacion.setStock(request.getStock());
        nuevaVariacion.setImagenUrl(request.getImagenUrl());
        
        // Guardar variación en la BD
        VariacionesProducto variacionGuardada = variacionProducto.save(nuevaVariacion);

        // Procesar las características de esta nueva variación
        for (VariacionRequest.CaracteristicaRequest caracReq : request.getCaracteristicas()) {
            
            // Buscar o crear el Atributo (Ej: Color)
            Atributos atributo = atributosRepository.findByNombreIgnoreCase(caracReq.getAtributoNombre())
                    .orElseGet(() -> {
                        Atributos nuevoAtributo = new Atributos();
                        nuevoAtributo.setNombre(caracReq.getAtributoNombre());
                        return atributosRepository.save(nuevoAtributo);
                    });

            // Buscar o crear el Valor del Atributo (Ej: Azul)
            ValoresAtributos valorAtributo = valoresAtributosRepository
                    .findByValorIgnoreCaseAndAtributoId(caracReq.getValorTexto(), atributo.getId())
                    .orElseGet(() -> {
                        ValoresAtributos nuevoValor = new ValoresAtributos();
                        nuevoValor.setValor(caracReq.getValorTexto());
                        nuevoValor.setAtributo(atributo);
                        return valoresAtributosRepository.save(nuevoValor);
                    });

            // Registrar la relación en la tabla intermedia (VariacionValores)
            VariacionValores variacionValor = new VariacionValores(variacionGuardada, valorAtributo);
            variacionValores.save(variacionValor);
        }

        return "Nueva variación añadida con éxito al producto: " + producto.getNombre();
    }

    @Transactional
    public String editarProducto(Integer id, ProductoEditRequest request) {
        // Buscar el producto existente
        Productos producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        // Validar que las nuevas categorías y marcas existan
        Categorias categoria = categoriaRepository.findById(request.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        Marcas marca = marcaRepository.findById(request.getIdMarca())
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));

        // Actualizar los campos en la tabla 'producto'
        producto.setNombre(request.getNombre());
        producto.setSlug(request.getSlug());
        producto.setImagen_url(request.getImagenUrl());
        producto.setDescripcion(request.getDescripcion());
        producto.setEstado(request.getEstado() != null ? request.getEstado() : producto.getEstado());
        producto.setCategoria(categoria);
        producto.setMarca(marca);

        productoRepository.save(producto);
        return "Datos del producto actualizados correctamente";
    }

    @Transactional
    public String editarVariaciones(String codigoInventario, VariacionRequest request) {
        // Buscar la variación
        VariacionesProducto variacion = variacionProducto.findByCodigoInventario(codigoInventario)
                .orElseThrow(() -> new RuntimeException("Variación no encontrada con código de inventario: " + codigoInventario));

        // Actualizar datos directos de la variación
        variacion.setCodigoInventario(request.getCodigoInventario());
        variacion.setPrecio(request.getPrecio());
        variacion.setStock(request.getStock());
        variacion.setImagenUrl(request.getImagenUrl());
        variacionProducto.save(variacion);

        // Borramos todas las asociaciones antiguas de esta variación específica antes de meter las nuevas
        variacionValores.deleteByVariacion(variacion);

        // Procesamos las nuevas características
        for (VariacionRequest.CaracteristicaRequest caracReq : request.getCaracteristicas()) {
            
            // Buscamos o creamos en la tabla atributo
            Atributos atributo = atributosRepository.findByNombreIgnoreCase(caracReq.getAtributoNombre())
                    .orElseGet(() -> {
                        Atributos nuevoAtributo = new Atributos();
                        nuevoAtributo.setNombre(caracReq.getAtributoNombre());
                        return atributosRepository.save(nuevoAtributo);
                    });

            // Buscamos o creamos en la tabla valores_atributo para obtener el valor específico del atributo
            ValoresAtributos valorAtributo = valoresAtributosRepository
                    .findByValorIgnoreCaseAndAtributoId(caracReq.getValorTexto(), atributo.getId())
                    .orElseGet(() -> {
                        ValoresAtributos nuevoValor = new ValoresAtributos();
                        nuevoValor.setValor(caracReq.getValorTexto());
                        nuevoValor.setAtributo(atributo);
                        return valoresAtributosRepository.save(nuevoValor);
                    });

            // Insertar la nueva combinación en la tabla intermedia variacion_valor
            VariacionValores nuevaRelacion = new VariacionValores(variacion, valorAtributo);
            variacionValores.save(nuevaRelacion);
        }

        return "Variación y características actualizadas con éxito";
    }

    public String estadoProducto(Integer id) {
        Productos producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        // Invertir el booleano directamente: si es true pasa a false, si es false pasa a true
        producto.setEstado(!producto.getEstado());
        // Guardar en la base de datos
        productoRepository.save(producto);
        // Retornar mensaje indicando el nuevo estado del producto
        String mensajeEstado = producto.getEstado() ? "ACTIVO" : "INACTIVO";
        return "Estado del producto actualizado a: " + mensajeEstado;
    }
}
