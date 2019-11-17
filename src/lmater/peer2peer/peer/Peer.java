package lmater.peer2peer.peer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.Socket;

import javax.json.Json;
import javax.json.JsonObject;

import lmater.peer2peer.server.ServerThread;

public class Peer {

	public static void main(String[] args) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("> enter username & port # for this peer :");
		String[] setupValues = bufferedReader.readLine().split(" ");
		ServerThread serverThread = new ServerThread(setupValues[1]);
		serverThread.start();
		new Peer().updateListenToPeers(bufferedReader, setupValues[0], serverThread);
	}

	public void updateListenToPeers(BufferedReader bufferedReader, String username, ServerThread serverThread)
			throws IOException {

		System.out.println("> enter (space separated) hostname:port#");
		System.out.println("  peers to recieve messages from (s to skip):");
		String input = bufferedReader.readLine();
		String[] setupValues = input.split(" ");
		if (!input.equals("s")) {
			for (int i = 0; i < setupValues.length; i++) {
				String[] address = setupValues[i].split(":");
				Socket socketOfServer = null;
				try {
					socketOfServer = new Socket(address[0], Integer.valueOf(address[1]));
					new PeerThread(socketOfServer).start();
				} catch (Exception e) {
					if (socketOfServer != null)
						socketOfServer.close();
					else
						System.out.println("invalid input, skipping to next step.");
				}
			}
			communicate(bufferedReader, username, serverThread);
		}

	}

	public void communicate(BufferedReader bufferedReader, String username, ServerThread serverThread)
			throws IOException {
		try {
			System.out.println("> you can now communicate (e to exit, c to change)");
			boolean flag = true;
			while (flag) {
				String message = bufferedReader.readLine();
				if (message.equals("e")) {
					flag = false;
					break;
				} else if (message.equals("c")) {
					updateListenToPeers(bufferedReader, username, serverThread);

				} else {
					StringWriter stringwriter = new StringWriter();
					Json.createWriter(stringwriter).writeObject(
							(JsonObject) Json.createObjectBuilder().add("username", username).add("message", message).build());
					serverThread.sendMessage(stringwriter.toString());
				}
			}
			System.exit(0);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
