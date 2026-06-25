import character.Enemy;
import character.Player;
import combat.Battle;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите имя героя: ");
        String name = scanner.nextLine();
        System.out.println("\nВыберите класс:");
        System.out.println("1 - Воин");
        System.out.println("2 - Маг");
        System.out.println("3 - Разбойник");

        int choice = scanner.nextInt();
        scanner.nextLine();
        Player player;
        if (choice == 1) {

            player = new Player(
                    name,
                    120,
                    25,
                    14,
                    "Воин"
            );

        }else if (choice == 2) {

            player = new Player(
                    name,
                    80,
                    30,
                    10,
                    "Маг"
            );

        }else {

            player = new Player(
                    name,
                    90,
                    22,
                    12,
                    "Разбойник"
            );

        }
        Enemy goblin = new Enemy("Орк", 85, 15, 11);

        Battle.fight(player, goblin, scanner);
    }
}
