package ui;

import components.*;
import eval.Enemy;
import eval.MovementValue;
import eval.Settings;
import eval.Wave;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collections;
import java.util.Stack;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

public class Game extends JPanel {
    
    int timer = 0;
    int waveCounter = 0;

    int marginLeft = 25;
    int marginRight = 25;
    int marginTop = 50;
    int marginBottom = 50;
    int windowCenter, windowHeight, windowWidth;
    Triple first = new Triple(-1, 0, 5000);
    Triple second = new Triple(1, 0, 3000);
    Movement movement;

    MathExpression mathExpression = null;


    private Settings settings;

    public CopyOnWriteArrayList<Alien> aliens = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<Obstacle> obstacles = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<Bullet> bullets = new CopyOnWriteArrayList<>();

    private final Player player;
    private int score = 0;
    private boolean isOver = false;


    public Game(JFrame frame, Settings s) {
        windowWidth = frame.getWidth();
        windowCenter = windowWidth / 2;
        windowHeight = frame.getHeight();
        this.settings = s;

        createAliens();

        // todo: allow varied speed for player
        player = new Player(this, (windowWidth - Player.WIDTH) / 2,
                windowHeight - marginBottom - Player.HEIGHT,
                this.settings.getPlayer().getHp(),
                this.settings.getPlayer().getSpeed().getC_speed());

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                player.keyReleased(e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                player.keyPressed(e);
            }
        });
        setFocusable(true);

        createObstacles(s.getBlockade().getSpawn(),s.getBlockade().getHp());

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        super.setBackground(Color.DARK_GRAY);

        Graphics2D g2d = (Graphics2D) g;

        renderAliens(g2d);
        renderObstacles(g2d);
        player.render(g2d);
        renderBullets(g2d);
        renderInfo(g2d);

        if(isOver) {
            renderGameOver(g2d);
        }
    }

    private void createObstacles(int n,int hp){
        int spacing =  windowWidth / (n+1);

        while (n > 0) {
            obstacles.add(new Obstacle(this, spacing * n, windowHeight - (windowHeight / 3), hp));
            n--;
        }
    }

    private void renderObstacles(Graphics2D g2d) {
        for (Obstacle o : obstacles) {
            o.render(g2d);
        }
    }

    private void createAliens() {
        List<Wave> waves = this.settings.getWaves();
        List<Enemy> enemies = waves.get(waveCounter).getEnemies();

        for (int i = 0; i < enemies.size(); i++) {
            int drawingSpace = (windowWidth - (Alien.WIDTH + Alien.SPACING) * enemies.get(i).getSpawn()) / 2;
            for (int k = 0; k < enemies.get(i).getRow(); k++) {
                for (int j = 0; j < enemies.get(i).getSpawn(); j++) {
                    int x = (drawingSpace + (j * (Alien.WIDTH + Alien.SPACING)));
                    int y = marginTop + ((k+i) * (Alien.WIDTH + Alien.SPACING));
                    Enemy e = enemies.get(i);
                    aliens.add(new Alien(this, x, y,
                            e.getHp(), e.getPoints(), e.getSpeed()));

                }
            }
        }

        Stack<Triple> arrayOfTriples = new Stack<>();
        Stack<Triple> lastTriples = new Stack<>();

        List<eval.Movement> mvs = waves.get(waveCounter).getMovements().getMovements();
        for (int i = 0; i < mvs.size(); i++) {
            eval.Movement m = mvs.get(i);
            int currLoop = 0;
            while(currLoop < m.getLoop()) {
                for (int j = 0; j < m.getMovementValue().size(); j++) {
                    MovementValue mv = m.getMovementValue().get(j);
                    int x = getXFromDirection(mv.getDirection());
                    int y = getYFromDirection(mv.getDirection());

                    Triple t = new Triple(x, y, mv.getSpeed());
                    arrayOfTriples.add(t);
                }
                currLoop++;
            }
        }
        Collections.reverse(arrayOfTriples);
        // populate last movement (to infinitely loop over)
        for (int j = 0; j < mvs.get(mvs.size()-1).getMovementValue().size(); j++) {
            MovementValue mv = mvs.get(mvs.size()-1).getMovementValue().get(j);
            int x = getXFromDirection(mv.getDirection());
            int y = getYFromDirection(mv.getDirection());
            Triple t = new Triple(x, y, mv.getSpeed());
            lastTriples.add(t);
        }

        movement = new Movement(arrayOfTriples, lastTriples);
    }

    private void renderAliens(Graphics2D g2d) {
        for (Alien a : aliens) {
            a.render(g2d);
        }
    }

    private void renderBullets(Graphics2D g2d) {
        for (Bullet b : bullets) {
            b.move();
            for (Alien a : aliens) {
                if (b.collision(a) && b.isFriendly()) {
                    a.setHp(a.getHp() - 1);
                    if (a.getHp() <= 0) {
                        aliens.remove(a);
                        score += a.getPoints();
                    }
                    bullets.remove(b);
                }
            }
            for (Obstacle o : obstacles) {
                if (b.collision(o)) {
                    if (o.damageObstacle(1)) {
                        obstacles.remove(o);
                    }
                    bullets.remove(b);
                }
            }
            if (b.collision(player) && !b.isFriendly() && player.canTakeDamage()) {
                bullets.remove(b);
                player.setCanTakeDamage(false);
                player.setHp(player.getHp() - 1);
                System.out.println("Player HP remaining: " + player.getHp());
            }
            b.render(g2d);
            if (b.getYPos() < 0 || b.getYPos() > windowHeight) bullets.remove(b);
        }
        // System.out.println(bullets);
    }

    private void renderInfo(Graphics2D g2d) {
        g2d.setFont(new Font("", Font.PLAIN, 18));
        g2d.setColor(Color.WHITE);
        g2d.drawString("Lives: " + player.getHp(), marginLeft, marginTop / 2);
        g2d.drawString("Score: " + score, windowWidth / 4, marginTop / 2);
    }

    public void update() {
        timer++;
        for (Alien a : aliens) {
            int x = a.getXPos();
            if (x < 0){
                x = (windowWidth - marginLeft - Alien.WIDTH - Alien.SPACING) - x;
            } else if (x > (windowWidth - marginLeft - Alien.WIDTH - Alien.SPACING)){
                x = x % (windowWidth - marginLeft - Alien.WIDTH - Alien.SPACING);
            }
            int y = a.getYPos();
            if (y < 0) {
                y = (325 - marginTop - Alien.HEIGHT - Alien.SPACING) - y;
            } else if (y > (325 - marginTop - Alien.HEIGHT - Alien.SPACING)) {
                y = y % (325 - marginTop - Alien.HEIGHT - Alien.SPACING);
            }

            //TODO: Weird Positioning
            if (a.isC_speed()) {
                a.setPosition(x + a.getC_speed() * movement.getX(), y + a.getC_speed() * movement.getY());
            } else {
                a.setPosition(x + a.getV_speed(timer) * movement.getX(), y + a.getV_speed(timer) * movement.getY());
            }
            a.shootBullet();
        }
        // reset player i-frames
        if (!player.canTakeDamage()) {
            if (timer % 200 == 0) {
                player.setCanTakeDamage(true);
            }
        }
        player.move(windowWidth);

        if (player.getHp() <= 0) {
            isOver = true;
        }

        if (aliens.isEmpty()) {
            if(waveCounter >= this.settings.getWaves().size() - 1) {
                isOver = true;
            } else {
                waveCounter++;
                createAliens();
            }
        }
    }

    public boolean isOver() {
        return isOver;
    }

    public void renderGameOver(Graphics2D g2d) {
        g2d.setFont(new Font("", Font.PLAIN, 70));
        g2d.setColor(Color.WHITE);
        g2d.drawString("Game Over", 100, windowHeight / 2);
    }


    private int getXFromDirection(String s) {
        return switch (s) {
            case "LEFT" -> -1;
            case "RIGHT" -> 1;
            default -> 0;
        };
    }

    private int getYFromDirection(String s) {
        return switch (s) {
            case "UP" -> -1;
            case "DOWN" -> 1;
            default -> 0;
        };
    }

    public int getWidth() {
        return this.windowWidth;
    }

    public int getHeight() {
        return this.windowHeight;
    }
}