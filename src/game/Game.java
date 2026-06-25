package game;
import character.Enemy;
import character.Goblin;
import character.Orc;
import character.Player;
import combat.Battle;
import combat.Dice;
import inventory.Item;

import java.util.Scanner;
public class Game {

    public static void start(Player player) {

        Scanner scanner = new Scanner(System.in);
        GameState state = new GameState();

        while (player.hp > 0) {

            System.out.println("\n====================");
            System.out.println("❤️ HP: " + player.hp + "/" + player.maxHp);
            System.out.println("💰 Золото: " + state.gold);
            System.out.println("🌑 Коррупция: " + state.corruption);
            System.out.println("📍 Текущая локация: " + state.location);
            System.out.println("====================");

            System.out.println("\nКуда пойти?");
            System.out.println("1 - В лес");
            System.out.println("2 - В Шир");
            System.out.println("3 - В пещеру");
            System.out.println("4 - Отдых");
            System.out.println("5 - Инветарь");
            int choice = scanner.nextInt();

            switch (choice) {

                case 1 -> enterForest(player, state);
                case 2 -> enterCity(player, state);
                case 3 -> enterCave(player, state);
                case 4 -> rest(player);
                case 5 -> showInventory(player);
            }
        }
    }
    static void enterForest(Player player, GameState state) {

        state.location = "Лес";

        System.out.println("\n🌲 Ты вошёл в лес...");
        if (state.questAccepted && !state.questCompleted) {

            int chance = Dice.rollD20();

            if (chance >= 15) {

                System.out.println("✨ Под деревом ты заметил медальон!");

                player.inventory.add(
                        new Item(
                                "Медальон старика",
                                "Старый серебряный медальон"
                        )
                );
                state.questCompleted = true;

                return;
            }
        }
        int event = Dice.rollD20();

        if (event < 12) {
            System.out.println("🧟 На тебя напал Орк!");
            Battle.fight(player, new Goblin(), new Scanner(System.in));
        }

        else if (event < 18) {
            System.out.println("💰 Ты нашёл золото!");
            state.gold += 10;
        }

        else {
            System.out.println("🌿 Спокойный лес. Ты отдыхаешь.");
            player.hp = Math.min(
                    player.maxHp,
                    player.hp + 5
            );
        }
    }
    static void enterCity(Player player, GameState state) {

        state.location = "Деревня";

        System.out.println("\n🏰 Ты в деревне.");
        if (state.questCompleted) {

            System.out.println("\n👴 Старик увидел медальон!");

            System.out.println("\"Ты нашёл его! Спасибо!\"");

            state.gold += 50;

            System.out.println("💰 Получено золото: 50");
            Item foundItem = null;

            for (Item item : player.inventory) {

                if (item.name.equals("Медальон старика")) {
                    foundItem = item;
                    break;
                }
            }

            if (foundItem != null) {
                player.inventory.remove(foundItem);
            }
            state.questCompleted = false;
            state.questAccepted = false;
            state.oldManQuestFinished = true;
            return;
        }
        if (!state.oldManQuestFinished
                && !state.questAccepted
                && !state.questCompleted) {

            Scanner scanner = new Scanner(System.in);

            System.out.println("\n👴 Старик обращается к тебе.");
            System.out.println("\"В лесу пропал мой медальон.\"");
            System.out.println("\"Поможешь найти его?\"");

            System.out.println("1 - Да");
            System.out.println("2 - Нет");

            int choice = scanner.nextInt();

            if (choice == 1) {
                state.questAccepted = true;
                System.out.println("📜 Квест получен!");
            } else {
                System.out.println("👴 Старик выглядит расстроенным.");
            }
        }
    }
    static void enterCave(Player player, GameState state) {

        state.location = "Пещера";

        System.out.println("\n⛰️ Тёмная пещера...");

        int event = Dice.rollD20();

        if (event < 15) {
            System.out.println("👹 БОСС ВСТРЕЧА!");
            Battle.fight(player, new Orc(), new Scanner(System.in));
        } else {
            System.out.println("💀 Пусто... но тревожно");
            state.corruption += 5;
        }
    }
    static void rest(Player player) {

        System.out.println("💤 Ты отдыхаешь...");

        player.hp += 20;

        if (player.hp > player.maxHp) {
            player.hp = player.maxHp;
        }

        System.out.println("💚 HP восстановлено: " + player.hp);
    }
        static void showInventory(Player player) {

            System.out.println("\n🎒 Инвентарь:");

            if (player.inventory.isEmpty()) {
                System.out.println("Пусто.");
                return;
            }

            for (Item item : player.inventory) {

                System.out.println(
                        "- " + item.name
                );

                System.out.println(
                        "  " + item.description
                );
            }
        }

}