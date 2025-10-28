import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Price {

    // Public API URL and user agent for getting market prices of cryptocurrencies
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String GET_URL = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin,ethereum,solana&vs_currencies=cad";

    public static double[] getPrice() {
      try{
        URL obj = new URL(GET_URL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) obj.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = httpURLConnection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) { // Successful response
            try (BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))) {
                String inputLine;
                StringBuffer response = new StringBuffer();

                // Getting full JSON response from API
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                // Store each cryptocurrency price in a variable
                String jsonResponse = response.toString();
                double bitcoinPrice = 0.0;
                double ethereumPrice = 0.0;
                double solanaPrice = 0.0;
                
                // Parsing JSON response manually to extract prices

                // Extract Bitcoin price
                String bitcoinSearchString = "\"bitcoin\":{\"cad\":";
                int bitcoinIndex = jsonResponse.indexOf(bitcoinSearchString);
                if (bitcoinIndex != -1) {
                    int startIndex = bitcoinIndex + bitcoinSearchString.length();
                    int endIndex = jsonResponse.indexOf("}", startIndex);
                    bitcoinPrice = Double.parseDouble(jsonResponse.substring(startIndex, endIndex));
                } else {
                    System.out.println("Bitcoin price not found in the response.");
                }
                
                // Extract Ethereum price
                String ethereumSearchString = "\"ethereum\":{\"cad\":";
                int ethereumIndex = jsonResponse.indexOf(ethereumSearchString);
                if (ethereumIndex != -1) {
                    int startIndex = ethereumIndex + ethereumSearchString.length();
                    int endIndex = jsonResponse.indexOf("}", startIndex);
                    ethereumPrice = Double.parseDouble(jsonResponse.substring(startIndex, endIndex));
                } else {
                    System.out.println("Ethereum price not found in the response.");
                }
                
                // Extract Solana price
                String solanaSearchString = "\"solana\":{\"cad\":";
                int solanaIndex = jsonResponse.indexOf(solanaSearchString);
                if (solanaIndex != -1) {
                    int startIndex = solanaIndex + solanaSearchString.length();
                    int endIndex = jsonResponse.indexOf("}", startIndex);
                    solanaPrice = Double.parseDouble(jsonResponse.substring(startIndex, endIndex));
                } else {
                    System.out.println("Solana price not found in the response.");
                }
                return new double[] {bitcoinPrice, ethereumPrice, solanaPrice};
            }
        } else {
            System.out.println("GET request not worked");
        }

    } catch(IOException e){
      e.printStackTrace();
    }
    // Returning preset defualt values incase of API call failure
    return new double[] {114280.21, 4122.12, 202.65};
  }
}