package combat;

import character.Player;
import character.Enemy;
import combat.StatusEffect;
import combat.Dice;
import character.Character;
import java.util.Iterator;
import java.util.Scanner;

public class CombatEngine {

    public static void fight(Player player, Enemy enemy, Scanner scanner) {

        int round = 1;

        while (player.hp > 0 && enemy.hp > 0) {

            System.out.println("\n==================");
            System.out.println("⚔️ РАУНД " + round);
            System.out.println(player.name + " ❤️ " + player.hp + "/" + player.maxHp);
            System.out.println(enemy.name + " ❤️ " + enemy.hp + "/" + enemy.maxHp);
            System.out.println("==================");

            applyEffects(player);
            applyEffects(enemy);

            if (player.hp <= 0 || enemy.hp <= 0) {
                break;
            }

            playerTurn(player, enemy, scanner);

            if (enemy.hp <= 0) {
                break;
            }

            enemyTurn(player, enemy);

            round++;
        }

        System.out.println(player.hp > 0 ? "🎉 Победа!" : "☠️ Поражение...");
    }

    static void applyEffects(Character character) {

        Iterator<StatusEffect> iterator = character.effects.iterator();

        while (iterator.hasNext()) {

            StatusEffect effect = iterator.next();

            switch (effect.name) {

                case "POISON" -> {
                    character.hp -= effect.value;
                    System.out.println("☠️ Яд наносит " + effect.value + " урона!");
                }

                case "BLEED" -> {
                    character.hp -= effect.value;
                    System.out.println("🩸 Кровотечение: -" + effect.value + " HP");
                }
            }

            effect.tick();

            if (effect.isExpired()) {
                iterator.remove();
            }
        }
    }
    static void playerTurn(Player player, Enemy enemy, Scanner scanner) {

        System.out.println("\n🎮 ТВОЙ ХОД");
        System.out.println("1 - Атака");
        System.out.println("2 - Защита");
        System.out.println("3 - Использовать способность");

        int choice = scanner.nextInt();

        switch (choice) {

            case 1 -> {
                int roll = Dice.rollD20();

                System.out.println("🎲 Атака: " + roll);

                if (roll >= enemy.armorClass) {
                    enemy.hp -= player.damage;
                    System.out.println("💥 Урон: " + player.damage);
                } else {
                    System.out.println("💨 Промах!");
                }
            }

            case 2 -> {
                player.effects.add(new StatusEffect("GUARD", 1, 3));
                System.out.println("🛡️ Защита активирована");
            }

            case 3 -> useSkill(player, enemy);
            default -> System.out.println("Неверное действие.");
        }
    }
    static void useSkill(Player player, Enemy enemy) {

        int roll = Dice.rollD20();

        System.out.println("🎲 Скилл бросок: " + roll);

        if (roll >= 12) {

            enemy.effects.add(new StatusEffect("POISON", 3, 5));
            System.out.println("☠️ Ты наложил яд!");
        } else {
            System.out.println("💨 Скилл провален");
        }
    }
    static void enemyTurn(Player player, Enemy enemy) {

        if (enemy.hp < enemy.maxHp * 0.3) {

            System.out.println("👹 Враг паникует и защищается!");
            enemy.effects.add(new StatusEffect("GUARD", 1, 2));
            return;
        }

        int roll = Dice.rollD20();

        if (roll >= player.armorClass) {

            int dmg = enemy.damage;
            player.hp -= dmg;

            System.out.println("💀 Враг наносит " + dmg);
        } else {
            System.out.println("🛡️ Ты уклонился!");
        }
    }

}