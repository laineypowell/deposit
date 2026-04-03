package com.laineypowell.deposit;

import com.laineypowell.deposit.html.Document;
import com.laineypowell.deposit.html.NameElement;
import com.laineypowell.deposit.html.StringElement;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.List;

public final class DepositTest {

    public @Test void test() {
        var document = new Document();

        var html = new NameElement("html", List.of("lang=\"en\""));
        document.add(html);

        var body = NameElement.nameElement("body");
        html.add(body);

        var header = NameElement.nameElement("header");
        body.add(header);

        header.add(new StringElement("test"));

        var pre = NameElement.nameElement("pre");
        body.add(pre);

        var builder = new StringBuilder();
        document.append(builder);

        var logger = LoggerFactory.getLogger("test");
        logger.info(builder.toString());
    }
}
