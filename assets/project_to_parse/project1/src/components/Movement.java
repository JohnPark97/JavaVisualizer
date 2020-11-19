package components;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;



public class Movement {

    Timer timer;

    public Stack<Triple> movements;
    public Stack<Triple> movementCycle;

    int interval = 1000;

    public Movement(Stack<Triple> mvts, Stack<Triple> lastMvts) {
        timer = new Timer();
        timer.schedule(new RemindTask(),
                0,
                1*interval);
        this.movements = mvts;
        this.movementCycle = lastMvts;
    }

    class RemindTask extends TimerTask {
        public void run() {
            if (!movements.empty()) {
                movements.peek().lowerDuration(interval);
                if(movements.peek().getDuration() <= 0){
                    movements.pop();
                }
            } else {
                movements = getDeepClone(movementCycle);
            }
        }
    }

    private Stack<Triple> getDeepClone(Stack<Triple> mvts) {
        Stack<Triple> newStack = new Stack<>();
        Stack<Triple> oldStack = (Stack<Triple>) mvts.clone();
        while (!oldStack.empty()) {
            Triple pop = oldStack.pop();
            Triple newTriple = new Triple(pop.getX(), pop.getY(), pop.duration);
            newStack.push(newTriple);

        }
        return newStack;
    }

    public int getX(){
        if (movements.size() > 0) {
            return movements.peek().getX();
        } else {
            return 0;
        }
    }

    public int getY(){
        if (movements.size() > 0) {
            return movements.peek().getY();
        } else {
            return 0;
        }
    }
}