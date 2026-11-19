# Practica de Laboratorio PL2.1: Ordenamiento de estudiantes con Comparator y Comparable

* **Autor:** Erick Fernando Solis Campoverde
* **Carrera:** Computacion - Universidad Politecnica Salesiana
* **Asignatura:** Estructura de Datos
* **Version del JDK:** OpenJDK 17.0.12 *

---

## 1. Objetivo

Implementar el orden natural de la clase `Estudiante` mediante `Comparable<Estudiante>` y construir criterios de ordenamiento externos con `Comparator<Estudiante>` (usando lambdas, referencias a metodos, criterios compuestos, sentidos ascendentes/descendentes y claves derivadas sin mutar el estado), verificando la estabilidad de TimSort, la conservación de registros y la preservacion intacta de la secuencia original.

---

## 2. Estructura del Proyecto

```text
PL2.1-Ordenamiento-Estudiantes/
├── README.md
├── evidencias/
│   ├── salida_consola.txt
│   └── tabla_pruebas.md
└── src/
    ├── modelo/
    │   └── Estudiante.java
    ├── comparadores/
    │   └── ComparadoresEstudiante.java
    ├── datos/
    │   └── DatosEstudiantes.java
    └── app/
        └── Main.java


 3. Comandos de Compilacion y Ejecucion

# Compilar todas las clases en el directorio out/
javac -d out src/modelo/Estudiante.java src/comparadores/ComparadoresEstudiante.java src/datos/DatosEstudiantes.java src/app/Main.java

# Ejecutar la aplicacion
java -cp out app.Main

# Generar el archivo de evidencia con la salida real
java -cp out app.Main > evidencias/salida_consola.txt

4. Tabla

ID  Orden Requerido  Requisito Técnico  Método Implementador
C1 Codigo ascendente Comparable<Estudiante> y ordenamiento natural (Collections.sort),Estudiante.compareTo(Estudiante)
C2 Nombre ascendente; código ascendente,Comparator con lambda de dos parámetros,ComparadoresEstudiante.consultaC2Lambda()
C2 (Alt),Nombre ascendente; código ascendente,"comparing(), thenComparing() y referencias a métodos",ComparadoresEstudiante.consultaC2MethodRef()
C3 "Nota ascendente, sin desempate",comparingDouble y referencia a método,ComparadoresEstudiante.consultaC3()
C4 Nota descendente; nombre ascendente,reversed() y thenComparing(),ComparadoresEstudiante.consultaC4()
C5 Edad ascendente; nombre descendente,comparingInt y sentido independiente (Comparator.reverseOrder()),ComparadoresEstudiante.consultaC5()
C6 Carrera ascendente; nota descendente; nombre ascendente; código ascendente,Composición de cuatro criterios encadenados,ComparadoresEstudiante.consultaC6()
C7 Nota ajustada descendente; nombre ascendente; código ascendente,"Lambda con Math.min(10.0, nota + 0.5) para Computacion sin mutar el objeto",ComparadoresEstudiante.consultaC7()





5. Análisis Técnico (Actividades 1, 3 y 4)

5.1 Signo del resultado de compareTo() (Actividad 1)
En Estudiante.compareTo(Estudiante otro), la comparación this.codigo.compareTo(otro.codigo) retorna un entero cuyo signo define el orden relativo:

Negativo (< 0): this precede a otro (código lexicográficamente menor).

Cero (== 0): Ambos estudiantes tienen el mismo código.

Positivo (> 0): this va después de otro (código lexicográficamente mayor).

5.2 Por qué invertir un comparador compuesto completo invierte también los desempates (Actividad 3)
Un Comparator compuesto devuelve el primer resultado distinto de cero obtenido de izquierda a derecha. Al llamar a .reversed() sobre todo el comparador compuesto ((c1.thenComparing(c2)).reversed()), Java niega el signo final de toda la comparación (-comp.compare(a, b)). Esto invierte tanto el criterio primario como cualquier criterio secundario que resuelva empates:

Relación con C4: En C4 se desea nota descendente pero nombre ascendente. Por eso se aplica .reversed() únicamente al comparador de nota (Comparator.comparingDouble(Estudiante::getNota).reversed()) antes de encadenar .thenComparing(Estudiante::getNombre). Si se hubiera puesto .reversed() al final, los empates de nota se habrían ordenado de Z a A.

Relación con C5: En C5 se desea edad ascendente y nombre descendente. Se mantiene intacto comparingInt(Estudiante::getEdad) y se pasa Comparator.reverseOrder() como segundo argumento de .thenComparing(...) para invertir solo el criterio secundario.

5.3 Por qué comparar dos estudiantes por nota puede devolver 0 con códigos distintos (Actividad 3)
Comparator evalúa únicamente la clave proyectada (en C3, el valor double de nota), ignorando la identidad o el resto de atributos del objeto. Si dos estudiantes distintos (E014 y E007) tienen ambos nota 8.5, Double.compare(8.5, 8.5) retorna 0 (equivalencia respecto al criterio de ordenamiento), lo cual es distinto de equals() e indispensable para que algoritmos estables como TimSort conserven su orden relativo original.

5.4 Justificación de los dos casos propios añadidos (Actividad 4)
Caso Propio 1 (C6 con empate en los tres primeros criterios): Evalúa tres registros de "Computacion", nota 8.5 y nombre "Ana" (E010, E014, E005). Comprueba que cuando los tres primeros niveles empatan, el cuarto criterio (Estudiante::getCodigo) resuelve el orden dejando E005 -> E010 -> E014.

Caso Propio 2 (C7 con saturación del tope 10.0): Evalúa dos estudiantes de "Computacion" con notas 9.8 (Zoe, E099) y 9.6 (Alba, E098). Comprueba que Math.min(10.0, nota + 0.5) trunca ambas notas ajustadas exactamente en 10.0 sin exceder el límite y que el segundo criterio (nombre ascendente) ubica a Alba antes que Zoe.




6. Conclusiones

1.Me resulto util dejar el orden por codigo dentro de Estudiante con compareTo porque ese es el orden natural, el que casi siempre usa la coordinacion y el que tiene sentido por defecto. En cambio, sacar las consultas C2 a C7 a ComparadoresEstudiante me evito estar tocando la clase base cada vez que pedian un orden distinto. Si hubiera metido todo en Estudiante habria terminado con una clase llena de if y desordenada. Asi, Estudiante queda limpia y cada criterio nuevo es solo un comparador nuevo, sin riesgo de romper lo que ya funcionaba.

2.En la salida de salida_consola.txt observe que en C3 los estudiantes con nota 8.5 (E014, E007, E005, E008, E010) no salieron en orden aleatorio, sino que mantuvieron el orden en el que estaban en la lista original. Lo mismo paso en C4 con E014, E005 y E010. Eso es por la estabilidad del sort de Java: cuando hay empate, no los revuelve, respeta el orden previo. Y eso solo funciono porque en cada consulta hice new ArrayList<>(originales). Si hubiera ordenado la lista original directamente, la lista inicial de 20 registros se habria perdido despues de la primera consulta y las siguientes ya no hubieran partido del mismo orden.

3.En C7 logre sumar los 0.5 puntos a Computacion con tope 10.0 usando una lambda que calcula la nota ajustada al momento de comparar, sin modificar el campo nota del objeto. Por eso la nota original de Sofia (9.5) y de Maria (9.2) quedo intacta, solo se uso el valor calculado para ordenar. Y usar comparingDouble / comparingInt es mas seguro que restar numeros porque la resta puede dar errores por precision en double o desbordamiento en int, y ademas no respeta bien el contrato de Comparator. Con comparingDouble la comparacion es exacta y mas legible.

