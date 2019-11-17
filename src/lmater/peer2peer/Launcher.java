package lmater.peer2peer;

import java.awt.Color;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import lmater.peer2peer.peer.IPeer;

public class Launcher extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IPeer iPeer;

	public static void main(String[] args) {
		new Launcher().Screen();
	}

	public void Screen() {

		this.setTitle("Bouton");
		this.setSize(600, 400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		JButton btn = new JButton("Run My Peer");
		JButton btnRemotePeer = new JButton("Listen To new Peers");
		JButton btnSend = new JButton("BroadCast Message");

		JLabel lPort = new JLabel("local port");
		JTextField port = new JTextField(6);

		JLabel lUsername = new JLabel("username");
		JTextField username = new JTextField(10);

		JLabel lRemotePeer = new JLabel("RemotePeer");
		JLabel lRemotePeer2 = new JLabel("RemotePeer");
		JTextField remotePeer = new JTextField(10);
		remotePeer.setText("localhost:");
		lRemotePeer2.setText("@ip:#Port");
		JTextField messageTosend = new JTextField(20);

		Panel p1 = new Panel();
		Panel p3 = new Panel();
		Panel p4 = new Panel();
		JTextPane pane = new JTextPane();
		JTextPane console = new JTextPane();
		JScrollPane p5 = new JScrollPane(console);
		JScrollPane tPane = new JScrollPane(pane);
		EmptyBorder eb = new EmptyBorder(new Insets(10, 10, 10, 10));
		pane.setBorder(eb);
		tPane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		pane.setMargin(new Insets(5, 5, 5, 5));
		new ConsoleThread(console).start();

		appendToPane(pane, "You are lestening to :\n", Color.BLUE);
		// On ajoute le bouton au content pane de la JFrame
		p1.add(btn);
		p1.add(lPort);
		p1.add(port);
		p1.add(lUsername);
		p1.add(username);
		p4.add(btnSend);
		p4.add(messageTosend);
		p3.add(btnRemotePeer);
		p3.add(lRemotePeer);
		p3.add(remotePeer);
		p3.add(lRemotePeer2);

		JPanel b1 = new JPanel();
		// On définit le layout en lui indiquant qu'il travaillera en ligne
		b1.setLayout(new BoxLayout(b1, BoxLayout.LINE_AXIS));
		b1.add(p1);

		JPanel b2 = new JPanel();
		// Idem pour cette ligne
		b2.setLayout(new BoxLayout(b2, BoxLayout.LINE_AXIS));
		b2.add(p4);

		JPanel b3 = new JPanel();
		// Idem pour cette ligne
		b3.setLayout(new BoxLayout(b3, BoxLayout.LINE_AXIS));
		b3.add(p3);

		JPanel b5 = new JPanel();
		// Idem pour cette ligne
		b5.setLayout(new BoxLayout(b5, BoxLayout.LINE_AXIS));
		b5.add(p5);

		JPanel b6 = new JPanel();
		// Idem pour cette ligne
		b6.setLayout(new BoxLayout(b6, BoxLayout.LINE_AXIS));
		b6.add(tPane);

		JPanel b4 = new JPanel();
		// On positionne maintenant ces trois lignes en colonne
		b4.setLayout(new BoxLayout(b4, BoxLayout.PAGE_AXIS));
		b4.add(b1);
		b4.add(b3);
		b4.add(b6);
		b4.add(b2);
		b4.add(b5);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					iPeer = IPeer.getInstance();
					iPeer.setMyPort(port.getText());
					iPeer.setMyUsername(username.getText());
					iPeer.startPeer();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
//					e1.getMessage();
				}
			}
		});
		btnRemotePeer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Set<String> peerThreads = iPeer.startRemotePeerListener(remotePeer.getText());
					pane.setText("");
					appendToPane(pane, "You are lestening to :\n", Color.BLUE);
					peerThreads.forEach(p -> appendToPane(pane, p.toString() + "\n", Color.GRAY));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
//					e1.getMessage();
				}
			}
		});
		btnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					iPeer.communicate(messageTosend.getText());
					messageTosend.setText("");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
//					e1.getMessage();
				}
			}
		});
		this.getContentPane().add(b4);
		this.setVisible(true);
	}

	public void appendToPane(JTextPane tp, String msg, Color c) {
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
