package eval;

import java.util.List;

public class Wave {
    private List<Enemy> enemies;
    private Movements movements;

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    public Movements getMovements() {
        return movements;
    }

    public void setMovements(Movements movements) {
        this.movements = movements;
    }
}
