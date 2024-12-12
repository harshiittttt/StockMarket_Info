import org.json.JSONObject;

import javax.swing.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class stockfetcher {
   public static void main(String[] args) {

       String API_KEY="PT8IIX1N0UQJP7QB";
       String SYMBOL= JOptionPane.showInputDialog(null,"ENTER SYMBOL OF STOCK YOU WANT TO FETCH PRICES FOR",JOptionPane.QUESTION_MESSAGE);;

       String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol="
               + SYMBOL + "&apikey=" + API_KEY;

       //now creating the http client
         HttpClient client = HttpClient.newHttpClient();


       HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

       // now sending the request using httpclient
       try{
           // storing the body of the request and creating a json object out of it and
           // then creating another json object of global Quote
           HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
           JSONObject object = new JSONObject(response.body());
           JSONObject globalQuote = object.getJSONObject("Global Quote");
           // gettting specific details
           String stockSymbol = globalQuote.getString("01. symbol");
           String price = globalQuote.getString("05. price");
           String changePercent = globalQuote.getString("10. change percent");

           // Display stock price
           String message = "Stock Symbol: " + stockSymbol.toUpperCase() + "\n"
                   + "Current Price: $" + price + "\n"
                   + "Change Percent: " + changePercent;
           JOptionPane.showMessageDialog(null, message,"STOCK CLOSING PRICE INFO",JOptionPane.INFORMATION_MESSAGE);
       } catch (Exception e) {
          JOptionPane.showInputDialog(null,"AN error occured"+e.getMessage(),JOptionPane.ERROR_MESSAGE);
           System.out.println(e);
       }

   }

}
