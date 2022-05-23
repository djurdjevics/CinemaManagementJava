package views;

import javax.swing.*;
import java.util.List;
import java.util.ArrayList;

import java.awt.EventQueue;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;
import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.table.TableModel;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DateTimePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.TimePickerSettings;

import common.ProjectionHelper;
import common.UniversalHelper;
import common.Models.AuditoriumModel;
import common.Models.BooleanTableModel;
import common.Models.MovieModel;
import common.Models.ProjectionModel;

import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ProjectionsWindow {

	private JFrame frame;
	private JTable projectionsTable;
	private JTextField textField_1;
	private String[] columnNames = {"ID Projekcije","ID filma","ID sale","Datum","Naziv bioskopa","Naziv filma","Naziv sale"};
	UniversalHelper helper = new UniversalHelper();
	private Object[][] projectionsData = null;
	private ResultSet auditsData = null;
	private ResultSet moviesData = null;
	ResultSet projections = null;
	private int selectedIndex = 0;
	
	private ProjectionModel projectionForManipulating = new ProjectionModel();
	
	//DateTimePicker dateTimePicker;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ProjectionsWindow window = new ProjectionsWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private List<MovieModel> GetMoviesForComboBox() throws SQLException{
		List<MovieModel> result = new ArrayList<MovieModel>();
		while(moviesData.next()) {
			MovieModel movie = new MovieModel();
			movie.id = UUID.fromString(moviesData.getString(1));
			movie.Title = moviesData.getString(2);
			result.add(movie);
		}
		return result;
	}
	
	private List<AuditoriumModel> GetAuditsForComboBox() throws SQLException{
		List<AuditoriumModel> result = new ArrayList<AuditoriumModel>();
		while(auditsData.next()) {
			AuditoriumModel audit = new AuditoriumModel();
			audit.ID = Integer.parseInt(auditsData.getString(1));
			audit.AuditName = auditsData.getString(2);
			result.add(audit);
		}
		return result;
	}
	
	
	/**
	 * Create the application.
	 */
	public ProjectionsWindow() {
		String queryStr = "SELECT * FROM Projekcije";
		try {
			projections = helper.GetRows(queryStr);
			moviesData = helper.GetRows("SELECT id,title FROM Movie");
			auditsData = helper.GetRows("SELECT Id,name FROM Auditorium");
			projectionsData = helper.GetTableData(projections, columnNames,"Projekcije");
		}
		catch(SQLException ex){
			System.out.println(ex.getStackTrace());
		}
		
		initialize();
	}
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1167, 749);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		projectionsTable = new JTable(new BooleanTableModel(projectionsData,columnNames));
		projectionsTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		projectionsTable.setRowSelectionAllowed(true);
		JScrollPane scrollPane = new JScrollPane(projectionsTable,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		
		JMenuBar menuBar = new JMenuBar();
		frame.getContentPane().add(menuBar, BorderLayout.NORTH);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Filmovi");
		mntmNewMenuItem.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.dispose();
				MoviesWindow newWindow = new MoviesWindow();
			}
		});
		menuBar.add(mntmNewMenuItem);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Sale");
		mntmNewMenuItem_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.dispose();
				AuditoriumsWindow newWindow = new AuditoriumsWindow();
			}
		});
		menuBar.add(mntmNewMenuItem_1);
		
		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Bioskopi");
		mntmNewMenuItem_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.dispose();
				CinemasWindow newWindow = new CinemasWindow();
			}
		});
		menuBar.add(mntmNewMenuItem_2);
		
		JMenuItem mntmNewMenuItem_3 = new JMenuItem("Projekcije");
		menuBar.add(mntmNewMenuItem_3);

		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
		
		DateTimePicker date = new DateTimePicker();
		
		
		List<MovieModel> moviesList = new ArrayList<MovieModel>();
		try {
			moviesList = GetMoviesForComboBox();
		}
		catch(SQLException ex) {
			System.out.println(ex.getMessage());
		}
		
		List<AuditoriumModel> auditsList = new ArrayList<AuditoriumModel>();
		try {
			auditsList = GetAuditsForComboBox();
		}
		catch(SQLException ex) {
			System.out.println(ex.getMessage());
		}
		
		
		JButton btnNewButton = new JButton("Sacuvaj izmene");
		JComboBox<AuditoriumModel> auditsProjectionComboBox = new JComboBox<AuditoriumModel>();
		JComboBox<MovieModel> moviesProjectionComboBox = new JComboBox<MovieModel>();
		moviesProjectionComboBox.setModel(new DefaultComboBoxModel<MovieModel>(moviesList.toArray(new MovieModel[0])));
		auditsProjectionComboBox.setModel(new DefaultComboBoxModel<AuditoriumModel>(auditsList.toArray(new AuditoriumModel[0])));
		
		ListSelectionModel rowSelectionModel = projectionsTable.getSelectionModel();

	    rowSelectionModel.addListSelectionListener(new ListSelectionListener() {
	      public void valueChanged(ListSelectionEvent e) {
	        String selectedData = null;
	        
	        ListSelectionModel lsm = (ListSelectionModel)e.getSource();
	        
	        if(!lsm.isSelectionEmpty()) {
	        	int selRow = projectionsTable.getSelectedRow();
	        	selectedIndex = selRow;
	        	projectionForManipulating.ProjectionID = UUID.fromString(projectionsTable.getModel().getValueAt(selRow, 0).toString());
	        	MovieModel movieForSelectedComboBox = new MovieModel();
	        	movieForSelectedComboBox.id=UUID.fromString(projectionsTable.getModel().getValueAt(selRow, 1).toString());
	        	movieForSelectedComboBox.Title = (String) projectionsTable.getModel().getValueAt(selRow, 5);
	        	AuditoriumModel auditForSelectedComboBox = new AuditoriumModel();
	        	auditForSelectedComboBox.ID = Integer.parseInt(projectionsTable.getModel().getValueAt(selRow, 2).toString());
	        	auditForSelectedComboBox.AuditName = projectionsTable.getModel().getValueAt(selRow, 6).toString();

	        	date.setDateTimePermissive((LocalDateTime)projectionsTable.getModel().getValueAt(selRow, 3));
	        	
	        	//SELEKCIJA MOVIES COMBO BOXA
	        	for(int i = 0;i<moviesProjectionComboBox.getItemCount();i++) {
	        		if(((MovieModel)moviesProjectionComboBox.getItemAt(i)).id.compareTo(movieForSelectedComboBox.id) == 0) {
	        			moviesProjectionComboBox.setSelectedIndex(i);
	        		}
	        	}
	        	
	        	//SELEKCIJA AUDITS COMBO BOXA
	        	for(int i = 0;i<auditsProjectionComboBox.getItemCount();i++) {
	        		if(((AuditoriumModel)auditsProjectionComboBox.getItemAt(i)).ID == auditForSelectedComboBox.ID) {
	        			auditsProjectionComboBox.setSelectedIndex(i);
	        		}
	        	}
	        	btnNewButton.setEnabled(true);
	        	
	        }
	      }

	    });
	    
		
		btnNewButton.setEnabled(false);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				projectionForManipulating.AuditoriumID = ((AuditoriumModel)auditsProjectionComboBox.getSelectedItem()).ID;
				projectionForManipulating.MovieID = ((MovieModel)moviesProjectionComboBox.getSelectedItem()).id;
				projectionForManipulating.ProjectionTime = date.getDateTimePermissive();
				String movieName = ((MovieModel)moviesProjectionComboBox.getSelectedItem()).Title;
				String auditName = ((AuditoriumModel)auditsProjectionComboBox.getSelectedItem()).AuditName;
				String cinemaName = "";
				try {
					String queryStr = String.format("SELECT cinema.name FROM auditorium,cinema WHERE cinema.id = auditorium.cinemaId AND auditorium.Id = %d", projectionForManipulating.AuditoriumID);
					ResultSet cinema = helper.GetRows(queryStr);
					cinema.next();
					cinemaName = cinema.getString(1);
				}
				catch(SQLException ex) {
					ex.getStackTrace();
				}
				
				try {
					ProjectionHelper helper = new ProjectionHelper();
					helper.UpdateProjection(projectionForManipulating);
					JOptionPane.showMessageDialog(null, "Uspesno izmenjena projekcija!");
					projectionsTable.getModel().setValueAt(String.valueOf(projectionForManipulating.MovieID).toUpperCase(), selectedIndex, 1);
					projectionsTable.getModel().setValueAt(projectionForManipulating.AuditoriumID, selectedIndex, 2);
					projectionsTable.getModel().setValueAt(projectionForManipulating.ProjectionTime, selectedIndex, 3);
					projectionsTable.getModel().setValueAt(cinemaName, selectedIndex, 4);
					projectionsTable.getModel().setValueAt(movieName, selectedIndex, 5);
					projectionsTable.getModel().setValueAt(auditName, selectedIndex, 6);
					BooleanTableModel btm = (BooleanTableModel)projectionsTable.getModel();
					btm.fireTableDataChanged();

				}
				catch(SQLException ex) {
					System.out.println(ex.getMessage());
				}
			}
		});
		
		JButton btnNewButton_1 = new JButton("Dodaj projekciju");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ProjectionModel projectionToAdd = new ProjectionModel();
				projectionToAdd.ProjectionID = UUID.randomUUID();
				projectionToAdd.AuditoriumID = ((AuditoriumModel)auditsProjectionComboBox.getSelectedItem()).ID;
				projectionToAdd.MovieID = ((MovieModel)moviesProjectionComboBox.getSelectedItem()).id;
				String auditName = ((AuditoriumModel)auditsProjectionComboBox.getSelectedItem()).AuditName;
				String movieName = ((MovieModel)moviesProjectionComboBox.getSelectedItem()).Title;
				projectionToAdd.ProjectionTime = date.getDateTimePermissive();
				String cinemaName = "";
				try {
					String queryStr = String.format("SELECT cinema.name FROM auditorium,cinema WHERE cinema.id = auditorium.cinemaId AND auditorium.Id = %d", projectionToAdd.AuditoriumID);
					ResultSet cinema = helper.GetRows(queryStr);
					cinema.next();
					cinemaName = cinema.getString(1);
				}
				catch(SQLException ex) {
					ex.getStackTrace();
				}
				try {
					ProjectionHelper helper = new ProjectionHelper();
					helper.InsertProjection(projectionToAdd);
					BooleanTableModel btm = (BooleanTableModel)projectionsTable.getModel();
					btm.addRow(new Object[] {String.valueOf(projectionToAdd.ProjectionID).toUpperCase(),String.valueOf(projectionToAdd.MovieID).toUpperCase(),projectionToAdd.AuditoriumID,projectionToAdd.ProjectionTime,cinemaName,movieName,auditName});
					btm.fireTableDataChanged();
					JOptionPane.showMessageDialog(null, "Uspesno dodata projekcija!");
				}
				catch(SQLException ex) {
					System.out.println(ex.getMessage());
				}
			}
		});
		
		JButton btnNewButton_1_1 = new JButton("Obrisi projekciju");
		btnNewButton_1_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					ProjectionHelper helper = new ProjectionHelper();
					helper.DeleteProjection(projectionForManipulating.ProjectionID);
					BooleanTableModel btm = (BooleanTableModel)projectionsTable.getModel();
					btm.removeRow(selectedIndex);
					btm.fireTableDataChanged();
					JOptionPane.showMessageDialog(null, "Uspesno obrisana projekcija!");
				}
				catch(SQLException ex) {
					System.out.println(ex.getMessage());
				}
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
							.addComponent(date, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
							.addComponent(auditsProjectionComboBox, 0, 217, Short.MAX_VALUE)
							.addComponent(moviesProjectionComboBox, 0, 204, Short.MAX_VALUE))
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(btnNewButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnNewButton_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnNewButton_1_1, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED, 68, GroupLayout.PREFERRED_SIZE)))
					.addGap(939))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
					.addContainerGap(58, Short.MAX_VALUE)
					.addComponent(moviesProjectionComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(date, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(auditsProjectionComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(26)
					.addComponent(btnNewButton)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNewButton_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNewButton_1_1)
					.addContainerGap())
		);
		panel.setLayout(gl_panel);
	}
}
