package island;

import cellOfMap.Cell;
import lombok.Data;
import map.Map;
import setting.Settings;
import structureOfAnimals.Animal;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
@Data
public class Island {
    private Cell[] cells;   // клетки
    private Settings settings = Settings.getInstance();
    private AtomicInteger counterOfSteps = new AtomicInteger(0);
    private int maxCounterOfSteps = 10;

    public Island() {
        Map map = Map.getInstance();
        if (!map.isReady()) {
            System.err.println("Подготовьте карту");
            System.exit(-1);
        }
        cells = new Cell[map.getDataOfWidthAndHeight().length];
        for (int i = 0; i < cells.length; i++) {
            cells[i] = (map.isEarth(i)) ? new Cell(i, this) : null;
        }
        maxCounterOfSteps = settings.getIterations();
    }

    // метод для объединения статистики
    private void addStat(HashMap<String, Integer> hashList1, HashMap<String, Integer> hashList2) {
        for (String key : hashList2.keySet()) {
            if (hashList1.containsKey(key)) hashList1.put(key, hashList1.get(key) + hashList2.get(key));
            else hashList1.put(key, hashList2.get(key));
        }
    }

    private void refreshStatAboutCells() {
        HashMap<String, Integer> resolution = new HashMap<>();

        for (Cell cell : cells) {
            if (cell == null) continue;
            cell.initNewLoop();
            addStat(resolution, cell.getStatOfAnimalsAndPlants());
        }
        for (String key : resolution.keySet()) {
            System.out.printf("%-10s %5d\n", key, resolution.get(key));
        }
    }

    private void initStepLoop() {

        counterOfSteps.incrementAndGet();

        System.out.println("\nШаг: " + counterOfSteps.get());
        refreshStatAboutCells();

        for (Cell cell : cells) {
            if (cell == null) continue;
            cell.startPoolOnThisCell();
        }
    }

    private ScheduledExecutorService poolOfStartSteps = Executors.newSingleThreadScheduledExecutor();

    private Runnable globalTask = new Runnable() {
        @Override
        public void run() {
            initStepLoop();

            if (counterOfSteps.get() >= maxCounterOfSteps) {
                poolOfStartSteps.shutdown();
                for (Cell cell : cells) {
                    if (cell == null) continue;
                    cell.shutdown();
                }
                System.out.println("shutdown");
                for (Cell cell : cells) {
                    if (cell == null) continue;
                    try {
                        cell.awaitEndingPool(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("\nРезультат:");
                refreshStatAboutCells();
            }
        }
    };

    public void start() {
        poolOfStartSteps.scheduleAtFixedRate(globalTask, 1, settings.getDuration(), TimeUnit.MILLISECONDS);
    }

    public boolean movingAnimalToDifferentCell(Animal animal, int toCell) {
        if (cells[toCell] == null) return false;
        cells[toCell].putNewcomer(animal);
        return true;
    }

    public static void main(String[] args) {
        Island island = new Island();
        island.start();
    }
}
