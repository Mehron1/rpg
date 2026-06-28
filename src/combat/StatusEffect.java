package combat;

public class StatusEffect {

    public String name;
    public int duration;
    public int value;

    public StatusEffect(String name, int duration, int value) {
        this.name = name;
        this.duration = duration;
        this.value = value;
    }

    public void tick() {
        duration--;
    }

    public boolean isExpired() {
        return duration <= 0;
    }
}