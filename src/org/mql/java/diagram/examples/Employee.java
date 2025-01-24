package org.mql.java.diagram.examples;

public class Employee implements Person {
    private String employeeId;
    private double salary;
    private EmployeeType employeeType;

    public Employee(String employeeId, double salary) {
        this.employeeId = employeeId;
        this.salary = salary;
    }

    
    public Employee(String employeeId, double salary, EmployeeType employeeType) {
        this.employeeId = employeeId;
        this.salary = salary;
        this.employeeType = employeeType;
    }

    @Override
    public String getName() {
        return "Employee " + employeeId;
    }

    @Override
    public int getAge() {
        return 30; 
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public double getSalary() {
        return salary;
    }

    public EmployeeType getEmployeeType() {
        return employeeType;
    }
}
