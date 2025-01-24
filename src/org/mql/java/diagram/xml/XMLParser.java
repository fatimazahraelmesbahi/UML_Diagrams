package org.mql.java.diagram.xml;

import org.mql.java.diagram.models.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;
import java.io.File;
import java.util.*;

public class XMLParser {

    public ProjectDescriptor parse(String xmlFilePath) {
        ProjectDescriptor projectDescriptor = new ProjectDescriptor();
        try {
            File xmlFile = new File(xmlFilePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            // Parsing du projet
            NodeList packageNodes = document.getElementsByTagName("package");

            for (int i = 0; i < packageNodes.getLength(); i++) {
                Node packageNode = packageNodes.item(i);
                if (packageNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element packageElement = (Element) packageNode;
                    String packageName = packageElement.getAttribute("name");
                    PackageDescriptor packageDescriptor = new PackageDescriptor(packageName);

                    // Parsing des classes
                    NodeList classNodes = packageElement.getElementsByTagName("class");
                    for (int j = 0; j < classNodes.getLength(); j++) {
                        Node classNode = classNodes.item(j);
                        if (classNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element classElement = (Element) classNode;
                            String className = classElement.getAttribute("name");
                            packageDescriptor.addClass(new ClassDescriptor(className));
                        }
                    }

                    // Parsing des interfaces
                    NodeList interfaceNodes = packageElement.getElementsByTagName("interface");
                    for (int j = 0; j < interfaceNodes.getLength(); j++) {
                        Node interfaceNode = interfaceNodes.item(j);
                        if (interfaceNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element interfaceElement = (Element) interfaceNode;
                            String interfaceName = interfaceElement.getAttribute("name");
                            packageDescriptor.addInterface(new InterfaceDescriptor(interfaceName));
                        }
                    }

                    // Parsing des enums
                    NodeList enumNodes = packageElement.getElementsByTagName("enum");
                    for (int j = 0; j < enumNodes.getLength(); j++) {
                        Node enumNode = enumNodes.item(j);
                        if (enumNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element enumElement = (Element) enumNode;
                            String enumName = enumElement.getAttribute("name");
                            packageDescriptor.addEnum(new EnumDescriptor(enumName));
                        }
                    }

                    // Parsing des annotations
                    NodeList annotationNodes = packageElement.getElementsByTagName("annotation");
                    for (int j = 0; j < annotationNodes.getLength(); j++) {
                        Node annotationNode = annotationNodes.item(j);
                        if (annotationNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element annotationElement = (Element) annotationNode;
                            String annotationName = annotationElement.getAttribute("name");
                            packageDescriptor.addAnnotation(new AnnotationDescriptor(annotationName));
                        }
                    }

                    // Ajouter le package à la structure du projet
                    projectDescriptor.addPackage(packageDescriptor);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return projectDescriptor;
    }
}
