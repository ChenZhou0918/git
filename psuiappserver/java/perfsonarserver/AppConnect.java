package perfsonarserver;

import java.util.Scanner;

import perfsonarserver.appConnect.Listener;

public class AppConnect
{
	public static void start(Scanner scan)
	{
		Thread listener = new Thread(new Listener());
		listener.start();
	}
}