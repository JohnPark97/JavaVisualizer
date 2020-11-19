package eval;

public class Speed {
    // constant speed
    private int c_speed;
    // varied speed (in case of function F(t)
    private String v_speed;

    public int getC_speed() {
        return c_speed;
    }

    public void setC_speed(int c_speed) {
        this.c_speed = c_speed;
    }

    public String getV_speed() {
        return v_speed;
    }

    public void setV_speed(String v_speed) {
        this.v_speed = v_speed;
    }
}
