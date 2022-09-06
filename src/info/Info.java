package info;

import lombok.Data;

@Data
public class Info {
    private double weight;
    private int maxCount, speed;
    private double maxFood;
    private int childs;
    private int initCount;
    private int probMove;

    // конструкторы

    public Info() {
    }

    public Info(double weight, int maxCount, int speed, double maxFood, int childs, int initCount, int probMove) {
        this.weight = weight;
        this.maxCount = maxCount;
        this.speed = speed;
        this.maxFood = maxFood;
        this.childs = childs;
        this.initCount = initCount;
        this.probMove = probMove;
    }

}
