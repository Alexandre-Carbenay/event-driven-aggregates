package com.example.library.loan.events;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
@SuppressWarnings("rawtypes")
public class EventDispatcher {

    private final Collection<EventHandler> handlers;

    @SuppressWarnings("unchecked")
    public void dispatch(Event event) {
        handlers.stream()
                .filter(handler -> handler.canHandle(event.type()))
                .forEach(handler -> handler.handle(event));
    }

}
