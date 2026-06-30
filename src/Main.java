import character.Player;
import game.Game;

public class Main {

    public static void main(String[] args) {

        Player player = new Player(
                "Смеагол",
                30,     // hp
                6,      // damage
                12,     // armorClass
                "Rogue",
                30      // maxHp
        );

        System.out.println("🎮 Добро пожаловать, дорогой друг. Сейчас ты познакомишься с судьбой того, чью душу загубила " +
                " и опустошила его прелесть... Путешествие начинается!");
        System.out.println("👤 Герой: " + player.name);

        Game.start(player);
    }
}