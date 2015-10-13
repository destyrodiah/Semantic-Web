package pokedex_web_content;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
public class jena_graph {

Model graph_model;
OntModel ont_model;

public void add_graph(String path) throws IOException{

if (graph_model==null);
graph_model=ModelFactory.createDefaultModel();

InputStream in = new FileInputStream(new File(path));
Model mdl = ModelFactory.createDefaultModel().read(in,null,"TTL");
graph_model.add(mdl);
in.close();
mdl.close();
}

public void make_ontology(){
ont_model= ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RULE_INF,graph_model);
}

public boolean ask_query(String qry){
Query query = QueryFactory.create(qry);
QueryExecution qe =QueryExecutionFactory.create(query, ont_model);
boolean b= qe.execAsk();
qe.close();
return b;	
}

public ResultSet select_query(String qry){
Query query = QueryFactory.create(qry);
QueryExecution qe =QueryExecutionFactory.create(query, ont_model);
ResultSet results= qe.execSelect();
//qe.close();
return results;
}

public void close(){
if (graph_model!=null);
graph_model.close();
if (ont_model!=null);
ont_model.close();

}

public void test_state(){

if (ont_model!=null)
System.out.println("Good ONT");
if (graph_model!=null)
System.out.println("Good GRAPH");
}

}
