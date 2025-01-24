package org.mql.java.diagram.models;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class FieldDescriptor {
    private String name;
    private String type;
    private String accessModifier;

    public FieldDescriptor(Field field) {
        this.name = field.getName();
        this.type = field.getType().getName();
        this.accessModifier = Modifier.toString(field.getModifiers());
    }
    
    public String getRepresentation() {
        return accessModifier + " " + type + " " + name;
    }

    

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getAccessModifier() {
        return accessModifier;
    }

    @Override
    public String toString() {
        return "FieldDescriptor{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", accessModifier='" + accessModifier + '\'' +
                '}';
    }
}
