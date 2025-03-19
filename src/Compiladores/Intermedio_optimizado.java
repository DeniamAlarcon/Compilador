/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compiladores;

import java.awt.List;
import java.util.ArrayList;

/**
 *
 * @author Deniam
 */
public class Intermedio_optimizado {

    public String[][] vectorDatos;
    public String regresar = "";
    public ArrayList<String> GuardarSeparar = new ArrayList<>();
    public ArrayList<Integer> posiciones = new ArrayList<>();
    public ArrayList<String> TemTemporales = new ArrayList<>();
    public ArrayList<String> GuardarOpreaciones = new ArrayList<>();
    public ArrayList<String> GuardarIf = new ArrayList<>();
    public ArrayList<String> EtiquetasIf = new ArrayList<>();
    public ArrayList<String> EtiquetasWhile = new ArrayList<>();
    public ArrayList<String> etiquetas = new ArrayList<>();
    public ArrayList<String> EtiquetasNo = new ArrayList<>();
    public ArrayList<String> EtiquetasAnd = new ArrayList<>();
    public ArrayList<String> EtiquetasOr = new ArrayList<>();
    public ArrayList<String> EtiquetasElse = new ArrayList<>();
    public ArrayList<String> GuardarE1 = new ArrayList<>();
    public ArrayList<String> GuardarE2 = new ArrayList<>();
    public ArrayList<ArrayList<String>> cuadruples = new ArrayList<>();
    public int contadorTemo = 0;
    public int contadorParentesis = 0;
    public int contadorOperandos = 0;
    public int contadorRecursivo = 0;
    public int recursivo = 0;
    public int contador = 0;
    public int contadorEtiquetas = 0;
    public boolean entrar = true;
    public boolean entrarElse = false;
    public boolean entrarIf = false;
    public boolean entrarWhile = false;

    //public int pasarDatos;
    public ArrayList<String> guardarOpreacion = new ArrayList<>();
    public ArrayList<String> guardarOpreacion2 = new ArrayList<>();
    public String etiqueta1;
    public String etiqueta2;
    public boolean bandera = false;
    public String[][] copiar;
    int numero1;
    int numero2;

    public Intermedio_optimizado(String[][] vectorDatos) {
        this.vectorDatos = vectorDatos;
    }

    public String generarIntermedio() {
        cuadruples.clear();
        for (int i = 0; i < 4; i++) {
            cuadruples.add(new ArrayList<>());
        }

        for (int i = 1; i < vectorDatos.length - 1; i++) {
            // System.out.println(vectorDatos[i][2]); //asi da los numero
            // System.out.println(vectorDatos[i][0]); //asi da el token
            //separar cada tipo de dato si es necesario
            if ("Int".equals(vectorDatos[i][0]) || "char".equals(vectorDatos[i][0])) {
                do {
                    GuardarSeparar.add(vectorDatos[i][0]);
                    i++;
                } while (!";".equals(vectorDatos[i][0]));
                separarD(GuardarSeparar);
            }

            //etiquetas del if
            if ("if".equals(vectorDatos[i - 1][0])) {
                GuardarIf.clear();
                do {
                    i++;
                    GuardarIf.add(vectorDatos[i][0]);
                } while (!"{".equals(vectorDatos[i + 2][0]));
                i = i + 2;
                GenerarEtiquetasIf(GuardarIf, -1);
                entrarIf = true;
                //i++;
            }

            if ("while".equals(vectorDatos[i - 1][0])) {
                GuardarIf.clear();
                do {
                    i++;
                    GuardarIf.add(vectorDatos[i][0]);
                } while (!"{".equals(vectorDatos[i + 2][0]));
                i = i + 2;
                GenerarEtiquetasWhile(GuardarIf, -1);
                entrarWhile = true;
                //i++;
            }

            if ("}".equals(vectorDatos[i][0]) && entrarWhile) {
                regresar = regresar.concat("goto " + EtiquetasWhile.get(0) + "\n");
                regresar = regresar.concat(EtiquetasWhile.get(2) + ":\n");
                entrarWhile = false;

            }

            if ("}".equals(vectorDatos[i][0]) && "else".equals(vectorDatos[i + 1][0])) {
                regresar = regresar.concat("goto " + EtiquetasIf.get(2) + "\n");
                regresar = regresar.concat(EtiquetasIf.get(1) + ":\n");
                entrarElse = true;
                entrarIf = false;
            }

            if ("}".equals(vectorDatos[i][0]) && ";".equals(vectorDatos[i + 1][0]) && entrarIf) {
                regresar = regresar.concat(EtiquetasIf.get(1) + ":\n");
            }

            //obtener los cuadruples de las operaciones
            if ("=".equals(vectorDatos[i][0])) {
                i--;
                do {
                    GuardarOpreaciones.add(vectorDatos[i][0]);
                    i++;
                } while (!";".equals(vectorDatos[i][0]));
                intermedios(GuardarOpreaciones);

                optimizar();
                for (int j = 0; j < cuadruples.get(0).size(); j++) {
                    for (int k = 0; k < cuadruples.size(); k++) {
                        if (k == 0 && j != cuadruples.get(0).size() - 1) {
                            regresar = regresar.concat(cuadruples.get(k).get(j) + " = " + " ");
                        } else {
                            regresar = regresar.concat(cuadruples.get(k).get(j) + " ");
                        }
                    }
                    regresar = regresar.concat("\n");
                }
                cuadruples.get(0).clear();
                cuadruples.get(1).clear();
                cuadruples.get(2).clear();
                cuadruples.get(3).clear();
                //i--;
            }

            if (entrarElse && ";".equals(vectorDatos[i][0]) && entrarIf == false) {
                regresar = regresar.concat(EtiquetasIf.get(2) + ":\n");
                entrarElse = false;
            }

            if ("printf".equals(vectorDatos[i][0])) {
                i--;
                do {
                    i++;
                    regresar = regresar.concat(vectorDatos[i][0]);
                } while (!";".equals(vectorDatos[i + 1][0]));
                regresar = regresar.concat("\n");
                //i++;
            } else {
                if ("scanf".equals(vectorDatos[i][0])) {
                    i--;
                    do {
                        i++;
                        regresar = regresar.concat(vectorDatos[i][0]);
                    } while (!";".equals(vectorDatos[i + 1][0]));
                    regresar = regresar.concat("\n");
                    //i++;
                }
            }

        }

        return regresar;
    }

    public void separarD(ArrayList<String> GuardarSeparar1) {
        for (int i = 0; i < GuardarSeparar1.size(); i++) {
            if (",".equals(GuardarSeparar1.get(i))) {
                regresar = regresar.concat("\n" + GuardarSeparar1.get(0) + " ");
            } else {
                regresar = regresar.concat(GuardarSeparar1.get(i) + " ");
            }

        }
        regresar = regresar.concat("\n");
    }

    public void intermedios(ArrayList<String> GuardarOpreaciones1) {
        //System.out.println(GuardarOpreaciones1);
        if (GuardarOpreaciones1.size() == 3 && entrar) {
            for (int i = 0; i < GuardarOpreaciones1.size(); i++) {
                if ("=".equals(GuardarOpreaciones1.get(i))) {
                    cuadruples.get(3).add(GuardarOpreaciones1.get(i + 1));
                    cuadruples.get(2).add(GuardarOpreaciones1.get(i));
                    cuadruples.get(1).add(GuardarOpreaciones1.get(i - 1));
                    cuadruples.get(0).add("");
                    entrar = false;
                }
            }
            entrar = true;
        } else {
            for (int i = 0; i < GuardarOpreaciones1.size(); i++) {
                if (!";".equals(GuardarOpreaciones1.get(i))) {
                    if (")".equals(GuardarOpreaciones1.get(i)) && "(".equals(GuardarOpreaciones1.get(i - 2))) {
                        GuardarOpreaciones1.remove(i);
                        GuardarOpreaciones1.remove(i - 2);
                        intermedios(GuardarOpreaciones1);
                    } else {
                        while (true) {
                            if (")".equals(GuardarOpreaciones1.get(i))) {
                                contador = i;
                                do {
                                    contador--;
                                } while (!"(".equals(GuardarOpreaciones1.get(contador)));
                                for (int j = contador; j < GuardarOpreaciones1.size(); j++) {
                                    if (!")".equals(GuardarOpreaciones1.get(j))) {
                                        if ("*".equals(GuardarOpreaciones1.get(j)) || "/".equals(GuardarOpreaciones1.get(j))) {
                                            cuadruples.get(3).add(GuardarOpreaciones1.get(j + 1));
                                            cuadruples.get(2).add(GuardarOpreaciones1.get(j));
                                            cuadruples.get(1).add(GuardarOpreaciones1.get(j - 1));
                                            contadorTemo++;
                                            TemTemporales.add("T" + contadorTemo);
                                            cuadruples.get(0).add(TemTemporales.get(contadorTemo - 1));
                                            GuardarOpreaciones1.set(j, cuadruples.get(0).get(contadorTemo - 1));
                                            GuardarOpreaciones1.remove(j + 1);
                                            GuardarOpreaciones1.remove(j - 1);
                                            intermedios(GuardarOpreaciones1);
                                        }

                                    } else {
                                        if (")".equals(GuardarOpreaciones1.get(j))) {
                                            for (int k = contador; k < GuardarOpreaciones1.size(); k++) {
                                                if (!")".equals(GuardarOpreaciones1.get(k))) {
                                                    if ("+".equals(GuardarOpreaciones1.get(k)) || "-".equals(GuardarOpreaciones1.get(k))) {
                                                        cuadruples.get(3).add(GuardarOpreaciones1.get(k + 1));
                                                        cuadruples.get(2).add(GuardarOpreaciones1.get(k));
                                                        cuadruples.get(1).add(GuardarOpreaciones1.get(k - 1));
                                                        contadorTemo++;
                                                        TemTemporales.add("T" + contadorTemo);
                                                        cuadruples.get(0).add(TemTemporales.get(contadorTemo - 1));
                                                        GuardarOpreaciones1.set(k, cuadruples.get(0).get(contadorTemo - 1));
                                                        GuardarOpreaciones1.remove(k + 1);
                                                        GuardarOpreaciones1.remove(k - 1);
                                                        intermedios(GuardarOpreaciones1);
                                                    }

                                                } else {
                                                }
                                            }
                                        }

                                    }
                                }
                            }
                            entrar = true;
                            break;
                        }
                    }
                }
            }

        }

        for (int i = 0;
                i < GuardarOpreaciones1.size();
                i++) {
            if ("(".equals(GuardarOpreaciones1.get(i)) || ")".equals(GuardarOpreaciones1.get(i))) {
                posiciones.add(i);
            }
        }

        for (int i = 0;
                i < GuardarOpreaciones1.size();
                i++) {
            if (")".equals(GuardarOpreaciones1.get(i)) && "(".equals(GuardarOpreaciones1.get(i - 2))) {
                //System.out.println("entro");
                GuardarOpreaciones1.remove(i);
                GuardarOpreaciones1.remove(i - 2);
                intermedios(GuardarOpreaciones1);
            }
        }

        if (GuardarOpreaciones1.size()
                > 3) {
            for (int i = 0; i < GuardarOpreaciones1.size(); i++) {
                if ("*".equals(GuardarOpreaciones1.get(i)) || "/".equals(GuardarOpreaciones1.get(i))) {
                    cuadruples.get(3).add(GuardarOpreaciones1.get(i + 1));
                    cuadruples.get(2).add(GuardarOpreaciones1.get(i));
                    cuadruples.get(1).add(GuardarOpreaciones1.get(i - 1));
                    contadorTemo++;
                    TemTemporales.add("T" + contadorTemo);
                    cuadruples.get(0).add(TemTemporales.get(contadorTemo - 1));
                    GuardarOpreaciones1.set(i, cuadruples.get(0).get(contadorTemo - 1));
                    GuardarOpreaciones1.remove(i + 1);
                    GuardarOpreaciones1.remove(i - 1);
                    //System.out.println(GuardarOpreaciones1);
                    intermedios(GuardarOpreaciones1);
                }
            }

            for (int i = 0; i < GuardarOpreaciones1.size(); i++) {
                if ("+".equals(GuardarOpreaciones1.get(i)) || "-".equals(GuardarOpreaciones1.get(i))) {
                    cuadruples.get(3).add(GuardarOpreaciones1.get(i + 1));
                    cuadruples.get(2).add(GuardarOpreaciones1.get(i));
                    cuadruples.get(1).add(GuardarOpreaciones1.get(i - 1));
                    contadorTemo++;
                    TemTemporales.add("T" + contadorTemo);
                    cuadruples.get(0).add(TemTemporales.get(contadorTemo - 1));
                    GuardarOpreaciones1.set(i, cuadruples.get(0).get(contadorTemo - 1));
                    GuardarOpreaciones1.remove(i + 1);
                    GuardarOpreaciones1.remove(i - 1);
                    //System.out.println(GuardarOpreaciones1);
                    intermedios(GuardarOpreaciones1);
                }
            }

        }
        TemTemporales.clear(); // aqui agregre esto
        GuardarOpreaciones.clear();
        contadorTemo = 0;

    }

    public void GenerarEtiquetasIf(ArrayList<String> GuardarIf1, int posicion) {
        if (posicion == -1) {
            EtiquetasIf.clear();
            EtiquetasNo.clear();
            EtiquetasOr.clear();
            EtiquetasAnd.clear();
            EtiquetasWhile.clear();
            etiquetas.clear();
            GuardarIf1.add(";");
            contadorEtiquetas += 10; //E verdadera
            EtiquetasIf.add("E" + contadorEtiquetas);
            contadorEtiquetas += 10; //E falsa
            EtiquetasIf.add("E" + contadorEtiquetas);
            contadorEtiquetas += 10; // E siguiente
            EtiquetasIf.add("E" + contadorEtiquetas);

            if (GuardarIf1.size() == 3 || GuardarIf1.size() == 6) {
                EtiquetasIf.clear();
                EtiquetasWhile.clear();
                EtiquetasNo.clear();
                EtiquetasOr.clear();
                EtiquetasAnd.clear();
                etiquetas.clear();
                if (GuardarIf1.size() == 3) {
                    for (int i = 0; i < GuardarIf1.size(); i++) {
                        GuardarE1.add(GuardarIf1.get(i));
                    }
                    regresar = regresar.concat("if " + GuardarE1.get(0) + " " + GuardarE1.get(1) + " " + GuardarE1.get(2));
                    regresar = regresar.concat("goto" + EtiquetasIf.get(0) + "\n");
                    regresar = regresar.concat("goto" + EtiquetasIf.get(1) + "\n");
                    regresar = regresar.concat(EtiquetasIf.get(0) + ":\n");
                    GuardarE1.clear();
                } else {
                    for (int i = 0; i < GuardarIf1.size(); i++) {
                        if ("(".equals(GuardarIf1.get(i))) {
                            EtiquetasNo.add(EtiquetasIf.get(1));
                            EtiquetasNo.add(EtiquetasIf.get(0));
                            GuardarE1.add(GuardarIf1.get(i + 1));
                            GuardarE1.add(GuardarIf1.get(i + 2));
                            GuardarE1.add(GuardarIf1.get(i + 3));
                            i = i + 3;
                        }
                    }
                    regresar = regresar.concat("if " + GuardarE1.get(0) + " " + GuardarE1.get(1) + " " + GuardarE1.get(2));
                    regresar = regresar.concat("goto" + EtiquetasNo.get(0) + "\n");
                    regresar = regresar.concat("goto" + EtiquetasNo.get(1) + "\n");
                    regresar = regresar.concat(EtiquetasIf.get(0) + ":\n");
                    GuardarE1.clear();
                }
            } else {
                posicion++;
                GenerarEtiquetasIf(GuardarIf1, posicion);
            }
        } else {
            if (posicion >= 0) {
                System.out.println(posicion);
                for (int i = posicion; i < GuardarIf1.size(); i++) {
                    if (";".equals(GuardarIf1.get(i)) && !")".equals(GuardarIf1.get(i - 1))) {
                        GuardarE1.add(GuardarIf1.get(i - 3));
                        GuardarE1.add(GuardarIf1.get(i - 2));
                        GuardarE1.add(GuardarIf1.get(i - 1));
                        regresar = regresar.concat("if " + GuardarE1.get(0) + " " + GuardarE1.get(1) + " " + GuardarE1.get(2));
                        regresar = regresar.concat("goto " + EtiquetasIf.get(0) + "\n");
                        regresar = regresar.concat("goto " + EtiquetasIf.get(1) + "\n");
                        regresar = regresar.concat(EtiquetasIf.get(0) + ":\n");
                    } else {
                        if (";".equals(GuardarIf1.get(i)) && ")".equals(GuardarIf1.get(i - 1))) {
                            GuardarE1.add(GuardarIf1.get(i - 4));
                            GuardarE1.add(GuardarIf1.get(i - 3));
                            GuardarE1.add(GuardarIf1.get(i - 2));
                            regresar = regresar.concat("if " + GuardarE1.get(0) + " " + GuardarE1.get(1) + " " + GuardarE1.get(2));
                            regresar = regresar.concat("goto " + EtiquetasIf.get(1) + "\n");
                            regresar = regresar.concat("goto " + EtiquetasIf.get(0) + "\n");
                            regresar = regresar.concat(EtiquetasIf.get(0) + ":\n");

                        } else {
                            if ("and".equals(GuardarIf1.get(i))) {
                                contadorEtiquetas += 10;
                                etiquetas.add("E" + contadorEtiquetas);
                                etiquetas.add(EtiquetasIf.get(1));
                                etiquetas.add(EtiquetasIf.get(0));
                                etiquetas.add(EtiquetasIf.get(1));

                                if (!")".equals(GuardarIf1.get(i - 1))) {
                                    GuardarE1.add(GuardarIf1.get(i - 3));
                                    GuardarE1.add(GuardarIf1.get(i - 2));
                                    GuardarE1.add(GuardarIf1.get(i - 1));
                                    regresar = regresar.concat("if " + GuardarE1.get(0) + " " + GuardarE1.get(1) + " " + GuardarE1.get(2));
                                    regresar = regresar.concat("goto " + etiquetas.get(0) + "\n");
                                    regresar = regresar.concat("goto " + etiquetas.get(1) + "\n");
                                    regresar = regresar.concat(etiquetas.get(0) + ":\n");
                                    GuardarE1.clear();
                                    etiquetas.clear();
                                    posicion = i + 1;
                                }
                                if (")".equals(GuardarIf1.get(i - 1))) {
                                    GuardarE1.add(GuardarIf1.get(i - 4));
                                    GuardarE1.add(GuardarIf1.get(i - 3));
                                    GuardarE1.add(GuardarIf1.get(i - 2));
                                    regresar = regresar.concat("if " + GuardarE1.get(0) + " " + GuardarE1.get(1) + " " + GuardarE1.get(2));
                                    regresar = regresar.concat("goto " + etiquetas.get(1) + "\n");
                                    regresar = regresar.concat("goto " + etiquetas.get(0) + "\n");
                                    regresar = regresar.concat(etiquetas.get(0) + ":\n");
                                    GuardarE1.clear();
                                    etiquetas.clear();
                                    posicion = i + 1;
                                }
                                //i++;
                            } else {
                                if ("or".equals(GuardarIf1.get(i))) {
                                    contadorEtiquetas += 10;
                                    etiquetas.add(EtiquetasIf.get(0));
                                    etiquetas.add("E" + contadorEtiquetas);
                                    etiquetas.add(EtiquetasIf.get(0));
                                    etiquetas.add(EtiquetasIf.get(1));

                                    if (!")".equals(GuardarIf1.get(i - 1))) {
                                        GuardarE1.add(GuardarIf1.get(i - 3));
                                        GuardarE1.add(GuardarIf1.get(i - 2));
                                        GuardarE1.add(GuardarIf1.get(i - 1));
                                        regresar = regresar.concat("if " + GuardarE1.get(0) + " " + GuardarE1.get(1) + " " + GuardarE1.get(2));
                                        regresar = regresar.concat("goto " + etiquetas.get(0) + "\n");
                                        regresar = regresar.concat("goto " + etiquetas.get(1) + "\n");
                                        regresar = regresar.concat(etiquetas.get(1) + ":\n");
                                        GuardarE1.clear();
                                        etiquetas.clear();
                                        posicion = i + 1;
                                    }
                                    if (")".equals(GuardarIf1.get(i - 1))) {
                                        GuardarE1.add(GuardarIf1.get(i - 4));
                                        GuardarE1.add(GuardarIf1.get(i - 3));
                                        GuardarE1.add(GuardarIf1.get(i - 2));
                                        regresar = regresar.concat("if " + GuardarE1.get(0) + " " + GuardarE1.get(1) + " " + GuardarE1.get(2));
                                        regresar = regresar.concat("goto " + etiquetas.get(1) + "\n");
                                        regresar = regresar.concat("goto " + etiquetas.get(0) + "\n");
                                        regresar = regresar.concat(etiquetas.get(1) + ":\n");
                                        GuardarE1.clear();
                                        etiquetas.clear();
                                        posicion = i + 1;
                                    }
                                }
                            }
                        }

                    }
                }

            }
        }

    }

    public void GenerarEtiquetasWhile(ArrayList<String> GuardarIf1, int posicion) {
        if (posicion == -1) {
            EtiquetasWhile.clear();
            EtiquetasNo.clear();
            EtiquetasOr.clear();
            EtiquetasAnd.clear();
            etiquetas.clear();
            EtiquetasWhile.clear();
            GuardarE1.clear();
            GuardarIf1.add(";");
            contadorEtiquetas += 10; //S comienzo
            EtiquetasWhile.add("E" + contadorEtiquetas);
            contadorEtiquetas += 10; //E verdadera
            EtiquetasWhile.add("E" + contadorEtiquetas);
            contadorEtiquetas += 10; // E false
            EtiquetasWhile.add("E" + contadorEtiquetas);
            EtiquetasWhile.add(EtiquetasWhile.get(0));// s siguiente
            regresar = regresar.concat(EtiquetasWhile.get(0) + ":\n");

            if (GuardarIf1.size() == 3 || GuardarIf1.size() == 6) {
                EtiquetasIf.clear();
                EtiquetasWhile.clear();
                EtiquetasNo.clear();
                EtiquetasOr.clear();
                EtiquetasAnd.clear();
                etiquetas.clear();
                GuardarE1.clear();
                if (GuardarIf1.size() == 3) {
                    for (int i = 0; i < GuardarIf1.size(); i++) {
                        GuardarE1.add(GuardarIf1.get(i));
                    }
                    regresar = regresar.concat("if " + GuardarE1.get(0) + " " + GuardarE1.get(1) + " " + GuardarE1.get(2));
                    regresar = regresar.concat("goto" + EtiquetasWhile.get(1) + "\n");
                    regresar = regresar.concat("goto" + EtiquetasWhile.get(2) + "\n");
                    regresar = regresar.concat(EtiquetasWhile.get(1) + ":\n");
                    GuardarE1.clear();
                } else {
                    for (int i = 0; i < GuardarIf1.size(); i++) {
                        if ("(".equals(GuardarIf1.get(i))) {
                            EtiquetasNo.add(EtiquetasWhile.get(2));
                            EtiquetasNo.add(EtiquetasWhile.get(1));
                            GuardarE1.add(GuardarIf1.get(i + 1));
                            GuardarE1.add(GuardarIf1.get(i + 2));
                            GuardarE1.add(GuardarIf1.get(i + 3));
                            i = i + 3;
                        }
                    }
                    regresar = regresar.concat("if " + GuardarE1.get(0) + " " + GuardarE1.get(1) + " " + GuardarE1.get(2));
                    regresar = regresar.concat("goto" + EtiquetasNo.get(0) + "\n");
                    regresar = regresar.concat("goto" + EtiquetasNo.get(1) + "\n");
                    regresar = regresar.concat(EtiquetasWhile.get(1) + ":\n");
                    GuardarE1.clear();
                }
            } else {
                posicion++;
                GenerarEtiquetasWhile(GuardarIf1, posicion);
            }
        } else {
            if (posicion >= 0) {
                System.out.println(posicion);
                for (int i = posicion; i < GuardarIf1.size(); i++) {
                    if (";".equals(GuardarIf1.get(i)) && !")".equals(GuardarIf1.get(i - 1))) {
                        GuardarE1.add(GuardarIf1.get(i - 3));
                        GuardarE1.add(GuardarIf1.get(i - 2));
                        GuardarE1.add(GuardarIf1.get(i - 1));
                        regresar = regresar.concat("if " + GuardarE1.get(0) + " " + GuardarE1.get(1) + " " + GuardarE1.get(2));
                        regresar = regresar.concat("goto " + EtiquetasWhile.get(1) + "\n");
                        regresar = regresar.concat("goto " + EtiquetasWhile.get(2) + "\n");
                        regresar = regresar.concat(EtiquetasWhile.get(1) + ":\n");
                        GuardarE1.clear();
                    } else {
                        if (";".equals(GuardarIf1.get(i)) && ")".equals(GuardarIf1.get(i - 1))) {
                            GuardarE1.add(GuardarIf1.get(i - 4));
                            GuardarE1.add(GuardarIf1.get(i - 3));
                            GuardarE1.add(GuardarIf1.get(i - 2));
                            regresar = regresar.concat("if " + GuardarE1.get(0) + " " + GuardarE1.get(1) + " " + GuardarE1.get(2));
                            regresar = regresar.concat("goto " + EtiquetasWhile.get(2) + "\n");
                            regresar = regresar.concat("goto " + EtiquetasWhile.get(1) + "\n");
                            regresar = regresar.concat(EtiquetasWhile.get(1) + ":\n");
                            GuardarE1.clear();

                        } else {
                            if ("and".equals(GuardarIf1.get(i))) {
                                contadorEtiquetas += 10;
                                etiquetas.add("E" + contadorEtiquetas);
                                etiquetas.add(EtiquetasWhile.get(2));
                                etiquetas.add(EtiquetasWhile.get(1));
                                etiquetas.add(EtiquetasWhile.get(2));

                                if (!")".equals(GuardarIf1.get(i - 1))) {
                                    GuardarE1.add(GuardarIf1.get(i - 3));
                                    GuardarE1.add(GuardarIf1.get(i - 2));
                                    GuardarE1.add(GuardarIf1.get(i - 1));
                                    regresar = regresar.concat("if " + GuardarE1.get(0) + " " + GuardarE1.get(1) + " " + GuardarE1.get(2));
                                    regresar = regresar.concat("goto " + etiquetas.get(0) + "\n");
                                    regresar = regresar.concat("goto " + etiquetas.get(1) + "\n");
                                    regresar = regresar.concat(etiquetas.get(0) + ":\n");
                                    GuardarE1.clear();
                                    etiquetas.clear();
                                    posicion = i + 1;
                                }
                                if (")".equals(GuardarIf1.get(i - 1))) {
                                    GuardarE1.add(GuardarIf1.get(i - 4));
                                    GuardarE1.add(GuardarIf1.get(i - 3));
                                    GuardarE1.add(GuardarIf1.get(i - 2));
                                    regresar = regresar.concat("if " + GuardarE1.get(0) + " " + GuardarE1.get(1) + " " + GuardarE1.get(2));
                                    regresar = regresar.concat("goto " + etiquetas.get(1) + "\n");
                                    regresar = regresar.concat("goto " + etiquetas.get(0) + "\n");
                                    regresar = regresar.concat(etiquetas.get(0) + ":\n");
                                    GuardarE1.clear();
                                    etiquetas.clear();
                                    posicion = i + 1;
                                }
                                //i++;
                            } else {
                                if ("or".equals(GuardarIf1.get(i))) {
                                    contadorEtiquetas += 10;
                                    etiquetas.add(EtiquetasWhile.get(1));
                                    etiquetas.add("E" + contadorEtiquetas);
                                    etiquetas.add(EtiquetasWhile.get(1));
                                    etiquetas.add(EtiquetasWhile.get(2));

                                    if (!")".equals(GuardarIf1.get(i - 1))) {
                                        GuardarE1.add(GuardarIf1.get(i - 3));
                                        GuardarE1.add(GuardarIf1.get(i - 2));
                                        GuardarE1.add(GuardarIf1.get(i - 1));
                                        regresar = regresar.concat("if " + GuardarE1.get(0) + " " + GuardarE1.get(1) + " " + GuardarE1.get(2));
                                        regresar = regresar.concat("goto " + etiquetas.get(0) + "\n");
                                        regresar = regresar.concat("goto " + etiquetas.get(1) + "\n");
                                        regresar = regresar.concat(etiquetas.get(1) + ":\n");
                                        GuardarE1.clear();
                                        etiquetas.clear();
                                        posicion = i + 1;
                                    }
                                    if (")".equals(GuardarIf1.get(i - 1))) {
                                        GuardarE1.add(GuardarIf1.get(i - 4));
                                        GuardarE1.add(GuardarIf1.get(i - 3));
                                        GuardarE1.add(GuardarIf1.get(i - 2));
                                        regresar = regresar.concat("if " + GuardarE1.get(0) + " " + GuardarE1.get(1) + " " + GuardarE1.get(2));
                                        regresar = regresar.concat("goto " + etiquetas.get(1) + "\n");
                                        regresar = regresar.concat("goto " + etiquetas.get(0) + "\n");
                                        regresar = regresar.concat(etiquetas.get(1) + ":\n");
                                        GuardarE1.clear();
                                        etiquetas.clear();
                                        posicion = i + 1;
                                    }
                                }
                            }
                        }

                    }
                }

            }
        }

    }

    public void optimizar() {
        if (cuadruples.get(0).size() <= 3) {
            for (int j = 0; j < cuadruples.get(0).size(); j++) {
                for (int k = 0; k < cuadruples.size(); k++) {
                    if (k == 0 && j != cuadruples.get(0).size() - 1) {
                        regresar = regresar.concat(cuadruples.get(k).get(j) + " = " + " ");
                    } else {
                        regresar = regresar.concat(cuadruples.get(k).get(j) + " ");
                    }
                }
                regresar = regresar.concat("\n");
            }
        } else {
            System.out.println(cuadruples);
            optimizar2(0);
        }
    }

    public void optimizar2(int posicion) {
        //        System.out.print(cuadruples.get(0).get(0)); //me da la etiquea principal
        //        System.out.print(cuadruples.get(1).get(0)); //me da lado izquierdo
        //        System.out.print(cuadruples.get(2).get(0)); // me da el operador +
        //        System.out.print(cuadruples.get(3).get(0)); // me da lado derecho

        for (int i = posicion; i < cuadruples.get(0).size(); i++) {
            for (int j = 0; j < cuadruples.size(); j++) {
                //empieza el mas
                if ("+".equals(cuadruples.get(j).get(i))) {
                    try {
                        numero1 = Integer.parseInt(cuadruples.get(j - 1).get(i));
                        numero2 = Integer.parseInt(cuadruples.get(j + 1).get(i));
                        etiqueta1 = cuadruples.get(j - 2).get(i); //etiqueta a buscar para remplazar
                        int operacion = numero1 + numero2;
                        etiqueta2 = String.valueOf(operacion); // etiqueta remplzar
                        cuadruples.get(j - 2).remove(i); //obtengo la posicion de la etiqueta a eliminar
                        cuadruples.get(j - 1).remove(i); //obtengo la posicion de la etiqueta a eliminar
                        cuadruples.get(j).remove(i); //obtengo la posicion de la etiqueta a eliminar
                        cuadruples.get(j + 1).remove(i); //obtengo la posicion de la etiqueta a eliminar
                        eliminar();
                        optimizar2(0);
                    } catch (NumberFormatException e) {
                    }
                    if ("0".equals(cuadruples.get(j - 1).get(i))) {
                        etiqueta1 = cuadruples.get(j - 2).get(i);
                        etiqueta2 = cuadruples.get(j + 1).get(i);
                        cuadruples.get(j - 2).remove(i); //obtengo la posicion de la etiqueta a eliminar
                        cuadruples.get(j - 1).remove(i); //obtengo la posicion de la etiqueta a eliminar
                        cuadruples.get(j).remove(i); //obtengo la posicion de la etiqueta a eliminar
                        cuadruples.get(j + 1).remove(i); //obtengo la posicion de la etiqueta a eliminar
                        eliminar();
                        optimizar2(0);
                    } else {
                        if ("0".equals(cuadruples.get(j + 1).get(i))) {
                            etiqueta1 = cuadruples.get(j - 2).get(i);
                            etiqueta2 = cuadruples.get(j - 1).get(i);
                            cuadruples.get(j - 2).remove(i); //obtengo la posicion de la etiqueta a eliminar
                            cuadruples.get(j - 1).remove(i); //obtengo la posicion de la etiqueta a eliminar
                            cuadruples.get(j).remove(i); //obtengo la posicion de la etiqueta a eliminar
                            cuadruples.get(j + 1).remove(i); //obtengo la posicion de la etiqueta a eliminar
                            eliminar();
                            optimizar2(0);
                        } else {
                            etiqueta2 = cuadruples.get(j - 2).get(i);
                            guardarOpreacion.add(cuadruples.get(j - 1).get(i));
                            guardarOpreacion.add(cuadruples.get(j).get(i));
                            guardarOpreacion.add(cuadruples.get(j + 1).get(i));

                            guardarOpreacion2.add(cuadruples.get(j + 1).get(i));
                            guardarOpreacion2.add(cuadruples.get(j).get(i));
                            guardarOpreacion2.add(cuadruples.get(j - 1).get(i));
                            eliminar2(i + 1);
                            guardarOpreacion.clear();
                            guardarOpreacion2.clear();
                        }
                    }

                }//aqui va el else jajaja

                //termina el mas y empieza la multipliacion
                if ("*".equals(cuadruples.get(j).get(i))) {
                    try {
                        numero1 = Integer.parseInt(cuadruples.get(j - 1).get(i));
                        numero2 = Integer.parseInt(cuadruples.get(j + 1).get(i));
                        etiqueta1 = cuadruples.get(j - 2).get(i); //etiqueta a buscar para remplazar
                        int operacion = numero1 * numero2;
                        etiqueta2 = String.valueOf(operacion); // etiqueta remplzar
                        cuadruples.get(j - 2).remove(i); //obtengo la posicion de la etiqueta a eliminar
                        cuadruples.get(j - 1).remove(i); //obtengo la posicion de la etiqueta a eliminar
                        cuadruples.get(j).remove(i); //obtengo la posicion de la etiqueta a eliminar
                        cuadruples.get(j + 1).remove(i); //obtengo la posicion de la etiqueta a eliminar
                        eliminar();
                        optimizar2(0);
                    } catch (NumberFormatException e) {
                    }
                    if ("0".equals(cuadruples.get(j - 1).get(i))) {
                        etiqueta1 = cuadruples.get(j - 2).get(i);
                        etiqueta2 = "0";
                        cuadruples.get(j - 2).remove(i); //obtengo la posicion de la etiqueta a eliminar
                        cuadruples.get(j - 1).remove(i); //obtengo la posicion de la etiqueta a eliminar
                        cuadruples.get(j).remove(i); //obtengo la posicion de la etiqueta a eliminar
                        cuadruples.get(j + 1).remove(i); //obtengo la posicion de la etiqueta a eliminar
                        eliminar();
                        optimizar2(0);
                    } else {
                        if ("0".equals(cuadruples.get(j + 1).get(i))) {
                            etiqueta1 = cuadruples.get(j - 2).get(i);
                            etiqueta2 = "0";
                            cuadruples.get(j - 2).remove(i); //obtengo la posicion de la etiqueta a eliminar
                            cuadruples.get(j - 1).remove(i); //obtengo la posicion de la etiqueta a eliminar
                            cuadruples.get(j).remove(i); //obtengo la posicion de la etiqueta a eliminar
                            cuadruples.get(j + 1).remove(i); //obtengo la posicion de la etiqueta a eliminar
                            eliminar();
                            optimizar2(0);
                        } else {
                            if ("1".equals(cuadruples.get(j - 1).get(i))) {
                                etiqueta1 = cuadruples.get(j - 2).get(i);
                                etiqueta2 = cuadruples.get(j + 1).get(i);
                                cuadruples.get(j - 2).remove(i); //obtengo la posicion de la etiqueta a eliminar
                                cuadruples.get(j - 1).remove(i); //obtengo la posicion de la etiqueta a eliminar
                                cuadruples.get(j).remove(i); //obtengo la posicion de la etiqueta a eliminar
                                cuadruples.get(j + 1).remove(i); //obtengo la posicion de la etiqueta a eliminar
                                eliminar();
                                optimizar2(0);
                            } else {
                                if ("1".equals(cuadruples.get(j + 1).get(i))) {
                                    etiqueta1 = cuadruples.get(j - 2).get(i);
                                    etiqueta2 = cuadruples.get(j - 1).get(i);
                                    cuadruples.get(j - 2).remove(i); //obtengo la posicion de la etiqueta a eliminar
                                    cuadruples.get(j - 1).remove(i); //obtengo la posicion de la etiqueta a eliminar
                                    cuadruples.get(j).remove(i); //obtengo la posicion de la etiqueta a eliminar
                                    cuadruples.get(j + 1).remove(i); //obtengo la posicion de la etiqueta a eliminar
                                    eliminar();
                                    optimizar2(0);
                                } else {
                                    etiqueta2 = cuadruples.get(j - 2).get(i);
                                    guardarOpreacion.add(cuadruples.get(j - 1).get(i));
                                    guardarOpreacion.add(cuadruples.get(j).get(i));
                                    guardarOpreacion.add(cuadruples.get(j + 1).get(i));

                                    guardarOpreacion2.add(cuadruples.get(j + 1).get(i));
                                    guardarOpreacion2.add(cuadruples.get(j).get(i));
                                    guardarOpreacion2.add(cuadruples.get(j - 1).get(i));
                                    eliminar2(i + 1);
                                    guardarOpreacion.clear();
                                    guardarOpreacion2.clear();
                                }
                            }
                        }
                    }

                }//aqui va el else jajaja

                //termina  la multipliacion y empieza la division
                if ("/".equals(cuadruples.get(j).get(i))) {
                    if ("0".equals(cuadruples.get(j - 1).get(i))) {
                        etiqueta1 = cuadruples.get(j - 2).get(i);
                        etiqueta2 = "0";
                        cuadruples.get(j - 2).remove(i); //obtengo la posicion de la etiqueta a eliminar
                        cuadruples.get(j - 1).remove(i); //obtengo la posicion de la etiqueta a eliminar
                        cuadruples.get(j).remove(i); //obtengo la posicion de la etiqueta a eliminar
                        cuadruples.get(j + 1).remove(i); //obtengo la posicion de la etiqueta a eliminar
                        eliminar();
                        optimizar2(0);
                    } else {
                        if ("0".equals(cuadruples.get(j + 1).get(i))) {
                            etiqueta1 = cuadruples.get(j - 2).get(i);
                            etiqueta2 = "0";
                            cuadruples.get(j - 2).remove(i); //obtengo la posicion de la etiqueta a eliminar
                            cuadruples.get(j - 1).remove(i); //obtengo la posicion de la etiqueta a eliminar
                            cuadruples.get(j).remove(i); //obtengo la posicion de la etiqueta a eliminar
                            cuadruples.get(j + 1).remove(i); //obtengo la posicion de la etiqueta a eliminar
                            eliminar();
                            optimizar2(0);
                        } else {
                            if ("1".equals(cuadruples.get(j + 1).get(i))) {
                                etiqueta1 = cuadruples.get(j - 2).get(i);
                                etiqueta2 = cuadruples.get(j - 1).get(i);
                                cuadruples.get(j - 2).remove(i); //obtengo la posicion de la etiqueta a eliminar
                                cuadruples.get(j - 1).remove(i); //obtengo la posicion de la etiqueta a eliminar
                                cuadruples.get(j).remove(i); //obtengo la posicion de la etiqueta a eliminar
                                cuadruples.get(j + 1).remove(i); //obtengo la posicion de la etiqueta a eliminar
                                eliminar();
                                optimizar2(0);
                            } else {
                                if (cuadruples.get(j - 1).get(i).equals(cuadruples.get(j + 1).get(i))) {
                                    etiqueta1 = cuadruples.get(j - 2).get(i);
                                    etiqueta2 = "1";
                                    cuadruples.get(j - 2).remove(i); //obtengo la posicion de la etiqueta a eliminar
                                    cuadruples.get(j - 1).remove(i); //obtengo la posicion de la etiqueta a eliminar
                                    cuadruples.get(j).remove(i); //obtengo la posicion de la etiqueta a eliminar
                                    cuadruples.get(j + 1).remove(i); //obtengo la posicion de la etiqueta a eliminar
                                    eliminar();
                                    optimizar2(0);
                                } else {
                                    etiqueta2 = cuadruples.get(j - 2).get(i);
                                    guardarOpreacion.add(cuadruples.get(j - 1).get(i));
                                    guardarOpreacion.add(cuadruples.get(j).get(i));
                                    guardarOpreacion.add(cuadruples.get(j + 1).get(i));
                                    eliminar2(i + 1);
                                    guardarOpreacion.clear();
                                }
                            }
                        }
                    }

                }//aqui va el else jajaja

                //termina  la  division y empieza la resta
                if ("-".equals(cuadruples.get(j).get(i))) {
                    try {
                        numero1 = Integer.parseInt(cuadruples.get(j - 1).get(i));
                        numero2 = Integer.parseInt(cuadruples.get(j + 1).get(i));
                        int operacion = numero1 - numero2;
                        if (operacion > 0) {
                            etiqueta1 = cuadruples.get(j - 2).get(i); //etiqueta a buscar para remplazar
                            etiqueta2 = String.valueOf(operacion); // etiqueta remplzar
                            cuadruples.get(j - 2).remove(i); //obtengo la posicion de la etiqueta a eliminar
                            cuadruples.get(j - 1).remove(i); //obtengo la posicion de la etiqueta a eliminar
                            cuadruples.get(j).remove(i); //obtengo la posicion de la etiqueta a eliminar
                            cuadruples.get(j + 1).remove(i); //obtengo la posicion de la etiqueta a eliminar
                            eliminar();
                            optimizar2(0);
                        }
                    } catch (NumberFormatException e) {
                    }
                    if ("0".equals(cuadruples.get(j + 1).get(i))) {
                        etiqueta1 = cuadruples.get(j - 2).get(i);
                        etiqueta2 = cuadruples.get(j - 1).get(i);
                        cuadruples.get(j - 2).remove(i); //obtengo la posicion de la etiqueta a eliminar
                        cuadruples.get(j - 1).remove(i); //obtengo la posicion de la etiqueta a eliminar
                        cuadruples.get(j).remove(i); //obtengo la posicion de la etiqueta a eliminar
                        cuadruples.get(j + 1).remove(i); //obtengo la posicion de la etiqueta a eliminar
                        eliminar();
                        optimizar2(0);
                    } else {

                        if (cuadruples.get(j - 1).get(i).equals(cuadruples.get(j + 1).get(i))) {
                            etiqueta1 = cuadruples.get(j - 2).get(i);
                            etiqueta2 = "1";
                            cuadruples.get(j - 2).remove(i); //obtengo la posicion de la etiqueta a eliminar
                            cuadruples.get(j - 1).remove(i); //obtengo la posicion de la etiqueta a eliminar
                            cuadruples.get(j).remove(i); //obtengo la posicion de la etiqueta a eliminar
                            cuadruples.get(j + 1).remove(i); //obtengo la posicion de la etiqueta a eliminar
                            eliminar();
                        } else {
                            etiqueta2 = cuadruples.get(j - 2).get(i);
                            guardarOpreacion.add(cuadruples.get(j - 1).get(i));
                            guardarOpreacion.add(cuadruples.get(j).get(i));
                            guardarOpreacion.add(cuadruples.get(j + 1).get(i));
                            eliminar2(i + 1);
                            guardarOpreacion.clear();
                        }
                    }
                }//aqui va el else jajaja
            }
        }
    }//termina el proceso 

    public void eliminar() {
        for (int i = 0; i < cuadruples.get(0).size(); i++) {
            for (int j = 0; j < cuadruples.size(); j++) {
                if (etiqueta1.equals(cuadruples.get(j).get(i))) {
                    cuadruples.get(j).set(i, etiqueta2);
                }
            }
        }
    }

    public void eliminar2(int inicar) {
        if ("+".equals(guardarOpreacion.get(1))) {
            for (int i = inicar; i < cuadruples.get(0).size(); i++) {
                for (int j = 0; j < cuadruples.size(); j++) {
                    if (guardarOpreacion.get(2).equals(cuadruples.get(j).get(i))) {
                        if (guardarOpreacion.get(1).equals(cuadruples.get(j - 1).get(i))) {
                            if (guardarOpreacion.get(0).equals(cuadruples.get(j - 2).get(i))) {
                                etiqueta1 = cuadruples.get(j - 3).get(i);
                                cuadruples.get(j).remove(i);
                                cuadruples.get(j - 1).remove(i);
                                cuadruples.get(j - 2).remove(i);
                                cuadruples.get(j - 3).remove(i);
                                eliminar();
                            }
                        }
                    } else {
                        if (guardarOpreacion2.get(2).equals(cuadruples.get(j).get(i))) {
                            if (guardarOpreacion2.get(1).equals(cuadruples.get(j - 1).get(i))) {
                                if (guardarOpreacion2.get(0).equals(cuadruples.get(j - 2).get(i))) {
                                    etiqueta1 = cuadruples.get(j - 3).get(i);
                                    cuadruples.get(j).remove(i);
                                    cuadruples.get(j - 1).remove(i);
                                    cuadruples.get(j - 2).remove(i);
                                    cuadruples.get(j - 3).remove(i);
                                    eliminar();
                                }
                            }
                        }
                    }
                }
            }

        }//aqui va un else
        if ("*".equals(guardarOpreacion.get(1))) {
            for (int i = inicar; i < cuadruples.get(0).size(); i++) {
                for (int j = 0; j < cuadruples.size(); j++) {
                    if (guardarOpreacion.get(2).equals(cuadruples.get(j).get(i))) {
                        if (guardarOpreacion.get(1).equals(cuadruples.get(j - 1).get(i))) {
                            if (guardarOpreacion.get(0).equals(cuadruples.get(j - 2).get(i))) {
                                etiqueta1 = cuadruples.get(j - 3).get(i);
                                cuadruples.get(j).remove(i);
                                cuadruples.get(j - 1).remove(i);
                                cuadruples.get(j - 2).remove(i);
                                cuadruples.get(j - 3).remove(i);
                                eliminar();
                            }
                        }
                    } else {
                        if (guardarOpreacion2.get(2).equals(cuadruples.get(j).get(i))) {
                            if (guardarOpreacion2.get(1).equals(cuadruples.get(j - 1).get(i))) {
                                if (guardarOpreacion2.get(0).equals(cuadruples.get(j - 2).get(i))) {
                                    etiqueta1 = cuadruples.get(j - 3).get(i);
                                    cuadruples.get(j).remove(i);
                                    cuadruples.get(j - 1).remove(i);
                                    cuadruples.get(j - 2).remove(i);
                                    cuadruples.get(j - 3).remove(i);
                                    eliminar();
                                }
                            }
                        }
                    }
                }
            }

        }//aqui va un else

        if ("/".equals(guardarOpreacion.get(1))) {
            for (int i = inicar; i < cuadruples.get(0).size(); i++) {
                for (int j = 0; j < cuadruples.size(); j++) {
                    if (guardarOpreacion.get(2).equals(cuadruples.get(j).get(i))) {
                        if (guardarOpreacion.get(1).equals(cuadruples.get(j - 1).get(i))) {
                            if (guardarOpreacion.get(0).equals(cuadruples.get(j - 2).get(i))) {
                                etiqueta1 = cuadruples.get(j - 3).get(i);
                                cuadruples.get(j).remove(i);
                                cuadruples.get(j - 1).remove(i);
                                cuadruples.get(j - 2).remove(i);
                                cuadruples.get(j - 3).remove(i);
                                eliminar();
                            }
                        }
                    }
                }
            }

        }//aqui va un else
        
        if ("-".equals(guardarOpreacion.get(1))) {
            for (int i = inicar; i < cuadruples.get(0).size(); i++) {
                for (int j = 0; j < cuadruples.size(); j++) {
                    if (guardarOpreacion.get(2).equals(cuadruples.get(j).get(i))) {
                        if (guardarOpreacion.get(1).equals(cuadruples.get(j - 1).get(i))) {
                            if (guardarOpreacion.get(0).equals(cuadruples.get(j - 2).get(i))) {
                                etiqueta1 = cuadruples.get(j - 3).get(i);
                                cuadruples.get(j).remove(i);
                                cuadruples.get(j - 1).remove(i);
                                cuadruples.get(j - 2).remove(i);
                                cuadruples.get(j - 3).remove(i);
                                eliminar();
                            }
                        }
                    }
                }
            }

        }//aqui va un else
    }

}
