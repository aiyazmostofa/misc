// author: Aiyaz Mostofa
// WARNING: JAVA 13+ ONLY - USES ENHANCED SWITCH CASE

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;

public class PaintEditor extends JPanel {
    // holds the current configuration and instance panels
    private Color selectedColor;
    private int selectedStrokeSize;
    private int selectedType;
    private int selectedCap;
    private int selectedJoin;
    private int lastIndex;
    private boolean selectedIfFilled, selectedIfStroke;
    private final PaintCanvas paintCanvas;
    private final ToolMenu toolMenu;

    // init config; adds the instance panels and key-strokes
    public PaintEditor() {
        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY);

        selectedType = Element.PATH;
        selectedColor = Color.BLACK;
        selectedStrokeSize = 5;
        selectedCap = BasicStroke.CAP_ROUND;
        selectedJoin = BasicStroke.JOIN_ROUND;
        selectedIfFilled = false;
        selectedIfStroke = true;
        lastIndex = -1;
        paintCanvas = new PaintCanvas(600, 400);
        add(paintCanvas, BorderLayout.CENTER);
        toolMenu = new ToolMenu();
        add(toolMenu, BorderLayout.SOUTH);

        Action undo = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (lastIndex > -1) {
                    lastIndex--;
                    paintCanvas.repaint();
                }
            }
        };
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_DOWN_MASK), "undo");
        getActionMap().put("undo", undo);

        Action redo = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (lastIndex < paintCanvas.elements.size() - 1) {
                    lastIndex++;
                    paintCanvas.repaint();
                }
            }
        };
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('Y', InputEvent.CTRL_DOWN_MASK), "redo");
        getActionMap().put("redo", redo);
    }

    // main methods hold the frame
    public static void main(String[] args) {
        JFrame frame = new JFrame("Paint Editor");
        PaintEditor paintEditor = new PaintEditor();
        frame.add(paintEditor);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // generate stroke based on config
    public Stroke generateStroke() {
        return new BasicStroke(selectedStrokeSize, selectedCap, selectedJoin);
    }

    // generate element based on config
    public Element generateElement() {
        return new Element(
                selectedType,
                selectedColor,
                generateStroke(),
                selectedIfFilled,
                selectedIfStroke);
    }

    // holds the main canvas
    private class PaintCanvas extends JPanel implements MouseMotionListener, MouseListener {
        private final ArrayList<Element> elements;
        private Element element;
        private double pivotX, pivotY;

        // init variables; adds mouse listeners
        public PaintCanvas(int width, int height) {
            setPreferredSize(new Dimension(width, height));
            setBackground(Color.WHITE);
            addMouseListener(this);
            addMouseMotionListener(this);
            elements = new ArrayList<>();
            element = generateElement();
        }

        // draws all saved elements
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            for (int i = 0; i <= lastIndex; i++) {
                Element element = elements.get(i);
                g2.setColor(element.color);
                g2.setStroke(element.stroke);
                if (element.ifFilled) g2.fill(element.shape);
                if (element.ifStroke) g2.draw(element.shape);
            }
            g2.setColor(element.color);
            g2.setStroke(element.stroke);
            if (element.ifFilled) g2.fill(element.shape);
            if (element.ifStroke) g2.draw(element.shape);
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        // saves the finished element and generates new one
        @Override
        public void mouseReleased(MouseEvent e) {
            while (lastIndex != elements.size() - 1) elements.remove(elements.size() - 1);
            elements.add(element);
            lastIndex++;
            element = generateElement();
            mouseMoved(e);
            repaint();
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        // adds to the unfinished element
        @Override
        public void mouseDragged(MouseEvent e) {
            switch (selectedType) {
                case Element.PATH -> ((Path2D) element.shape).lineTo(e.getX(), e.getY());
                case Element.RECTANGLE -> ((Rectangle2D.Double) element.shape).setRect(
                        e.getX() < pivotX ? e.getX() : pivotX,
                        e.getY() < pivotY ? e.getY() : pivotY,
                        Math.abs(e.getX() - pivotX),
                        Math.abs(e.getY() - pivotY)
                );
                case Element.ELLIPSE -> ((Ellipse2D.Double) element.shape).setFrame(
                        e.getX() < pivotX ? e.getX() : pivotX,
                        e.getY() < pivotY ? e.getY() : pivotY,
                        Math.abs(e.getX() - pivotX),
                        Math.abs(e.getY() - pivotY)
                );
                case Element.ROUNDED_RECTANGLE -> ((RoundRectangle2D.Double) element.shape).setRoundRect(
                        e.getX() < pivotX ? e.getX() : pivotX,
                        e.getY() < pivotY ? e.getY() : pivotY,
                        Math.abs(e.getX() - pivotX),
                        Math.abs(e.getY() - pivotY),
                        30d,
                        30d
                );
                case Element.LINE -> ((Line2D.Double) element.shape).setLine(pivotX, pivotY, e.getX(), e.getY());
                case Element.CIRCLE -> ((Ellipse2D.Double) element.shape).setFrame(
                        e.getX() < pivotX ? e.getX() : pivotX,
                        e.getY() < pivotY ? e.getY() : pivotY,
                        Math.min(Math.abs(e.getX() - pivotX), Math.abs(e.getY() - pivotY)),
                        Math.min(Math.abs(e.getX() - pivotX), Math.abs(e.getY() - pivotY))
                );
            }
            repaint();
        }

        // keeps track of mouse
        @Override
        public void mouseMoved(MouseEvent e) {
            switch (selectedType) {
                case Element.PATH -> ((Path2D) element.shape).moveTo(e.getX(), e.getY());
                case Element.CIRCLE, Element.LINE, Element.ELLIPSE, Element.ROUNDED_RECTANGLE, Element.RECTANGLE -> {
                    pivotX = e.getX();
                    pivotY = e.getY();
                }
            }
        }
    }

    // holds the menu bar
    private class ToolMenu extends JPanel {
        private final ColorSelector colorSelector;
        private final OpacitySelector opacitySelector;
        private final StrokeSelector strokeSelector;
        private final ShapeSelector shapeSelector;

        // init the sub menus
        public ToolMenu() {
            setBackground(Color.DARK_GRAY);
            colorSelector = new ColorSelector();
            add(colorSelector);

            JPanel opacityAndStroke = new JPanel();
            opacityAndStroke.setLayout(new BorderLayout());
            opacitySelector = new OpacitySelector();
            opacityAndStroke.add(opacitySelector, BorderLayout.NORTH);
            strokeSelector = new StrokeSelector();
            opacityAndStroke.add(strokeSelector, BorderLayout.SOUTH);
            add(opacityAndStroke);

            shapeSelector = new ShapeSelector();
            add(shapeSelector);
        }

        // the color submenu; has display, sliders, and spinners
        private class ColorSelector extends JPanel {
            private final JPanel colorDisplay;
            private final JPanel colorChangers;
            private JSlider redSlider, greenSlider, blueSlider;
            private JSpinner redSpinner, greenSpinner, blueSpinner;

            // init instance; adds sliders, spinners, manages actions
            public ColorSelector() {
                setBackground(Color.DARK_GRAY);
                TitledBorder colorSelectorBorder = new TitledBorder("Color Selector");
                colorSelectorBorder.setTitleColor(Color.LIGHT_GRAY);
                setBorder(colorSelectorBorder);

                ChangeListener changeSlider = e -> {
                    selectedColor = new Color(
                            redSlider.getValue(),
                            greenSlider.getValue(),
                            blueSlider.getValue(),
                            selectedColor.getAlpha()
                    );
                    updateColor();
                };

                ChangeListener changeSpinner = e -> {
                    selectedColor = new Color(
                            (int) redSpinner.getValue(),
                            (int) greenSpinner.getValue(),
                            (int) blueSpinner.getValue(),
                            selectedColor.getAlpha()
                    );
                    updateColor();
                };

                colorDisplay = new JPanel();
                colorDisplay.setPreferredSize(new Dimension(100, 100));
                colorDisplay.setBackground(selectedColor);
                add(colorDisplay);

                colorChangers = new JPanel();
                colorChangers.setLayout(new BoxLayout(colorChangers, BoxLayout.PAGE_AXIS));

                JPanel red = new JPanel();
                red.setBackground(Color.RED);
                redSlider = new JSlider(0, 255, selectedColor.getRed());
                redSlider.setBackground(Color.DARK_GRAY);
                redSlider.addChangeListener(changeSlider);
                red.add(redSlider);
                redSpinner = new JSpinner(new SpinnerNumberModel(selectedColor.getRed(), 0, 255, 1));
                redSpinner.addChangeListener(changeSpinner);
                red.add(redSpinner);
                colorChangers.add(red);

                JPanel green = new JPanel();
                green.setBackground(Color.GREEN);
                greenSlider = new JSlider(0, 255, selectedColor.getGreen());
                greenSlider.setBackground(Color.DARK_GRAY);
                greenSlider.addChangeListener(changeSlider);
                green.add(greenSlider);
                greenSpinner = new JSpinner(new SpinnerNumberModel(selectedColor.getGreen(), 0, 255, 1));
                greenSpinner.addChangeListener(changeSpinner);
                green.add(greenSpinner);
                colorChangers.add(green);

                JPanel blue = new JPanel();
                blue.setBackground(Color.BLUE);
                blueSlider = new JSlider(0, 255, selectedColor.getBlue());
                blueSlider.setBackground(Color.DARK_GRAY);
                blueSlider.addChangeListener(changeSlider);
                blue.add(blueSlider);
                blueSpinner = new JSpinner(new SpinnerNumberModel(selectedColor.getBlue(), 0, 255, 1));
                blueSpinner.addChangeListener(changeSpinner);
                blue.add(blueSpinner);
                colorChangers.add(blue);

                add(colorChangers);
            }

            // updates color program-wide
            public void updateColor() {
                colorDisplay.setBackground(new Color(
                        selectedColor.getRed(),
                        selectedColor.getGreen(),
                        selectedColor.getBlue())
                );
                redSlider.setValue(selectedColor.getRed());
                greenSlider.setValue(selectedColor.getGreen());
                blueSlider.setValue(selectedColor.getBlue());
                redSpinner.setValue(selectedColor.getRed());
                greenSpinner.setValue(selectedColor.getGreen());
                blueSpinner.setValue(selectedColor.getBlue());
                paintCanvas.element.color = selectedColor;
            }
        }

        // opacity submenu; has slider and spinner
        private class OpacitySelector extends JPanel {
            private final JSlider opacitySlider;
            private final JSpinner opacitySpinner;
            private final JLabel opacityLabel;

            // init instance; adds slider and spinner, manages actions
            public OpacitySelector() {
                setBackground(Color.DARK_GRAY);
                opacitySlider = new JSlider(0, 255, 255);
                opacitySlider.setBackground(Color.DARK_GRAY);
                opacitySpinner = new JSpinner(new SpinnerNumberModel(selectedColor.getAlpha(), 0, 255, 1));

                opacitySlider.addChangeListener(e -> {
                    selectedColor = new Color(
                            selectedColor.getRed(),
                            selectedColor.getGreen(),
                            selectedColor.getBlue(),
                            opacitySlider.getValue()
                    );
                    opacitySpinner.setValue(selectedColor.getAlpha());
                    colorSelector.updateColor();
                });

                opacitySpinner.addChangeListener(e -> {
                    selectedColor = new Color(
                            selectedColor.getRed(),
                            selectedColor.getGreen(),
                            selectedColor.getBlue(),
                            (int) opacitySpinner.getValue()
                    );
                    opacitySlider.setValue(selectedColor.getAlpha());
                    colorSelector.updateColor();
                });

                opacityLabel = new JLabel("Opacity");
                opacityLabel.setForeground(Color.LIGHT_GRAY);
                add(opacityLabel);
                add(opacitySlider);
                add(opacitySpinner);
            }
        }

        // stroke submenu; has slider and spinner and radio buttons
        private class StrokeSelector extends JPanel {
            private final JSlider strokeSizeSlider;
            private JSpinner strokeSizeSpinner;
            private final JLabel strokeSizeLabel;
            private final JLabel joinLabel;
            private final JLabel capLabel;
            private final ButtonGroup joinSelector;
            private final ButtonGroup capSelector;
            private final JRadioButton joinBevel;
            private final JRadioButton joinMiter;
            private final JRadioButton joinRound;
            private final JRadioButton capButt;
            private final JRadioButton capRound;
            private final JRadioButton capSquare;
            private final JCheckBox fillButton;
            private final JCheckBox strokeButton;

            // init instance; adds slider, spinner, radio buttons, labels, manages actions
            public StrokeSelector() {
                setBackground(Color.DARK_GRAY);
                TitledBorder strokeSelectorBorder = new TitledBorder("Stroke Selector");
                strokeSelectorBorder.setTitleColor(Color.LIGHT_GRAY);
                setBorder(strokeSelectorBorder);
                setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
                JPanel strokeSize = new JPanel();
                strokeSize.setBackground(Color.DARK_GRAY);
                strokeSizeSlider = new JSlider(1, 50, selectedStrokeSize);
                strokeSizeSlider.setBackground(Color.DARK_GRAY);
                strokeSizeSlider.addChangeListener(e -> {
                    selectedStrokeSize = strokeSizeSlider.getValue();
                    strokeSizeSpinner.setValue(selectedStrokeSize);
                    paintCanvas.element.stroke = generateStroke();
                });
                strokeSizeSpinner = new JSpinner(new SpinnerNumberModel(selectedStrokeSize, 1, 50, 1));
                strokeSizeSpinner.addChangeListener(e -> {
                    selectedStrokeSize = (int) strokeSizeSpinner.getValue();
                    strokeSizeSlider.setValue(selectedStrokeSize);
                    paintCanvas.element.stroke = generateStroke();
                });
                strokeSizeLabel = new JLabel("Size");
                strokeSizeLabel.setForeground(Color.LIGHT_GRAY);
                strokeSize.add(strokeSizeLabel);
                strokeSize.add(strokeSizeSlider);
                strokeSize.add(strokeSizeSpinner);
                add(strokeSize);

                JPanel join = new JPanel();
                join.setBackground(Color.DARK_GRAY);
                joinSelector = new ButtonGroup();
                ActionListener joinAction = e -> {
                    switch (((JRadioButton) e.getSource()).getActionCommand()) {
                        case "Miter" -> selectedJoin = BasicStroke.JOIN_MITER;
                        case "Round" -> selectedJoin = BasicStroke.JOIN_ROUND;
                        case "Bevel" -> selectedJoin = BasicStroke.JOIN_BEVEL;
                    }
                    paintCanvas.element.stroke = generateStroke();
                };
                joinLabel = new JLabel("Join Type");
                joinLabel.setForeground(Color.LIGHT_GRAY);
                join.add(joinLabel);

                joinMiter = new JRadioButton();
                joinMiter.setForeground(Color.LIGHT_GRAY);
                joinMiter.setBackground(Color.DARK_GRAY);
                joinMiter.setActionCommand("Miter");
                joinMiter.setText("Miter");
                joinMiter.addActionListener(joinAction);
                joinSelector.add(joinMiter);
                join.add(joinMiter);

                joinRound = new JRadioButton();
                joinRound.setSelected(true);
                joinRound.setForeground(Color.LIGHT_GRAY);
                joinRound.setBackground(Color.DARK_GRAY);
                joinRound.setActionCommand("Round");
                joinRound.setText("Round");
                joinRound.addActionListener(joinAction);
                joinSelector.add(joinRound);
                join.add(joinRound);

                joinBevel = new JRadioButton();
                joinBevel.setForeground(Color.LIGHT_GRAY);
                joinBevel.setBackground(Color.DARK_GRAY);
                joinBevel.setActionCommand("Bevel");
                joinBevel.setText("Bevel");
                joinBevel.addActionListener(joinAction);
                joinSelector.add(joinBevel);
                join.add(joinBevel);

                add(join);

                JPanel cap = new JPanel();
                cap.setBackground(Color.DARK_GRAY);
                capSelector = new ButtonGroup();
                ActionListener capAction = e -> {
                    switch (((JRadioButton) e.getSource()).getActionCommand()) {
                        case "Butt" -> selectedCap = BasicStroke.CAP_BUTT;
                        case "Round" -> selectedCap = BasicStroke.CAP_ROUND;
                        case "Square" -> selectedCap = BasicStroke.CAP_SQUARE;
                    }
                    paintCanvas.element.stroke = generateStroke();
                };
                capLabel = new JLabel("Cap Type");
                capLabel.setForeground(Color.LIGHT_GRAY);
                cap.add(capLabel);

                capButt = new JRadioButton();
                capButt.setForeground(Color.LIGHT_GRAY);
                capButt.setBackground(Color.DARK_GRAY);
                capButt.setActionCommand("Butt");
                capButt.setText("Butt");
                capButt.addActionListener(capAction);
                capSelector.add(capButt);
                cap.add(capButt);

                capRound = new JRadioButton();
                capRound.setSelected(true);
                capRound.setForeground(Color.LIGHT_GRAY);
                capRound.setBackground(Color.DARK_GRAY);
                capRound.setActionCommand("Round");
                capRound.setText("Round");
                capRound.addActionListener(capAction);
                capSelector.add(capRound);
                cap.add(capRound);

                capSquare = new JRadioButton();
                capSquare.setForeground(Color.LIGHT_GRAY);
                capSquare.setBackground(Color.DARK_GRAY);
                capSquare.setActionCommand("Square");
                capSquare.setText("Square");
                capSquare.addActionListener(capAction);
                capSelector.add(capSquare);
                cap.add(capSquare);

                add(cap);

                JPanel ifChecks = new JPanel();
                ifChecks.setBackground(Color.DARK_GRAY);
                fillButton = new JCheckBox("Fill Enabled");
                fillButton.setBackground(Color.DARK_GRAY);
                fillButton.setForeground(Color.LIGHT_GRAY);
                fillButton.addItemListener(e -> {
                    selectedIfFilled = !selectedIfFilled;
                    paintCanvas.element.ifFilled = selectedIfFilled;
                });
                ifChecks.add(fillButton);
                strokeButton = new JCheckBox("Stroke Enabled", true);
                strokeButton.setBackground(Color.DARK_GRAY);
                strokeButton.setForeground(Color.LIGHT_GRAY);
                strokeButton.addItemListener(e -> {
                    selectedIfStroke = !selectedIfStroke;
                    paintCanvas.element.ifStroke = selectedIfStroke;
                });
                ifChecks.add(strokeButton);
                add(ifChecks);
            }
        }

        // shape submenu; has radio buttons; manages actions
        private class ShapeSelector extends JPanel {
            private final JRadioButton path;
            private final JRadioButton rectangle;
            private final JRadioButton ellipse;
            private final JRadioButton roundedRectangle;
            private final JRadioButton line;
            private final JRadioButton circle;
            private final ButtonGroup shapesGroup;

            // init instance; radio buttons, manages actions
            public ShapeSelector() {
                setBackground(Color.DARK_GRAY);
                setLayout(new GridLayout(0, 2));
                TitledBorder shapesBorder = new TitledBorder("Tool Selector");
                shapesBorder.setTitleColor(Color.LIGHT_GRAY);
                setBorder(shapesBorder);
                shapesGroup = new ButtonGroup();
                ActionListener shapesAction = e -> {
                    switch (((JRadioButton) e.getSource()).getActionCommand()) {
                        case "Path" -> selectedType = Element.PATH;
                        case "Rectangle" -> selectedType = Element.RECTANGLE;
                        case "Ellipse" -> selectedType = Element.ELLIPSE;
                        case "Rounded Rectangle" -> selectedType = Element.ROUNDED_RECTANGLE;
                        case "Line" -> selectedType = Element.LINE;
                        case "Circle" -> selectedType = Element.CIRCLE;
                    }
                    paintCanvas.element = generateElement();
                };

                path = new JRadioButton();
                path.setBackground(Color.DARK_GRAY);
                path.setForeground(Color.LIGHT_GRAY);
                path.setSelected(true);
                path.setActionCommand("Path");
                path.setText("Free Draw");
                path.addActionListener(shapesAction);
                shapesGroup.add(path);
                add(path);

                rectangle = new JRadioButton();
                rectangle.setBackground(Color.DARK_GRAY);
                rectangle.setForeground(Color.LIGHT_GRAY);
                rectangle.setActionCommand("Rectangle");
                rectangle.setText("Rectangle");
                rectangle.addActionListener(shapesAction);
                shapesGroup.add(rectangle);
                add(rectangle);

                ellipse = new JRadioButton();
                ellipse.setBackground(Color.DARK_GRAY);
                ellipse.setForeground(Color.LIGHT_GRAY);
                ellipse.setActionCommand("Ellipse");
                ellipse.setText("Ellipse");
                ellipse.addActionListener(shapesAction);
                shapesGroup.add(ellipse);
                add(ellipse);

                roundedRectangle = new JRadioButton();
                roundedRectangle.setBackground(Color.DARK_GRAY);
                roundedRectangle.setForeground(Color.LIGHT_GRAY);
                roundedRectangle.setActionCommand("Rounded Rectangle");
                roundedRectangle.setText("Rounded Rectangle");
                roundedRectangle.addActionListener(shapesAction);
                shapesGroup.add(roundedRectangle);
                add(roundedRectangle);

                line = new JRadioButton();
                line.setBackground(Color.DARK_GRAY);
                line.setForeground(Color.LIGHT_GRAY);
                line.setActionCommand("Line");
                line.setText("Line");
                line.addActionListener(shapesAction);
                shapesGroup.add(line);
                add(line);

                circle = new JRadioButton();
                circle.setBackground(Color.DARK_GRAY);
                circle.setForeground(Color.LIGHT_GRAY);
                circle.setActionCommand("Circle");
                circle.setText("Circle");
                circle.addActionListener(shapesAction);
                shapesGroup.add(circle);
                add(circle);
            }
        }
    }

    // WARNING * LACK OF ABSTRACTION * WARNING
    private class Element {
        // STATIC VARIABLES
        public static final int PATH = 0;
        public static final int RECTANGLE = 1;
        public static final int ELLIPSE = 2;
        public static final int ROUNDED_RECTANGLE = 3;
        public static final int LINE = 4;
        public static final int CIRCLE = 5;

        // Non private instance variables
        Shape shape;
        int type;
        Color color;
        Stroke stroke;
        boolean ifFilled, ifStroke;

        // Init element
        public Element(int type, Color color, Stroke stroke, boolean ifFilled, boolean ifStroke) {
            switch (type) {
                case PATH -> shape = new Path2D.Double();
                case RECTANGLE -> shape = new Rectangle2D.Double();
                case ELLIPSE, CIRCLE -> shape = new Ellipse2D.Double();
                case ROUNDED_RECTANGLE -> shape = new RoundRectangle2D.Double();
                case LINE -> shape = new Line2D.Double(-100, -100, -100, -100);
            }
            this.type = type;
            this.color = color;
            this.stroke = stroke;
            this.ifFilled = ifFilled;
            this.ifStroke = ifStroke;
        }
    }
}