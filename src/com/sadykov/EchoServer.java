package com.sadykov;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ScatteringByteChannel;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer {

    private static int response;
    private final int port;
    private final String END_WORLD = "bye";

    private final ExecutorService pool = Executors.newCachedThreadPool();

    private EchoServer(int port) {
        this.port = port;
    }

    static EchoServer bingToPort(int port) {
        final EchoServer echoServer = new EchoServer(port);
        System.out.println("create echo server");
        return echoServer;
    }

    public void run() {
        try (ServerSocket server = new ServerSocket(port)) {
            while (!server.isClosed()) {
                Socket clientSocket = server.accept();
                pool.submit(() -> UnoServer.handle(clientSocket));
            }
        } catch (IOException e) {
            String formatMsg = "Veroyatnee vsego port %s zanyat.%n";
            System.out.printf(formatMsg, port);
            e.printStackTrace();
        }
    }
}



