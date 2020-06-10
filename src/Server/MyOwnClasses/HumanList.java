/**
 * This class contains the list of HumanBeings
 *
 * @param getHumanBeings allows us to get the List of HumanBeings from this class
 * @param getHumanBeing allows us to get concrete HumanBeing from list
 * @param setHumanBeings allows us to set our List of HumanBeings to this class
 */

package Server.MyOwnClasses;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="humanList")
public class HumanList {
    private List<HumanBeing> humanBeings = new ArrayList<HumanBeing>(); //список людей (HumanBeing)
    private java.time.LocalDateTime creationDate;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void setCreationDate() { this.creationDate = LocalDateTime.now(); }

    @XmlElement(name="creationDate")
    public String getCreationDate() { return creationDate.format(formatter); }
    public void setCreationDate (LocalDateTime Date){ this.creationDate = Date;}


    @XmlElement(name="humanBeing")
    public List<HumanBeing> getHumanBeings() {  //getter для списка людей
        return humanBeings;
    }
    public void setHumanBeings(List<HumanBeing> humanBeings) {
        this.humanBeings = humanBeings;
    }

    public HumanBeing getHumanBeing(int i){ //getter для конкретного человека
        return humanBeings.get(i);
    }
}
