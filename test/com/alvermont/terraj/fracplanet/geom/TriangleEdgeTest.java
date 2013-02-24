/*
 * TriangleEdgeTest.java
 * JUnit based test
 *
 * Created on December 30, 2005, 10:17 AM
 */

package com.alvermont.terraj.fracplanet.geom;

import junit.framework.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author martin
 */
public class TriangleEdgeTest extends TestCase
{
    
    public TriangleEdgeTest(String testName)
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
        TestSuite suite = new TestSuite(TriangleEdgeTest.class);
        
        return suite;
    }

    
    /**
     * Test of constructor, of class com.alvermont.fracplanet.geom.TriangleEdge.
     */
    public void testConstructor()
    {
        System.out.println("Constructor");
        
        TriangleEdge e1 = new TriangleEdge(10, 20);
        TriangleEdge e2 = new TriangleEdge(20, 10);

        assertEquals(e1.getVertex0(), 10);
        assertEquals(e1.getVertex1(), 20);        

        assertEquals(e2.getVertex0(), 10);
        assertEquals(e2.getVertex1(), 20);        
    }
    
    /**
     * Test of copy constructor, of class com.alvermont.fracplanet.geom.TriangleEdge.
     */
    public void testCopyConstructor()
    {
        System.out.println("Copy Constructor");
        
        TriangleEdge e1 = new TriangleEdge(10, 20);
        TriangleEdge e2 = new TriangleEdge(e1);
        
        assertEquals(e1.getVertex0(), 10);
        assertEquals(e1.getVertex1(), 20);        

        assertEquals(e2.getVertex0(), 10);
        assertEquals(e2.getVertex1(), 20);        
    }
    
    /**
     * Test of compareTo method, of class com.alvermont.fracplanet.geom.TriangleEdge.
     */
    public void testCompareTo()
    {
        System.out.println("compareTo");
        
        TriangleEdge o = null;
        TriangleEdge instance = new TriangleEdge(10, 20);
        
        int expResult = 0;
        int result = 0;
        
        try
        {
            instance.compareTo(o);
            
            fail("Did not throw expected null pointer exception");
        }
        catch (NullPointerException npe)
        {
            // expected according to comparable interface, ignore
        }
        
        o = new TriangleEdge(1, 2);
        
        result = instance.compareTo(o);
        assertEquals(result, 1);
        
        result = o.compareTo(instance);
        assertEquals(result, -1);
        
        result = instance.compareTo(instance);
        assertEquals(result, 0);
        
        result = instance.compareTo(new TriangleEdge(10, 20));
        assertEquals(result, 0);
    }

    /**
     * Test of getVertex0 method, of class com.alvermont.fracplanet.geom.TriangleEdge.
     */
    public void testGetVertex0()
    {
        System.out.println("getVertex0");
        
        TriangleEdge instance = new TriangleEdge(10, 20);
        
        int expResult = 10;
        int result = instance.getVertex0();
        assertEquals(expResult, result);
    }

    /**
     * Test of getVertex1 method, of class com.alvermont.fracplanet.geom.TriangleEdge.
     */
    public void testGetVertex1()
    {
        System.out.println("getVertex1");
        
        TriangleEdge instance = new TriangleEdge(10, 20);
        
        int expResult = 20;
        int result = instance.getVertex1();
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class com.alvermont.fracplanet.geom.TriangleEdge.
     */
    public void testEquals()
    {
        System.out.println("equals");
        
        TriangleEdge obj = new TriangleEdge(10, 20);
        TriangleEdge instance = new TriangleEdge(20, 10);
        TriangleEdge instance2 = new TriangleEdge(20, 11);
        TriangleEdge instance3 = new TriangleEdge(19, 10);
        
        boolean expResult = true;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
        
        assertEquals(instance.equals(null), false);
        assertEquals(instance.equals(instance2), false);
        assertEquals(instance.equals(instance3), false);
    }

    /**
     * Test of hashCode method, of class com.alvermont.fracplanet.geom.TriangleEdge.
     */
    public void testHashCode()
    {
        System.out.println("hashCode");
        
        TriangleEdge instance = new TriangleEdge(10, 20);
        TriangleEdge instance2 = new TriangleEdge(10, 20);
        TriangleEdge instance3 = new TriangleEdge(11, 20);
        TriangleEdge instance4 = new TriangleEdge(10, 19);
        
        int expResult = instance2.hashCode();
        int result = instance.hashCode();
        assertEquals(expResult, result);
        
        assertFalse(instance.hashCode() == instance3.hashCode());
        assertFalse(instance.hashCode() == instance4.hashCode());
    }
    
}
