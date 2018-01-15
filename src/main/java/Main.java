import static spark.Spark.*;

public class Main {
	private static Talk talk;
    public static void main(String[] args) {
    	talk = new Talk();
    	talk.LoadRepository();
    	
        get("/sparql/:query", (req, res) ->{
        	String query = req.params(":query");
		    System.out.println(query);
        	return talk.ExecuteQuery(query);
        });
    }
}