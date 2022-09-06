package herbivore;

import cellOfMap.Cell;
import structureOfAnimals.Animal;

public class Duck extends Herbivore {
    public Duck() {
        super("Утка(\uD83E\uDD86)");
    }

    @Override
    public void eatOfAnimals(Cell cell) {
        Animal food = null;
        double hungry = getMaxFood() - getSatiety();
        for (int i = 0; i < 10 && hungry > 0; i++) {
            try {
                food = cell.getRandomOfNames("Гусеница(\uD83D\uDC1B)");
                if (food != null) {
                    food.setDeath(true);

                    double weight = food.getWeight();
                    double satiety = getSatiety() + ((hungry > weight) ? weight : hungry);
                    setSatiety(satiety);
                    hungry = getMaxFood() - satiety;

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (food != null) food.unlock();
            }
        }
        super.eatOfAnimals(cell);
    }
}
