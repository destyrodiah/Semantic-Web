package pokedex_web_content;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class web_builder {

jena_graph web_graph;


public web_builder(String path) throws IOException
{
	web_graph= new jena_graph();
	web_graph.add_graph(path);
	web_graph.make_ontology();
	
}

public String extract_int_string(RDFNode uri)
{

Literal lit=(Literal) uri;
int i=lit.getInt();

return Integer.toString(i);
}

public String extract_uri_string(RDFNode uri)
{
String[] split= uri.toString().split("#");

return split[1];
}

public void build_web_page(String parameter) throws IOException{


Writer web_doc= new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\Pokedex\\data\\webpage.html")));

ResultSet results_id = this.get_poke_id(parameter);
web_doc.write("<!DOCTYPE html>");
web_doc.write("<html>");
web_doc.write("<head></head>");
web_doc.write("<body>");
while(results_id.hasNext()){
	
	RDFNode id_array[]=get_rdf_nodes(results_id.next(),2);
	RDFNode results_array[];
	
	
	
	web_doc.write("<div style=\"border:2px solid black; margin: 10px\">");
	
	web_doc.write("<h2>Pokemon</h2>");
	web_doc.write("<p>Pokemon Name: "+ extract_uri_string(id_array[0])+ "");
	web_doc.write("<br>Pokemon ID: "+ extract_int_string(id_array[1])+ "</p>");
	web_doc.write("<p><img src=\"C:\\Pokedex\\data\\pics\\"+extract_int_string(id_array[1])+".png\" style=\"width:152px;height:114px\"></p>");	
	web_doc.write("<p><audio controls><source src=\"C:\\Pokedex\\data\\cries\\"+extract_int_string(id_array[1])+".mp3\" type=\"audio/mpeg\"></audio></p>");
	web_doc.write("<h2>Stats</h2>");
	ResultSet results_stats = this.get_poke_stats(extract_int_string(id_array[1]));
	while(results_stats.hasNext()) {
		results_array=get_rdf_nodes(results_stats.next(), 7);
		web_doc.write("<p>Base Experience: " + extract_int_string(results_array[0]) + "<br>");
		web_doc.write("Base HP: " + extract_int_string(results_array[1]) + "<br>");
		web_doc.write("Base Attack: " + extract_int_string(results_array[2]) + "<br>");
		web_doc.write("Base Defense: " + extract_int_string(results_array[3]) + "<br>");
		web_doc.write("Base Speed: " + extract_int_string(results_array[4]) + "<br>");
		web_doc.write("Base Special Attack: " + extract_int_string(results_array[5]) + "<br>");
		web_doc.write("Base Special Defence: " + extract_int_string(results_array[6]) + "</p>");
	}
	
	web_doc.write("<h2>Pokedex Description</h2>");

	if(this.test_family(extract_int_string(id_array[1]))){
	ResultSet results_description = this.get_poke_description(extract_int_string(id_array[1]));//extract_int_string(id));
	while(results_description.hasNext()) {
		
		results_array=get_rdf_nodes(results_description.next(),8);
		web_doc.write("<p>Bio: " + results_array[0].toString() + "<br>");
		web_doc.write("Species Type 1: " + extract_uri_string(results_array[1]) + "<br>");
		web_doc.write("Species Type 2: " + extract_uri_string(results_array[2]) + "<br>");
		web_doc.write("Height: " + extract_int_string(results_array[3]) + "<br>");
		web_doc.write("Weight: " + extract_int_string(results_array[4]) + "<br>");
		web_doc.write("Color: " + results_array[5].toString() + "<br>");
		web_doc.write("Growth Rate: " + extract_uri_string(results_array[6]) + "<br>");
		web_doc.write("Catch Habitat: " + extract_uri_string(results_array[7]) + "</p>");
		}
		
	}
	else{
	ResultSet results_description = this.get_poke_description2(extract_int_string(id_array[1]));
	
	while(results_description.hasNext()) {
		
		results_array=get_rdf_nodes(results_description.next(),7);
		
		web_doc.write("<p>Bio: " + results_array[0].toString() + "<br>");
		
		web_doc.write("Species Type 1: " + extract_uri_string(results_array[1]) + "<br>");
		
		web_doc.write("Height: " + extract_int_string(results_array[2]) + "<br>");
		
		web_doc.write("Weight: " + extract_int_string(results_array[3]) + "<br>");
		web_doc.write("Color: " + results_array[4].toString() + "<br>");
		web_doc.write("Growth Rate: " + extract_uri_string(results_array[5]) + "<br>");
		web_doc.write("Catch Habitat: " + extract_uri_string(results_array[6]) + "</p>");
		}
		
	}
	
	ResultSet results_will_moves = this.get_will_moves(extract_int_string(id_array[1]));
	web_doc.write("<table border=\"1\"><tbody><tr><th>Level Learned</th><th>Move Learned</th></tr>");
	while(results_will_moves.hasNext()){
	results_array=get_rdf_nodes(results_will_moves.next(),2);
	web_doc.write("<tr><td>"+extract_int_string(results_array[0])+"</td><td>"+ extract_uri_string(results_array[1])+"</td></tr>");
	
	}
	web_doc.write("</table></body>");
	
	ResultSet results_can_moves = this.get_can_moves(extract_int_string(id_array[1]));
	web_doc.write("<table border=\"1\"><tbody><tr><th>Move Teaching Machine</th><th>Move Learned</th></tr>");
	while(results_can_moves.hasNext()){
	results_array=get_rdf_nodes(results_can_moves.next(),2);
	web_doc.write("<tr><td>"+extract_uri_string(results_array[0])+"</td><td>"+ extract_uri_string(results_array[1])+"</td></tr>");
	
	}
	web_doc.write("</table></body>");
web_doc.write("</div>");
}

web_doc.write("</html>");
;web_doc.write("</body>");
web_doc.close();
//web_graph.close();
};

public RDFNode[] get_rdf_nodes(QuerySolution row, int num_nodes){

RDFNode node_array[]= new RDFNode[num_nodes];
String query_nodes[]=  {"a", "b", "c", "d", "e", "f", "g", "h"};

for(int j = 0; j< num_nodes; j++)
{
node_array[j]=row.get(query_nodes[j]);
}

return node_array;
}

public String dereference_parameter(String parameters){

if(parameters=="" || parameters==" ")
return"";

else{
String[] split= parameters.toString().split(":");
String qry="";
for(int j = 0; j< split.length; j++)
{
switch (split[j].charAt(0)){
//context involving pokemon ID
case '?':
qry=qry+"?a pkmn:id "+split[j].substring(1)+" . ";
System.out.println(qry);
break;
case '@':
qry=qry+"?a pkmn:color \""+split[j].substring(1)+"\" . ";
System.out.println(qry);
break;
case '*':
qry=qry+"?a ?family_slot pkmn:"+split[j].substring(1)+" . ";
System.out.println(qry);
break;
case '$':
qry=qry+"?a pkmn:growth_rate pkmn:"+split[j].substring(1)+" . ";
System.out.println(qry);
break;
case '!':
qry=qry+"?a pkmn:will_learn ?node . ?node pkmn:level_learned_move pkmn:"+split[j].substring(1)+" . ";
System.out.println(qry);
case '%':
qry=qry+"?a pkmn:can_learn pkmn:"+split[j].substring(1)+" . ";
System.out.println(qry);
break;
}


}
return qry;
}
}


public ResultSet get_poke_id(String parameters){

String qry_filter = this.dereference_parameter(parameters);

String qry= "prefix pkmn: <http://example.org/stuff/1.0/pkmn#> " +
			"prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
			"SELECT ?a ?b  "+ 
			"WHERE "+
			"{?a pkmn:id ?b . "+ qry_filter+" }"+
			"ORDER BY ?b ";

return web_graph.select_query(qry);
}


public ResultSet get_will_moves(String id){

String qry="prefix pkmn: <http://example.org/stuff/1.0/pkmn#> "+ 
"prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
"SELECT ?a ?b "+
"WHERE "+
"{?pkmn pkmn:id "+id+" . "+
"?pkmn pkmn:will_learn ?node . "+
"?node pkmn:learned_level ?a . "+
"?node pkmn:level_learned_move ?b .} "+
"order by ?a";
return web_graph.select_query(qry);
}

public ResultSet get_can_moves(String id){

String qry="prefix pkmn: <http://example.org/stuff/1.0/pkmn#> "+ 
"prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
"SELECT ?a ?b "+
"WHERE "+
"{?pkmn pkmn:id "+id+" . "+
"?pkmn pkmn:can_learn ?b . "+
"?a pkmn:teaches ?b . }"+
"order by ?a ";
return web_graph.select_query(qry);
}


public ResultSet get_poke_stats(String id){
String qry= "prefix pkmn: <http://example.org/stuff/1.0/pkmn#> " +
			"prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
			"SELECT ?a ?b ?c ?d ?e ?f ?g "+ 
			"WHERE {"+
			"?pkmn pkmn:id "+id+" ."+ 
			"?pkmn pkmn:base_exp ?a . "+
			"?pkmn pkmn:base_hp ?b . "+
			"?pkmn pkmn:base_attack ?c . "+
			"?pkmn pkmn:base_defense ?d . "+
			"?pkmn pkmn:base_speed ?e . "+
			"?pkmn pkmn:base_special-attack ?f . "+
			"?pkmn pkmn:base_special-defense ?g . "+	
			"}";
return web_graph.select_query(qry);
}

public boolean test_family(String id){

String q_string=
"prefix pkmn: <http://example.org/stuff/1.0/pkmn#> "+
"prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
"ASK WHERE {?pkmn pkmn:id "+id +" . " +
"?pkmn pkmn:family_type_slot_2 ?c . "+
"}";

return web_graph.ask_query(q_string);
}

public ResultSet get_poke_description(String id){

String qry= "prefix pkmn: <http://example.org/stuff/1.0/pkmn#> "+  
"prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
"SELECT ?pkmn ?a ?b ?c ?d ?e ?f ?g ?h WHERE {?pkmn pkmn:id "+id +" . " + 
"?pkmn pkmn:pokedex_entry ?a . " +
"?pkmn pkmn:family_type_slot_1 ?b . "+
"?pkmn pkmn:family_type_slot_2 ?c . "+
"?pkmn pkmn:height ?d . "+ 
"?pkmn pkmn:weight ?e . "+
"?pkmn pkmn:color ?f . "+
"?pkmn pkmn:growth_rate ?g . "+
"?pkmn pkmn:catch_habitat ?h . "+ 
"}";
return web_graph.select_query(qry);
}

public ResultSet get_poke_description2(String id){

String qry= "prefix pkmn: <http://example.org/stuff/1.0/pkmn#> "+  
"prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
"SELECT ?pkmn ?a ?b ?c ?d ?e ?f ?g WHERE {?pkmn pkmn:id "+id+" . "+
"?pkmn pkmn:pokedex_entry ?a . " +
"?pkmn pkmn:family_type_slot_1 ?b . "+
"?pkmn pkmn:height ?c . "+ 
"?pkmn pkmn:weight ?d . "+
"?pkmn pkmn:color ?e . "+
"?pkmn pkmn:growth_rate ?f . "+
"?pkmn pkmn:catch_habitat ?g . "+ 
"}";
return web_graph.select_query(qry);
}

public ResultSet get_poke_can_learn(String id){

String qry= "prefix pkmn: <http://example.org/stuff/1.0/pkmn#> " +  
"prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + 
"SELECT ?a  "+
"WHERE { " +
"?pkmn pkmn:id "+ id +" . " +
"?pkmn pkmn:can_learn ?a . " + 
"}";
return web_graph.select_query(qry);
}

public ResultSet get_poke_will_learn(String id){

String qry= "prefix pkmn: <http://example.org/stuff/1.0/pkmn#> " +  
"prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + 
"SELECT ?a  "+
"WHERE { " +
"?pkmn pkmn:id "+ id +" . " +
"?pkmn pkmn:will_learn ?a . " + 
"}";
return web_graph.select_query(qry);
}


public ResultSet get_poke_evolution_cause(String id){

			
String qry= "prefix pkmn: <http://example.org/stuff/1.0/pkmn#> " +  
"prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + 
"SELECT ?a ?b "+
"WHERE { " +
"?pkmn pkmn:id "+ id +" . " +
"?pkmn pkmn:evolution_cause_type ?a . " +
"?pkmn pkmn:evolution_cause_trigger ?b . " +  
"}";
return web_graph.select_query(qry);
}

public void close(){
web_graph.close();
}

}
