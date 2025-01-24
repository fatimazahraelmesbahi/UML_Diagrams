package org.mql.java.diagram.models;

import java.lang.reflect.*;
import java.util.*;

public class EnumDescriptor {
    private String name;
    private String accessModifiers;
    private List<String> constants;
    private Set<String> usedClasses;

    public EnumDescriptor(String classPath) {
        this.constants = new ArrayList<>();
        this.usedClasses = new HashSet<>();
        
        try {
            Class<?> cls = Class.forName(classPath);
            this.name = cls.getName();
            this.accessModifiers = Modifier.toString(cls.getModifiers());
            this.constants = getEnumConstants(cls);
            analyzeRelations(cls);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private List<String> getEnumConstants(Class<?> cls) {
        List<String> constantList = new ArrayList<>();
        if (cls.isEnum()) {
            for (Object constant : cls.getEnumConstants()) {
                constantList.add(constant.toString());
            }
        }
        return constantList;
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

    public String getAccessModifiers() {
        return accessModifiers;
    }

    public List<String> getConstants() {
        return constants;
    }

    public Set<String> getUsedClasses() {
        return usedClasses;
    }

    public String getClassName() {
        return name;
    }

    @Override
    public String toString() {
        return "EnumDescriptor{" +
                "name='" + name + '\'' +
                ", accessModifiers='" + accessModifiers + '\'' +
                ", constants=" + constants +
                ", usedClasses=" + usedClasses +
                '}';
    }
}
