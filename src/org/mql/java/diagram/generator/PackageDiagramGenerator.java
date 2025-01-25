package org.mql.java.diagram.generator;

import org.mql.java.diagram.models.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PackageDiagramGenerator {

    public void generatePackageDiagram(Map<String, List<ClassDescriptor>> groupedClasses, String outputPath) {
        StringBuilder dotContent = new StringBuilder();
        dotContent.append("digraph Packages {\n")
                  .append("    rankdir=LR;\n")
                  .append("    node [shape=box, style=filled, color=lightblue];\n");

        groupedClasses.forEach((packageName, classDescriptors) -> {
            dotContent.append(String.format("    subgraph cluster_%s {\n", packageName.replace('.', '_')))
                      .append(String.format("        label=\"%s\";\n", packageName));
            for (ClassDescriptor classDescriptor : classDescriptors) {
                dotContent.append(String.format("        \"%s\";\n", classDescriptor.getClassName()));
                for (String usedClass : classDescriptor.getUsedClasses()) {
                    dotContent.append(String.format("    \"%s\" -> \"%s\";\n", classDescriptor.getClassName(), usedClass));
                }
            }
            dotContent.append("    }\n");
        });

        dotContent.append("}");

        try (FileWriter writer = new FileWriter(outputPath)) {
            writer.write(dotContent.toString());
            System.out.println("Package diagram successfully generated: " + outputPath);
        } catch (IOException e) {
            System.err.println("Error while writing the package diagram to " + outputPath);
            e.printStackTrace();
        }
    }
}
