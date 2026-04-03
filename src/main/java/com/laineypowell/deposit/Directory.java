package com.laineypowell.deposit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Directory {
    private final Gson gson = new GsonBuilder().registerTypeAdapter(Basic.class, Basic.JSON_DESERIALIZER).create();

    private final Path path;

    public Directory(Path path) {
        this.path = path;
    }

    public void get(HttpExchange exchange) throws IOException {

    }

    public void put(HttpExchange exchange) throws IOException {
        if (basic().compare(exchange)) {

            exchange.sendResponseHeaders(200, -1);
        } else {
            exchange.sendResponseHeaders(401, -1);
        }
    }

    public Basic basic() throws IOException {
        try (var reader = Files.newBufferedReader(path.resolve("credentials.json"))) {
            return gson.fromJson(reader, Basic.class);
        }
    }

}
