package structureOfAnimals;

import cellOfMap.Cell;
import predators.*;
import herbivore.*;
import info.Info;
import map.Map;
import setting.Settings;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

abstract public class Animal {
    private String name;
    private boolean death = false;
    private ReentrantLock lock = new ReentrantLock();
    private double satiety;
    //Info
    private Info info;

    public void lock() {
        lock.lock();
    }

    public boolean tryLock() {
        boolean result = lock.tryLock();
        return result;
    }

    public void unlock() {
        lock.unlock();
    }

    public Animal(String name) {
        this.name = name;
        info = Settings.getInstance().getInfo(name);
        satiety = info.getMaxFood();
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return info.getWeight();
    }

    public int getSpeed() {
        return info.getSpeed();
    }

    public double getMaxFood() {
        return info.getMaxFood();
    }

    public double getSatiety() {
        return satiety;
    }


    public boolean satietyOfAnimals() {
        double value = getMaxFood() * 0.1;
        setSatiety(getSatiety() - value);
        boolean liveFlag = (getSatiety() > 0);
        return liveFlag;
    }


    public void setSatiety(double satiety) {
        this.satiety = satiety;
    }

    public boolean isDeath() {
        return death;
    }

    public void setDeath(boolean death) {
        this.death = death;
    }


    public void liveCycleOfAnimals(Cell cell) {
        int probMove = info.getProbMove();
        int probEat, probPair;
        if (probMove < 0 || probMove > 60) {
            probMove = 10;
        }
        probPair = (int) ((100 - probMove) * 0.2);
        if (probPair < 10) probPair = 10;

        probEat = 100 - probMove - probPair;

        int selector = ThreadLocalRandom.current().nextInt(100);
        if (selector < probMove) {
            moveAnimalsToNextCells(cell);
        } else if (selector < probMove + probPair) {
            searchPair(cell);
        } else {
            eatOfAnimals(cell);
        }

    }

    abstract public void eatOfAnimals(Cell cell);

    public void searchPair(Cell cell) {
        Animal p = null;
        try {
            for (int i = 0; i < 10 && p == null; i++) {
                p = cell.getRandomOfNames(getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (p != null) p.unlock();
        }
        if (p == null) return;
        int lotOfBabies = ThreadLocalRandom.current().nextInt(info.getChilds()) + 1;
        for (int i = 0; i < lotOfBabies; i++) {
            cell.putNewborn(createAnimals(getName()));
        }
    }

    public void moveAnimalsToNextCells(Cell cell) {
        int len = getSpeed();
        int newPosition = Map.getInstance().moveOnCellAndChangeSideLine(cell.getPos(), len);
        cell.moveAnimalsOnDifferentCells(this, newPosition);
    }

    public static Animal createAnimals(String name) {
        if ("Лошадь(\uD83D\uDC0E)".equals(name)) return new Horse();
        if ("Олень(\uD83E\uDD8C)".equals(name)) return new Deer();
        if ("Кролик(\uD83D\uDC07)".equals(name)) return new Rabbit();
        if ("Мышь(\uD83D\uDC2D)".equals(name)) return new Mouse();
        if ("Коза(\uD83D\uDC10)".equals(name)) return new Goat();
        if ("Овца(\uD83D\uDC11)".equals(name)) return new Sheep();
        if ("Кабан(\uD83D\uDC17)".equals(name)) return new Hog();
        if ("Буйвол(\uD83D\uDC03)".equals(name)) return new Buffalo();
        if ("Утка(\uD83E\uDD86)".equals(name)) return new Duck();
        if ("Гусеница(\uD83D\uDC1B)".equals(name)) return new Caterpillar();
        if ("Волк(\uD83D\uDC3A)".equals(name)) return new Wolf();
        if ("Змея(\uD83D\uDC0D)".equals(name)) return new Snake();
        if ("Лиса(\uD83E\uDD8A)".equals(name)) return new Fox();
        if ("Медведь(\uD83D\uDC3B)".equals(name)) return new Bear();
        if ("Орёл(\uD83E\uDD85)".equals(name)) return new Eagle();
        return null;
    }
}
