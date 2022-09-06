package setting;

import info.Info;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

// Настройки
public class Settings {
    private static Settings instance;
    private static final String animals_config = "animalsConfiguration.cfg";
    private static final String params_config = "parametersConfiguration.cfg";


    private Settings() {
        init();
    }

    public static synchronized Settings getInstance() {
        if (instance == null) instance = new Settings();
        return instance;
    }

    private HashMap<String, Info> mapOfInfoAnimals = new HashMap<>();
    private int iterations = 10;
    private int duration = 2000;
    private int cellPoolSize = 10;


    public HashMap<String, Info> getInfoMap() {
        return mapOfInfoAnimals;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCellPoolSize() {
        return cellPoolSize;
    }

    public void setCellPoolSize(int cellPoolSize) {
        this.cellPoolSize = cellPoolSize;
    }

    private void init() {

        // загружаем значения по умолчанию
        mapOfInfoAnimals.put("Волк(\uD83D\uDC3A)", new Info(50, 30, 3, 8, 2, 5, 10));
        mapOfInfoAnimals.put("Змея(\uD83D\uDC0D)", new Info(15, 30, 1, 3, 2, 4, 10));
        mapOfInfoAnimals.put("Лиса(\uD83E\uDD8A)", new Info(8, 30, 2, 2, 2, 8, 10));
        mapOfInfoAnimals.put("Медведь(\uD83D\uDC3B)", new Info(500, 5, 2, 80, 1, 2, 10));
        mapOfInfoAnimals.put("Орёл(\uD83E\uDD85)", new Info(6, 20, 3, 1, 2, 5, 10));
        mapOfInfoAnimals.put("Лошадь(\uD83D\uDC0E)", new Info(400, 20, 4, 60, 1, 20, 10));
        mapOfInfoAnimals.put("Олень(\uD83E\uDD8C)", new Info(300, 20, 4, 50, 1, 15, 10));
        mapOfInfoAnimals.put("Кролик(\uD83D\uDC07)", new Info(2, 150, 2, 0.45, 2, 10, 10));
        mapOfInfoAnimals.put("Мышь(\uD83D\uDC2D)", new Info(0.05, 500, 1, 0.01, 10, 20, 10));
        mapOfInfoAnimals.put("Коза(\uD83D\uDC10)", new Info(60, 140, 3, 10, 2, 10, 10));
        mapOfInfoAnimals.put("Овца(\uD83D\uDC11)", new Info(70, 140, 3, 15, 2, 10, 10));
        mapOfInfoAnimals.put("Кабан(\uD83D\uDC17)", new Info(400, 50, 2, 50, 3, 10, 10));
        mapOfInfoAnimals.put("Буйвол(\uD83D\uDC03)", new Info(700, 10, 3, 100, 2, 10, 10));
        mapOfInfoAnimals.put("Утка(\uD83E\uDD86)", new Info(1, 200, 4, 0.15, 3, 20, 10));
        mapOfInfoAnimals.put("Гусеница(\uD83D\uDC1B)", new Info(0.01, 1000, 0, 0.01, 4, 100, 10));
        mapOfInfoAnimals.put("Растения", new Info(1, 200, 0, 0, 1, 200, 0));


        loadAnimals();
        loadParams();
    }


    public int getMaxCount(String name) {
        if (!mapOfInfoAnimals.containsKey(name)) return 0;
        return mapOfInfoAnimals.get(name).getMaxCount();
    }

    public double getWeight(String name) {
        if (!mapOfInfoAnimals.containsKey(name)) return 0;
        return mapOfInfoAnimals.get(name).getWeight();
    }

    public int getSpeed(String name) {
        if (!mapOfInfoAnimals.containsKey(name)) return 0;
        return mapOfInfoAnimals.get(name).getSpeed();
    }

    public double getMaxFood(String name) {
        if (!mapOfInfoAnimals.containsKey(name)) return 0;
        return mapOfInfoAnimals.get(name).getMaxFood();
    }

    public Info getInfo(String name) {
        if (!mapOfInfoAnimals.containsKey(name)) return new Info();
        return mapOfInfoAnimals.get(name);
    }

    public int getInitCount(String name) {
        if (!mapOfInfoAnimals.containsKey(name)) return 0;
        return mapOfInfoAnimals.get(name).getInitCount();
    }

    public void loadAnimals() {
        try {
            File file = new File(animals_config);
            if (!file.exists()) {
                System.out.println("Файл AnimalConfiguration отсутствует");
                storeAnimals();
                return;
            }
            List<String> lines = Files.readAllLines(file.toPath());

            for (String line : lines) {
                line = line.trim();
                if (line.length() == 0 || line.startsWith("#")) continue;
                try {
                    String[] items = line.split("\\s*;\\s*");
                    if (items.length < 8) {
                        System.err.println("Неправильный формат: " + line);
                        continue;
                    }
                    Info info = new Info();
                    info.setWeight(Double.parseDouble(items[1]));
                    info.setMaxCount(Integer.parseInt(items[2]));
                    info.setSpeed(Integer.parseInt(items[3]));
                    info.setMaxFood(Double.parseDouble(items[4]));
                    info.setChilds(Integer.parseInt(items[5]));
                    info.setInitCount(Integer.parseInt(items[6]));
                    info.setProbMove(Integer.parseInt(items[7]));
                    mapOfInfoAnimals.put(items[0], info);
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void storeAnimals() {
        try (FileWriter fw = new FileWriter(animals_config)) {
            fw.write("#name     ; weight;count;speed;maxfood;childs;initcount;move probability\n");
            for (String key : mapOfInfoAnimals.keySet()) {
                Info info = mapOfInfoAnimals.get(key);
                String line = String.format(Locale.US, "%-10s;\t%6.2f;\t%4d;\t%2d;\t%6.2f;\t%2d;\t%3d;\t%2d\n", key,
                        info.getWeight(),
                        info.getMaxCount(),
                        info.getSpeed(),
                        info.getMaxFood(),
                        info.getChilds(),
                        info.getInitCount(),
                        info.getProbMove()
                );
                fw.write(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadParams() {
        Properties props = new Properties();
        File file = new File(params_config);
        if (!file.exists()) return;
        try (FileReader fr = new FileReader(file)) {
            props.load(fr);
            duration = Integer.parseInt((String) props.getOrDefault("duration", "" + duration), 10);
            iterations = Integer.parseInt((String) props.getOrDefault("iterations", "" + iterations), 10);
            cellPoolSize = Integer.parseInt((String) props.getOrDefault("cellPoolSize", "" + cellPoolSize), 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void storeParams() {
        try (FileWriter fw = new FileWriter(params_config)) {
            fw.write("duration=" + duration + "\n");
            fw.write("iterations=" + iterations + "\n");
            fw.write("cellPoolSize=" + cellPoolSize + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
