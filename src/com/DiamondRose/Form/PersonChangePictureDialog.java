package com.DiamondRose.Form;

import com.DiamondRose.User.User;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

final public class PersonChangePictureDialog extends JDialog{
	private JPanel contentPane;
	private JFileChooser fileSelector;
	private JButton buttonOK;
	private JButton buttonCancel;

	public PersonChangePictureDialog(User user) {
		setContentPane(contentPane);
		setModal(true);

		this.setTitle("Select a profile picture (must be of .png format)");

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});

		contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

		this.fileSelector.addActionListener(e -> {
			if(e.getActionCommand().equals("ApproveSelection")){
				String[] data = this.fileSelector.getSelectedFile().getName().split("\\.");
				String extension = data[data.length - 1];
				if(!extension.equals("png")){
					JOptionPane.showMessageDialog(this, "Only PNG files are supported", "Failed to set profile picture", JOptionPane.ERROR_MESSAGE);
					return;
				}

				try{
					Files.copy(this.fileSelector.getSelectedFile().getAbsoluteFile().toPath(), Path.of("resources/user/user-" + user.id + ".png"), StandardCopyOption.REPLACE_EXISTING);
				}catch(IOException ex){
					JOptionPane.showMessageDialog(this, "An error occurred while copying files. Please report this error", "Failed to set profile picture", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}

			this.onOK();
		});
	}

	private void onOK() {
		dispose();
	}

	private void onCancel() {
		dispose();
	}
}
