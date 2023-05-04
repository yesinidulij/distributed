
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class BudgetThread extends Thread {
   private String serverAddress;
   private int serverPort;
   private String budgetName;
   private double amount;

   public BudgetThread(String serverAddress, int serverPort, String budgetName, double amount) {
      this.serverAddress = serverAddress;
      this.serverPort = serverPort;
      this.budgetName = budgetName;
      this.amount = amount;
   }

   public void run() {
    try {
        Socket socket = new Socket(serverAddress, serverPort);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

        // Send request to server to retrieve budget
        out.writeObject("GETBUDGET");
        out.writeObject(budgetName);
        out.flush();

        // Receive response from server with budget information
        Budget budget = (Budget)in.readObject();
        synchronized (budget) {
            double currentAmount = budget.getAmount();
            currentAmount += amount;
            budget.setAmount(currentAmount);
            System.out.println("Added " + amount + " to " + budgetName);

            // Send updated budget information back to server
            out.writeObject("UPDATEBUDGET");
            out.writeObject(budget);
            out.flush();
        }

        // Close the connection
        out.close();
        in.close();
        socket.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

}
