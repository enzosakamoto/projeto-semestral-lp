package server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientSide {
    private static Socket client;
    private static Scanner input;
    private static PrintStream output;

    public static void startClient() throws IOException {
        client = new Socket(ServerSide.IP, ServerSide.PORT);
        System.out.println(
                "Cliente: " + ServerSide.IP + ":" + ServerSide.PORT + " conectado ao servidor!");
    }

    public static void sendToServer(String msg, String orderId) throws IOException {
        output = new PrintStream(client.getOutputStream());
        output.println(msg);
        output.println(orderId);
        // System.out.println("Enviado para o servidor: " + msg);
    }

    public static String messageReceived() throws IOException {
        String msg = input.nextLine();
        System.out.println("Chegou do servidor: " + msg);
        return msg;
    }
}
