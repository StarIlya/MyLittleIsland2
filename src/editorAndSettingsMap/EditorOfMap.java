package editorAndSettingsMap;


import map.Map;

import javax.swing.*;
import java.awt.*;

public class EditorOfMap {

    private JTabbedPane tabbedPane;

    public void editor() {
        tabbedPane = new JTabbedPane();

        JPanel pInfo = new JPanel(new BorderLayout(3, 3));
        InfoTableModel tableModel = new InfoTableModel();
        JTable table = new JTable(tableModel);
        pInfo.add(table, BorderLayout.CENTER);
        JPanel pInfoButtons = new JPanel();
        JButton btnInfoSave = new JButton("Save");
        pInfoButtons.add(btnInfoSave);
        pInfo.add(pInfoButtons, BorderLayout.SOUTH);

        tabbedPane.add(pInfo, "Animals");

        JPanel pMap = new JPanel(new BorderLayout(3, 3));
        MapPane mapPane = new MapPane();
        pMap.add(mapPane, BorderLayout.CENTER);
        Map map = Map.getInstance();
        JFormattedTextField tfWidth = new JFormattedTextField(new Integer(map.getWidthOfCells()));
        tfWidth.setColumns(5);
        JFormattedTextField tfHeight = new JFormattedTextField(new Integer(map.getHeightOfCells()));
        tfHeight.setColumns(5);
        JPanel pMapEdits = new JPanel();
        pMapEdits.add(new JLabel("width: "));
        pMapEdits.add(tfWidth);
        pMapEdits.add(Box.createHorizontalStrut(20));
        pMapEdits.add(new JLabel("height: "));
        pMapEdits.add(tfHeight);
        pMap.add(pMapEdits, BorderLayout.NORTH);
        JPanel pMapButtons = new JPanel();
        JButton bMapNew = new JButton("New map");
        bMapNew.addActionListener(e -> mapPane.newMap((Integer) (tfWidth.getValue()), (Integer) (tfHeight.getValue())));
        JButton bMapStore = new JButton("Store");
        bMapStore.addActionListener(e -> mapPane.storeMap());
        pMapButtons.add(bMapNew);
        pMapButtons.add(Box.createHorizontalStrut(20));
        pMapButtons.add(bMapStore);
        pMap.add(pMapButtons, BorderLayout.SOUTH);

        tabbedPane.add(pMap, "Map");

        ParamsPane paramsPane = new ParamsPane();

        tabbedPane.add(paramsPane, "Params");

        JFrame frame = new JFrame("Editor");
        frame.setSize(700, 400);

        frame.setContentPane(tabbedPane);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        new EditorOfMap().editor();
    }
}
