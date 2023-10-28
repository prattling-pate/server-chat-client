package Client;

import java.io.PrintWriter;
import java.util.Scanner;

public class InputHandler extends Thread {

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
