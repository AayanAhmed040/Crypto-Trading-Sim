import java.util.Scanner;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

//Our Login class gives the user an option to sign up or login to an existing account
public class Main {

  public static String currentUser = "";

  public static void main(String[] args){
    System.out.println("Welcome to the Crypto Trading Simulator!\n\nPlease pick an option:");
      // We start in the sign-in menu
      signInMenu();
  }

  private static void signInMenu(){
    //Asking the user to either signup, login, or leave

    Scanner myObj = new Scanner(System.in);
    System.out.println("[1]: Sign Up\n[2]: Login\n[3]: Quit");
    int choice = myObj.nextInt();

    switch(choice){
      case 1:
        signUp();
        break;
      case 2 :
        login();
        break;
      case 3:
        System.out.println("Exiting simulator...\n\nSee you again soon!");
        break;
      default:
        System.out.println("Invalid option\nExiting simulator...\n\nSee you again soon!");
    }
  }


  private static void signUp(){
    Scanner myObj = new Scanner(System.in);
    String line = "";
    System.out.println("\nUsername: ");
    String username = myObj.nextLine();
    ArrayList<String> usersUsed = new ArrayList<String>();

    try (BufferedReader br = new BufferedReader(new FileReader("loginInfo.csv"))) {

      //Checking our loginInfo file to check which usernames are already taken
      while((line = br.readLine()) != null){
        String[] values = line.split(",");
        usersUsed.add(values[0]);}

      // Only letting user continue if the their username is not already being used
      if(usersUsed.contains(username)){
        System.out.println("\nThis username is already being used.\nPlease select another username or login.");
        signInMenu();

      // Asking for the users password, if they entered a valid username
      } else{
        System.out.println("Password: ");
        String password = myObj.nextLine();
        String logIN = username + "," + password;
        System.out.println("\nNew user created. Welcome " + username);
        System.out.println("How much CAD do you want to start with? ");
        double startingCAD = myObj.nextDouble();
        startingCAD = Math.round(startingCAD * 100.0) / 100.0;
        currentUser = username;

        //Saving username and password to our file
        try (PrintWriter myWriter = new PrintWriter(new BufferedWriter(new FileWriter("loginInfo.csv", true)));
          PrintWriter myWriter2 = new PrintWriter(new BufferedWriter(new FileWriter("balCAD.csv", true)));
          PrintWriter myWriter3 = new PrintWriter(new BufferedWriter(new FileWriter("balCrypto.csv", true)))) {

          myWriter.println("\n"+logIN);
          myWriter2.println("\n"+username+","+startingCAD);
          myWriter3.println("\n"+username+",0,0,0"); //Adding crypto balances in following order; BTC, ETH, SOL

          // Going to our buy/sell class
          Menu.initalizeBalances(currentUser);

        } catch(FileNotFoundException e){
          e.printStackTrace();
        } catch(IOException e){
          e.printStackTrace();
        }
      }

    } catch(FileNotFoundException e){
      e.printStackTrace();
      System.out.println(e);
    } catch(IOException e){
      e.printStackTrace();
      System.out.println(e);
    }

  }

    //Letting previous users login
    private static void login(){

      Scanner myObj = new Scanner(System.in);
      String line = "";
      String path = "loginInfo.csv";
      System.out.println("\nUsername: ");
      String usernameLogin = myObj.nextLine();

      try (BufferedReader br = new BufferedReader(new FileReader(path))) {
        ArrayList<String> usersLogins = new ArrayList<String>();
        ArrayList<String> passLogins = new ArrayList<String>();

        // Checking our loginInfo file to check which usernames are already taken
        while((line = br.readLine()) != null){
          String[] values = line.split(",");
          usersLogins.add(values[0]);
          passLogins.add(values[1]);
          }

        // Only letting user continue if the their username is in the file
        if (usersLogins.contains(usernameLogin)){
          for (int i = 0; i<usersLogins.size(); i++){
            //getting the index that the username is in
            if (usersLogins.get(i).equals(usernameLogin)){  
              System.out.println("Password: ");
              String passwordLogin = myObj.nextLine();
              //checking if the password in that index matches
              if(passLogins.get(i).equals(passwordLogin)){
                System.out.println("\nWelcome " + usernameLogin);
                currentUser = usernameLogin;
                // Going to our buy/sell class
                Menu.initalizeBalances(currentUser);
              }
              else{
                System.out.println("\nWrong password for username!\nPlease try again.");
                signInMenu();
              }
            } else{
              ;
            }
          }
        } else{
          System.out.println("\nThis usename does not exit.\nPlease try again.");
          signInMenu();
        }

      } catch(IOException e){
        e.printStackTrace();
      }
  }
}