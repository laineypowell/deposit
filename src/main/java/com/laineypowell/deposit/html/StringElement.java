package com.laineypowell.deposit.html;

public final class StringElement implements Element {
    private final String s;

    public StringElement(String s) {
        this.s = s;
    }

    @Override
    public void append(StringBuilder builder) {
        builder.append(s);
    }
}
