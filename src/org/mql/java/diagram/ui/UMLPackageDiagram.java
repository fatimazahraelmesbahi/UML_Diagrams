package org.mql.java.diagram.ui;

import org.mql.java.diagram.models.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class UMLPackageDiagram extends JPanel {

    private static final long serialVersionUID = 1L;
    private ProjectDescriptor projectDescriptor; // L'architecture globale du projet
    private Map<String, Point> packagePositions; // Pour le positionnement des packages

    public UMLPackageDiagram(ProjectDescriptor projectDescriptor) {
        this.projectDescriptor = projectDescriptor;
        this.packagePositions = new HashMap<>();
        setPreferredSize(new Dimension(1200, 800));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        layoutPackages(graphics);
        drawPackageRelationships(graphics);
    }

    private void layoutPackages(Graphics2D graphics) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(getWidth(), getHeight()) / 3;

        int numPackages = projectDescriptor.getPackages().size();
        double angleStep = 2 * Math.PI / numPackages;

        int i = 0;
        for (PackageDescriptor packageDescriptor : projectDescriptor.getPackages()) {
            String packageName = packageDescriptor.getPackageName();
            List<ClassDescriptor> classes = new ArrayList<>();
            for (String className : packageDescriptor.getClassNames()) {
                // Créer des ClassDescriptors ou les récupérer
                classes.add(new ClassDescriptor(className));
            }

            double angle = i * angleStep;
            int x = (int) (centerX + radius * Math.cos(angle)) - 75;  // Ajustez la position pour le centrage
            int y = (int) (centerY + radius * Math.sin(angle)) - 50;  // Ajustez la position pour le centrage

            packagePositions.put(packageName, new Point(x, y));  // Enregistrer la position du package
            drawPackage(graphics, packageName, classes, new Point(x, y));
            i++;
        }
    }

    private void drawPackage(Graphics2D graphics, String packageName, List<ClassDescriptor> classes, Point position) {
        int width = 200;
        int height = 150;
        int x = position.x;
        int y = position.y;

        graphics.setColor(new Color(240, 240, 255)); // Couleur bleue claire pour le package
        graphics.fillRect(x, y, width, height);
        graphics.setColor(Color.BLACK);
        graphics.drawRect(x, y, width, height);

        graphics.setFont(new Font("SansSerif", Font.BOLD, 14));
        graphics.drawString(packageName, x + 10, y + 20);

        // Dessiner les classes à l'intérieur du package
        int classY = y + 30;
        for (ClassDescriptor classDescriptor : classes) {
            graphics.setFont(new Font("SansSerif", Font.PLAIN, 12));
            graphics.drawString(classDescriptor.getClassName(), x + 10, classY);
            classY += 20;
        }
    }

    private void drawPackageRelationships(Graphics2D graphics) {
        // Dessiner les relations entre les packages (en fonction de l'utilisation des classes)
        for (PackageDescriptor packageDescriptor : projectDescriptor.getPackages()) {
            String packageName = packageDescriptor.getPackageName();
            for (String usedClass : packageDescriptor.getUsedClasses()) {
                Point from = packagePositions.get(packageName);
                // Vous pouvez maintenant récupérer la position de l'autre package (ou classe)
                Point to = findPackagePositionByClassName(usedClass);
                if (from != null && to != null) {
                    drawRelationshipLine(graphics, from, to);
                }
            }
        }
    }

    private Point findPackagePositionByClassName(String className) {
        // Cette méthode doit retourner la position du package qui contient cette classe
        for (Map.Entry<String, Point> entry : packagePositions.entrySet()) {
            // Logique pour trouver le package contenant la classe
            if (className.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    private void drawRelationshipLine(Graphics2D graphics, Point from, Point to) {
        // Dessiner une ligne entre les packages pour représenter une relation
        graphics.setColor(Color.BLACK);
        graphics.drawLine(from.x + 100, from.y + 75, to.x + 100, to.y + 75); // Alignement au centre des boîtes
        drawArrow(graphics, from, to);  // Dessiner la flèche de relation
    }

    private void drawArrow(Graphics2D graphics, Point from, Point to) {
        // Calculer l'angle de la flèche
        int arrowSize = 10;
        double angle = Math.atan2(to.y - from.y, to.x - from.x);

        int x1 = to.x - (int) (arrowSize * Math.cos(angle - Math.PI / 6));
        int y1 = to.y - (int) (arrowSize * Math.sin(angle - Math.PI / 6));

        int x2 = to.x - (int) (arrowSize * Math.cos(angle + Math.PI / 6));
        int y2 = to.y - (int) (arrowSize * Math.sin(angle + Math.PI / 6));

        Polygon arrowHead = new Polygon(new int[]{to.x, x1, x2}, new int[]{to.y, y1, y2}, 3);
        graphics.fillPolygon(arrowHead);  // Remplir la flèche pour la rendre solide
    }
}
