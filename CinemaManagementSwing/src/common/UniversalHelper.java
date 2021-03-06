package common;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;



public  class UniversalHelper {	
	public Connection DBSetup() {
	Connection conn = null;
	 
    try {

        //String dbURL = "jdbc:sqlserver://vegaithackathon.database.windows.net:1433;database=CinemaSeminarski;user=djurdjevics@vegaithackathon;password=Intelligence023!;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
        String dbURL = "jdbc:mysql://localhost/cinemaseminarski?user=root&password=Q1w2e3r4!";
    	conn = DriverManager.getConnection(dbURL);
        if (conn != null) {
            DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
            System.out.println("Driver name: " + dm.getDriverName());
            System.out.println("Driver version: " + dm.getDriverVersion());
            System.out.println("Product name: " + dm.getDatabaseProductName());
            System.out.println("Product version: " + dm.getDatabaseProductVersion());
        }

    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return conn;
}
	
	public ResultSet GetRows(String queryStr) {
		
		ResultSet rs = null;
		try{
		Connection conn = DBSetup();
		Statement st = conn.createStatement();
		String sqlStr = queryStr;
		rs = st.executeQuery(sqlStr);
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		return rs;
	}
	
	//VARIJANTA 1
	
	public Object[][] GetTableData(ResultSet rs,String[] columns,String tableName) throws SQLException
	{
		ResultSet rsRows = GetRows("SELECT COUNT(*) AS 'rowCount' FROM " + tableName);
		rsRows.next();
		int rows = rsRows.getInt("rowCount");
	    ResultSetMetaData metaData = null;
	    metaData = rs.getMetaData();
	    // names of columns
	    Vector<String> columnNames = new Vector<String>();
	    int columnCount = metaData.getColumnCount();
	    for (int column = 0; column < columns.length; column++) {
	        columnNames.add(columns[column]);
	    }

	    // data of the table
	    Object[][] data = new Object[rows][columnCount];
	    int rowIndex = 0;
	    while (rs.next()) {
	        Object[] row = new Object[columnCount];
	        for (int columnIndex = 1; columnIndex <=columnCount; columnIndex++) {
	        	Object a = rs.getObject(columnIndex);
	        	if(a instanceof Timestamp) {
	        		row[columnIndex-1] = ((Timestamp) a).toLocalDateTime();
	        	}
	        	else {
	            row[columnIndex-1]=a;
	        	}
	        }
	        data[rowIndex] = row;
	        rowIndex++;
	    }

	    return data;
	}
	
}
