import java.net.Socket;

import javax.swing.SwingUtilities;

import java.net.InetAddress;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ClientApplication {

    public static void main(String[] args) {

        ClientFrame clientFrame = new ClientFrame();

        try {
            // Retrieve the word/phrase and target language from the GUI components
            String text = clientFrame.getRequestText();
            String targetLanguage = clientFrame.getSelectedLanguage();

            // Connect to the server @ localhost, port 1234
            Socket socket = new Socket(InetAddress.getLocalHost(), 1234);

            // Update the status of the connection
            clientFrame.updateConnectionStatus(socket.isConnected());
            System.out.println(socket.isConnected());

            // Write text and target language to the server
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

            // Send text and target language
            printWriter.println(text);
            printWriter.println(targetLanguage);

            // Read translated text from the server
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Display translated text
            String translated = bufferedReader.readLine();
            clientFrame.updateTranslatedWord(translated);
            

            // Close everything
            bufferedReader.close();
            printWriter.flush();
            printWriter.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}