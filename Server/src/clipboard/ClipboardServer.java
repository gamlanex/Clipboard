package clipboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import utils.Pause;

public class ClipboardServer extends Thread implements DataSender {

	private String name = "";
	private ServerSocket serverSocket;
	private String remoteAddress;
	private int remotePort;

	private volatile boolean stop;
	private volatile boolean disabled = false;

	public ClipboardServer(String name, InetAddress myAddress, int myPort, String remoteAddress, int remotePort) {

		this.name = name;
		this.remoteAddress = remoteAddress;
		this.remotePort = remotePort;
		
		if (remoteAddress == null) {
			System.err.println("\nNo remote address, exiting !!!");
			System.exit(1);
		}

		System.out.println("\n- Creating " + name + " server -\n");
		
		System.out.println("- Remote address\t" + remoteAddress + ":" + remotePort);
		
		try {

			serverSocket = createServerSocket(myAddress, myPort);

			if (serverSocket == null) {
				System.err.println("Error: Socket creation error!");
				return;
			}  

			// System.out.println("- Binding " + name + "\tserver socket to: " + inetAddress
			// + " Port: " + port);
			// serverSocket.bind(new InetSocketAddress(inetAddress, port));

		} catch (IOException e) {
			System.err.println("Error: Not able to start " + name + " Server: " + e.getMessage());
			System.out.println("\n- Terminating -");
			return;
		} catch (SecurityException e) {
			System.err.println("Error: Not able to bind socket: " + e.getMessage());
			System.out.println("\n- Terminating -");
			return;
		} catch (IllegalArgumentException e) {
			System.err.println("Error: Not able to bind socket: " + e.getMessage());
			System.out.println("\n- Terminating -");
			return;
		}

	}

	@Override
	public void run() {

		try {
			stop = false;

			while (!stop) {

				if (disabled) {
					Pause.p(300);
					continue;
				}

				System.out.println("- Server: " + name + " waiting for connection");

				(new ClipboardReqestHandler()).handleRequest(serverSocket.accept());
			}

		} catch (IOException e) {
			System.err.println("Error: Server " + name + "Connection error : " + e.getMessage());
		} finally {
			System.err.println("- Stopping " + name + " server (pending requests will be finished)");
			try {
				if (serverSocket != null) {
					System.err.println("- Closing " + name + " server socket");
					serverSocket.close();
				}
			} catch (IOException e) {
				System.err.println("Error closing server socket");
			}

		}

	}

	public void stopServer() {
		stop = true;
	}

	public void disable(boolean b) {
		disabled = b;
	}

	private ServerSocket createServerSocket(InetAddress myAddress, int myPort) throws IOException {

		System.out.println("- My address\t\t" + myAddress.getHostAddress() + ":" + myPort);
		ServerSocket socket = null;

		try {
			socket = new ServerSocket();
			socket.bind(new InetSocketAddress(myAddress, myPort));
			
		} catch (IOException | SecurityException | IllegalArgumentException e) {
			System.err.println("Error creating plain socket: " + e);
			System.out.println("- Exit -");
			System.exit(1);
		}

		return socket;
	}

	@Override
	public void send(String data) {

		try {
			System.out.println("send out, to: " + remoteAddress + ":" + remotePort);
			
			@SuppressWarnings("resource")
			Socket socket = new Socket(remoteAddress, remotePort);
		
			System.out.println("send out - 2");			
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			
			System.out.println("send out - data:\n" + data);
			
			out.println(data);

		} catch (UnknownHostException e) {
			System.err.println("Unknsend outost: " + remoteAddress);
		} catch (IOException e) {
			System.err.println("Error connecting to the server: " + e.getMessage());
		}
	}

}
