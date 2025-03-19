/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compiladores;

/**
 *
 * @author Eduardo Gallegos
 */
public class Lexico {

    String lexema;
    String nombre;
    int numero;
    //se usa la tabla de tokens fijos
     String diccionario[][] = {
        {"Int", "tipo de dato", "1"},
        {"char", "tipo de dato", "1"},
        {",", "coma", "2"},
        {"=", "igual", "3"},
        {";", "Punto y coma", "4"},
        {"+", "Suma", "5"},
        {"-", "Resta", "6"},
        {"*", "Multiplicacion", "7"},
        {"/", "Division", "8"},
        {"scanf", "lectura", "9"},
        {"(", "Parentesis abre", "10"},
        {")", "Parentesis cierra", "11"},
        {"printf", "Imprimir", "12"},
        {"if", "if", "13"},
        {"else", "else", "14"},
        {"<", "Operador relacional", "15"},
        {">", "Operador relacional", "15"},
        {"<=", "Operador relacional", "15"},
        {">=", "Operador relacional", "15"},
        {"!=", "Operador relacional", "15"},
        {"==", "Operador relacional", "15"},
        {"!", "Negacion", "16"},
        {"no", "Negacion", "16"},
        {"and", "Operador logico", "17"},
        {"or", "Operador logico", "17"},
        {"&&", "Operador logico", "17"},
        {"while", "while", "18"},        
        {"{", "Llave que abre", "19"},
        {"}", "Llave que cierra", "20"},
        {"--", "decremento", "21"},
        {"++", "incremento", "22"},
        {"\"", "Comillas", "23"},
        {"while", "while", "24"} ,
        {"*,/", "operador aritmetico", "25"}
        
        

    };


    public Lexico etiquetar(String palabra) {
        Lexico objLex = new Lexico();
        objLex.lexema = palabra;
        int i = 0;
        boolean bandera = false;
        while (i < objLex.diccionario.length) {
            if (palabra.equals(diccionario[i][0])) {
                bandera = true;
                objLex.nombre = diccionario[i][1];
                objLex.numero = Integer.parseInt(diccionario[i][2]);
                break;
            }
            i++;
        }
        if (bandera) {
            return objLex;
        }
        //se agrega el automata 
        int matTra[][] = {
            {1, 2, 3,6,-1},
            {1, 1, -1,-1,-1},
            {-1, 2, -1,-1,-1},
            {4, 4, -1, -1,-1},            
            {-1, -1, 5,-1,-1},
            {-1, -1, -1,-1,-1},
            {7,7,-1,-1,-1}, 
            {7,7,-1,8,7}, 
            {-1,-1,-1,-1,-1}
            
    }
    ;
        String vecNom[]={"Caracter desconocido","Variable","Entero","Caracter no valido",
                        "Caracter no valido","Caracter","Mensaje no valido","Mensaje no valido","Mensaje"};
        int vecNum[]={104,51,50,103,103,52,102,102,53};
        String vecErrores[]={"Carácter desconocido","Variable no valida","Entero no valido","Carácter no valido","Carácter no valido","Carácter no valido"
                            ,"Mensaje no valido","Mensaje no valido","Mensaje no valido"};
        int vecNumError[]={104,101,100,103,103,103,102,102,102};
        int edo=0,pos=0;
        boolean ban2=true;
        char vec[]=palabra.toCharArray();
        for (int j = 0; j <vec.length; j++) {
            if(Character.isLetter(vec[j])){
                pos=0;
            }else{
                if(Character.isDigit(vec[j])){
                    pos=1;
                }else{
                    if(vec[j]=='\''){
                        pos=2;
                    }else{
                        if(vec[j]=='%'){
                            pos=3;
                        }else{
                            if(vec[j]=='_'){
                            pos=4;
                            
                            }else{
                              ban2=false;
                            break;      
                             }                       
                      
                        }
                    
                }
            }
            }
            if(matTra[edo][pos]!=-1){
                edo=matTra[edo][pos];
            }else{
                ban2=false;
                break;
            }
        }
        if(ban2){
            objLex.nombre=vecNom[edo];
            objLex.numero=vecNum[edo];
        }else{
            objLex.nombre=vecErrores[edo];
            objLex.numero=vecNumError[edo];
        }
        
        //objLex.nombre  = "Pendiente";
   // objLex.numero  = 0;
    return objLex ;

    //se agrega el automata //se agregan los estados en caso de que no interactue se le pone -1
    //se usa la tabla de transiciones
}
}