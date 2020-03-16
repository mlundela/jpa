package com.example.jpa.rsadmin;

import lombok.Value;

@Value
public class RSCommandWrapper<Command> implements RSCommand {
    private final Command command;
    private final RSSystemId systemId;
}
