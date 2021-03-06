package views;

import java.awt.EventQueue;

import common.MovieHelper;
import common.UniversalHelper;
import common.Models.BooleanTableModel;
import common.Models.MovieModel;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;

import java.awt.BorderLayout;
import javax.swing.ListSelectionModel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JCheckBox;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;

public class MoviesWindow {

	private JFrame frame;
	ResultSet movies = null;
	String[] columnNames = {"ID","Naslov","Godina","Ocena","U projekciji","Ima oskara","Slika","Trailer URL"};
	private JTable projectionsTable;
	UniversalHelper helper = new UniversalHelper();
	private Object[][] moviesData = null;
	private JTextField ratingTextBox;
	private JTextField titleTextBox;
	private JTextField yearTextBox;
	private int selectedIndex = 0;
	
	
	MovieModel movieForManipulating = new MovieModel();
	private JTextField bannerUrlTextBox;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MoviesWindow window = new MoviesWindow();
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
	public MoviesWindow() {
		
		movies = helper.GetRows("SELECT * FROM Movie");
		try{
			moviesData = helper.GetTableData(movies, columnNames,"Movie");
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
		frame.setVisible(true);
		BorderLayout borderLayout = (BorderLayout) frame.getContentPane().getLayout();
		frame.setBounds(100, 100, 1171, 795);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Filmovi");
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
		mntmNewMenuItem_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.dispose();
				ProjectionsWindow newWindow = new ProjectionsWindow();
			}
		});
		menuBar.add(mntmNewMenuItem_3);
		JTableHeader header = null;
		projectionsTable = new JTable(new BooleanTableModel(moviesData,columnNames));
		projectionsTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		projectionsTable.setRowSelectionAllowed(true);
		JScrollPane scrollPane = new JScrollPane(projectionsTable,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(scrollPane,BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
		
		ratingTextBox = new JTextField();
		ratingTextBox.setColumns(10);
		
		JCheckBox currentCheckBox = new JCheckBox("U projekciji");
		
		JCheckBox hasOscarCheckBox = new JCheckBox("Ima oskara");
		
		yearTextBox = new JTextField();
		yearTextBox.setColumns(10);
		
		titleTextBox = new JTextField();
		titleTextBox.setColumns(10);
		
		JButton btnNewButton = new JButton("Sacuvaj izmene");
		btnNewButton.setEnabled(false);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				MovieHelper updateMovie = new MovieHelper();
				movieForManipulating.Title = titleTextBox.getText();
				movieForManipulating.year = Integer.parseInt(yearTextBox.getText());
				movieForManipulating.rating = Float.parseFloat(ratingTextBox.getText());
				movieForManipulating.current = currentCheckBox.isSelected();
				movieForManipulating.hasOscar = hasOscarCheckBox.isSelected();
				movieForManipulating.bannerUrl = bannerUrlTextBox.getText();
				try {
					updateMovie.UpdateMovie(movieForManipulating);
					BooleanTableModel btm = (BooleanTableModel)projectionsTable.getModel();
					btm.setValueAt(movieForManipulating.Title, selectedIndex, 1);
					btm.setValueAt(movieForManipulating.year, selectedIndex, 2);
					btm.setValueAt(movieForManipulating.rating, selectedIndex, 3);
					btm.setValueAt(movieForManipulating.current, selectedIndex, 4);
					btm.setValueAt(movieForManipulating.hasOscar, selectedIndex, 5);
					btm.setValueAt(movieForManipulating.bannerUrl, selectedIndex, 6);
					btm.fireTableDataChanged();
					JOptionPane.showMessageDialog(null, "Uspesno izmenjen film");
				}
				catch(SQLException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage());
				}
			}
		});
		
		JButton btnNewButton_1 = new JButton("Dodaj film");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				MovieModel movie = new MovieModel();
				movie.id = UUID.randomUUID();
				movie.rating = Float.parseFloat(ratingTextBox.getText());
				movie.Title = titleTextBox.getText();
				movie.year = Integer.parseInt(yearTextBox.getText());
				movie.current = currentCheckBox.isSelected();
				movie.hasOscar = hasOscarCheckBox.isSelected();
				movie.bannerUrl = bannerUrlTextBox.getText();
				MovieHelper addHelper = new MovieHelper();
				try {
				addHelper.InsertMovie(movie);
				BooleanTableModel btm = (BooleanTableModel)projectionsTable.getModel();
				btm.addRow(new Object[] {String.valueOf(movie.id).toUpperCase(),movie.Title,movie.year,movie.rating,movie.current,movie.hasOscar,movie.bannerUrl,movie.trailerUrl});
				btm.fireTableDataChanged();
				JOptionPane.showMessageDialog(null, "Uspesno dodat film");
				}
				catch(SQLException ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
				}
			}
		});
		
		JButton btnNewButton_1_1 = new JButton("Obrisi film");
		btnNewButton_1_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				MovieHelper removeMovie = new MovieHelper();
				try
				{
					removeMovie.DeleteMovie(movieForManipulating.id);
					BooleanTableModel btm = (BooleanTableModel)projectionsTable.getModel();
					btm.removeRow(selectedIndex);
					JOptionPane.showMessageDialog(null, "Uspesno obrisan film");
				}
				catch(SQLException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage());
				}
			}
		});
		
		bannerUrlTextBox = new JTextField();
		bannerUrlTextBox.setColumns(10);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(ratingTextBox, Alignment.LEADING)
						.addComponent(currentCheckBox, Alignment.LEADING)
						.addGroup(Alignment.LEADING, gl_panel.createParallelGroup(Alignment.TRAILING, false)
							.addComponent(btnNewButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btnNewButton_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btnNewButton_1_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE))
						.addComponent(hasOscarCheckBox, Alignment.LEADING)
						.addGroup(Alignment.LEADING, gl_panel.createParallelGroup(Alignment.LEADING, false)
							.addComponent(titleTextBox, GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
							.addComponent(yearTextBox))
						.addComponent(bannerUrlTextBox, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE))
					.addContainerGap(995, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(titleTextBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(yearTextBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(ratingTextBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createSequentialGroup()
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(bannerUrlTextBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(currentCheckBox)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(hasOscarCheckBox)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnNewButton)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNewButton_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNewButton_1_1)
					.addContainerGap(11, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);

		
		ListSelectionModel rowSelectionModel = projectionsTable.getSelectionModel();

	    rowSelectionModel.addListSelectionListener(new ListSelectionListener() {
	      public void valueChanged(ListSelectionEvent e) {
	        String selectedData = null;
	        
	        ListSelectionModel lsm = (ListSelectionModel)e.getSource();
	        
	        
	        if(!lsm.isSelectionEmpty()) {
	        	int selRow = projectionsTable.getSelectedRow();
	        	selectedIndex = selRow;
	        	movieForManipulating.id = UUID.fromString(projectionsTable.getModel().getValueAt(selRow, 0).toString());
	        	titleTextBox.setText((String) projectionsTable.getModel().getValueAt(selRow,1));
	        	yearTextBox.setText( projectionsTable.getModel().getValueAt(selRow,2).toString());
	        	ratingTextBox.setText(projectionsTable.getModel().getValueAt(selRow,3).toString());
	        	if(projectionsTable.getModel().getValueAt(selRow,6)!=null)
	        		bannerUrlTextBox.setText(projectionsTable.getModel().getValueAt(selRow,6).toString());
	        	else {
	        		bannerUrlTextBox.setText("");
	        	}
	        	currentCheckBox.setSelected((boolean)projectionsTable.getModel().getValueAt(selRow, 4));
	        	hasOscarCheckBox.setSelected((boolean)projectionsTable.getModel().getValueAt(selRow, 5));
	        	btnNewButton.setEnabled(true);
	        }
	      }

	    });
}
		
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
