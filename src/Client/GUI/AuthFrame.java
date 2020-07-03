package Client.GUI;

import Client.ServerRequest;
import Server.Database.Credentials;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import static Server.Tools.AppConstant.*;

public class AuthFrame extends JFrame{
    private JTextArea humanBeingTableTextArea;
    private JComboBox languageComboBox;
    private JTextArea loginTextArea;
    private JTextField loginField;
    private JButton loginButton;
    private JButton registerButton;
    private JTextField passwordField;
    private JPanel AuthPanel;
    private JPanel TitlePanel;
    private JPanel LoginStringPanel;
    private JPanel PasswordStringPanel;
    private JTextArea notificationTextArea;
    private JTextArea passwordTextArea;
    private ArrayList<Locale> locale_list = new ArrayList<>();
    private static Credentials credentials = new Credentials(-1, DEFAULT_LOGIN, DEFAULT_PASSWORD);
    private ResourceBundle resourceBundle;

    public AuthFrame () {
        add(AuthPanel);
        locale_list.add(new Locale("ru","RU"));
        locale_list.add(new Locale("be", "BY"));
        locale_list.add(new Locale("sv", "SE"));
        locale_list.add(new Locale("en", "NZ"));
        resourceBundle = ResourceBundle.getBundle("locales", locale_list.get(0));
        System.out.println();
        loginTextArea.setText(resourceBundle.getString("login.textField"));
        passwordTextArea.setText(resourceBundle.getString("password.textField"));
        loginButton.setText(resourceBundle.getString("loginButton.textField"));
        registerButton.setText(resourceBundle.getString("registerButton.textField"));

        setTitle("Human Being Table");
        setSize(800, 400);
        setResizable(false);

        languageComboBox.addActionListener(this::languageChangePerformed);

        loginButton.addActionListener(this::loginPerformed);
        registerButton.addActionListener(this::registerPerformed);
    }

    private void languageChangePerformed(ActionEvent e){
        resourceBundle = ResourceBundle.getBundle("locales", locale_list.get(languageComboBox.getSelectedIndex()));
        loginTextArea.setText(resourceBundle.getString("login.textField"));
        passwordTextArea.setText(resourceBundle.getString("password.textField"));
        loginButton.setText(resourceBundle.getString("loginButton.textField"));
        registerButton.setText(resourceBundle.getString("registerButton.textField"));
    }

    private static String sendData(ServerRequest serverRequest) throws IOException {
        Socket outcoming = new Socket(InetAddress.getLocalHost(), DEFAULT_PORT);
        ObjectOutputStream outputStream = new ObjectOutputStream(outcoming.getOutputStream());
        outputStream.writeObject(serverRequest);
        outputStream.flush();

        InputStreamReader inputStreamReader = new InputStreamReader(outcoming.getInputStream());
        BufferedReader reader = new BufferedReader(inputStreamReader);
        char readChar = (char) reader.read();
        StringBuilder builder = new StringBuilder();
        builder.append(readChar);
        while (reader.ready()) {
            readChar = (char) reader.read();
            builder.append(readChar);
        }
        outcoming.close();
        String answer = builder.toString();
        StringTokenizer stringTokenizer = new StringTokenizer(serverRequest.getArguments());
        if (Boolean.parseBoolean(answer)) credentials = new Credentials(-1,stringTokenizer.nextToken(),stringTokenizer.nextToken());
        return answer;
    }

    private void loginPerformed(ActionEvent e) {
        String name = loginField.getText();
        String password = passwordField.getText();
        notificationTextArea.setForeground(new Color(159,51,49));
        if (name.equals("") || password.equals("")) notificationTextArea.setText(resourceBundle.getString("fillingFieldsException.textField"));
        else {
            ServerRequest serverRequest = new ServerRequest("login", name + " " + password, credentials);
            try {
                String answer = sendData(serverRequest);
                if (Boolean.parseBoolean(answer)) {
                    ClientFrame clientFrame = new ClientFrame(new Credentials(-1, name, password), resourceBundle);
                    clientFrame.setVisible(true);
                    dispose();
                } else notificationTextArea.setText(resourceBundle.getString("wrongPassword.textField"));
            } catch (IOException ex) {
                notificationTextArea.setText(resourceBundle.getString("serverError.textField"));
            }
        }
    }

    private void registerPerformed(ActionEvent e){
        String name = loginField.getText();
        String password = passwordField.getText();
        if (name.equals("") || password.equals("")) notificationTextArea.setText(resourceBundle.getString("fillingFieldsException.textField"));
        else {
            ServerRequest serverRequest = new ServerRequest("register", name + " " + password, credentials);
            try {
                String answer = sendData(serverRequest);
                if (Boolean.parseBoolean(answer)) {
                    notificationTextArea.setForeground(new Color(93,193,83));
                    notificationTextArea.setText(resourceBundle.getString("successfullyRegistered.textField"));
                } else {
                    notificationTextArea.setForeground(new Color(159,51,49));
                    notificationTextArea.setText(resourceBundle.getString("thisNameIsUsedByAnotherUser.textField"));
                }
            } catch (IOException ex) {
                notificationTextArea.setText(resourceBundle.getString("serverError.textField"));
            }
        }
    }
}