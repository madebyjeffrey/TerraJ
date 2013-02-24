/*
 * TriangleBufferArrayTest.java
 * JUnit based test
 *
 * Created on December 31, 2005, 2:45 PM
 */

package com.alvermont.terraj.fracplanet.geom;

import junit.framework.*;
import java.nio.IntBuffer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Test cases for the triangle buffer array
 *
 * @author martin
 * @version $Id: TriangleBufferArrayTest.java,v 1.2 2006/04/24 14:58:50 martin Exp $
 */
public class TriangleBufferArrayTest extends TestCase
{
    
    public TriangleBufferArrayTest(String testName)
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
        TestSuite suite = new TestSuite(TriangleBufferArrayTest.class);
        
        return suite;
    }

    /**
     * Test of resizeBuffer method, of class com.alvermont.fracplanet.geom.TriangleBufferArray.
     */
    public void testResizeBuffer()
    {
        System.out.println("resizeBuffer");
        
        TriangleBufferArray instance = new TriangleBufferArray(10);

        assertEquals(30, instance.getBuffer().capacity());
        
        instance.resizeBuffer();

        assertEquals(51, instance.getBuffer().capacity());
    }

    /**
     * Test of set method, of class com.alvermont.fracplanet.geom.TriangleBufferArray.
     */
    public void testSet()
    {
        System.out.println("set");
        
        int index = 0;
        Triangle t1 = new SimpleTriangle(1,2,3);
        Triangle t2 = new SimpleTriangle(4,5,6);
        TriangleBufferArray instance = new TriangleBufferArray();
        
        instance.set(index, t1);
        
        assertEquals(t1.getVertex(0), instance.get(0).getVertex(0));
        assertEquals(t1.getVertex(1), instance.get(0).getVertex(1));
        assertEquals(t1.getVertex(2), instance.get(0).getVertex(2));

        instance.set(index, t2);

        assertEquals(t2.getVertex(0), instance.get(0).getVertex(0));
        assertEquals(t2.getVertex(1), instance.get(0).getVertex(1));
        assertEquals(t2.getVertex(2), instance.get(0).getVertex(2));
    }

    /**
     * Test of get method, of class com.alvermont.fracplanet.geom.TriangleBufferArray.
     */
    public void testGet()
    {
        System.out.println("get");
        
        int index = 0;
        TriangleBufferArray instance = new TriangleBufferArray();
        
        instance.set(0, new SimpleTriangle(1,2,3));
        
        Triangle expResult = new SimpleTriangle(1, 2, 3);
        Triangle result = instance.get(index);
        
        assertEquals(expResult.getVertex(0), result.getVertex(0));
        assertEquals(expResult.getVertex(1), result.getVertex(1));
        assertEquals(expResult.getVertex(2), result.getVertex(2));
    }

    /**
     * Test of add method, of class com.alvermont.fracplanet.geom.TriangleBufferArray.
     */
    public void testAdd()
    {
        System.out.println("add");
        
        Triangle t1 = new SimpleTriangle(1,2,3);
        Triangle t2 = new SimpleTriangle(4,5,6);
        
        TriangleBufferArray instance = new TriangleBufferArray();

        assertEquals(0, instance.size());
        
        instance.add(t1);
        assertEquals(1, instance.size());
        
        instance.add(t2);
        assertEquals(2, instance.size());

        assertEquals(1, instance.get(0).getVertex(0));
        assertEquals(2, instance.get(0).getVertex(1));
        assertEquals(3, instance.get(0).getVertex(2));

        assertEquals(4, instance.get(1).getVertex(0));
        assertEquals(5, instance.get(1).getVertex(1));
        assertEquals(6, instance.get(1).getVertex(2));
    }

    /**
     * Test of clear method, of class com.alvermont.fracplanet.geom.TriangleBufferArray.
     */
    public void testClear()
    {
        System.out.println("clear");
        
        Triangle t1 = new SimpleTriangle(1,2,3);
        TriangleBufferArray instance = new TriangleBufferArray();

        instance.add(t1);
        assertEquals(1, instance.size());
        assertEquals(3, instance.getBuffer().limit());
        
        instance.clear();
        assertEquals(0, instance.size());
        assertEquals(0, instance.getBuffer().limit());
    }

    /**
     * Test of addAll method, of class com.alvermont.fracplanet.geom.TriangleBufferArray.
     */
    public void testAddAll()
    {
        System.out.println("addAll");
        
        TriangleBufferArray source = new TriangleBufferArray();
        TriangleBufferArray instance = new TriangleBufferArray();
        
        instance.add(new SimpleTriangle(1,2,3));
        instance.add(new SimpleTriangle(4,5,6));
        
        source.add(new SimpleTriangle(7,8,9));
        source.add(new SimpleTriangle(10,11,12));
        
        instance.addAll(source);

        assertEquals(4, instance.size());
        assertEquals(12, instance.getBuffer().limit());

        assertEquals(1, instance.get(0).getVertex(0));
        assertEquals(2, instance.get(0).getVertex(1));
        assertEquals(3, instance.get(0).getVertex(2));

        assertEquals(4, instance.get(1).getVertex(0));
        assertEquals(5, instance.get(1).getVertex(1));
        assertEquals(6, instance.get(1).getVertex(2));

        assertEquals(7, instance.get(2).getVertex(0));
        assertEquals(8, instance.get(2).getVertex(1));
        assertEquals(9, instance.get(2).getVertex(2));

        assertEquals(10, instance.get(3).getVertex(0));
        assertEquals(11, instance.get(3).getVertex(1));
        assertEquals(12, instance.get(3).getVertex(2));
    }

    /**
     * Test of size method, of class com.alvermont.fracplanet.geom.TriangleBufferArray.
     */
    public void testSize()
    {
        System.out.println("size");
        
        TriangleBufferArray instance = new TriangleBufferArray();
        
        int expResult = 0;
        int result = instance.size();
        assertEquals(expResult, result);
        
        instance.add(new SimpleTriangle(1,2,3));
        
        assertEquals(1, instance.size());

        instance.add(new SimpleTriangle(4,5,6));
        
        assertEquals(2, instance.size());
    }    
}
