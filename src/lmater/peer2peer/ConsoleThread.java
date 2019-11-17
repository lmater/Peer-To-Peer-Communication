package lmater.peer2peer;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;


public class ConsoleThread extends Thread {

	private JTextPane console;
	private ByteArrayOutputStream newConsole;

	public ConsoleThread(JTextPane console) {
		super();
		this.console = console;
		newConsole = new ByteArrayOutputStream();
		System.setOut(new PrintStream(newConsole));
	}

	@Override
	public void run() {
		String old = "";
		while (true) {
			if (old != newConsole.toString()) {
				console.setEditable(true);
				appendToPane(console, newConsole.toString(), Color.BLACK);
				old = newConsole.toString();
				console.setEditable(false);
			}
			console.setCaretPosition(console.getDocument().getLength());
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void appendToPane(JTextPane tp, String msg, Color c) {
		tp.setText("");
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

		aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
		aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

		int len = tp.getDocument().getLength();
		tp.setCaretPosition(len);
		tp.setCharacterAttributes(aset, false);
		tp.replaceSelection(msg);
	}
}
