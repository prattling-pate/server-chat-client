import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {

	public ChatClient(String ip, int port, String user) {
		try {
			Socket connection = new Socket(ip, port);
			PrintWriter output = new PrintWriter(connection.getOutputStream(), true);
			BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			System.out.println("Successfully joined Chat Server");
			output.println("< " + user + " has joined the chat >");
			InputHandler inputHandler = new InputHandler(output, user);
			inputHandler.start();
			OutputHandler outputHandler = new OutputHandler(input, this);
			outputHandler.start();
		} catch (IOException exception) {
			System.out.println("Server does not exist");
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		Scanner inputScanner = new Scanner(System.in);
		System.out.print("Please enter your username: ");
		ChatClient client = new ChatClient("0.0.0.0", 1024, inputScanner.nextLine());
	}

	public void closeClient() {
		System.out.println("Client is shutting down");
		System.exit(0);
	}

}

class OutputHandler extends Thread {

	private final BufferedReader input;

	private final ChatClient client;

	public OutputHandler(BufferedReader input, ChatClient client) {
		this.input = input;
		this.client = client;
	}

	public void run() {
		try {
			String message;
			while (true) {
				message = input.readLine();
				if (message.equals("Server has been closed :)")) {
					client.closeClient();
				}
				System.out.println(message);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

class InputHandler extends Thread {

	private final PrintWriter output;

	private final String user;

	public InputHandler(PrintWriter out, String user) {
		this.user = user;
		output = out;
	}

	public void run() {
		boolean running = true;
		Scanner inputScanner = new Scanner(System.in);
		String message = "";
		while (running) {
			message = inputScanner.nextLine();
			if (message.equals("!quit")) {
				running = false;
			}
			output.println(user + ": " + message);
		}
	}
}

