package clipboard;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

public class ClipboardObserver {

	DataSender dataSender = null;

	public ClipboardObserver(DataSender dataSender) {
		this.dataSender = dataSender;
	}

	public void startMonitoring() {

		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

	    if (dataSender == null) {
	      	return;
	    }

		clipboard.addFlavorListener(e -> {
			
			System.out.println("Clipboard content changed!");
			
			try {

				Transferable contents = clipboard.getContents(null);

				if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
					String clipboardText = (String) contents.getTransferData(DataFlavor.stringFlavor);
					System.out.println("New clipboard content: " + clipboardText);
                    dataSender.send(clipboardText);
				}

			} catch (Exception ex) {
				System.out.println("Error fetching clipboard data:" + ex);
				//e.printStackTrace();
			}
		});

		System.out.println("Clipboard monitoring started. Press CTRL+C to stop.");

	}

}
