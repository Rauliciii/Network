import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import model.DataSocketManage;
import controller.Interpreter;
import model.User;
import repository.UsersRegistry;

public class Server implements Runnable {

	@Override
	public void run() {
		while (true) {
			try {
				final int port = 54321;
				System.out.println("Server waiting for connection on port "
						+ port);
				ServerSocket ss = new ServerSocket(port);
				Socket clientSocket = ss.accept();
				System.out.println("Recieved connection from "
						+ clientSocket.getInetAddress() + " on port "
						+ clientSocket.getPort());
				// create two threads to send and recieve from client
				RecieveFromClientThread recieve = new RecieveFromClientThread(
						clientSocket);
				Thread thread = new Thread(recieve);
				thread.start();
				SendToClientThread send = new SendToClientThread(clientSocket);
				Thread thread2 = new Thread(send);
				thread2.start();
				ss.close();
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}


class RecieveFromClientThread implements Runnable {
	Socket clientSocket = null;
	BufferedReader brBufferedReader = null;

	public RecieveFromClientThread(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	private String handleClient(String command) {
		// takes the command as argument, takes care of the command and
		// retrives a string containing the action that must be done
		System.out.println("S: I recived command: " + command);
		List<String> myList = Interpreter.interpretString(command);

		String messgBTClient = "";
		switch (myList.get(0)) {
		case "connect":
			System.out.println("S: connecting " + myList.get(2));
			boolean userAdded = new UsersRegistry().addUser(new User(myList
					.get(2)));
			if (userAdded == true) {
				messgBTClient = "CONNECTION SUCCESS";
				DataSocketManage.add(myList.get(2), clientSocket); //if worked, I also put it in the list of sockets
			} else {
				messgBTClient = "CONNECTION FAIL";
			}

			break;
		case "list":
			System.out.println("S: This must be send to client:"
					+ new UsersRegistry().toString());
			messgBTClient += new UsersRegistry().toString();
			break;
		case "send":
			String nickname = myList.get(1);
			Socket sock = DataSocketManage.getSocketFromString(nickname);
			try {
				PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(
						sock.getOutputStream()));
				System.out.println("Sending: " + myList.get(1)+": "+ myList.get(2) );
				printWriter.println(myList.get(2));
				printWriter.flush();
				messgBTClient += "SUCCESS SEND";
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case "disconnect":
			messgBTClient += "EXIT";
			break;
		default:
			messgBTClient += "Bad command";
		}
		return messgBTClient;
	}
	
	
	public void run() {
		try {
			brBufferedReader = new BufferedReader(new InputStreamReader(
					this.clientSocket.getInputStream()));

			String messageString;
			while (true) {

				while ((messageString = brBufferedReader.readLine()) != null) {
					
					String returnMessage = this.handleClient(messageString);
					PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(
							this.clientSocket.getOutputStream()));// get outputstream
					
					if (returnMessage.equals("EXIT")) {
						break;
					}
					printWriter.println(returnMessage);
					printWriter.flush();
				}
				this.clientSocket.close();
				//System.exit(0);
			}

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}

class SendToClientThread implements Runnable {
	private PrintWriter pwPrintWriter;
	public Socket clientSock = null;

	public SendToClientThread(Socket clientSock) {
		this.clientSock = clientSock;
	}

	public void run() {
		try {
			pwPrintWriter = new PrintWriter(new OutputStreamWriter(
					this.clientSock.getOutputStream()));// get outputstream

			while (true) {
				String msgToClientString = null;
				BufferedReader input = new BufferedReader(
						new InputStreamReader(System.in));// get userinput
				msgToClientString = input.readLine();// get message to send to
														// client
				pwPrintWriter.println(msgToClientString);// send message to
															// client with
															// PrintWriter
				pwPrintWriter.flush();

			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
}