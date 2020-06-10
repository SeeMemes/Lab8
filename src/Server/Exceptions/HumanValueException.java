/**
 * This exception allows us to recreate a class if we made a logical mistake while making a new HumanBeing
 */

package Server.Exceptions;

import java.util.InputMismatchException;

public class HumanValueException extends InputMismatchException {
    public HumanValueException (String message){
        super (message);
    }
}
