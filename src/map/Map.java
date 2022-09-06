package map;

import lombok.Data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
@Data
public class Map {
    private int widthOfCells, heightOfCells;
    private char[] dataOfWidthAndHeight;
    private static final String fileNameOfMap = "mapConfiguration.cfg";

    public static final int LEFT = 0, UP = 1, RIGHT = 2, DOWN = 3;

    private static Map instance;

    private Map() {
        widthOfCells = 0;
        heightOfCells = 0;
        dataOfWidthAndHeight = new char[0];
        loadMap();
    }

    private void loadMap() {
        try {
            List<String> lines = Files.readAllLines(new File(fileNameOfMap).toPath());
            if (lines.size() < 2) {
                System.err.println("Нет карты");
                return;
            }
            String[] sizes = lines.get(0).trim().split("(\\s|,|\\*)+");
            if (sizes.length < 2) {
                System.err.println("В первой строке карты нужно указать width height");
                return;
            }
            widthOfCells = Integer.parseInt(sizes[0], 10);
            heightOfCells = Integer.parseInt(sizes[1], 10);
            dataOfWidthAndHeight = new char[widthOfCells * heightOfCells];

            Arrays.fill(dataOfWidthAndHeight, '=');
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < lines.size(); i++) {
                sb.append(lines.get(i).trim());
            }
            char[] map = sb.toString().toCharArray();
            for (int i = 0; i < dataOfWidthAndHeight.length && i < map.length; i++) {
                dataOfWidthAndHeight[i] = map[i];
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void storeMap() {
        try (FileWriter fw = new FileWriter(fileNameOfMap)) {
            fw.write("" + widthOfCells + " " + heightOfCells + "\n");
            for (int i = 0; i < heightOfCells; i++) {
                fw.write(new String(dataOfWidthAndHeight, i * widthOfCells, widthOfCells));
                fw.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void StoreNewMap(int width, int height) {
        this.widthOfCells = width;
        this.heightOfCells = height;
        dataOfWidthAndHeight = new char[width * height];
        Arrays.fill(dataOfWidthAndHeight, '=');
    }

    public static synchronized Map getInstance() {
        if (instance == null) {
            instance = new Map();
        }
        return instance;
    }

    public boolean isReady() {
        return (widthOfCells > 0 && heightOfCells > 0 && dataOfWidthAndHeight != null);
    }

    public void printMap() {
        if (!isReady()) {
            System.err.println("Карты не существует");
            return;
        }
        for (int i = 0; i < heightOfCells; i++) {
            String line = new String(dataOfWidthAndHeight, i * widthOfCells, widthOfCells);
            System.out.println(line);
        }
    }

    public int position(int x, int y) {
        return y * widthOfCells + x;
    }

    public int shift(int pos, int direction) {
        int x = pos % widthOfCells;
        int y = pos / widthOfCells;
        switch (direction) {
            case LEFT:
                x--;
                break;
            case UP:
                y--;
                break;
            case RIGHT:
                x++;
                break;
            case DOWN:
                y++;
                break;
        }
        int new_pos = position(x, y);
        return new_pos;
    }

    public boolean isMoveOnlyEarthAndNotTooLateMap(int position, int direction) {
        int x = position % widthOfCells;
        int y = position / widthOfCells;
        switch (direction) {
            case LEFT:
                if (x == 0) return false;
                x--;
                break;
            case UP:
                if (y == 0) return false;
                y--;
                break;
            case RIGHT:
                if (x == widthOfCells - 1) return false;
                x++;
                break;
            case DOWN:
                if (y == heightOfCells - 1) return false;
                y++;
                break;
        }
        int newPosition = position(x, y);
        return dataOfWidthAndHeight[newPosition] == '#';
    }

    public int getWidthOfCells() {
        return widthOfCells;
    }

    public int getHeightOfCells() {
        return heightOfCells;
    }

    public char[] getDataOfWidthAndHeight() {
        return dataOfWidthAndHeight;
    }

    public boolean isEarth(int position) {
        if (!isReady()) return false;
        if (position < 0 || position >= dataOfWidthAndHeight.length) return false;
        return dataOfWidthAndHeight[position] == '#';
    }

    public boolean isMayMoveOtherLine(int position) {
        int x = position % widthOfCells;
        int y = position / widthOfCells;
        if (x > 0 && isEarth(position(x - 1, y))) return false;
        if (y > 0 && isEarth(position(x, y - 1))) return false;
        if (x < widthOfCells - 1 && isEarth(position(x + 1, y))) return false;
        if (y < heightOfCells - 1 && isEarth(position(x, y + 1))) return false;
        return true;
    }

    public int moveOnCellAndChangeSideLine(int fromCell, int count) {
        int position = fromCell;
        if (isMayMoveOtherLine(fromCell)) return fromCell;
        while (count > 0) {
            int direction = ThreadLocalRandom.current().nextInt(4);
            if (!isMoveOnlyEarthAndNotTooLateMap(position, direction)) continue;
            do {
                position = shift(position, direction);
                count--;
            } while (count > 0 && isMoveOnlyEarthAndNotTooLateMap(position, direction));
        }
        return position;
    }

    public void toChangeEarthOnWaterAndOpposite(int x, int y) {
        int pos = position(x, y);
        dataOfWidthAndHeight[pos] = (dataOfWidthAndHeight[pos] == '=') ? '#' : '=';
    }

    public static void main(String[] args) {
        Map map = Map.getInstance();
        map.printMap();

        System.out.println("false =" + map.isMoveOnlyEarthAndNotTooLateMap(7, LEFT));
        System.out.println("false =" + map.isMoveOnlyEarthAndNotTooLateMap(5, LEFT));
        System.out.println("false =" + map.isMoveOnlyEarthAndNotTooLateMap(7, UP));
        System.out.println("false =" + map.isMoveOnlyEarthAndNotTooLateMap(4, UP));
        System.out.println("false =" + map.isMoveOnlyEarthAndNotTooLateMap(7, RIGHT));
        System.out.println("false =" + map.isMoveOnlyEarthAndNotTooLateMap(4, RIGHT));
        System.out.println("false =" + map.isMoveOnlyEarthAndNotTooLateMap(17, DOWN));
        System.out.println("false =" + map.isMoveOnlyEarthAndNotTooLateMap(20, DOWN));

        System.out.println("true =" + map.isMoveOnlyEarthAndNotTooLateMap(7, DOWN));

        System.out.println("true = " + map.isMayMoveOtherLine(0));
        System.out.println("false = " + map.isMayMoveOtherLine(7));

    }
}
