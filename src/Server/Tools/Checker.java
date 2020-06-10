/**
 * This class allows us to check HumanList or HumanBeing if there is no creation rules violation
 *
 * @throws Exceptions.HumanValueException
 */

package Server.Tools;

import Server.Exceptions.HumanValueException;
import Server.MyOwnClasses.HumanBeing;
import Server.MyOwnClasses.HumanList;

public class Checker {
    public static void checkHuman (HumanBeing humanBeing) throws HumanValueException {
        if (humanBeing.getId() <= 0) throw new HumanValueException("Программой выдано неправильное ID человека");
        if (humanBeing.getName() == null || humanBeing.getName().equals("")) throw new HumanValueException("Значение поля Name не может быть null или быть пустым");
        if (humanBeing.getRealHero() == null) throw new HumanValueException ("Значение поля RealHero не может быть null");
        if (humanBeing.getCoordinates() == null) throw new HumanValueException ("Значение поля Coordinates не может быть null");
        if (humanBeing.getCoordinates().getX() == null || humanBeing.getCoordinates().getX() <= -807) throw new HumanValueException ("Значение поля X не может быть null или <= -807");
        if (humanBeing.getImpactSpeed() == null || humanBeing.getImpactSpeed() > 729) throw new HumanValueException ("Значение поля impactSpeed не может быть null или > 729");
        if (humanBeing.getCar() == null) throw new HumanValueException ("Значение поля car не может быть null");
    }

    public static void checkHumanList (HumanList humanList) throws HumanValueException {
        for (int i = 0; i < humanList.getHumanBeings().size(); i++){
            HumanBeing humanBeing = humanList.getHumanBeing(i);
            checkHuman(humanBeing);
        }
    }
}
