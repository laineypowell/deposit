package com.laineypowell.deposit;

import com.google.gson.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Host {
    public static final JsonDeserializer<Host> JSON_DESERIALIZER = new JsonDeserializer<Host>() {
        @Override
        public Host deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            var jsonObject = json.getAsJsonObject();
            var host = jsonObject.getAsJsonPrimitive("host").getAsString();
            var port = jsonObject.getAsJsonPrimitive("port").getAsInt();
            return new Host(new InetSocketAddress(host, port));
        }
    };

    private final InetSocketAddress address;

    private Host(InetSocketAddress address) {
        this.address = address;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public static Host host(Path path) throws IOException {
        try (var reader = Files.newBufferedReader(path)) {
            return new GsonBuilder().registerTypeAdapter(Host.class, JSON_DESERIALIZER).create().fromJson(reader, Host.class);
        }
    }
}
