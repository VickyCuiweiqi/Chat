import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;




public class Server {
	// private static Object LOCK = new Object();  
	 //private static boolean flag = false; 
	public Server() {
		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(12345);
		} catch (IOException e) {
			System.err.println("Could not listen on port: " + 12345);
			System.exit(-1);
		}

		//connect with two clients
		//ConnectionThread[] st= new ConnectionThread[2];
		
			try {
				Game game=new Game();
				//game.currentPlayer='X';
			    System.err.println("Waiting for connection");
				Socket s1 = serverSocket.accept();
				System.err.println("Started new connection from " + s1.getPort());
				Game.Player p1= game.new Player(s1,'X',game);
				Socket s2 = serverSocket.accept();
				System.err.println("Started new connection from " + s2.getPort());
				Game.Player p2=game.new Player(s2,'O',game);
				p1.setOpponent(p2);
				p2.setOpponent(p1);
				p1.start();
				p2.start();
				//System.out.println(game.board[0]);

			} catch (IOException e) {
				e.printStackTrace();
			}
			
		
		
		
	}

	public static void main(String[] args) {
		new Server();
	}
	 class Game{
		 	//public char currentPlayer;
		 	public int[] board = {
		 	        0, 0, 0,
		 	        0, 0, 0,
		 	        0, 0, 0};
		 	public boolean hasWinner() {
		        return
		            (board[0] != 0 && board[0] == board[1] && board[0] == board[2])
		          ||(board[3] != 0 && board[3] == board[4] && board[3] == board[5])
		          ||(board[6] != 0 && board[6] == board[7] && board[6] == board[8])
		          ||(board[0] != 0 && board[0] == board[3] && board[0] == board[6])
		          ||(board[1] != 0 && board[1] == board[4] && board[1] == board[7])
		          ||(board[2] != 0 && board[2] == board[5] && board[2] == board[8])
		          ||(board[0] != 0 && board[0] == board[4] && board[0] == board[8])
		          ||(board[2] != 0 && board[2] == board[4] && board[2] == board[6]);
		    }
		 	/*
		 	 * check if the board is full
		 	 */
		 	public Boolean CheckBoard(int[] a){
		 		for(int i=0;i<9;i++)
		 		{
		 			if(a[i]==0)return true;
		 		}
		 		return false;
		 	}
		 	/*
			 * update the Board
			 * check if the request grid number is available in the board
			 */
			public Boolean UpdateBoard(int temp,char mark){
				//for player1
				if(mark=='X'){
					
						if(board[temp]==0){
							board[temp]=1;
							return true;
							}
						else return false;//board[temp] is occupied
							
					}
				//for player2
				else if(mark=='O'){
					if(board[temp]==0){
					board[temp]=2;
					return true;
					}
					else return false;//board[temp] is occupied
				}
				return false;
			}
		 	
		 	
		 	
		 class Player extends Thread {
				private Socket socket = null;
				char mark;
				Game game;
				Player opponent;
				BufferedReader inputReader;
		        PrintWriter outputWriter;
				public Player(Socket socket, char mark,Game game) {
					super("ConnectionThread");
					this.socket = socket;
					this.mark=mark;
					this.game=game;
					
				}
				public void setOpponent(Player opponent) {
		            this.opponent = opponent;
		        }
				
				public void display(char[] a){
					outputWriter.println(a[0]+' '+a[1]+' '+a[2]);
					outputWriter.println(a[3]+' '+a[4]+' '+a[5]);
					outputWriter.println(a[6]+' '+a[7]+' '+a[8]);
				}
		 
		
				@SuppressWarnings("null")
				public void run() {
					
					try {	
							//System.out.println("currentPlayer: "+game.currentPlayer);
							System.out.println("mark: "+ mark);
							InputStream inputStream = socket.getInputStream();
							OutputStream outputStream = socket.getOutputStream();

							inputReader = new BufferedReader(new InputStreamReader(inputStream));
							outputWriter = new PrintWriter(outputStream);
							//check if board is full
							if(!game.CheckBoard(game.board)){
								outputWriter.println("GAME END");
								return;
							}
							
							outputWriter.println("please input the grid number from 0-8");
							outputWriter.println("0 1 2");
							outputWriter.println("3 4 5");
							outputWriter.println("6 7 8");
							char[] a = new char[9];
							for (int j=0;j<9;j++){
								if(game.board[j]==0)a[j]='-';
								else if(game.board[j]==1)a[j]='X';
								else if(game.board[j]==2)a[j]='O';
							}
							//Display board to client
							outputWriter.println("Current Situation");
							this.display(a);
							String text = inputReader.readLine();
							//read grid number form client
							System.err.println("Grid number from client is " + text);

							int temp=Integer.parseInt(text);
							//update the value in board;
							while(temp>9||temp<0){
								outputWriter.println("Please enter a new grid number");
								temp=Integer.parseInt(text);
							}
							//temp from 0-8
							boolean temp1=game.UpdateBoard(temp,mark);
							while(!temp1){
									outputWriter.println("Please enter a new grid number, the one you entered has been occupied");
									temp=Integer.parseInt(inputReader.readLine());
									temp1=game.UpdateBoard(temp,mark);
								}
							
							
							/*
							 * Decide winner
							 */
							if(game.hasWinner()){
								outputWriter.println("The winner is "+mark+"! END GAME");
								this.opponent.outputWriter.println("The winner is "+mark+"! END GAME");
								return;
							}
							/*
							 * check if the borad is full
							 */
							if(!game.CheckBoard(game.board)){
								outputWriter.println("GAME END");
								return;
							}
							outputWriter.flush();
							
						
				} 
				
					catch (IOException e) {
					e.printStackTrace();

				}		
				//}
					
				}
			}
	 }

	
}

