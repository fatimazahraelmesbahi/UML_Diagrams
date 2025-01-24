package org.mql.java.diagram.models;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

public class ConstructorDescriptor {
    private String name;
    private String modifiers;
    private String[] parameterTypes;

    public ConstructorDescriptor(Constructor<?> constructor) {
        this.name = constructor.getName();
        this.modifiers = Modifier.toString(constructor.getModifiers());
        this.parameterTypes = new String[constructor.getParameterCount()];
        Class<?>[] paramTypes = constructor.getParameterTypes();
        for (int i = 0; i < paramTypes.length; i++) {
            parameterTypes[i] = paramTypes[i].getName();
        }
    }

    public String getName() {
        return name;
    }

    public String getModifiers() {
        return modifiers;
    }

    public String[] getParameterTypes() {
        return parameterTypes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ConstructorDescriptor{")
          .append("name='").append(name).append('\'')
          .append(", modifiers='").append(modifiers).append('\'')
          .append(", parameterTypes=");
        for (String paramType : parameterTypes) {
            sb.append(paramType).append(" ");
        }
        sb.append('}');
        return sb.toString();
    }
}
