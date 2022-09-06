package predators;


// Орел
public class Eagle extends Predator {
    public Eagle() {
        super("Орёл(\uD83E\uDD85)");
        setProbability("Лиса(\uD83E\uDD8A)", 10);
        setProbability("Кролик(\uD83D\uDC07)", 90);
        setProbability("Мышь(\uD83D\uDC2D)", 90);
        setProbability("Утка(\uD83E\uDD86)", 80);
    }

}
