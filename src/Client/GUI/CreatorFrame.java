package Client.GUI;

import Client.ServerRequest;
import Server.Database.Credentials;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static Server.Tools.AppConstant.DEFAULT_PORT;

public class CreatorFrame extends JFrame{
    private JTextArea nameTextArea;
    private JTextField nameTextField;
    private JTextArea xCoordinateTextArea;
    private JTextField xCoordinateTextField;
    private JTextArea yCoordinateTextArea;
    private JTextField yCoordinateTextField;
    private JCheckBox toothpickCheckBox;
    private JTextArea toothpickTextArea;
    private JTextArea impactTextArea;
    private JTextField impactTextField;
    private JTextArea weaponTextArea;
    private JComboBox weaponTypeComboBox;
    private JTextArea carTextArea;
    private JCheckBox carCheckBox;
    private JButton applyButton;
    private JTextArea heroTextArea;
    private JCheckBox heroCheckBox;
    private JPanel creatorPanel;
    private JTextArea notificationTextArea;
    private JComboBox moodComboBox;
    private Credentials credentials;
    private String command;
    private int id;
    private String[] humanBeing;
    ArrayList<String> weaponType = new ArrayList<>();
    ArrayList<String> mood = new ArrayList<>();
    ResourceBundle resourceBundle;

    private static CreatorFrame INSTANCE;

    public static void open (Credentials credentials, String command, int id, String[] humanBeing, ResourceBundle resourceBundle){
        if (INSTANCE == null) {
            INSTANCE = new CreatorFrame(credentials, command, id, humanBeing, resourceBundle);
        }

        INSTANCE.resourceBundle = resourceBundle;
        INSTANCE.credentials = credentials;
        INSTANCE.command = command;
        INSTANCE.id = id;
        INSTANCE.humanBeing = humanBeing;
        INSTANCE.setVisible(true);
        if (command.startsWith("update")) INSTANCE.setFields(humanBeing);
        else INSTANCE.defaultFields();
    }

    public CreatorFrame (Credentials credentials, String command, int id, String[] humanBeing, ResourceBundle resourceBundle) {
        setMoodandWeaponType();
        this.credentials = credentials;
        this.command = command;
        this.id = id;
        this.humanBeing = humanBeing;
        if (!(humanBeing.length == 0)) { setFields(humanBeing); }
        applyButton.setText(resourceBundle.getString("applyButton.textField"));
        add(creatorPanel);
        setSize(500, 700);
        setResizable(false);

        applyButton.addActionListener(this::applyPerformed);
    }

    private void applyPerformed (ActionEvent e){
        try {
            sendData(new ServerRequest(command, createRequest(), credentials));
            dispose();
        } catch (IOException ex){
            notificationTextArea.setText(resourceBundle.getString("serverError.textField"));
        }
    }

    private void setFields(String[] humanBeing){
        nameTextField.setText(humanBeing[1]);

        xCoordinateTextField.setText(humanBeing[2]);

        yCoordinateTextField.setText(humanBeing[3]);

        heroCheckBox.setSelected(Boolean.parseBoolean(humanBeing[4]));

        toothpickCheckBox.setSelected(Boolean.parseBoolean(humanBeing[5]));

        impactTextField.setText(humanBeing[6]);

        weaponTypeComboBox.setSelectedIndex(weaponType.indexOf(humanBeing[7]));

        moodComboBox.setSelectedIndex(mood.indexOf(humanBeing[8]));

        carCheckBox.setSelected(Boolean.parseBoolean(humanBeing[9]));
    }

    private void defaultFields(){
        nameTextField.setText("");
        xCoordinateTextField.setText("");
        yCoordinateTextField.setText("");
        heroCheckBox.setSelected(false);
        toothpickCheckBox.setSelected(false);
        impactTextField.setText("");
        weaponTypeComboBox.setSelectedIndex(0);
        moodComboBox.setSelectedIndex(0);
        carCheckBox.setSelected(false);
    }

    private static String sendData(ServerRequest serverRequest) throws IOException {
        Socket outcoming = new Socket(InetAddress.getLocalHost(), DEFAULT_PORT);
        ObjectOutputStream outputStream = new ObjectOutputStream(outcoming.getOutputStream());
        outputStream.writeObject(serverRequest);
        outputStream.flush();

        InputStreamReader inputStreamReader = new InputStreamReader(outcoming.getInputStream());
        outcoming.close();
        return "";
    }

    private void setMoodandWeaponType(){
        weaponType.add("hammer");
        weaponType.add("pistol");
        weaponType.add("shotgun");
        weaponType.add("rifle");
        weaponType.add("knife");

        mood.add("sadness");
        mood.add("sorrow");
        mood.add("gloom");
        mood.add("rage");
        mood.add("frenzy");
    }

    private String createRequest(){
        String request = "";

        try {
            request = Integer.toString(id);

            request += " " + nameTextField.getText();

            float x = Float.parseFloat(xCoordinateTextField.getText());
            request += " " + xCoordinateTextField.getText();

            int y = Integer.parseInt(yCoordinateTextField.getText());
            request += " " + yCoordinateTextField.getText();

            boolean realHero = heroCheckBox.isSelected();
            request += " " + heroCheckBox.isSelected();

            boolean hasToothPick = toothpickCheckBox.isSelected();
            request += " " + toothpickCheckBox.isSelected();

            int impactSpeed = Integer.parseInt(impactTextField.getText());
            request += " " + impactTextField.getText();

            request += " " + weaponType.get(weaponTypeComboBox.getSelectedIndex());

            request += " " + mood.get(moodComboBox.getSelectedIndex());

            boolean car = carCheckBox.isSelected();
            request += " " + carCheckBox.isSelected();

            notificationTextArea.setText("");

            return request;
        } catch (NumberFormatException e){
            notificationTextArea.setText("Введите правильные данные и заполните все поля");
            return request;
        }
    }
}
