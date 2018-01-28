import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.rio.datatypes.GeoSPARQLDatatypeHandler;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.swrlapi.core.SWRLAPIRule;
import org.swrlapi.core.SWRLRuleEngine;
import org.swrlapi.factory.SWRLAPIFactory;
import org.swrlapi.ui.model.FileBackedSWRLRuleEngineModel;

public class Talk {
	String rdf4jServer = "http://77.55.220.23:8080//rdf4j-server/";
	String repositoryID = "family-memory-rdf";
	String getOntologyClassesQuery = "PREFIX bc: <http://a.com/ontology#> SELECT DISTINCT ?type WHERE { ?type a owl:Class FILTER ( STRSTARTS(STR(?type),str(bc:)))}";
	String getAllClassesQuery = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> SELECT DISTINCT ?type WHERE {?s a ?type.}";
	String getOwlObjectProperties = GetPrefixes() + "SELECT DISTINCT ?p WHERE {?p rdf:type owl:ObjectProperty.}";
	String getOwlDatatypeProperties = GetPrefixes() + "SELECT DISTINCT ?p WHERE {?p rdf:type owl:DatatypeProperty.}";
	String getRules = GetPrefixes()+ "SELECT DISTINCT *\r\n" + 
			"{\r\n" + 
			"?atomListExt ?Header ?atomListInt.\r\n" + 
			"?atomListExt swrl:head ?atomListInt.\r\n" + 
			"?atomListInt rdf:first ?swrlSub.\r\n" + 
			"?swrlSub ?swrlPred ?swrlObj.\r\n}";
	
	String getRules2 = GetPrefixes()+ "SELECT DISTINCT *\r\n" + 
			"{\r\n" + 
			"?atomListExt ?Header ?atomListInt.\r\n" + 
			"?atomListExt swrl:body ?atomListInt.\r\n" + 
			"?atomListInt rdf:first ?swrlSub.\r\n" + 
			"?swrlSub ?swrlPred ?swrlObj.\r\n}";
	
	String getRules3 = GetPrefixes()+ "SELECT DISTINCT *\r\n" + 
			"{\r\n" + 
			"?atomListExt ?Header ?atomListInt.\r\n" + 
			"?atomListExt swrl:body ?atomListInt.\r\n" + 
			"?atomListInt rdf:rest ?swrlSub.\r\n" + 
			"?swrlSub rdf:first ?swrlObj.\r\n" + 
			"?swrlObj ?a ?b.\r\n}";
	
	String getRulesWith = GetPrefixes()+ "select ?p ?d where {\r\n" + 
			"  ?p rdfs:domain|rdfs:range ?d\r\n" + 
			"  filter isIri(?d)\r\n" + 
			"}";
	
	Repository repo;
	
	static File fileRepo;
	
	public void LoadRepository() {			
		repo = new HTTPRepository(rdf4jServer, repositoryID);
		repo.initialize();
		
		fileRepo = repo.getDataDir();
	}
	
	public String ExecuteQuery(String query, String filter) {
		String resultToReturn = "";
		try (RepositoryConnection conn = repo.getConnection()){
			
			TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL, query);
			try (TupleQueryResult result = tupleQuery.evaluate()) {
				while (result.hasNext()) {  // iterate over the result
					BindingSet bindingSet = result.next();
					Set<String>l = bindingSet.getBindingNames();
					for(String s : l) {
						if(!filter.equals("")) {
							if(s.equals("atomListExt") && !bindingSet.getValue((s)).toString().contains(filter.toString()))
								break;
						}
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
		return ExecuteQuery(getOntologyClassesQuery,"");
	}
	
	public String GetAllClasses() {
		return ExecuteQuery(getAllClassesQuery,"");
	}

	public String GetObjectProperties() {
		return ExecuteQuery(getOwlObjectProperties,"");
	}

	public String GetDatatypeProperties() {
		return ExecuteQuery(getOwlDatatypeProperties,"");
	}

	public String GetRules() {		
		Scanner scanner = new Scanner(ExecuteQuery(getRules,""));
		String result = "";
		List<String>list = new ArrayList<String>();
		while (scanner.hasNextLine()) {
			String s = scanner.nextLine();
			if(s.contains("http://a.com/ontology#Def")) {
				if(!list.contains(s)) {
					String[] temp = s.split("#Def-");
					result+=temp[1]+"\n";
					list.add(s);
				}
			}
		}
		scanner.close();		
		return result;
	}
	
	public String GetRuleDefinition(String ruleName) {
		String a,b,c, result="";
		a=ExecuteQuery(getRules,ruleName);
		b=ExecuteQuery(getRules2,ruleName);
		c=ExecuteQuery(getRules3,ruleName);
		a = a+"\n"+b+"\n"+c;
				
		Scanner scanner = new Scanner(a);
		while (scanner.hasNextLine()) {
			String s = scanner.nextLine();
			System.out.println(s);
			if(s.startsWith("swrlObj = http://a.com/ontology")||s.startsWith("b = http://a.com/ontology")) {
				String[] temp = s.split("#");
				try {
				result+=temp[1]+"\n";
				}
				catch(Exception e) {
					result+=s+"\n";
				}
			}			
		}	
		scanner.close();
		System.out.println(result);
		return result;
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

	public Object GetRulesWith(String className) {
		String rules[] = GetRules().split("\\r?\\n");
		String a=ExecuteQuery(getRulesWith,"");
		String result="";
		String temp="";
		Scanner scanner = new Scanner(a);
		
		while (scanner.hasNextLine()) {
			String s = scanner.nextLine();
			if(s.equals("d = http://a.com/ontology#"+className)) {
				String[] t = temp.split("#");
				try {
				for(String r:rules) {
					if(r.equals(t[1]))
						result+=t[1]+"\n";
				}				
				}
				catch(Exception e) {
					result+=temp+"\n";
				}
			}
			else {
				temp=s;
			}			
		}	
		scanner.close();
		System.out.println(result);
		return result;
	}			
	
}