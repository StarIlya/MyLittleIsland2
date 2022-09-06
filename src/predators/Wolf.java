package predators;



// Волк
public class Wolf extends Predator {
    public Wolf() {
        super("Волк(\uD83D\uDC3A)");
        setProbability("Лошадь",20);
        setProbability("Олень(\uD83E\uDD8C)", 15);
        setProbability("Кролик(\uD83D\uDC07)", 60);
        setProbability("Мышь(\uD83D\uDC2D)", 80);
        setProbability("Коза(\uD83D\uDC10)", 60);
        setProbability("Овца(\uD83D\uDC11)", 70);
        setProbability("Кабан(\uD83D\uDC17)", 15);
        setProbability("Буйвол(\uD83D\uDC03)", 10);
        setProbability("Утка(\uD83E\uDD86)", 40);
    }

}
