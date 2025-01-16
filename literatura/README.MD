Desafío Alura Cursos - Aplicación de Gestión de Libros y Autores

Descripción General

Esta aplicación es una solución desarrollada en Java que permite la gestión de libros y autores mediante el uso de una base de datos y el consumo de una API externa para la búsqueda de libros. Se incluye un sistema de menú interactivo para realizar diferentes operaciones, como listar libros, buscar autores vivos en un año específico y filtrar libros por idioma.

Características

Búsqueda de libros por título con opción de guardar en la base de datos.

Listado de libros y autores registrados.

Filtrado de autores vivos en un año específico.

Búsqueda de libros por idioma.

Persistencia de datos usando Spring Data JPA.

Consumo de API externa para obtener datos de libros.

Tecnologías Utilizadas

Java: Lenguaje principal.

Spring Framework: Para la gestión de dependencias y persistencia de datos.

Jakarta Persistence (JPA): Manejo de entidades y relaciones.

API externa: Gutendex para la búsqueda de libros.

Requisitos Previos

Java 17+

Maven: Para la gestión de dependencias.

Base de Datos: Configurada para uso con JPA (ejemplo: MySQL o H2).

Dependencias necesarias:

spring-boot-starter-data-jpa

spring-boot-starter-web

com.fasterxml.jackson

Estructura del Proyecto

com.aluracursos.desafio
├── model
│   ├── Autor.java
│   ├── Libro.java
│   ├── Datos.java
│   └── DatosLibros.java
├── repository
│   ├── AutorRepository.java
│   └── LibroRepository.java
├── service
│   ├── ConsumoAPI.java
│   └── ConvierteDatos.java
├── principal
│   └── Principal.java

Modelos

Autor:

id: Identificador único del autor.

nombre: Nombre del autor.

fechaDeNacimiento: Fecha de nacimiento.

fechaDeMuerte: Fecha de muerte (puede ser null).

libros: Lista de libros asociados al autor.

Libro:

id: Identificador único del libro.

titulo: Título del libro.

idioma: Idioma del libro.

numeroDeDescargas: Número de descargas.

autor: Autor asociado al libro.

Repositorios

AutorRepository: Interfaz para operaciones CRUD en la tabla autores.

LibroRepository: Interfaz para operaciones CRUD en la tabla libros.

Servicios

ConsumoAPI: Clase para interactuar con la API externa Gutendex.

ConvierteDatos: Convierte los datos JSON obtenidos de la API en objetos de la aplicación.

Principal

Clase principal que ejecuta el programa y ofrece un menú interactivo para las operaciones.

Funcionalidades

Menú Interactivo

El programa ofrece un menú con las siguientes opciones:

Buscar libro por título: Permite buscar libros en la API externa y guardarlos en la base de datos.

Listar libros registrados: Muestra los libros almacenados en la base de datos.

Listar autores registrados: Muestra todos los autores almacenados.

Listar autores vivos en un año específico: Filtra autores que estaban vivos en un año dado.

Listar libros por idioma: Permite buscar libros registrados según su idioma.

Salir: Finaliza el programa.

Ejecución

Clonar el repositorio:

git clone <url_del_repositorio>

Configurar la base de datos:

Edita el archivo application.properties para incluir las credenciales de la base de datos.

Ejecutar la aplicación:

mvn spring-boot:run

Ejemplo de Uso

Ingrese "1" para buscar un libro por título.

Escriba el título deseado y presione Enter.

Si el libro está disponible en la API, puede guardarlo en la base de datos.

Use "2" para listar los libros registrados.

Notas

Asegúrese de configurar la URL base de la API correctamente: https://gutendex.com/books/.

Evite ingresar datos vacíos o incorrectos, ya que pueden generar errores de validación.

Contribución

Fork del proyecto.

Crea una rama para tus cambios:

git checkout -b feature/nueva-funcionalidad

Envía un pull request describiendo los cambios.