package structureOfAnimals;

import cellOfMap.Cell;

public class AnimalLiveCycle implements Runnable {
    private Animal animal;
    private Cell cell;

    public AnimalLiveCycle(Animal animal, Cell cell) {
        this.animal = animal;
        this.cell = cell;
    }


    @Override
    public void run() {
        try {
            animal.lock();
            if(!animal.satietyOfAnimals()) animal.setDeath(true);
            if(animal.isDeath()) {
                return;
            }
            animal.liveCycleOfAnimals(cell);
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            animal.unlock();
        }
    }
}
