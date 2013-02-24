/*
 * FloatRGBTest.java
 * JUnit based test
 *
 * Created on January 2, 2006, 9:34 PM
 */

package com.alvermont.terraj.fracplanet.colour;

import junit.framework.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Test cases for FloatRGBA
 * 
 * 
 * @author martin
 * @version $Id: FloatRGBTest.java,v 1.2 2006/04/24 14:58:50 martin Exp $
 */
public class FloatRGBTest extends TestCase
{
    
    public FloatRGBTest(String testName)
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
        TestSuite suite = new TestSuite(FloatRGBTest.class);
        
        return suite;
    }

    /**
     * Test of copy constructor
     */
    public void testCopyConstructor()
    {
        System.out.println("opCopyConstructor");
        
        FloatRGBA v = new FloatRGBA(1.0f, 2.0f, 3.0f);
        
        FloatRGBA v2 = new FloatRGBA(v);
        
        assertEquals(v, v2);
    }
    
    /**
     * Test of opAddAssign method, of class com.alvermont.fracplanet.colour.FloatRGBA.
     */
    public void testOpAddAssign()
    {
        System.out.println("opAddAssign");
        
        FloatRGBA v = new FloatRGBA(1.0f, 2.0f, 3.0f);
        FloatRGBA instance = new FloatRGBA(3.0f, 4.0f, 5.0f);
        FloatRGBA result = new FloatRGBA(4.0f, 6.0f, 8.0f);
        
        instance.opAddAssign(v);
        
        assertEquals(result.getR(), instance.getR());
        assertEquals(result.getG(), instance.getG());
        assertEquals(result.getB(), instance.getB());
    }

    /**
     * Test of opSubtractAssign method, of class com.alvermont.fracplanet.colour.FloatRGBA.
     */
    public void testOpSubtractAssign()
    {
        System.out.println("opSubtractAssign");
        
        FloatRGBA v = new FloatRGBA(1.0f, 2.0f, 3.0f);
        FloatRGBA instance = new FloatRGBA(4.0f, 5.0f, 7.0f);
        FloatRGBA result = new FloatRGBA(3.0f, 3.0f, 4.0f);
        
        instance.opSubtractAssign(v);
        
        assertEquals(result.getR(), instance.getR());
        assertEquals(result.getG(), instance.getG());
        assertEquals(result.getB(), instance.getB());
    }

    /**
     * Test of opMultiplyAssign method, of class com.alvermont.fracplanet.colour.FloatRGBA.
     */
    public void testOpMultiplyAssign()
    {
        System.out.println("opMultiplyAssign");
        
        FloatRGBA v = new FloatRGBA(1.0f, 2.0f, 3.0f);
        FloatRGBA instance = new FloatRGBA(4.0f, 5.0f, 7.0f);
        FloatRGBA result = new FloatRGBA(4.0f, 10.0f, 21.0f);
        
        instance.opMultiplyAssign(v);

        assertEquals(result.getR(), instance.getR());
        assertEquals(result.getG(), instance.getG());
        assertEquals(result.getB(), instance.getB());
    }

    /**
     * Test of opEquals method, of class com.alvermont.fracplanet.colour.FloatRGBA.
     */
    public void testOpEquals()
    {
        System.out.println("opEquals");
        
        FloatRGBA a = new FloatRGBA(1.0f, 2.0f, 3.0f);
        FloatRGBA b = new FloatRGBA(1.0f, 2.0f, 3.0f);
        FloatRGBA c = new FloatRGBA(1.0f, 2.0f, 3.5f);
        
        boolean expResult = true;
        boolean result = a.opEquals(a, b);
        
        assertEquals(expResult, result);
        
        assertEquals(false, a.opEquals(a, c));
        assertEquals(false, a.opEquals(b, c));
    }

    /**
     * Test of opNotEquals method, of class com.alvermont.fracplanet.colour.FloatRGBA.
     */
    public void testOpNotEquals()
    {
        System.out.println("opNotEquals");
        
        FloatRGBA a = new FloatRGBA(1.0f, 2.0f, 3.0f);
        FloatRGBA b = new FloatRGBA(1.0f, 2.0f, 3.0f);
        FloatRGBA c = new FloatRGBA(1.0f, 2.0f, 3.5f);
        
        boolean expResult = false;
        boolean result = a.opNotEquals(a, b);
        
        assertEquals(expResult, result);
        
        assertEquals(true, a.opNotEquals(a, c));
        assertEquals(true, a.opNotEquals(b, c));
    }

    /**
     * Test of equals method, of class com.alvermont.fracplanet.colour.FloatRGBA.
     */
    public void testEquals()
    {
        System.out.println("equals");
        
        FloatRGBA a = new FloatRGBA(1.0f, 2.0f, 3.0f);
        FloatRGBA b = new FloatRGBA(1.0f, 2.0f, 3.0f);
        FloatRGBA c = new FloatRGBA(1.0f, 2.0f, 3.5f);
        
        boolean expResult = true;
        boolean result = a.equals(b);
        
        assertEquals(expResult, result);
        
        assertEquals(true, a.equals(a));
        assertEquals(false, a.equals(c));
    }

    /**
     * Test of opSubtract method, of class com.alvermont.fracplanet.colour.FloatRGBA.
     */
    public void testOpSubtract()
    {
        System.out.println("opSubtract");
        
        FloatRGBA a = new FloatRGBA(1.0f, 2.0f, 3.0f, 0);
        FloatRGBA b = new FloatRGBA(4.0f, 5.0f, 6.0f, 0);
        
        FloatRGBA expResult = new FloatRGBA(-3.0f, -3.0f, -3.0f, 0);
        FloatRGBA result = FloatRGBA.opSubtract(a, b);
        assertEquals(expResult, result);
    }

    /**
     * Test of opNegate method, of class com.alvermont.fracplanet.colour.FloatRGBA.
     */
    public void testOpNegate()
    {
        System.out.println("opNegate");
        
        FloatRGBA c = new FloatRGBA(1.0f, 2.0f, 3.5f);
        
        FloatRGBA expResult = new FloatRGBA(-1.0f, -2.0f, -3.5f);
        FloatRGBA result = FloatRGBA.opNegate(c);

        assertEquals(expResult.getR(), result.getR());
        assertEquals(expResult.getG(), result.getG());
        assertEquals(expResult.getB(), result.getB());
    }

    /**
     * Test of opAdd method, of class com.alvermont.fracplanet.colour.FloatRGBA.
     */
    public void testOpAdd()
    {
        System.out.println("opAdd");
        
        FloatRGBA v = new FloatRGBA(1.0f, 2.0f, 3.0f);
        FloatRGBA b = new FloatRGBA(4.0f, 5.0f, 6.0f);

        FloatRGBA result = new FloatRGBA(5.0f, 7.0f, 9.0f);
        
        FloatRGBA res = v.opAdd(b, v);
        
        assertEquals(res.getR(), result.getR());
        assertEquals(res.getG(), result.getG());
        assertEquals(res.getB(), result.getB());
    }

    /**
     * Test of opMultiply method, of class com.alvermont.fracplanet.colour.FloatRGBA.
     */
    public void testOpMultiply()
    {
        System.out.println("opMultiply");
        
        FloatRGBA v = new FloatRGBA(1.0f, 2.0f, 3.0f);
        FloatRGBA b = new FloatRGBA(4.0f, 5.0f, 6.0f);

        FloatRGBA result = new FloatRGBA(4.0f, 10.0f, 18.0f);
        
        FloatRGBA res = v.opMultiply(b, v);
        
        assertEquals(res.getR(), result.getR());
        assertEquals(res.getG(), result.getG());
        assertEquals(res.getB(), result.getB());
    }
    
}
