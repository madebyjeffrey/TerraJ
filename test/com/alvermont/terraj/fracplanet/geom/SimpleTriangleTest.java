/*
 * SimpleTriangleTest.java
 * JUnit based test
 *
 * Created on December 30, 2005, 10:11 AM
 */

package com.alvermont.terraj.fracplanet.geom;

import junit.framework.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Test cases for simple triangle
 *
 * @author martin
 */
public class SimpleTriangleTest extends TestCase
{
    
    public SimpleTriangleTest(String testName)
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
        TestSuite suite = new TestSuite(SimpleTriangleTest.class);
        
        return suite;
    }

    /**
     * Test of getVertex method, of class com.alvermont.fracplanet.geom.Triangle.
     */
    public void testGetVertex()
    {
        System.out.println("getVertex");
        
        int index = 0;
        Triangle instance = new SimpleTriangle(1,2,3);
        
        assertEquals(instance.getVertex(0), 1);
        assertEquals(instance.getVertex(1), 2);
        assertEquals(instance.getVertex(2), 3);
    }
    
    /**
     * Test of getVertex method, of class com.alvermont.fracplanet.geom.Triangle.
     */
    public void testCopyConsructor()
    {
        SimpleTriangle instance = new SimpleTriangle(4,5,6);
        Triangle t = new SimpleTriangle(instance);

        assertEquals(instance.getVertex(0), 4);
        assertEquals(instance.getVertex(1), 5);
        assertEquals(instance.getVertex(2), 6);

        assertEquals(t.getVertex(0), 4);
        assertEquals(t.getVertex(1), 5);
        assertEquals(t.getVertex(2), 6);
    }
}
