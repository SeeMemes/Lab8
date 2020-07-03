package Client.GUI;

import Client.ServerRequest;
import Server.Database.Credentials;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static Server.Tools.AppConstant.DEFAULT_PORT;

public class RemoveFrame extends JFrame{
    private static final int WIDTH = 600;
    private static final int HEIGHT = 250;

    private JPanel removePanel;
    private JTextField idTextField;
    private JButton removeButton;
    private JTextArea nameTextArea;
    private JTextArea idTextArea;
    private JTextArea notificationTextArea;
    private Credentials credentials;
    private String command;
    private ArrayList<String []> humanList;
    ResourceBundle resourceBundle;

    public RemoveFrame (String command, Credentials credentials, ArrayList<String []> humanList, ResourceBundle resourceBundle){
        this.resourceBundle = resourceBundle;
        this.command = command;
        this.credentials = credentials;
        add(removePanel);
        setSize(new Dimension(WIDTH, HEIGHT));
        setResizable(false);
        idTextArea.setText(resourceBundle.getString("enterID.textField"));
        removeButton.setText(resourceBundle.getString("removeButton.textField"));

        this.humanList = humanList;
        removeButton.addActionListener(this::removePerformed);
    }

    private void removePerformed (ActionEvent e){
        try {
            System.out.println(sendData(new ServerRequest(command + idTextField.getText(), idTextField.getText(), credentials)));
            if (!(askHumanList().size() == humanList.size())) {
                System.out.println(askHumanList());
                System.out.println(humanList);
                dispose();
            }
            else notificationTextArea.setText("Невозможно удалить объект. " +
                    "\nПроверьте существует ли объект и принадлежит ли он вам.");
        } catch (IOException ex){
            notificationTextArea.setText("Server is offline. Try again later.");
        }
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
        return answer;
    }

    private ArrayList<String[]> askHumanList() {
        ServerRequest serverRequest = new ServerRequest("get_human_being_list", "", credentials);
        ArrayList<String[]> readArrayList = new ArrayList<>();
        try{
            Socket outcoming = new Socket(InetAddress.getLocalHost(), DEFAULT_PORT);
            ObjectOutputStream outputStream = new ObjectOutputStream(outcoming.getOutputStream());
            outputStream.writeObject(serverRequest);

            ObjectInputStream objectInputStream = new ObjectInputStream(outcoming.getInputStream());
            readArrayList = (ArrayList<String []>) objectInputStream.readObject();
            outcoming.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return readArrayList;
    }
}
