package combat;

import character.Character;

public class DamageCalculator {
    static void attack(Character attacker, Character defender) {

        int roll = Dice.rollD20();
        int damage = attacker.damage;

        System.out.println(attacker.name + " бросает кубик: " + roll);

        if (roll == 1) {
            System.out.println("💨 ПРОМАХ!");
            return;
        }

        if (roll == 20) {
            damage *= 2;
            System.out.println("🔥 КРИТ!");
        }

        if (defender.hasEffect("GUARD")) {
            System.out.println("🛡️ " + defender.name + " под защитой!");
            damage -= 3;

            if (damage < 0) damage = 0;
        }

        if (roll >= defender.armorClass) {
            defender.takeDamage(damage);
            System.out.println("💥 " + attacker.name + " наносит " + damage + " урона!");
        } else {
            System.out.println("🛡️ " + defender.name + " увернулся!");
        }
    }
}
