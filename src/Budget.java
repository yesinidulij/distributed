import java.io.Serializable;

public class Budget implements Serializable {
   private static final long serialVersionUID = 1L;
   private String name;
   private double amount;
   
   public Budget(String name, double amount) {
      this.name = name;
      this.amount = amount;
   }
   
   public String getName() {
      return name;
   }
   
   public double getAmount() {
      return amount;
   }
   
   public void setAmount(double amount) {
      this.amount = amount;
   }
}
