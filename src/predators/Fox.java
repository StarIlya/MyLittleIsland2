package predators;


// Лиса
public class Fox extends Predator {
    public Fox() {
        super("Лиса(\uD83E\uDD8A)");
        setProbability("Кролик", 70);
        setProbability("Мышь(\uD83D\uDC2D)", 90);
        setProbability("Утка(\uD83E\uDD86)", 10);
        setProbability("Гусеница(\uD83D\uDC1B)", 40);
    }

}
