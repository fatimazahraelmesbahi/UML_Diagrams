package org.mql.java.diagram.examples;

public class Manager extends Employee {
    private double bonus;

    public Manager(String employeeId, double salary, double bonus) {
        super(employeeId, salary);
        this.bonus = bonus;
    }

    public double getBonus() {
        return bonus;
    }

    @Override
    public String getEmployeeId() {
        return super.getEmployeeId() + " (Manager)"; 
    }

    @Override
    public double getSalary() {
        return super.getSalary() + bonus;  
    }

    @Override
    public String toString() {
        return "Manager{" +
                "employeeId='" + getEmployeeId() + '\'' +
                ", salary=" + getSalary() +
                ", bonus=" + bonus +
                '}';
    }
}
