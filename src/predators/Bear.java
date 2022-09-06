package predators;


public class Bear extends Predator {
    public Bear() {
        super("Медведь(\uD83D\uDC3B)");
        setProbability("Змея(\uD83D\uDC0D)", 80);
        setProbability("Лошадь(\uD83D\uDC0E)", 40);
        setProbability("Олень(\uD83E\uDD8C)", 80);
        setProbability("Кролик(\uD83D\uDC07)", 80);
        setProbability("Мышь(\uD83D\uDC2D)", 90);
        setProbability("Коза(\uD83D\uDC10)", 70);
        setProbability("Овца(\uD83D\uDC11)", 70);
        setProbability("Кабан(\uD83D\uDC17)", 50);
        setProbability("Буйвол(\uD83D\uDC03)", 20);
        setProbability("Утка(\uD83E\uDD86)", 10);
    }


}
