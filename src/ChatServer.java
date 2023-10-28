import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
	private final ServerSocket serverSocket;
	private final ArrayList<ClientHandler> sockets;

	public ChatServer() throws IOException {
		sockets = new ArrayList<>();
		serverSocket = new ServerSocket(1024);
	}

	public static void main(String[] args) throws IOException {
		ChatServer server = new ChatServer();
		server.startServer();
	}

	// need to run handling adding new users, handling IO and console all in parallel
	public void startServer() throws IOException {
		// IDK if this will work rn
		while (true) {
			// waits for connection request from clients
			sockets.add(new ClientHandler(serverSocket.accept()));
			System.out.println(sockets.getLast().getInput().readLine());
			sockets.getLast().start();
		}
	}
}

class ClientHandler extends Thread {

	private final Socket connection;

	private PrintWriter output;

	private BufferedReader input;

	public ClientHandler(Socket accept) throws IOException {
		connection = accept;
		output = new PrintWriter(connection.getOutputStream(),
				true);
		input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
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
				output.println(inputLine);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
