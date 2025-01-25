package org.mql.java.diagram.models;

import java.util.*;

public class PackageDescriptor {
    private String packageName;
    private Set<String> classNames;
    private Set<String> interfaceNames;
    private Set<String> usedClasses;
    private Set<String> implementedInterfaces;
    private Set<String> enums;
    private Set<String> annotations;
    private Set<PackageDescriptor> subPackages;

    public PackageDescriptor(String packageName, List<ClassDescriptor> classes) {
        this.packageName = packageName;
        this.classNames = new HashSet<>();
        this.interfaceNames = new HashSet<>();
        this.usedClasses = new HashSet<>();
        this.implementedInterfaces = new HashSet<>();
        this.enums = new HashSet<>();
        this.annotations = new HashSet<>();
        this.subPackages = new HashSet<>();
        setClasses(classes);  // Appel à la méthode pour ajouter les classes à partir de la liste
    }


    public void addClass(ClassDescriptor classDescriptor) {
        classNames.add(classDescriptor.getClassName());
    }

    public void addUsedClass(String className) {
        usedClasses.add(className);
    }
    
    public void addInterface(InterfaceDescriptor interfaceDescriptor) {
        interfaceNames.add(interfaceDescriptor.getClassName());
    }

    public void addEnum(EnumDescriptor enumDescriptor) {
        enums.add(enumDescriptor.getClassName());
    }

    public void addAnnotation(AnnotationDescriptor annotationDescriptor) {
        annotations.add(annotationDescriptor.getClassName());
    }

    public void addSubPackage(PackageDescriptor subPackage) {
        subPackages.add(subPackage);
    }
    
    public void display() {
        System.out.println("Package: " + packageName);
        System.out.println("Classes: " + classNames);
        System.out.println("Interfaces: " + interfaceNames);
        System.out.println("Enums: " + enums);
        System.out.println("Annotations: " + annotations);
        
        for (PackageDescriptor subPackage : subPackages) {
            subPackage.display(); 
        }
    }

    public String getPackageName() {
        return packageName;
    }

    public Set<String> getClassNames() {
        return classNames;
    }

    public Set<String> getInterfaceNames() {
        return interfaceNames;
    }

    public Set<String> getUsedClasses() {
        return usedClasses;
    }

    public Set<String> getImplementedInterfaces() {
        return implementedInterfaces;
    }

    public Set<String> getEnums() {
        return enums;
    }

    public Set<String> getAnnotations() {
        return annotations;
    }

    public Set<PackageDescriptor> getSubPackages() {
        return subPackages;
    }

    public void setClasses(List<ClassDescriptor> classes) {
        for (ClassDescriptor classDescriptor : classes) {
            classNames.add(classDescriptor.getClassName());
        }
    }

    public void setInterfaces(List<InterfaceDescriptor> interfaces) {
        for (InterfaceDescriptor interfaceDescriptor : interfaces) {
            interfaceNames.add(interfaceDescriptor.getClassName());
        }
    }

    public void setAnnotations(List<AnnotationDescriptor> annotations) {
        for (AnnotationDescriptor annotationDescriptor : annotations) {
            this.annotations.add(annotationDescriptor.getClassName());
        }
    }

    public void setEnums(List<EnumDescriptor> enums) {
        for (EnumDescriptor enumDescriptor : enums) {
            this.enums.add(enumDescriptor.getClassName());
        }
    }

    public void setSubPackages(List<PackageDescriptor> subPackages) {
        this.subPackages.addAll(subPackages);
    }

    @Override
    public String toString() {
        return "PackageDescriptor{" +
                "packageName='" + packageName + '\'' +
                ", classNames=" + classNames +
                ", interfaceNames=" + interfaceNames +
                ", usedClasses=" + usedClasses +
                ", implementedInterfaces=" + implementedInterfaces +
                ", enums=" + enums +
                ", annotations=" + annotations +
                ", subPackages=" + subPackages +
                '}';
    }
}
