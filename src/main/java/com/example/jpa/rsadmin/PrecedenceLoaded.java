package com.example.jpa.rsadmin;

import lombok.Value;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

@Value
public class PrecedenceLoaded<T> implements ResolvableTypeProvider {

    private final T source;
    private final T destination;
    private final boolean deleted;

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forInstance(source));
    }
}
