/**
 * Simple Class which contains x and y position which we can get from it or set
 */

package Server.MyOwnClasses;

import javax.xml.bind.annotation.XmlElement;

public class Coordinates {
    private Float x = 0f; //Значение поля должно быть больше -807, Поле не может быть null
    private int y = 0;

    public Coordinates() {}
    public Coordinates(Float x, int y){
        this.x = x;
        this.y = y;
    }

    @XmlElement
    public void setX(Float x) {
        this.x = x;
    }
    public Float getX() {
        return x;
    }

    @XmlElement
    public void setY(int y) {
        this.y = y;
    }
    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
