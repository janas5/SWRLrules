import java.util.Set;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;

public class Talk {
	String rdf4jServer = "http://localhost:8080/rdf4j-server/";
	String repositoryID = "family-memory-rdfs";
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
						resultToReturn+="s = ";
						resultToReturn+=bindingSet.getValue((s));
						resultToReturn+=System.lineSeparator();
					}
			      }	
			}
		}
		return resultToReturn;
	
	}
}