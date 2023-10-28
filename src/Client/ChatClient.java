package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {

	private String user;

	private PrintWriter output;

	private BufferedReader input;

	public ChatClient(String ip, int port, String user) {
		try {
			this.user = user;
			Socket connection = new Socket(ip, port);
			output = new PrintWriter(connection.getOutputStream(), true);
			input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
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

	private void startChatting() {
		System.out.println("Successfully joined Chat Server");
		output.println("< " + user + " has joined the chat >");
		startIOThreads();
	}

	// starts the parallel processing threads to handle output from server
	// and input into server from console interface.
	private void startIOThreads() {
		InputHandler inputHandler = new InputHandler(output, user);
		inputHandler.start();
		OutputHandler outputHandler = new OutputHandler(input, this);
		outputHandler.start();
	}

	public void closeClient() {
		System.out.println("Client is shutting down");
		System.exit(0);
	}

}

