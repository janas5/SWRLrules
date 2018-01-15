import static spark.Spark.*;

public class Main {
	private static Talk talk;
    public static void main(String[] args) {
    	talk = new Talk();
    	talk.LoadRepository();
    	
        get("/sparql/:query", (req, res) ->{
        	String query = req.params(":query");
		    String queryString = "PREFIX foaf:  <http://a.com/ontology/> SELECT DISTINCT ?property"+

		    		" WHERE { "+

		    	 " ?s ?property ?o ."+

		    	"}";
        	return talk.ExecuteQuery(queryString);
        });
    }
}