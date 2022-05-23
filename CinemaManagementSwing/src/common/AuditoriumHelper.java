package common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import common.Models.AuditoriumModel;
import common.Models.ProjectionModel;
import common.Models.SeatModel;

public class AuditoriumHelper {
	UniversalHelper helper = new UniversalHelper();
	Connection conn = helper.DBSetup();
	
	
	public int InsertAuditorium(AuditoriumModel auditorium) throws SQLException {
		String id = String.valueOf(auditorium.ID);
		String cinemaID = String.valueOf(auditorium.CinemaID);
		String auditName = auditorium.AuditName;
		int insertedId = 0;
		try {
			Statement st = conn.createStatement();
			String strQuery = "INSERT INTO Auditorium  VALUES (" + "0,"+ "'" + auditName + "'" + "," + cinemaID + ")"; 
			st.execute(strQuery);
			ResultSet rs = st.executeQuery("SELECT last_insert_id() as 'lastId' from auditorium");
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
	
	public void DeleteAuditorium(int id) throws SQLException {
		try {
			Statement st = conn.createStatement();
			String strQuery = "DELETE  FROM Auditorium WHERE id = " + "'" + id + "'";
			st.execute(strQuery);
			conn.close();
		}
		catch(SQLException ex) {
			System.out.println(ex.getMessage());
			conn.close();
		}
	}
	
	public void UpdateAuditorium(AuditoriumModel auditorium) throws SQLException {
		try {
			Statement st = conn.createStatement();
			String strQuery = String.format("UPDATE Auditorium SET name = '%s',cinemaId = %d WHERE id = '%d'",auditorium.AuditName,auditorium.CinemaID,auditorium.ID);
			st.execute(strQuery);
		}
		catch(SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void InsertSeatByAuditID(SeatModel seat) {
		try {
			Statement st = conn.createStatement();
			String strQuery = String.format("INSERT INTO Seat VALUES ('%s',%d,%d,%d)",String.valueOf(seat.ID),seat.AuditID,seat.Row,seat.Column);
			st.execute(strQuery);
		}
		catch(SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}
}
