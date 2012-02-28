package org.bioinfo.infrared.ws.server.rest.functgen.jtg.lib;

import java.io.File;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionManager {
	
	public static Session createSession(){	
		Session session = null;
		String confFile = "/home/jtarraga/appl/biopax-lib/src/main/java/hibernate.cfg.xml";
		//String confFile = "org/bioinfo/biopax/hibernate.cfg.xml";
		File f = new File(confFile);

		try {
			SessionFactory sessionFactory;
			//			System.out.println("before configure: " + confFile);
			//			System.out.println("before configure: " + f.getAbsolutePath());
			//BY DEFAULT SEARCH ON DEPLOY DIRECTORY (TARGET/CLASSES)

			//			sessionFactory = new Configuration().configure().buildSessionFactory();
			sessionFactory = new Configuration().configure(f).buildSessionFactory();
			//			System.out.println("configured");
			//also, resources xml must be in resources directory and linked in hibernateConf as: resource="org/bioinfo/bioinfo-rank/web/Disease.hbm.xml"
			session = sessionFactory.openSession();
			//			System.out.println("OPENED SESSION");		

		} catch (Throwable ex) {
			// Log exception!
			session = null;
			System.err.println("Initial SessionFactory creation failed." + ex);
			System.err.println(ex);
		}
		return session;	
	}	


	public static void closeSession(Session session){	
		try {
			session.close();			
		} catch (Throwable ex) {
			System.err.println(ex);
		}	
	}

}
