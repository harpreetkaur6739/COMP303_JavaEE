package atm;
// BalanceInquiry.java
// Represents a balance inquiry ATM transaction

public class BalanceInquiry extends Transaction
{
   // BalanceInquiry constructor
   public BalanceInquiry(int userAccountNumber,BankDatabase atmBankDatabase)
   {
      super(userAccountNumber, atmBankDatabase);
   } // end BalanceInquiry constructor

   // performs the transaction
   @Override
   public String execute()
   {
	   String message = "";
      // get references to bank database and screen
      BankDatabase bankDatabase = getBankDatabase();
     // get the available balance for the account involved
      double availableBalance = 
         bankDatabase.getAvailableBalance(getAccountNumber());

      // get the total balance for the account involved
      double totalBalance = 
         bankDatabase.getTotalBalance(getAccountNumber());
      
      // display the balance information on the screen
      message += "\nBalance Information";
      message += "\nAvailable balance: " + availableBalance;
      message += "\nTotal balance: " + totalBalance;
      
      return message;
      
   } // end method execute
} // end class BalanceInquiry
