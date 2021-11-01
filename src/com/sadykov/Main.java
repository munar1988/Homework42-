package com.sadykov;

public class Main {

    private static Integer PORT = 7777;
    public static void main(String[] args) {

        final EchoServer echoServer = EchoServer.bingToPort(PORT);
        echoServer.run();
    }
}
