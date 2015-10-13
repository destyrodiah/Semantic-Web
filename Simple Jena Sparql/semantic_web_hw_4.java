//Author: Kris Rutherford
//Date: 11/05/2014
//Title: SPARQL JENA Assignment 4

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;


public class semantic_web_hw_4 {

	//Method that parses query and prints results using Jena
	//Has a toggle feature to change up logic for ASK query
	public static void sparql_query(String Q_Num, String Qry, Model data, boolean toggle) throws IOException{
	
		//Jena SPARQL Query object to be used throughout each query
		Query query = QueryFactory.create(Qry);
		//Query execution object to be used throughout each query
		QueryExecution qe =QueryExecutionFactory.create(query, data);
		ResultSet results;
		if(toggle){
		//Jena SPARQL Result set object to be used throughout each query
		results= qe.execSelect();
		
		//outputs Query header, query, and then results to console
		System.out.println(Q_Num+"\r\n");
		ResultSetFormatter.out(System.out, results, query);
		qe.close();
		}
		else{
		boolean b= qe.execAsk();
		System.out.println(Q_Num+"\r\n");
		ResultSetFormatter.out(System.out, b);
		System.out.println("\r\n");
		qe.close();
		}
		
	}

	public static void main(String[] args) throws IOException {
		
		//Load and build default model based off pokemon rdf data in turtle format
		InputStream in = new FileInputStream(new File("C://hw4//poke.ttl"));
		Model data = ModelFactory.createDefaultModel().read(in,null,"TTL");
		
		//Close input stream
		in.close();
		
		//will have each query string assigned to this variable
		String q_string;		
		
		//********QUERY 1
		//Learn structure of predicates
		q_string=
		"prefix pkmn: <http://example.org/stuff/1.0/pkmn#> " +
		"prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
		"SELECT DISTINCT ?predicate WHERE {?a ?predicate ?c .} ";
		
		sparql_query("************QUERY 1 Results", q_string, data, true);
		
		//********QUERY 2
		//Find pokemon growth rates
		q_string=
		"prefix pkmn: <http://example.org/stuff/1.0/pkmn#> " +
		"prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"SELECT DISTINCT ?rates WHERE {?a pkmn:growth_rate ?rates .} ";
		
		sparql_query("************QUERY 2 Results", q_string, data, true);
		
		//********QUERY 3
		//Find out if there are pokemon with a fast growth rates
		q_string=
		"prefix pkmn: <http://example.org/stuff/1.0/pkmn#> "+
		"prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
		"ASK WHERE {?a pkmn:growth_rate pkmn:fast .} ";
		
		sparql_query("************QUERY 3 Results", q_string, data, false);
		
		//********QUERY 4
		//Since it is now know that there are fast growth rate pokemon
		//the count of them can be acquired
		q_string=
		"prefix pkmn: <http://example.org/stuff/1.0/pkmn#> "+
		"prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
		"SELECT (count(?pokemon) AS ?num_of_fast_pkmn) WHERE{?pokemon pkmn:growth_rate pkmn:fast .} ";
		
		sparql_query("************QUERY 4 Results", q_string, data, true);
		
		//********QUERY 5
		//Lists all immediately relatable information about
		//fast growth rate pokemon
		q_string=
		"prefix pkmn: <http://example.org/stuff/1.0/pkmn#> "+
		"prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
		"SELECT ?pokemon ?predicate ?object WHERE { "+
		"?pokemon pkmn:growth_rate pkmn:fast . "+
		"?pokemon ?predicate ?object . }";
		
		sparql_query("************QUERY 5 Results", q_string, data, true);
		
		//********QUERY 6
		//The same query, but with information sorted in a standardized format
		q_string=
		"prefix pkmn: <http://example.org/stuff/1.0/pkmn#> "+
		"prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
		"SELECT ?pokemon ?predicate ?object WHERE { "+
		"?pokemon pkmn:growth_rate pkmn:fast . "+
		"?pokemon ?predicate ?object . "+
		"}ORDER BY ?pokemon ?predicate ?object ";
		
		sparql_query("************QUERY 6 Results", q_string, data, true);
		
		//********QUERY 7
		//This will display all the moves and the machines able to be taught
		//to fast growth rate pokemon
		q_string=
		"prefix pkmn: <http://example.org/stuff/1.0/pkmn#> "+
		"prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
		"SELECT ?pokemon ?taught_move ?machine_number "+
		"WHERE { "+
		"?pokemon pkmn:can_learn ?taught_move . "+
		"?machine_number pkmn:teaches ?taught_move. "+ 
		"?pokemon pkmn:growth_rate pkmn:fast .} "+
		"ORDER BY ?pokemon ";
		
		sparql_query("************QUERY 7 Results", q_string, data, true);
		
		//********QUERY 8
		//This will display all the pokemone that can learn machine TM15 with
		//a filter
		q_string=
		"prefix pkmn: <http://example.org/stuff/1.0/pkmn#> "+
		"prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
		"SELECT ?pokemon ?taught_move "+
		"WHERE { "+
		"?pokemon pkmn:can_learn ?taught_move . "+
		"?machine_number pkmn:teaches ?taught_move. "+ 
		"FILTER (?machine_number = pkmn:TM15) .} "+
		"ORDER BY ?pokemon ";
		
		sparql_query("************QUERY 8 Results", q_string, data, true);
		
		//********QUERY 9
		//This is similar to the previous query, but will display only
		//pokemon that can be taught the move and will learn the move.
		q_string=
		"prefix pkmn: <http://example.org/stuff/1.0/pkmn#> "+
		"prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
		"SELECT ?pokemon ?taught_move "+
		"WHERE { "+
		"?pokemon pkmn:can_learn ?taught_move . "+
		"?pokemon pkmn:will_learn ?learned_move . "+
		"?machine_number pkmn:teaches ?taught_move. "+ 
		"FILTER (?machine_number = pkmn:TM15) . "+
		"FILTER (?taught_move = ?learned_move) .} "+
		"ORDER BY ?pokemon ";
		
		sparql_query("************QUERY 9 Results", q_string, data, true);
		
		//********QUERY 10
		//This is similar to the previous query, but uses a limit to
		//get the first pokemon alphabetically
		q_string=
		"prefix pkmn: <http://example.org/stuff/1.0/pkmn#> "+
		"prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
		"SELECT ?pokemon ?taught_move "+
		"WHERE { "+
		"?pokemon pkmn:can_learn ?taught_move . "+
		"?pokemon pkmn:will_learn ?learned_move . "+
		"?machine_number pkmn:teaches ?taught_move. "+ 
		"FILTER (?machine_number = pkmn:TM15) . " +
		"FILTER (?taught_move = ?learned_move) .} "+
		"ORDER BY ?pokemon "+
		"LIMIT 1 ";
		
		sparql_query("************QUERY 10 Results", q_string, data, true);


	}

}
