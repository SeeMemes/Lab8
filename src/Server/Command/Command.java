package Server.Command;

import Server.Database.Credentials;
import Server.Database.DatabaseController;
import Server.MyOwnClasses.HumanBeing;
import Server.MyOwnClasses.HumanList;
import Server.MyOwnClasses.HumanBeing;

import java.util.LinkedHashMap;
import java.util.StringTokenizer;

public abstract class Command {
    LinkedHashMap<Integer, HumanBeing> human;
    String command;
    HumanList humanList;
    StringTokenizer tokenizer;

    public Command(LinkedHashMap<Integer, HumanBeing> human, String command, HumanList humanList) {
        this.human = human;
        this.command = command;
        this.humanList = humanList;
        tokenizer = new StringTokenizer(command);
    }
    public Command() {}

    public Object execute (LinkedHashMap<Integer, HumanBeing> human, String command, HumanList humanList, boolean b){
        return human;
    }

    public String execute(){return "Недостаточное кол-во данных";}

    public Boolean execute (DatabaseController databaseController, Credentials credentials){
        boolean registered = false;
        return registered;
    }

    public LinkedHashMap<Integer, HumanBeing> getHuman() { return human; }
    public String getCommand() { return command; }
    public void setHuman(LinkedHashMap<Integer, HumanBeing> human) { this.human = human; }
    public void setCommand(String comand) { this.command = comand; }
    StringTokenizer getTokenizer() { return tokenizer; }

    @Override
    public String toString() {
        return "Comand{" +
                "human=" + human +
                ", comand='" + command + '\'' +
                '}';
    }
}
