package uabc.david.practica6poo2026;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * La clase administra el estado y la lógica de una ronda activa del juego Betweenle.
 * Esta clase lleva el registro de los límites (superior e inferior),
 * el historial de intentos (palabras), las letras usadas por el jugador, y calcula
 * qué tan cerca está cada intento de la palabra secreta.
 */
public class ProcesadorRonda {
    private ArrayList<String> historialIntentos;
    private ArrayList<String> palabrasOrdenadas;
    private HashSet<String> letrasUsadas;
    private String dificultad;
    private String limiteSuperior;
    private String limiteInferior;
    private String palabraSecreta;
    private int intentosRestantes;
    private int longitudPalabra;
    private boolean pistaUtilizada;

    /**
     * Crea una nueva ronda con todos sus parámetros iniciales.
     * @param palabraSecreta La palabra que el jugador debe adivinar.
     * @param intentosMaximos Número máximo de intentos.
     * @param dificultad Nivel de dificultad elegido por el jugador.
     * @param palabrasOrdenadas ArrayList ordenada de palabras válidas para calcular proximidad.
     */
    public ProcesadorRonda(String palabraSecreta, int intentosMaximos, String dificultad, ArrayList<String> palabrasOrdenadas) {
        this.palabraSecreta = palabraSecreta;
        this.intentosRestantes = intentosMaximos;
        this.dificultad = dificultad;
        this.longitudPalabra = palabraSecreta.length();
        this.letrasUsadas = new HashSet<>();
        this.historialIntentos = new ArrayList<>();
        this.pistaUtilizada = false;
        this.limiteSuperior = "a".repeat(longitudPalabra);
        this.limiteInferior = "z".repeat(longitudPalabra);
        this.palabrasOrdenadas = palabrasOrdenadas;
    }

    /**
     * Devuelve las letras que han sido usadas.
     * @return Conjunto de letras utilizadas por el jugador.
     */
    public HashSet<String> getLetrasUsadas() {
        return letrasUsadas;
    }

    /**
     * Devuelve en un arreglo el historial de las palabras usadas.
     * @return ArrayList del historial de intentos (palabras usadas).
     */
    public ArrayList<String> getHistorialIntentos() {
        return historialIntentos;
    }

    /**
     * Regresa la palabra que esté en el límite superior.
     * @return La palabra del límite superior.
     */
    public String getLimiteSuperior() {
        return limiteSuperior;
    }

    /**
     * Calcula el límite superior de la palabra y la regresa.
     * @return Porcentaje de proximidad del límite superior.
     */
    public double getProximidadSuperior() {
        return calcularProximidadLimite(limiteSuperior);
    }

    /**
     * Regresa la palabra que esté en el límite inferior.
     * @return La palabra del límite inferior.
     */
    public String getLimiteInferior() {
        return limiteInferior;
    }

    /**
     * Calcula el límite inferior de la palabra y la regresa.
     * @return Porcentaje de proximidad del límite inferior.
     */
    public double getProximidadInferior() {
        return calcularProximidadLimite(limiteInferior);
    }

    /**
     * Devuelve la palabra secreta.
     * @return La palabra secreta de la ronda.
     */
    public String getPalabraSecreta() {
        return palabraSecreta;
    }

    /**
     * Regresa la cantidad de intentos.
     * @return Número de intentos que le quedan al jugador.
     */
    public int getIntentosRestantes() {
        return intentosRestantes;
    }

    /**
     * Regresa la longitud de la palabra.
     * @return Longitud (en letras) de la palabra secreta.
     */
    public int getLongitudPalabra() {
        return longitudPalabra;
    }

    /**
     * Evalúa la palabra ingresada por el usuario, actualizando los límites,
     * además, guarda las letras utilizadas en un HashSet.
     * @param intento Palabra ingresada por el jugador.
     * @return "correcto" si coincide, "antes" si está después de la meta, o "despues" si está antes.
     */
    public String realizarIntento(String intento) {
        // El intento actual se le resta 1.
        intentosRestantes--;

        // Dentro del ciclo, toma la palabra y su longitud
        for (int i = 0; i < intento.length(); i++) {
            // Posteriormente, descompone la palabra en caracteres individuales.
            // Estos caracteres se añaden al HashSet que los guarda,
            // evitando duplicaciones posteriores de letras iguales.
            letrasUsadas.add(String.valueOf(intento.charAt(i)));
        }

        String resultado;
        // Usar compareTo() compara caracter por caracter.
        int intentoComparado = intento.compareTo(palabraSecreta);

        // Si el intento es igual 0, entonces la palabra ingresada y la secreta son iguales.
        if (intentoComparado == 0) {
            resultado = "correcto";
        } else if (intentoComparado > 0) {
            // Si el intento es alfabéticamente mayor, "resultado" será el nuevo rango inferior.
            resultado = "antes";
            limiteInferior = intento;
        } else {
            // Si el intento es alfabéticamente menor, "resultado" será el nuevo rango superior.
            resultado = "despues";
            limiteSuperior = intento;
        }

        historialIntentos.add(intento);

        return resultado;
    }

    /**
     * Calcula el porcentaje de distancia alfabética entre un límite y la palabra secreta,
     * basándose en sus índices dentro del diccionario filtrado.
     * @param limite Palabra que marca el límite actual evaluado.
     * @return Porcentaje decimal de distancia (0.00 a 100.0).
     */
    public double calcularProximidadLimite(String limite) {
        // Si el diccionario llegase a estar vacío o con una sola palabra,
        // retorna automáticamente 0.01 para evitar excepciones.
        int totalPalabras = palabrasOrdenadas.size();
        if (totalPalabras <= 1) {
            return 0.01;
        }

        // Se usa binarySearch() ya que palabrasOrdenadas está ordenado de antes,
        // y en vez de recorrer el ArrayList de inicio a fin, inspecciona el centro y divide la lista a la mitad.
        int indiceSecreto = Collections.binarySearch(palabrasOrdenadas, palabraSecreta.toLowerCase());
        int indiceLimite = Collections.binarySearch(palabrasOrdenadas, limite.toLowerCase());


        // Se maneja un caso si la palabra secreta/límite no existe,
        // binarySearch() regresa un número negativo.
        if (indiceSecreto < 0) {
            // Se pasa el número negativo en el índice a un entero positivo,
            // dicho índice le correspondería ocupar en la ordenación alfabética.
            indiceSecreto = -(indiceSecreto + 1);
        }
        if (indiceLimite < 0) {
            indiceLimite = -(indiceLimite + 1);
        }

        // Uso de Math.abs() para calcula el valor absoluto.
        // No importa si el límite está a la izquierda o a la derecha de la palabra secreta.
        // Es para saber la cantidad de palabras que las separan en el diccionario.
        double distanciaIndices = Math.abs(indiceLimite - indiceSecreto);

        // Este cálculo permite conocer qué tan lejos está un rango
        // en términos porcentuales basados en la lista de palabras.
        double distanciaPorcentaje = (distanciaIndices / totalPalabras) * 100.0;

        // La condición permite que el valor de la aproximación
        // no se acorte ni se alargue de más.
        if (distanciaPorcentaje < 0.001) {
            return 0.001;
        }
        if (distanciaPorcentaje > 100.0) {
            return 100.0;
        }

        // La distancia calculada se multiplica por 100, después
        // se redondea a un valor entero y luego se divide entre 100.
        return Math.round(distanciaPorcentaje * 100.0) / 100.0;
    }

    /**
     * Verifica si un intento está dentro del rango válido actual (entre los límites).
     * @param intento La palabra a verificar.
     * @return true si está dentro del rango, false si está fuera.
     */
    public boolean estaEnRango(String intento) {
        // La palabra debe ser posterior al límite superior Y anterior al límite inferior.
        return intento.compareTo(limiteSuperior) > 0 && intento.compareTo(limiteInferior) < 0;
    }

    /**
     * Indica si al jugador le quedan intentos disponibles.
     * @return true si aún tiene intentos, false si ya no tiene.
     */
    public boolean tieneIntentos() {
        return intentosRestantes > 0;
    }

    /**
     * Indica si el jugador ya utilizó su pista en la ronda.
     * @return true si la pista ya fue usada.
     */
    public boolean pistaUtilizada() {
        return pistaUtilizada;
    }

    /**
     * Modifica el límite superior acercándolo un 1% hacia la palabra secreta.
     * @return El nuevo límite superior asignado.
     */
    public String recorrerPalabraArriba() {
        if (calcularProximidadLimite(limiteSuperior) <= 1.00) {
            return "demasiado cerca";
        }

        this.pistaUtilizada = true;

        int indiceSecreto = Collections.binarySearch(palabrasOrdenadas, palabraSecreta.toLowerCase());
        int indiceLimite = Collections.binarySearch(palabrasOrdenadas, limiteSuperior.toLowerCase());

        if (indiceSecreto < 0) {
            indiceSecreto = -(indiceSecreto + 1);
        }
        if (indiceLimite < 0) {
            indiceLimite = -(indiceLimite + 1);
        }

        int distancia = indiceSecreto - indiceLimite;

        // Se calcula cuántos elementos equivalen al 1% del tamaño total del diccionario.
        int pasos = Math.max(1, (int)(palabrasOrdenadas.size() * 0.01));

        // Si el 1% de "pasos" se pasa de la palabra secreta, se ajusta
        // para que quede exactamente una palabra antes del objetivo.
        if (pasos >= distancia) {
            pasos = Math.max(1, distancia - 1);
        }

        int nuevoIndice = indiceLimite + pasos;
        // Obliga al índice nuevo a mantenerse en rangos válidos del arreglo.
        nuevoIndice = Math.max(0, Math.min(indiceSecreto, nuevoIndice));

        // Entonces el nuevo límite superior será este nuevo índice tomado del cálculo.
        this.limiteSuperior = palabrasOrdenadas.get(nuevoIndice);
        return this.limiteSuperior;
    }

    /**
     * Modifica el límite inferior acercándolo un 1% hacia la palabra secreta.
     * @return El nuevo límite inferior asignado.
     */
    public String recorrerPalabraAbajo() {
        if (calcularProximidadLimite(limiteInferior) <= 1.00) {
            return "demasiado cerca";
        }

        this.pistaUtilizada = true;

        int indiceSecreto = Collections.binarySearch(palabrasOrdenadas, palabraSecreta.toLowerCase());
        int indiceLimite = Collections.binarySearch(palabrasOrdenadas, limiteInferior.toLowerCase());

        if (indiceSecreto < 0) {
            indiceSecreto = -(indiceSecreto + 1);
        }
        if (indiceLimite < 0) {
            indiceLimite = -(indiceLimite + 1);
        }

        int distancia = indiceLimite - indiceSecreto;

        int pasos = Math.max(1, (int)(palabrasOrdenadas.size() * 0.01));

        if (pasos >= distancia) {
            pasos = Math.max(1, distancia - 1);
        }

        int nuevoIndice = indiceLimite - pasos;
        nuevoIndice = Math.max(indiceSecreto, Math.min(palabrasOrdenadas.size() - 1, nuevoIndice));

        // El nuevo límite inferior será este nuevo índice tomado del cálculo.
        this.limiteInferior = palabrasOrdenadas.get(nuevoIndice);
        return this.limiteInferior;
    }

    /**
     * Pista que extrae el primer caracter de la palabra secreta.
     * @return Caracter inicial en formato String.
     */
    public String pistaLetraInicial() {
        pistaUtilizada = true;
        return String.valueOf(palabraSecreta.charAt(0));
    }

    /**
     * Agrega una palabra recién validada a la lista de juego de la ronda actual
     * y mantiene el orden alfabético estricto para no romper la búsqueda binaria.
     * @param palabra La palabra nueva a incorporar.
     */
    public void agregarPalabraEnRonda(String palabra) {
        // Agregamos la palabra a la lista de términos de esta longitud.
        this.palabrasOrdenadas.add(palabra.toLowerCase().trim());
        // Vuelve a ordenar la lista alfabéticamente al instante.
        Collections.sort(this.palabrasOrdenadas);
    }

}
