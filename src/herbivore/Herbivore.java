package herbivore;

import cellOfMap.Cell;
import structureOfAnimals.Animal;

abstract public class Herbivore extends Animal {

    public Herbivore(String name) {
        super(name);
    }

    @Override
    public void eatOfAnimals(Cell cell) {
        double hungry = getMaxFood() - getSatiety();
        setSatiety(getSatiety() + cell.HowMuchWasAteGrass(hungry));
    }

}
