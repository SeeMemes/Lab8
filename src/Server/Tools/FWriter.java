/**
 * This class allows us to write our answer in xml file "Answer.xml"
 *
 * @param unParse allows us to unparse HumanList to xml string
 * @param write allows us to write xml string to file "Answer.xml"
 */

package Server.Tools;

import Server.Exceptions.ExistanceException;
import Server.Exceptions.RightException;
import Server.MyOwnClasses.HumanList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;


public class FWriter {
    public FWriter(){}
    private static void write(String str, String name){
        try{
            File file = new File(name);
            if (!file.exists()) throw new ExistanceException("Файл не существует");
            if (!file.canWrite()) throw new RightException("Невозможно записать результат в файл");
            OutputStreamWriter filewriter = new OutputStreamWriter(new FileOutputStream(name));
            filewriter.write(str);
            filewriter.close();
            System.out.println("Коллекция сохранена в файл " + name);
        } catch (IOException e) {
            System.out.println("File write failed: " + e.toString());
        } catch (RightException e) {
            System.out.println("Проблема с правами доступа к файлу. Попробуйте изменить права доступа или попробуйте позднее.");
        }
    }
    public static void unParse(HumanList humanList, String path){
        try{
            JAXBContext jaxbContext = JAXBContext.newInstance(HumanList.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter sw = new StringWriter();
            jaxbMarshaller.marshal(humanList, sw);
            String xmlContent = sw.toString();
            write( xmlContent , path);
        } catch(JAXBException e){
            e.printStackTrace();
        }

    }
}
