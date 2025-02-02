package com.agrotis.backendtest.adapter;

import org.springframework.stereotype.Component;

@Component
public interface Adapter<Entity, Request> {
    Entity toEntity(Request request);

}