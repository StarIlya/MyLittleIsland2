package cellOfMap;

import island.Island;
import setting.Settings;
import structureOfAnimals.Animal;
import structureOfAnimals.AnimalLiveCycle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Cell {
    private int pos;
    private double grassOnCells;
    private Island island;

    private Settings settings = Settings.getInstance();
    private ReentrantLock lock = new ReentrantLock();
    private HashMap<String, List<Animal>> listOfAnimals = new HashMap<>();
    private HashMap<String, List<Animal>> listOfNewComerAnimals = new HashMap<>();
    private HashMap<String, List<Animal>> listOfNewBornAnimals = new HashMap<>();


    public Cell(int pos, Island island) {
        this.pos = pos;
        this.island = island;
        randomFillCellsOtherAnimals();
    }

    public Animal getRandomOfNames(String name) {
        Animal result = null;
        lock.lock();
        try {
            List<Animal> list = listOfAnimals.get(name);
            if (list == null || list.size() == 0) return null;
            result = list.get(ThreadLocalRandom.current().nextInt(list.size()));
            if (result == null || !result.tryLock()) {
                result = null;
            }
            if (result != null && result.isDeath()) {
                result.unlock();
                result = null;
            }
        } finally {
            lock.unlock();
        }
        return result;
    }

    public List<String> getNames() {
        ArrayList<String> result = new ArrayList<>();
        lock.lock();
        try {
            for (String key : listOfAnimals.keySet()) {
                if (listOfAnimals.get(key).size() > 0) {
                    result.add(key);
                }
            }
        } finally {
            lock.unlock();
        }
        return result;
    }

    public Animal getRandomOther(String name) {
        List<String> keys = getNames();
        keys.remove(name);
        if (keys.size() == 0) return null;
        Animal result = null;
        int cnt = 0;
        do {
            String key = keys.get(ThreadLocalRandom.current().nextInt(keys.size()));

            result = getRandomOfNames(key);
            cnt++;
        } while (result == null && cnt <= 10);
        return result;
    }


    public Animal getRandomOfNamesList(String[] list) {
        List<String> names = getNames();
        List<String> keys = new ArrayList<>();
        for (String name : list) {
            if (names.contains(name)) keys.add(name);
        }

        if (keys.size() == 0) return null;
        Animal result = null;
        int cnt = 0;
        do {
            String key = keys.get(ThreadLocalRandom.current().nextInt(keys.size()));

            result = getRandomOfNames(key);
            cnt++;
        } while (result == null && cnt <= 10);
        return result;
    }

    private void fill(String name, int count) {
        lock.lock();
        try {
            for (int i = 0; i < count; i++) {
                putToMap(listOfAnimals, Animal.createAnimals(name));
            }
        } finally {
            lock.unlock();
        }
    }

    private void randomFillCellsOtherAnimals() {
        grassOnCells = (ThreadLocalRandom.current().nextInt(settings.getInitCount("Растения")));
        for (String key : settings.getInfoMap().keySet()) {
            if ("Растения".equals(key)) continue;
            fill(key, ThreadLocalRandom.current().nextInt(settings.getInitCount(key)));
        }
    }

    public void initNewLoop() {
        lock.lock();
        try {

            if (grassOnCells > 0) {
                grassOnCells = (Math.min(grassOnCells * 2, settings.getMaxCount("Растения")));
            } else {
                grassOnCells = 10;
            }
            for (String key : listOfAnimals.keySet()) {
                List<Animal> src = listOfAnimals.get(key);
                List<Animal> listRemove = new ArrayList<>();
                for (Animal animal : src) {
                    if (animal.isDeath()) {
                        listRemove.add(animal);
                    }
                }
                for (Animal animal : listRemove) {
                    src.remove(animal);
                }
                listRemove.clear();
            }
            addAnimals(listOfNewComerAnimals);
            listOfNewComerAnimals.clear();
            addAnimals(listOfNewBornAnimals);
            listOfNewBornAnimals.clear();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private void addAnimals(HashMap<String, List<Animal>> src) {
        lock.lock();
        try {
            for (String name : src.keySet()) {
                int maxCount = settings.getMaxCount(name);
                if (maxCount == 0) continue;
                List<Animal> list = src.get(name);
                List<Animal> dst = listOfAnimals.get(name);
                if (dst == null || dst.size() == 0) continue;
                int size = dst.size();
                if (size >= maxCount) {
                    continue;
                }
                int current = maxCount - size;
                for (int i = 0; i < current && i < list.size(); i++) {
                    dst.add(list.get(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public HashMap<String, Integer> getStatOfAnimalsAndPlants() {
        HashMap<String, Integer> resolution = new HashMap<>();
        for (String name : listOfAnimals.keySet()) {
            resolution.put(name, listOfAnimals.get(name).size());
        }
        resolution.put("Растения", (int) Math.round(grassOnCells));
        return resolution;
    }

    private void putToMap(HashMap<String, List<Animal>> map, Animal animal) {
        if (animal == null) return;
        lock.lock();
        try {
            List<Animal> list = map.get(animal.getName());
            if (list == null) {
                list = new ArrayList<>();
                map.put(animal.getName(), list);
            }
            list.add(animal);
        } finally {
            lock.unlock();
        }
    }

    public void putNewcomer(Animal animal) {
        putToMap(listOfNewComerAnimals, animal);
    }

    public void putNewborn(Animal animal) {
        putToMap(listOfNewBornAnimals, animal);
    }

    public void moveAnimalsOnDifferentCells(Animal animal, int toCell) {
        if (island.movingAnimalToDifferentCell(animal, toCell)) {
            lock.lock();
            try {
                listOfAnimals.get(animal.getName()).remove(animal);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public int getPos() {
        return pos;
    }

    private ExecutorService poolOfLiveAnimals = Executors.newFixedThreadPool(settings.getCellPoolSize());

    public void startPoolOnThisCell() {
        lock.lock();
        try {
            for (String name : listOfAnimals.keySet()) {
                for (Animal animal : listOfAnimals.get(name)) {
                    poolOfLiveAnimals.submit(new AnimalLiveCycle(animal, Cell.this));
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void awaitEndingPool(int milliseconds) throws InterruptedException {
        poolOfLiveAnimals.awaitTermination(milliseconds, TimeUnit.MILLISECONDS);
    }

    public double HowMuchWasAteGrass(double value) {
        double result = 0;
        lock.lock();
        try {
            if (value < 0) value = 0;
            if (grassOnCells > value) {
                result = value;
                grassOnCells -= value;
            } else {
                result = grassOnCells;
                grassOnCells = 0;
            }
        } finally {
            lock.unlock();
        }
        return result;
    }

    public void shutdown() {
        poolOfLiveAnimals.shutdown();
    }

}
