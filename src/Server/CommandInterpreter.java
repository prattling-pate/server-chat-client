package Server;

public class CommandInterpreter {

	private final ChatServer server;

	public CommandInterpreter(ChatServer server) {
		this.server = server;
	}

	public void interpretCommands(String command) {
		if (command.matches("!quit")) {
			server.stopServer();
		}
	}
}
