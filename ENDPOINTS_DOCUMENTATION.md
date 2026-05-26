# 📚 Documentación de Endpoints - Tiendamia

## 🔐 Autenticación
Todos los endpoints requieren un **Bearer Token** enviado en el header `Authorization: Bearer {token}`

---

## 👤 Endpoints de Usuario

### 1. **Obtener Perfil del Usuario**
```
GET /api/usuario/me
```
**Descripción:** Obtiene la información del usuario autenticado incluyendo sus direcciones.

**Headers:**
```
Authorization: Bearer {token}
Content-Type: application/json
```

**Response (200):**
```json
{
  "id": 1,
  "nombres": "Edson",
  "apellidos": "Rojas",
  "correo": "edson@example.com",
  "telefono": "+51912345678",
  "activo": true,
  "rol": "CLIENTE",
  "direcciones": [
    {
      "id": 1,
      "direccion": "Calle Principal 123",
      "distrito": "San Juan de Lurigancho",
      "provincia": "Lima",
      "departamento": "Lima",
      "referencia": "Frente a la plaza",
      "es_principal": true,
      "createAt": "2024-01-15T10:30:00"
    }
  ]
}
```

---

### 2. **Actualizar Perfil del Usuario**
```
PATCH /api/usuario/me
```
**Descripción:** Actualiza la información del perfil del usuario autenticado.

**Headers:**
```
Authorization: Bearer {token}
Content-Type: application/json
```

**Request Body:**
```json
{
  "nombres": "Edson",
  "apellidos": "Rojas",
  "telefono": "+51912345678",
  "password": "nuevaPassword123" // opcional
}
```

**Response (200):**
```json
{
  "mensaje": "Perfil actualizado"
}
```

---

## 📍 Endpoints de Direcciones

### 3. **Obtener Todas las Direcciones del Usuario**
```
GET /api/usuario/direcciones
```
**Descripción:** Obtiene la lista de todas las direcciones registradas del usuario autenticado.

**Headers:**
```
Authorization: Bearer {token}
Content-Type: application/json
```

**Response (200):**
```json
[
  {
    "id": 1,
    "direccion": "Calle Principal 123",
    "distrito": "San Juan de Lurigancho",
    "provincia": "Lima",
    "departamento": "Lima",
    "referencia": "Frente a la plaza",
    "es_principal": true,
    "createAt": "2024-01-15T10:30:00"
  },
  {
    "id": 2,
    "direccion": "Av. Secundaria 456",
    "distrito": "Miraflores",
    "provincia": "Lima",
    "departamento": "Lima",
    "referencia": "Cerca del parque",
    "es_principal": false,
    "createAt": "2024-01-20T15:45:00"
  }
]
```

---

### 4. **Agregar Nueva Dirección**
```
POST /api/usuario/direcciones
```
**Descripción:** Crea una nueva dirección para el usuario autenticado.

**Headers:**
```
Authorization: Bearer {token}
Content-Type: application/json
```

**Request Body:**
```json
{
  "direccion": "Calle Principal 123",
  "distrito": "San Juan de Lurigancho",
  "provincia": "Lima",
  "departamento": "Lima",
  "referencia": "Frente a la plaza",
  "es_principal": false
}
```

**Response (201):**
```json
{
  "id": 3,
  "usuario": {
    "id": 1
  },
  "direccion": "Calle Principal 123",
  "distrito": "San Juan de Lurigancho",
  "provincia": "Lima",
  "departamento": "Lima",
  "referencia": "Frente a la plaza",
  "es_principal": false,
  "createAt": "2024-05-26T12:00:00"
}
```

---

### 5. **Eliminar una Dirección**
```
DELETE /api/usuario/direcciones/{id}
```
**Descripción:** Elimina una dirección específica del usuario autenticado.

**Headers:**
```
Authorization: Bearer {token}
Content-Type: application/json
```

**Path Parameters:**
- `{id}` - ID de la dirección a eliminar

**Response (200):**
```json
{
  "mensaje": "Dirección eliminada"
}
```

**Response (404):**
```json
{
  "error": "Dirección no encontrada"
}
```

**Response (403):**
```json
{
  "error": "No autorizado"
}
```

---

## 🛒 Carrito

> **Nota:** Los endpoints del carrito están disponibles pero no documentados completamente en este momento. Consulta los controladores en `/Controllers/carrito/` para más información.

---

## 🔄 Códigos de Respuesta HTTP

| Código | Significado |
|--------|-------------|
| **200** | OK - Solicitud exitosa |
| **201** | Created - Recurso creado exitosamente |
| **400** | Bad Request - Solicitud inválida |
| **401** | Unauthorized - Token inválido o expirado |
| **403** | Forbidden - No tiene permisos para realizar esta acción |
| **404** | Not Found - Recurso no encontrado |
| **500** | Internal Server Error - Error del servidor |

---

## 🔑 Autenticación JWT

El sistema utiliza **JWT (JSON Web Tokens)** para la autenticación.

**Formato del token en el header:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## 🚀 URL Base
```
http://localhost:8080
```

---

## 📝 Notas Importantes

1. **Autenticación requerida:** Todos los endpoints requieren autenticación con JWT
2. **CORS:** El servidor permite solicitudes desde `http://localhost:3000` (frontend Next.js)
3. **Datos de prueba:** Usa usuario `cliente@gmail.com` para pruebas locales
4. **Servidor:** Spring Boot 4.0.6 ejecutándose en puerto 8080
5. **Base de datos:** H2 Database (en desarrollo)

---

**Última actualización:** 26 de mayo de 2024
