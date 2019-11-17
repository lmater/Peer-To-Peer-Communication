package lmater.peer2peer.server;

import java.io.IOException;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonObject;

public class ServerThread extends Thread {
	private ServerSocket serverSocket;
	private Set<ServerThreadThread> serverThreadThreads = new HashSet<ServerThreadThread>();

	/**
	 * @param socketPort
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public ServerThread(String socketPort) throws NumberFormatException, IOException {
		this.serverSocket = new ServerSocket(Integer.valueOf(socketPort));
	}

	@Override
	public void run() {
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				ServerThreadThread serverThreadThread = new ServerThreadThread(this, socket);
				serverThreadThreads.add(serverThreadThread);
				serverThreadThread.start();
				connectionAwareness(serverThreadThread, socket);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				cancel();
			}
		}
	}

	/**
	 * @param message
	 */
	public void sendMessage(String message) {
		try {
			serverThreadThreads.forEach(t -> t.getPrintwriter().println(message));
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * @param message
	 */
	public void connectionAwareness(ServerThreadThread t, Socket socket) {
		String message = "this client is listenning to you at socket: " + socket;

		StringWriter stringwriter = new StringWriter();
		Json.createWriter(stringwriter).writeObject(
				(JsonObject) Json.createObjectBuilder().add("username", t.getName()).add("message", message).build());
		System.out.println(stringwriter.toString());
	}

	public Set<ServerThreadThread> getServerThreadThreads() {
		return serverThreadThreads;
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	void cancel() {
		getServerThreadThreads().forEach(t -> t.cancel());
		getServerThreadThreads().clear();
	}
}
