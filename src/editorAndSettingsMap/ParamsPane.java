package editorAndSettingsMap;


import setting.Settings;

import javax.swing.*;
import java.awt.*;

public class ParamsPane extends JPanel {
    private Settings settings = Settings.getInstance();

    public ParamsPane() {
        init();
    }

    private void init() {
        setLayout(new BorderLayout(3, 3));
        JFormattedTextField tfDuration = new JFormattedTextField(new Integer(settings.getDuration()));
        tfDuration.setColumns(7);
        JFormattedTextField tfIterations = new JFormattedTextField(new Integer(settings.getIterations()));
        tfIterations.setColumns(7);
        JFormattedTextField tfCellPool = new JFormattedTextField(new Integer(settings.getCellPoolSize()));
        tfCellPool.setColumns(7);
        JPanel line = new JPanel();
        line.add(new JLabel("Duration: "));
        line.add(tfDuration);
        line.add(Box.createHorizontalStrut(20));
        line.add(new JLabel("Iterations: "));
        line.add(tfIterations);
        line.add(Box.createHorizontalStrut(20));
        line.add(new JLabel("CellPoolSize: "));
        line.add(tfCellPool);
        add(line, BorderLayout.NORTH);

        JButton bSave = new JButton("Save");
        bSave.addActionListener(e -> {
            settings.setDuration((Integer) (tfDuration.getValue()));
            settings.setIterations((Integer) (tfIterations.getValue()));
            settings.setCellPoolSize((Integer) (tfCellPool.getValue()));
            settings.storeParams();
        });
        JPanel pButtons = new JPanel();
        pButtons.add(bSave);
        add(pButtons, BorderLayout.SOUTH);
    }

}
