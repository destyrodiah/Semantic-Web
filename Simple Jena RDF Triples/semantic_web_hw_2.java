//Semantic Web Assignment 2
//Author Kristopher Rutherford
//Date 10/2/2014

//Import IO Packages
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

//Import Jena Packages
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;

public class semantic_web_hw_2 {

		public static void main (String args[]) {
		
		//Declare placeholder for writer class to write data from model later
		Writer writer= null;
		
		//creates new Jena Model and loads it with rdf file from British Library.
		//May need to change directory path to match new host machine
		Model model = ModelFactory.createDefaultModel();
		InputStream in = FileManager.get().open("C:\\dump\\lodbnb_serials.rdf");
		model.read(in,"");
		
		//declare new iterator based on jena graph statements
		StmtIterator iter = model.listStatements();
		
		
		try{
		//Create new buffered writer class to write identical content from console to file
		//for homework audit purposes.
		writer = new BufferedWriter(new OutputStreamWriter(
          new FileOutputStream("C:\\dump\\print_output.txt")));
          
		while(iter.hasNext()){
		
		//isolate respective components from triples to print each component to file and console
		Statement stmt = iter.nextStatement();
		Resource subject = stmt.getSubject();
		Property predicate = stmt.getPredicate();
		RDFNode object = stmt.getObject();
		
		//print to console
		System.out.print("Subject: "+subject.toString() + "\n"
						+ "Predicate: "+predicate.toString() + "\n"
						+"Object: "+object.toString() + "\n");
						
		//write to output file				
		writer.write("Subject: "+subject.toString() + "\n"
						+ "Predicate: "+predicate.toString() + "\n"
						+"Object: "+object.toString() + "\n");						
		
		}
		//Java exception block to allow IO Class usage
		}catch(IOException ex){} 
		
		//close writer class
		finally {try {writer.close();} catch (Exception ex) {}}	
		
		//write Jena graph to rdf/XML format with Java exception block to allow IO Class usage
		try{
		model.write(new FileOutputStream("C:\\dump\\jena_rdf_output_file.xml"));
		}catch(IOException ex){}

		}
	}
