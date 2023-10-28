package Client;

import java.io.BufferedReader;
import java.io.IOException;

public class OutputHandler extends Thread {

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
