import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
	private String currentUser;
	private Socket connection;

	public ChatClient(String ip, int port, String user) throws IOException {
		currentUser = user;
		connection = new Socket(ip, port);
		PrintWriter output = new PrintWriter(connection.getOutputStream(), true);
		BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		System.out.println("Successfully joined Chat Server");
		output.println(currentUser + " has joined the chat");
		InputHandler inputHandler = new InputHandler(output, user);
		inputHandler.start();
		OutputHandler outputHandler = new OutputHandler(input);
		outputHandler.start();
	}

	public static void main(String[] args) throws IOException {
		Scanner inputScanner = new Scanner(System.in);
		ChatClient client = new ChatClient("localhost", 1024, inputScanner.nextLine());
	}

}

class OutputHandler extends Thread {

	private final BufferedReader input;

	public OutputHandler(BufferedReader input) {
		this.input = input;
	}

	public void run() {
		try {
			while (true) {
				System.out.println(input.readLine());
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
		Scanner inputScanner = new Scanner(System.in);
		while (true) {
			output.println(user + ": " + inputScanner.nextLine());
		}
	}
}

