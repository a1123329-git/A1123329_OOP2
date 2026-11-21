import java.io.*;
import java.net.*;
import java.util.Random;

/**
 * 猜數字遊戲 - 伺服器端
 * 重點：
 *  #3 readLine() / println()：一次讀一行指令、回一行結果
 *  #4 try-with-resources：結束時自動關閉 socket 與串流
 *
 * 協定：玩家每送一行，伺服器就回一行。
 */
public class GuessServer {

    static final int PORT = 5000;

    public static void main(String[] args) {
        System.out.println("Server started, waiting for a player on port " + PORT + "...");

        // #4 try-with-resources：括號內的資源結束時會自動 close()
        try (ServerSocket server = new ServerSocket(PORT);
             Socket socket = server.accept();                       // 等待一位玩家連線
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) { // true = 自動 flush

            System.out.println("Player connected: " + socket.getInetAddress());

            Random random = new Random();
            int secret = random.nextInt(100) + 1;   // 1 ~ 100
            int tries = 0;

            out.println("Welcome! I'm thinking of a number from 1 to 100. Type a guess, or 'quit' to leave.");

            // #3 readLine()：一次讀一行，null 代表玩家斷線
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                System.out.println("Player sent: " + line);

                if (line.equalsIgnoreCase("quit")) {
                    out.println("Bye! The number was " + secret + ".");
                    break;
                }

                int guess;
                try {
                    guess = Integer.parseInt(line);
                } catch (NumberFormatException e) {
                    out.println("Please type a whole number from 1 to 100.");
                    continue;
                }

                if (guess < 1 || guess > 100) {
                    out.println("Out of range! Pick a number from 1 to 100.");
                    continue;
                }

                tries++;
                if (guess < secret) {
                    out.println("Higher!");
                } else if (guess > secret) {
                    out.println("Lower!");
                } else {
                    out.println("Correct! The number was " + secret + ". You took " + tries
                            + " tries. New round started - guess again, or 'quit'.");
                    secret = random.nextInt(100) + 1;   // 開新一局
                    tries = 0;
                }
            }

            System.out.println("Player left. Server closing.");
        } catch (IOException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
        // 離開 try 之後，in、out、socket、server 都已自動關閉
    }
}