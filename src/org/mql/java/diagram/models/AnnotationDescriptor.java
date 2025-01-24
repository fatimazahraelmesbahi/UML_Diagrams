package org.mql.java.diagram.models;

import java.lang.reflect.*;
import java.util.*;

public class AnnotationDescriptor {
    private String name;
    private String accessModifiers;
    private Map<String, String> properties;
    private Set<String> usedClasses;

    public AnnotationDescriptor(String classPath) {
        this.properties = new HashMap<>();
        this.usedClasses = new HashSet<>();
        
        try {
            Class<?> cls = Class.forName(classPath);
            this.name = cls.getName();
            this.accessModifiers = Modifier.toString(cls.getModifiers());
            this.properties = getAnnotationProperties(cls);
            analyzeRelations(cls);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> getAnnotationProperties(Class<?> cls) {
        Map<String, String> propertyMap = new HashMap<>();
        if (cls.isAnnotation()) {
            for (Method method : cls.getDeclaredMethods()) {
                try {
                    propertyMap.put(method.getName(), method.getDefaultValue() != null ? method.getDefaultValue().toString() : "No Default Value");
                } catch (Exception e) {
                    propertyMap.put(method.getName(), "Error");
                }
            }
        }
        return propertyMap;
    }

    private void analyzeRelations(Class<?> cls) {
        for (Method method : cls.getDeclaredMethods()) {
            for (Class<?> paramType : method.getParameterTypes()) {
                if (!paramType.isPrimitive() && !usedClasses.contains(paramType.getName())) {
                    usedClasses.add(paramType.getName());
                }
            }
            if (!method.getReturnType().isPrimitive() && !usedClasses.contains(method.getReturnType().getName())) {
                usedClasses.add(method.getReturnType().getName());
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return name;
    }
    
    public String getAccessModifiers() {
        return accessModifiers;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public Set<String> getUsedClasses() {
        return usedClasses;
    }

    @Override
    public String toString() {
        return "AnnotationDescriptor{" +
                "name='" + name + '\'' +
                ", accessModifiers='" + accessModifiers + '\'' +
                ", properties=" + properties +
                ", usedClasses=" + usedClasses +
                '}';
    }
}
