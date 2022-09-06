package editorAndSettingsMap;

import info.Info;

import setting.Settings;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;


public class InfoTableModel extends AbstractTableModel {
    private Settings settings = Settings.getInstance();
    private HashMap<String, Info> listInfoOfAnimals = settings.getInfoMap();
    private String[] names;

    public InfoTableModel() {
        names = listInfoOfAnimals.keySet().toArray(new String[0]);
        Arrays.sort(names);
    }

    @Override
    public int getRowCount() {
        return listInfoOfAnimals.size();
    }

    @Override
    public int getColumnCount() {
        return 8;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= getRowCount() || columnIndex < 0 || columnIndex >= getColumnCount())
            return null;
        String key = names[rowIndex];
        Info row = listInfoOfAnimals.get(key);
        switch (columnIndex) {
            case 0:
                return key;
            case 1:
                return row.getWeight();
            case 2:
                return row.getMaxCount();
            case 3:
                return row.getSpeed();
            case 4:
                return row.getMaxFood();
            case 5:
                return row.getChilds();
            case 6:
                return row.getInitCount();
            case 7:
                return row.getProbMove();
        }
        return null;
    }

    private String[] titlesOfColumns = {"Name", "Weight", "MaxCount", "Speed", "MaxFood", "Childs", "InitCount", "Probability Move"};

    @Override
    public String getColumnName(int column) {
        if (column < 0 || column >= getColumnCount()) return null;
        return titlesOfColumns[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex < 0 || columnIndex >= getColumnCount()) return null;
        if (columnIndex == 0) return String.class;
        else if (columnIndex == 1 || columnIndex == 4) return Double.class;
        else return Integer.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return (columnIndex > 0);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= getRowCount() || columnIndex < 0 || columnIndex >= getColumnCount()) return;
        String key = names[rowIndex];
        Info row = listInfoOfAnimals.get(key);
        switch ((columnIndex)) {
            case 1:
                row.setWeight((Double) aValue);
                break;
            case 2:
                row.setMaxCount((Integer) aValue);
                break;
            case 3:
                row.setSpeed((Integer) aValue);
                break;
            case 4:
                row.setMaxFood((Double) aValue);
                break;
            case 5:
                row.setChilds((Integer) aValue);
                break;
            case 6:
                row.setInitCount((Integer) aValue);
                break;
            case 7:
                row.setProbMove((Integer) aValue);
                break;
        }

    }

    public static void main(String[] args) {
        JTable table = new JTable(new InfoTableModel());
        Settings settings = Settings.getInstance();

        JFrame frame = new JFrame("Table");
        frame.setSize(500, 300);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        JButton save = new JButton("Save");
        panel.add(save, BorderLayout.SOUTH);
        save.addActionListener(e -> settings.storeAnimals());
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}
