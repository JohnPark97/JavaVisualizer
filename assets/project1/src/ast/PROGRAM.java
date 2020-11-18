package ast;

import eval.*;
import eval.DIRECTION;
import eval.E_SPRITE;
import libs.Node;

import java.util.ArrayList;
import java.util.List;


public class PROGRAM extends Node {
    List<VARS> variables;
    PLAYER player;
    BLOCKADE blockade;
    List<WAVE> waveList;

    public PROGRAM(List<VARS> variables, PLAYER player, BLOCKADE blockade, List<WAVE> waves) {
        this.variables = variables;
        this.player = player;
        this.blockade = blockade;
        this.waveList = waves;
    }

    public List<VARS> getVariables() {
        return variables;
    }

    public PLAYER getPlayer() {
        return player;
    }

    public BLOCKADE getBlockade() {
        return blockade;
    }

    public List<WAVE> getWaveList() {
        return waveList;
    }

    public Settings evaluate() {
        Player player = new Player();
        //set hp
        player.setHp(getPlayer().getHp().getNumber());
        //set speed
        Speed speed = new Speed();

        speed.setC_speed(getPlayer().getSpeed().getNumber());
        try {
            String Fot = getPlayer().getSpeed().getFxn().getFot();
            speed.setV_speed(Fot);
        } catch (NullPointerException e) {
            System.out.println("No function of time value found");
        }
        player.setSpeed(speed);

        Blockade blockade = new Blockade();
        try {
            //set hp
            blockade.setHp(getBlockade().getHp().getNumber());
            //set spawn
            blockade.setSpawn(getBlockade().getSpawn().getNumber());
        } catch (NullPointerException e) {
            System.out.println("No blockade value found");
        }

        List<WAVE> parsedWaves = getWaveList();
        List<Wave> waves = new ArrayList<>();

        for (WAVE parsedWave: parsedWaves){
            Wave wave = new Wave();

            List<ENEMY> parsedEnemies = parsedWave.getEnemies();
            List<Enemy> enemies = new ArrayList<>();

            //Set enemies
            for (ENEMY parsedEnemy: parsedEnemies){
                Enemy enemy = new Enemy();
                ENEMYVALUE enemyvalue = parsedEnemy.getEnemyvalue();

                enemy.setHp(enemyvalue.getHp().getNumber());
                enemy.setPoints(enemyvalue.getPoints().getPoint());

                Speed enemySpeed = new Speed();
                enemySpeed.setC_speed(enemyvalue.getSpeed().getNumber());
                try {
                    enemySpeed.setV_speed(enemyvalue.getSpeed().getFxn().getFot());
                } catch (NullPointerException e) {
                    System.out.println("No function of time value found for enemy hp: "+ enemy.getHp() + " point: " + enemy.getPoints());
                }
                enemy.setSpeed(enemySpeed);

                E_SPRITE e_sprite = E_SPRITE.valueOf(enemyvalue.e_sprite.getSprite());
                enemy.setSprite(e_sprite);

                enemy.setRow(parsedEnemy.getRows().getRow());
                enemy.setSpawn(parsedEnemy.getSpawn().getNumber());

                enemies.add(enemy);
            }
            //Set Movements
            MOVEMENTS parsedMovements = parsedWave.getMovements();
            List<MOVEMENT> parsedMovementList = parsedMovements.getMovement();
            Movements movements = new Movements();
            List<Movement> movementList = new ArrayList<>();

            for (MOVEMENT parsedMovement: parsedMovementList) {
                Movement movement = new Movement();
                List<MOVEMENTVALUE> parsedMovementValues = parsedMovement.getMovementValueList();
                List<MovementValue> movementValues = new ArrayList<>();
                for (MOVEMENTVALUE parsedMovementValue: parsedMovementValues) {
                    MovementValue movementValue = new MovementValue();
                    movementValue.setDirection(parsedMovementValue.getDirection().getDir());
                    movementValue.setSpeed(parsedMovementValue.getSpeed());
                    movementValues.add(movementValue);
                }
                movement.setMovementValue(movementValues);
                try {
                    movement.setLoop(parsedMovement.getLoop().getNumber());
                } catch (NullPointerException e) {
                    System.out.println("No Loop value found for movement: " + movement.getMovementValue());
                }
                movementList.add(movement);
            }
            movements.setMovements(movementList);

            wave.setEnemies(enemies);
            wave.setMovements(movements);

            waves.add(wave);

        }

        System.out.println("Player hp: " + player.getHp());
        System.out.println("Player speed: " + player.getSpeed().getV_speed());
        System.out.println("Player speed: " + player.getSpeed().getC_speed());
        System.out.println("Blockade hp: " + blockade.getHp());
        System.out.println("Bloackade speed: " + blockade.getSpawn());

        return new Settings(player, blockade, waves);
    }

    @Override
    public <C, T> T accept(C context, SpaceInvaderVisitor<C, T> v) {
        return v.visit(context, this);
    }
}