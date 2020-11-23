package components;

import ui.Game;

import java.awt.*;
import java.awt.Rectangle;
import java.util.Random;

public class Obstacle {

    Game game;

    private int x;
    private int y;
    private int health = 1;

    private int width,height;

    public Obstacle(Game game, int xPos, int yPos, int hp){
        this.width = game.getWidth()/7;
        this.height = game.getHeight()/15;
        this.game = game;
        this.x = xPos - this.width/2;
        this.y = yPos;
        this.health = hp;
    }

    public Obstacle(Game game, int xPos, int yPos){
        this.width = game.getWidth()/7;
        this.height = game.getHeight()/15;
        this.game = game;
        this.x = xPos - this.width/2;
        this.y = yPos;

    }

    public void render(Graphics2D g2d) {
        g2d.setColor(Color.CYAN);
        g2d.fillRect(x, y, width, height);
    }

    public void setHP(int hp) {
        this.health = hp;
    }

    public boolean damageObstacle(int damage) {
        this.health -= damage;
        if (this.health <= 0) {
            return true;
        }

        return false;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

}
