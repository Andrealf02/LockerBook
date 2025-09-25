# Documentación Técnica del Proyecto

## 1. Decisiones de Arquitectura

### 1.1. Separación de Responsabilidades

El proyecto implementa una arquitectura hexagonal que separa claramente las siguientes capas:

- **Capa de Dominio:**  
  Contiene las entidades `Plan` y `Zone`, así como los puertos (interfaces) del repositorio.  
  Permite aislar la lógica de negocio de los detalles técnicos.

- **Capa de Infraestructura:**  
  Incluye:
   - Adaptadores para la persistencia (por ejemplo, `PlanRepositoryAdapter`)
   - Controladores web para la API REST

- **Capa de Aplicación:**  
  Contiene los casos de uso encargados de orquestar la lógica de negocio.  
  Esta separación facilita:
   - La realización de pruebas mediante mocks de los puertos.
   - El cambio de implementaciones sin afectar la capa de dominio.

## 2. Gestión de Datos y Persistencia

### 2.1. Implementación del Repositorio

- Utiliza JPA para la persistencia.
- Emplea transaccionalidad para garantizar la consistencia de los datos.
- Define mapeo bidireccional entre `Plan` y `Zone`.
- Utiliza consultas optimizadas para búsquedas basadas en rangos de fechas.

### 2.2. Optimización de Consultas

- Utiliza índices en campos de fechas para búsquedas eficientes.
- Configura carga lazy de relaciones cuando es apropiado.
- Realiza conversiones eficientes entre entidades y objetos de dominio.

## 3. Estrategia de Pruebas

### 3.1. Pruebas de Integración

- Se realizan tests de los controladores utilizando MockMvc.
- Se validan las respuestas HTTP y el formato JSON.
- Se cubren casos de error en las pruebas.

### 3.2. Pruebas Unitarias

- Se realizan pruebas específicas para el repositorio.
- Se validan los mapeos entre entidades y objetos de dominio.
- Se verifica la lógica de negocio de forma individual.

## 4. API REST

La implementación sigue la especificación OpenAPI e incluye:

- Un endpoint GET para la búsqueda de planes.
- Validación de parámetros de fecha.
- Filtrado por modalidad online.
- Respuestas HTTP apropiadas.
- Manejo consistente de errores.

## 5. Herramientas de Desarrollo

### 5.1. Build y Ejecución

- Se emplea Gradle como sistema de build.
- Se utiliza un Makefile para ejecutar comandos comunes.
- Se configura mediante properties adaptadas a cada ambiente.

### 5.2. Documentación

- Se genera JavaDoc para las clases principales.
- Se proporciona una colección de Postman para la prueba de la API.
- Se incluye un README con instrucciones detalladas de setup.

### 5.3. Calidad de Código

- Se utiliza Lombok para reducir el código repetitivo.
- Se siguen convenciones de código consistentes.
- Se implementa un manejo estructurado de excepciones.

### 5.4. Monitorización

- Se implementan métricas de rendimiento.
- Se utiliza logging estructurado para facilitar el análisis.
- Se incorporan health checks para asegurar la disponibilidad del servicio.

---
##  Author
This project was developed by **ALF**.
