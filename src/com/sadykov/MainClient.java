package com.sadykov;

public class MainClient {
    private static Integer PORT = 7777;

    public static void main(String[] args) {
        final EchoClient echoClient = EchoClient.connectTo(PORT);
        echoClient.run();
    }
}
