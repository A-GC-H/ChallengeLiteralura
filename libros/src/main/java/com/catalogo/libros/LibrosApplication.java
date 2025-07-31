package com.catalogo.libros;

import com.catalogo.libros.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.InputMismatchException;
import java.util.Scanner;

@SpringBootApplication
public class LibrosApplication implements CommandLineRunner {

    @Autowired
    private LibroService libroService;

    public static void main(String[] args) {
        SpringApplication.run(LibrosApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner teclado = new Scanner(System.in);
        int opcion = -1;

        while (opcion != 0) {
            mostrarMenu();
            try {
                opcion = teclado.nextInt();
                teclado.nextLine(); // Consumir el salto de línea

                switch (opcion) {
                    case 1:
                        System.out.println("Ingrese el título del libro a buscar:");
                        String tituloLibro = teclado.nextLine();
                        libroService.buscarYLguardarLibro(tituloLibro);
                        break;
                    case 2:
                        System.out.println("--- Libros Registrados ---");
                        libroService.listarLibrosRegistrados().forEach(System.out::println);
                        break;
                    case 3:
                        System.out.println("--- Autores Registrados ---");
                        libroService.listarAutoresRegistrados().forEach(System.out::println);
                        break;
                    case 4:
                        System.out.println("Ingrese el año para buscar autores vivos:");
                        Integer anio = teclado.nextInt();
                        teclado.nextLine(); // Consumir el salto de línea
                        System.out.println("--- Autores Vivos en " + anio + " ---");
                        libroService.listarAutoresVivosEnAnio(anio).forEach(System.out::println);
                        break;
                    case 5:
                        System.out.println("Ingrese el idioma (ej. es, en, fr):");
                        String idioma = teclado.nextLine();
                        System.out.println("--- Libros en Idioma " + idioma.toUpperCase() + " ---");
                        libroService.listarLibrosPorIdioma(idioma).forEach(System.out::println);
                        break;
                    case 0:
                        System.out.println("Saliendo del programa. ¡Hasta luego!");
                        break;
                    default:
                        System.out.println("Opción inválida. Por favor, intente de nuevo.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número.");
                teclado.nextLine(); // Limpiar el buffer de entrada
                opcion = -1; // Para que el bucle continúe
            }
        }
        teclado.close();
    }

    private void mostrarMenu() {
        System.out.println("\n--- Menú de Catálogo de Libros ---");
        System.out.println("1. Buscar libro por título y registrarlo");
        System.out.println("2. Listar libros registrados");
        System.out.println("3. Listar autores registrados");
        System.out.println("4. Listar autores vivos en un año específico");
        System.out.println("5. Listar libros por idioma");
        System.out.println("0. Salir");
        System.out.print("Elija una opción: ");
    }
}
