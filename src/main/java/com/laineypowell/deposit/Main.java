package com.laineypowell.deposit;

import java.io.IOException;

public final class Main {

    public static void main(String[] args) throws IOException {
        Deposit.start(Host.host(Deposit.PATH.resolve(Deposit.JSON)));
    }
}
