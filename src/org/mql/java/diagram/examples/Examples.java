package org.mql.java.diagram.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.mql.java.diagram.generator.ProjectAnalyzer;
import org.mql.java.diagram.models.ClassDescriptor;
import org.mql.java.diagram.models.PackageDescriptor;
import org.mql.java.diagram.models.ProjectDescriptor;
import org.mql.java.diagram.ui.UMLDiagram;
import org.mql.java.diagram.ui.UMLPackageDiagram;
import org.mql.java.diagram.xml.XMIGenerator;
import org.mql.java.diagram.xml.XMLGenerator;

public class Examples {
	public Examples() {
		exp01();
	}

	public void exp01() {
		String rootPackage = "org.mql.java.diagram.examples"; 
        String outputPath = "resources/project_structure.xml"; 
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

        String outputPath = "resources/output.xmi"; 
        xmiGenerator.generateXMI(classDescriptors, outputPath);

        System.out.println("Le fichier XMI a été généré avec succès à l'emplacement : " + outputPath);
	}
	
/*	public void exp04() {
	    String rootPackage = "org.mql.java.diagram.examples";

	    try {
	        ProjectAnalyzer analyzer = new ProjectAnalyzer(rootPackage);

	        List<ClassDescriptor> classDescriptors = analyzer.findAllClasses();

	        Map<String, List<ClassDescriptor>> packagesMap = new HashMap<>();

	        for (ClassDescriptor classDescriptor : classDescriptors) {
	            String packageName = classDescriptor.getPackageName();
	            packagesMap.computeIfAbsent(packageName, k -> new ArrayList<>()).add(classDescriptor);
	        }

	        UMLDiagram umlDiagram = new UMLDiagram("UML Diagram of " + rootPackage, packagesMap);

	        JFrame frame = new JFrame("UML Diagram");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	        JScrollPane scrollPane = new JScrollPane(umlDiagram);
	        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	        frame.getContentPane().add(scrollPane);

	        frame.setSize(1200, 800); 
	        frame.setLocationRelativeTo(null); 
	        frame.setVisible(true);
	    } catch (Exception e) {
	        JOptionPane.showMessageDialog(null,
	            "An error occurred while generating the UML diagram:\n" + e.getMessage(),
	            "Error",
	            JOptionPane.ERROR_MESSAGE);
	        e.printStackTrace(); 
	    }
	}
*/

	public void exp05() {
		List<ClassDescriptor> classesPackageA = new ArrayList<>();
	    ClassDescriptor class1 = new ClassDescriptor("org.mql.java.diagram.examples.Person");
	    ClassDescriptor class2 = new ClassDescriptor("org.mql.java.diagram.examples.Employee");

	    classesPackageA.add(class1);
	    classesPackageA.add(class2);

	    List<ClassDescriptor> classesPackageB = new ArrayList<>();
	    ClassDescriptor class3 = new ClassDescriptor("org.mql.java.diagram.models.InterfaceDescriptor");
	    ClassDescriptor class4 = new ClassDescriptor("org.mql.java.diagram.models.EnumDescriptor");

	    classesPackageB.add(class4);
	    classesPackageB.add(class3);

	    PackageDescriptor packageA = new PackageDescriptor("org.mql.java.diagram.examples", classesPackageA);
	    PackageDescriptor packageB = new PackageDescriptor("org.mql.java.diagram.models", classesPackageB);

	    packageA.addUsedClass("org.mql.java.diagram.models.InterfaceDescriptor");
	    packageB.addUsedClass("org.mql.java.diagram.examples.Person");

	    List<PackageDescriptor> packages = new ArrayList<>();
	    packages.add(packageA);
	    packages.add(packageB);
        
        ProjectDescriptor projectDescriptor = new ProjectDescriptor(packages);

        JFrame frame = new JFrame("UML Package Diagram");
        UMLPackageDiagram umlPackageDiagram = new UMLPackageDiagram(projectDescriptor);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(umlPackageDiagram);
        frame.pack();
        frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		new Examples();
	}
	
	
}
