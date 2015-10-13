//Author: Kris Rutherford
//Date: 11/18/2014
//Title: SPARQL JENA Assignment 6
import java.io.FileInputStream;
import java.io.IOException;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;


public class jena_hw_6 {

		//model that will hold four datasets from DBPedia, LinkedMovie, New York Times, and the Owl:sameAs ontology
		static Model union_model= ModelFactory.createDefaultModel();
	
		//takes the file location for a rdf ttl file and adds it's triples to the current model
		public static void add_union_model (String str) throws IOException{
	
		Model mdl = ModelFactory.createDefaultModel().read(new FileInputStream(str),null,"TTL");
		union_model.add(mdl);
		mdl.close();
		}
	
		//parses and prints query based on inference model
		public static void sparql_query(String Qry, Model data) throws IOException{
	
		//Jena SPARQL Query object to be used throughout each query
		Query query = QueryFactory.create(Qry);
		//Query execution object to be used throughout each query
		QueryExecution qe =QueryExecutionFactory.create(query, data);
		ResultSet results;
		
		//Jena SPARQL Result set object to be used throughout each query
		results= qe.execSelect();
		
		//outputs Query and then results to console
		ResultSetFormatter.out(System.out, results, query);
		qe.close();
		
	}
	
	public static void main(String[] args) throws IOException {
		
		//Pass file locations for various data sources about Samuel L Jackson
		//and the owl:sameAs ontology to relate to be added to the union_model graph
		add_union_model("C://hw_6//DBPedia_Samuel_L_Jackson.ttl");
		add_union_model("C://hw_6//Linked_Movie_Database_Samuel_L_jackson.ttl");
		add_union_model("C://hw_6//newyork_times_Samuel_L_Jackson.ttl");
		add_union_model("C://hw_6//owl_data_bindings.ttl");
		
		//Creates owl inference model based off of the union_model
		OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RULE_INF,union_model);
		
		//Query tests to see that all subjects representing Samuel L Jackson
		// are related to the ns1:spouse based on the inferences.
		//<http://data.nytimes.com/jackson_samuel_l_per> and
		//<http://data.linkedmdb.org/resource/actor/29873> should be inferred from
		//the DBPedia predicate
		String q_string=
		"prefix ns1: <http://dbpedia.org/ontology/> " +
		"SELECT DISTINCT ?actor_subject WHERE {?actor_subject ns1:spouse ?c .} " + 
		"ORDER BY ?actor_subject";
		//Passes query string and model
		sparql_query(q_string, ontModel);
		
		
		//Query should retrieve all movies that Samuel L Jackson acted in
		//from the linkedmdb and his birth name from the DBPedia dataset
		//using a filter applied to the query using the new york times 
		//representation of Samuel L Jackson
		q_string=
		"prefix ns0: <http://data.linkedmdb.org/resource/movie/> "+
		"prefix ns1: <http://dbpedia.org/property/> "+
		"prefix ns2: <http://data.nytimes.com/> "+
		"SELECT DISTINCT ?movie ?name "+
		"WHERE { "+
		"?movie ns0:actor ?actor . "+
		"?actor ns1:birthName ?name . "+
		"FILTER (?actor = ns2:82283153807785222583) . }";
		//Passes string 
		sparql_query(q_string, ontModel);
		ontModel.close();
				
	}
	
}
