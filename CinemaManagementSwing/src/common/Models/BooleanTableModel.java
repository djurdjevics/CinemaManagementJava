package common.Models;

import java.sql.ResultSetMetaData;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

public class BooleanTableModel extends DefaultTableModel {
	String[] columns; 
	Object[][] data;

	public BooleanTableModel(Object[][] _data,String[] _columns) {
		super(_data,_columns);
		data = _data;
		columns = _columns;
	}
	

//	//@Override
//	public int getRowCount() {
//		// TODO Auto-generated method stub
//		return data.length;
//	}
//
//	//@Override
//	public int getColumnCount() {
//		// TODO Auto-generated method stub
//		return columns.length;
//	}
//
//	//@Override
//	public Object getValueAt(int rowIndex, int columnIndex) {
//		// TODO Auto-generated method stub
//		return data[rowIndex][columnIndex];
//	}
//	
//	//@Override
//	public String getColumnName(int column) {
//		return columns[column];
//	}
	
	
    //@Override
    public Class<?> getColumnClass(int columnIndex) {
    	if(data[0][columnIndex]==null) return Object.class;
        return data[0][columnIndex].getClass();
    }
    
    public void AddRow(Vector<Object> rowVector) {
    	super.addRow(rowVector);
    }
}
