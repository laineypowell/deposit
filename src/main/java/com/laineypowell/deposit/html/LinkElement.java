package com.laineypowell.deposit.html;

public final class LinkElement implements Element {
    private final String href;
    private final String name;

    public LinkElement(String href, String name) {
        this.href = href;
        this.name = name;
    }

    @Override
    public void append(StringBuilder builder) {
        builder.append(String.format("<a href=\"%s\">%s</a>", href, name));
    }
}
