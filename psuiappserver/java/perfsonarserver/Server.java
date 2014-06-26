package perfsonarserver;

import java.util.Scanner;

public class Server
{
	public static void main(String[] args)
	{
		boolean exit = false;
		Scanner scan = new Scanner(System.in);

		do
		{
			System.out.println("1: AppConnect test");
			System.out.println("2: Database/Cassandra test");
			System.out.println("3: FetchData test");
			System.out.println("0: Complete");
			System.out.println("E: Exit");
			System.out.println();
			System.out.print("Chose: ");

			switch (scan.nextLine())
			{
			case "1":
				AppConnect.start(scan);
				break;
			case "2":
				Database.start(scan);
				
				break;
			case "3":
				FetchData.start(scan);
				break;
			
			case "0":
				complete(scan);
				break;
			case "E":
			case "e":
				exit = true;
				break;
			default:
				System.out.println("Falsche Eingabe!");
			}
			System.out.println();
		}
		while (!exit);
		scan.close();
	}

	private static void complete(Scanner scan)
	{
		AppConnect.start(scan);
	}
}