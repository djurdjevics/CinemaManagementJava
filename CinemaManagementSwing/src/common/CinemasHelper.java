package common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import common.Models.CinemaModel;

public class CinemasHelper {
	UniversalHelper helper = new UniversalHelper();
	Connection conn = helper.DBSetup();
	
	
	public int InsertCinema(CinemaModel cinema) throws SQLException {
		String id = String.valueOf(cinema.ID);
		String name = cinema.Name;
		int insertedId = 0;
		try {
			Statement st = conn.createStatement();
			String strQuery = "INSERT INTO Cinema VALUES (" + "0," + "'" + name + "'" + ")"; 
			st.execute(strQuery);
			ResultSet rs = st.executeQuery("SELECT last_insert_id() as 'lastId' from cinema");
			rs.next();
			insertedId = rs.getInt(1);
			conn.close();
			
		}
		catch(SQLException ex) {
			System.out.println(ex.getMessage());
			conn.close();
		}
		return insertedId;
	}
	
	public void DeleteCinema(int id) throws SQLException {
		try {
			Statement st = conn.createStatement();
			String strQuery = "DELETE  FROM Cinema WHERE id = " + "'" + id + "'";
			st.execute(strQuery);
			conn.close();
		}
		catch(SQLException ex) {
			System.out.println(ex.getMessage());
			conn.close();
		}
	}
	
	public void UpdateCinema(CinemaModel cinema) throws SQLException {
		try {
			Statement st = conn.createStatement();
			String strQuery = String.format("UPDATE Cinema SET name = '%s' WHERE id = '%d'",cinema.Name,cinema.ID);
			st.execute(strQuery);
		}
		catch(SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}
}
