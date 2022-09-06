package predators;

import cellOfMap.Cell;
import structureOfAnimals.Animal;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

abstract public class Predator extends Animal {

    // вероятности съесть другое животное
    private HashMap<String, Integer> probabilityEatOtherAnimal = new HashMap<>();

    // конструктор
    public Predator(String name) {
        super(name);
    }

    public int getProbability(String name) {
        return probabilityEatOtherAnimal.getOrDefault(name, 0);
    }

    protected void setProbability(String name, int value) {
        probabilityEatOtherAnimal.put(name, value);
    }

    protected String[] getFoodList() {
        return probabilityEatOtherAnimal.keySet().toArray(new String[0]);
    }

    public void eatOfAnimals(Cell cell) {
        double hungry = getMaxFood() - getSatiety();
        Animal food = null;
        String[] foodList = getFoodList();

        int attempts = 0;

        do {
            try {
                food = cell.getRandomOfNamesList(foodList);

                if (food != null) {
                    int prb = getProbability(food.getName());
                    boolean result = (ThreadLocalRandom.current().nextInt(100) < prb);
                    if (result) {
                        food.setDeath(true);
                        double weight = food.getWeight();
                        double satiety = getSatiety() + ((hungry > weight) ? weight : hungry);
                        setSatiety(satiety);
                        hungry = getMaxFood() - satiety;
                    }
                }
                attempts++;

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (food != null) food.unlock();
            }
        } while (attempts < 3 && hungry > 0);
    }

}
