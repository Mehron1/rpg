package character;

import combat.StatusEffect;

import java.util.ArrayList;
import java.util.List;

public class Enemy extends Character {
    public List<StatusEffect> effects = new ArrayList<>();
    public int maxHp;
    public Enemy(
            String name,
            int hp,
            int damage,
            int armorClass
    ) {
        super(name, hp, damage, armorClass);
    }
}