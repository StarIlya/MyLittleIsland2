package editorAndSettingsMap;


import map.Map;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MapPane extends JPanel {
    private Map map = Map.getInstance();
    private int cellSize = 10;
    private Dimension size;
    private Color colorOfWater = new Color(15, 133, 241);
    private Color colorOfEarth = new Color(253, 81, 0);

    public MapPane() {
        init();
        addMouseListener(mouse);
    }

    private void init() {
        cellSize = (map.getDataOfWidthAndHeight().length <= 100) ? 30 : 10;
        size = new Dimension(map.getWidthOfCells() * cellSize, map.getHeightOfCells() * cellSize);
    }

    public void newMap(int width, int height) {
        if (width < 1) width = 1;
        if (height < 1) height = 1;
        map.StoreNewMap(width, height);
        init();
        repaint();
    }

    public void storeMap() {
        map.storeMap();
    }

    private MouseAdapter mouse = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            int cellX = e.getX() / cellSize;
            int cellY = e.getY() / cellSize;

            if (cellX >= map.getWidthOfCells() || cellY > map.getHeightOfCells()) return;

            map.toChangeEarthOnWaterAndOpposite(cellX, cellY);
            repaint();
        }
    };

    @Override
    public Dimension getMinimumSize() {
        return size;
    }

    @Override
    public Dimension getMaximumSize() {
        return size;
    }

    @Override
    public Dimension getPreferredSize() {
        return size;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2d = (Graphics2D) graphics;

        int width = map.getWidthOfCells() * cellSize;
        int height = map.getHeightOfCells() * cellSize;


        g2d.setColor(colorOfWater);
        g2d.fillRect(0, 0, width, height);

        g2d.setColor(colorOfEarth);
        char[] buf = map.getDataOfWidthAndHeight();
        for (int i = 0; i < buf.length; i++) {
            if (buf[i] == '=') continue;
            int x = i % map.getWidthOfCells();
            int y = i / map.getWidthOfCells();

            g2d.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
        }

        g2d.setColor(Color.DARK_GRAY);
        for (int i = 0; i <= map.getWidthOfCells(); i++) {
            g2d.drawLine(i * cellSize, 0, i * cellSize, height);
        }
        for (int i = 0; i <= map.getHeightOfCells(); i++) {
            g2d.drawLine(0, i * cellSize, width, i * cellSize);
        }
    }

    public static void main(String[] args) {
        MapPane pane = new MapPane();
        JPanel content = new JPanel(new BorderLayout(3, 3));
        content.add(new JScrollPane(pane));
        JPanel pButtons = new JPanel();
        JFormattedTextField tfWidth = new JFormattedTextField(new Integer(0));
        tfWidth.setColumns(5);
        JFormattedTextField tfHeight = new JFormattedTextField(new Integer(0));
        tfHeight.setColumns(5);
        JButton btnNew = new JButton("New map");
        btnNew.addActionListener(e -> pane.newMap((Integer) (tfWidth.getValue()), (Integer) (tfHeight.getValue())));
        pButtons.add(new JLabel("width: "));
        pButtons.add(tfWidth);
        pButtons.add(new JLabel(" height: "));
        pButtons.add(tfHeight);
        pButtons.add(Box.createHorizontalStrut(20));
        pButtons.add(btnNew);

        JButton btnStore = new JButton("Store");
        btnStore.addActionListener(e -> pane.storeMap());
        pButtons.add(Box.createHorizontalStrut(20));
        pButtons.add(btnStore);

        content.add(pButtons, BorderLayout.SOUTH);

        JFrame frame = new JFrame("Island");

        frame.setSize(500, 300);
        frame.setContentPane(content);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

    }

}
