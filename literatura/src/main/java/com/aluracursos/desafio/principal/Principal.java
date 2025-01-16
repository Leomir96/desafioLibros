package com.aluracursos.desafio.principal;

import com.aluracursos.desafio.model.Autor;
import com.aluracursos.desafio.model.Datos;
import com.aluracursos.desafio.model.DatosLibros;
import com.aluracursos.desafio.model.Libro;
import com.aluracursos.desafio.repository.AutorRepository;
import com.aluracursos.desafio.repository.LibroRepository;
import com.aluracursos.desafio.service.ConsumoAPI;
import com.aluracursos.desafio.service.ConvierteDatos;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);
    private List<DatosLibros> datosLibros = new ArrayList<>();
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestraElMenu() {
        int opcion;
        do {
            System.out.println("\nElija la opción a través de su número:");
            System.out.println("1 - Buscar libro por título");
            System.out.println("2 - Listar libros registrados");
            System.out.println("3 - Listar autores registrados");
            System.out.println("4 - Listar autores vivos en un determinado año");
            System.out.println("5 - Listar libros por idioma");
            System.out.println("0 - Salir");
            System.out.print("Opción: ");

            opcion = teclado.nextInt();
            teclado.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosEnAno();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 0);
    }

    private void buscarLibroPorTitulo() {
        System.out.println("\nIngrese el nombre del libro que desea buscar:");
        String tituloLibro = teclado.nextLine().trim();
        if (tituloLibro.isEmpty()) {
            System.out.println("El título no puede estar vacío.");
            return;  // Salimos del método si el título está vacío
        }

        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "+"));
        System.out.println(json);
        System.out.println((URL_BASE + "?search=" + tituloLibro.replace(" ", "+")));
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);
        Optional<DatosLibros> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();

        if (libroBuscado.isPresent()) {
            System.out.println("Libro encontrado:");
            DatosLibros datos = libroBuscado.get();
            var autorDatos = datos.autor().get(0);
            Autor autor = new Autor(autorDatos.nombre(), autorDatos.fechaDeNacimiento(), autorDatos.fechaDeMuerte());

            // Crear objeto Libro y asignar valores
            Libro libro = new Libro();
            libro.setTitulo(datos.titulo());
            libro.setAutor(autor);
            libro.setNumeroDeDescargas(datos.numeroDeDescargas());

            // Asignar el primer idioma al libro
            if (datos.idiomas() != null && !datos.idiomas().isEmpty()) {
                libro.setIdioma(datos.idiomas().get(0));  // Asignando el primer idioma
            }

            try {
                libroRepository.save(libro);
                System.out.println("Libro guardado en la base de datos.");
            } catch (DataIntegrityViolationException e) {
                System.out.println("Error: El libro con título '" + libro.getTitulo() + "' ya existe en la base de datos.");
            } catch (Exception e) {
                System.err.println("Error al guardar el libro: " + e.getMessage());
            }
        } else {
            System.out.println("Libro no encontrado.");
        }
    }



    private void listarLibrosRegistrados() {
        List<Libro> libros = libroRepository.findAll();
        libros.forEach(System.out::println);
    }

    private void listarAutoresRegistrados() {
        System.out.println("\nAutores registrados:");
        List<Autor> autores = autorRepository.findAll();

        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
        } else {
            autores.forEach(autor -> System.out.println(autor.getNombre()));
        }
    }


    private void listarAutoresVivosEnAno() {
        // Utiliza una variable final para almacenar el año válido
        final int ano = obtenerAnoValido();

        System.out.println("Autores vivos en el año " + ano + ":");
        List<String> autoresVivos = datosLibros.stream()
                .flatMap(libro -> libro.autor().stream())
                .filter(autor -> {
                    Integer nacimiento = parseIntOrNull(autor.fechaDeNacimiento());
                    Integer muerte = parseIntOrNull(autor.fechaDeMuerte());
                    // Utiliza 'ano' como una constante dentro de la lambda
                    return nacimiento != null && nacimiento <= ano && (muerte == null || muerte >= ano);
                })
                .map(autor -> autor.nombre())
                .distinct()
                .collect(Collectors.toList());

        if (autoresVivos.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año especificado.");
        } else {
            autoresVivos.forEach(System.out::println);
        }
    }

    private int obtenerAnoValido() {
        int ano = -1;  // Valor inicial inválido
        while (ano == -1) {  // Continuar hasta obtener un año válido
            System.out.println("\nIngrese el año para buscar autores vivos:");
            String input = teclado.nextLine();
            try {
                ano = Integer.parseInt(input);  // Intentamos convertir el input a número
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un año válido.");
            }
        }
        return ano;
    }



    private void listarLibrosPorIdioma() {
        System.out.println("\nIngrese el idioma para buscar libros (por ejemplo, 'es', 'en'): ");
        String idioma = teclado.nextLine().trim().toLowerCase();

        if (idioma.isEmpty()) {
            System.out.println("El idioma no puede estar vacío.");
            return;  // Salimos si no se ingresa un idioma válido
        }

        System.out.println("Libros en el idioma '" + idioma + "':");
        List<Libro> librosPorIdioma = libroRepository.findAll().stream()
                .filter(libro -> libro.getIdioma() != null && libro.getIdioma().toLowerCase().equals(idioma))
                .collect(Collectors.toList());

        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma especificado.");
        } else {
            librosPorIdioma.forEach(System.out::println);
        }
    }




    private Integer parseIntOrNull(String value) {
        try {
            return value != null && !value.isEmpty() ? Integer.parseInt(value) : null;
        } catch (NumberFormatException e) {
            return null;  // Si el valor no es un número, retornamos null
        }
    }
}