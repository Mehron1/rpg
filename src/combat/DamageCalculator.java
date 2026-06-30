package combat;

import character.Character;

public class DamageCalculator {
    static void attack(Character attacker, Character defender) {

        int roll = Dice.rollD20();
        int attackRoll = roll;
        int damage = attacker.damage;

        if (attacker.hasEffect("INVISIBLE")) {
            attackRoll += 3;
            System.out.println(attacker.name + " бросает кубик: " + roll + " + 3 за невидимость = " + attackRoll);
        } else {
            System.out.println(attacker.name + " бросает кубик: " + roll);
        }

        if (defender.hasEffect("INVISIBLE")) {
            System.out.println("💍 " + defender.name + " невидим. Атака проходит мимо.");
            return;
        }

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

        if (attackRoll >= defender.armorClass) {
            defender.takeDamage(damage);
            System.out.println("💥 " + attacker.name + " наносит " + damage + " урона!");
        } else {
            System.out.println("🛡️ " + defender.name + " увернулся!");
        }
    }
}
