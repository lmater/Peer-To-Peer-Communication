package lmater.peer2peer.peer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.json.Json;
import javax.json.JsonObject;

public class PeerThread extends Thread {
	private BufferedReader bufferedReader;
	private Socket socketOfServer;

	/**
	 * @param socketOfServer
	 * @throws IOException
	 */
	public PeerThread(Socket socketOfServer) throws IOException {
		this.socketOfServer = socketOfServer;
		bufferedReader = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));
	}

	@Override
	public void run() {
		boolean flag = true;
		while (flag && !interrupted()) {
			try {
				JsonObject jsonObject = Json.createReader(bufferedReader).readObject();
				if (jsonObject.containsKey("username"))
					System.out
							.println("[" + jsonObject.getString("username") + "]: " + jsonObject.getString("message"));
			} catch (Exception e) {
				flag = false;
				e.printStackTrace();
				interrupt();
			}
		}
	}

	public Socket getSocketOfServer() {
		return socketOfServer;
	}


	void cancel() { interrupt(); }
	
}
