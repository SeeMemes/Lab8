/**
 * Main HumanBeing class which contains variables and methods to get and set them
 */

package Server.MyOwnClasses;

import Server.enums.Mood;
import Server.enums.WeaponType;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@XmlRootElement
public class HumanBeing {
    private long id = 0;
    private String name = "";
    private Coordinates coordinates;
    private java.time.LocalDateTime creationDate;
    private Boolean realHero = true;
    private boolean hasToothpick = true;
    private Integer impactSpeed = 0;
    private WeaponType weaponType = WeaponType.KNIFE;
    private Mood mood = Mood.FRENZY;
    private Car car;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public HumanBeing() {
        this.creationDate = LocalDateTime.now();
        this.coordinates = new Coordinates();
        this.car = new Car();
    }

    public HumanBeing (String name, Coordinates coordinates, Boolean realHero, boolean hasToothpick, Integer impactSpeed, WeaponType weaponType, Mood mood, Car car){
        this.name = name;
        this.coordinates = coordinates;
        this.realHero = realHero;
        this.hasToothpick = hasToothpick;
        this.impactSpeed = impactSpeed;
        this.weaponType = weaponType;
        this.mood = mood;
        this.car = car;
        this.creationDate = LocalDateTime.now();
    }

    @XmlElement
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @XmlElement
    public String getName() { return name; }
    public void setName(String name) {
        this.name = name;
    }



    @XmlElement
    public Coordinates getCoordinates() { return coordinates; }
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
    //public Float getCoordinatesX() { return coordinates.getX(); }
    public void setCoordinatesX(Float x) { coordinates.setX(x); }
    //public int getCoordinatesY() { return coordinates.getY(); }
    public void setCoordinatesY(int y) { coordinates.setY(y); }

    @XmlElement(name="creationDate_of_element")
    public String getCreationDate() { return creationDate.format(formatter); }
    public void setCreationDate() { this.creationDate = LocalDateTime.now(); }

    @XmlElement
    public Boolean getRealHero() {
        return realHero;
    }
    public void setRealHero(Boolean realHero) {
        this.realHero = realHero;
    }

    @XmlElement
    public boolean isHasToothpick() {
        return hasToothpick;
    }
    public void setHasToothpick(boolean hasToothpick) {
        this.hasToothpick = hasToothpick;
    }

    @XmlElement
    public Integer getImpactSpeed() {
        return impactSpeed;
    }
    public void setImpactSpeed(Integer impactSpeed) {
        this.impactSpeed = impactSpeed;
    }

    @XmlElement
    public WeaponType getWeaponType() {
        return weaponType;
    }
    public void setWeaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
    }

    @XmlElement
    public Mood getMood() {
        return mood;
    }
    public void setMood(Mood mood) { this.mood = mood; }

    @XmlElement
    public Car getCar() {
        return car;
    }
    public void setCar(Car car) {
        this.car = car;
    }
    public void setCarCool (boolean cool) { this.car.setCool(cool);}

    @Override
    public String toString() {
        return "HumanBeing{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", realHero=" + realHero +
                ", hasToothpick=" + hasToothpick +
                ", impactSpeed=" + impactSpeed +
                ", weaponType=" + weaponType +
                ", mood=" + mood +
                ", car=" + car +
                '}';
    }
}
