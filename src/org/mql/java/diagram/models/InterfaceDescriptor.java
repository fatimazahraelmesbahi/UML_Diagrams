package org.mql.java.diagram.models;

import java.lang.reflect.*;
import java.util.*;

public class InterfaceDescriptor {
    private String name;
    private String accessModifiers;
    private List<String> methods;
    private Set<String> extendedInterfaces;
    private Set<String> usedClasses;

    public InterfaceDescriptor(String classPath) {
        this.methods = new ArrayList<>();
        this.extendedInterfaces = new HashSet<>();
        this.usedClasses = new HashSet<>();
        
        try {
            Class<?> cls = Class.forName(classPath);
            this.name = cls.getName();
            this.accessModifiers = Modifier.toString(cls.getModifiers());
            this.methods = getMethodDescriptors(cls);
            analyzeRelations(cls);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private List<String> getMethodDescriptors(Class<?> cls) {
        List<String> methodList = new ArrayList<>();
        for (Method method : cls.getDeclaredMethods()) {
            StringBuilder methodInfo = new StringBuilder("Method name: " + method.getName() +
                                                         ", Return type: " + method.getReturnType().getName() +
                                                         ", Modifiers: " + Modifier.toString(method.getModifiers()));
            methodInfo.append(", Parameters: ");
            for (Class<?> param : method.getParameterTypes()) {
                methodInfo.append(param.getName()).append(" ");
            }
            methodList.add(methodInfo.toString());
        }
        return methodList;
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

        for (Class<?> iface : cls.getInterfaces()) {
            extendedInterfaces.add(iface.getName());
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

    public List<String> getMethods() {
        return methods;
    }

    public Set<String> getExtendedInterfaces() {
        return extendedInterfaces;
    }

    public Set<String> getUsedClasses() {
        return usedClasses;
    }

    @Override
    public String toString() {
        return "InterfaceDescriptor{" +
                "name='" + name + '\'' +
                ", accessModifiers='" + accessModifiers + '\'' +
                ", methods=" + methods +
                ", extendedInterfaces=" + extendedInterfaces +
                ", usedClasses=" + usedClasses +
                '}';
    }
}
