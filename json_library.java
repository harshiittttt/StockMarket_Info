import org.json.JSONObject;
 // we use JSONObject library for handling the json data more effectively
public class json_library {

    public static void main(String[] args) {
        // example string that we will get from api
        String json_string = "{\"name\":\"John\", \"age\":30, \"city\":\"New York\"}";
        // creating a obj instance of JSONObject library for accessing the items using
        // inbuilt library codes
        JSONObject obj = new JSONObject(json_string);
        // getting the json data and storing it
        String name = obj.getString("name");
        int age = obj.getInt("age");
        String city = obj.getString("city");
        // printing the data that we stored

        System.out.println(name
        + " "+ age+" "+city);
    }
}
