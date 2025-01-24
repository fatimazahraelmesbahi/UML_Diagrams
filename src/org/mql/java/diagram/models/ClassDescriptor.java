package org.mql.java.diagram.models;

import java.lang.reflect.*;
import java.util.*;

public class ClassDescriptor {
    private String name;
    private String accessModifiers;
    private String parentClass;
    private List<String> fields;
    private List<String> methods;
    private Set<String> usedClasses;
    private Set<String> implementedInterfaces;
    private Set<String> composedClasses;
    private Set<String> aggregatedClasses;
    private List<Relationship> relationships;

    public ClassDescriptor(String classPath) {
        this.usedClasses = new HashSet<>();
        this.implementedInterfaces = new HashSet<>();
        this.composedClasses = new HashSet<>();
        this.aggregatedClasses = new HashSet<>();
        this.fields = new ArrayList<>();
        this.methods = new ArrayList<>();
        this.relationships = new ArrayList<>();
        
        try {
            Class<?> cls = Class.forName(classPath);
            this.name = cls.getSimpleName(); 
            this.accessModifiers = getAccessModifiers(cls);
            this.parentClass = getParentClass(cls);
            this.fields = getFieldDescriptors(cls);
            this.methods = getMethodDescriptors(cls);
            analyzeRelations(cls);
            
            // Affichage des informations sous forme de diagramme UML
            printUML();
            
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur : La classe " + classPath + " n'a pas été trouvée.");
            e.printStackTrace();
        }
    }

    private String getAccessModifiers(Class<?> cls) {
        return Modifier.toString(cls.getModifiers());
    }

    private String getParentClass(Class<?> cls) {
        Class<?> superClass = cls.getSuperclass();
        return (superClass != null && !superClass.equals(Object.class)) ? superClass.getSimpleName() : null;
    }

    private List<String> getFieldDescriptors(Class<?> cls) {
        List<String> fieldList = new ArrayList<>();
        for (Field field : cls.getDeclaredFields()) {
            String fieldInfo = "-" + field.getName() + " : " + field.getType().getSimpleName();
            fieldList.add(fieldInfo);
        }
        return fieldList;
    }

    private List<String> getMethodDescriptors(Class<?> cls) {
        List<String> methodList = new ArrayList<>();
        for (Method method : cls.getDeclaredMethods()) {
            StringBuilder methodInfo = new StringBuilder("+ " + method.getName() + "()");
            methodList.add(methodInfo.toString());
        }
        return methodList;
    }

    private void analyzeRelations(Class<?> cls) {
        for (Field field : cls.getDeclaredFields()) {
            String fieldType = field.getType().getSimpleName();
            if (!field.getType().isPrimitive()) {
                if (!usedClasses.contains(fieldType)) {
                    usedClasses.add(fieldType);
                }
                if (field.getType().getName().endsWith("List")) {
                    composedClasses.add(fieldType);
                    relationships.add(new Relationship(cls.getSimpleName(), fieldType, "Composition"));
                } else {
                    aggregatedClasses.add(fieldType);
                    relationships.add(new Relationship(cls.getSimpleName(), fieldType, "Aggregation"));
                }
            }
        }

        for (Method method : cls.getDeclaredMethods()) {
            for (Class<?> paramType : method.getParameterTypes()) {
                if (!paramType.isPrimitive() && !usedClasses.contains(paramType.getSimpleName())) {
                    usedClasses.add(paramType.getSimpleName());
                    relationships.add(new Relationship(cls.getSimpleName(), paramType.getSimpleName(), "Use"));
                }
            }
            if (!method.getReturnType().isPrimitive() && !usedClasses.contains(method.getReturnType().getSimpleName())) {
                usedClasses.add(method.getReturnType().getSimpleName());
                relationships.add(new Relationship(cls.getSimpleName(), method.getReturnType().getSimpleName(), "Use"));
            }
            for (Class<?> iface : method.getDeclaringClass().getInterfaces()) {
                implementedInterfaces.add(iface.getSimpleName());
                relationships.add(new Relationship(cls.getSimpleName(), iface.getSimpleName(), "Implementation"));
            }
        }

        if (cls.getSuperclass() != null) {
            relationships.add(new Relationship(cls.getSimpleName(), cls.getSuperclass().getSimpleName(), "Inheritance"));
        }
    }

    private void printUML() {
        // Affichage des informations sous forme de diagramme UML
        System.out.println("Class: " + name + " {");
        fields.forEach(field -> System.out.println("\t" + field));
        methods.forEach(method -> System.out.println("\t" + method));
        System.out.println("}");

        // Affichage des relations
        relationships.forEach(relationship -> {
        	System.out.println(relationship.getFrom() + " -- " + relationship.getTo() + " : " + relationship.getRelationshipType());
        });
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

    public String getParentClass() {
        return parentClass;
    }

    public List<String> getFields() {
        return fields;
    }

    public List<String> getMethods() {
        return methods;
    }

    public Set<String> getUsedClasses() {
        return usedClasses;
    }

    public Set<String> getImplementedInterfaces() {
        return implementedInterfaces;
    }

    public Set<String> getComposedClasses() {
        return composedClasses;
    }

    public Set<String> getAggregatedClasses() {
        return aggregatedClasses;
    }

    public List<Relationship> getRelationships() {
        return relationships;
    }

    @Override
    public String toString() {
        return "ClassDescriptor{" +
                "name='" + name + '\'' +
                ", accessModifiers='" + accessModifiers + '\'' +
                ", parentClass='" + parentClass + '\'' +
                ", fields=" + fields +
                ", methods=" + methods +
                ", usedClasses=" + usedClasses +
                ", implementedInterfaces=" + implementedInterfaces +
                ", composedClasses=" + composedClasses +
                ", aggregatedClasses=" + aggregatedClasses +
                ", relationships=" + relationships +
                '}';
    }
}
