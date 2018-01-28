import static spark.Spark.*;

public class Main {
    private static Talk talk;
 // Enables CORS on requests. This method is an initialization method and should be called once.
    private static void enableCORS(final String origin, final String methods, final String headers) {

        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
            // Note: this may or may not be necessary in your particular application
            response.type("application/json");
        });
    }
    
    public static void main(String[] args) {
    	//enableCORS("*","*","*");
        talk = new Talk();
        talk.LoadRepository();

        get("/sparql/:query", (req, res) ->{
            String query = req.params(":query");
            //System.out.println(query);
            return talk.ExecuteQuery(query,"");
        });

        get("/classes", (req, res) -> {
            return talk.GetClasses();
        });

        get("/classes-all", (req, res) -> {
            return talk.GetAllClasses();
        });

        get("/obj-prop", (req, res) -> {
            return talk.GetObjectProperties();
        });

        get("/datatype-prop", (req, res) -> {
            return talk.GetDatatypeProperties();
        });
        

        get("/rules", (req, res) -> {
            return talk.GetRules();
        });

        get("/rules/:ruleName", (req, res) -> {
        	String ruleName = req.params(":ruleName");
        	return talk.GetRuleDefinition(ruleName);
        });
        
        get("/rules/with/:className", (req, res) -> {
        	String className = req.params(":className");
        	return talk.GetRulesWith(className);
        });
        
    }
}