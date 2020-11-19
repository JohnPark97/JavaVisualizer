package eval;

import java.util.List;

public class Movement {
    private List<MovementValue> movementValue;
    private int loop;

    public int getLoop() {
        return loop;
    }

    public void setLoop(int loop) {
        this.loop = loop;
    }

    public List<MovementValue> getMovementValue() {
        return movementValue;
    }

    public void setMovementValue(List<MovementValue> movementValue) {
        this.movementValue = movementValue;
    }
}
