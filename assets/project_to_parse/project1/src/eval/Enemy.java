package eval;

public class Enemy {
    private E_SPRITE sprite;
    private int hp;
    private Speed speed;
    private int points;
    private int row;
    private int spawn;

    public E_SPRITE getSprite() {
        return sprite;
    }

    public void setSprite(E_SPRITE sprite) {
        this.sprite = sprite;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public Speed getSpeed() {
        return speed;
    }

    public void setSpeed(Speed speed) {
        this.speed = speed;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getSpawn() {
        return spawn;
    }

    public void setSpawn(int spawn) {
        this.spawn = spawn;
    }
}
