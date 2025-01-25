package org.mql.java.diagram.generator;

import org.mql.java.diagram.models.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

public class ProjectAnalyzer {

    private final String rootPackage;

    public ProjectAnalyzer(String rootPackage) {
        this.rootPackage = rootPackage;
    }

    public List<ClassDescriptor> findAllClasses() {
        return traverseProjectFiles().stream()
                .filter(file -> file.getName().endsWith(".java"))
                .map(file -> {
                    String fullClassName = generateFullClassName(file.toPath());  
                    System.out.println("Class found: " + fullClassName);  
                    return new ClassDescriptor(fullClassName);
                })
                .collect(Collectors.toList());
    }

    private void generateClassDiagram(String packageName) {
        ProjectAnalyzer analyzer = new ProjectAnalyzer("org.mql.java");
        PackageDescriptor packageDescriptor = analyzer.analyzePackage(packageName);

        if (packageDescriptor != null) {
            List<ClassDescriptor> classDescriptors = packageDescriptor.getClasses();
            System.out.println("Generating diagram for package: " + packageName);
            for (ClassDescriptor classDescriptor : classDescriptors) {
                System.out.println(classDescriptor); 
            }
        } else {
            JOptionPane.showMessageDialog(null, this, "No classes found in the package: " + packageName, 0);
        }
    }

    public ProjectDescriptor analyzeProject() {
        ProjectDescriptor projectDescriptor = new ProjectDescriptor();

        PackageDescriptor package1 = new PackageDescriptor("com.example.package1", List.of(
            new ClassDescriptor("ClassA"),
            new ClassDescriptor("ClassB")
        ));
        package1.addUsedClass("com.example.package2.ClassC");

        PackageDescriptor package2 = new PackageDescriptor("com.example.package2", List.of(
            new ClassDescriptor("ClassC"),
            new ClassDescriptor("ClassD")
        ));

        projectDescriptor.addPackage(package1);
        projectDescriptor.addPackage(package2);

        return projectDescriptor;
    }
    
    public List<String> getPackages() {
        List<String> packages = new ArrayList<>();
        Path projectRoot = Paths.get("src", rootPackage.replace('.', File.separatorChar));

        try {
            Files.walk(projectRoot)
                    .filter(Files::isDirectory)
                    .forEach(dir -> {
                        String packagePath = dir.toString().replace(File.separatorChar, '.');
                        if (packagePath.contains(rootPackage)) {
                            String packageName = packagePath.substring(packagePath.indexOf(rootPackage));
                            packages.add(packageName);
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return packages;
    }


    public PackageDescriptor analyzePackage(String packageName) {
        Path packagePath = resolvePackagePath(packageName);
        if (Files.exists(packagePath) && Files.isDirectory(packagePath)) {
            PackageDescriptor packageDescriptor = new PackageDescriptor(packageName, null);
            List<ClassDescriptor> classes = new ArrayList<>();
            List<InterfaceDescriptor> interfaces = new ArrayList<>();
            List<AnnotationDescriptor> annotations = new ArrayList<>();
            List<EnumDescriptor> enums = new ArrayList<>();
            List<PackageDescriptor> subPackages = new ArrayList<>();

            try {
                Files.walk(packagePath)
                        .filter(Files::isRegularFile)
                        .filter(file -> file.toString().endsWith(".java"))
                        .forEach(file -> {
                            String className = generateFullClassName(file);
                            String classType = determineType(className);
                            switch (classType) {
                                case "class":
                                    classes.add(new ClassDescriptor(className));
                                    break;
                                case "interface":
                                    interfaces.add(new InterfaceDescriptor(className));
                                    break;
                                case "enum":
                                    enums.add(new EnumDescriptor(className));
                                    break;
                                case "annotation":
                                    annotations.add(new AnnotationDescriptor(className));
                                    break;
                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }

            packageDescriptor.setClasses(classes);
            packageDescriptor.setInterfaces(interfaces);
            packageDescriptor.setAnnotations(annotations);
            packageDescriptor.setEnums(enums);
            packageDescriptor.setSubPackages(subPackages);

            return packageDescriptor;
        }

        return null;
    }

    public PackageDescriptor scan() {
        return analyzePackage(rootPackage);
    }

    private Path resolvePackagePath(String packageName) {
        return Paths.get("src", packageName.replace('.', File.separatorChar));
    }

    private List<File> traverseProjectFiles() {
        List<File> projectFiles = new ArrayList<>();
        Path projectRoot = Paths.get("src", rootPackage.replace('.', File.separatorChar));

        try {
            Files.walk(projectRoot)
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .filter(file -> file.getName().endsWith(".java"))  
                    .forEach(file -> {
                        System.out.println("Found file: " + file.getPath());  
                        projectFiles.add(file);
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return projectFiles;
    }

    private String generateFullClassName(Path file) {
        String packagePath = file.getParent().toString().replace(File.separatorChar, '.');
        if (packagePath.startsWith(rootPackage.replace('.', File.separatorChar))) {
            packagePath = packagePath.substring(packagePath.indexOf(rootPackage.replace('.', File.separatorChar)));
        }
        String fullClassName = packagePath + "." + file.getFileName().toString().replace(".java", "");

        if (fullClassName.startsWith("src.")) {
            fullClassName = fullClassName.substring(4);
        }
        return fullClassName;
    }

    private String determineType(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            if (clazz.isEnum()) {
                return "enum";
            } else if (clazz.isInterface()) {
                return "interface";
            } else if (clazz.isAnnotation()) {
                return "annotation";
            } else {
                return "class";
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String derivePackageName(File file) {
        String packagePath = file.getParentFile().getPath().replace(File.separatorChar, '.');
        String relativePath = packagePath.substring(packagePath.indexOf(rootPackage));
        return relativePath;
    }
}