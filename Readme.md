# 🛒 Tiendamia Clone - Backend REST API

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) ![Spring Boot](https://img.shields.io/badge/springboot-000000?style=for-the-badge&logo=springboot&logoColor=green) ![MySQL/PostgreSQL](https://img.shields.io/badge/Database-SQL-blue?style=for-the-badge&logo=postgresql) ![Jira](https://img.shields.io/badge/jira-%230A0FFF.svg?style=for-the-badge&logo=jira&logoColor=white)

Este repositorio contiene el código fuente del **Back End** para el clon de la plataforma de e-commerce Tiendamia. Está construido como una API RESTful robusta utilizando el framework de **Spring Boot**.

## 🎯 Objetivo del Proyecto
El objetivo principal es desarrollar una arquitectura de software orientada a servicios que soporte las operaciones centrales de un comercio electrónico de extremo a extremo. Esto incluye la gestión de usuarios, catálogo de productos, procesamiento de carritos de compras y flujos de órdenes, asegurando una persistencia de datos eficiente y una comunicación fluida con el Front End mediante endpoints seguros.

## 🏗️ Arquitectura y Flujo de Datos
Para garantizar que el código sea lo más accesible, escalable y legible posible, el proyecto implementa una estricta separación de responsabilidades a través de un patrón de arquitectura por capas:

1. **Controllers (Controladores):** Capa de exposición de la API. Recibe las peticiones HTTP (GET, POST, PUT, DELETE, PATCH), valida los *payloads* y delega la ejecución.
2. **Services (Lógica de Negocio):** Contiene las reglas centrales del e-commerce. Aquí se procesan los datos y se toman decisiones antes de interactuar con la base de datos. Aislar esta lógica permite un mantenimiento mucho más sencillo.
3. **Repositories (Acceso a Datos):** Interfaces de Spring Data JPA que se comunican directamente con la base de datos relacional.
4. **Entities & DTOs:** Modelado de las tablas de la base de datos y objetos de transferencia de datos (JSON) para evitar la sobreexposición de información sensible hacia el cliente.
5. **Util**: Para estructurar las respuesta con response y manejo global de errores con el GlobalHandler

El flujo estándar de una petición es: `Cliente (React) ➔ Controller ➔ Service ➔ Repository ➔ Base de Datos`.

## 🤝 Colaboración y Flujo de Trabajo (Git Workflow)
El desarrollo se lleva a cabo en equipo aplicando prácticas de integración continua. Nuestra estrategia de ramas (Branching Strategy) está diseñada para evitar conflictos y mantener un entorno estable:

* 🌿 **`master`**: Rama principal de producción. Solo contiene código testeado y 100% funcional. No se trabaja directamente aquí.
* 👨‍💻 **`ramas personales (integrantes)`**: Cada desarrollador del equipo cuenta con su propia rama aislada (ej. `daniel`, `leandro`). Aquí se desarrollan los *features* individuales y luego se evaluan en antes de enviarlo a la rama `Master`.

## 📋 Gestión de Tareas con Jira Software
Toda la planificación y ejecución técnica del proyecto está respaldada por metodologías ágiles utilizando **Jira**. 

* **Sprints:** El trabajo se divide en iteraciones (sprints). Cada funcionalidad épica (ej. "Módulo de Autenticación") se desglosa en historias de usuario y tareas técnicas más pequeñas.
* **Flujo de Tickets:** Las tareas viajan por un tablero Kanban clásico: *To Do ➔ In Progress ➔ Code Review ➔ Done*.
* **Trazabilidad:** Cada commit y Pull Request en GitHub incluye la nomenclatura o el ID del ticket de Jira asignado. Esto nos permite conectar el código directamente con los requerimientos del negocio y saber exactamente quién, cómo y por qué se desarrolló cada segmento del proyecto.

## 🚀 Requisitos e Instalación

1. Clonar el repositorio:
   ```bash
   git clone [https://github.com/ZER0SEV7N/Tiendamia-backend.git](https://github.com/ZER0SEV7N/Tiendamia-backend.git)
   ```
2. Cargar la base de datos.sql
3. Modificar el application.properties para utilizar tus credenciales para enviar correos
