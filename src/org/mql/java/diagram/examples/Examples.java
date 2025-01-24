package org.mql.java.diagram.examples;

import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;

import org.mql.java.diagram.generator.ProjectAnalyzer;
import org.mql.java.diagram.generator.XMIGenerator;
import org.mql.java.diagram.generator.XMLGenerator;
import org.mql.java.diagram.models.ClassDescriptor;
import org.mql.java.diagram.ui.UMLDiagram;

public class Examples {
	public Examples() {
		exp04();
	}

	public void exp01() {
		String rootPackage = "org.mql.java.diagram.examples"; 
        String outputPath = "resources/project_structure3.xml"; 
        ProjectAnalyzer analyzer = new ProjectAnalyzer(rootPackage);
        List<ClassDescriptor> classDescriptors = analyzer.findAllClasses();

        XMLGenerator xmlGenerator = new XMLGenerator();
        xmlGenerator.generateXML(classDescriptors, outputPath);
	}
	
	public void exp02() {
		 Employee employee = new Employee("E001", 50000.0, EmployeeType.FULL_TIME);

	        System.out.println("Employee ID: " + employee.getEmployeeId());
	        System.out.println("Salary: " + employee.getSalary());
	        System.out.println("Employee Type: " + employee.getEmployeeType());
	        System.out.println("Name: " + employee.getName());
	        System.out.println("Age: " + employee.getAge());
	}

	public void exp03() {
        ClassDescriptor class1 = new ClassDescriptor("org.mql.java.diagram.examples.Person");
        ClassDescriptor class2 = new ClassDescriptor("org.mql.java.diagram.examples.Employee");
        
        List<ClassDescriptor> classDescriptors = Arrays.asList(class1, class2);

        XMIGenerator xmiGenerator = new XMIGenerator();

        String outputPath = "resources/output4.xmi"; 
        xmiGenerator.generateXMI(classDescriptors, outputPath);

        System.out.println("Le fichier XMI a été généré avec succès à l'emplacement : " + outputPath);
	}
	
	public void exp04() {
		String rootPackage = "org.mql.java.diagram.examples";

        ProjectAnalyzer analyzer = new ProjectAnalyzer(rootPackage);
        List<ClassDescriptor> classDescriptors = analyzer.findAllClasses(); 

        JFrame frame = new JFrame("UML Diagram");
        UMLDiagram umlDiagram = new UMLDiagram("UML Diagram of Project", classDescriptors);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(umlDiagram);
        frame.pack();
        frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		new Examples();
	}
	
	
}
