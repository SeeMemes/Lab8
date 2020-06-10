package Server.Exceptions;

import java.io.FileNotFoundException;

public class ExistanceException extends FileNotFoundException {
    public ExistanceException (String message){
        super (message);
    }
}

