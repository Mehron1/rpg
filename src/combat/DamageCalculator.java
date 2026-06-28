package combat;

import character.Character;

public class DamageCalculator {
    static void attack(Character attacker, Character defender) {

        int roll = Dice.rollD20();
        int damage = attacker.damage;

        System.out.println("🎲 " + attacker.name + " выбросил " + roll);

        if (roll == 1) {
            System.out.println("💨 ПРОМАХ!");
            return;
        }

        if (roll == 20) {
            damage *= 2;
           defender.takeDamage(damage);

            System.out.println("🔥 КРИТ!");
            System.out.println("💥 Урон: " + damage);

            return;
        }

        if (roll >= defender.armorClass) {
            defender.takeDamage(damage);
            System.out.println(attacker.name +
                    " наносит " +
                    damage +
                    " урона!");
        } else {
            System.out.println("🛡️ Враг увернулся!");
        }
    }
}
