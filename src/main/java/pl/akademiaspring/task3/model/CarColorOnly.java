package pl.akademiaspring.task3.model;

public class CarColorOnly {

    private long carId;
    private Color color;

    public CarColorOnly() {
    }

    public CarColorOnly(long id, Color color) {
        this.carId = id;
        this.color = color;
    }

    public long getId() {
        return carId;
    }

    public void setId(long id) {
        this.carId = id;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
