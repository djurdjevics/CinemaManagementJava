package views;

import java.awt.EventQueue;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JSpinner;
import javax.swing.JScrollPane;
import java.awt.Component;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import common.AuditoriumHelper;
import common.UniversalHelper;
import common.Models.AuditoriumModel;
import common.Models.BooleanTableModel;
import common.Models.CinemaModel;
import common.Models.MovieModel;
import common.Models.SeatModel;

import javax.swing.ListSelectionModel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SpinnerNumberModel;

public class AuditoriumsWindow {

	private JFrame frame;
	private JTable auditsTable;
	private UniversalHelper helper = new UniversalHelper();
	ResultSet audits = null;
	ResultSet cinemas = null;
	String[] columnNames = {"ID","Naziv sale","ID Bioskopa","Naziv bioskopa"};
	private Object[][] auditsData = null;
	AuditoriumModel auditForManipulating = new AuditoriumModel();
	private JTextField auditNameTextBox;
	private int selectedIndex = 0;
	
	private List<CinemaModel> GetCinemasForComboBox() throws SQLException {
		List<CinemaModel> result = new ArrayList<CinemaModel>();
		while(cinemas.next()) {
			CinemaModel cinemaForList = new CinemaModel();
			cinemaForList.ID = cinemas.getInt(1);
			cinemaForList.Name = cinemas.getString(2);
			result.add(cinemaForList);
		}
		return result;
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AuditoriumsWindow window = new AuditoriumsWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AuditoriumsWindow() {
		try {
			String strQuery = "SELECT * FROM AuditoriumsAndCinemaView";
			audits = helper.GetRows(strQuery);
			cinemas = helper.GetRows("SELECT id,name FROM Cinema");
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		initialize();
	}
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1132, 771);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		try {
			auditsData = helper.GetTableData(audits, columnNames, "AuditoriumsAndCinemaView");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		auditsTable = new JTable(new BooleanTableModel(auditsData,columnNames));
		auditsTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		auditsTable.setRowSelectionAllowed(true);
		
		JScrollPane scrollPane = new JScrollPane(auditsTable,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(scrollPane,BorderLayout.CENTER);
		
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
		mntmNewMenuItem_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.dispose();
				ProjectionsWindow newWindow = new ProjectionsWindow();
			}
		});
		menuBar.add(mntmNewMenuItem_3);

		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
		
		
		List<CinemaModel> cinemaList = null;
		
		try {
			cinemaList = GetCinemasForComboBox();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		JComboBox<CinemaModel> comboBox = new JComboBox<CinemaModel>();
		comboBox.setModel(new DefaultComboBoxModel<CinemaModel>(cinemaList.toArray(new CinemaModel[0])));
		
		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		
		JLabel lblNewLabel = new JLabel("Kolone:");
		
		JLabel lblRedovi = new JLabel("Redovi:");
		
		JSpinner spinner_1 = new JSpinner();
		spinner_1.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		
		auditNameTextBox = new JTextField();
		auditNameTextBox.setColumns(10);
		
		JButton btnNewButton = new JButton("Sacuvaj izmene");
		btnNewButton.setEnabled(false);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				auditForManipulating.AuditName = auditNameTextBox.getText();
				auditForManipulating.CinemaID = ((CinemaModel)comboBox.getSelectedItem()).ID;
				try {
					AuditoriumHelper auditHelper = new AuditoriumHelper();
					auditHelper.UpdateAuditorium(auditForManipulating);
					BooleanTableModel btm = (BooleanTableModel)auditsTable.getModel();
					btm.setValueAt(auditForManipulating.AuditName, selectedIndex,1);
					btm.setValueAt(auditForManipulating.CinemaID, selectedIndex,2);
					btm.setValueAt(((CinemaModel)comboBox.getSelectedItem()).Name, selectedIndex,3);
					btm.fireTableDataChanged();
					JOptionPane.showMessageDialog(null, "Uspesno izmenjena sala!");
				}
				catch(SQLException ex) {
					System.out.println(ex.getMessage());
				}
			}
		});
		
		JButton btnNewButton_1 = new JButton("Dodaj salu");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				AuditoriumModel auditToAdd = new AuditoriumModel();
				auditToAdd.AuditName = auditNameTextBox.getText();
				auditToAdd.CinemaID = ((CinemaModel)comboBox.getSelectedItem()).ID;
				try {
					AuditoriumHelper auditHelper = new AuditoriumHelper();
					int insertedId = auditHelper.InsertAuditorium(auditToAdd);
					auditHelper = new AuditoriumHelper();
					for(int i = 1;i<= (int)spinner.getValue();i++) {
						for(int j = 1;j<=(int)spinner.getValue();j++) {
							SeatModel seatToAdd = new SeatModel();
							seatToAdd.ID = UUID.randomUUID();
							seatToAdd.AuditID = insertedId;
							seatToAdd.Row = i;
							seatToAdd.Column = j;
							auditHelper.InsertSeatByAuditID(seatToAdd);
						}
					}
					BooleanTableModel btm = (BooleanTableModel)auditsTable.getModel();
					btm.addRow(new Object[] {insertedId,auditToAdd.AuditName,auditToAdd.CinemaID,((CinemaModel)comboBox.getSelectedItem()).Name});
					JOptionPane.showMessageDialog(null, "Uspesno dodata sala sa id-em: " + insertedId);
				}
				catch(SQLException ex) {
					System.out.println(ex.getMessage());
				}
			}
		});
		
		JButton btnNewButton_1_1 = new JButton("Obrisi salu");
		btnNewButton_1_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					AuditoriumHelper auditHelper = new AuditoriumHelper();
					auditHelper.DeleteAuditorium(auditForManipulating.ID);
					BooleanTableModel btm = (BooleanTableModel)auditsTable.getModel();
					btm.removeRow(selectedIndex);
					JOptionPane.showMessageDialog(null, "Uspesno obrisana sala!");
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
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
								.addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnNewButton_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnNewButton_1_1, GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE))
							.addContainerGap(1003, Short.MAX_VALUE))
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(spinner, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE))
								.addComponent(comboBox, 0, 116, Short.MAX_VALUE))
							.addGap(1003))
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(auditNameTextBox, Alignment.LEADING)
								.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
									.addComponent(lblRedovi, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(spinner_1, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)))
							.addContainerGap())))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(38)
					.addComponent(auditNameTextBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblRedovi)
						.addComponent(spinner_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnNewButton)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNewButton_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNewButton_1_1)
					.addContainerGap(11, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		
		ListSelectionModel rowSelectionModel = auditsTable.getSelectionModel();

	    rowSelectionModel.addListSelectionListener(new ListSelectionListener() {
	      public void valueChanged(ListSelectionEvent e) {
	        
	        ListSelectionModel lsm = (ListSelectionModel)e.getSource();
	        
	        if(!lsm.isSelectionEmpty()) {
	        	int selRow = auditsTable.getSelectedRow();
	        	selectedIndex = selRow;
	        	auditForManipulating.ID = Integer.parseInt(auditsTable.getModel().getValueAt(selRow, 0).toString());
	        	auditNameTextBox.setText(auditsTable.getModel().getValueAt(selRow, 1).toString());
	        	for(int i = 0;i<comboBox.getItemCount();i++) {
	        		if(((CinemaModel)comboBox.getItemAt(i)).ID == Integer.parseInt(auditsTable.getModel().getValueAt(selRow, 2).toString())) {
	        			comboBox.setSelectedIndex(i);
	        		}
	        	}
	        }
	        btnNewButton.setEnabled(true);
	      }

	    });
	}
}
