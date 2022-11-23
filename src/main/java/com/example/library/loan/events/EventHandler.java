package com.example.library.loan.events;

public interface EventHandler<T extends Event> {

    boolean canHandle(String type);

    void handle(T event);

}
