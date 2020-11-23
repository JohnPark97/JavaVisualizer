package components;

public class Triple {

    int x,y,duration;

    public Triple(int x, int y, int duration){
        this.x = x;
        this. y = y;
        this.duration = duration;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public double getDuration(){
        return this.duration;
    }

    public void lowerDuration(int n){
        this.duration = this.duration - n;
    }
}
