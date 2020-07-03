package Client.GUI;

import Client.ServerRequest;
import Server.Database.Credentials;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;

import static Server.Tools.AppConstant.DEFAULT_PORT;

public class ClientFrame extends JFrame{
    private JTable humanBeingTable;
    private JPanel mainPanel;
    private JButton helpButton;
    private JScrollPane tablePane;
    private JButton addButton;
    private JButton remove_idButton;
    private JTextArea responseTextArea;
    private JButton drawObjects;
    private JButton refreshButton;
    private JTextArea usernameTextArea;
    private JRadioButton autoRefreshRadioButton;
    private JButton clearButton;
    private JButton removeSelectedButton;
    private JButton removeLowerKeyButton;
    private JButton removeGreaterKeyButton;
    private JButton replaceIfLowerButton;
    private JButton sumOfImpactSpeedButton;
    private JButton averageOfImpactSpeedButton;
    private JButton printFieldDescendingMoodButton;
    private JTextField commandTextField;
    private JButton sendCommandButton;
    private JTextArea commandLineTextArea;
    private HumanBeingTable model;
    private Credentials credentials;
    private ArrayList<String[]> humanList;
    private final static int INTERVAL = 500;
    private int row;
    private ResourceBundle resourceBundle;

    public ClientFrame (Credentials credentials, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        this.credentials = credentials;
        add(mainPanel);
        setSize(1350, 800);
        setResizable(false);
        repaintHumanBeingTable();
        tablePane.getViewport().setBackground(new Color(255,255,240));
        usernameTextArea.setText(" " + resourceBundle.getString("login.textField") + credentials.getUsername() + " ");
        commandLineTextArea.setText(resourceBundle.getString("commandLine.textField"));
        refreshButton.setText(resourceBundle.getString("refresh.textField"));
        autoRefreshRadioButton.setText(resourceBundle.getString("autorefresh.textField"));
        drawObjects.setText(resourceBundle.getString("drawobjects.textField"));
        sendCommandButton.setText(resourceBundle.getString("sendCommand.textField"));
        addButton.setText(resourceBundle.getString("addButton.textField"));

        Timer timer = new Timer(INTERVAL, evt -> {
            if (autoRefreshRadioButton.isSelected()) {
                repaintHumanBeingTable();
                tablePane.getViewport().setBackground(new Color(255, 255, 240));
            }
        });

        timer.start();

        humanBeingTable.getSelectionModel().addListSelectionListener(event -> {
            if (humanBeingTable.getSelectedRow() > -1) {
                row = humanBeingTable.getSelectedRow();
                String[] humanBeingRequest = new String[11];
                for (int i=0; i < humanBeingTable.getColumnCount(); i++) {
                    humanBeingRequest[i] = humanBeingTable.getModel().getValueAt(row, i).toString();
                }
                CreatorFrame.open(credentials, "update " + humanBeingRequest[0],
                        Integer.parseInt(humanBeingRequest[0]), humanBeingRequest, resourceBundle);
            }
        });



        responseTextArea.setEditable(false);

        removeSelectedButton.addActionListener(this::removeSelectedPerformed);
        removeLowerKeyButton.addActionListener(this::removeLowerPerformed);
        replaceIfLowerButton.addActionListener(this::replaceIfLowerPerformed);
        removeGreaterKeyButton.addActionListener(this::removeGreaterPerformed);
        sumOfImpactSpeedButton.addActionListener(this::sumOfImpactSpeedPerformed);
        averageOfImpactSpeedButton.addActionListener(this::averageOfImpactSpeedPerformed);
        printFieldDescendingMoodButton.addActionListener(this::printFieldDescendingMoodPerformed);
        sendCommandButton.addActionListener(this::sendCommandPerformed);
        clearButton.addActionListener(this::clearPerformed);
        drawObjects.addActionListener(this::drawObjects);
        helpButton.addActionListener(this::helpPerformed);
        addButton.addActionListener(this::addPerformed);
        refreshButton.addActionListener(this::refreshPerformed);
        remove_idButton.addActionListener(this::removeIdPerformed);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void sendCommandPerformed(ActionEvent e){
        responseTextArea.setForeground(new Color(1,1,1));
        String command = "";
        String arguments = "";
        try{
            StringTokenizer stringTokenizer = new StringTokenizer(commandTextField.getText());
            command = stringTokenizer.nextToken();
            arguments = stringTokenizer.nextToken();
        } catch (Exception ex) {}
        try {
            responseTextArea.setText(sendData(new ServerRequest(command, arguments, credentials)));
        } catch (IOException ex) {
            responseTextArea.setText(resourceBundle.getString("serverError.textField"));
        }
    }

    private void sumOfImpactSpeedPerformed(ActionEvent e) {
        try {
            String response = sendData(new ServerRequest("sum_of_impact_speed", "", credentials));
            responseTextArea.setForeground(new Color(1,1,1));
            responseTextArea.setText(response);
        } catch (IOException ex){
            responseTextArea.setText(resourceBundle.getString("serverError.textField"));
        }
    }

    private void averageOfImpactSpeedPerformed(ActionEvent e) {
        try {
            String response = sendData(new ServerRequest("average_of_impact_speed", "", credentials));
            responseTextArea.setForeground(new Color(1,1,1));
            responseTextArea.setText(response);
        } catch (IOException ex){
            responseTextArea.setText(resourceBundle.getString("serverError.textField"));
        }
    }

    private void printFieldDescendingMoodPerformed(ActionEvent e) {
        try {
            String response = sendData(new ServerRequest("print_field_descending_mood", "", credentials));
            responseTextArea.setForeground(new Color(1,1,1));
            responseTextArea.setText(response);
        } catch (IOException ex){
            responseTextArea.setText(resourceBundle.getString("serverError.textField"));
        }
    }

    private void replaceIfLowerPerformed(ActionEvent e){
        String[] request = {};
        try {
            String id = humanBeingTable.getModel().getValueAt(row, 0).toString();
            CreatorFrame.open(credentials, "replace_if_lowe " + id, Integer.parseInt(id), request, resourceBundle);
        } catch(NullPointerException ex){
            responseTextArea.setText(resourceBundle.getString("pickRowException.textField"));
        }
    }

    private void removeLowerPerformed(ActionEvent e){
        RemoveFrame removeFrame = new RemoveFrame("remove_lower_key ", credentials, humanList, resourceBundle);
        removeFrame.setVisible(true);
    }

    private void removeGreaterPerformed(ActionEvent e){
        RemoveFrame removeFrame = new RemoveFrame("remove_greater_key ", credentials, humanList, resourceBundle);
        removeFrame.setVisible(true);
    }

    private void removeSelectedPerformed(ActionEvent e) {
        try{
            String id = humanBeingTable.getModel().getValueAt(row, 0).toString();
            String response = sendData(new ServerRequest("remove_key", id, credentials));
            responseTextArea.setText(response);
        } catch (NullPointerException ex){
            responseTextArea.setText(resourceBundle.getString("pickRowException.textField"));
        } catch (IOException ex1){
            responseTextArea.setText(resourceBundle.getString("serverError.textField"));
        }
    }

    private void clearPerformed(ActionEvent e){
        try {
            String response = sendData(new ServerRequest("clear", "", credentials));
            responseTextArea.setForeground(new Color(1,1,1));
            responseTextArea.setText(response);
        } catch (IOException ex){
            responseTextArea.setText(resourceBundle.getString("serverError.textField"));
        }
    }

    private void removeIdPerformed(ActionEvent e){
        RemoveFrame removeFrame = new RemoveFrame("remove_key ", credentials, humanList, resourceBundle);
        removeFrame.setVisible(true);
    }

    private void refreshPerformed(ActionEvent e){
        repaintHumanBeingTable();
        tablePane.getViewport().setBackground(new Color(255,255,240));
    }

    private void helpPerformed(ActionEvent e) {
        try {
            String response = sendData(new ServerRequest("help", "", credentials));
            responseTextArea.setForeground(new Color(1,1,1));
            responseTextArea.setText(response);
        } catch (IOException ex){
            responseTextArea.setText(resourceBundle.getString("serverError.textField"));
        }
    }

    private void addPerformed(ActionEvent e) {
        String[] request = {};
        ArrayList<Integer> id = new ArrayList<>();
        int new_id;
        for (int i = 0; i < humanList.size(); i++) id.add(Integer.parseInt(humanList.get(i)[0]));
        try{
            new_id = Collections.max(id)+1;
        } catch (NoSuchElementException ex) {
            new_id = 1;
        }

        CreatorFrame.open(credentials, "insert " + new_id, new_id, request, resourceBundle);
    }

    private void drawObjects(ActionEvent e){
        PaintFrame paintFrame = new PaintFrame(credentials, resourceBundle);
        paintFrame.setVisible(true);
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

    private void repaintHumanBeingTable(){
        String [] header={"id","name","cord_x","cord_y","realHero","hasToothPick","impactSpeed","weaponType","mood","car","username"};

        this.humanList = askHumanList();
        JTable table = createTable(humanList);

        humanBeingTable.setModel(table.getModel());
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
        humanBeingTable.setRowSorter(sorter);
        java.util.List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
        sortKeys.add(new RowSorter.SortKey(2, SortOrder.ASCENDING));
        sortKeys.add(new RowSorter.SortKey(3, SortOrder.ASCENDING));
        sortKeys.add(new RowSorter.SortKey(4, SortOrder.ASCENDING));
        sortKeys.add(new RowSorter.SortKey(5, SortOrder.ASCENDING));
        sortKeys.add(new RowSorter.SortKey(6, SortOrder.ASCENDING));
        sortKeys.add(new RowSorter.SortKey(7, SortOrder.ASCENDING));
        sortKeys.add(new RowSorter.SortKey(8, SortOrder.ASCENDING));
        sortKeys.add(new RowSorter.SortKey(9, SortOrder.ASCENDING));
        sortKeys.add(new RowSorter.SortKey(10, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);

        JTableHeader th = humanBeingTable.getTableHeader();
        th.setBackground(new Color(65,52,101));
        th.setForeground(Color.white);
        TableColumnModel tcm = th.getColumnModel();
        for (int i = 0; i < header.length; i++) {
            TableColumn tc = tcm.getColumn(i);
            tc.setHeaderValue(header[i]);
            th.repaint();
        }
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

    public JTable createTable(ArrayList<String []> readArrayList) {
        this.model = new HumanBeingTable(readArrayList);
        JTable table = new JTable(model);
        return table;
    }
}
