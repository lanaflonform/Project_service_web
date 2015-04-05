package fr.dauphine.vues;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.JPasswordField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;

import fr.dauphine.main.Session;

public class JPanelConnexion extends JPanel{

	private static final long serialVersionUID = 1L;
	private JTextField txt_email;
	private JPasswordField txt_mdp;
	private JButton bt_seConnecter;
	private final ConnexionOk callback;
	
	public JPanelConnexion(){
		initUI();
		initListeners();
		callback=null;
	}
	
	public JPanelConnexion(ConnexionOk callback){
		initUI();
		initListeners();
		this.callback=callback;
	}

	private void initListeners() {
		ActionListener actionListenerSeConnecter = new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String email = txt_email.getText();
				String password = new String(txt_mdp.getPassword());
				if (email.isEmpty()||password.isEmpty()){
					if (email.isEmpty())
						txt_email.requestFocus();
					else
						txt_mdp.requestFocus();
					return;
				}
					
				if (Session.seConnecter(email,password)){
					System.out.println("connexion OK");
					JPanelConnexion.this.setEnabled(false);
					callback.connexionOk();
				}
				else
					JOptionPane.showConfirmDialog(JPanelConnexion.this, "Votre email ou votre mot de passe est incorrecte", "Echec", JOptionPane.OK_OPTION);
			}
		};
		
		bt_seConnecter.addActionListener(actionListenerSeConnecter);
		
	}

	private void initUI() {
		JLabel lblNewLabel = new JLabel("Email");
		
		txt_email = new JTextField();
		txt_email.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Mot de passe");
		
		txt_mdp = new JPasswordField();
		txt_mdp.setColumns(10);
		
		txt_email.setText("eugen.tiganu@gmail.com");
		txt_mdp.setText("eugen");
		
		bt_seConnecter = new JButton("Se connecter");
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(64)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblNewLabel)
								.addComponent(lblNewLabel_1))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(txt_mdp)
								.addComponent(txt_email, GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(103)
							.addComponent(bt_seConnecter)))
					.addContainerGap(201, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblNewLabel)
							.addComponent(txt_email, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(31)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(txt_mdp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNewLabel_1))))
					.addGap(18)
					.addComponent(bt_seConnecter)
					.addGap(33))
		);
		setLayout(groupLayout);

	}
}
