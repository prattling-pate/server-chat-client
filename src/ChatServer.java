import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ChatServer {
	private final ServerSocket serverSocket;
	private final ArrayList<ClientHandler> clients;

	private final CommandLineInterface commandLineInterface;


	public ChatServer() throws IOException {
		clients = new ArrayList<>();
		serverSocket = new ServerSocket(1024);
		commandLineInterface = new CommandLineInterface(this);
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
	}

	private void mainServerLoop() throws IOException {
		while (true) {
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

	public void kickUser(String username) {

	}

	public void stopServer() {
		closeServer();
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

class ClientHandler extends Thread {

	private final PrintWriter output;
	private final BufferedReader input;
	private final ArrayList<ClientHandler> clients;

	public ClientHandler(Socket accept, ArrayList<ClientHandler> clients) throws IOException {
		this.clients = clients;
		output = new PrintWriter(accept.getOutputStream(),
				true);
		input = new BufferedReader(new InputStreamReader(accept.getInputStream()));
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
				if (inputLine.contains("!quit")) {
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


class CommandInterpreter {

	private final ChatServer server;

	public CommandInterpreter(ChatServer server) {
		this.server = server;
	}

	public void interpretCommands(String command) {
		if (command.matches("!quit")) {
			server.stopServer();
		}
		//else if (command.matches("!kick\\s+\\w+"))
		//
		//
	}
}

class CommandLineInterface extends Thread {
	private final CommandInterpreter commandInterpreter;
	private final Scanner inputScanner;

	public CommandLineInterface(ChatServer server) {
		inputScanner = new Scanner(System.in);
		commandInterpreter = new CommandInterpreter(server);
	}

	public void run() {
		boolean running = true;
		String command;
		while (running) {
			command = inputScanner.next();
			commandInterpreter.interpretCommands(command);
		}
	}

}

