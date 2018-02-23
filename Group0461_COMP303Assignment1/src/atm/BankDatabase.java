package atm;
import java.util.HashMap;
import java.util.Map;

// BankDatabase.java
// Represents the bank account information database 

public class BankDatabase
{
   private Map<Integer,Account> accounts; // array of Accounts
   
   // no-argument BankDatabase constructor initializes accounts
   public BankDatabase()
   {
      accounts = new HashMap<Integer, Account>(); 
      accounts.put(98765,new Account(98765, 56789, 1000.0, 1200.0));
      accounts.put(45678, new Account(45678, 87654, 800.0, 1100.0));
   } 

   private Account getAccount(int accountNumber)
   {
      return accounts.get(accountNumber);
   } // end method getAccount

   // determine whether user-specified account number and PIN match
   // those of an account in the database
   public boolean authenticateUser(int userAccountNumber, int userPIN)
   {
      // attempt to retrieve the account with the account number
      Account userAccount = getAccount(userAccountNumber);

      if (userAccount != null)
         return userAccount.validatePIN(userPIN);
      else
         return false;
   }

   public double getAvailableBalance(int userAccountNumber)
   {
      return getAccount(userAccountNumber).getAvailableBalance();
   }

  public double getTotalBalance(int userAccountNumber)
   {
      return getAccount(userAccountNumber).getTotalBalance();
   }

   public void credit(int userAccountNumber, double amount)
   {
	   getAccount(userAccountNumber).credit(amount);
      
   }
   
   public void debit(int userAccountNumber, double amount)
   {
      getAccount(userAccountNumber).debit(amount);
   }
}

