package com.DiamondRose.Form.Util;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

abstract class SearchKeyListener implements KeyListener{

	protected final JTextField textField;

	public SearchKeyListener(JTextField textField){
		this.textField = textField;
	}

	abstract void onAnyKeyChange(String query);

	@Override
	public void keyTyped(KeyEvent e){
		if(Character.isLetterOrDigit(e.getKeyChar())){
			this.onAnyKeyChange(this.textField.getText() + e.getKeyChar());
		}
	}

	@Override
	public void keyPressed(KeyEvent e){
	}

	@Override
	public void keyReleased(KeyEvent e){
		if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
			this.onAnyKeyChange(this.textField.getText());
		}
	}
}
