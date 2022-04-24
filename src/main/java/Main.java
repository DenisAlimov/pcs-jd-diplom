import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.imageio.IIOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    private static final int PORT = 8989;
    private static final String PATH = "pdfs";

    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File(PATH));
        ServerSocket serverSocket = new ServerSocket(PORT);

        while (true) {
            try (Socket clientSocket = serverSocket.accept();
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                out.println("СЕРВЕР: Введите слово для поиска или \"end\" для выхода.");

                String request = in.readLine();
                if (request.equals("end")) {
                    out.println("СЕРВЕР: Работа сервера завершена!");
                    break;
                }
                GsonBuilder builder = new GsonBuilder();
                builder.setPrettyPrinting();
                Gson gson = builder.create();

                out.println("{" + request + "} ->");

                for (PageEntry entry : engine.search(request)) {
                    out.println(gson.toJson(entry));
                }

            } catch (IIOException e) {
                e.printStackTrace();
            }
        }
        serverSocket.close();
    }
}
