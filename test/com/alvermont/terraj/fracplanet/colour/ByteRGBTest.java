/*
 * ByteRGBTest.java
 * JUnit based test
 *
 * Created on January 2, 2006, 9:55 PM
 */

package com.alvermont.terraj.fracplanet.colour;

import junit.framework.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Test cases for ByteRGBA
 * 
 * 
 * @author martin
 * @version $Id: ByteRGBTest.java,v 1.2 2006/04/24 14:58:50 martin Exp $
 */
public class ByteRGBTest extends TestCase
{
    
    public ByteRGBTest(String testName)
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
        TestSuite suite = new TestSuite(ByteRGBTest.class);
        
        return suite;
    }

    /**
     * Test of copy constructor
     */
    public void testCopyConstructor()
    {
        System.out.println("copyConstructor");
        
        ByteRGBA a = new ByteRGBA(10, 20, 30);
        ByteRGBA b = new ByteRGBA(a);
        
        assertEquals(a.getR(), b.getR());
        assertEquals(a.getG(), b.getG());
        assertEquals(a.getB(), b.getB());
    }
    
    /**
     * Test of copy constructor
     */
    public void testFloatCopyConstructor()
    {
        System.out.println("floatCopyConstructor");
        
        FloatRGBA a = new FloatRGBA(1.0f, 0.5f, 0.25f);
        ByteRGBA b = new ByteRGBA(a);
        
        assertEquals(127, b.getR());
        assertEquals(63, b.getG());
        assertEquals(31, b.getB());
    }

    /**
     * Test of opAddAssign method, of class com.alvermont.fracplanet.colour.ByteRGBA.
     */
    public void testOpAddAssign()
    {
        System.out.println("opAddAssign");
        
        ByteRGBA v = new ByteRGBA(10, 20, 30);
        ByteRGBA instance = new ByteRGBA(1, 2, 3);
        
        instance.opAddAssign(v);
        
        assertEquals(11, instance.getR());
        assertEquals(22, instance.getG());
        assertEquals(33, instance.getB());
    }

    /**
     * Test of opSubtractAssign method, of class com.alvermont.fracplanet.colour.ByteRGBA.
     */
    public void testOpSubtractAssign()
    {
        System.out.println("opSubtractAssign");
        
        ByteRGBA v = new ByteRGBA(10, 20, 30);
        ByteRGBA instance = new ByteRGBA(1, 2, 3);
        
        instance.opSubtractAssign(v);
        
        assertEquals(-9, instance.getR());
        assertEquals(-18, instance.getG());
        assertEquals(-27, instance.getB());
    }

    /**
     * Test of getR method, of class com.alvermont.fracplanet.colour.ByteRGBA.
     */
    public void testGetR()
    {
        System.out.println("getR");
        
        ByteRGBA instance = new ByteRGBA(10, 20, 30);
        
        short expResult = 10;
        short result = instance.getR();
        assertEquals(expResult, result);
    }

    /**
     * Test of getG method, of class com.alvermont.fracplanet.colour.ByteRGBA.
     */
    public void testGetG()
    {
        System.out.println("getG");
        
        ByteRGBA instance = new ByteRGBA(10, 20, 30);
        
        short expResult = 20;
        short result = instance.getG();
        assertEquals(expResult, result);
    }

    /**
     * Test of getB method, of class com.alvermont.fracplanet.colour.ByteRGBA.
     */
    public void testGetB()
    {
        System.out.println("getB");
        
        ByteRGBA instance = new ByteRGBA(10, 20, 30);
        
        short expResult = 30;
        short result = instance.getB();
        assertEquals(expResult, result);
    }
    
}
