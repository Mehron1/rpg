package world;

import character.Goblin;
import character.Player;
import combat.CombatEngine;
import combat.Dice;
import game.GameState;
import inventory.Item;

import java.util.Scanner;
public class Forest {

    public static void enter(Player player,
                             GameState state,
                             Scanner scanner) {

        state.location = "Лес";
        state.visitedForest = true;

        System.out.println("\n🌲 Ты вошёл в лес...");

        if (checkQuest(player, state)) {
            return;
        }

        randomEvent(player, state, scanner);
    }

    private static boolean checkQuest(Player player,
                                      GameState state) {

        if (!state.questAccepted || state.questCompleted) {
            return false;
        }

        int chance = Dice.rollD20();

        if (chance < 15) {
            return false;
        }

        System.out.println("✨ Под деревом ты заметил медальон!");

        player.inventory.add(
                new Item(
                        "Медальон старика",
                        "Старый серебряный медальон"
                )
        );

        state.questCompleted = true;

        return true;
    }

    private static void randomEvent(Player player,
                                    GameState state,
                                    Scanner scanner) {

        int event = Dice.rollD20();

        System.out.println("Бросаешь кубик... выпадает" + event);
        if (event < 12) {
            fightGoblin(player, scanner);
        }
        else if (event < 18) {
            findGold(state);
        }
        else {
            rest(player);
        }
    }

    private static void fightGoblin(Player player,
                                    Scanner scanner) {

        System.out.println("🧟 На тебя напал Гоблин!");

        CombatEngine.fight(player, new Goblin(), scanner);
    }

    private static void findGold(GameState state) {

        System.out.println("💰 Ты нашёл золото!");

        state.gold += 10;
    }

    private static void rest(Player player) {

        System.out.println("🌿 Спокойный лес. Ты отдыхаешь.");

        player.hp = Math.min(player.maxHp, player.hp + 5);
    }
}