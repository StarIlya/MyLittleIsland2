package predators;


// Удав
public class Snake extends Predator {
    public Snake() {
        super("Змея(\uD83D\uDC0D)");
        setProbability("Лиса", 15);
        setProbability("Кролик(\uD83D\uDC07)", 20);
        setProbability("Мышь(\uD83D\uDC2D)", 40);
        setProbability("Утка(\uD83E\uDD86)", 10);
    }

}
