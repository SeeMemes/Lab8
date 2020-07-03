package Client.GUI.DrawObjects;

import Client.GUI.CreatorFrame;
import Client.ServerRequest;
import Server.Database.Credentials;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static Server.Tools.AppConstant.DEFAULT_PORT;

public class Display extends JPanel {
    private ArrayList<Float> x;
    private ArrayList<Integer> y;
    private ArrayList<Integer> r;
    private ArrayList<Integer> id = new ArrayList<>();
    private ArrayList<Color> colors = new ArrayList<>();
    private Credentials credentials;
    ArrayList<String []> humanList;
    ArrayList<Boolean> clicked = new ArrayList<>();
    ResourceBundle resourceBundle;


    public Display(Credentials credentials, ResourceBundle resourceBundle){
        this.resourceBundle = resourceBundle;
        this.credentials = credentials;
        this.humanList = askHumanList();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e){
                updateDrawings(askHumanList());
                int mouse_x = getMousePosition().x;
                int mouse_y = getMousePosition().y;

                for (int i = 0; i < humanList.size(); i++){
                    int t_x = Math.round(x.get(i));
                    int t_y = y.get(i);
                    int min_x = t_x;
                    int max_x = t_x + r.get(i);
                    int min_y = t_y;
                    int max_y = t_y + r.get(i);
                    if (mouse_x <= max_x && mouse_x >= min_x && mouse_y <= max_y && mouse_y >= min_y
                        && (mouse_x-t_x-r.get(i)/2)*(mouse_x-t_x-r.get(i)/2) + (mouse_y-t_y-r.get(i)/2)*(mouse_y-t_y-r.get(i)/2) <= r.get(i)*r.get(i)/4){
                        CreatorFrame.open(credentials,"update" + " " + id.get(i), id.get(i), humanList.get(i), resourceBundle);
                        clicked.set(i, true);
                    }
                }
            }
        });

    }

    public void updateDrawings(ArrayList<String []> humanList){
        ArrayList<Float> x = new ArrayList<>();
        ArrayList<Integer> y = new ArrayList<>();
        ArrayList<Integer> r = new ArrayList<>();
        for (int i = 0; i < humanList.size(); i++){
            x.add(Float.parseFloat(humanList.get(i)[2])*2);
            y.add(Integer.parseInt(humanList.get(i)[3])*2);
            r.add(Integer.parseInt(humanList.get(i)[6])*10);
            id.add(Integer.parseInt(humanList.get(i)[0]));

            int username_to_color = Math.abs(humanList.get(i)[10].hashCode());
            int red = (username_to_color)%256;
            int green = (username_to_color/10)%256;
            int blue = (username_to_color/100)%256;
            colors.add(new Color(red, green, blue));
            clicked.add(false);
        }
        this.x = x;
        this.y = y;
        this.r = r;
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

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        for (int i = 0; i < humanList.size(); i++)
            if (!compare(humanList.get(i), askHumanList().get(i))) clicked.set(i, false);

        this.humanList = askHumanList();
        updateDrawings(humanList);
        setBackground(new Color(255,255,240));
        for (int i = 0; i<x.size(); i++){
            if (clicked.get(i)){
                g.setColor(Color.green);
                g.fillOval(Math.round(x.get(i))+10, y.get(i)+10, r.get(i)+10, r.get(i)+10);
                g.setColor(Color.red);
                g.fillOval(Math.round(x.get(i))+5, y.get(i)+5, r.get(i)+5, r.get(i)+5);
                g.setColor(colors.get(i));
                g.fillOval(Math.round(x.get(i)), y.get(i), r.get(i), r.get(i));
            }
            g.setColor(colors.get(i));
            g.fillOval(Math.round(x.get(i)), y.get(i), r.get(i), r.get(i));
        }
    }

    private boolean compare(String[] a, String[] b){
        for (int i = 0; i < a.length; i++)
            if (!a[i].equals(b[i])) return false;
        return true;
    }
}
