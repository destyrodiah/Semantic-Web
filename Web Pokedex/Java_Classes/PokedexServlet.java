package pokedex_web_content;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PokedexServlet
 */
@WebServlet("/PokedexServlet")
public class PokedexServlet extends HttpServlet implements javax.servlet.Servlet{
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		  web_builder n= new web_builder("C:\\Pokedex\\data\\pokemon.ttl");
		  n.build_web_page("?1");//request.getParameter("searchTerm"));
		  n.close();
		response.sendRedirect("C:\\Pokedex\\data\\webpage.html");
		;
	}
	
	
}



