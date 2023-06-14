import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.JTextField;

public class ClientFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    // Private frame components
    private JTextArea requestText;
    private JTextArea responseText;
    private JLabel lblWordCount;
    private JLabel lblTranslatedWord;
    private JLabel lblStatusValue;
    private JTextField txtRequest;
    private JComboBox<String> languageDropdown;
    private JButton btnTranslate;

    // Private attributes for frame size
    private int width = 700;
    private int height = 200;

    /**
     * The constructor that initializes and organizes the Swing components on the frame.
     */
    public ClientFrame() {
        // Default frame setting
        this.setLayout(new BorderLayout());
        this.setTitle("TCP Application: Client Side");
        this.setSize(width, height);

        // Center the frame on the screen
        this.setLocationRelativeTo(null);

        // Must close on X
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize default value for labels
        lblTranslatedWord = new JLabel("-");
        lblStatusValue = new JLabel("-");

        // Initialize text field for request
        txtRequest = new JTextField(20);

        // Initialize language dropdown
        languageDropdown = new JComboBox<String>();
        languageDropdown.addItem("Bahasa Melayu");
        languageDropdown.addItem("English");
        languageDropdown.addItem("Spanish");
        
        responseText = new JTextArea(3, 30);

        // Initialize translate button
        btnTranslate = new JButton("Translate");
        btnTranslate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = getRequestText();
                String targetLanguage = getSelectedLanguage();
                sendTranslationRequest(text, targetLanguage);
            }
        });

        // Organize components
        loadComponents();

        this.setVisible(true);
    }

    /**
     * This method sends the translation request to the server.
     *
     * @param text            The text to be translated
     * @param targetLanguage  The target language for translation
     */
    private void sendTranslationRequest(String text, String targetLanguage) {
        try {
            // Connect to the server @ localhost, port 1234
            Socket socket = new Socket(InetAddress.getLocalHost(), 1234);

            // Update the status of the connection
            updateConnectionStatus(socket.isConnected());
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
            updateTranslatedWord(translated);

            // Close everything
            bufferedReader.close();
            printWriter.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method updates the translated word on the frame.
     *
     * @param translatedWord: The translated word
     */
    public void updateTranslatedWord(String translatedWord) {
        SwingUtilities.invokeLater(() -> {
            if (responseText != null) {
                responseText.setText(translatedWord);
            }
            else {
            	responseText.setText("error");
            }
        });
    }

    /**
     * This method updates the status of the connection to the server.
     *
     * @param connStatus: Connection status (true/false)
     */
    public void updateConnectionStatus(boolean connStatus) {
        // Default status. Assuming the worst-case scenario.
        String status = "No connection to server.";

        // Validate status of connection
        if (connStatus)
            status = "Connection has been established.";

        // Update the status on the frame
        this.lblStatusValue.setText(status);
    }

    /**
     * This method creates and organizes Swing components to display the connection
     * status of the client.
     *
     * @param font: The font style
     * @return Swing components organized in a panel.
     */
    private JPanel getConnectionStatusPanel(Font font) {
        // Create components
        JPanel pnlStatus = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblStatus = new JLabel("Status: ");
        lblStatus.setFont(font);
        lblStatusValue.setFont(font);

        // Add components to panel
        pnlStatus.add(lblStatus);
        pnlStatus.add(lblStatusValue);

        return pnlStatus;
    }

    /**
     * This method creates and organizes Swing components to display the request and
     * response sections.
     *
     * @param font: The font style
     * @return Swing components organized in a panel.
     */
    private JPanel getRequestResponsePanel(Font font) {
        // Create components
        JPanel pnlRequestResponse = new JPanel(new BorderLayout());

        JLabel lblRequest = new JLabel("Enter word/phrase to translate: ");
        lblRequest.setFont(font);

        JLabel lblLanguage = new JLabel("Select target language: ");
        lblLanguage.setFont(font);

        responseText = new JTextArea(3, 30);
        responseText.setEditable(false);
        responseText.setBackground(Color.LIGHT_GRAY);
        JScrollPane scrollPane = new JScrollPane(responseText);

        JPanel pnlRequest = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlRequest.add(lblRequest);
        pnlRequest.add(txtRequest);

        JPanel pnlLanguage = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlLanguage.add(lblLanguage);
        pnlLanguage.add(languageDropdown);

        JPanel pnlTranslate = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlTranslate.add(btnTranslate);

        JPanel pnlResponse = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlResponse.add(new JLabel("Translated word/phrase: "));
        pnlResponse.add(scrollPane);

        // Add components to panel
        pnlRequestResponse.add(pnlRequest, BorderLayout.NORTH);
        pnlRequestResponse.add(pnlLanguage, BorderLayout.CENTER);
        pnlRequestResponse.add(pnlTranslate, BorderLayout.SOUTH);
        pnlRequestResponse.add(pnlResponse, BorderLayout.SOUTH);

        return pnlRequestResponse;
    }

    /**
     * This method loads and organizes the Swing components on the frame.
     */
    private void loadComponents() {
        // Initialize font
        Font font = new Font("Verdana", Font.PLAIN, 12);

        // Create connection status panel
        JPanel pnlStatus = getConnectionStatusPanel(font);

        // Create request/response panel
        JPanel pnlRequestResponse = getRequestResponsePanel(font);

        // Create translate button and add ActionListener
        JButton btnTranslate = createTranslateButton();

        // Add panels and button to frame
        this.add(pnlStatus, BorderLayout.NORTH);
        this.add(pnlRequestResponse, BorderLayout.CENTER);
        this.add(btnTranslate, BorderLayout.SOUTH);
    }
    
    private JButton createTranslateButton() {
        JButton btnTranslate = new JButton("Translate");
        btnTranslate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = txtRequest.getText();
                String targetLanguage = (String) languageDropdown.getSelectedItem();
                Translator.translate(text, targetLanguage);
            }
        });
        return btnTranslate;
    }

    /**
     * Returns the text entered in the request text field.
     *
     * @return The text entered in the request text field
     */
    public String getRequestText() {
        return txtRequest.getText();
    }

    /**
     * Returns the selected language from the language dropdown.
     *
     * @return The selected language from the language dropdown
     */
    public String getSelectedLanguage() {
        return (String) languageDropdown.getSelectedItem();
    }
}