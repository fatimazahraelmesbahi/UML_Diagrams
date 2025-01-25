package org.mql.java.diagram.ui;

import org.mql.java.diagram.models.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UMLDiagram extends JPanel {

    private static final long serialVersionUID = 1L;
    private List<ClassDescriptor> umlClasses;
    private Map<String, Point> classPositionMap;
    private JComboBox<String> packageComboBox; 
    private Map<String, List<ClassDescriptor>> packagesMap; 
	private String title;

	public UMLDiagram(String title, List<ClassDescriptor> classDescriptors) {
	    this.title = title;
	    this.umlClasses = classDescriptors;
	    this.classPositionMap = new HashMap<>();
	    setPreferredSize(new Dimension(1000, 1000));
	    setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}

    
    
    
    public UMLDiagram(String title, Map<String, List<ClassDescriptor>> packagesMap) {
        this.packagesMap = packagesMap;
        this.classPositionMap = new HashMap<>();
        setPreferredSize(new Dimension(1000, 1000));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel headerLabel = new JLabel(title, SwingConstants.CENTER);
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        headerPanel.add(headerLabel, BorderLayout.NORTH);

        packageComboBox = new JComboBox<>(packagesMap.keySet().toArray(new String[0]));
        packageComboBox.addActionListener(new PackageSelectionHandler());
        headerPanel.add(packageComboBox, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);

        String firstPackage = packagesMap.keySet().iterator().next();
        this.umlClasses = packagesMap.get(firstPackage);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (umlClasses == null || umlClasses.isEmpty()) {
            graphics.drawString("No classes to display", getWidth() / 2 - 50, getHeight() / 2);
            return;
        }

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(getWidth(), getHeight()) / 3;

        int numClasses = umlClasses.size();
        double angleStep = 2 * Math.PI / numClasses;

        for (int i = 0; i < numClasses; i++) {
            double angle = i * angleStep;
            int x = (int) (centerX + radius * Math.cos(angle)) - 100;
            int y = (int) (centerY + radius * Math.sin(angle)) - 50;

            Point position = new Point(x, y);
            classPositionMap.put(umlClasses.get(i).getName(), position);
            drawClass(graphics, umlClasses.get(i), position);
        }

        drawConnections(graphics);
    }

    private void drawClass(Graphics2D graphics, ClassDescriptor umlClass, Point position) {
        int width = 150;
        int x = position.x, y = position.y;

        int titleHeight = 30;
        int fieldsHeight = drawFields(graphics, umlClass.getFields(), x, y + titleHeight, width);
        int methodsHeight = drawMethods(graphics, umlClass.getMethods(), x, y + titleHeight + fieldsHeight, width);
        int totalHeight = titleHeight + fieldsHeight + methodsHeight;

        graphics.setColor(new Color(70, 130, 180));
        graphics.fillRect(x, y, width, titleHeight);
        graphics.setColor(Color.BLACK);
        graphics.drawRect(x, y, width, totalHeight);

        graphics.setFont(new Font("SansSerif", Font.BOLD, 12));
        graphics.drawString(umlClass.getName(), x + 10, y + 20);

        graphics.drawLine(x, y + titleHeight, x + width, y + titleHeight);
        graphics.drawLine(x, y + titleHeight + fieldsHeight, x + width, y + titleHeight + fieldsHeight);
    }

    private int drawFields(Graphics2D g2, List<String> fields, int x, int y, int width) {
        int fieldHeight = fields.size() * 15;
        g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
        int currentY = y + 12;
        for (String field : fields) {
            g2.drawString(field, x + 5, currentY);
            currentY += 15;
        }
        return fieldHeight;
    }

    private int drawMethods(Graphics2D g2, List<String> methods, int x, int y, int width) {
        int methodHeight = methods.size() * 15;
        g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
        int currentY = y + 12;
        for (String method : methods) {
            g2.drawString(method, x + 5, currentY);
            currentY += 15;
        }
        return methodHeight;
    }

    private void drawConnections(Graphics2D graphics) {
        for (ClassDescriptor umlClass : umlClasses) {
            for (Relationship relationship : umlClass.getRelationships()) {
                Point from = classPositionMap.get(relationship.getFrom());
                Point to = classPositionMap.get(relationship.getTo());
                if (from != null && to != null) {
                    drawRelationshipLine(graphics, from, to, relationship.getRelationshipType());
                }
            }
        }
    }

    private void drawRelationshipLine(Graphics2D graphics, Point from, Point to, String relationshipType) {
        int width = 150;
        int height = 80;

        Point start = calculateConnectionPoint(from, to, width, height);
        Point end = calculateConnectionPoint(to, from, width, height);

        graphics.drawLine(start.x, start.y, end.x, end.y);

        if (relationshipType.equals("Inheritance")) {
            drawArrow(graphics, start, end);
        } else if (relationshipType.equals("Aggregation")) {
            drawDiamond(graphics, start, end, false);
        } else if (relationshipType.equals("Composition")) {
            drawDiamond(graphics, start, end, true);
        } else if (relationshipType.equals("Implementation")) {
            drawDashedLine(graphics, start, end);
            drawHollowArrow(graphics, start, end);
        }
    }

    private Point calculateConnectionPoint(Point source, Point target, int width, int height) {
        int x = source.x + width / 2;
        int y = source.y + height / 2;

        if (source.y + height < target.y) {
            y = source.y + height;
        } else if (source.y > target.y + height) {
            y = source.y;
        }

        if (source.x + width < target.x) {
            x = source.x + width;
        } else if (source.x > target.x + width) {
            x = source.x;
        }

        return new Point(x, y);
    }

    private void drawDiamond(Graphics2D graphics, Point from, Point to, boolean filled) {
        int diamondSize = 10;

        double dx = to.x - from.x;
        double dy = to.y - from.y;
        double length = Math.sqrt(dx * dx + dy * dy);

        double unitDx = dx / length;
        double unitDy = dy / length;

        int offsetX = (int) (unitDx * diamondSize);
        int offsetY = (int) (unitDy * diamondSize);

        int centerX = from.x + offsetX;
        int centerY = from.y + offsetY;

        int topX = centerX;
        int topY = centerY - diamondSize;

        int leftX = centerX - diamondSize;
        int leftY = centerY;

        int bottomX = centerX;
        int bottomY = centerY + diamondSize;

        int rightX = centerX + diamondSize;
        int rightY = centerY;

        Polygon diamond = new Polygon(
            new int[]{topX, leftX, bottomX, rightX},
            new int[]{topY, leftY, bottomY, rightY},
            4
        );

        double angle = Math.atan2(dy, dx);
        AffineTransform transform = new AffineTransform();
        transform.setToRotation(angle, centerX, centerY);

        Shape rotatedDiamond = transform.createTransformedShape(diamond);

        if (filled) {
            graphics.setColor(Color.BLACK);
            graphics.fill(rotatedDiamond);
        } else {
            graphics.setColor(Color.BLACK);
            graphics.fill(rotatedDiamond);
        }

        graphics.setColor(Color.BLACK);
        graphics.draw(rotatedDiamond);
    }

    private void drawDashedLine(Graphics2D graphics, Point start, Point end) {
        float[] dashPattern = {10.0f, 10.0f};
        Stroke originalStroke = graphics.getStroke();
        graphics.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dashPattern, 0.0f));
        graphics.drawLine(start.x, start.y, end.x, end.y);
        graphics.setStroke(originalStroke);
    }

    private void drawArrow(Graphics2D graphics, Point start, Point end) {
        int arrowSize = 20;
        double angle = Math.atan2(end.y - start.y, end.x - start.x);

        int x1 = end.x - (int) (arrowSize * Math.cos(angle - Math.PI / 6));
        int y1 = end.y - (int) (arrowSize * Math.sin(angle - Math.PI / 6));

        int x2 = end.x - (int) (arrowSize * Math.cos(angle + Math.PI / 6));
        int y2 = end.y - (int) (arrowSize * Math.sin(angle + Math.PI / 6));

        Polygon arrowHead = new Polygon(
            new int[]{end.x, x1, x2},
            new int[]{end.y, y1, y2},
            3
        );

        graphics.fillPolygon(arrowHead);
    }

    private void drawHollowArrow(Graphics2D graphics, Point start, Point end) {
        int arrowSize = 10;
        double angle = Math.atan2(end.y - start.y, end.x - start.x);

        int x1 = end.x - (int) (arrowSize * Math.cos(angle - Math.PI / 6));
        int y1 = end.y - (int) (arrowSize * Math.sin(angle - Math.PI / 6));

        int x2 = end.x - (int) (arrowSize * Math.cos(angle + Math.PI / 6));
        int y2 = end.y - (int) (arrowSize * Math.sin(angle + Math.PI / 6));

        graphics.setColor(Color.BLACK);
        graphics.drawLine(end.x, end.y, x1, y1);
        graphics.drawLine(end.x, end.y, x2, y2);
    }
   
    private class PackageSelectionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedPackage = (String) packageComboBox.getSelectedItem();
            if (selectedPackage != null) {
                umlClasses = packagesMap.get(selectedPackage);
                repaint(); 
            }
        }
    }
}