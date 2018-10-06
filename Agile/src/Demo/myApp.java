package Demo;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.NameDAO;
import model.Name;

/**
 * Servlet implementation class myApp
 */
@WebServlet("/myApp")
public class myApp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public myApp() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		System.out.println("Checking for name in db");
		String name = request.getParameter("name");
		NameDAO.instance.checkFirstName(name);
		request.getRequestDispatcher("Index.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//PrintWriter p = response.getWriter();
		//String name = request.getParameter("name");
		//normally output will be in views
		//p.println("<h3>Hello  " + name + "</h3>");
		//p.close();
			
		doGet(request, response);
		
		String name = request.getParameter("name");
		Name A_Name= new Name(name);
		NameDAO.instance.saveFirstName(A_Name);
		request.getRequestDispatcher("Index.jsp").forward(request, response);
	}

}
