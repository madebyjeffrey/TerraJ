/*
 * SimpleVertexTest.java
 * JUnit based test
 *
 * Created on December 30, 2005, 9:48 AM
 */

package com.alvermont.terraj.fracplanet.geom;

import junit.framework.*;
import com.alvermont.terraj.fracplanet.colour.ByteRGBA;
import com.alvermont.terraj.fracplanet.colour.FloatRGBA;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Test cases for SimpleVertex
 *
 * @author martin
 * @version $Id: SimpleVertexTest.java,v 1.2 2006/04/24 14:58:50 martin Exp $
 */
public class SimpleVertexTest extends TestCase
{
    
    public SimpleVertexTest(String testName)
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
        TestSuite suite = new TestSuite(SimpleVertexTest.class);
        
        return suite;
    }

    /**
     * Test of getPosition method, of class com.alvermont.fracplanet.geom.Vertex.
     */
    public void testGetPosition()
    {
        System.out.println("getPosition");
        
        XYZ p = new SimpleXYZ(1.0f, 2.0f, 3.0f);
        
        Vertex instance = new SimpleVertex(p);
        
        XYZ expResult = null;
        XYZ result = instance.getPosition();
        
        assertEquals(result.getX(), 1.0f);
        assertEquals(result.getY(), 2.0f);
        assertEquals(result.getZ(), 3.0f);
    }

    /**
     * Test of setPosition method, of class com.alvermont.fracplanet.geom.Vertex.
     */
    public void testSetPosition()
    {
        System.out.println("setPosition");
        
        XYZ p = new SimpleXYZ(4.0f,5.0f,6.0f);
        XYZ position = new SimpleXYZ(1.0f,2.0f,3.0f);
        Vertex instance = new SimpleVertex(p);
        
        assertEquals(instance.getPosition().getX(), 4.0f);
        assertEquals(instance.getPosition().getY(), 5.0f);
        assertEquals(instance.getPosition().getZ(), 6.0f);

        instance.setPosition(position);

        assertEquals(instance.getPosition().getX(), 1.0f);
        assertEquals(instance.getPosition().getY(), 2.0f);
        assertEquals(instance.getPosition().getZ(), 3.0f);
        
        assertEquals(position.getX(), 1.0f);
        assertEquals(position.getY(), 2.0f);
        assertEquals(position.getZ(), 3.0f);

        assertEquals(p.getX(), 4.0f);
        assertEquals(p.getY(), 5.0f);
        assertEquals(p.getZ(), 6.0f);
    }

    /**
     * Test of getNormal method, of class com.alvermont.fracplanet.geom.Vertex.
     */
    public void testGetNormal()
    {
        System.out.println("getNormal");
        
        XYZ p = new SimpleXYZ(4.0f,5.0f,6.0f);
        Vertex instance = new SimpleVertex(p);
        
        XYZ expResult = new SimpleXYZ(0.0f,0.0f,0.0f);
        XYZ result = instance.getNormal();
        
        assertEquals(expResult.getX(), result.getX());
        assertEquals(expResult.getY(), result.getY());
        assertEquals(expResult.getZ(), result.getZ());
    }

    /**
     * Test of setNormal method, of class com.alvermont.fracplanet.geom.Vertex.
     */
    public void testSetNormal()
    {
        System.out.println("setNormal");
        
        XYZ p = new SimpleXYZ(4.0f,5.0f,6.0f);
        XYZ normal = new SimpleXYZ(1.0f,2.0f,3.0f);
        Vertex instance = new SimpleVertex(p);
        
        instance.setNormal(normal);
        
        assertEquals(instance.getPosition().getX(), 4.0f);
        assertEquals(instance.getPosition().getY(), 5.0f);
        assertEquals(instance.getPosition().getZ(), 6.0f);

        assertEquals(instance.getNormal().getX(), 1.0f);
        assertEquals(instance.getNormal().getY(), 2.0f);
        assertEquals(instance.getNormal().getZ(), 3.0f);
    }

    /**
     * Test of getColour method, of class com.alvermont.fracplanet.geom.Vertex.
     */
    public void testGetColour()
    {
        System.out.println("getColour");
        
        int index = 0;
        Vertex instance = new SimpleVertex();
        
        ByteRGBA expResult = ByteRGBA.BLACK;
        ByteRGBA result = instance.getColour(index);
        assertEquals(expResult, result);        
    }

    /**
     * Test of setColour method, of class com.alvermont.fracplanet.geom.Vertex.
     */
    public void testSetColour()
    {
        System.out.println("setColour");
        
        int index = 0;
        ByteRGBA colour = new ByteRGBA(192,192,192);
        Vertex instance = new SimpleVertex();
        
        ByteRGBA expResult = ByteRGBA.BLACK;
        ByteRGBA result = instance.getColour(index);
        assertEquals(expResult, result);        

        instance.setColour(index, colour);

        result = instance.getColour(index + 1);
        assertEquals(expResult, result);        
    }

    /**
     * Test of getEmissive method, of class com.alvermont.fracplanet.geom.Vertex.
     */
    public void testGetEmissive()
    {
        System.out.println("getEmissive");
        
        int index = 0;
        Vertex instance = new SimpleVertex();
        
        boolean expResult = false;
        boolean result = instance.getEmissive(index);
        assertEquals(expResult, result);
    }

    /**
     * Test of setEmissive method, of class com.alvermont.fracplanet.geom.Vertex.
     */
    public void testSetEmissive()
    {
        System.out.println("setEmissive");
        
        int index = 0;
        boolean emissive = true;
        Vertex instance = new SimpleVertex();
        
        instance.setEmissive(index, emissive);
        
        assertEquals(instance.getEmissive(index), true);
        assertEquals(instance.getEmissive(index + 1), false);
    }
    
}
