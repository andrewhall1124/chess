package client;


import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Repl {
    private final PreLogin preLogin;

    public Repl(String serverUrl) {
        preLogin = new PreLogin(serverUrl);
    }

    public void run() {
        System.out.println("Welcome to CS 240 Chess. Sign in to start.");
        System.out.print(preLogin.help());
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = preLogin.eval(line);
                System.out.print(BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }
    private void printPrompt() {
        System.out.print("\n" + RESET + ">>> " + GREEN);
    }

}
