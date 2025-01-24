package org.mql.java.diagram.models;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class MethodDescriptor {
    private String name;
    private String returnType;
    private String accessModifier;
    private String[] parameterTypes;

    public MethodDescriptor(Method method) {
        this.name = method.getName();
        this.returnType = method.getReturnType().getName();
        this.accessModifier = Modifier.toString(method.getModifiers());
        this.parameterTypes = getParameterTypes(method);
    }

    private String[] getParameterTypes(Method method) {
        Class<?>[] parameterClasses = method.getParameterTypes();
        String[] params = new String[parameterClasses.length];
        for (int i = 0; i < parameterClasses.length; i++) {
            params[i] = parameterClasses[i].getName();
        }
        return params;
    }
    
    public String getRepresentation() {
        StringBuilder representation = new StringBuilder();
        representation.append(accessModifier).append(" ")
                .append(returnType).append(" ")
                .append(name).append("(");
        for (int i = 0; i < parameterTypes.length; i++) {
            representation.append(parameterTypes[i]);
            if (i < parameterTypes.length - 1) {
                representation.append(", ");
            }
        }
        representation.append(")");
        return representation.toString();
    }


    public String getName() {
        return name;
    }

    public String getReturnType() {
        return returnType;
    }

    public String getAccessModifier() {
        return accessModifier;
    }

    public String[] getParameterTypes() {
        return parameterTypes;
    }

    @Override
    public String toString() {
        return "MethodDescriptor{" +
                "name='" + name + '\'' +
                ", returnType='" + returnType + '\'' +
                ", accessModifier='" + accessModifier + '\'' +
                ", parameterTypes=" + String.join(", ", parameterTypes) +
                '}';
    }
}
