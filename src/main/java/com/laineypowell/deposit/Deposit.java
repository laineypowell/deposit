package com.laineypowell.deposit;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Deposit implements HttpHandler {
    public static final String GET = "get";
    public static final String PUT = "put";

    public static final Path PATH = Paths.get("deposit");
    public static final Path JSON = Paths.get("deposit.json");

    private final Directory directory = new Directory(PATH);

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        var method = exchange.getRequestMethod();

        if (method.equalsIgnoreCase(GET)) {
            directory.get(exchange);
        } else if (method.equalsIgnoreCase(PUT)) {
            directory.put(exchange);
        } else {
            exchange.getResponseHeaders().add("Allow", "GET, PUT");
            exchange.sendResponseHeaders(405, -1);
        }
    }

    public static void start(Host host) throws IOException {
        var server = HttpServer.create(host.getAddress(), -1);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop(1);
        }));

        server.createContext("/", new Deposit());
        server.start();
    }
}
