package atm;
// Withdrawal.java
// Represents a withdrawal ATM transaction

public class Withdrawal extends Transaction
{
   private int amount; // amount to withdraw
   private CashDispenser cashDispenser; // reference to cash dispenser

    // Withdrawal constructor
   public Withdrawal(int userAccountNumber, BankDatabase atmBankDatabase, 
      CashDispenser atmCashDispenser, int withdrawalAmount)
   {
      // initialize superclass variables
      super(userAccountNumber, atmBankDatabase);
      
      // initialize references to keypad and cash dispenser
      cashDispenser = atmCashDispenser;
      amount = withdrawalAmount;
   } // end Withdrawal constructor

   // perform transaction
   @Override
   public String execute()
   {
      double availableBalance; // amount available for withdrawal
      String message = "";
      // get references to bank database and screen
      BankDatabase bankDatabase = getBankDatabase(); 

            // get available balance of account involved
            availableBalance = 
               bankDatabase.getAvailableBalance(getAccountNumber());
      
            // check whether the user has enough money in the account 
            if (amount <= availableBalance)
            {   
               // check whether the cash dispenser has enough money
               if (cashDispenser.isSufficientCashAvailable(amount))
               {
                  // update the account involved to reflect the withdrawal
                  bankDatabase.debit(getAccountNumber(), amount);
                  
                  cashDispenser.dispenseCash(amount); // dispense cash

                  // instruct user to take cash
                 message = "\nYour cash has been dispensed. Please take your cash now.";
                 message += "Your total balance is: " + bankDatabase.getTotalBalance(getAccountNumber());
               
               } // end if
               else // cash dispenser does not have enough cash
                  message =
                     "\nInsufficient cash available in the ATM." +
                     "\n\nPlease choose a smaller amount.";
            } // end if
            else // not enough money available in user's account
            {
               message = "\nInsufficient funds in your account." +
                  "\n\nPlease choose a smaller amount."; 
            } // end else
      
      return message;

   } // end method execute
} // end class Withdrawal

