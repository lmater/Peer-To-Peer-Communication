package lmater.peer2peer.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThreadThread extends Thread {
	private ServerThread serverThread;
	private Socket socket;
	private PrintWriter printwriter;

	/**
	 * @param severThread
	 * @param socket
	 */
	public ServerThreadThread(ServerThread severThread, Socket socket) {
		this.serverThread = severThread;
		this.socket = socket;
	}

	/**
	 *
	 */
	@Override
	public void run() {
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.printwriter = new PrintWriter(socket.getOutputStream(), true);
//			while (true) {
//				serverThread.sendMessage(bufferedReader.readLine());
//			}
		} catch (IOException e) {
			e.printStackTrace();
			serverThread.getServerThreadThreads().remove(this);
			cancel();
		} 
	}

	/**
	 * @return
	 */
	public PrintWriter getPrintwriter() {
		return printwriter;
	}

	void cancel() {
		try {
			interrupt();
			this.socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
