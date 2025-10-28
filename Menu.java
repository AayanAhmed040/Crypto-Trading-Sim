import java.util.Scanner;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Menu {

  private static String currentUserGlobal = "";
  private static double CADBal = 0.00;
  private static ArrayList<Double> cryptoBal = new ArrayList<Double>();

  public static void initalizeBalances(String currentUser){

    String line = "";
    currentUserGlobal = currentUser;

    //Getting CAD balance from balCAD.csv
    try (BufferedReader br = new BufferedReader(new FileReader("balCAD.csv"))){
      while((line = br.readLine()) != null){
        String[] values = line.split(",");
        if(values[0].equals(currentUser)){
          CADBal = Double.parseDouble(values[1]);
        }
      }
    } catch(IOException e){
      e.printStackTrace();
    }

    //Getting Crypto balances from balCrypto.csv
    try (BufferedReader br = new BufferedReader(new FileReader("balCrypto.csv"))){
      while((line = br.readLine()) != null){
        String[] values = line.split(",");
        if(values[0].equals(currentUser)){
          cryptoBal.add(Double.parseDouble(values[1])); //BTC
          cryptoBal.add(Double.parseDouble(values[2])); //ETH
          cryptoBal.add(Double.parseDouble(values[3])); //SOL
        }
      }
    } catch(IOException e){
      e.printStackTrace();
    }
    menu();

  }

  private static void menu() {

    Scanner myObj = new Scanner(System.in);
    System.out.println("\n---Current Balances---\nYou have $" + CADBal + " CAD");
    System.out.println("\n" + cryptoBal.get(0) + "₿ Bitcoin, " + cryptoBal.get(1) + "Ξ Ethereum, " + cryptoBal.get(2) + "◎ Solana");
    System.out.println("\nPlease pick an option:");
    System.out.println("[1]Buy\n[2]Sell\n[3]Quit");
    int choice = myObj.nextInt();

    // Asking the user for desired action and taking them to respective menu
    switch (choice) {
      case 1:
        buy();
        break;
      case 2:
        sell();
        break;
      case 3:
        quit();
        break;
      default:
        System.out.println("Invalid entry, please try again");
        menu();
        break;
    }
  }

  // Method that lets users simulate buying cryptocurrencies
  private static void buy(){

    // Giving user current market prices of cryptocurrencies
    System.out.println("\n---Here are the current market prices---");
    System.out.println("Price data by CoinGecko");
    double[] prices = Price.getPrice();
    double BTC = prices[0];
    double ETH = prices[1];
    double SOL = prices[2];

    System.out.println("BTC: $" + BTC + " ETH: $" + ETH + " SOL: $" + SOL );

    System.out.println("\nPlease pick an option:");
    System.out.println("[1]Buy BTC\n[2]Buy ETH\n[3]Buy SOL");
    Scanner myObj = new Scanner(System.in);
    int choice = myObj.nextInt();
    String buyingCrypto = "";

    switch(choice){
      case 1:
        buyingCrypto = "BTC";
        break;
      case 2:
        buyingCrypto = "ETH";
        break;
      case 3:
        buyingCrypto = "SOL";
        break;
      case 4:
        quit();
        break;
      default:
        System.out.println("Invalid entry, please try again");
        buy();
        break;
    }

    // Letting user buy crypto depending on how much CAD they want to spend
    System.out.print("\nPlease input how much CAD value of how much " + buyingCrypto +" you want to buy: \nPlease format like this: #.## (10.00) "); 
    double buyCADValue = myObj.nextDouble(); 

    // Checking if the user has enough CAD for transaction
    if (buyCADValue > CADBal) {
      System.out.println("\nYou do not have enough CAD to make this purchase. Please try again.");
      menu();
    }
    else {

      double buyAmmount = (buyCADValue / (buyingCrypto == "BTC" ? BTC : (buyingCrypto == "ETH" ? ETH : SOL)));
      System.out.println("\nYou just bought " + buyAmmount + " " + buyingCrypto + " for $" + buyCADValue + " CAD at the price of $" + buyCADValue / buyAmmount + " CAD");
      cryptoBal.set(0, cryptoBal.get(0) + (buyingCrypto == "BTC" ? buyAmmount : 0.0));
      cryptoBal.set(1, cryptoBal.get(1) + (buyingCrypto == "ETH" ? buyAmmount : 0.0));
      cryptoBal.set(2, cryptoBal.get(2) + (buyingCrypto == "SOL" ? buyAmmount : 0.0));
      CADBal -= buyCADValue; 

      System.out.println("\nYou have $" + CADBal + " CAD\n " + cryptoBal.get(0) + "₿ Bitcoin, " + cryptoBal.get(1) + "Ξ Ethereum, " + cryptoBal.get(2) + "◎ Solana");
      menu();
    }
  }


  private static void sell() {

    System.out.println("---Here are the current market prices---");
    System.out.println("Prices data by CoinGecko");
    double[] prices = Price.getPrice();
    double BTC = prices[0];
    double ETH = prices[1];
    double SOL = prices[2];

    System.out.println("BTC: $" + BTC + " ETH: $" + ETH + " SOL: $" + SOL);

    System.out.println("\nPlease pick an option:");
    System.out.println("[1]Sell BTC\n[2]Sell ETH\n[3]Sell SOL");
    Scanner myObj = new Scanner(System.in);
    int choice = myObj.nextInt();
    String sellingCrypto = "";

    switch (choice) {
      case 1:
        sellingCrypto = "BTC";
        break;
      case 2:
        sellingCrypto = "ETH";
        break;
      case 3:
        sellingCrypto = "SOL";
        break;
      case 4:
        quit();
        break;
      default:
        System.out.println("Invalid entry, please try again");
        sell();
        break;
    }
    // Let user sell based on amount of crypto
    System.out.print("\nPlease input how much " + sellingCrypto + " you want to sell: \nPlease format like this: #.## (10.00) ");
    double sellCryptoAmount = myObj.nextDouble();

    // Checking if user has enough crypto to sell
    if (sellCryptoAmount > (sellingCrypto == "BTC" ? cryptoBal.get(0) : (sellingCrypto == "ETH" ? cryptoBal.get(1) : cryptoBal.get(2)))) {
      System.out.println("\nYou do not have enough " + sellingCrypto + " to make this sale. Please try again.");
      menu();
    }
    else {
      double receiveAmount = (sellCryptoAmount * (sellingCrypto == "BTC" ? BTC : (sellingCrypto == "ETH" ? ETH : SOL)));
      System.out.println("\nYou just sold " + sellCryptoAmount + " " + sellingCrypto + " for $" + receiveAmount + " CAD at the price of $" + receiveAmount / sellCryptoAmount + " CAD");

      cryptoBal.set(0, cryptoBal.get(0) - (sellingCrypto == "BTC" ? sellCryptoAmount : 0.0));
      cryptoBal.set(1, cryptoBal.get(1) - (sellingCrypto == "ETH" ? sellCryptoAmount : 0.0));
      cryptoBal.set(2, cryptoBal.get(2) - (sellingCrypto == "SOL" ? sellCryptoAmount : 0.0));
      CADBal += receiveAmount;
      menu();
    }
  }

  private static void quit(){
    System.out.println("Exiting simulator... \n\nSee you again soon!");

    List<String> cadLines = new ArrayList<String>();
    String line = "";

    // Saving CAD balance after leaving sim
    try (BufferedReader br = new BufferedReader(new FileReader("balCAD.csv"))){
      while((line = br.readLine()) != null){
          String[] values = line.split(",");
          if (values[0].equals(currentUserGlobal)) {
            line = currentUserGlobal + "," + CADBal;
          }
          cadLines.add(line);
      } 
    } catch(IOException e){
      e.printStackTrace();
    }
    try (PrintWriter myWriter = new PrintWriter(new BufferedWriter(new FileWriter("balCAD.csv", false)))){
      for(String cadLine : cadLines){
        myWriter.write(cadLine + "\n");
      }
    } catch(IOException e){
      e.printStackTrace();
    }

    // Saving crypto balance after leaving sim
    List<String> cryptoLines = new ArrayList<String>();
    
    try (BufferedReader br2 = new BufferedReader(new FileReader("balCrypto.csv"))){
      while((line = br2.readLine()) != null){
          String[] values = line.split(",");
          if (values[0].equals(currentUserGlobal)) {
            line = currentUserGlobal + "," + cryptoBal.get(0) + "," + cryptoBal.get(1) + "," + cryptoBal.get(2);
          }
          cryptoLines.add(line);
      } 
    } catch(IOException e){
      e.printStackTrace();
    }

    try (PrintWriter myWriter = new PrintWriter(new BufferedWriter(new FileWriter("balCrypto.csv", false)))){
      for(String l : cryptoLines){
        myWriter.write(l + "\n");
      }
    } catch(IOException e){
      e.printStackTrace();
    }
}
}