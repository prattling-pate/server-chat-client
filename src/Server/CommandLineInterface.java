package Server;

import java.util.Scanner;

public class CommandLineInterface extends Thread {
	private Scanner inputScanner;

	private final CommandInterpreter commandInterpreter;

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
