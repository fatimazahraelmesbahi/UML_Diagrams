package org.mql.java.diagram.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import org.mql.java.diagram.generator.ProjectAnalyzer;
import org.mql.java.diagram.models.ClassDescriptor;

public class UMLClassDiagramUI extends JFrame {
    private JComboBox<String> packageComboBox;
    private JButton generateButton;

    public UMLClassDiagramUI(List<String> packages) {
        setTitle("Package Explorer");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ComboBox for packages
        packageComboBox = new JComboBox<>(packages.toArray(new String[0]));
        packageComboBox.setPreferredSize(new Dimension(300, 30));
        add(packageComboBox, BorderLayout.NORTH);

        // Button to generate class diagram
        generateButton = new JButton("Generate Class Diagram");
        add(generateButton, BorderLayout.SOUTH);

        generateButton.addActionListener(e -> {
            String selectedPackage = (String) packageComboBox.getSelectedItem();
            if (selectedPackage != null) {
                generateClassDiagram(selectedPackage);
            }
        });
    }

    private void generateClassDiagram(String packageName) {
        try {
            ProjectAnalyzer analyzer = new ProjectAnalyzer(packageName);

            List<ClassDescriptor> classDescriptors = analyzer.findAllClasses();

            UMLDiagram umlDiagram = new UMLDiagram("UML Diagram for " + packageName, classDescriptors);

            JFrame diagramFrame = new JFrame("Class Diagram");
            diagramFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            diagramFrame.getContentPane().add(new JScrollPane(umlDiagram));
            diagramFrame.setSize(1200, 800); 
            diagramFrame.setLocationRelativeTo(null);
            diagramFrame.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        ProjectAnalyzer analyzer = new ProjectAnalyzer("org.mql.java");
        List<String> packages = analyzer.getPackages();

        SwingUtilities.invokeLater(() -> {
            UMLClassDiagramUI ui = new UMLClassDiagramUI(packages);
            ui.setVisible(true);
        });
    }
}
