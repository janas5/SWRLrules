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
	String getOntologyClassesQuery = "PREFIX bc: <http://a.com/ontology#> SELECT DISTINCT ?type WHERE { ?type a owl:Class FILTER ( STRSTARTS(STR(?type),str(bc:)))}";
	String getAllClassesQuery = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> SELECT DISTINCT ?type WHERE {?s a ?type.}";
	String getOwlObjectProperties = GetPrefixes() + "SELECT DISTINCT ?p WHERE {?p rdf:type owl:ObjectProperty.}";
	String getOwlDatatypeProperties = GetPrefixes() + "SELECT DISTINCT ?p WHERE {?p rdf:type owl:DatatypeProperty.}";
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
						resultToReturn+="\n";
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

	public String GetObjectProperties() {
		return ExecuteQuery(getOwlObjectProperties);
	}

	public String GetDatatypeProperties() {
		return ExecuteQuery(getOwlDatatypeProperties);
	}

	public String GetPrefixes()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ");
		sb.append("PREFIX owl: <http://www.w3.org/2002/07/owl#> ");
		sb.append("PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> ");
		sb.append("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> ");
		sb.append("PREFIX bc: <http://a.com/ontology#> ");
		sb.append("PREFIX swrl: <http://www.w3.org/2003/11/swrl#> ");

		return sb.toString();
	}
	
}