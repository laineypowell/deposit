package com.laineypowell.deposit.html;

import java.util.ArrayList;
import java.util.List;

public final class Document implements Element {
    private final List<Element> elements = new ArrayList<>();

    @Override
    public void append(StringBuilder builder) {
        builder.append("<!doctype html>");

        for (var element : elements) {
            element.append(builder);
        }
    }

    public void add(Element element) {
        elements.add(element);
    }
}
