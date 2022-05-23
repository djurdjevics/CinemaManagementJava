package views;

import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.border.*;

import common.UniversalHelper;

public class LoginDialog extends JDialog {

	private JTextField tfUsername;
    private JPasswordField pfPassword;
    private JLabel lbUsername;
    private JLabel lbPassword;
    private JButton btnLogin;
    private JButton btnCancel;
    
    private UniversalHelper helper = new UniversalHelper();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			LoginDialog dialog = new LoginDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public LoginDialog() {
		JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
        GridBagConstraints cs2 = new GridBagConstraints();
        GridBagConstraints cs3 = new GridBagConstraints();
        GridBagConstraints cs4 = new GridBagConstraints();

        //cs.fill = GridBagConstraints.VERTICAL;

        lbUsername = new JLabel("Username: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(lbUsername, cs);

        tfUsername = new JTextField(20);
        cs2.gridx = 1;
        cs2.gridy = 0;
        cs2.gridwidth = 2;
        panel.add(tfUsername, cs2);

        lbPassword = new JLabel("Password: ");
        cs3.gridx = 0;
        cs3.gridy = 1;
        cs3.gridwidth = 1;
        panel.add(lbPassword, cs3);

        pfPassword = new JPasswordField(20);
        cs4.gridx = 1;
        cs4.gridy = 1;
        cs4.gridwidth = 2;
        panel.add(pfPassword, cs4);
        panel.setBorder(new LineBorder(Color.GRAY));

        btnLogin = new JButton("Login");
        btnLogin.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		String pw = "";
        		for(int i = 0;i<pfPassword.getPassword().length;i++) {
        			pw+=pfPassword.getPassword()[i];
        		}
        		String s = String.format("SELECT COUNT(*) as 'rowCount' FROM User WHERE username = '%s' AND password = '%s'",tfUsername.getText(),pw);
        		ResultSet rs = null;
        		try {
        		rs = helper.GetRows(s);
        		rs.next();
        		if(rs.getInt("rowCount")==1) {
        			MoviesWindow moviesWindow = new MoviesWindow();
        			dispose();
        		}
        		else {
        			JOptionPane.showMessageDialog(null, "Pogresni kredencijali!");
        		}
        		}
        		catch(Exception ex) {
        			System.out.println(ex.getMessage());
        		}
        		
        	}
        });
        btnCancel = new JButton("Cancel");

        JPanel bp = new JPanel();
        bp.add(btnLogin);
        bp.add(btnCancel);

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.SOUTH);

        pack();
        setResizable(false);
	}
}
