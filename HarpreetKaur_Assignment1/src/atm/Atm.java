package atm;

import java.util.Map;

public class Atm
{
   private boolean userAuthenticated; // whether user is authenticated
   private int currentAccountNumber; // current user's account number
  
   private CashDispenser cashDispenser; // ATM's cash dispenser
   private BankDatabase bankDatabase; // account information database

   // constants corresponding to main menu options
   private static final int BALANCE_INQUIRY = 1;
   private static final int WITHDRAWAL = 2;
   private static final int DEPOSIT = 3;
   private String customer;
   private String password;
   private String amount;
   private String operation;
   private String response;

   public Atm(Map<String, String> userData) 
   {
	   customer = userData.get("customer");
	   password = userData.get("customerPin");
	   amount = userData.get("amount");
	   operation = userData.get("operation");
	   
	   cashDispenser = new CashDispenser(); // create cash dispenser
	   bankDatabase = new BankDatabase(); // create acct info database
   }

   //process request by authenticating and then performing transaction
   public String processRequest()
   {
         String status = authenticateUser(); // authenticate user
         if(status.length()>0){ // unsuccessful authentication
        	 return status;
         }         
       
         //successful authentication
         
         response =  performTransactions(); //Perform the transaction and return the response back to client
         
         return response;

   } 

  
   private String authenticateUser() 
   {
	   if(customer.equals("") || password.equals("") || operation.equals("0") || 
			   (amount.equals("") && !operation.equals("1"))){ //If withdrawing or depositing, amount is required
		   return "Error in data.";
	   }
      
	   int accountNumber = Integer.parseInt(customer); 
      int pin = Integer.parseInt(password); 
      
      // set userAuthenticated to boolean value returned by database
      userAuthenticated = 
         bankDatabase.authenticateUser(accountNumber, pin);
      
      // check whether authentication succeeded
      if (userAuthenticated)
      {
         currentAccountNumber = accountNumber; // save user's account #
         return "";
      }
      else
         return "Invalid account number or PIN. Please try again.";
      
   }

 
   private String performTransactions() 
   {
      // local variable to store transaction currently being processed
      Transaction currentTransaction = null;
          
      int operationSelection = Integer.parseInt(operation);

      // initialize as new object of chosen type
      currentTransaction = createTransaction(operationSelection);

      response = currentTransaction.execute(); // execute transaction
               
      return response;
   }
   
  
   // return object of specified Transaction subclass
   private Transaction createTransaction(int type)
   {
      Transaction temp = null; // temporary Transaction variable
      
      int withdrawalAmt;
      // determine which type of Transaction to create     
      switch (type){
         case BALANCE_INQUIRY: // create new BalanceInquiry transaction
            temp = new BalanceInquiry(currentAccountNumber, bankDatabase);
            break;
         case WITHDRAWAL: // create new Withdrawal transaction
        	withdrawalAmt = Integer.parseInt(amount);
            temp = new Withdrawal(currentAccountNumber, bankDatabase, cashDispenser, withdrawalAmt);
            break; 
         case DEPOSIT: // create new Deposit transaction
        	withdrawalAmt = Integer.parseInt(amount);
            temp = new Deposit(currentAccountNumber, bankDatabase, withdrawalAmt);
            break;
      }

      return temp;
   }
}
