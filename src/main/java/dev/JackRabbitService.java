package dev;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.jcr.Binary;
import javax.jcr.GuestCredentials;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.Session; 
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.jackrabbit.core.TransientRepository;
import org.apache.log4j.*;


public class JackRabbitService {
	
	Repository rep = new TransientRepository();
	
	public JackRabbitService() throws Exception {		

	}
	
	public void addContent(String pathFileSystem, String nameNode) throws Exception {
		Session session = rep.login(new SimpleCredentials("admin", "admin".toCharArray()));
		
		try {
			InputStream stream = new BufferedInputStream(new FileInputStream(pathFileSystem));
					
					//JackRabbitService.class.getRe
					//.getResourceAsStream("/home/serg/workspace_spr/JackRabbitEx/src/main/resources/s_kem_ne_bivaet.jpg"));
            Node folder = session.getRootNode(); 
            Node file = folder.addNode(nameNode,"nt:file");
            Node content = file.addNode("jcr:content","nt:resource");
            Binary binary = session.getValueFactory().createBinary(stream);
            content.setProperty("jcr:data",binary);
            content.setProperty("jcr:mimeType","image/gif");
            session.save(); 
		}
		finally {
			session.logout();
		}
	}
	
	
	
	public void getContentExample(String nameNode, String pathFileSystem) throws Exception {
		Session session = rep.login(new SimpleCredentials("admin", "admin".toCharArray()));
		
		Node folder = session.getRootNode();
		Node file = folder.getNode(nameNode);
		Node content = file.getNode("jcr:content");
		String path = content.getPath();
		
		Binary bin = session.getNode(path).getProperty("jcr:data").getBinary();	
		InputStream stream = bin.getStream();
	
		OutputStream outputStream = new FileOutputStream(new File(pathFileSystem));

		int read = 0;
		byte[] bytes = new byte[1024];

		while ((read = stream.read(bytes)) != -1) {
			outputStream.write(bytes, 0, read);
		}

		System.out.println("Done!");
	}
	
	static Logger logger = Logger.getLogger(JackRabbitService.class);
	
	public static void main(String [] args) throws Exception {
		String workPath = "/home/serg/workspace_spr/JackRabbitEx/src/main/resources/"; 
		
		JackRabbitService jack = new JackRabbitService();
		logger.info("Add content");
		
		jack.addContent(workPath + "s_kem_ne_bivaet.jpg", "s_kem_ne_bivaet.jpg");
		
		logger.info("Get content");
		jack.getContentExample("s_kem_ne_bivaet.jpg", workPath + "output_image.jpg");
		
	}	
}
