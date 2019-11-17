package lmater.peer2peer.peer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonObject;

import lmater.peer2peer.server.ServerThread;

public class IPeer {
//	private Set<PeerThread> peerThreads = new HashSet<PeerThread>();

	private Set<String> peerThreads = new HashSet<String>();
	private String myPort;
	private String myUsername;
	private ServerThread serverThread;
	private BufferedReader bufferedReader;
	private static IPeer instance;

	public static IPeer getInstance() {
		if (instance != null)
			return instance;
		else {
			instance = new IPeer();
			return instance;
		}
	}

	/**
	 * @throws IOException
	 */
	public void startPeer() throws IOException {
		bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		serverThread = new ServerThread(myPort);
		serverThread.start();
		System.out.println("> enter (space separated) hostname:port#");
		System.out.println("  peers to recieve messages from (s to skip):");

	}

	public Set<String> startRemotePeerListener(String remotePeer) throws IOException {
		return this.updateListenToPeers(bufferedReader, myUsername, serverThread, remotePeer);
	}

	/**
	 * @param bufferedReader
	 * @param username
	 * @param serverThread
	 * @throws IOException
	 */
	public Set<String> updateListenToPeers(BufferedReader bufferedReader, String username, ServerThread serverThread,
			String remotePeer) throws IOException {
		String[] setupValues = remotePeer.split(" ");
		if (!remotePeer.equals("s")) {
			for (int i = 0; i < setupValues.length; i++) {
				String[] address = setupValues[i].split(":");
				Socket socketOfServer = null;
				try {
					if (!peerThreads.contains(setupValues[i])) {
						peerThreads.add(setupValues[i]);
						socketOfServer = new Socket(address[0], Integer.valueOf(address[1]));
						PeerThread peerThread = new PeerThread(socketOfServer);
						peerThread.start();
					}
				} catch (Exception e) {
					if (socketOfServer != null) {
						socketOfServer.close();
						e.printStackTrace();
						peerThreads.remove(setupValues[i]);
					} else
						System.out.println("invalid xinput, skipping to next step.");
				}
			}
		}
		System.out.println("> you can now communicate (e to exit, c to change)");
		return peerThreads;
	}

	/**
	 * @param bufferedReader
	 * @param username
	 * @param serverThread
	 * @throws IOException
	 */
	public void communicate(String message) throws IOException {
		try {
			StringWriter stringwriter = new StringWriter();
			Json.createWriter(stringwriter).writeObject((JsonObject) Json.createObjectBuilder()
					.add("username", myUsername).add("message", message).build());
			serverThread.sendMessage(stringwriter.toString());
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public String getMyPort() {
		return myPort;
	}

	public void setMyPort(String myPort) {
		this.myPort = myPort;
	}

	public String getMyUsername() {
		return myUsername;
	}

	public void setMyUsername(String myUsername) {
		this.myUsername = myUsername;
	}

}
