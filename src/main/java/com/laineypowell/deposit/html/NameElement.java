package com.laineypowell.deposit.html;

import java.util.ArrayList;
import java.util.List;

public final class NameElement implements Element {
    private final List<Element> elements = new ArrayList<>();

    private final String name;
    private final List<String> properties;

    public NameElement(String name, List<String> properties) {
        this.name = name;
        this.properties = properties;
    }

    @Override
    public void append(StringBuilder builder) {
        builder.append((properties.isEmpty() ? String.format("<%s>", name) : String.format("<%s%s>", name, properties())));
        for (var element : elements) {
            element.append(builder);
        }
        builder.append(String.format("</%s>", name));
    }

    public String properties() {
        var builder = new StringBuilder();
        for (var property : properties) {
            builder.append(" ").append(property);
        }
        return builder.toString();
    }

    public void add(Element element) {
        elements.add(element);
    }

    public static NameElement nameElement(String name) {
        return new NameElement(name, List.of());
    }
}
