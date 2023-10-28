import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
	private final ServerSocket serverSocket;
	private final ArrayList<ClientHandler> clients;

	public ChatServer() throws IOException {
		clients = new ArrayList<>();
		serverSocket = new ServerSocket(1024);
	}

	public static void main(String[] args) throws IOException {
		ChatServer server = new ChatServer();
		server.startServer();
	}

	// need to run handling adding new users, handling IO and console all in parallel
	public void startServer() throws IOException {
		String message;
		while (true) {
			// waits for socket request from clients
			clients.add(new ClientHandler(serverSocket.accept(), clients));
			message = clients.getLast().getInput().readLine();
			System.out.println(message);
			for (ClientHandler client : clients) {
				client.getOutput().println(message);
			}
			clients.getLast().start();
		}
	}
}

class ClientHandler extends Thread {

	private final Socket socket;

	private ArrayList<ClientHandler> clients;

	private final PrintWriter output;

	private final BufferedReader input;

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
