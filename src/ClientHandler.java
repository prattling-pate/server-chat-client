import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread {

	private final Socket socket;
	private final PrintWriter output;
	private final BufferedReader input;
	private final ArrayList<ClientHandler> clients;

	public ClientHandler(Socket accept, ArrayList<ClientHandler> clients) throws IOException {
		this.clients = clients;
		socket = accept;
		output = new PrintWriter(socket.getOutputStream(),
				true);
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	public Socket getSocket() {
		return socket;
	}

	public PrintWriter getOutput() {
		return output;
	}

	public BufferedReader getInput() {
		return input;
	}

	public void run() {
		try {
			String inputLine;
			while ((inputLine = input.readLine()) != null) {
				if ("!exit".equals(inputLine)) {
					output.println("Exiting chat");
					System.out.println("User leaving");
					break;
				}
				System.out.println(inputLine);
				for (ClientHandler client : clients) {
					client.getOutput().println(inputLine);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
