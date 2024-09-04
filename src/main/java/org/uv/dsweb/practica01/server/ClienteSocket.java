package org.uv.dsweb.practica01.server;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteSocket extends Thread {

    private final Socket cliente;
    private BufferedReader entrada;
    private BufferedWriter salida;

    public ClienteSocket(Socket cliente) {
        this.cliente = cliente;

        try {
            entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            salida = new BufferedWriter(new OutputStreamWriter(cliente.getOutputStream()));
        } catch (IOException e) {
            Logger.getLogger(ClienteSocket.class.getName()).log(Level.SEVERE, "Error al obtener flujos de E/S", e);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                String operacion = entrada.readLine();
                System.out.println("Recibido: " + operacion);

                String respuesta = procesarOperacion(operacion);

                salida.write(respuesta + "\n");
                salida.flush();
            } catch (IOException e) {
                Logger.getLogger(ClienteSocket.class.getName()).log(Level.SEVERE, "Error en la comunicación", e);
            }
        }
    }

    private String procesarOperacion(String operacion) {
        int suma = operacion.indexOf("+");
        int resta = operacion.indexOf("-");
        int multiplicacion = operacion.indexOf("*");
        int division = operacion.indexOf("/");

        try {
            if (suma != -1) {
                return realizarSuma(operacion, suma);
            } else if (resta != -1) {
                return realizarResta(operacion, resta);
            } else if (multiplicacion != -1) {
                return realizarMultiplicacion(operacion, multiplicacion);
            } else if (division != -1) {
                return realizarDivision(operacion, division);
            } else {
                return "Error: Operación no reconocida.";
            }
        } catch (NumberFormatException e) {
            return "Error: Formato numérico inválido.";
        }
    }

    private String realizarSuma(String operacion, int indice) {
        String a = operacion.substring(0, indice).trim();
        String b = operacion.substring(indice + 1).trim();
        int resultado = Integer.parseInt(a) + Integer.parseInt(b);
        return "a: " + a + ", b: " + b + ", suma: " + resultado;
    }

    private String realizarResta(String operacion, int indice) {
        String a = operacion.substring(0, indice).trim();
        String b = operacion.substring(indice + 1).trim();
        int resultado = Integer.parseInt(a) - Integer.parseInt(b);
        return "a: " + a + ", b: " + b + ", resta: " + resultado;
    }

    private String realizarMultiplicacion(String operacion, int indice) {
        String a = operacion.substring(0, indice).trim();
        String b = operacion.substring(indice + 1).trim();
        int resultado = Integer.parseInt(a) * Integer.parseInt(b);
        return "a: " + a + ", b: " + b + ", multiplicación: " + resultado;
    }

    private String realizarDivision(String operacion, int indice) {
        String a = operacion.substring(0, indice).trim();
        String b = operacion.substring(indice + 1).trim();

        try {
            int resultado = Integer.parseInt(a) / Integer.parseInt(b);
            return "a: " + a + ", b: " + b + ", división: " + resultado;
        } catch (ArithmeticException e) {
            return "Error: División por cero no permitida.";
        }
    }
}
