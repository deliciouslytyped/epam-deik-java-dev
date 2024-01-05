package com.epam.training.ticketservice;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.jline.PromptProvider;

@Configuration
public class CliConfig implements PromptProvider { //TODO move?

    @Override
    public AttributedString getPrompt() {
        return new AttributedStringBuilder()
                .append("T", AttributedStyle.DEFAULT.foreground(AttributedStyle.RED))
                .append("i", AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW))
                .append("c", AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN))
                .append("k", AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE))
                .append("e", AttributedStyle.DEFAULT.foreground(AttributedStyle.CYAN))
                .append("t", AttributedStyle.DEFAULT.foreground(AttributedStyle.MAGENTA))
                .append(" ", AttributedStyle.DEFAULT.foreground(AttributedStyle.RED))
                .append("s", AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW))
                .append("e", AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN))
                .append("r", AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE))
                .append("v", AttributedStyle.DEFAULT.foreground(AttributedStyle.CYAN))
                .append("i", AttributedStyle.DEFAULT.foreground(AttributedStyle.MAGENTA))
                .append("c", AttributedStyle.DEFAULT.foreground(AttributedStyle.RED))
                .append("e", AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW))
                .append(">", AttributedStyle.DEFAULT.foreground(AttributedStyle.WHITE))
                .toAttributedString();
    }
}
