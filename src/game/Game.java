package game;
import character.Enemy;
import character.Goblin;
import character.Orc;
import character.Player;
import combat.CombatEngine;
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
            System.out.println("🌑 Влияние кольца: " + state.corruption);
            System.out.println("📍 Текущая локация: " + state.location);
            System.out.println("====================");

            System.out.println("\nКуда отправимся?");
            System.out.println("1 - В лес");
            System.out.println("2 - В деревню");
            System.out.println(canEnterCave(state) ? "3 - В пещеру" : "3 - Путь к пещере закрыт");
            System.out.println("4 - К реке");
            System.out.println("5 - Отдых");
            System.out.println("6 - Инвентарь");
            System.out.println("7 - Завершить приключение");
            int choice = scanner.nextInt();

            switch (choice) {

                case 1 -> enterForest(player, state, scanner);
                case 2 -> enterCity(player, state, scanner);
                case 3 -> enterCave(player, state, scanner);
                case 4 -> enterRiver(player, state, scanner);
                case 5 -> rest(player);
                case 6 -> showInventory(player);
                case 7 -> {
                    System.out.println("Смеагол уходит в темноту. Пока что...");
                    return;
                }
                default -> System.out.println("Выбери действие из списка.");
            }
        }
    }
    static void enterForest(Player player, GameState state, Scanner scanner) {

        state.location = "Лес";
        state.visitedForest = true;

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
            System.out.println("🧟 На тебя напал Гоблин!");
            CombatEngine.fight(player, new Goblin(), scanner);
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
    static void enterCity(Player player, GameState state, Scanner scanner) {

        state.location = "Деревня";

        System.out.println("\n🏰 Ты в деревне.");

        if (state.ringFound) {
            state.corruption += 5;
            System.out.println("🌑 Кольцо тяжелеет в кармане. Его влияние растёт: +5.");
        }

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

            System.out.println("\n👴 Старик обращается к тебе.");
            System.out.println("\"В лесу пропал мой медальон.\"");
            System.out.println("\"Поможешь найти его?\"");

            System.out.println("1 - Да");
            System.out.println("2 - Нет");

            int choice = scanner.nextInt();

            if (choice == 1) {
                if (state.ringFound) {
                    state.corruption += 7;
                    System.out.println("Смеагол щурится и шипит на старика:");
                    System.out.println("\"Сам ищи свой медальон. У нас есть дела важнее!\"");
                    System.out.println("Квест не взят. Влияние кольца +7.");
                    System.out.println("Смеагол резко разворачивается и уходит в лес.");
                    enterForest(player, state, scanner);
                    return;
                }

                state.questAccepted = true;
                System.out.println("📜 Квест получен!");
            } else {
                System.out.println("👴 Старик выглядит расстроенным.");
            }
        }
    }
    static void enterCave(Player player, GameState state, Scanner scanner) {

        if (!canEnterCave(state)) {
            System.out.println("\n⛰️ Дорога к пещере пока закрыта.");
            System.out.println("Смеаголу нужно побывать в лесу, найти кольцо и разобраться с Деаголом.");
            return;
        }

        state.location = "Пещера";

        System.out.println("\n⛰️ Тёмная пещера...");
        System.out.println("Здесь холодно и сыро. Пещеру ещё предстоит исследовать глубже.");

        int event = Dice.rollD20();

        if (event < 15) {
            System.out.println("👹 БОСС ВСТРЕЧА!");
            CombatEngine.fight(player, new Orc(), scanner);
        } else if (event < 18) {
            System.out.println("🐟 В подземном ручье ты поймал рыбу и восстановил силы.");
            player.hp = Math.min(player.maxHp, player.hp + 10);
        } else {
            System.out.println("💀 Пусто... но тревожно.");
            state.corruption += 5;
        }
    }
    static void enterRiver(Player player, GameState state, Scanner scanner) {

        state.location = "Река";

        System.out.println("\n🌊 Смеагол и Деагол идут на рыбалку к реке.");
        System.out.println("Вода тихая, но под гладью что-то сверкает.");

        if (!state.ringFound) {
            findRingWithDeagol(player, state, scanner);
            return;
        }

        int event = Dice.rollD20();

        if (event < 12) {
            System.out.println("🐟 Смеагол ловит рыбу и немного восстанавливает силы.");
            player.hp = Math.min(player.maxHp, player.hp + 8);
        } else if (event < 18) {
            System.out.println("💰 На берегу нашлось несколько монет.");
            state.gold += 5;
        } else {
            System.out.println("🌑 Смеагол смотрит на воду и вспоминает, как всё началось.");
            state.corruption += 3;
        }
    }
    static void findRingWithDeagol(Player player, GameState state, Scanner scanner) {

        System.out.println("Деагол тянет удочку и падает в воду.");
        System.out.println("Через мгновение он выбирается на берег с маленьким золотым кольцом.");
        System.out.println("\"Смотри, Смеагол! Что я нашёл!\"");
        System.out.println("У Смеагола сегодня день рождения.");
        System.out.println("1 - Попросить кольцо в подарок на день рождения");
        System.out.println("2 - Напасть на Деагола и забрать кольцо");
        System.out.println("3 - Оставить кольцо Деаголу");

        int choice = scanner.nextInt();

        if (choice == 1) {
            System.out.println("Деагол крепко сжимает кольцо.");
            System.out.println("\"Нет, Смеагол. Я нашёл его. Оно моё.\"");
            System.out.println("Слова режут хуже ножа. Смеагол бросается на Деагола.");
            killDeagolAndTakeRing(player, state, 18);
        } else if (choice == 2) {
            System.out.println("Смеагол бросается на Деагола и срывает кольцо с его руки.");
            killDeagolAndTakeRing(player, state, 22);
        } else {
            System.out.println("Смеагол отводит взгляд, но блеск кольца остаётся в памяти.");
            state.corruption += 4;
        }
    }
    static void killDeagolAndTakeRing(Player player, GameState state, int corruption) {

        state.deagolKilled = true;
        System.out.println("Деагол больше не отвечает. На берегу остаётся только шум воды.");
        takeRing(player, state, corruption);
    }
    static void takeRing(Player player, GameState state, int corruption) {

        if (!state.ringFound) {
            player.inventory.add(
                    new Item(
                            "Кольцо",
                            "Золотое кольцо, найденное Деаголом на рыбалке. Оно зовёт Смеагола к себе."
                    )
            );
            player.damage += 2;
            player.armorClass += 1;
            state.ringFound = true;
        }

        state.corruption += corruption;

        System.out.println("Смеагол прижимает кольцо к себе. \"Моя прелесть...\"");
        System.out.println("Урон +2, защита +1, коррупция +" + corruption + ".");
    }
    static boolean canEnterCave(GameState state) {
        return state.visitedForest && state.ringFound && state.deagolKilled;
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
