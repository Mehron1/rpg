package combat;

import character.Enemy;
import character.Player;
import java.util.Scanner;
public class Battle {

    public static void fight(Player player, Enemy enemy, Scanner scanner) {

        System.out.println("\n⚔️ Бой начинается!");
        System.out.println(player.name + " (" + player.playerClass + ")" + " VS " + enemy.name);
        System.out.println(" | HP врага: " + enemy.hp);
        System.out.println(" | Защита врага: " + enemy.armorClass);
        System.out.println(" | HP "+player.name+": " + player.hp);
        System.out.println(" | Защита "+player.name+": " + player.armorClass);
        int playerInit = Dice.rollD20();
        int enemyInit = Dice.rollD20();
        int round = 1;
        System.out.println("\n🎲 ИНИЦИАТИВА:");
        boolean playerFirst = playerInit >= enemyInit;
        System.out.println(player.name + ": " + playerInit);
        System.out.println(enemy.name + ": " + enemyInit);

        while (player.isAlive() && enemy.isAlive()) {

            System.out.println("\n====================");
            System.out.println("⚔️ РАУНД " + round);
            System.out.println("====================");

            if (playerFirst) {
                playerTurn(player, enemy, scanner);
                if (enemy.hp <= 0) break;

                attackEnemy(player, enemy);
            } else {
                attackEnemy(player, enemy);
                if (player.hp <= 0) break;

                playerTurn(player, enemy, scanner);
            }

            round++;
        }

        System.out.println("\n🏁 РЕЗУЛЬТАТ:");

        if (player.hp > 0) {
            System.out.println("🎉"+player.name+" победил! Оставшееся здоровье: "+player.hp);
        } else {
            System.out.println("☠️☠️☠️ "+player.name+"  проиграл...");
        }
    }

    static void playerTurn(Player player, Enemy enemy, Scanner scanner) {

        System.out.println("\n🎮 ТВОЙ ХОД");
        System.out.println("1 - Атака");
        System.out.println("2 - Защита (меньше урона)");
        System.out.println("3 - Лечение");

        int choice = scanner.nextInt();

        switch (choice) {

            case 1 -> attack(player, enemy, scanner);

            case 2 -> {
                player.armorClass += 3;
                System.out.println("🛡️ Ты в защите (+3 AC на ход)");
            }

            case 3 -> {
                player.hp += 20;
                System.out.println("💚 Ты восстановил HP. Текущее HP: "+player.hp);
            }
        }
    }
    static void attack(Player player, Enemy enemy, Scanner scanner) {

        int roll = Dice.rollD20();
        int damage = player.damage;

        System.out.println("🎲 Ты выбросил: " + roll);

        if (roll == 1) {
            System.out.println("💨 ПРОМАХ!");
            return;
        }

        if (roll == 20) {
            damage *= 2;
            System.out.println("🔥 КРИТ!");
        }

        if (roll >= enemy.armorClass) {
            enemy.takeDamage(damage);
            System.out.println("💥 Удар! урон: " + damage +"\n Оставшееся HP у врага: " + enemy.hp);
        } else {
            System.out.println("🛡️ Враг увернулся!");
        }
    }
    static void attackEnemy(Player player, Enemy enemy) {

        int roll = Dice.rollD20();
        int damage = enemy.damage;

        System.out.println("👹 Враг выбросил: " + roll);

        if (roll == 1) {
            System.out.println("🛡️ Враг промахнулся!");
            return;
        }

        if (roll == 20) {
            damage *= 2;
            System.out.println("💀 ВРАГ КРИТ!");
        }

        if (roll >= player.armorClass) {
            enemy.takeDamage(damage);
            System.out.println("💀 Враг попал! урон: " + damage+"\n Оставшееся HP у " + player.name + " "+player.hp);
        } else {
            System.out.println("🛡️ Ты увернулся!");
        }
    }


}