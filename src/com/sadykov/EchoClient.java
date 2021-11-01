package com.sadykov;

import javax.imageio.IIOException;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class EchoClient {
    private final int port;
    private final String host;

    private EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static EchoClient connectTo(int port) {
        String localhost = "127.0.0.1";
        return new EchoClient(localhost, port);
    }

    public void run() {
        System.out.printf("napishi 'bye' chto-by vyiti%n%n");
        try (Socket socket = new Socket(host, port)) {
            try (Scanner scanner = new Scanner(System.in, "UTF-8");) {
                OutputStream output = socket.getOutputStream();
                InputStream input = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(input);
                PrintWriter writer = new PrintWriter(output);
                Scanner clientScanner = new Scanner(isr);
                try (writer) {
                    while (true) {
                        System.out.print("SMS: ");
                        String message = scanner.nextLine();
                        writer.write(message);
                        writer.write(System.lineSeparator());
                        writer.flush();
                        if ("bye".equals(message.toLowerCase())) {
                            return;
                        }

                        String resp = clientScanner.nextLine();
                        System.out.println("Otvet: " + resp);
                    }
                }
            }
        } catch (NoSuchElementException ex) {
            System.out.printf("Connection dropped!%n");
        } catch (IIOException e) {
            String msg = "Can't connect to %s:%s:%n";
            System.out.printf(msg, host, port);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

