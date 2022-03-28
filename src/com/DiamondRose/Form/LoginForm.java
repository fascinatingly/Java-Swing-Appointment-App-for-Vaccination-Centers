package com.DiamondRose.Form;

import com.DiamondRose.Main;
import com.DiamondRose.User.User;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.Objects;

final public class LoginForm extends JFrame{
	public JPanel panel;
	private JButton loginButton;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton signupButton;

	public LoginForm(JFrame mainFrame){
		this.add(this.panel);
		mainFrame.setTitle("Vaccination Manager - Login");
		mainFrame.setPreferredSize(this.getPreferredSize());
		mainFrame.setSize(this.getPreferredSize());

		this.loginButton.addActionListener(e -> {
			String username = this.usernameField.getText();
			String password = String.valueOf(this.passwordField.getPassword());

			User user = Main.getUserManager().getByUsernameOrEmail(username);
			if(user == null){
				JOptionPane.showMessageDialog(this, "Please check your username once again. If you are a new user, sign up for a new account instead.", "Account not found", JOptionPane.ERROR_MESSAGE);
				return;
			}

			if(!user.password.equals(password)){
				JOptionPane.showMessageDialog(this, "Please check your password and try again.", "Incorrect Password", JOptionPane.ERROR_MESSAGE);
				return;
			}

			mainFrame.setContentPane(new SessionMainMenuForm(mainFrame, user).panel);
			mainFrame.revalidate();
		});

		this.signupButton.addActionListener(e -> {
			mainFrame.setContentPane(new SignupForm(mainFrame).panel);
			mainFrame.revalidate();
		});

		this.panel.registerKeyboardAction(e -> this.loginButton.doClick(), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	}
}


