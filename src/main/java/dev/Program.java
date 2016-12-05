package dev;

import javax.jcr.GuestCredentials;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.Session; 
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.commons.JcrUtils;

public class Program {

	public static void exampleLogin() throws Exception {
		Repository repository = JcrUtils.getRepository();
		Session session = repository.login(new GuestCredentials());
		try { 			
			String user = session.getUserID(); 
			String name = repository.getDescriptor(Repository.REP_NAME_DESC); 
			System.out.println("Logged in as " + user + " to a " + name + " repository.");			
		} 
		finally {
			session.logout();
		}
	}

	public static void workingWithContent() throws Exception {
		Repository repository = JcrUtils.getRepository();
		Session session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
		
		try {
			Node root = session.getRootNode();
			
			Node hello = root.addNode("hello");
			Node world = hello.addNode("world");
			
			world.setProperty("message", "Hello, World!");
			session.save();
			
			//Retrieve content
			Node node = root.getNode("hello/world");
			System.out.println(node.getPath());
			System.out.println(node.getProperty("message").getString());
			
			
			//Remove content
			root.getNode("hello").remove();
			session.save();
			
		}
		finally {
			session.logout();
		}
		
	}
	
	
	public static void main(String[] args) throws Exception { 
		
		workingWithContent();
		
		
	}


}
