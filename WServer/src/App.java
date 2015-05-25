import repository.UsersRegistry;


public class App {

	public static void main(String[] args) {
		Thread t = new Thread(new Server());
		UsersRegistry.emptyFile();
		t.start();
	}

}
