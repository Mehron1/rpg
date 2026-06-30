package character;

public class Enemy extends Character {
    public int maxHp;
    public Enemy(
            String name,
            int hp,
            int damage,
            int armorClass
    ) {
        super(name, hp, damage, armorClass);
        this.maxHp = hp;
    }
}
