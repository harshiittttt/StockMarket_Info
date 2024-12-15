import org.json.JSONObject;

import javax.swing.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class comparingbtw2exchanges {


    public static double getprice(String symbol){
        // yahoo finance api url
        String apiKey ="PT8IIX1N0UQJP7QB";
        String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + apiKey;

        double price = 0;
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
            JSONObject obj = new JSONObject(response.body());
            JSONObject quote = obj.getJSONObject("Global Quote");
            price = quote.getDouble("05. price");
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(null,e.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
        }
        return price;
    }

    public static void compareprices(String ticker){
        String nasdaqSymbol = "NASDAQ:" + ticker;
        String nyseSymbol = "NYSE:" + ticker;

        double nasdaqprice = getprice(nasdaqSymbol);
        double nyseprice = getprice(nyseSymbol);
        double pricedifference = nasdaqprice - nyseprice;
        System.out.println("Price on NASDAQ: " + nasdaqprice);
        System.out.println("Price on NYSE: " + nyseprice);
        JOptionPane.showMessageDialog(null,"price difference is: "+pricedifference,"PRICES",JOptionPane.INFORMATION_MESSAGE);
    }



    public static void main(String[] args) {
       String ticker = JOptionPane.showInputDialog(null,"Please enter a ticker symbol",JOptionPane.QUESTION_MESSAGE);
       compareprices(ticker);
    }
}
