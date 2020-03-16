package com.example.jpa.rsadmin;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

@Data
@AllArgsConstructor
public class RSCommandFailed<Command> implements ResolvableTypeProvider {

    private Exception exception;
    private Command command;

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forInstance(command));
    }
}
