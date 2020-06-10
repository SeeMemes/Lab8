/**
 * Class Car allows us to contain the answer on the question "Is the character`s car cool?" in the variable of the same name
 */

package Server.MyOwnClasses;

import javax.xml.bind.annotation.XmlElement;

public class Car {
    private boolean cool = true;

    public Car () {}

    @XmlElement
    public boolean isCool() { return cool; }
    public void setCool(boolean cool) { this.cool = cool; }

    @Override
    public String toString() {
        return "Car{" +
                "cool=" + cool +
                '}';
    }
}
