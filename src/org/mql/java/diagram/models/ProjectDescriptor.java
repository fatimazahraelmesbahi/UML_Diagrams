package org.mql.java.diagram.models;

import java.util.ArrayList;
import java.util.List;

public class ProjectDescriptor {
    private List<PackageDescriptor> packages;

    public ProjectDescriptor() {
        this.packages = new ArrayList<>();
    }

    public void addPackage(PackageDescriptor packageDescriptor) {
        this.packages.add(packageDescriptor);
    }

    public List<PackageDescriptor> getPackages() {
        return packages;
    }

    public void display() {
        for (PackageDescriptor packageDescriptor : packages) {
            System.out.println("Package: " + packageDescriptor.getPackageName());
            packageDescriptor.display();
        }
    }
}
