package common.Models;

import java.util.UUID;


public class MovieModel {
	public UUID id;
	public String Title;
	public int year;
	public float rating;
	public boolean current;
	public boolean hasOscar;
	public String bannerUrl;
	public String trailerUrl;
	
	@Override
	public String toString() {
		return Title;
	}
}
