package Server.SocketServer;

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
import java.util.*;

import static Server.Tools.Checker.checkHumanList;

public final class CollectionManager {

    private static HumanList humanList;
    private static LinkedHashMap<Integer, HumanBeing> human = new LinkedHashMap<Integer, HumanBeing>();
    /*private Gson serializer;*/
    private Type collectionType;
    private File xmlCollection;
    private Date initDate;
    private String collectionPath;

    /*{
        citizens = Collections.synchronizedList(new LinkedList<>());
        serializer = new Gson();
        collectionType = new TypeToken<LinkedList<Shorty>>() {}.getType();
    }*/

    /**
     * Предоставляет доступ к коллекции и связанному с ней файлу.
     * @param collectionPath путь к файлу коллекции в файловой системе.
     */
    public CollectionManager(String collectionPath) {
        try {
            if (collectionPath == null) throw new FileNotFoundException();
        } catch (FileNotFoundException ex) {
            System.err.println("Путь к файлу должен быть задан аргументом командной строки");
            System.exit(1);
        }
        this.collectionPath = collectionPath;
        xmlCollection = new File(collectionPath);
        try {
            String extension = xmlCollection.getAbsolutePath().substring(xmlCollection.getAbsolutePath().lastIndexOf(".") + 1);
            if (!xmlCollection.exists() | !extension.equals("xml")) throw new FileNotFoundException();
            if (!xmlCollection.canRead() || !xmlCollection.canWrite()) throw new SecurityException();
            try (BufferedReader collectionReader = new BufferedReader(new FileReader(xmlCollection))) {
                System.out.println("Загрузка коллекции " + xmlCollection.getAbsolutePath());
                String nextLine;
                StringBuilder result = new StringBuilder();
                while ((nextLine = collectionReader.readLine()) != null) result.append(nextLine);
                try {
                    File file = new File(collectionPath);
                    if ((!file.canRead() || !file.canWrite()) && file.exists()) throw new RightException("Возникла ошибка с правами файла");
                    if (!file.exists()) throw new ExistanceException("Данного файла не существует");
                    JAXBContext jaxbContext = JAXBContext.newInstance(HumanList.class);
                    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                    HumanList humanList = (HumanList) jaxbUnmarshaller.unmarshal(file);
                    this.humanList = humanList;
                    try{
                        for (int i = 0; i < humanList.getHumanBeings().size(); i++) humanList.getHumanBeing(i).setId(i+1);
                    }catch (NullPointerException e){}
                    try {
                        checkHumanList(humanList);
                    } catch (HumanValueException e){
                        e.printStackTrace();
                    }
                    this.human = Converter.сonvertToLinkedHashMap(humanList);
                    System.out.println("Коллекция успешно загружена. Добавлено " + humanList.getHumanBeings().size() + " элементов.");
                } catch (JAXBException e) {
                    System.err.println("Ошибка синтаксиса XML. " + e.getMessage());
                } catch (RightException | ExistanceException e) {
                    System.err.println("Ошибка c доступом к файлу. ");
                    System.exit(1);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Файл по указанному пути не найден или он пуст.");
            System.exit(1);
        } catch (SecurityException e) {
            System.err.println("Файл защищён от чтения.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Что-то не так с файлом.");
            System.exit(1);
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

    public HumanList getHumans(){
        return humanList;
    }

    public void setCreationDate(){ humanList.setCreationDate(); }

    public void setHumans(List<HumanBeing> humanBeings) { this.humanList.setHumanBeings(humanBeings); }

    public List<HumanBeing> getHumanBeingList() {
        return humanList.getHumanBeings();
    }

    public String getCollectionPath(){ return this.collectionPath; }

    public File getxmlCollection() {
        return xmlCollection;
    }

    public Type getCollectionType() {
        return collectionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollectionManager that = (CollectionManager) o;
        return Objects.equals(collectionType, that.collectionType) &&
                Objects.equals(xmlCollection, that.xmlCollection) &&
                Objects.equals(initDate, that.initDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(collectionType, xmlCollection, initDate);
    }

    @Override
    public String toString() {
        return "CollectionManager{" +
                "collectionType=" + collectionType +
                ", xmlCollection=" + xmlCollection +
                ", initDate=" + initDate +
                '}';
    }
}