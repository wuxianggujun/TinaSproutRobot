package com.wuxianggujun.robotcore.aspect;

import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.Set;

public class TypeRepository {
    private final Reflections reflections;

    private Set<Field> typeIdentifiers;
    private Set<Field> typeProperties;

    public TypeRepository(Reflections reflections) {
        this.reflections = reflections;
    }

    public Set<Field> getTypeIdentifiers() {
        if (typeIdentifiers == null) {
            //typeIdentifiers = reflections.getFieldsAnnotatedWith(Type.Identifier.class);
        }

        return typeIdentifiers;
    }

    public Set<Field> getTypeProperties() {
        if (typeProperties == null) {
            //typeProperties = reflections.getFieldsAnnotatedWith(Type.Property.class);
        }
        return typeProperties;
    }
}
