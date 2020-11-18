package components;

import java.awt.*;
import java.awt.Rectangle;

// TODO probably have to create invisible hitbox above window to delete bullets
public class Bullet {

    public static final int WIDTH = 2;
    public static final int BULLET_HEIGHT = 5;
    public boolean IS_FRIENDLY;

    int x;
    int y;
    int dy;

    public Bullet(boolean isFriendly, int xPos, int yPos) {
        IS_FRIENDLY = isFriendly;
        x = xPos;
        y = yPos;
        dy = isFriendly ? -1 : 1;
    }

    public void render(Graphics2D g2d) {
        if (IS_FRIENDLY) {
            g2d.setColor(Color.WHITE);
        } else g2d.setColor(Color.RED);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.fillOval(x, y, WIDTH, BULLET_HEIGHT);
    }

    public void move() {
        setY(y + (dy * 5));
    }

    public void setY(int yPos) {
        y = yPos;
    }

    public void setPosition(int xPos, int yPos) {
        x = xPos;
        y = yPos;
    }

    public int getXPos() {
        return x;
    }

    public int getYPos() {
        return y;
    }

    public boolean isFriendly() { return IS_FRIENDLY; }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, BULLET_HEIGHT);
    }

    public boolean collision(Alien alien) {
        return alien.getBounds().intersects(getBounds());
    }

    public boolean collision(Obstacle obstacle) {
        return obstacle.getBounds().intersects(getBounds());
    }

    public boolean collision(Player player) {
        return player.getBounds().intersects(getBounds());
    }
}
