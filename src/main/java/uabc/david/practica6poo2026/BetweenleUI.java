package uabc.david.practica6poo2026;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Esta clase representa la interfaz de usuario en consola para el juego Betweenle.
 * Muestra menús, lee entradas y presenta los resultados.
 */
public class BetweenleUI {
    private Betweenle juego;
    private Scanner scanner;

    /**
     * Inicializa la interfaz con un scanner para leer desde consola,
     * configura el juego con 7 letras por defecto para el modo difícil.
     */
    public BetweenleUI() {
        this.scanner = new Scanner(System.in);
        this.juego = new Betweenle(7);
    }

    /**
     * Muestra el menú de configuración,
     * ejecuta la partida y cierra el scanner al terminar.
     */
    public void iniciar() {
        mostrarMenu();
        ejecutarPartida();
        scanner.close();
    }

    /**
     * Muestra el menú de configuración inicial y arranca la partida.
     * El jugador elige el idioma del diccionario, la dificultad y el número de intentos.
     */
    private void mostrarMenu() {
        System.out.println("\nSelecciona el idioma del diccionario:");
        System.out.println("1. Español");
        System.out.println("2. Inglés");
        int opcionIdioma = leerOpcion("Opción: ", 1, 2);
        String idioma = opcionIdioma == 1 ? "español" : "ingles";

        System.out.println("\nSelecciona la dificultad:");
        System.out.println("1. Fácil (5 letras)");
        System.out.println("2. Intermedio (6 letras)");
        System.out.println("3. Difícil (n letras)");
        int opcionDificultad = leerOpcion("Opción: ", 1, 3);

        String dificultad;
        if (opcionDificultad == 1) {
            dificultad = "facil";
        } else if (opcionDificultad == 2) {
            dificultad = "intermedio";
        } else {
            // En difícil, el jugador puede elegir la longitud de la palabra.
            dificultad = "dificil";
            int letras = leerOpcion("Ingresa el número de letras para el modo difícil: ", 7, 15);
            // Se crea entonces el juego con la longitud elegida.
            juego = new Betweenle(letras);
        }

        // El jugador elige cuántos intentos quiere tener en la partida.
        System.out.println("\nSelecciona el número de intentos:");
        System.out.println("1. 10 intentos");
        System.out.println("2. 12 intentos");
        System.out.println("3. 14 intentos");
        int opcionIntentos = leerOpcion("Opción: ", 1, 3);

        int intentosElegidos;
        if (opcionIntentos == 1) {
            intentosElegidos = 10;
        } else if (opcionIntentos == 2) {
            intentosElegidos = 12;
        } else {
            intentosElegidos = 14;
        }

        System.out.println("\nCargando diccionario...");

        boolean iniciado = juego.iniciarPartida(idioma, dificultad, intentosElegidos);

        // Si no hay palabras con la longitud requerida, se vuelve a mostrar el menú.
        if (!iniciado) {
            System.out.println("No se encontraron palabras de esa longitud en el diccionario.");
            mostrarMenu();
            return;
        }

        int longitud = juego.getLongitudDificultad(dificultad);

        // Se muestra la longitud junto con los intentos elegidos por el jugador.
        System.out.println("Es una palabra de " + longitud +
                " letras, tienes " + intentosElegidos + " intentos disponibles.");
    }

    /**
     * Lee un número entero ingresado por el jugador dentro de un rango válido.
     * @param mensaje Texto que se muestra al jugador antes de leer.
     * @param opc1 Valor mínimo aceptable.
     * @param opcAlt Valor máximo aceptable.
     * @return El número válido ingresado por el jugador.
     */
    private int leerOpcion(String mensaje, int opc1, int opcAlt) {
        int opcion = 0;
        boolean valido = false;

        while (!valido) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim();
            try {
                opcion = Integer.parseInt(entrada);
                if (opcion >= opc1 && opcion <= opcAlt) {
                    valido = true;
                } else {
                    System.out.println("Elige una opción entre " + opc1 + " y " + opcAlt + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Escribe un número válido");
            }
        }

        return opcion;
    }

    /**
     * Ejecuta el bucle principal de la partida hasta que el jugador gane, pierda o se quede sin intentos.
     */
    private void ejecutarPartida() {
        int numeroIntento = 1;
        boolean esGanador = false;

        // El juego continúa mientras haya intentos y la partida esté activa.
        while (juego.juegoActivo()) {
            System.out.println("\n-----------------------------------------");
            // Aquí se muestran los límites, proximidad e intentos restantes.
            System.out.println(juego.getEstado());
            System.out.println("-----------------------------------------");

            mostrarHistorial(numeroIntento);
            mostrarLetrasUsadas();

            System.out.println("\nOpciones:");
            System.out.println("[1] Escribir una palabra");
            System.out.println("[2] Pedir pista");
            int accion = leerOpcion("Elige una opción: ", 1, 2);

            // Si el jugador pide su pista, se gestiona aparte y se regresa al inicio del turno.
            if (accion == 2) {
                gestionarPista();
                continue;
            }

            String intento = leerEntrada("Escribe una palabra: ");
            String resultado = juego.procesarIntento(intento);

            // Si la palabra ingresada tiene otra longitud, avisará al usuario.
            if (resultado.equals("longitud")) {
                System.out.println("\nLa palabra debe tener " +
                        juego.getRondaActual().getLongitudPalabra() + " letras. Intenta de nuevo.");
                continue;
            }

            // Si no existe la palabra ingresada, se da la opción de agregarla.
            if (resultado.equals("no encontrada")) {
                manejarPalabraInexistente(intento);
                continue;
            }

            // Si la palabra está fuera de los límites, avisará al usuario cuál límite se pasó.
            if (resultado.equals("fuera de rango")) {
                String limSuperior = juego.getRondaActual().getLimiteSuperior().toUpperCase();
                String limInferior = juego.getRondaActual().getLimiteInferior().toUpperCase();
                String intentoMayus = intento.toUpperCase();

                System.out.println("\nLa palabra " + intentoMayus + " está fuera del rango válido.");

                // Si intento es <= que el límite superior, significa que se desvió hacia arriba.
                if (intento.toLowerCase().compareTo(limSuperior.toLowerCase()) <= 0) {
                    System.out.println("Debe ser una palabra alfabéticamente posterior a " + limSuperior + ".");
                }
                // Si no, significa que es >= que el límite inferior.
                else {
                    System.out.println("Debe ser una palabra alfabéticamente anterior a " + limInferior + ".");
                }
                continue;
            }

            // Si ya no quedan intentos, avisará al jugador.
            if (resultado.equals("sin intentos")) {
                System.out.println("No te quedan intentos disponibles.");
                break;
            }

            // Si el jugador acertó, se mostrará un mensaje de victoria.
            if (resultado.equals("correcto")) {
                esGanador = true;
                break;
            }

            // Si no acertó, pero está en los límites, se indica la dirección alfabética de la palabra secreta.
            if (resultado.equals("antes")) {
                System.out.println("\nLa palabra secreta está ANTES de " + intento.toUpperCase() + " alfabéticamente.");
            } else if (resultado.equals("despues")) {
                System.out.println("\nLa palabra secreta está DESPUÉS de " + intento.toUpperCase() + " alfabéticamente.");
            }

            numeroIntento++;
        }

        // Al finalizar la partida, se muestra el resumen completo.
        System.out.println("\n---------------------------------------------------");
        System.out.println("               RESUMEN DE LA PARTIDA               ");
        System.out.println("---------------------------------------------------");

        mostrarHistorial(esGanador ? numeroIntento + 1 : numeroIntento);

        System.out.println("\nLA PALABRA ERA: " + juego.getRondaActual().getPalabraSecreta().toUpperCase());

        if (esGanador) {
            System.out.println("\nADIVINASTE LA PALABRA");
            System.out.println("¡GANASTE LA PARTIDA!");
        } else {
            System.out.println("\nTE QUEDASTE SIN INTENTOS");
            System.out.println("PERDISTE LA PARTIDA :(");
        }
    }

    /**
     * Lee una línea de texto ingresada por el jugador desde la consola.
     * @param mensaje Texto que se muestra antes de leer.
     * @return La entrada del jugador.
     */
    private String leerEntrada(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim().toLowerCase();
    }

    /**
     * Muestra el historial de palabras de la partida.
     * @param intentosTotales Número total de intentos hasta el momento.
     */
    private void mostrarHistorial(int intentosTotales) {
        ArrayList<String> historial = juego.getHistorial();
        if (historial.isEmpty()) {
            System.out.println("\nNo se ha escrito ninguna palabra");
            return;
        }

        System.out.println("\nHistorial de intentos:");
        int numero = 1;
        for (String linea : historial) {
            System.out.println("  " + numero + ". " + linea);
            numero++;
        }
    }

    /**
     * Muestra todas las letras que el jugador ha utilizado hasta el momento.
     * Usa un para recorrer el HashSet de letras usadas.
     * Si aún no se ha intentado ninguna palabra, no muestra nada.
     */
    private void mostrarLetrasUsadas() {
        Iterator<String> iterador = juego.getRondaActual().getLetrasUsadas().iterator();

        // Si el HashSet está vacío, no hay nada que mostrar.
        if (!iterador.hasNext()) {
            return;
        }

        System.out.print("\nLetras usadas: ");
        while (iterador.hasNext()) {
            System.out.print(iterador.next().toUpperCase() + " ");
        }
        System.out.println();
    }

    /**
     * Gestiona la pista solicitada por el jugador.
     * Verifica primero si la pista ya fue usada y luego presenta las opciones disponibles.
     */
    private void gestionarPista() {
        // Si se usó la pista, ya no se mostrarán las opciones.
        if (juego.getRondaActual().pistaUtilizada()) {
            System.out.println("\nYa usaste tu pista en esta partida.");
            return;
        }

        System.out.println("\nElige tu pista (solo puedes usar 1 por partida):");
        System.out.println("1. Acercar el límite superior a la palabra secreta");
        System.out.println("2. Acercar el límite inferior a la palabra secreta");
        System.out.println("3. Revelar la letra inicial de la palabra secreta");
        int opcionPista = leerOpcion("Elige una opción: ", 1, 3);

        String resultadoPista = juego.pedirPista(opcionPista);

        // Se valida que las pistas 1 y 2 requieren al menos un intento previo.
        if (resultadoPista.equals("requiere intento")) {
            System.out.println("\nNo puedes usar esta pista.");
            System.out.println("Ingresa al menos una palabra para establecer los límites iniciales.");
            return;
        }

        if (resultadoPista.equals("demasiado cerca")) {
            System.out.println("\nNo puedes usar ya esta pista, estás muy cerca.");
            return;
        }

        System.out.println("\nPista: " + resultadoPista);
    }

    /**
     * Maneja el caso en que el jugador intenta una palabra que no está en el diccionario.
     * Se ofrece la posibilidad de agregarla si considera que es una palabra válida.
     * @param palabra La palabra que no encontrada en el diccionario.
     */
    private void manejarPalabraInexistente(String palabra) {
        System.out.println("\nLa palabra " + palabra + " no está en el diccionario.");
        System.out.println("¿Puedes demostrar que es una palabra válida?");
        System.out.println("1. Sí, agregarla al diccionario");
        System.out.println("2. No, escribir otra palabra");
        int opcion = leerOpcion("Elige una opción: ", 1, 2);

        // Si el jugador confirma, se agrega al diccionario correspondiente.
        if (opcion == 1) {
            juego.agregarPalabraAlDiccionario(palabra);
            System.out.println("La palabra " + palabra + " fue agregada al diccionario.");
        }
    }

    /**
     * Crea la interfaz e inicia con el juego.
     */
    public static void main(String[] args) {
        BetweenleUI ui = new BetweenleUI();
        ui.iniciar();
    }
}
