package ru.practicum.explorewithme.util;

@FunctionalInterface
public interface TreeFunction<A, B, C, Z> {
    Z apply(A a, B b, C c);
}
