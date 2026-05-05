# Medicontrol Web - Proyecto Profesional

Sistema web para control y recordatorio de medicamentos, desarrollado para feria universitaria de Ingenieria de Sistemas.

## Tecnologias

- Java 17
- Spring Boot 3.3.5
- Spring Web
- Spring Data JPA
- MySQL
- HTML, CSS y JavaScript
- Maven

## Modulos incluidos

- Pagina de presentacion
- Login demo
- Dashboard profesional
- Registro, edicion, busqueda y eliminacion de pacientes
- Registro y eliminacion de medicamentos
- Programacion de horarios de toma
- Control de dosis: tomada, pendiente u omitida
- Historial de dosis
- Reportes de cumplimiento
- Diseno responsive para navegador y celular

## Requisitos

- JDK 17 instalado
- Maven instalado
- MySQL activo
- Visual Studio Code
- Extension Pack for Java para VS Code

## Base de datos

El proyecto crea automaticamente la base de datos si MySQL permite conexion con el usuario configurado.

Nombre de BD:

```sql
medicontrol_db
```

Archivo de configuracion:

```text
src/main/resources/application.properties
```

Si tu MySQL tiene clave, edita esta linea:

```properties
spring.datasource.password=TU_CLAVE
```

## Ejecutar en VS Code

1. Descomprime el ZIP.
2. Abre la carpeta `Medicontrol_Web_Profesional` en VS Code.
3. Verifica que MySQL este encendido.
4. En la terminal ejecuta:

```bash
mvn spring-boot:run
```

5. Abre en el navegador:

```text
http://localhost:8080
```

## Usuario demo

```text
Correo: admin@medicontrol.com
Contrasena: 123456
```

## Rutas principales

```text
/                  Pagina de inicio
/login.html        Login
/dashboard.html    Panel principal
/pacientes.html    Gestion de pacientes
/medicamentos.html Gestion de medicamentos
/dosis.html        Control de dosis del dia
/historial.html    Historial
/reportes.html     Reportes
```

## API REST

```text
POST   /api/auth/login
GET    /api/pacientes
POST   /api/pacientes
PUT    /api/pacientes/{id}
DELETE /api/pacientes/{id}
GET    /api/medicamentos
POST   /api/medicamentos
PUT    /api/medicamentos/{id}
DELETE /api/medicamentos/{id}
GET    /api/dosis/hoy
POST   /api/dosis/{id}/tomado
POST   /api/dosis/{id}/omitido
POST   /api/dosis/{id}/pendiente
GET    /api/dosis/historial
GET    /api/reportes/resumen
```

## Recomendacion para exposicion

Presentar el flujo:

1. Ingresar al sistema.
2. Mostrar dashboard.
3. Registrar paciente.
4. Registrar medicamento con hora de toma.
5. Entrar a dosis de hoy.
6. Marcar dosis como tomada u omitida.
7. Revisar historial y reportes.

## Importante

Medicontrol Web no receta medicamentos ni reemplaza la atencion medica. Su proposito es apoyar la organizacion y seguimiento de tratamientos indicados por profesionales de salud.
