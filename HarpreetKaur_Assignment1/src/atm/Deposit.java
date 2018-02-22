package atm;
// Deposit.java
// Represents a deposit ATM transaction

public class Deposit extends Transaction
{
   private double amount; // amount to deposit
   
   // Deposit constructor
   public Deposit(int userAccountNumber, BankDatabase atmBankDatabase, int withdrawalAmount)
   {
      super(userAccountNumber, atmBankDatabase); 
      amount = withdrawalAmount;
   } 

   // perform transaction
   @Override
   public String execute()
   {
      BankDatabase bankDatabase = getBankDatabase();
      // credit account to reflect the deposit
      
      bankDatabase.credit(getAccountNumber(), amount); 
      
      return "Your balance is: " + bankDatabase.getTotalBalance(getAccountNumber());
      
   } 

  
} // end class Deposit
