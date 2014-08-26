package markovchain.ui;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Admin
 */
public class HozDataTableModel extends AbstractTableModel {

    private final static String[] rowNames =
            new String[]{" Hits", "Prob."};
    private ArrayList<StatisticalInfo> dataSource;
    private String[] columnNames;
    private int columnCount = 0;

    public HozDataTableModel(ArrayList<StatisticalInfo> source) {
        dataSource = source;
        columnCount = source.size() + 1;
        int index = 0;
        String[] columns = new String[columnCount];
        columns[index++] = "State";
        for (StatisticalInfo item : source) {
            columns[index++] = item.getName();
        }
        this.columnNames = columns;
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getRowCount() {
        return rowNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return rowNames[rowIndex];
        }
        StatisticalInfo data = dataSource.get(columnIndex - 1);
        Object result = "";
        switch (rowIndex) {
            case 0:
                result = data.getHits();
                break;
            case 1:
                result = String.format("%.7f", data.getProb());
                break;
        }
        return result;
    }
}