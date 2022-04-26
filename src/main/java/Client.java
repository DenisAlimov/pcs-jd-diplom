import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final String HOST = "localhost";
    private static final int PORT = 8989;
    public static Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        String buffer;
        System.out.println("Введите слово для поиска или \"end\".");
        String word = SCANNER.nextLine();

        try (
                Socket clientSocket = new Socket(HOST, PORT);
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {

            out.println(word);

            while (true) {
                buffer = in.readLine();
                if (buffer == null)
                    break;
                System.out.println(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}