import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class cryptoarbitrage {

    // Function to get price from Binance API
    public static double getBinancePrice(String symbol) {
        String url = "https://api.binance.com/api/v3/ticker/price?symbol=" + symbol;

        double price = 0;
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject obj = new JSONObject(response.body());
            price = obj.getDouble("price");
        } catch (Exception e) {
            System.out.println("Error fetching Binance data: " + e.getMessage());
        }
        return price;
    }

    // Function to get price from Coinbase API
    public static double getCoinbasePrice(String symbol) {
        String url = "https://api.coinbase.com/v2/prices/" + symbol + "-USD/spot";

        double price = 0;
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject obj = new JSONObject(response.body());
            price = obj.getJSONObject("data").getDouble("amount");
        } catch (Exception e) {
            System.out.println("Error fetching Coinbase data: " + e.getMessage());
        }
        return price;
    }

    // Function to calculate the profit after fees
    public static double calculateProfit(double capital, double binancePrice, double coinbasePrice, boolean isBuyOnBinance) {
        double tradeAmount = capital;

        // Assume a simple 0.1% trading fee for Binance and 1.49% trading fee for Coinbase
        double binanceTradeFee = 0.001; // 0.1% fee for Binance
        double coinbaseTradeFee = 0.0149; // 1.49% fee for Coinbase

        // Withdrawal fees (example for BTC withdrawal fee)
        double binanceWithdrawalFee = 0.0005; // BTC withdrawal fee for Binance
        double coinbaseWithdrawalFee = 0.0005; // BTC withdrawal fee for Coinbase

        double tradedBTC;

        // Calculate how much Bitcoin you will buy or sell based on the price
        if (isBuyOnBinance) {
            tradedBTC = tradeAmount / binancePrice;
            tradedBTC -= binanceWithdrawalFee; // subtract withdrawal fee from the amount of BTC
            // Sell on Coinbase
            double revenue = tradedBTC * coinbasePrice; // amount received in USD after selling on Coinbase
            revenue -= revenue * coinbaseTradeFee; // subtract Coinbase trading fee
            revenue -= coinbaseWithdrawalFee * coinbasePrice; // subtract Coinbase withdrawal fee
            return revenue - capital; // Profit (revenue - initial capital)
        } else {
            tradedBTC = tradeAmount / coinbasePrice;
            tradedBTC -= coinbaseWithdrawalFee; // subtract withdrawal fee from the amount of BTC
            // Sell on Binance
            double revenue = tradedBTC * binancePrice; // amount received in USD after selling on Binance
            revenue -= revenue * binanceTradeFee; // subtract Binance trading fee
            revenue -= binanceWithdrawalFee * binancePrice; // subtract Binance withdrawal fee
            return revenue - capital; // Profit (revenue - initial capital)
        }
    }

    public static void main(String[] args) {
        String symbol = "BTCUSDT"; // Bitcoin to USD pair

        double binancePrice = getBinancePrice(symbol);
        double coinbasePrice = getCoinbasePrice("BTC");

        System.out.println("Price of BTC on Binance: " + binancePrice);
        System.out.println("Price of BTC on Coinbase: " + coinbasePrice);

        // Compare prices and print the result
        double priceDifference = binancePrice - coinbasePrice;
        System.out.println("Price Difference (Binance - Coinbase): " + priceDifference);

        double capital = 117.89; // $10,000 capital

        // Arbitrage decision: Buy on one exchange and sell on the other
        double profit;
        if (priceDifference > 0) {
            System.out.println("Arbitrage opportunity: Buy on Coinbase, Sell on Binance");
            profit = calculateProfit(capital, binancePrice, coinbasePrice, false); // Buy on Coinbase, sell on Binance
        } else if (priceDifference < 0) {
            System.out.println("Arbitrage opportunity: Buy on Binance, Sell on Coinbase");
            profit = calculateProfit(capital, binancePrice, coinbasePrice, true); // Buy on Binance, sell on Coinbase
        } else {
            System.out.println("No arbitrage opportunity.");
            profit = 0;
        }

        // Display the calculated profit
        System.out.println("Profit from the arbitrage opportunity: $" + profit);
    }
}
