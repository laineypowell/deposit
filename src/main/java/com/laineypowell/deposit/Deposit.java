package com.laineypowell.deposit;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public final class Deposit implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }

    public static void start() throws IOException {

        var address = new InetSocketAddress("localhost", 8080);
        var server = HttpServer.create(address, -1);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop(1);
        }));

        server.createContext("/", new Deposit());
        server.start();
    }
}
