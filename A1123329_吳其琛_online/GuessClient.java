import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * 猜數字遊戲 - 玩家端
 * 從鍵盤讀輸入 → println() 送給伺服器 → readLine() 讀回結果
 */
public class GuessClient {

    public static void main(String[] args) {
        String host = (args.length > 0) ? args[0] : "localhost";   // 可指定伺服器 IP
        int port = 5000;

        // #4 try-with-resources：結束時自動關閉 socket、串流與 Scanner
        try (Socket socket = new Socket(host, port);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner keyboard = new Scanner(System.in)) {

            System.out.println("Server: " + in.readLine());   // 歡迎訊息

            while (true) {
                System.out.print("Your guess: ");
                if (!keyboard.hasNextLine()) break;
                String input = keyboard.nextLine();

                out.println(input);                // #3 送出一行
                String reply = in.readLine();      // #3 讀回一行
                if (reply == null) {
                    System.out.println("Server disconnected.");
                    break;
                }
                System.out.println("Server: " + reply);

                if (input.trim().equalsIgnoreCase("quit")) break;
            }
        } catch (IOException e) {
            System.out.println("Cannot connect to server: " + e.getMessage());
        }
    }
}