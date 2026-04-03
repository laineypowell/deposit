package com.laineypowell.deposit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.laineypowell.deposit.html.Document;
import com.laineypowell.deposit.html.LinkElement;
import com.laineypowell.deposit.html.NameElement;
import com.laineypowell.deposit.html.StringElement;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class Directory {
    private final Gson gson = new GsonBuilder().registerTypeAdapter(Basic.class, Basic.JSON_DESERIALIZER).create();

    private final Path path;

    public Directory(Path path) {
        this.path = path;
    }

    public void get(HttpExchange exchange) throws IOException {

        exchange.sendResponseHeaders(404, -1);
    }

    public void list(HttpExchange exchange, Path path) throws IOException {
        var document = new Document();

        var html = new NameElement("html", List.of("lang=\"en\""));
        document.add(html);

        var body = NameElement.nameElement("body");
        html.add(body);

        var header = NameElement.nameElement("header");
        body.add(header);

        var uriPath = uri(exchange);
        header.add(new StringElement(stylised(uriPath)));

        var pre = NameElement.nameElement("pre");
        body.add(pre);

        try (var stream = Files.newDirectoryStream(path)) {
            for (var entry : stream) {

                var href = String.format("http://%s/%s", exchange.getRequestHeaders().getFirst("Host"), null);
                pre.add(new LinkElement(href, entry.getFileName().toString()));
                pre.add(new StringElement("<br>"));
            }
        }

        var builder = new StringBuilder();
        document.append(builder);

        send(exchange, builder.toString().getBytes());
    }

    public void send(HttpExchange exchange, byte[] bytes) throws IOException {
        exchange.sendResponseHeaders(200, bytes.length);

        try (var outputStream = exchange.getResponseBody()) {
            outputStream.write(bytes);
            outputStream.flush();
        }
    }

    public void put(HttpExchange exchange) throws IOException {
        if (basic().compare(exchange)) {
            var path = this.path.resolve(uri(exchange));

            var parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            try (var inputStream = exchange.getRequestBody(); var outputStream = Files.newOutputStream(path)) {
                inputStream.transferTo(outputStream);
                outputStream.flush();
            }
            exchange.sendResponseHeaders(200, -1);
        } else {
            exchange.sendResponseHeaders(401, -1);
        }
    }

    public String uri(HttpExchange exchange) {
        return exchange.getRequestURI().toString().substring(1);
    }

    public String stylised(String s) {
        return s.replace("\\", "/");
    }

    public Basic basic() throws IOException {
        try (var reader = Files.newBufferedReader(path.resolve("basic.json"))) {
            return gson.fromJson(reader, Basic.class);
        }
    }
}
