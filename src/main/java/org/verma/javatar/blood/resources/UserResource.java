package org.verma.javatar.blood.resources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.verma.javatar.blood.model.Credential;
import org.verma.javatar.blood.model.User;

@Path("/users")
public class UserResource {
		Connection con;
		
		public UserResource(){
			try {
				Class.forName("org.postgresql.Driver");
				con = DriverManager.getConnection("jdbc:postgresql://ec2-54-225-200-15.compute-1.amazonaws.com:5432/d1kvh0214fuo42", "chzopxklezjqom", "66e3d9afb927c511e9e5b55e5d9d7631eb051edaa58c708139c70eaffb422731");
			} catch(Exception ex) {
				System.out.println(ex.getMessage());
			}	
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<HashMap<String,Object>> getAllUsers() throws SQLException {
		PreparedStatement stmt = con.prepareStatement("SELECT * FROM \"public\".\"blood_user\"");
		List<HashMap<String, Object>> userDataList = new ArrayList<>();
		HashMap<String, Object> userDataMap;
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			userDataMap = new HashMap<>();
			userDataMap.put("id", rs.getInt(1));
			userDataMap.put("fname", rs.getString(2));
			userDataMap.put("lname", rs.getString(3));
			userDataMap.put("email", rs.getString(4));
			userDataMap.put("password", rs.getString(5));
			userDataMap.put("blood_group", rs.getString(6));
			userDataMap.put("city", rs.getString(7));
			userDataList.add(userDataMap);
		}
		return userDataList;
	}
	
	@GET
	@Path("/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public HashMap<String,Object> getUser(@PathParam("userId") Integer id) throws SQLException {
		PreparedStatement stmt = con.prepareStatement("SELECT * FROM \"public\".\"blood_user\" WHERE id="+id.toString());
		HashMap<String, Object> userDataMap;
		ResultSet rs = stmt.executeQuery();
		userDataMap = new HashMap<>();
		while(rs.next()) {
			userDataMap.put("id", rs.getInt(1));
			userDataMap.put("fname", rs.getString(2));
			userDataMap.put("lname", rs.getString(3));
			userDataMap.put("email", rs.getString(4));
			userDataMap.put("password", rs.getString(5));
			userDataMap.put("blood_group", rs.getString(6));
			userDataMap.put("city", rs.getString(7));
		}
		return userDataMap;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response addUser(User user) throws SQLException {
		if(user == null)
		{
			System.out.println("NULL");
			return Response.serverError().entity("user details are missing").build();
		}
		PreparedStatement stmt = con.prepareStatement("INSERT INTO \"public\".\"blood_user\" (fname,lname,email,password,blood_group,city) values ('"+
														user.getFname()+"','"+
														user.getLname()+"','"+
														user.getEmail()+"','"+
														user.getPassword()+"','"+
														user.getBlood_group()+"','"+
														user.getCity()+"')"
														);
		
		if(stmt.executeUpdate()==1) {
			return Response.status(Response.Status.CREATED).entity("user details are created").build();
		}
		else
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	}
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response authenticateUser(Credential user) throws SQLException {
		if(user == null)
		{
			System.out.println("NULL");
			return Response.serverError().entity("user details are missing").build();
		}
		PreparedStatement stmt = con.prepareStatement("SELECT password FROM \"public\".\"blood_user\" WHERE email='"+ user.getEmail()+"'");
		ResultSet rs = stmt.executeQuery();
		while(rs.next())
		{
		String password = rs.getString(1);
		if(password.equals(user.getPassword()))
		{
			return Response.status(Response.Status.ACCEPTED).entity("user details are matched").build();
		}
		}
		return Response.status(Response.Status.UNAUTHORIZED).build();
	}
	
}
