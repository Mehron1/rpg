package character;

import combat.StatusEffect;

import java.util.ArrayList;
import java.util.List;

public class Character {

    public String name;
    public int hp;
    public int damage;
    public int armorClass;
    public List<StatusEffect> effects = new ArrayList<>();
    public Character(
            String name,
            int hp,
            int damage,
            int armorClass
    ) {
        this.name = name;
        this.hp = hp;
        this.damage = damage;
        this.armorClass = armorClass;
    }
    public void takeDamage(int damage) {
        hp -= damage;
    }
    public boolean isAlive() {
        return hp > 0;
    }
}