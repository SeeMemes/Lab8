/**
 * This class allows us to parse xml to HumanList and check if there are any issues
 *
 * @exception Exceptions.HumanValueException is thrown if there are some class creation rules violations
 * @throws checkHumanList checks HumanList on issue existence
 * @param parse allows us to parse xml to HumanList
 * @param getHumanList allows us to get HumanList from this class
 */

package Server.Tools;

import Server.Exceptions.ExistanceException;
import Server.Exceptions.HumanValueException;
import Server.Exceptions.RightException;
import Server.MyOwnClasses.HumanList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

import static Server.Tools.Checker.checkHumanList;


public class Parser{
    private static HumanList humanList;
    public  void parse(String string) throws RightException, JAXBException, ExistanceException {

            File file = new File(string);
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
    }
    public static HumanList getHumanList(){
        return humanList;
    }
}
