package components;

import eval.Speed;
import ui.Game;

import java.awt.*;
import java.awt.Rectangle;
import java.util.Random;

public class Alien {
    public static int WIDTH = 30;
    public static int HEIGHT = 30;
    public static int SPACING = 10;

    Game game;

    private int x;
    private int y;
    private int hp = 1;
    private int points = 10;
    private int c_speed = -1;
    private String v_speed = null;

    private MathExpression mathExpression = null;

    public Alien(Game game, int xPos, int yPos) {
        this.game = game;
        x = xPos;
        y = yPos;
    }

    public Alien(Game game, int xPos, int yPos, int hp, int points, Speed speed) {
        this.hp = hp;
        this.game = game;
        this.points = points;
        if (speed.getC_speed() == -1 ) {
            this.v_speed = speed.getV_speed();
            mathExpression = new MathExpression(v_speed);
        } else this.c_speed = speed.getC_speed();
        x = xPos;
        y = yPos;
    }

    public void render(Graphics2D g2d) {
        g2d.setColor(Color.GREEN);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.fillOval(x, y, WIDTH, HEIGHT);
    }

    public void shootBullet() {
        Random rand = new Random();
        int i = rand.nextInt(400);
        if (i % 400 == 0) {
            Bullet bullet = new Bullet(false, x + (WIDTH / 2), y + (HEIGHT));
            game.bullets.add(bullet);
        }
    }

    public void setPosition(int xPos, int yPos) {
        x = xPos;
        y = yPos;
    }

    public void setHp(int hp) { this.hp = hp; }

    public int getXPos() {
        return x;
    }

    public int getYPos() {
        return y;
    }

    public int getHp() { return hp; }

    public int getPoints() { return points; }

    public int getC_speed() { return c_speed; }

    public int getV_speed(int t) {
        return mathExpression.getValue(t);
    }

    // Returns true if c_speed
    public boolean isC_speed() {
        if (c_speed != -1) {
            return true;
        }
        return false;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }


}
