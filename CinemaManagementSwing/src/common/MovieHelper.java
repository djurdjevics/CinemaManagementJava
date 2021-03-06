package common;

import java.sql.*;
import java.util.UUID;

import common.UniversalHelper;

import common.Models.MovieModel;

public class MovieHelper {
	UniversalHelper helper = new UniversalHelper();
	Connection conn = helper.DBSetup();
	public void InsertMovie(MovieModel movie) throws SQLException {
		String id = movie.id.toString();
		String title = movie.Title;
		String rating = String.valueOf(movie.rating);
		String year = String.valueOf(movie.year);
		String current = (movie.current==true)?String.valueOf(0):String.valueOf(1);
		String hasOscar = (movie.hasOscar==true)?String.valueOf(0):String.valueOf(1);
		String bannerURL = (movie.bannerUrl == null)?movie.bannerUrl:"";
		String trailerURL = (movie.trailerUrl == null)?movie.trailerUrl:"";
		try {
			Statement st = conn.createStatement();
			String strQuery = "INSERT INTO Movie VALUES (" + "'" + id + "'"  + "," +  "'" + title + "'" + "," + year + "," + rating + "," + current + "," + hasOscar + ", '" + bannerURL + "' , '" + trailerURL + "')"; 
			st.execute(strQuery);
			conn.close();
			
		}
		catch(SQLException ex) {
			System.out.println(ex.getMessage());
			conn.close();
		}
	}
	
	public void DeleteMovie(UUID id) throws SQLException {
		try {
			Statement st = conn.createStatement();
			String strQuery = "DELETE  FROM Movie WHERE id = " + "'" + id + "'";
			st.execute(strQuery);
			conn.close();
		}
		catch(SQLException ex) {
			System.out.println(ex.getMessage());
			conn.close();
		}
	}
	
	public void UpdateMovie(MovieModel movie) throws SQLException {
		try {
			Statement st = conn.createStatement();
			int current = (movie.current==true)?0:1;
			int hasOscar = (movie.hasOscar==true)?0:1;
			String strQuery = String.format("UPDATE Movie SET title = '%s',year = %d,rating = %.2f,\"current\"=%d,hasOscar = %d WHERE id = '%s'", movie.Title,movie.year,movie.rating,current,hasOscar,movie.id);
			st.execute(strQuery);
		}
		catch(SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}
}
