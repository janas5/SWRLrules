import static spark.Spark.*;

public class Main {
    private static Talk talk;
    public static void main(String[] args) {
        talk = new Talk();
        talk.LoadRepository();
        talk.SwrlRepresentant();

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
        
        get("/swrl/rules", (req, res) -> {
            return talk.printSwrlRules();
        });
        
        get("/swrl/rules/with/:element", (req, res) -> {
        	String className = req.params(":element");
        	return talk.GetSwrlRulesWith(className);
        });
        
    }
}