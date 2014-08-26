package markovchain.ui;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Admin
 */
public class DataTableModel extends AbstractTableModel {

    private final static String[] defaultColumns =
            new String[]{"State", " Hits", "Prob."};
    private ArrayList<StatisticalInfo> dataSource;
    private String[] columnNames;

    public DataTableModel(ArrayList<StatisticalInfo> source) {
        dataSource = source;
        this.columnNames = defaultColumns;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getRowCount() {
        return dataSource.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        StatisticalInfo data = dataSource.get(rowIndex);
        Object result = null;
        switch (columnIndex) {
            case 1:
                result = data.getHits();
                break;
            case 2:
                result = String.format("%.7f", data.getProb());
                break;
            default:
                result = data.getName();
                break;
        }
        return result;
    }
}
