package mainApl;


import clipboard.ClipboardObserver;
import clipboard.ClipboardServer;
import utils.Nif;



public class Apl {
	
	public static final String  				VERSION 				= "1.0";	
	public static int 							myPort 					= 8084;
	public static int 							remotePort				= myPort;	
	public static ClipboardServer				clipboardServer			= null;	
	public static ClipboardObserver				clipboardObserver		= null;	
	public static String						remoteAddress;

	
	public static boolean parseCommandLineAndSetValues(String[] args) {		
		boolean retVal = true;
		int i = 0;
		String arg;

		while (i < args.length) {
			
			arg = args[i++].toLowerCase();
			
			if (arg.startsWith("-"))
		        arg = arg.substring(1);
						
			if (arg.equals("?") || arg.equals("h")) {
				displayHelpInformation();
				return false;
			}
			
			else if (arg.equals("n") || arg.equals("net")) {
				if (i < args.length)
					Nif.setPrefferedInetAddress(Nif.getInetAddressOfNic(args[i++]));
				else 
					System.err.println("Error: - n requires a network interface name");
			}
			
			else if (arg.equals("p1") || arg.equals("port1")) {
				if (i < args.length)
					try {
						myPort	= Integer.parseInt(args[i++]);
					}
					catch(Exception e) {
						System.err.println("Error: can not parse port number!");	
					}				
			}

			else if (arg.equals("p2") || arg.equals("port2")) {
				if (i < args.length)
					try {
						remotePort	= Integer.parseInt(args[i++]);
					}
					catch(Exception e) {
						System.err.println("Error: can not parse port number!");	
					}				
			}
			
			else if (arg.equals("r") || arg.equals("remote")) {
				if (i < args.length)
					try {
						remoteAddress = args[i++];
					}
					catch(Exception e) {
						System.err.println("Error: can not parse file path!");	
					}				
			}
			
			
		}
	
		return retVal;
	}
		
	public static void displayHead() {
		System.out.println("");
		System.out.println("  +----------------------------------------------+");
		System.out.println("  |                                              |");
		System.out.println("  |           Cliboard  Server ver. " + Apl.VERSION + "          |");
		System.out.println("  |                                              |");
		System.out.println("  +----------------------------------------------+\n");		
		
		Apl.displayNetworkInformation();
	}
	
	public static void displayNetworkInformation() {
		System.out.println("My address:  " +  Nif.getPrefferedIPAddress());
		System.out.println("My port:     " +  myPort);
	}
	
	public static void displayHelpInformation() {
		System.out.println("Command line options: ");
		System.out.println("\t-port PORT NUMBER         - port number the server wil be listening on (the default is: " + myPort + ")" );
		System.out.println("\t-n NETWORK INTERFACE NAME - the name of the network interface name which will be used (listed below)" );
		System.out.println("");		
	}
	
}
