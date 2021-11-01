package com.sadykov;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UnoServer {

    public void run() {
        int port = 0;
        try (ServerSocket server = new ServerSocket(port)) {
            while (!server.isClosed()) {
                Socket clientSocket = server.accept();
                AbstractExecutorService pool = null;
                pool.submit(() -> handle(clientSocket));
            }
        } catch (IOException e) {
            String formatMsg = "Veroyatnee vsego port %s zanyat.%n";
            System.out.printf(formatMsg, port);
            e.printStackTrace();
        }
    }

    private void handle(Socket clientSocket) {
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

    private void sendResponse(String response, Writer writer) throws IOException {
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

    private boolean isQuitMsg(String message) {
        return "bye".equalsIgnoreCase(message);
    }

    private boolean isEmptyMsg(String message) {
        return message == null || message.isBlank();
    }
}



