package org.mql.java.diagram.generator;

import org.mql.java.diagram.models.ClassDescriptor;
import org.mql.java.diagram.models.Relationship;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.List;

public class XMIGenerator {

    public void generateXMI(List<ClassDescriptor> classDescriptors, String outputPath) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element rootElement = document.createElement("XMI");
            document.appendChild(rootElement);

            for (ClassDescriptor classDescriptor : classDescriptors) {
                Element classElement = document.createElement("Class");
                classElement.setAttribute("name", classDescriptor.getName());
                classElement.setAttribute("accessModifiers", classDescriptor.getAccessModifiers());

                if (classDescriptor.getParentClass() != null) {
                    Element parentElement = document.createElement("ParentClass");
                    parentElement.setTextContent(classDescriptor.getParentClass());
                    classElement.appendChild(parentElement);
                }

                Element fieldsElement = document.createElement("Fields");
                for (String field : classDescriptor.getFields()) {
                    Element fieldElement = document.createElement("Field");
                    fieldElement.setTextContent(field);
                    fieldsElement.appendChild(fieldElement);
                }
                classElement.appendChild(fieldsElement);

                Element methodsElement = document.createElement("Methods");
                for (String method : classDescriptor.getMethods()) {
                    Element methodElement = document.createElement("Method");
                    methodElement.setTextContent(method);
                    methodsElement.appendChild(methodElement);
                }
                classElement.appendChild(methodsElement);

                Element relationshipsElement = document.createElement("Relationships");
                for (Relationship relationship : classDescriptor.getRelationships()) {
                    Element relationshipElement = document.createElement("Relationship");
                    relationshipElement.setAttribute("type", relationship.getRelationshipType());
                    relationshipElement.setAttribute("to", relationship.getTo());
                    relationshipsElement.appendChild(relationshipElement);
                }
                classElement.appendChild(relationshipsElement);

                rootElement.appendChild(classElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(outputPath));
            transformer.transform(domSource, streamResult);

            System.out.println("Fichier XMI généré : " + outputPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
