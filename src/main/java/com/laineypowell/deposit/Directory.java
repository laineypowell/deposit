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
import java.nio.file.Paths;
import java.util.List;

public final class Directory {
    private final Gson gson = new GsonBuilder().registerTypeAdapter(Basic.class, Basic.JSON_DESERIALIZER).create();

    private final Path path;

    public Directory(Path path) {
        this.path = path;
    }

    public void get(HttpExchange exchange) throws IOException {
        var path = this.path.resolve(uri(exchange));
        if (shouldIgnore(path)) {
            exchange.sendResponseHeaders(401, -1);
            return;
        }

        if (Files.isDirectory(path)) {
            list(exchange, path);
        } else if (Files.isRegularFile(path)) {
            try (var inputStream = Files.newInputStream(path)) {
                send(exchange, inputStream.readAllBytes());
            }
        } else {
            exchange.sendResponseHeaders(404, -1);
        }
    }

    public boolean shouldIgnore(Path path) {
        var s = this.path.toString();
        if (Files.isRegularFile(path)) {
            return path.getParent().toString().equals(s);
        }
        return path.toString().equals(s);
    }

    public void list(HttpExchange exchange, Path path) throws IOException {
        var document = new Document();

        var html = new NameElement("html", List.of("lang=\"en\""));
        document.add(html);

        var body = NameElement.nameElement("body");
        html.add(body);

        var header = NameElement.nameElement("h1");
        body.add(header);

        var uri = uri(exchange);
        var uriPath = Paths.get(uri);

        header.add(new StringElement(stylised(uri)));

        var pre = NameElement.nameElement("pre");
        body.add(pre);

        var parent = uriPath.getParent();
        if (parent != null) {
            path(exchange, pre, parent.toString(), stylised(String.format("%s/..", parent)));
        }

        try (var stream = Files.newDirectoryStream(path)) {
            for (var entry : stream) {
                var name = entry.getFileName().toString();
                path(exchange, pre, uriPath.resolve(name).toString(), name);
            }
        }

        var builder = new StringBuilder();
        document.append(builder);

        send(exchange, builder.toString().getBytes());
    }

    public void path(HttpExchange exchange, NameElement element, String path, String name) {
        var href = String.format("http://%s/%s", exchange.getRequestHeaders().getFirst("Host"), path);
        element.add(new LinkElement(href, name));
        element.add(new StringElement("<br>"));
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
                outputStream.write(inputStream.readAllBytes());

                exchange.getResponseHeaders().set("Content-Type", "application/octet-stream");
                exchange.sendResponseHeaders(200, 0);

                exchange.getResponseBody().close();
            }
        } else {
            exchange.sendResponseHeaders(401, -1);
        }
    }

    public String uri(HttpExchange exchange) {
        var uri = exchange.getRequestURI().toString();
        return uri.startsWith("/") ? uri.substring(1) : uri;
    }

    public String stylised(String s) {
        return s.replace("\\", "/");
    }

    public Basic basic() throws IOException {
        try (var reader = Files.newBufferedReader(path.resolve(Deposit.JSON))) {
            return gson.fromJson(reader, Basic.class);
        }
    }
}
