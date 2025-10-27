import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Price {

    private static final String USER_AGENT = "Mozilla/5.0";

    private static final String GET_URL = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin,ethereum,solana&vs_currencies=cad";

    public static double[] getPrice() {
      try{
        URL obj = new URL(GET_URL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) obj.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = httpURLConnection.getResponseCode();
        // System.out.println("GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in .readLine()) != null) {
                response.append(inputLine);
            } in .close();

            // Store each cryptocurrency price in a variable
            String jsonResponse = response.toString();
            double bitcoinPrice = 0.0;
            double ethereumPrice = 0.0;
            double solanaPrice = 0.0;
            
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

        } else {
            System.out.println("GET request not worked");
        }

        // for (int i = 1; i <= 8; i++) {
        //     System.out.println(httpURLConnection.getHeaderFieldKey(i) + " = " + httpURLConnection.getHeaderField(i));
        // }
    } catch(IOException e){
      e.printStackTrace();
    }
    return new double[] {0.0, 0.0, 0.0};
  }
}

//https://www.javatpoint.com/how-to-get-value-from-json-object-in-java-example#:~:text=getJsonObject()%20Method&text=It%20is%20used%20to%20get%20the%20(JsonObject)get(name,no%20mapping%20for%20the%20parameter.