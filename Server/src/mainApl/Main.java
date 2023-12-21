package mainApl;

import clipboard.ClipboardObserver;
import clipboard.ClipboardServer;
import utils.Nif;

public class Main {

	public static void main(String[] args) {
		
		Apl.displayHead();		
		
		if (!Apl.parseCommandLineAndSetValues(args))
			return;
		
		Apl.clipboardServer = new ClipboardServer("Clipboard", Nif.getPrefferedIPAddress(),  Apl.servicePort+1, Apl.remoteAddress, Apl.remotePort);							
		Apl.clipboardServer.start();

		Apl.clipboardObserver = new ClipboardObserver(Apl.clipboardServer);
		Apl.clipboardObserver.startMonitoring();
	}

	
}
