package Client.GUI;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class HumanBeingTable extends AbstractTableModel {
    private ArrayList<String[]> humanBeings;

    public HumanBeingTable(ArrayList<String[]> humanBeings){
        this.humanBeings = humanBeings;
        for (int i = 0; i < humanBeings.size(); i++){
            String str [] = humanBeings.get(i);
            for (int j = 0; j < str.length; j++){
                System.out.print(str[j] + " ");
            }
            System.out.println();
        }
    }

    @Override
    public int getRowCount() {
        return humanBeings.size();
    }

    @Override
    public int getColumnCount() {
        try{
            return humanBeings.get(0).length;
        } catch (IndexOutOfBoundsException e) {
            return 11;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return humanBeings.get(rowIndex)[columnIndex];
    }

    /*@Override
    public boolean isCellEditable(int row, int col)
    { return true; }*/

}
