package views;

import java.awt.EventQueue;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import java.awt.BorderLayout;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import java.awt.Component;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import common.CinemasHelper;
import common.UniversalHelper;
import common.Models.BooleanTableModel;
import common.Models.CinemaModel;

import javax.swing.ListSelectionModel;
import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CinemasWindow {

	private JFrame frame;
	private JTextField textField;
	private JTable cinemasTable;
	String[] columnNames = {"ID Bioskopa","Naziv Bioskopa"};
	ResultSet cinemas = null;
	Object[][] cinemasData = null;
	UniversalHelper helper = new UniversalHelper();
	int selectedIndex = 0;
	CinemaModel cinemaForManipulating = new CinemaModel();
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CinemasWindow window = new CinemasWindow();
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
	public CinemasWindow() {
		try {
			String queryStr = "SELECT * FROM Cinema";
			cinemas = helper.GetRows(queryStr);
			cinemasData = helper.GetTableData(cinemas, columnNames, "Cinema");
		}
		catch(Exception ex) {
			ex.getStackTrace();
		}
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1162, 742);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
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
		

		
		cinemasTable = new JTable(new BooleanTableModel(cinemasData,columnNames));
		//cinemasTable = new JTable(new DefaultTableModel(cinemasData,columnNames));
		cinemasTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		cinemasTable.setRowSelectionAllowed(true);
		
		JScrollPane scrollPane = new JScrollPane(cinemasTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
		
		textField = new JTextField();
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("Sacuvaj izmene");
		btnNewButton.setEnabled(false);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cinemaForManipulating.Name=textField.getText();
				try {
					CinemasHelper cinemasHelper = new CinemasHelper();
					cinemasHelper.UpdateCinema(cinemaForManipulating);
					JOptionPane.showMessageDialog(null, "Uspesno izmenjen bioskop!");
					BooleanTableModel btm = (BooleanTableModel)cinemasTable.getModel();
					btm.setValueAt(cinemaForManipulating.Name, selectedIndex, 1);
					btm.fireTableDataChanged();
				}
				catch(SQLException ex) {
					System.out.println(ex.getMessage());
				}
				
			}
		});
		
		JButton btnNewButton_1 = new JButton("Dodaj bioskop");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				CinemaModel cinemaToAdd = new CinemaModel();
				cinemaToAdd.Name=textField.getText();
				try {
					CinemasHelper cinemasHelper = new CinemasHelper();
					int insertedId = cinemasHelper.InsertCinema(cinemaToAdd);
					JOptionPane.showMessageDialog(null, "Uspesno dodat bioskop!");
					BooleanTableModel btm = (BooleanTableModel)cinemasTable.getModel();
					btm.addRow(new Object[] {insertedId,cinemaToAdd.Name});
					btm.fireTableDataChanged();
				}
				catch(SQLException ex) {
					System.out.println(ex.getMessage());
				}
			}
		});
		
		JButton btnNewButton_1_1 = new JButton("Obrisi bioskop");
		btnNewButton_1_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					CinemasHelper cinemasHelper = new CinemasHelper();
					cinemasHelper.DeleteCinema(cinemaForManipulating.ID);
					BooleanTableModel btm = (BooleanTableModel)cinemasTable.getModel();
					btm.removeRow(selectedIndex);
					btm.fireTableDataChanged();
				}
				catch(SQLException ex) {
					System.out.println(ex.getMessage());
				}
			}
		});
		
		JLabel lblNewLabel = new JLabel("Naziv bioskopa:");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
							.addComponent(btnNewButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btnNewButton_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btnNewButton_1_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE))
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(992, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(110)
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnNewButton)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNewButton_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNewButton_1_1)
					.addContainerGap(11, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		
		ListSelectionModel rowSelectionModel = cinemasTable.getSelectionModel();

	    rowSelectionModel.addListSelectionListener(new ListSelectionListener() {
	      public void valueChanged(ListSelectionEvent e) {
	        
	        ListSelectionModel lsm = (ListSelectionModel)e.getSource();
	        
	        if(!lsm.isSelectionEmpty()) {
	        	int selRow = cinemasTable.getSelectedRow();
	        	selectedIndex = selRow;
	        	cinemaForManipulating.ID = Integer.parseInt(cinemasTable.getModel().getValueAt(selRow, 0).toString());
	        	textField.setText(cinemasTable.getModel().getValueAt(selRow, 1).toString());
	        	btnNewButton.setEnabled(true);
	        }
	      }

	    });
	}
}
