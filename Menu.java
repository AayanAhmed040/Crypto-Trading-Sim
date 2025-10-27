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

  public static String currentUserGlobal = "";
  public static double CADBal = 0.00;
  public static ArrayList<Double> cryptoBal = new ArrayList<Double>();

  public static void initalizeBalances(String currentUser){

    String line = "";
    currentUserGlobal = currentUser;
    //Getting CAD balance from balCAD.csv
    try{
      BufferedReader br = new BufferedReader(new FileReader("balCAD.csv"));
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
    try{
      BufferedReader br = new BufferedReader(new FileReader("balCrypto.csv"));
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

    // Main login = new Main();
    // currentUser = login.currentUser;

    Scanner myObj = new Scanner(System.in);
    System.out.println("\nYou have $" + CADBal + " CAD\n " + cryptoBal.get(0) + "₿ Bitcoin, " + cryptoBal.get(1)
        + "Ξ Ethereum, " + cryptoBal.get(2) + "◎ Solana");
    System.out.println("\nPlease pick an option:");
    System.out.println("[1]Buy\n[2]Sell\n[3]Quit");
    int choice = myObj.nextInt();

    switch (choice) {
      case 1:
        buy();
        break;
      case 2:
        sell();
        break;
      case 3:
        quit();
    }
  }

  // Our method that lets users buy
  private static void buy(){

    // Telling the user current prices
    System.out.println("Here are the current market prices");
    Price priceClass = new Price();
    // do to api limitations, need to edit so it only calls api once per menu load and stores values

    double[] prices = priceClass.getPrice();
    double BTC = prices[0];
    double ETH = prices[1];
    double SOL = prices[2];

    System.out.println("BTC: $" + BTC + " ETH: $" + ETH + " SOL: $" + SOL );

    System.out.println("\nPlease pick an option:");
    System.out.println("[1]Buy BTC\n[2]Buy ETH\n[3]Buy SOL");
    Scanner myObj = new Scanner(System.in);
    int choice = myObj.nextInt();
    String buyingCrypto = "";

    int cryptoId = 0;
    switch(choice){
      case 1:
        buyingCrypto = "BTC";
        cryptoId = 1;
        break;
      case 2:
        buyingCrypto = "ETH";
        cryptoId = 2;
        break;
      case 3:
        buyingCrypto = "SOL";
        cryptoId = 3;
        break;
      case 4:
        quit();
    }

    System.out.print("\nPlease input how much CAD value of how much " + buyingCrypto +" you want to buy: \nPlease format like this: #.## (10.00) "); 
    double buyCADValue = myObj.nextDouble(); 

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
      
      System.out.println("\nYou have $" + CADBal + " CAD\n " + cryptoBal.get(0) + "₿ Bitcoin, " + cryptoBal.get(1)
          + "Ξ Ethereum, " + cryptoBal.get(2) + "◎ Solana");
      System.out.println("\nPlease pick an option:");
      System.out.println("[1]Buy\n[2]Sell\n[3]Quit");
      menu();
    }
  }


  private static void sell() {

    System.out.println("Here are the current market prices");
    Price priceClass = new Price();
    double[] prices = priceClass.getPrice();
    double BTC = prices[0];
    double ETH = prices[1];
    double SOL = prices[2];

    System.out.println("BTC: $" + BTC + " ETH: $" + ETH + " SOL: $" + SOL);

    System.out.println("\nPlease pick an option:");
    System.out.println("[1]Sell BTC\n[2]Sell ETH\n[3]Sell SOL");
    Scanner myObj = new Scanner(System.in);
    int choice = myObj.nextInt();
    String sellingCrypto = "";

    int cryptoId = 0;

    switch (choice) {
      case 1:
        sellingCrypto = "BTC";
        cryptoId = 1;
        break;
      case 2:
        sellingCrypto = "ETH";
        cryptoId = 2;
        break;
      case 3:
        sellingCrypto = "SOL";
        cryptoId = 3;
        break;
      case 4:
        quit();
    }
    // fix from selling CAD to selling crypto ammount
    System.out.print("\nPlease input how much " + sellingCrypto + " you want to sell: \nPlease format like this: #.## (10.00) ");
    double sellCryptoAmount = myObj.nextDouble();
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
    try{
      List<String> cadLines = new ArrayList<String>();
      BufferedReader br = new BufferedReader(new FileReader("balCAD.csv"));
      String line = "";
      while((line = br.readLine()) != null){
          String[] values = line.split(",");
          if (values[0].equals(currentUserGlobal)) {
            line = currentUserGlobal + "," + CADBal;
          }
          cadLines.add(line);
      } try{
        PrintWriter myWriter = new PrintWriter(new BufferedWriter(new FileWriter("balCAD.csv", false)));
        for(String cadLine : cadLines){
          myWriter.write(cadLine + "\n");
        }
        myWriter.close();
      } catch(IOException e){
        e.printStackTrace();
      }
    }
    catch(IOException e){
      e.printStackTrace();
  }
  try{
      List<String> cryptoLines = new ArrayList<String>();
      BufferedReader br2 = new BufferedReader(new FileReader("balCrypto.csv"));
      String line = "";
      while((line = br2.readLine()) != null){
          String[] values = line.split(",");
          if (values[0].equals(currentUserGlobal)) {
            line = currentUserGlobal + "," + cryptoBal.get(0) + "," + cryptoBal.get(1) + "," + cryptoBal.get(2);
          }
          cryptoLines.add(line);
      } try{
        PrintWriter myWriter = new PrintWriter(new BufferedWriter(new FileWriter("balCrypto.csv", false)));
        for(String l : cryptoLines){
          myWriter.write(l + "\n");
        }
        myWriter.close();
      } catch(IOException e){
        e.printStackTrace();
      }
    }
    catch(IOException e){
      e.printStackTrace();
  }
}
}
// https://www.geeksforgeeks.org/convert-string-to-double-in-java/