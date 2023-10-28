package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ChatServer {
	private final ServerSocket serverSocket;
	private final ArrayList<ClientHandler> clients;

	private final CommandLineInterface commandLineInterface;

	private boolean running;

	public ChatServer() throws IOException {
		clients = new ArrayList<>();
		serverSocket = new ServerSocket(1024);
		commandLineInterface = new CommandLineInterface(this);
		running = true;
	}

	public static void main(String[] args) throws IOException {
		ChatServer server = new ChatServer();
		server.startServer();
	}

	// need to run handling adding new users, handling IO and console all in parallel
	public void startServer() throws IOException {
		System.out.println("Server now open at " + serverSocket.getInetAddress());
		commandLineInterface.start();
		mainServerLoop();
		closeServer();
	}

	private void mainServerLoop() throws IOException {
		while (running) {
			// waits for socket request from clients
			clients.add(new ClientHandler(serverSocket.accept(), clients));
			introduceNewUser();
			clients.getLast().start();
		}
	}

	private void introduceNewUser() throws IOException {
		String message;
		message = clients.getLast().getInput().readLine();
		System.out.println(message);
		for (ClientHandler client : clients) {
			client.getOutput().println(message);
		}
	}

	public void stopServer() {
		running = false;
	}

	private void closeServer() {
		String message = "Server has been closed :)";
		for (ClientHandler client : clients) {
			client.getOutput().println(message);
		}
		System.out.println("Server closing");
		System.exit(0);
	}
}

