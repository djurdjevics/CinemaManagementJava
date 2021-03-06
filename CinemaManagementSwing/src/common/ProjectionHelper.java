package common;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import common.Models.ProjectionModel;

public class ProjectionHelper {
	UniversalHelper helper = new UniversalHelper();
	Connection conn = helper.DBSetup();
	
	
	public void InsertProjection(ProjectionModel projection) throws SQLException {
		String id = projection.ProjectionID.toString();
		String movieId = projection.MovieID.toString();
		String auditoriumId = String.valueOf(projection.AuditoriumID);
		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
		String formattedDateTime = projection.ProjectionTime.format(formatter);
		try {
			Statement st = conn.createStatement();
			String strQuery = "INSERT INTO Projection VALUES (" + "'" + id + "'"  + "," +  "'" + movieId + "'" + "," + auditoriumId + "," + "'" +  formattedDateTime + "')"; 
			st.execute(strQuery);
			conn.close();
			
		}
		catch(SQLException ex) {
			System.out.println(ex.getMessage());
			conn.close();
		}
	}
	
	public void DeleteProjection(UUID id) throws SQLException {
		try {
			Statement st = conn.createStatement();
			String strQuery = "DELETE  FROM Projection WHERE id = " + "'" + id + "'";
			st.execute(strQuery);
			conn.close();
		}
		catch(SQLException ex) {
			System.out.println(ex.getMessage());
			conn.close();
		}
	}
	
	public void UpdateProjection(ProjectionModel projection) throws SQLException {
		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
		String formattedDateTime = projection.ProjectionTime.format(formatter);
		try {
			Statement st = conn.createStatement();
			String strQuery = String.format("UPDATE Projection SET movieId = '%s',auditoriumId = %d, projectionTime = '%s' WHERE id = '%s'",projection.MovieID,projection.AuditoriumID,formattedDateTime,projection.ProjectionID);
			st.execute(strQuery);
		}
		catch(SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}
}
