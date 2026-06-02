package com.spring.team1.tiendamia.services.usuario;

import java.util.List;
import com.spring.team1.tiendamia.Models.Usuario.*;

public interface DireccionUsuarioService {
    List<Direcciones_usuarios> getDireccionesByUsuarioId(Integer usuarioId);
    Direcciones_usuarios saveDireccion(Direcciones_usuarios direccion);
    void deleteDireccion(Integer id);
}