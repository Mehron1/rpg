package world;

import character.Player;
import game.GameState;
import inventory.Item;

import java.util.Scanner;

public class Village {

    public static void enter(Player player, GameState state, Scanner scanner) {

        state.location = "Деревня";

        System.out.println("\n🏰 Ты в деревне.");

        applyRingEffect(state);

        handleQuestCompletion(player, state);

        offerQuest(player, state, scanner);
    }

    // --------------------

    private static void applyRingEffect(GameState state) {
        if (state.ringFound) {
            state.corruption += 5;
            System.out.println("🌑 Кольцо тяжелеет: +5 коррупции.");
        }
    }

    // --------------------

    private static void handleQuestCompletion(Player player, GameState state) {

        if (!state.questCompleted) return;

        System.out.println("\n👴 Старик увидел медальон!");
        System.out.println("\"Ты нашёл его! Спасибо!\"");

        state.gold += 50;

        removeQuestItem(player);

        state.questCompleted = false;
        state.questAccepted = false;
        state.oldManQuestFinished = true;
    }

    // --------------------

    private static void removeQuestItem(Player player) {

        player.inventory.removeIf(
                item -> item.name.equals("Медальон старика")
        );
    }

    // --------------------

    private static void offerQuest(Player player, GameState state, Scanner scanner) {

        if (state.oldManQuestFinished
                || state.questAccepted
                || state.questCompleted) {
            return;
        }

        System.out.println("\n👴 Старик обращается к тебе.");
        System.out.println("\"В лесу пропал мой медальон.\"");
        System.out.println("\"Поможешь найти его?\"");

        System.out.println("1 - Да");
        System.out.println("2 - Нет");

        int choice = scanner.nextInt();

        if (choice == 1) {

            if (state.ringFound) {
                rejectQuestDueToRing(player, state, scanner);
            } else {
                state.questAccepted = true;
                System.out.println("📜 Квест получен!");
            }

        } else {
            System.out.println("👴 Старик расстроен.");
        }
    }

    // --------------------

    private static void rejectQuestDueToRing(Player player,
                                             GameState state,
                                             Scanner scanner) {

        state.corruption += 7;

        System.out.println("💍 Кольцо влияет на Смеагола...");
        System.out.println("Квест отвергнут.");

        enter(player, state, scanner);
    }
}