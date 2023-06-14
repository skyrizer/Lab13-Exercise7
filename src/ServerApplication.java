import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class ServerApplication {

	public static void main(String[] args) throws IOException {
		
		// Binding to a port or any other port no you are fancy of
		int portNo = 1234;
		ServerSocket serverSocket = new ServerSocket(portNo);

		// Launch the server frame
		ServerFrame serverFrame = new ServerFrame();
		serverFrame.setVisible(true);

		// Counter to keep track the number of requested connection
		int totalRequest = 0;

		// Server needs to be alive forever
		while (true) {

			// Message to indicate server is alive
			serverFrame.updateServerStatus(false);


			// Accept client request for connection
			Socket clientSocket = serverSocket.accept();


			// Read stream data from the client
			BufferedReader bufferReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			String input = bufferReader.readLine();
			String targetLanguage = bufferReader.readLine();
			System.out.println("input = " + input);
			
			String translatedWord = Translator.translate(input,targetLanguage);

			// Create stream to write data on the network
			PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream());

			printWriter.println(translatedWord);
			printWriter.flush();
			System.out.println("translated word sent to client");
			

			// Close the socket
			clientSocket.close();
			
			printWriter.close();
	
			bufferReader.close();
			
			// Update the request status
			serverFrame.updateRequestStatus(
					"Translated Word is " + translatedWord);
			serverFrame.updateRequestStatus(input);
			serverFrame.updateRequestStatus("Accepted connection to from the "
					+ "client. Total request = " + ++totalRequest );
			
			

		}
		
		
	}
	


}
