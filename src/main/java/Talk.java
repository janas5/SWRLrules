import java.util.Set;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;

public class Talk {
	String rdf4jServer = "http://77.55.220.23:8080//rdf4j-server/";
	String repositoryID = "family-memory-rdfs";
	String getOntologyClassesQuery = "PREFIX bc: <http://a.com/ontology#> SELECT DISTINCT ?type WHERE {?subject a ?type.FILTER( STRSTARTS(STR(?type),str(bc:)) )}";
	String getAllClassesQuery = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> SELECT DISTINCT ?type WHERE {?s a ?type.}";
	Repository repo;
	
	public void LoadRepository() {			
		repo = new HTTPRepository(rdf4jServer, repositoryID);
		repo.initialize();
	}
	
	public String ExecuteQuery(String query) {
		String resultToReturn = "";
		try (RepositoryConnection conn = repo.getConnection()){
			TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL, query);
			try (TupleQueryResult result = tupleQuery.evaluate()) {
				while (result.hasNext()) {  // iterate over the result
					BindingSet bindingSet = result.next();
					Set<String>l = bindingSet.getBindingNames();
					for(String s : l) {
						resultToReturn+=(s+" = ");
						resultToReturn+=bindingSet.getValue((s));
						resultToReturn+="||";
					}
			      }	
			}
		}
		return resultToReturn;	
	}
	
	public String GetClasses() {
		return ExecuteQuery(getOntologyClassesQuery);
	}
	
	public String GetAllClasses() {
		return ExecuteQuery(getAllClassesQuery);
	}
	
}