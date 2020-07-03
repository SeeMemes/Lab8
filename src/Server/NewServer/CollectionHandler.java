package Server.NewServer;

import Server.Database.DataBase;
import Server.Database.DatabaseController;
import Server.Exceptions.ExistanceException;
import Server.Exceptions.HumanValueException;
import Server.Exceptions.RightException;
import Server.MyOwnClasses.HumanBeing;
import Server.MyOwnClasses.HumanList;
import Server.Tools.Converter;
import Server.Tools.FWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static Server.Tools.Checker.checkHumanList;

public final class CollectionHandler {

    private static HumanList humanList = new HumanList();
    private static LinkedHashMap<Integer, HumanBeing> human = new LinkedHashMap<Integer, HumanBeing>();
    private Date initDate;
    private DataBase dataBase;


    public CollectionHandler(DataBase dataBase) {

        try {
            this.humanList.setHumanBeings(Converter.convertToList(dataBase.updateHumanBeings()));
            this.humanList.setCreationDate(LocalDateTime.now());
            this.human = dataBase.updateHumanBeings();
            this.dataBase = dataBase;
        } catch (SQLException e){
            System.out.println("Ошибка с базой данных");
        }

        initDate = new Date();
    }

    /**
     * Записывает элементы коллекции в файл. Так как необходим нескольким командам, реализован в этом классе.
     */
    public void save(String path) {
        try{
            FWriter.unParse(humanList, path);
        } catch(NoSuchElementException e){
            String newPath = "New_File";
            FWriter.unParse(humanList, newPath);
        }
    }

    public LinkedHashMap<Integer, HumanBeing> getHuman(){
        return human;
    }

    public void setHuman(LinkedHashMap<Integer, HumanBeing> human) { this.human = human; }

    public DataBase getDataBase() {
        return dataBase;
    }

    public HumanList getHumans(){
        return humanList;
    }

    public void setCreationDate(){ humanList.setCreationDate(); }

    public void setHumans(List<HumanBeing> humanBeings) { this.humanList.setHumanBeings(humanBeings); }

    public List<HumanBeing> getHumanBeingList() {
        return humanList.getHumanBeings();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollectionHandler that = (CollectionHandler) o;
        return Objects.equals(initDate, that.initDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(initDate);
    }

    @Override
    public String toString() {
        return "CollectionManager{" +
                ", initDate=" + initDate +
                '}';
    }
}