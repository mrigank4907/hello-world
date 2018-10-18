import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

import java.sql.*;

@SuppressWarnings("serial")
public class JDBCMainWindowContent extends JInternalFrame implements ActionListener
{	
	String cmd = null;

	// DB Connectivity Attributes
	private Connection con = null;
	private Statement stmt = null;
	private Statement stmt2 = null;
	private ResultSet rs = null;

	private Container content;

	private JPanel detailsPanel;
	private JPanel exportButtonPanel;
	private JPanel picsPanel;
	private JPanel navPanel;
	//private JPanel exportConceptDataPanel;
	private JScrollPane dbContentsPanel;
	private JScrollPane dbAuditPanel;


	private Border lineBorder;

	private JLabel IDLabel=new JLabel("ID:                 ");
	private JLabel MovieNameLabel=new JLabel("Movie Name:               ");
	private JLabel GenreLabel=new JLabel("Movie Genre:      ");
	private JLabel YearReleaseLabel=new JLabel("Year Release:        ");
	private JLabel DirFNameLabel=new JLabel("Director First Name:                 ");
	private JLabel DirLNameLabel=new JLabel("Director Last Name:               ");
	private JLabel CountryLabel=new JLabel("Country:      ");
	private JLabel DirAgeLabel=new JLabel("Directors Age:      ");
	private JLabel RatingLabel=new JLabel("Rating:        ");
	private JLabel image = new JLabel();
	private JLabel fileName = new JLabel("Enter file name");

	private JTextField IDTF= new JTextField(10);
	private JTextField MovieNameTF=new JTextField(10);
	private JTextField GenreTF=new JTextField(10);
	private JTextField YearRelTF=new JTextField(10);
	private JTextField DirFNameTF=new JTextField(10);
	private JTextField DirLNameTF=new JTextField(10);
	private JTextField CountryTF=new JTextField(10);
	private JTextField DirAgeTF=new JTextField(10);
	private JTextField RatingTF=new JTextField(10);
	private JTextField fileNameTF = new JTextField(20);


	private static QueryTableModel TableModel = new QueryTableModel();
	private static QueryTableModel2 TableModel2 = new QueryTableModel2();
	//Add the models to JTabels
	private JTable TableofDBContents=new JTable(TableModel);
	private JTable TableofDBAudit=new JTable(TableModel2);
	//Buttons for inserting, and updating members
	//also a clear button to clear details panel
	private JButton updateButton = new JButton("Update");
	private JButton insertButton = new JButton("Insert");
	private JButton exportButton  = new JButton("Export");
	private JButton deleteButton  = new JButton("Delete");
	private JButton clearButton  = new JButton("Clear");
	private JButton showButton  = new JButton("Show Img");
	private JButton nextButton  = new JButton("Next");
	private JButton prevButton  = new JButton("Previous");

	private JButton  NumDirectors = new JButton("NumDirectorsForCountry:");
	private JTextField NumDirectorsTF  = new JTextField(12);
	private JButton avgAgeCountry  = new JButton("AvgAgeForCountry");
	private JTextField avgAgeCountryTF  = new JTextField(12);
	private JButton ListAllCountries  = new JButton("ListAllCountries");
	private JButton ListAllGenre  = new JButton("ListAllGenre");

	public int current = 0;


	public JDBCMainWindowContent( String aTitle)
	{	
		//setting up the GUI
		super(aTitle, false,false,false,false);
		setEnabled(true);

		initiate_db_conn();
		//add the 'main' panel to the Internal Frame
		content=getContentPane();
		content.setLayout(null);
		content.setBackground(Color.CYAN);
		lineBorder = BorderFactory.createEtchedBorder(15, Color.green, Color.pink);

		//setup details panel and add the components to it
		detailsPanel=new JPanel();
		detailsPanel.setLayout(new GridLayout(11,2));
		detailsPanel.setBackground(Color.CYAN);
		detailsPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "CRUD Actions"));

		detailsPanel.add(IDLabel);
		detailsPanel.add(IDTF);
		detailsPanel.add(MovieNameLabel);		
		detailsPanel.add(MovieNameTF);
		detailsPanel.add(GenreLabel);		
		detailsPanel.add(GenreTF);
		detailsPanel.add(YearReleaseLabel);	
		detailsPanel.add(YearRelTF);
		detailsPanel.add(DirFNameLabel);		
		detailsPanel.add(DirFNameTF);
		detailsPanel.add(DirLNameLabel);
		detailsPanel.add(DirLNameTF);
		detailsPanel.add(CountryLabel);
		detailsPanel.add(CountryTF);
		detailsPanel.add(DirAgeLabel);
		detailsPanel.add(DirAgeTF);
		detailsPanel.add(RatingLabel);
		detailsPanel.add(RatingTF);

		//set picture pannel
		picsPanel=new JPanel();
		picsPanel.setLayout(new GridLayout(11,2));
		picsPanel.setBackground(Color.CYAN);
		picsPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "Movie Picture"));
		picsPanel.setSize(300, 200);
		picsPanel.setLocation(530, 300);
		picsPanel.setLayout(new FlowLayout());
		image.setSize(500, 200);
		content.add(picsPanel);
		picsPanel.add(image);


		navPanel = new JPanel();
		navPanel.setLayout(new GridLayout(2,2));
		navPanel.setBackground(Color.CYAN);
		navPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "Navigation Panel"));
		navPanel.setSize(200, 100);
		navPanel.setLocation(10, 300);
		content.add(navPanel);

		navPanel.add(fileName);
		navPanel.add(fileNameTF);
		fileNameTF.setSize(50, 20);

		navPanel.add(exportButton);
		navPanel.add(clearButton);


		
		 
		TableofDBAudit.setPreferredScrollableViewportSize(new Dimension(900, 300));

		dbAuditPanel=new JScrollPane(TableofDBAudit,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		dbAuditPanel.setBackground(Color.CYAN);
		dbAuditPanel.setBorder(BorderFactory.createTitledBorder(lineBorder,"Audit Table Content"));
		dbAuditPanel.setSize(350, 200);
		dbAuditPanel.setLocation(830, 300);

		content.add(dbAuditPanel);

		//setup details panel and add the components to it
		exportButtonPanel=new JPanel();
		exportButtonPanel.setLayout(new GridLayout(3,2));
		exportButtonPanel.setBackground(Color.CYAN);
		exportButtonPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "Export Data"));
		exportButtonPanel.add(NumDirectors);
		exportButtonPanel.add(NumDirectorsTF);
		exportButtonPanel.add(avgAgeCountry);
		exportButtonPanel.add(avgAgeCountryTF);
		exportButtonPanel.add(ListAllCountries);
		exportButtonPanel.add(ListAllGenre);
		exportButtonPanel.setSize(300, 200);
		exportButtonPanel.setLocation(230, 300);
		content.add(exportButtonPanel);

		insertButton.setSize(100, 30);
		updateButton.setSize(100, 30);
		exportButton.setSize (50, 30);
		deleteButton.setSize (100, 30);
		clearButton.setSize (50, 30);
		showButton.setSize (100, 30);
		nextButton.setSize(100,30);
		prevButton.setSize(100,30);

		insertButton.setLocation(370, 10);
		updateButton.setLocation(370, 110);
		exportButton.setLocation (5, 30);
		deleteButton.setLocation (370, 60);
		clearButton.setLocation (5, 50);
		showButton.setLocation (370, 260);
		nextButton.setLocation(370,160);
		prevButton.setLocation(370,210);

		insertButton.addActionListener(this);
		updateButton.addActionListener(this);
		exportButton.addActionListener(this);
		deleteButton.addActionListener(this);
		clearButton.addActionListener(this);
		showButton.addActionListener(this);
		nextButton.addActionListener(this);
		prevButton.addActionListener(this);

		this.ListAllCountries.addActionListener(this);
		this.NumDirectors.addActionListener(this);
		this.avgAgeCountry.addActionListener(this);
		this.ListAllGenre.addActionListener(this);

		content.add(insertButton);
		content.add(updateButton);
		content.add(deleteButton);
		content.add(showButton);
		content.add(nextButton);
		content.add(prevButton);


		TableofDBContents.setPreferredScrollableViewportSize(new Dimension(900, 300));

		dbContentsPanel=new JScrollPane(TableofDBContents,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		dbContentsPanel.setBackground(Color.CYAN);
		dbContentsPanel.setBorder(BorderFactory.createTitledBorder(lineBorder,"Database Content"));

		detailsPanel.setSize(360, 300);
		detailsPanel.setLocation(3,0);
		dbContentsPanel.setSize(700, 300);
		dbContentsPanel.setLocation(477, 0);

		content.add(detailsPanel);
		content.add(dbContentsPanel);


		setSize(982,645);
		setVisible(true);

		TableModel.refreshFromDB(stmt);
		TableModel2.refreshFromDB(stmt2);
	}

	public void initiate_db_conn()
	{
		try
		{
			// Load the JConnector Driver
			Class.forName("com.mysql.jdbc.Driver");
			// Specify the DB Name
			String url="jdbc:mysql://localhost:3307/Assign";
			// Connect to DB using DB URL, Username and password
			con = DriverManager.getConnection(url, "root", "admin");
			//Create a generic statement which is passed to the TestInternalFrame1
			stmt = con.createStatement();
			stmt2 = con.createStatement();
		}
		catch(Exception e)
		{
			System.out.println("Error: Failed to connect to database\n"+e.getMessage());
		}
	}

	//event handling 
	public void actionPerformed(ActionEvent e)
	{
		Object target=e.getSource();
		if (target == clearButton)
		{
			IDTF.setText("");
			MovieNameTF.setText("");
			GenreTF.setText("");
			YearRelTF.setText("");
			DirFNameTF.setText("");
			DirLNameTF.setText("");
			CountryTF.setText("");
			DirAgeTF.setText("");
			RatingTF.setText("");
			fileNameTF.setText("");

		}

		if (target == insertButton)
		{		 
			try
			{
				String updateTemp ="INSERT INTO movie_details VALUES("+
						null +",'"+MovieNameTF.getText()+"','"+GenreTF.getText()+"',"+YearRelTF.getText()+",'"+DirFNameTF.getText()+"','"
						+DirLNameTF.getText()+"','"+CountryTF.getText()+"',"+DirAgeTF.getText()+",'"+RatingTF.getText()+"');";

				stmt.executeUpdate(updateTemp);

			}
			catch (SQLException sqle)
			{
				System.err.println("Error with  insert:\n"+sqle.toString());
			}
			finally
			{
				TableModel.refreshFromDB(stmt);
				TableModel2.refreshFromDB(stmt2);

			}
		}
		if (target == deleteButton)
		{

			try
			{
				String updateTemp ="DELETE FROM movie_details WHERE id = "+IDTF.getText()+";"; 
				stmt.executeUpdate(updateTemp);

			}
			catch (SQLException sqle)
			{

				System.err.println("Error with delete:\n"+sqle.toString());
			}
			finally
			{
				TableModel.refreshFromDB(stmt);
				TableModel2.refreshFromDB(stmt2);
			}
		}
		if (target == updateButton)
		{	 	
			try
			{ 			
				String updateTemp ="UPDATE movie_details SET " +
						"Movie_Name = '"+MovieNameTF.getText()+
						"', Genre = '"+GenreTF.getText()+
						"', Year_Release = "+YearRelTF.getText()+
						", Dir_First_Name ='"+DirFNameTF.getText()+
						"', Dir_Last_Name = '"+DirLNameTF.getText()+
						"', Country = '"+CountryTF.getText()+
						"', Age = "+DirAgeTF.getText()+
						", Rating = '"+RatingTF.getText()+
						"' where id = "+IDTF.getText();


				stmt.executeUpdate(updateTemp);
				//these lines do nothing but the table updates when we access the db.
				rs = stmt.executeQuery("SELECT * from movie_details ");
				rs.next();
				rs.close();	
			}
			catch (SQLException sqle){
				System.err.println("Error with  update:\n"+sqle.toString());
			}
			finally{
				TableModel.refreshFromDB(stmt);
				TableModel2.refreshFromDB(stmt2);
			}
		}


		if(target == exportButton)
		{
			cmd = "select * from movie_details;";

			try{					
				rs= stmt.executeQuery(cmd); 
				String name = fileNameTF.getText();
				writeToFileExportAll(rs,name);
			}
			catch(Exception e1){e1.printStackTrace();}
		}

		if(target == showButton)
		{
			System.out.println("Show image button");
			try{
				String idmovie = IDTF.getText();
				int idmov = Integer.parseInt(idmovie);
				String sql = "SELECT ImageNo, Image, Movie_id FROM movie_images where Movie_id = "+ idmov +";";
				PreparedStatement stmt = con.prepareStatement(sql);
				ResultSet resultSet = stmt.executeQuery();
				while (resultSet.next()) {
					String imgno = resultSet.getString(1);
					byte[] imageBlob = resultSet.getBytes(2);
					String movieid = resultSet.getString(3);
					System.out.println(movieid);
					ImageIcon imageIcon = new ImageIcon(imageBlob);
					imageIcon.getImage();
					image.setIcon(imageIcon);
				}
				sql = "Select Movie_Name,Genre,Year_Release,Dir_First_Name,Dir_Last_Name,Country,Age,Rating from movie_details where id = " + idmovie+";";
				stmt = con.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery();
				while(rs.next())
				{
					String movieName = rs.getString(1);
					String genre = rs.getString(2);
					String year = rs.getString(3);
					String dirFName = rs.getString(4);
					String dirLName = rs.getString(5);
					String country = rs.getString(6);
					String age = rs.getString(7);
					String rating = rs.getString(8);

					IDTF.setText(idmovie);
					MovieNameTF.setText(movieName);
					GenreTF.setText(genre);
					YearRelTF.setText(year);
					DirFNameTF.setText(dirFName);
					DirLNameTF.setText(dirLName);
					CountryTF.setText(country);
					DirAgeTF.setText(age);
					RatingTF.setText(rating);

				}		

				current = idmov;
			}catch(Exception e1){e1.printStackTrace();}		
		}


		if(target == nextButton)
		{
			current++;
			System.out.println("Next button");
			try{

				String sql = "SELECT ImageNo, Image, Movie_id FROM movie_images where Movie_id = "+ current +";";
				PreparedStatement stmt = con.prepareStatement(sql);
				ResultSet resultSet = stmt.executeQuery();
				while (resultSet.next()) {
					String imgno = resultSet.getString(1);
					byte[] imageBlob = resultSet.getBytes(2);
					String movieid = resultSet.getString(3);
					System.out.println(movieid);
					ImageIcon imageIcon = new ImageIcon(imageBlob);
					imageIcon.getImage();
					image.setIcon(imageIcon);
				}
				sql = "Select id,Movie_Name,Genre,Year_Release,Dir_First_Name,Dir_Last_Name,Country,Age,Rating from movie_details where id = " + current+";";
				stmt = con.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery();
				while(rs.next())
				{
					String id = rs.getString(1);
					String movieName = rs.getString(2);
					String genre = rs.getString(3);
					String year = rs.getString(4);
					String dirFName = rs.getString(5);
					String dirLName = rs.getString(6);
					String country = rs.getString(7);
					String age = rs.getString(8);
					String rating = rs.getString(9);

					IDTF.setText(id);
					MovieNameTF.setText(movieName);
					GenreTF.setText(genre);
					YearRelTF.setText(year);
					DirFNameTF.setText(dirFName);
					DirLNameTF.setText(dirLName);
					CountryTF.setText(country);
					DirAgeTF.setText(age);
					RatingTF.setText(rating);

				}		
			}catch(Exception e1){e1.printStackTrace();}	
		}

		if(target == prevButton)
		{
			current--;
			System.out.println("Previous button");
			try{
				String sql = "SELECT ImageNo, Image, Movie_id FROM movie_images where Movie_id = "+ current +";";
				PreparedStatement stmt = con.prepareStatement(sql);
				ResultSet resultSet = stmt.executeQuery();
				while (resultSet.next()) {
					String imgno = resultSet.getString(1);
					byte[] imageBlob = resultSet.getBytes(2);
					String movieid = resultSet.getString(3);
					System.out.println(movieid);
					ImageIcon imageIcon = new ImageIcon(imageBlob);
					imageIcon.getImage();
					image.setIcon(imageIcon);
				}
				sql = "Select id,Movie_Name,Genre,Year_Release,Dir_First_Name,Dir_Last_Name,Country,Age,Rating from movie_details where id = " + current+";";
				stmt = con.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery();
				while(rs.next())
				{
					String id = rs.getString(1);
					String movieName = rs.getString(2);
					String genre = rs.getString(3);
					String year = rs.getString(4);
					String dirFName = rs.getString(5);
					String dirLName = rs.getString(6);
					String country = rs.getString(7);
					String age = rs.getString(8);
					String rating = rs.getString(9);

					IDTF.setText(id);
					MovieNameTF.setText(movieName);
					GenreTF.setText(genre);
					YearRelTF.setText(year);
					DirFNameTF.setText(dirFName);
					DirLNameTF.setText(dirLName);
					CountryTF.setText(country);
					DirAgeTF.setText(age);
					RatingTF.setText(rating);

				}		
			}catch(Exception e1){e1.printStackTrace();}	

		}

		/////////////////////////////////////////////////////////////////////////////////////
		//I have only added functionality of 2 of the button on the lower right of the template
		///////////////////////////////////////////////////////////////////////////////////

		if(target == this.ListAllCountries){

			cmd = "select distinct country from movie_details;";

			try{					
				rs= stmt.executeQuery(cmd); 	
				writeToFile(rs);
			}
			catch(Exception e1){e1.printStackTrace();}

		}

		if(target == this.NumDirectors){
			String countryName = this.NumDirectorsTF.getText();

			cmd = "select country, count(*) as NumberOfDirectors "+  "from movie_details " + "where country = '"  +countryName+"';";

			System.out.println(cmd);
			try{					
				rs= stmt.executeQuery(cmd); 	
				writeToFile(rs);
			}
			catch(Exception e1){e1.printStackTrace();}

		}

		if(target == this.avgAgeCountry){

			cmd = "Select country,avg(age) as AverageAgeForCountry From movie_details Where country = '" +avgAgeCountryTF.getText() +"';";

			try{					
				rs= stmt.executeQuery(cmd); 	
				writeToFile(rs);
			}
			catch(Exception e1){e1.printStackTrace();}

		}

		if(target == this.ListAllGenre){

			cmd = "select distinct genre from movie_details;";

			try{					
				rs= stmt.executeQuery(cmd); 	
				writeToFile(rs);
			}
			catch(Exception e1){e1.printStackTrace();}

		}

	}
	///////////////////////////////////////////////////////////////////////////


	////////////////////////////////////////////////////////////////////////////

	private void writeToFile(ResultSet rs){
		try{
			System.out.println("In writeToFile");
			FileWriter outputFile = new FileWriter("ExportedFileOnQuery.csv");
			PrintWriter printWriter = new PrintWriter(outputFile);
			ResultSetMetaData rsmd = rs.getMetaData();
			int numColumns = rsmd.getColumnCount();

			for(int i=0;i<numColumns;i++){
				printWriter.print(rsmd.getColumnLabel(i+1)+",");
			}
			printWriter.print("\n");
			while(rs.next()){
				for(int i=0;i<numColumns;i++){
					printWriter.print(rs.getString(i+1)+",");
				}
				printWriter.print("\n");
				printWriter.flush();
			}
			printWriter.close();
		}
		catch(Exception e){e.printStackTrace();}
	}


	private void writeToFileExportAll(ResultSet rs, String fileName){
		try{
			System.out.println("In writeToFile");
			FileWriter outputFile = new FileWriter(fileName+".csv");
			PrintWriter printWriter = new PrintWriter(outputFile);
			ResultSetMetaData rsmd = rs.getMetaData();
			int numColumns = rsmd.getColumnCount();

			for(int i=0;i<numColumns;i++){
				printWriter.print(rsmd.getColumnLabel(i+1)+",");
			}
			printWriter.print("\n");
			while(rs.next()){
				for(int i=0;i<numColumns;i++){
					printWriter.print(rs.getString(i+1)+",");
				}
				printWriter.print("\n");
				printWriter.flush();
			}
			printWriter.close();
		}
		catch(Exception e){e.printStackTrace();}
	}
}
