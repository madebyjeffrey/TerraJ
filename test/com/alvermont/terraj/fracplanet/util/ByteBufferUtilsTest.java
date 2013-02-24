/*
 * ByteBufferUtilsTest.java
 * JUnit based test
 *
 * Created on January 1, 2006, 11:18 AM
 */

package com.alvermont.terraj.fracplanet.util;

import junit.framework.*;
import java.nio.ByteBuffer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Test cases for ByteBufferUtilsTest
 *
 * @author martin
 * @version $Id: ByteBufferUtilsTest.java,v 1.1 2006/01/28 19:05:08 martin Exp $
 */
public class ByteBufferUtilsTest extends TestCase
{
    
    public ByteBufferUtilsTest(String testName)
    {
        super(testName);
    }

    protected void setUp() throws Exception
    {
    }

    protected void tearDown() throws Exception
    {
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite(ByteBufferUtilsTest.class);
        
        return suite;
    }

    /**
     * Test of sizeofFloat method, of class com.alvermont.fracplanet.util.ByteBufferUtils.
     */
    public void testSizeofFloat()
    {
        System.out.println("sizeofFloat");
        
        int expResult = 0;
        int result = ByteBufferUtils.sizeofFloat();
        assertEquals(4, result);
    }

    /**
     * Test of sizeofInt method, of class com.alvermont.fracplanet.util.ByteBufferUtils.
     */
    public void testSizeofInt()
    {
        System.out.println("sizeofInt");
        
        int expResult = 0;
        int result = ByteBufferUtils.sizeofInt();
        assertEquals(4, result);
    }
    
}
