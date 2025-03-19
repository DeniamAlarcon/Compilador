/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Compiladores;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author jgeaa
 */
public class Semantico {

    // status = 0 -> Sin errores
    // status = 1 -> Con errores
    public int status = 0;

    public boolean errores[] = {
        false, // error de incompatibilidad de tipo
        false, // error por variable repetida
        false, // error por variable no declarada
        false, // error por variable no inicializada
        false, // error por variable no utilizada
        false, // error por operacion no posible
        false, // error por division entre cero
    };

    public String descErrores[] = {
        "Error Semantico. Posible incompatibilidad de tipo.",
        "Error Semantico. Variable ya declarada.",
        "Error Semantico. Variable no declarada.",
        "Error Semantico. Variable no inicializada.",
        "Error Semantico. Variable no utilizada.",
        "Error Semantico. Operacion Aritmetica con incompatibilidad de tipo.",
        "Error Semantico. Division entre cero."
    };

    public String errorDetallado = "";

    public String[] usoVariables = {
        "scanf", "printf", "while", "if"
    };

    public String[] compVariables = {
        "while", "if"
    };

    // Tabla de asignacion
    // Puede funcionar como traductor de tipos de datos
    public HashMap<String, String> tablaAsignacion = new HashMap();

    // Tabla Operadores Aritmeticos
    public HashMap<String, String> tablaOpArit = new HashMap();

    // Tabla de comparacion
    public String tablaComparacion[][] = {
        //          int, char
        // int      
        // char
        {"Si", "Si"},
        {"Si", "Si"}
    };

    // Tabla de simbolos
    public ArrayList<String[]> tablaSimbolos = new ArrayList<>();

    public String vectorDatos[][];

    public ArrayList<String[]> tempDeclaraciones = new ArrayList<>();
    public ArrayList<String[]> tempUtilizaciones = new ArrayList<>();
    public ArrayList<String[]> tempAsignaciones = new ArrayList<>();

    public Semantico(String[][] vectorDatos) {
        this.vectorDatos = vectorDatos;
    }

    public void generarTablaAsignacion() {
        tablaAsignacion.put("Int", "Entero");
        tablaAsignacion.put("char", "Caracter");
    }

    public void generarTablaOpArit() {
        tablaOpArit.put("Suma", "Entero");
        tablaOpArit.put("Resta", "Entero");
        tablaOpArit.put("Multiplicacion", "Entero");
        tablaOpArit.put("Division", "Entero");

    }

    public String analisisSemantico() {
        // Se genera la tabla de asignacion
        generarTablaAsignacion();
        // Se genera la tabla de operadores aritmeticos
        generarTablaOpArit();

        // Banderas
        boolean declaracion = false;
        boolean utilizacion = false;
        boolean asignacion = false;

        // Se recorre el vector con los datos del analisis lexico
        for (String[] datos : vectorDatos) {
            // Asignacion y creacion de variables
            // Si datos[1] es tipo de dato se realiza una asignacion
            if (datos[1].equals("tipo de dato")) {
                declaracion = true;
            }

            if (declaracion) {
                tempDeclaraciones.add(datos);
            }

            if (datos[1].equals("Punto y coma") && declaracion) {
                declaracion = false;
                // Se realiza la asignacion
                System.out.println("\nDeclaracion\n");
                imprimirTablaAClasficar(tempDeclaraciones);
                generarDeclaracion(tempDeclaraciones);

                // Se limpia el arraylists
                tempDeclaraciones.clear();
            }
            // Uso de variables
            // If, printf, scanf, while
            if (in(datos[0], usoVariables)) {
                utilizacion = true;
            }

            if (utilizacion) {
                tempUtilizaciones.add(datos);
            }

            if (datos[1].equals("Parentesis cierra") && utilizacion) {
                utilizacion = false;
                System.out.println("\nUtilizacion\n");
                imprimirTablaAClasficar(tempUtilizaciones);
                // Se realiza la utilizacion
                generarUtilizacion(tempUtilizaciones);
                tempUtilizaciones.clear();
            }
            // Asignacion
            if (datos[1].equals("Variable") && !declaracion && !utilizacion) {
                asignacion = true;
            }

            if (asignacion) {
                tempAsignaciones.add(datos);
            }

            if (datos[1].equals("Punto y coma") && asignacion) {
                asignacion = false;
                System.out.println("\nAsignacion\n");
                imprimirTablaAClasficar(tempAsignaciones);
                // Se realiza la utilizacion
                generarAsignacion(tempAsignaciones);
                tempAsignaciones.clear();
            }
            // Se gestionan errores
            if (errores[0]) {
                status = 1;
                return descErrores[0].concat(errorDetallado);
            } else if (errores[1]) {
                status = 1;
                return descErrores[1].concat(errorDetallado);
            } else if (errores[2]) {
                status = 1;
                return descErrores[2].concat(errorDetallado);
            } else if (errores[3]) {
                status = 1;
                return descErrores[3].concat(errorDetallado);
            } else if (errores[4]) {
                status = 1;
                return descErrores[4].concat(errorDetallado);
            } else if (errores[5]) {
                status = 1;
                return descErrores[5].concat(errorDetallado);
            } else if (errores[6]) {
                status = 1;
                return descErrores[6].concat(errorDetallado);
            }
        }

        // Todas las variables deben de estar inicializadas al final de la ejecucion
        if (!variablesFinalInicializada()) {
            status = 1;
            return descErrores[3].concat(errorDetallado);
        }
        if (!variablesFinalUtilizada()) {
            status = 1;
            return descErrores[4].concat(errorDetallado);
        }

        System.out.println("\nTabla de simbolos");
        System.out.println(generarTablaSimbolos());
        return generarTablaSimbolos();

    }

    // Funciones del analisis semantico
    // Todas las variables deben de estar declaradas y asignadas
    public boolean variablesFinalInicializada() {
        for (String[] variable : tablaSimbolos) {
            if (!variable[2].equals("Si")) {
                errorDetallado = "\n La variable ".concat(variable[0]) + " no ha sido inicializada";
                return false;
            }
        }
        return true;

    }

    // Todas las variables deben de ser usadas
    public boolean variablesFinalUtilizada() {
        for (String[] variable : tablaSimbolos) {
            if (!variable[3].equals("Si")) {
                errorDetallado = "\n La variable ".concat(variable[0]) + " no ha sido utilizada";
                return false;
            }
        }
        return true;
    }

    // Generar Declaracion
    public void generarDeclaracion(ArrayList<String[]> declaracion) {
        // Se genera la entrada de la tabla de simbolos
        // Asignacion 

        // Banderas
        boolean enAsignacion = false;
        boolean asignacionCero = false;
        boolean division = false;
        // Tabla de asignacion
        // 0 -> Nombre de la variable
        // 1 -> Tipo
        // 2 -> Inicializada
        // 3 -> Utilizada
        // 4 -> Cero
        String variableActual = "";
        int indice = 0;
        String tipoDato = "";
        for (String[] dato : declaracion) {
            if (indice == 0) {
                tipoDato = dato[0];
                indice++;
            } else {
                // Si sigue una variable debe de verificarse 
                // si existe en la tabla de simbolos, si no 
                // existe debe de crearse
                if (dato[1].equals("Variable")) {
                    String datosTablaSimbolos[] = new String[5];
                    if (!tablaSimbolos.isEmpty()) {
                        // Se crea la entrada en la tabla de simbolos
                        // si la variable no existe
                        if (!existeVariable(dato[0])) {
                            if (enAsignacion) {
                                errores[2] = true;
                                errorDetallado = "\nVariable no declarada: ".concat(dato[0]);
                                return;
                            }
                            variableActual = dato[0];
                            datosTablaSimbolos[0] = dato[0]; // Nombre variable
                            datosTablaSimbolos[1] = tipoDato; // Tipo
                            datosTablaSimbolos[2] = "No"; // Iniciada
                            datosTablaSimbolos[3] = "No"; // Utilizada 
                            datosTablaSimbolos[4] = "No"; // Cero
                            // Se inserta en la tabla
                            tablaSimbolos.add(datosTablaSimbolos);
                        } else {
                            // Existe la variable en la tabla de simbolos
                            if (enAsignacion) {
                                // La variable debe de estar inicializada
                                if (!estaInicializada(informacionVariable(dato[0]))) {
                                    errores[3] = true;
                                    errorDetallado = "\nVariable no inicializada: ".concat(dato[0])
                                            + "\n En declaracion de variable: ".concat(variableActual);
                                    return;
                                } else {
                                    // Si esta inicializada, debe de coincidir con tipoDato 
                                    if (!mismoTipoDatoVariable(variableActual, dato[0])) {
                                        // Error por incompatibilidad de tipo
                                        errores[0] = true;
                                        errorDetallado = "\nVariable: " + variableActual
                                                + " \n Tipo de dato: " + informacionVariable(variableActual)[1]
                                                + " \nVariable de asignacion: " + dato[0]
                                                + " \n Tipo de dato recibido: " + informacionVariable(dato[0])[1];
                                        return;
                                    }
                                    // Si la variable actual tiene cero, se activa la bandera de cero
                                    if (informacionVariable(dato[0])[4].equals("Si")) {
                                        asignacionCero = true;
                                    } else {
                                        asignacionCero = false;
                                    }
                                    // La bandera de division debe de estar apagada
                                    // El valor cero no puede ser "Si"
                                    if (division && informacionVariable(dato[0])[4].equals("Si")) {
                                        // Se retorna un error por division entre cero
                                        errores[6] = true;
                                        errorDetallado = "\nVariable: ".concat(variableActual)
                                                .concat("\nTipo de dato: ".concat(tipoDato))
                                                .concat("\nVariable de error: ".concat(dato[0]));
                                    }
                                    // Tipo de dato correcto
                                    // Debe de actualizarse la tabla de simbolos para 
                                    // utilizar la variable
                                    utilizarVariable(dato[0]);
                                }
                            } else {
                                errores[1] = true;
                                errorDetallado = "\nVariable ya declarada: ".concat(dato[0]);
                                return;
                            }
                        }
                    } else {
                        // Si la tabla esta vacia se crea la entrada
                        // en la tabla de simbolos
                        variableActual = dato[0];
                        datosTablaSimbolos[0] = dato[0];
                        datosTablaSimbolos[1] = tipoDato;
                        datosTablaSimbolos[2] = "No";
                        datosTablaSimbolos[3] = "No";
                        datosTablaSimbolos[4] = "No";
                        // Se inserta en la tabla
                        tablaSimbolos.add(datosTablaSimbolos);
                    }
                } else if (dato[1].equals("igual")) {
                    // Se activa una bandera de asignacion
                    enAsignacion = true;
                } else if (dato[1].equals("coma") || dato[1].equals("Punto y coma")) {
                    // Se inicializa la variable si asignacion es true
                    if (enAsignacion) {
                        inicializarVariable(variableActual);
                    }
                    // Si la bandera asignacion esta activa, se inicializa en cero
                    if (asignacionCero) {
                        asignarCero(variableActual);
                    }
                    // Se desactivan las bandera
                    enAsignacion = false;
                    division = false;
                    asignacionCero = false;
                    // Se borra la variable actual
                    variableActual = "";
                } else if (dato[1].equals("Entero") || dato[1].equals("Caracter")) {
                    // Dato[1] debe de coincidir con el tipo de la variable
                    if (!mismoTipoDato(dato[1], variableActual)) {
                        // Error por incompatibilidad de tipo
                        errores[0] = true;
                        errorDetallado = "\nVariable: " + variableActual
                                + " \nTipo de dato: " + informacionVariable(variableActual)[1]
                                + " \nTipo de dato recibido: " + dato[1];
                        return;
                    }
                    if (dato[0].equals("0")) {
                        asignacionCero = true;
                    } else {
                        asignacionCero = false;
                    }
                    // Dato[0] no puede ser cero si la bandera de division esta activa
                    if (dato[0].equals("0") && division) {
                        // Se retorna un error por division entre cero
                        errores[6] = true;
                        errorDetallado = "\nVariable: ".concat(variableActual)
                                .concat("\nTipo de dato: ".concat(tipoDato));
                    }
                } else if (tablaOpArit.containsKey(dato[1])) {
                    // El tipo de dato debe de corresponder con 
                    // el tipo de dato de la variable actual
                    if (!mismoTipoDatoOpArit(dato[1], variableActual)) {
                        // Error por incompatibilidad de tipo
                        errores[0] = true;
                        errorDetallado = "\nVariable: ".concat(variableActual)
                                + " \nTipo de dato: ".concat(informacionVariable(variableActual)[1])
                                + " \nTipo de dato de operador: ".concat(tablaOpArit.get(dato[1]));
                        return;
                    }
                    // Si dato[1] = Division se activa la bandera division
                    // Si no, la bandera se apaga
                    if (dato[1].equals("Division")) {
                        division = true;
                    } else {
                        division = false;
                    }
                }
            }
        }
    }

    // Generar Utilizacion
    public void generarUtilizacion(ArrayList<String[]> utilizacion) {
        // Banderas
        boolean enComparacion = false;
        boolean primerVariable = true;
        boolean comparacionCons = false;
        // Variables
        int indice = 0;
        int valorInt = 0;
        int valorChar = 1;
        String variableActual = "";
        // Se debe de recorrer el array para leer las posibles variables
        for (String[] datos : utilizacion) {
            if (indice == 0) {
                // If y while requieren de comparaciones
                if (in(datos[0], compVariables)) {
                    enComparacion = true;
                }
                // Si en comparacion es falso, se trata de una impresion
                // o una lectura de datos   
                indice++;
            } else {
                if (enComparacion) {
                    if (datos[1].equals("Variable")) {
                        // La variable debe de existir en la tabla de simbolos
                        if (existeVariable(datos[0])) {
                            // La variable debe de estar inicializada
                            if (estaInicializada(informacionVariable(datos[0]))) {
                                // Si esta inicializada, se debe de comprobar 
                                // si es la primera variable
                                if (primerVariable) {
                                    primerVariable = false;
                                    // Se modifica el valor de la variable actual
                                    variableActual = datos[0];
                                } else {
                                    // Si no lo es, se realiza la comparacion
                                    // Posibilidades
                                    // Variable Variable
                                    // Constante Variable
                                    if (comparacionCons) {
                                        // Constante Variable
                                        // Se cambia la bandera 
                                        comparacionCons = false;
                                        // Se obtiene el tipo de dato de las variables
                                        String tipoVarA = informacionVariable(datos[0])[1];
                                        // Se busca en la tabla de comparacion
                                        int indiceA = (variableActual.equals("Entero")) ? valorInt : valorChar;
                                        int indiceB = (tipoVarA.equals("Char")) ? valorChar : valorInt;
                                        // Si es posible realizar la comparacion, se puede utilizar la variable
                                        if (tablaComparacion[indiceA][indiceB].equals("Si")) {
                                            utilizarVariable(datos[0]);
                                        }
                                        variableActual = datos[0];
                                    } else {
                                        // Variable Variable
                                        // Se obtiene el tipo de dato de las variables
                                        String tipoVarA = informacionVariable(variableActual)[1];
                                        String tipoVarB = informacionVariable(datos[0])[1];
                                        // Se busca en la tabla de comparacion
                                        int indiceA = (tipoVarA.equals("Int")) ? valorInt : valorChar;
                                        int indiceB = (tipoVarB.equals("Char")) ? valorChar : valorInt;
                                        // Si es posible realizar la comparacion, se puede utilizar la variable
                                        if (tablaComparacion[indiceA][indiceB].equals("Si")) {
                                            utilizarVariable(variableActual);
                                            utilizarVariable(datos[0]);
                                        }
                                        // Se cambia el valor de la variable actual
                                        variableActual = datos[0];
                                    }
                                }
                            } else {
                                // Se retorna un error de variable no inicializada
                                errores[3] = true;
                                errorDetallado = "\nVariable no inicializada: ".concat(datos[0]);
                                return;
                            }
                        } else {
                            // Se retorna un error de variable no declarada
                            errores[2] = true;
                            errorDetallado = "\nVariable no declarada: ".concat(datos[0]);
                            return;
                        }
                    } else if (datos[1].equals("Entero") || datos[1].equals("Caracter")) {
                        // Se puede realizar la comparacion con un valor constante
                        if (primerVariable) {
                            primerVariable = false;
                            // Se activa la bandera de comparacion con constantes
                            comparacionCons = true;
                            // Se guarda el tipo de constante en variable actual
                            variableActual = datos[1];
                        } else {
                            // Posibilidades
                            // Constante Constante
                            // Variable Constante
                            if (comparacionCons) {
                                // Constante Constante
                                // No se utilizan variables
                                // Se comprueba que puedan compararse
                                int indiceA = (variableActual.equals("Entero")) ? valorInt : valorChar;
                                int indiceB = (datos[1].equals("Caracter")) ? valorChar : valorInt;
                                // Si es posible realizar la comparacion
                            } else {
                                // Variable Constante
                                // Se obtiene el tipo de dato de las variables
                                String tipoVarA = informacionVariable(variableActual)[1];
                                // Se comprueba que puedan compararse
                                int indiceA = (variableActual.equals("Int")) ? valorInt : valorChar;
                                int indiceB = (datos[1].equals("Caracter")) ? valorChar : valorInt;
                                // Si es posible realizar la comparacion, se puede utilizar la variable
                                if (tablaComparacion[indiceA][indiceB].equals("Si")) {
                                    utilizarVariable(variableActual);
                                }
                                variableActual = datos[1];
                            }
                        }
                    }
                } else {
                    // Impresion o lectura
                    // La variable debe de estar inicializada
                    if (datos[1].equals("Variable")) {
                        // La variable debe de existir en la tabla de simbolos
                        if (existeVariable(datos[0])) {
                            // Si la variable esta inicializada
                            // entonces puede ser utilizada
                            if (estaInicializada(informacionVariable(datos[0]))) {
                                // Se realiza la utilizacion de la variable
                                utilizarVariable(datos[0]);
                            } else {
                                // Se retorna un error de variable no inicializada
                                errores[3] = true;
                                errorDetallado = "\nVariable no inicializada: ".concat(datos[0]);
                                return;
                            }
                        } else {
                            // Se retorna un error de variable no declarada
                            errores[2] = true;
                            errorDetallado = "\nVariable no declarada: ".concat(datos[0]);
                            return;
                        }
                    }
                }
            }
        }
    }

    // Generar Asignacion
    public void generarAsignacion(ArrayList<String[]> asignacion) {
        // Variables
        int indice = 0;
        String tipoDato = "";
        String variableActual = "";
        // Banderas
        boolean division = false;
        boolean asignacionCero = false;
        // Se recorre el array de datos
        for (String[] datos : asignacion) {
            if (indice == 0) {
                // Se verifica que la variable existe
                if (!existeVariable(datos[0])) {
                    // Se retorna error por variable no declarada
                    errores[2] = true;
                    errorDetallado = "\nVariable no declarada: ".concat(datos[0]);
                    return;
                }
                // Si existe, se obtiene el tipo de dato
                tipoDato = informacionVariable(datos[0])[1];
                // Tambien el nombre de la variable
                variableActual = datos[0];
                indice++;
            } else {
                // Token actual variable
                if (datos[1].equals("Variable")) {
                    // Se comprueba que exista
                    if (!existeVariable(datos[0])) {
                        // Se retirna error por variable no declarada
                        errores[2] = true;
                        errorDetallado = "\nVariable no declarada: ".concat(datos[0]);
                        return;
                    }
                    // Se comprueba que este inicializada
                    if (!estaInicializada(informacionVariable(datos[0]))) {
                        // Se retirna error por variable no inicializada
                        errores[3] = true;
                        errorDetallado = "\nVariable no inicializada: ".concat(datos[0]);
                        return;
                    }
                    // Se comprueba que la variable tenga el mismo tipo de dato
                    if (!tipoDato.equals(informacionVariable(datos[0])[1])) {
                        System.out.println("tipo de dato");
                        // Se retirna error por incompatibilidad de tipo
                        errores[0] = true;
                        errorDetallado = "\nVariable: ".concat(variableActual)
                                .concat("\nTipo de dato: ".concat(tipoDato))
                                .concat("\nVariable de error: ".concat(datos[0]))
                                .concat("\nTipo de dato recibido: ".concat(informacionVariable(datos[0])[1]));
                        return;
                    }
                    // Si la variable tiene cero, se activa la bandera de cero
                    if (informacionVariable(datos[0])[4].equals("Si")) {
                        asignacionCero = true;
                    } else {
                        asignacionCero = true;
                    }
                    // Si la variable tiene cero y division estan activadas,
                    // entonces se retorna un error
                    if (division && informacionVariable(datos[0])[4].equals("Si")) {
                        errores[6] = true;
                        errorDetallado = "\nVariable: ".concat(variableActual)
                                .concat("\nTipo de dato: ".concat(tipoDato))
                                .concat("\nVariable de error: ".concat(datos[0]));
                    }
                    // Si son del mismo tipo, la variable puede ser utilizada
                    utilizarVariable(datos[0]);
                } else if (tablaOpArit.containsKey(datos[1])) {
                    // Token actual operador aritmetico
                    // El tipo de dato debe de poder realizar operaciones aritmeticas
                    // Si son tipos diferentes, debe de retornar un error por operacion aritmetica no permitida
                    if (!tablaAsignacion.get(tipoDato).equals(tablaOpArit.get(datos[1]))) {
                        errores[5] = true;
                        errorDetallado = "\nTipo de dato: ".concat(tipoDato)
                                .concat("\nOperacion Aritmetica: ".concat(datos[0]))
                                .concat("\nTipo de dato de operacion: ".concat(tablaOpArit.get(datos[1])));
                        return;
                    }
                    // Si el operador aritmetico es division, se activa la bandera de division
                    if (datos[1].equals("Division")) {
                        division = true;
                    } else {
                        division = false;
                    }
                } else if (datos[1].equals("Entero") || datos[1].equals("Caracter")) {
                    // Token actual constante
                    // Debe de ser del mismo tipo que la variable actual
                    if (!tablaAsignacion.get(tipoDato).equals(datos[1])) {
                        // Se retorna un error de tipo
                        errores[0] = true;
                        errorDetallado = "\nVariable: ".concat(variableActual)
                                .concat("\nTipo de dato: ".concat(tipoDato))
                                .concat("\nConstante: ".concat(datos[0]))
                                .concat("\nTipo de dato: ".concat(datos[1]));
                        return;
                    }
                    // Si datos[0] es cero, se activa la bandera cero
                    if (datos[0].equals("0")) {
                        asignacionCero = false;
                    } else {
                        asignacionCero = false;
                    }
                    // Si la bandera de division esta activa, y el valor es cero,
                    // se retorna un error por division entre cero
                    if (division && datos[0].equals("0")) {
                        // Error por divion entre cero
                        errores[6] = true;
                        errorDetallado = "\nVariable: ".concat(variableActual)
                                .concat("\nTipo de dato: ".concat(tipoDato))
                                .concat("\nValor de error: ".concat(datos[0]));
                    }
                } else if (datos[1].equals("Punto y coma")) {
                    // Token actual ;
                    // Si no hay errores se inicializa la variable
                    inicializarVariable(variableActual);
                    // Si la bandera asignacion cero esta encendida
                    // se inicializa en cero 
                    if (asignacionCero) {
                        asignarCero(variableActual);
                    }
                }
            }
        }
    }

    public boolean existeVariable(String variable) {
        for (String[] dato : tablaSimbolos) {
            // 0 -> Nombre de la variable
            // 1 -> Tipo
            // 2 -> Inicializada
            // 3 -> Utilizada
            if (dato[0].equals(variable)) {
                return true;
            }
        }
        return false;
    }

    public String[] informacionVariable(String variable) {
        String[] informacion = new String[4];
        for (String[] datos : tablaSimbolos) {
            if (datos[0].equals(variable)) {
                informacion = datos;
            }
        }
        return informacion;
    }

    public int indiceInformacionVariable(String variable) {
        int indice = 0;
        for (String[] datos : tablaSimbolos) {
            if (datos[0].equals(variable)) {
                break;
            }
            indice++;
        }
        return indice;
    }

    public boolean estaInicializada(String[] datos) {
        return datos[2].equals("Si");
    }

    public void inicializarVariable(String variable) {
        int indice = indiceInformacionVariable(variable);
        String[] datos = informacionVariable(variable);
        // Se inicializa la variable
        datos[2] = "Si";
        tablaSimbolos.set(indice, datos);
    }

    public void utilizarVariable(String variable) {
        int indice = indiceInformacionVariable(variable);
        String[] datos = informacionVariable(variable);
        // Se utiliza la variable
        datos[3] = "Si";
        tablaSimbolos.set(indice, datos);
    }

    public void asignarCero(String variable) {
        int indice = indiceInformacionVariable(variable);
        String[] datos = informacionVariable(variable);
        datos[4] = "Si";
        tablaSimbolos.set(indice, datos);
    }

    public boolean mismoTipoDato(String nombre, String variable) {
        String[] datos = informacionVariable(variable);
        // Indice 1 indica tipo
        return tablaAsignacion.get(datos[1]).equals(nombre);
    }

    public boolean mismoTipoDatoVariable(String variableActual, String variableAsignacion) {
        String[] datosActual = informacionVariable(variableActual);
        String[] datosAsignacion = informacionVariable(variableAsignacion);
        // Indice 1 indica tipo
        return datosActual[1].equals(datosAsignacion[1]);
    }

    public boolean mismoTipoDatoOpArit(String nombre, String variableActual) {
        String[] datos = informacionVariable(variableActual);
        return tablaAsignacion.get(datos[1]).equals(tablaOpArit.get(nombre));
    }

    public boolean in(String nombre, String[] valores) {
        // System.out.println("IN");
        // System.out.println("nombre: " + nombre);
        for (String valor : valores) {
            // System.out.println("valor: " + valor);
            if (nombre.equals(valor)) {
                return true;
            }
        }
        return false;
    }

    public void imprimirTablaSimbolos() {
        for (String[] datos : tablaSimbolos) {
            System.out.println("Variable: " + datos[0]
                    + "\tTipo: " + datos[1]
                    + "\tInicializada: " + datos[2]
                    + "\tUtilizada: " + datos[3]);
        }
    }

    public String generarTablaSimbolos() {
        String tablaSimbolosTexto = "\tTabla de Simbolos\nVariable\tTipo\tInicializada\tUtilizada\n";
        for (String[] datos : tablaSimbolos) {
            tablaSimbolosTexto += datos[0]
                    + "\t" + datos[1]
                    + "\t" + datos[2]
                    + "\t" + datos[3]
                    + "\n";
        }

        return tablaSimbolosTexto;
    }

    public void imprimirTablaAClasficar(ArrayList<String[]> vector) {
        for (String[] datos : vector) {
            System.out.println("Variable: " + datos[0]
                    + " Nombre: " + datos[1]);
        }
    }

    public int getStatus() {
        return status;
    }

}
