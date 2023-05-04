import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class BudgetServer {
   private ConcurrentHashMap<String, Budget> budgets;
   private ServerSocket serverSocket;
   private int port;

   public BudgetServer(int port) {
      budgets = new ConcurrentHashMap<>();
      this.port = port;
   }
   
   public void start() throws IOException {
      serverSocket = new ServerSocket(port);
      System.out.println("Server listening on port " + port);
      
      while (true) {
         Socket clientSocket = serverSocket.accept();
         new ClientHandler(clientSocket).start();
      }
   }
   
   private class ClientHandler extends Thread {
      private Socket clientSocket;
      private ObjectInputStream input;
      private ObjectOutputStream output;
      
      public ClientHandler(Socket clientSocket) {
         this.clientSocket = clientSocket;
      }
      
      public void run() {
         try {
            input = new ObjectInputStream(clientSocket.getInputStream());
            output = new ObjectOutputStream(clientSocket.getOutputStream());
            
            while (true) {
               String budgetName = (String) input.readObject();
               
               double amount = input.readDouble();
               
               synchronized (budgets) {
                  Budget budget = budgets.get(budgetName);
                  System.out.println(budgets.get(budgetName));
                  if (budget == null) {
                     budget = new Budget(budgetName, amount);
                     budgets.put(budgetName, budget);
                  } else {
                     double currentAmount = budget.getAmount();
                     currentAmount += amount;
                     budget.setAmount(currentAmount);
                  }
               }
               
               output.writeObject(budgets.get(budgetName));
               output.reset();
            }
         } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
         } finally {
            try {
               clientSocket.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }
   }
   
   public void printBudgets() {
      synchronized (budgets) {
         for (Budget budget : budgets.values()) {
            System.out.println(budget.getName() + ": " + budget.getAmount());
         }
      }
   }
   
   public static void main(String[] args) throws IOException {
    int port = 12345;
    BudgetServer server = new BudgetServer(port);
    server.start();
}

   
}
