package com.sadykov;

import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class UnoServer {

    private static int response;
    private final String END_WORLD = "bye";
    private String handle;

    public UnoServer(String handle) {
        this.handle = handle;
    }

    public static void handle(Socket clientSocket) {
        System.out.printf("Client connected: %s%n", clientSocket);
        try (clientSocket;
             Scanner reader = getReader(clientSocket);
             PrintWriter writer = getWriter(clientSocket)) {
            sendResponse("" + clientSocket, writer);
            while (true) {
                String message = reader.nextLine();
                if (isEmptyMsg(message) || isQuitMsg(message)) {
                    break;
                }
                sendResponse(message.toUpperCase(), writer);
            }
        } catch (NoSuchElementException | IOException ex) {
            System.out.printf("Client disconnected: %s%n", clientSocket);
        }
    }

    private static void sendResponse(String response, Writer writer) throws IOException {
        writer.write(response);
        writer.write(System.lineSeparator());
        writer.flush();
    }

    private static PrintWriter getWriter(Socket clientSocket) throws IOException {
        OutputStream stream = clientSocket.getOutputStream();
        return new PrintWriter(stream);
    }

    private static Scanner getReader(Socket clientSocket) throws IOException {
        InputStream stream = clientSocket.getInputStream();
        InputStreamReader input = new InputStreamReader(stream, "UTF-8");
        return new Scanner(input);
    }

    private static boolean isQuitMsg(String message) {
        return "bye".equalsIgnoreCase(message);
    }

    private static boolean isEmptyMsg(String message) {
        return message == null || message.isBlank();
    }
}



