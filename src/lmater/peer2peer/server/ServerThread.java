package lmater.peer2peer.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

public class ServerThread extends Thread {
	private ServerSocket serverSocket;
	private Set<ServerThreadThread> serverThreadThreads = new HashSet<ServerThreadThread>();

	public ServerThread(String socketPort) throws NumberFormatException, IOException {
		this.serverSocket = new ServerSocket(Integer.valueOf(socketPort));
	}

	@Override
	public void run() {
		while (true) {
			try {
				ServerThreadThread serverThreadThread = new ServerThreadThread(this, serverSocket.accept());
				serverThreadThreads.add(serverThreadThread);
				serverThreadThread.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void sendMessage(String message) {
		try {
			serverThreadThreads.forEach(t -> t.getPrintwriter().println(message));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public Set<ServerThreadThread> getServerThreadThreads() {
		return serverThreadThreads;
	}

}
