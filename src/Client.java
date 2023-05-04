import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        String serverAddress = "localhost"; // IP address of server
        int serverPort = 12345; // Port on which server is listening
        try (
            Socket socket = new Socket(serverAddress, serverPort);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            String budgetName;
            double amount;
            while (true) {
                // Prompt the user for the budget name and amount
                System.out.print("Enter budget name: ");
                budgetName = stdIn.readLine();
                System.out.print("Enter amount: ");
                amount = Double.parseDouble(stdIn.readLine());

                // Send request to server to update budget
                out.writeObject(budgetName);
                out.writeDouble(amount);
                out.flush();

                // Receive response from server with updated budget information
                Budget budget = (Budget)in.readObject();
                System.out.println("Updated budget: " + budget.getName() + " - " + budget.getAmount());
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error communicating with server: " + e.getMessage());
        }
    }
}
