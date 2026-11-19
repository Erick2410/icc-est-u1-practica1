
import comparadores.ComparadoresEstudiante;
import datos.DatosEstudiantes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import modelo.Estudiante;

public class App {

    public static void main(String[] args) {
        List<Estudiante> originales = DatosEstudiantes.obtenerEstudiantesIniciales();
        List<String> secuenciaOriginalCodigos = extraerCodigos(originales);

        System.out.println("=== LISTA ORIGINAL (20 REGISTROS) ===");
        imprimirLista(originales, false);

        // C1: Orden natural (Comparable<Estudiante>)
        List<Estudiante> resC1 = new ArrayList<>(originales);
        Collections.sort(resC1);
        mostrarYVerificar("C1", "Codigo ascendente (Orden natural Comparable<Estudiante>)",
                resC1, Comparator.naturalOrder(), originales, false);

        // C2 (Lambda de dos parámetros)
        List<Estudiante> resC2Lambda = new ArrayList<>(originales);
        resC2Lambda.sort(ComparadoresEstudiante.consultaC2Lambda());
        mostrarYVerificar("C2 (Lambda)", "Nombre ascendente; codigo ascendente",
                resC2Lambda, ComparadoresEstudiante.consultaC2Lambda(), originales, false);

        // C2 (comparing + thenComparing + referencias a métodos)
        List<Estudiante> resC2MethodRef = new ArrayList<>(originales);
        resC2MethodRef.sort(ComparadoresEstudiante.consultaC2MethodRef());
        mostrarYVerificar("C2 (Method Ref)", "Nombre ascendente; codigo ascendente",
                resC2MethodRef, ComparadoresEstudiante.consultaC2MethodRef(), originales, false);

        // Comparacion de implementaciones C2 (Actividad 3)
        System.out.println("\n[VERIFICACIoN ACTIVIDAD 3 - C2]");
        System.out.println("Ambas versiones de C2 producen exactamente la misma secuencia de codigos?: "
                + extraerCodigos(resC2Lambda).equals(extraerCodigos(resC2MethodRef)));

        // C3: Nota ascendente, sin desempate
        List<Estudiante> resC3 = new ArrayList<>(originales);
        resC3.sort(ComparadoresEstudiante.consultaC3());
        mostrarYVerificar("C3", "Nota ascendente, sin desempate (comparingDouble + referencia a método)",
                resC3, ComparadoresEstudiante.consultaC3(), originales, false);

        // C4: Nota descendente; nombre ascendente
        List<Estudiante> resC4 = new ArrayList<>(originales);
        resC4.sort(ComparadoresEstudiante.consultaC4());
        mostrarYVerificar("C4", "Nota descendente; nombre ascendente (reversed + thenComparing)",
                resC4, ComparadoresEstudiante.consultaC4(), originales, false);

        // C5: Edad ascendente; nombre descendente
        List<Estudiante> resC5 = new ArrayList<>(originales);
        resC5.sort(ComparadoresEstudiante.consultaC5());
        mostrarYVerificar("C5", "Edad ascendente; nombre descendente (comparingInt + reverseOrder)",
                resC5, ComparadoresEstudiante.consultaC5(), originales, false);

        // C6: Carrera ascendente; nota descendente; nombre ascendente; código ascendente
        List<Estudiante> resC6 = new ArrayList<>(originales);
        resC6.sort(ComparadoresEstudiante.consultaC6());
        mostrarYVerificar("C6", "Carrera asc; nota desc; nombre asc; código asc (4 criterios)",
                resC6, ComparadoresEstudiante.consultaC6(), originales, false);

        // C7: Nota ajustada descendente; nombre ascendente; código ascendente
        List<Estudiante> resC7 = new ArrayList<>(originales);
        resC7.sort(ComparadoresEstudiante.consultaC7());
        mostrarYVerificar("C7", "Nota ajustada desc (+0.5 Computacion, max 10.0); nombre asc; codigo asc",
                resC7, ComparadoresEstudiante.consultaC7(), originales, true);

        // Verificaciones de estabilidad y casos de aceptación (Actividad 4)
        ejecutarVerificacionesYCasosAceptacion(originales, secuenciaOriginalCodigos, resC3);
    }

    private static void mostrarYVerificar(String id, String criterio, List<Estudiante> resultado,
                                          Comparator<Estudiante> comp, List<Estudiante> originales,
                                          boolean mostrarNotaAjustada) {
        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println("CONSULTA: " + id + " | CRITERIO: " + criterio);
        System.out.println("--------------------------------------------------------------------------------");
        imprimirLista(resultado, mostrarNotaAjustada);

        boolean conservaTodos = verificarConservacionRegistros(originales, resultado);
        boolean ordenCorrecto = verificarOrdenConsecutivo(resultado, comp);
        System.out.printf("-> Verificación %s: Conserva 20 registros unicos = %b | Orden consecutivo válido = %b%n",
                id, conservaTodos, ordenCorrecto);
    }

    private static void imprimirLista(List<Estudiante> lista, boolean mostrarNotaAjustada) {
        for (int i = 0; i < lista.size(); i++) {
            Estudiante e = lista.get(i);
            if (mostrarNotaAjustada) {
                double ajustada = ComparadoresEstudiante.calcularNotaAjustada(e);
                System.out.printf("%2d. %s | Nota Ajustada: %4.1f%n", (i + 1), e, ajustada);
            } else {
                System.out.printf("%2d. %s%n", (i + 1), e);
            }
        }
    }

    private static List<String> extraerCodigos(List<Estudiante> lista) {
        return lista.stream().map(Estudiante::getCodigo).collect(Collectors.toList());
    }

    private static boolean verificarConservacionRegistros(List<Estudiante> original, List<Estudiante> ordenada) {
        if (original.size() != ordenada.size()) {
            return false;
        }
        Set<String> codigosOrig = new HashSet<>(extraerCodigos(original));
        Set<String> codigosOrd = new HashSet<>(extraerCodigos(ordenada));
        return codigosOrig.equals(codigosOrd) && codigosOrd.size() == original.size();
    }

    private static boolean verificarOrdenConsecutivo(List<Estudiante> lista, Comparator<Estudiante> comp) {
        for (int i = 0; i < lista.size() - 1; i++) {
            if (comp.compare(lista.get(i), lista.get(i + 1)) > 0) {
                return false;
            }
        }
        return true;
    }

    private static void ejecutarVerificacionesYCasosAceptacion(List<Estudiante> originales,
                                                               List<String> secuenciaOriginalCodigos,
                                                               List<Estudiante> resC3) {
        System.out.println("\n================================================================================");
        System.out.println("ACTIVIDAD 4: PRUEBAS DE ESTABILIDAD, BORDES Y CASOS DE ACEPTACION");
        System.out.println("================================================================================");

        // 1. Estabilidad de C3 para estudiantes con nota 8.5
        List<String> codigosNota85 = resC3.stream()
                .filter(e -> Double.compare(e.getNota(), 8.5) == 0)
                .map(Estudiante::getCodigo)
                .collect(Collectors.toList());
        System.out.println("1. Estabilidad C3 (nota == 8.5): " + codigosNota85
                + " -> Esperado [E014, E007, E005, E008, E010]: "
                + codigosNota85.equals(List.of("E014", "E007", "E005", "E008", "E010")));

        // 2. Lista vacía y lista de un solo elemento
        List<Estudiante> listaVacia = new ArrayList<>();
        listaVacia.sort(ComparadoresEstudiante.consultaC6());
        List<Estudiante> listaUnElemento = new ArrayList<>(List.of(originales.get(0)));
        listaUnElemento.sort(ComparadoresEstudiante.consultaC6());
        System.out.println("2. Lista vacia tamanio = " + listaVacia.size()
                + " | Lista 1 elemento conserva = " + listaUnElemento.get(0).getCodigo());

        // 3. C4 sobre [E014, E005, E010] (empate completo en nota 8.5 y nombre Ana)
        List<Estudiante> sublistaAnas = DatosEstudiantes.obtenerSublistaAnasEmpateC4();
        sublistaAnas.sort(ComparadoresEstudiante.consultaC4());
        List<String> codigosAnasC4 = extraerCodigos(sublistaAnas);
        System.out.println("3. C4 sobre [E014, E005, E010] (empate nota y nombre): " + codigosAnasC4
                + " -> Esperado [E014, E005, E010]: "
                + codigosAnasC4.equals(List.of("E014", "E005", "E010")));

        // 4. Caso Propio 1: Desempate en 4to nivel de C6 (Carrera, Nota y Nombre iguales -> desempata Código)
        List<Estudiante> casoPropio1 = DatosEstudiantes.obtenerCasoPropioEmpateTotalC6();
        casoPropio1.sort(ComparadoresEstudiante.consultaC6());
        System.out.println("4. Caso Propio 1 (C6 cuarto criterio codigo ascendente): "
                + extraerCodigos(casoPropio1) + " -> Esperado [E005, E010, E014]");

        // 5. Caso Propio 2: Tope máximo 10.0 en C7 y desempate cuando Computacion alcanza 10.0
        List<Estudiante> casoPropio2 = new ArrayList<>(List.of(
            new Estudiante("E099", "Zoe",  20, 9.8, "Computacion"), // 9.8 + 0.5 -> tope 10.0
            new Estudiante("E098", "Alba", 20, 9.6, "Computacion")  // 9.6 + 0.5 -> tope 10.0
        ));
        casoPropio2.sort(ComparadoresEstudiante.consultaC7());
        System.out.println("5. Caso Propio 2 (C7 tope 10.0 y desempate por nombre): "
                + extraerCodigos(casoPropio2) + " -> Esperado [E098, E099]");

        // 6. Integridad final de la lista original
        boolean originalIntacta = extraerCodigos(originales).equals(secuenciaOriginalCodigos);
        System.out.println("6. Lista original conserva los 20 codigos en secuencia inicial: " + originalIntacta);
    }
}

