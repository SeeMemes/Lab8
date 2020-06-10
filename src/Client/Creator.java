package Client;

import Client.Exceptions.HumanValueException;

import java.util.*;

public class Creator {


    public static String StringDataCreator ()
    {
        String result = "";
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Введите имя: ");
            result+=" " + scanner.nextLine();

            System.out.print("Введите координату X: ");
            result+=" " + scanner.nextFloat();

            System.out.print("Введите координату Y: ");
            result+=" " + scanner.nextInt();

            System.out.println("Является ли персонаж настоящим героем? (true/false)");
            result+=" " + scanner.nextBoolean();

            System.out.println("Обладает ли персонаж зубной щеткой? (true/false)");
            result+=" " + scanner.nextBoolean();

            System.out.println("Введите скорость удара: ");
            result+=" " + scanner.nextInt();

            System.out.println("Введите оружие персонажа из предложенных (Hammer  Pistol  Shotgun  Rifle  Knife): ");
            scanner.nextLine();
            String str = scanner.nextLine();
            if (str.toUpperCase().equals("HAMMER")) result+=" " + str;
            else if (str.toUpperCase().equals("PISTOL")) result+=" " + str;
            else if (str.toUpperCase().equals("SHOTGUN")) result+=" " + str;
            else if (str.toUpperCase().equals("RIFLE")) result+=" " + str;
            else if (str.equals("KNIFE")) result+=" " + str;
            else throw new HumanValueException("Enter correct data type");

            System.out.println("Введите настроение персонажа из предложенных (Sadness  Sorrow  Gloom  Rage  Frenzy): ");
            str = scanner.nextLine();
            if (str.toUpperCase().equals("SADNESS")) result+=" " + str;
            else if (str.toUpperCase().equals("SORROW")) result+=" " + str;
            else if (str.toUpperCase().equals("GLOOM")) result+=" " + str;
            else if (str.toUpperCase().equals("RAGE")) result+=" " + str;
            else if (str.toUpperCase().equals("FRENZY")) result+=" " + str;
            else throw new HumanValueException("Enter correct data type");

            System.out.println("По настоящему ли крута машина персонажа (true/false): ");
            result+=" " + scanner.nextBoolean();


        } catch (
                InputMismatchException e) { System.out.println("Введите правильный тип данных. Попытайтесь снова, введя команду заново.");
        } catch(
                NoSuchElementException e) { System.out.println("Вы не ввели id персонажа. Попытайтесь снова, введя команду заново"); }
        return result;
    }
}
