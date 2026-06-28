package character;
import combat.StatusEffect;
import inventory.Item;
import java.util.ArrayList;
import java.util.List;

public class Player extends Character {
    public ArrayList<Item> inventory = new ArrayList<>();
    public List<StatusEffect> effects = new ArrayList<>();
    public String playerClass;
    public int maxHp;
    public Player(
            String name,
            int hp,
            int damage,
            int armorClass,
            String playerClass,
            int maxHp
    ) {
        super(name, hp, damage, armorClass);

        this.playerClass = playerClass;
        this.maxHp = maxHp;
    }
}