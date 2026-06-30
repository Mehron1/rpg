package combat;

import character.Player;
import character.Enemy;
import character.Character;
import java.util.Iterator;
import java.util.Scanner;

public class CombatEngine {

    public static void fight(Player player, Enemy enemy, Scanner scanner) {

        int round = 1;
        boolean ringPowerUsed = false;

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

            if (hasRing(player) && !ringPowerUsed && player.hp <= 20) {
                activateRingPower(player);
                ringPowerUsed = true;
            }

            if (playerTurn(player, enemy, scanner, ringPowerUsed)) {
                ringPowerUsed = true;
            }

            if (enemy.hp <= 0) {
                break;
            }

            if (enemyTurn(player, enemy, ringPowerUsed)) {
                ringPowerUsed = true;
            }

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
    static boolean playerTurn(Player player, Enemy enemy, Scanner scanner, boolean ringPowerUsed) {

        System.out.println("\n🎮 ТВОЙ ХОД");
        System.out.println("1 - Атака");
        System.out.println("2 - Защита");

        if (hasRing(player)) {
            System.out.println("3 - Надеть Кольцооо...");
        }

        int choice = scanner.nextInt();

        switch (choice) {

            case 1 -> {
                DamageCalculator.attack(player, enemy);
            }

            case 2 -> {
                player.effects.add(new StatusEffect("GUARD", 1, 3));
                System.out.println("🛡️ Защита активирована");
            }

            case 3 -> {
                if (!hasRing(player)) {
                    System.out.println("У Смеагола ещё нет Кольца.");
                } else if (ringPowerUsed) {
                    System.out.println("💍 Кольцо уже помогло в этом бою.");
                } else {
                    return useSkill(player);
                }
            }
            default -> System.out.println("Неверное действие.");
        }

        return false;
    }
    static boolean useSkill(Player player) {

        return activateRingPower(player);
    }
    static boolean enemyTurn(Player player, Enemy enemy, boolean ringPowerUsed) {

        if (player.hasEffect("INVISIBLE")) {
            System.out.println("💍 Смеагол невидим. Враг не может на него напасть.");
            return false;
        }

        if (enemy.hp < enemy.maxHp * 0.3) {

            System.out.println("👹 Враг паникует и защищается!");
            enemy.effects.add(new StatusEffect("GUARD", 2, 2));
            return false;
        }

        int roll = Dice.rollD20();
        int damage = enemy.damage;

        System.out.println(enemy.name + " бросает кубик: " + roll);

        if (roll == 1) {
            System.out.println("💨 ПРОМАХ!");
            return false;
        }

        if (roll == 20) {
            damage *= 2;
            System.out.println("🔥 КРИТ!");
        }

        if (player.hasEffect("GUARD")) {
            System.out.println("🛡️ " + player.name + " под защитой!");
            damage -= 3;

            if (damage < 0) damage = 0;
        }

        if (roll < player.armorClass) {
            System.out.println("🛡️ " + player.name + " увернулся!");
            return false;
        }

        if (hasRing(player) && !ringPowerUsed && player.hp - damage <= 0) {
            System.out.println("💍 Удар должен был стать смертельным, но Кольцо скрывает Смеагола.");
            return activateRingPower(player);
        }

        player.takeDamage(damage);
        System.out.println("💥 " + enemy.name + " наносит " + damage + " урона!");
        return false;
    }
    static boolean activateRingPower(Player player) {

        if (player.hasEffect("INVISIBLE")) {
            System.out.println("💍 Смеагол уже невидим.");
            return false;
        }

        player.effects.add(new StatusEffect("INVISIBLE", 4, 3));
        System.out.println("💍 Кольцо скрывает Смеагола на 4 хода.");
        System.out.println("Враги не могут атаковать его, а к броскам Смеагола прибавляется +3.");
        return true;
    }
    static boolean hasRing(Player player) {
        return player.hasItem("Кольцо");
    }

}
