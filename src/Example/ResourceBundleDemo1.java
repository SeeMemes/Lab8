package Example;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceBundleDemo1 {
    public static void main(String[] args) throws UnsupportedEncodingException {
        //printInfo("", "");
        printInfo("uk", "UA");
    }

    private static void printInfo(String language, String country)
            throws UnsupportedEncodingException {
        Locale current = new Locale(language, country);
        ResourceBundle rb = ResourceBundle.getBundle("example", current);

        String s1 = rb.getString("label.button");
        System.out.println(s1);

        String s2 = rb.getString("message.welcome");
        System.out.println(s2);

        System.out.println();
    }
}
