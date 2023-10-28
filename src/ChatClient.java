import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.Buffer;
import java.util.Scanner;

public class ChatClient {
	private String currentUser;
	private Socket connection;

	public ChatClient(String ip, int port, String user) throws IOException {
		currentUser = user;
		connection = new Socket(ip, port);
		System.out.println("Successfully joined ChatServer");
		PrintWriter output = new PrintWriter(connection.getOutputStream(), true);
		BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		output.println(currentUser + " has joined the chat");
		InputHandler inputHandler = new InputHandler(output, user);
		inputHandler.start();
		OutputHandler outputHandler;
	}

	public static void main(String[] args) throws IOException {
		Scanner inputScanner = new Scanner(System.in);
		ChatClient client = new ChatClient("localhost", 1024, inputScanner.nextLine());

	}

	private String getUser() {
		return currentUser;
	}

	public void setUser(String username) {
		currentUser = username;
	}

}

class OutputHandler extends Thread {

	private BufferedReader input;

	public OutputHandler(BufferedReader input) {
		this.input = input;
	}
}

class InputHandler extends Thread {

	private PrintWriter output;

	private String user;
	public InputHandler(PrintWriter out, String user) {
		this.user = user;
		output = out;
	}

	public void run() {
		Scanner inputScanner = new Scanner(System.in);
		while (true) {
			output.println(user +": "+inputScanner.nextLine());
		}
	}
}

