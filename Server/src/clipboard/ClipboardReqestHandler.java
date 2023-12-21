package clipboard;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClipboardReqestHandler {

	private Socket socket;

	public synchronized void handleRequest(Socket socket) {
		this.socket = socket;
		BufferedReader in = null;
		BufferedOutputStream dataOut = null;

		try {

			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			dataOut = new BufferedOutputStream(socket.getOutputStream());
			String input = in.readLine();

			if (input == null || input.length() < 1) {
				System.err.println("Error processing request, " + input == null ? "no input!" : "input length= " + input.length());
				return;
			}

			System.out.println("Got data from: " + socket.getRemoteSocketAddress() + " > data: " + input);

			try {

				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				StringSelection selection = new StringSelection(input); 		// Create a StringSelection object with the data to be copied
				clipboard.setContents(selection, null); 						// Set the StringSelection object as the clipboard's contents

			} catch (Exception e) {
				System.out.println("\nException: " + e);
			}

		}

		catch (Exception e) {
			System.out.println("Handle request server error : " + e);
		}

		finally {

			try {
				if (in != null)
					in.close();
			} catch (Exception e) {
				System.err.println("Error closing in stream: " + e);
			}

			try {
				if (socket != null)
					socket.close();
			} catch (Exception e) {
				System.err.println("Error closing stream - socket: " + e);
			}

			try {
				if (dataOut != null)
					dataOut.close();
			} catch (Exception e) {
				System.err.println("Error closing stream - dataOut: " + e);
			}

		}

	}

}
