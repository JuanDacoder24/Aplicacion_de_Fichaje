# 🕒 Aplicación de Fichaje

![Estado](https://img.shields.io/badge/estado-en%20desarrollo-yellow)
![Angular](https://img.shields.io/badge/frontend-Angular-red)
![SpringBoot](https://img.shields.io/badge/backend-SpringBoot-green)
![Licencia](https://img.shields.io/badge/licencia-Educativa-blue)

Sistema web de control de fichajes desarrollado con **Angular** y **Spring Boot**, diseñado para gestionar la jornada laboral de empleados de forma eficiente.

---

## 📖 Descripción

Esta aplicación permite a los empleados registrar sus horas de entrada y salida, mientras que los administradores pueden supervisar, gestionar y analizar la información de fichajes.

El objetivo del proyecto es simular un sistema real de control horario utilizado en empresas.

---

## ✨ Características principales

* 🟢 Registro de entrada y salida en tiempo real
* 📅 Consulta de fichajes por fecha
* 👤 Gestión de usuarios (empleados)
* ⏱️ Control de jornada laboral
* 📊 Historial de fichajes
* 🔐 Autenticación de usuarios (si la tienes implementada)
* ⚡ Interfaz moderna y responsive

---

## 🏗️ Arquitectura

El proyecto sigue una arquitectura **cliente-servidor**:

* **Frontend:** Aplicación SPA con Angular
* **Backend:** API REST con Spring Boot
* **Comunicación:** HTTP (JSON)

---

## 🧰 Tecnologías utilizadas

### 🔹 Frontend

* Angular
* TypeScript
* HTML5 / CSS3
* Bootstrap

### 🔹 Backend

* Spring Boot
* Java
* Spring Data JPA
* Hibernate

### 🔹 Base de datos

* MySQL 

---

## 📂 Estructura del proyecto

```
Aplicacion_de_Fichaje/
│
├── Backend/                  # API REST con Spring Boot
│   ├── controllers/
│   ├── services/
│   ├── repositories/
│   └── models/
│
├── Frontend/
│   └── FrontFichajes/        # Aplicación Angular
│       ├── src/
│       └── app/
│
└── README.md
```

---

## ⚙️ Instalación

### 🔹 Requisitos previos

* Node.js
* Angular CLI
* Java 17+
* Maven
* Base de datos MySQL

---

## ▶️ Ejecución del proyecto

### 🔸 Backend (Spring Boot)

```bash
cd Backend
./mvnw spring-boot:run
```

O ejecutarlo desde IntelliJ / Eclipse.

El backend se ejecutará en:

```
http://localhost:8080
```

---

### 🔸 Frontend (Angular)

```bash
cd Frontend/FrontFichajes
npm install
ng serve
```

Abrir en el navegador:

```
http://localhost:4200
```

---

## 🔗 Configuración de la API

Asegúrate de que Angular apunta correctamente al backend.

Ejemplo en `environment.ts`:

```ts
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'
};
```


---

## 📊 Posibles mejoras

* 📱 Aplicación móvil
* 📍 Geolocalización de fichajes
* 📸 Registro con reconocimiento facial
* 📈 Dashboard con estadísticas
* 🔔 Notificaciones en tiempo real

---

## 🚧 Estado del proyecto

En desarrollo activo.

---

## 👨‍💻 Autor

**Juan David Servellón**

* GitHub: https://github.com/JuanDacoder24

---

## 📄 Licencia

Este proyecto ha sido desarrollado con fines educativos.

---

## ⭐ Contribuciones

Las contribuciones son bienvenidas. Si quieres mejorar el proyecto:

1. Haz un fork
2. Crea una rama (`git checkout -b feature/nueva-funcionalidad`)
3. Haz commit de tus cambios
4. Haz push a la rama
5. Abre un Pull Request

---

## 💡 Nota

Este proyecto forma parte de un desarrollo académico y está enfocado en aprender buenas prácticas de desarrollo full-stack.
