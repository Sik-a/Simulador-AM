/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programacionfinal;

import java.io.*;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Esta clase simula el funcionamiento de un cajero automático para el trabajo
 * final de la materia programación procedimental.
 * @author Andrés Sicachá y Viviana Ríos
 * @version 1.0
 */
public class ProgramacionFinal {

    // matrices globales
    static int[] tarjetas = new int[30];
    static int[] cuentas = new int[30];
    static float[] saldos = new float[30];
    static String[] tipoCuenta = new String[30];
    static int[] contrasenas = new int[30];
    static String linea = null, datos;
    static int pos = 0, posTransfer = 0, tamano = 0, nuevaPsw, saldoCajero, total50, total20, total10;
    static final String nomArchivo = "usuarios.txt", nomArchivoCajero = "saldocajero.txt";

    /**
     * Método principal del programa para la ejecución del cajero automático.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Declarando variables
        int op, tarjeta;
        datosUsuario();
        datosCajero();

        // Habilitando el teclado
        Scanner teclado = new Scanner(System.in);

        // Pedimos número tarjeta al usuario
        System.out.println("Inserte su tarjeta");
        tarjeta = teclado.nextInt();

        // Condicional con el menú principal del cajero
        if (buscarTarjeta(tarjeta)) {
            // Habilitamos el menú
            System.out.println("\n ** MENÚ PRINCIPAL **");
            System.out.println("¿Qué desea realizar?"
                    + "\n1. Retiro en cuenta"
                    + "\n2. Saldo en cuenta"
                    + "\n3. Transferencia"
                    + "\n4. Cambio de clave");
            op = teclado.nextInt();

            // Condicional del menú principal
            if (op >= 1 || op <= 4) {
                switch (op) {
                    case 1:
                        // Llamamos el método retiro
                        retiro();
                        break;
                    case 2:
                        // Llamamos el método saldo
                        saldo();
                        break;
                    case 3:
                        // Transferencia
                        transferencia();
                        break;
                    case 4:
                        // Llamamos el método cambioContrasena
                        cambioContrasena();
                        break;
                    default:
                        System.out.println("Opción inválida");
                        break;
                } // Fin switch op

                if (guardarDatosUsuario() && guardarDatosCajero()) {
                    System.out.println("GRACIAS POR UTILIZAR NUESTROS SERVICIOS");
                    System.out.println("POR FAVOR, RETIRE SU TARJETA");
                } else {
                    System.out.println("ESTAMOS EXPERIMENTANDO FALLAS TÉCNICAS");
                    System.out.println("INTENTELO DE NUEVO MÁS TARDE");
                }

            } // Fin if
        } else {
            System.out.println("Error al leer la tarjeta");
        } // Fin if-else
    } // Fin main

    /**
     * Método que permite la carga de información del archivo usuarios.txt a los
     * vectores declarados e inicializados de forma global.
     */
    public static void datosUsuario() {
        Scanner in = null, line = null;

        // Leemos el archivo txt (cargando datos)
        try {
            in = new Scanner(new FileReader(nomArchivo));
            in.useLocale(Locale.ENGLISH);
            in.useDelimiter("\t");

            while (in.hasNextLine()) {
                line = new Scanner(in.nextLine());
                line.useLocale(Locale.ENGLISH);
                in.useDelimiter("\t");
                tarjetas[pos] = line.nextInt();
                cuentas[pos] = line.nextInt();
                saldos[pos] = line.nextFloat();
                tipoCuenta[pos] = line.next();
                contrasenas[pos] = line.nextInt();
                pos++;
            } // Fin while
            tamano = pos;
        } // Fin try
        catch (Exception e) {
            System.out.println("Error en el archivo: " + e);
        } // Fin catch
    } // Fin método datosUsuario
    
    /**
     * Método que permite la carga de información del archivo usuarios.txt a los
     * vectores declarados e inicializados de forma global.
     */
    public static void datosCajero() {
        Scanner in = null, line = null;

        // Leemos el archivo txt (cargando datos)
        try {
            in = new Scanner(new FileReader(nomArchivoCajero));
            in.useLocale(Locale.ENGLISH);
            in.useDelimiter("\t");

            while (in.hasNextLine()) {
                line = new Scanner(in.nextLine());
                line.useLocale(Locale.ENGLISH);
                in.useDelimiter("\t");
                saldoCajero = line.nextInt();
                total50 = line.nextInt();
                total20 = line.nextInt();
                total10 = line.nextInt();
            } // Fin while
        } // Fin try
        catch (Exception e) {
            System.out.println("Error en el archivo: " + e);
        } // Fin catch
    } // Fin método datosCajero

    /**
     * Método que permite buscar la tarjeta del cliente en el archivo
     * usuarios.txt.
     * @param tarjeta
     * @return - Retorna un true en caso de coincidir la tarjeta ingresada con
     * la existente en el archivo txt.
     */
    public static boolean buscarTarjeta(int tarjeta) {
        // Declarando variables
        boolean tarEncontrada = false;

        // Ciclo y condicional de comparación
        for (int i = 0; i < tamano; i++) {
            if (tarjeta == tarjetas[i]) {
                tarEncontrada = true;
                pos = i;
            } // Fin if
        } // Fin for

        return tarEncontrada;
    } // Fin método buscar
    
    /**
     * Método que permite buscar una cuenta del cliente en el archivo
     * usuarios.txt.
     * @param cuenta
     * @return - Retorna un true al encontrar la cuenta buscada en el archivo txt.
     */
    public static boolean buscarCuenta(int cuenta) {
        // Declarando variables
        boolean cuentaEncontrada = false;

        // Ciclo y condicional de comparación
        for (int i = 0; i < tamano; i++) {
            if (cuenta == cuentas[i]) {
                cuentaEncontrada = true;
                posTransfer = i;
            } // Fin if
        } // Fin for

        return cuentaEncontrada;
    } // Fin método buscar

    /**
     * Método que permite validar la contraseña ingresada con las existentes en
     * el archivo usuarios.txt.
     *
     * @return - Retorna un true si la contraseña ingresada coincide con la
     * existente en el archivo txt.
     */
    public static boolean validarContrasena() {
        // Habilitamos el teclado
        Scanner teclado = new Scanner(System.in);

        // Variables
        int pass;
        boolean validPass = false;

        System.out.println("Ingrese su contraseña");
        pass = teclado.nextInt();

        if (pass == contrasenas[pos]) {
            validPass = true;
        } // Fin if

        return validPass;
    } // Fin método validarContrasena

    /**
     * Método que permite la consulta de saldo del cliente.
     */
    public static void saldo() {
        // Condicional para imprimir información
        if (validarContrasena()) {
            System.out.println("*** REGISTRO DE OPERACIÓN ***"
                    + "\n TARJETA N.º " + tarjetas[pos]
                    + "\n"
                    + "\n TIPO DE OPERACIÓN CONSULTA DE " + tipoCuenta[pos] + " N.º " + cuentas[pos]
                    + "\n DISPONIBLE " + saldos[pos]
                    + "\n COSTO OPERACIÓN $0.00");
        } else {
            System.out.println("Contraseña incorrecta");
        } // Fin if-else
    } // Fin método saldo

    /**
     * Método que pide al cliente digitar y validar si es exacta la nueva
     * contraseña ingresada por el cliente.
     *
     * @return - Retorna un true si la nueva contraseña coinciden.
     */
    public static boolean nuevaContrasena() {
        // Habilitamos el teclado
        Scanner teclado = new Scanner(System.in);

        // Variables
        int nuevaPsw2;
        boolean newPsw = false;

        System.out.println("Ingrese la nueva clave");
        nuevaPsw = teclado.nextInt();
        System.out.println("Confirme la nueva clave");
        nuevaPsw2 = teclado.nextInt();

        if (nuevaPsw == nuevaPsw2) {
            newPsw = true;
        } // Fin if

        return newPsw;
    } // Fin método nuevaContrasena

    /**
     * Método que permite realizar el cambio de contraseña en la cuenta del cliente.
     */
    public static void cambioContrasena() {
        if (validarContrasena()) {
            if (nuevaContrasena()) {
                contrasenas[pos] = nuevaPsw;
            } else {
                System.out.println("LAS CONTRASEÑAS NO COINCIDEN");
            } // Fin if-else
        } else {
            System.out.println("CONTRASEÑA INCORRECTA");
        } // Fin if-else
    } // Fin módulo clave
    // Módulo retiro

    /**
     * Método que nos indica el monto a retirar de la cuenta.
     */
    public static void retiro() {
        // Habilitamos el teclado
        Scanner teclado = new Scanner(System.in);

        // Variables
        int op, otroValor = 0;

        System.out.println("Seleccione la cantidad a retirar"
                + "\n1. 10000"
                + "\n2. 20000"
                + "\n3. 50000"
                + "\n4. 100000"
                + "\n5. 200000"
                + "\n6. 400000"
                + "\n7. 600000"
                + "\n8. Otro valor"
                + "");
        op = teclado.nextInt();

        // Condicional de cantidad a retirar
        if (op >= 1 || op <= 8) {
            if (validarContrasena()) {
                switch (op) {
                    case 1:
                        aplicarRetiro(10000);
                        break;
                    case 2:
                        aplicarRetiro(20000);
                        break;
                    case 3:
                        aplicarRetiro(50000);
                        break;
                    case 4:
                        aplicarRetiro(100000);
                        break;
                    case 5:
                        aplicarRetiro(200000);
                        break;
                    case 6:
                        aplicarRetiro(400000);
                        break;
                    case 7:
                        aplicarRetiro(600000);
                        break;
                    case 8:
                        System.out.println("INGRESE EL VALOR (SOLO MÚLTIPLOS DE 10000)");
                        otroValor = teclado.nextInt();
                        
                        if(otroValor % 10000 == 0){
                            aplicarRetiro(otroValor);
                        } // Fin if
                        break;
                    default:
                        System.out.println("Opción inválida");
                        break;
                } // Fin switch
            } // Fin if
        } // Fin if
    } // Fin módulo retiro

    /**
     * Método para aplicar el retiro una vez se valida tiene saldo suficiente.
     * @param valorRetiro 
     */
    public static void aplicarRetiro(int valorRetiro) {
        if (saldos[pos] >= valorRetiro) {
            if (entregarDinero(valorRetiro)) {
                saldos[pos] = saldos[pos] - valorRetiro;
            } else {
                System.out.println("LO SENTIMOS, CAJERO FUERA DE SERVICIO");
            } // Fin if-else
        } else {
            System.out.println("FONDOS INSUFICIENTES");
        } // Fin if-else
    } // Fin aplicarRetiro
    
    /**
     * Método para realizar la entrega de dinero al finalizar la transacción.
     * @param valorRetiro
     * @return - Retorna un true si se ha realizado la entrega de dinero
     * exitosamente.
     */
    public static boolean entregarDinero(int valorRetiro) {
        // Variables
        int restante, cant50 = 0, cant20 = 0, cant10 = 0;
        boolean dineroEntregado = false;
        
        if (valorRetiro <= saldoCajero){
            restante = valorRetiro;
            while(restante != 0){
                if (restante >= 50000){
                    total50--;
                    cant50++;
                    restante = restante - 50000;
                } else if (restante >= 20000){
                    total20--;
                    cant20++;
                    restante = restante - 20000;
                } else if (restante >= 10000){
                    total10--;
                    cant10++;
                    restante = restante - 10000;
                } // Fin if-else
                
                dineroEntregado = true;
            } // Fin while
            System.out.println("Le estamos entregando su dinero......");
            if(cant50>0)
                System.out.println(cant50+" billetes de 50 mil");
            if(cant20>0)
                System.out.println(cant20+" billetes de 20 mil");
            if(cant10>0)
                System.out.println(cant10+" billetes de 10 mil");
            saldoCajero = saldoCajero - valorRetiro;
        } // Fin if
        
        return dineroEntregado;
    } // Fin entregarDinero

    /**
     * Método para realizar la transferencia de dinero entre dos cuentas.
     */
    public static void transferencia() {
        // Declaramos variables
        int numCuenta = 0, valTransfer = 0;
        boolean op = false;
        
        // Habilitamos el teclado
        Scanner teclado = new Scanner(System.in);
        
        System.out.println("Digite el número de cuenta");
        numCuenta = teclado.nextInt();
        
        if(buscarCuenta(numCuenta)){
            System.out.println("Ingrese el valor a transferir");
            valTransfer = teclado.nextInt();
            
            System.out.println("Desea conocer el costo de la transacción (true/false)");
            op = teclado.nextBoolean();
            
            if(op){
                System.out.println("La transferencia tiene un costo de $4000 COP, desea continuar (true/false)");
                op = teclado.nextBoolean();
            } else{
                op = true;
            } // Fin if-else
            
            if(op){
                if(validarContrasena()){
                    if(saldos[pos] >= valTransfer){
                        saldos[pos] = saldos[pos] - valTransfer;
                        saldos[posTransfer] = saldos[posTransfer] + valTransfer - 4000;
                    } // Fin if
                } // Fin if validarContrasena
            } // Fin if op
        } // Fin if buscarCuenta
    } // Fin módulo transferencia
    
    /**
     * Método que guardar los cambios en el archivo txt de usuarios.
     * @return - Retorna un true al guardar los archivos nuevos en el 
     * documento usuarios.txt
     * 
     */
    public static boolean guardarDatosUsuario() {
        // Variables
        boolean estaGuardado = false;

        // Try-catch para escribir los nuevos datos en el archivo de usuario
        try {
            FileWriter out = null;

            out = new FileWriter(nomArchivo);

            for (int i = 0; i < tamano; i++) {
                linea = Integer.toString(tarjetas[i]) + "\t";
                linea = linea + Integer.toString(cuentas[i]) + "\t";
                linea = linea + Float.toString(saldos[i]) + "\t";
                linea = linea + tipoCuenta[i] + "\t";
                linea = linea + Integer.toString(contrasenas[i]);

                if (i != tamano - 1) {
                    linea = linea + "\n";
                } // Fin if

                
                out.write(linea);
            } // Fin for

            out.close();
            
            estaGuardado = true;
        } catch (IOException ex) {
            System.out.println(ex);
            estaGuardado = false;
        } // Fin try-catch
        
        return estaGuardado;
    } // Fin guardarDatosUsuarios
    
    /**
     * Método que guardar los cambios en el archivo txt de saldocajero.
     * @return - Retorna un true al guardar los cambios en el archivo
     * del cajero.
     */
    public static boolean guardarDatosCajero() {
        // Variables
        boolean estaGuardado = false;

        // Try-catch para escribir los nuevos datos en el archivo de cajero
        try {
            FileWriter out = null;

            out = new FileWriter(nomArchivoCajero);

                linea = Integer.toString(saldoCajero) + "\t";
                linea = linea + Integer.toString(total50) + "\t";
                linea = linea + Integer.toString(total20) + "\t";
                linea = linea + Integer.toString(total10);
                
                out.write(linea);

            out.close();
            
            estaGuardado = true;
        } catch (IOException ex) {
            estaGuardado = false;
        } // Fin try-catch
        
        return estaGuardado;
    } // Fin guardarDatosUsuarios

} // Fin class ProgramacionFinal
