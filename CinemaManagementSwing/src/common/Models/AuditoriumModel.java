package common.Models;

public class AuditoriumModel {
	public int ID;
	public int CinemaID;
	public String AuditName;
	
	@Override
	public String toString() {
		return AuditName;
	}
}
