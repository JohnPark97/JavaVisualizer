package eval;

import java.util.List;

public class Settings {
    private Player player;
    private Blockade blockade;
    private List<Wave> waves;


    public Settings (Player player, Blockade blockade, List<Wave> waves) {
        this.player = player;
        this.blockade = blockade;
        this.waves = waves;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Blockade getBlockade() {
        return blockade;
    }

    public void setBlockade(Blockade blockade) {
        this.blockade = blockade;
    }

    public List<Wave> getWaves() {
        return waves;
    }

    public void setWaves(List<Wave> waves) {
        this.waves = waves;
    }

}
