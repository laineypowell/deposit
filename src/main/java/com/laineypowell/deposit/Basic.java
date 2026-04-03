package com.laineypowell.deposit;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;

import java.lang.reflect.Type;
import java.util.Base64;

public final class Basic {
    public static final JsonDeserializer<Basic> JSON_DESERIALIZER = new JsonDeserializer<Basic>() {
        @Override
        public Basic deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            var jsonObject = json.getAsJsonObject();

            var username = jsonObject.getAsJsonPrimitive("username").getAsString();
            var password = jsonObject.getAsJsonPrimitive("password").getAsString();
            return new Basic(new String(Base64.getEncoder().encode(String.format("%s:%s", username, password).getBytes())));
        }
    };

    private final String basic;

    public Basic(String basic) {
        this.basic = basic;
    }

    public boolean compare(HttpExchange exchange) {
        var split = exchange.getRequestHeaders().getFirst("Authorization").split(" ", 2);
        return split[1].equals(basic);
    }

}
