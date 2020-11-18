package components;

import ui.Game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.concurrent.atomic.AtomicBoolean;

public class Player {
    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;

    private final AtomicBoolean canShoot = new AtomicBoolean(true);
    public boolean canTakeDamage = true;

    Game game;

    int x;
    int dx = 0;
    int dxSpeed = 3;
    int y;
    int hp = 5;



    public Player(Game game, int xPos, int yPos) {
        this.game = game;
        x = xPos;
        y = yPos;
    }

    public Player(Game game, int xPos, int yPos, int hp) {
        this.hp = hp;
        this.game = game;
        x = xPos;
        y = yPos;
    }

    public Player(Game game, int xPos, int yPos, int hp, int speed) {
        this.dxSpeed = speed;
        this.hp = hp;
        this.game = game;
        x = xPos;
        y = yPos;
    }

    public void render(Graphics2D g2d) {
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(x, y, WIDTH, HEIGHT);
    }

    public void move() {
        setX(x + dx);
    }

    public void move(int width) {
        setX(x + dx);
        if (getXPos() >= width){
            setX(getXPos()-width);
        } else if (getXPos() <= 0){
            setX(width + x);
        }
    }

    // TODO figure out a way to make movement smoother
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            dx = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            dx = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            canShoot.set(true);
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            dx = -dxSpeed;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            dx = dxSpeed;
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (canShoot.compareAndSet(true, false)) {
                Bullet bullet = new Bullet(true, x + (WIDTH / 2), y);
                game.bullets.add(bullet);
            }
        }
    }

    public void setX(int xPos) {
        x = xPos;
    }

    public void setHp(int hp) { this.hp = hp; }

    public void setCanTakeDamage(boolean canTakeDamage) { this.canTakeDamage = canTakeDamage; }

    public int getXPos() {
        return x;
    }

    public int getYPos() {
        return y;
    }

    public int getHp() { return hp; }

    public boolean canTakeDamage() { return canTakeDamage; }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

}
