import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client{
	public Client(){
		try{
			Socket socket = new Socket("127.0.0.1",12345);
			PrintWriter outputWriter=new PrintWriter(socket.getOutputStream());
			InputStreamReader isr=new InputStreamReader(socket.getInputStream());
			BufferedReader inputReader= new BufferedReader(isr);
			Scanner inputKeyboard = new Scanner(System.in);
			
			//asks for input from user to play
			System.out.println("Please enter the grid number:");
			String text=inputKeyboard.nextLine();
			outputWriter.println(text);
			outputWriter.flush();
			
			//read response from server
			String response= inputReader.readLine();
			System.out.println("Respnse from server is"+response);
		socket.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		new Client();
	}
}

