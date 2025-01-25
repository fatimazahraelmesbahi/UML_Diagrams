package org.mql.java.diagram.models;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DiagramGeneratorGUI {
    private JFrame frame;
    private JComboBox<String> projectComboBox;
    private JButton generateButton;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                DiagramGeneratorGUI window = new DiagramGeneratorGUI();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public DiagramGeneratorGUI() {
        initialize();
    }

    private void initialize() {
        // Frame
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new FlowLayout());

        // Project ComboBox
        String[] projects = {"Projet 1", "Projet 2", "Projet 3", "Package A", "Package B"};
        projectComboBox = new JComboBox<>(projects);
        frame.getContentPane().add(projectComboBox);

        // Generate Button
        generateButton = new JButton("Générer Diagramme");
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedProject = (String) projectComboBox.getSelectedItem();
                generateDiagram(selectedProject);
            }
        });
        frame.getContentPane().add(generateButton);
    }

    private void generateDiagram(String selectedProject) {
        // Vous pouvez ajouter la logique pour générer le diagramme ici
        JOptionPane.showMessageDialog(frame, "Génération du diagramme pour : " + selectedProject);
    }
}
