/** 
 * Copyright (c) IBM Corporation, 2000
 * This software and documentation is the confidential and proprietary
 * information of IBM, Corp. ("Confidential Information").
 * Raleigh, NC USA
 */

import java.lang.*;
import java.lang.reflect.*;
import junit.framework.*;

/**
 * A TestSuite that puts together all the test classes from
 * all com.ibm.jusb, com.ibm.posj and  and javax.usb packages
 * @author Helen Li                                        
 */

public class AllTests extends TestSuite
{
	//-------------------------------------------------------------------------
	// Public class methods
	//

	public static TestSuite suite()
	{
               TestSuite suite = new TestSuite();

               try {
                    Class testClass=Class.forName("javax.usb.AllTests");
                    Method testMethod=testClass.getMethod("suite", new Class[0]);
                    suite.addTest( (TestSuite)testMethod.invoke(null, new Object[0]) );
                }

                catch (Exception e) {
                }
                

                try {
                    Class testClass=Class.forName("com.ibm.posj.AllTests");
                    Method testMethod=testClass.getMethod("suite", new Class[0]);
                    suite.addTest( (TestSuite)testMethod.invoke(null, new Object[0]) );
                }

                catch (Exception e) {
                }


                try {
                     Class testClass=Class.forName("com.ibm.jusb.AllTests");
                     Method testMethod=testClass.getMethod("suite", new Class[0]);
                     suite.addTest( (TestSuite)testMethod.invoke(null, new Object[0]) );
                }

                catch (Exception e) {
                }
                
		
		return suite;
	}
        
}

