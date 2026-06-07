package com.spring.team1.tiendamia.services.usuario;

import java.util.List;
import com.spring.team1.tiendamia.models.usuario.ubicacion.DireccionesUsuarios;

public interface DireccionUsuarioService {
    List<DireccionesUsuarios> getDireccionesByUsuarioId(Integer usuarioId);
    DireccionesUsuarios saveDireccion(DireccionesUsuarios direccion);
    void deleteDireccion(Integer id);
}