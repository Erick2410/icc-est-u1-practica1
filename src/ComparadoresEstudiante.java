
import java.util.Comparator;
import modelo.Estudiante;

public final class ComparadoresEstudiante {

    private ComparadoresEstudiante() {
    }

    // C2 (Version 1): Nombre ascendente; codigo ascendente.
    // Requisito: Comparator con lambda de dos parametros.
    public static Comparator<Estudiante> consultaC2Lambda() {
        return (e1, e2) -> {
            int cmpNombre = e1.getNombre().compareTo(e2.getNombre());
            if (cmpNombre != 0) {
                return cmpNombre;
            }
            return e1.getCodigo().compareTo(e2.getCodigo());
        };
    }

    // C2 (Version 2 - Actividad 3): Nombre ascendente; codigo ascendente.
    // Requisito: comparing(), thenComparing() y referencias a metodos.
    public static Comparator<Estudiante> consultaC2MethodRef() {
        return Comparator.comparing(Estudiante::getNombre)
                .thenComparing(Estudiante::getCodigo);
    }

    // C3: Nota ascendente, sin desempate.
    // Requisito: comparingDouble y referencia a metodo.
    public static Comparator<Estudiante> consultaC3() {
        return Comparator.comparingDouble(Estudiante::getNota);
    }

    // C4: Nota descendente; nombre ascendente.
    // Requisito: reversed y thenComparing.
    public static Comparator<Estudiante> consultaC4() {
        return Comparator.comparingDouble(Estudiante::getNota)
                .reversed()
                .thenComparing(Estudiante::getNombre);
    }

    // C5: Edad ascendente; nombre descendente.
    // Requisito: comparingInt y sentido independiente para el criterio secundario.
    public static Comparator<Estudiante> consultaC5() {
        return Comparator.comparingInt(Estudiante::getEdad)
                .thenComparing(Estudiante::getNombre, Comparator.reverseOrder());
    }

    // C6: Carrera ascendente; nota descendente; nombre ascendente; codigo ascendente.
    // Requisito: Composicion de cuatro criterios.
    public static Comparator<Estudiante> consultaC6() {
        return Comparator.comparing(Estudiante::getCarrera)
                .thenComparing(Comparator.comparingDouble(Estudiante::getNota).reversed())
                .thenComparing(Estudiante::getNombre)
                .thenComparing(Estudiante::getCodigo);
    }

    // Funcion auxiliar pura para calcular la nota ajustada de C7 sin modificar el objeto.
    public static double calcularNotaAjustada(Estudiante e) {
        return "Computacion".equals(e.getCarrera())
                ? Math.min(10.0, e.getNota() + 0.5)
                : e.getNota();
    }

    // C7: Nota ajustada descendente; nombre ascendente; codigo ascendente.
    // Requisito: Lambda que calcule Math.min(10.0, nota + 0.5) para Computacion y conserve las demás.
    public static Comparator<Estudiante> consultaC7() {
        return Comparator.comparingDouble(
                (Estudiante e) -> "Computacion".equals(e.getCarrera())
                        ? Math.min(10.0, e.getNota() + 0.5)
                        : e.getNota()
        ).reversed()
         .thenComparing(Estudiante::getNombre)
         .thenComparing(Estudiante::getCodigo);
    }
}

