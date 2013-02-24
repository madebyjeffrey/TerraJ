/*
 * VertexBufferArrayTest.java
 * JUnit based test
 *
 * Created on January 1, 2006, 11:14 AM
 */

package com.alvermont.terraj.fracplanet.geom;

import junit.framework.*;
import com.alvermont.terraj.fracplanet.colour.ByteRGBA;
import com.alvermont.terraj.fracplanet.colour.FloatRGBA;
import com.alvermont.terraj.fracplanet.util.ByteBufferUtils;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Test cases for VertexBufferArray
 *
 * @author martin
 * @version $Id: VertexBufferArrayTest.java,v 1.2 2006/04/24 14:58:50 martin Exp $
 */
public class VertexBufferArrayTest extends TestCase
{
    
    public VertexBufferArrayTest(String testName)
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
        TestSuite suite = new TestSuite(VertexBufferArrayTest.class);
        
        return suite;
    }

    /**
     * Test of resizeBuffer method, of class com.alvermont.fracplanet.geom.VertexBufferArray.
     */
    public void testResizeBuffer()
    {
        System.out.println("resizeBuffer");
        
        VertexBufferArray instance = new VertexBufferArray(10);
        
        assertEquals(400, instance.getBuffer().capacity());
        
        instance.resizeBuffer();

        assertEquals(680, instance.getBuffer().capacity());
    }

    /**
     * Test of set method, of class com.alvermont.fracplanet.geom.VertexBufferArray.
     */
    public void testSet()
    {
        System.out.println("set");
        
        int index = 0;

        VertexBufferArray instance = new VertexBufferArray();
        
        XYZ p = new SimpleXYZ(1.0f, 2.0f, 3.0f);

        Vertex v = new SimpleVertex();
        
        v.setPosition(p);
        v.setNormal(ImmutableXYZ.XYZ_ZERO);
        
        instance.set(index, v);
    }

    /**
     * Test of get method, of class com.alvermont.fracplanet.geom.VertexBufferArray.
     */
    public void testGet()
    {
        System.out.println("get");
        
        int index = 0;
        VertexBufferArray instance = new VertexBufferArray();
        
        XYZ p = new SimpleXYZ(1.0f, 2.0f, 3.0f);

        Vertex v = new SimpleVertex();
        
        v.setPosition(p);
        v.setNormal(ImmutableXYZ.XYZ_ZERO);

        instance.set(index, v);
        
        Vertex result = instance.get(index);

        assertEquals(result.getPosition().getX(), 1.0f);
        assertEquals(result.getPosition().getY(), 2.0f);
        assertEquals(result.getPosition().getZ(), 3.0f);
        
        assertEquals(result.getNormal().getX(), 0.0f);
        assertEquals(result.getNormal().getY(), 0.0f);
        assertEquals(result.getNormal().getZ(), 0.0f);
    }

    /**
     * Test of setPosition method, of class com.alvermont.fracplanet.geom.VertexBufferArray.
     */
    public void testSetPosition()
    {
        System.out.println("setPosition");
        
        int index = 0;
        XYZ position = null;
        VertexBufferArray instance = new VertexBufferArray();

        XYZ p = new SimpleXYZ(1.0f, 2.0f, 3.0f);
        XYZ q = new SimpleXYZ(4.0f, 5.0f, 6.0f);
        
        instance.setPosition(index, p);
        
        XYZ p2 = instance.getPosition(index);
        
        assertEquals(p.getX(), p2.getX());
        assertEquals(p.getY(), p2.getY());
        assertEquals(p.getZ(), p2.getZ());

        instance.setPosition(index, q);
        
        p2 = instance.getPosition(index);
        
        assertEquals(q.getX(), p2.getX());
        assertEquals(q.getY(), p2.getY());
        assertEquals(q.getZ(), p2.getZ());
    }

    /**
     * Test of getPosition method, of class com.alvermont.fracplanet.geom.VertexBufferArray.
     */
    public void testGetPosition()
    {
        System.out.println("getPosition");
        
        int index = 0;
        VertexBufferArray instance = new VertexBufferArray();
        
        XYZ p = new SimpleXYZ(1.0f, 2.0f, 3.0f);

        instance.setPosition(index, p);
        
        XYZ expResult = new SimpleXYZ(1.0f, 2.0f, 3.0f);
        XYZ result = instance.getPosition(index);

        assertEquals(expResult.getX(), result.getX());
        assertEquals(expResult.getY(), result.getY());
        assertEquals(expResult.getZ(), result.getZ());
    }

    /**
     * Test of setNormal method, of class com.alvermont.fracplanet.geom.VertexBufferArray.
     */
    public void testSetNormal()
    {
        System.out.println("setNormal");
        
        int index = 0;
        XYZ position = null;
        VertexBufferArray instance = new VertexBufferArray();

        XYZ p = new SimpleXYZ(1.0f, 2.0f, 3.0f);
        XYZ q = new SimpleXYZ(4.0f, 5.0f, 6.0f);
        
        instance.setNormal(index, p);
        
        XYZ p2 = instance.getNormal(index);
        
        assertEquals(p.getX(), p2.getX());
        assertEquals(p.getY(), p2.getY());
        assertEquals(p.getZ(), p2.getZ());

        instance.setNormal(index, q);
        
        p2 = instance.getNormal(index);
        
        assertEquals(q.getX(), p2.getX());
        assertEquals(q.getY(), p2.getY());
        assertEquals(q.getZ(), p2.getZ());
    }

    /**
     * Test of getNormal method, of class com.alvermont.fracplanet.geom.VertexBufferArray.
     */
    public void testGetNormal()
    {
        System.out.println("getNormal");
        
        int index = 0;
        VertexBufferArray instance = new VertexBufferArray();
                
        XYZ p = new SimpleXYZ(1.0f, 2.0f, 3.0f);

        instance.setNormal(index, p);
        
        XYZ expResult = new SimpleXYZ(1.0f, 2.0f, 3.0f);
        XYZ result = instance.getNormal(index);

        assertEquals(expResult.getX(), result.getX());
        assertEquals(expResult.getY(), result.getY());
        assertEquals(expResult.getZ(), result.getZ());
    }

    /**
     * Test of add method, of class com.alvermont.fracplanet.geom.VertexBufferArray.
     */
    public void testAdd()
    {
        System.out.println("add");
        
        VertexBufferArray instance = new VertexBufferArray();
        
        XYZ p1 = new SimpleXYZ(1.0f, 2.0f, 3.0f);

        Vertex v1 = new SimpleVertex();
        
        v1.setPosition(p1);
        v1.setNormal(ImmutableXYZ.XYZ_ZERO);

        assertEquals(0, instance.size());
        instance.add(v1);
        assertEquals(1, instance.size());

        assertEquals(1.0f, instance.get(0).getPosition().getX());
        assertEquals(2.0f, instance.get(0).getPosition().getY());
        assertEquals(3,0f, instance.get(0).getPosition().getZ());    
    }

    /**
     * Test of addAll method, of class com.alvermont.fracplanet.geom.VertexBufferArray.
     */
    public void testAddAll()
    {
        System.out.println("addAll");
        
        VertexBufferArray source = new VertexBufferArray();
        VertexBufferArray instance = new VertexBufferArray();
        
        XYZ p1 = new SimpleXYZ(1.0f, 2.0f, 3.0f);

        Vertex v1 = new SimpleVertex();
        
        v1.setPosition(p1);
        v1.setNormal(ImmutableXYZ.XYZ_ZERO);
        
        XYZ p2 = new SimpleXYZ(4.0f, 5.0f, 6.0f);

        Vertex v2 = new SimpleVertex();
        
        v2.setPosition(p2);
        v2.setNormal(ImmutableXYZ.XYZ_ZERO);

        XYZ p3 = new SimpleXYZ(7.0f, 8.0f, 9.0f);

        Vertex v3 = new SimpleVertex();
        
        v3.setPosition(p3);
        v3.setNormal(ImmutableXYZ.XYZ_ZERO);
        
        XYZ p4 = new SimpleXYZ(10.0f, 11.0f, 12.0f);

        Vertex v4 = new SimpleVertex();
        
        v4.setPosition(p4);
        v4.setNormal(ImmutableXYZ.XYZ_ZERO);
        
        source.add(v1);
        source.add(v2);
        instance.add(v3);
        instance.add(v4);
        
        assertEquals(2, instance.size());
        assertEquals(2, source.size());
        
        instance.addAll(source);
        
        assertEquals(4, instance.size());
        
        assertEquals(1.0f, instance.get(2).getPosition().getX());
        assertEquals(2.0f, instance.get(2).getPosition().getY());
        assertEquals(3,0f, instance.get(2).getPosition().getZ());

        assertEquals(4.0f, instance.get(3).getPosition().getX());
        assertEquals(5.0f, instance.get(3).getPosition().getY());
        assertEquals(6,0f, instance.get(3).getPosition().getZ());

        assertEquals(7.0f, instance.get(0).getPosition().getX());
        assertEquals(8.0f, instance.get(0).getPosition().getY());
        assertEquals(9,0f, instance.get(0).getPosition().getZ());

        assertEquals(10.0f, instance.get(1).getPosition().getX());
        assertEquals(11.0f, instance.get(1).getPosition().getY());
        assertEquals(12,0f, instance.get(1).getPosition().getZ());
    }

    /**
     * Test of size method, of class com.alvermont.fracplanet.geom.VertexBufferArray.
     */
    public void testSize()
    {
        System.out.println("size");
        
        VertexBufferArray instance = new VertexBufferArray();
        
        XYZ p = new SimpleXYZ(1.0f, 2.0f, 3.0f);

        Vertex v = new SimpleVertex();
        
        v.setPosition(p);
        v.setNormal(ImmutableXYZ.XYZ_ZERO);
        
        int expResult = 0;
        int result = instance.size();
        assertEquals(expResult, result);
        
        instance.add(v);
        assertEquals(1, instance.size());

        instance.add(v);
        assertEquals(2, instance.size());
    }

    /**
     * Test of clear method, of class com.alvermont.fracplanet.geom.VertexBufferArray.
     */
    public void testClear()
    {
        System.out.println("clear");
        
        VertexBufferArray instance = new VertexBufferArray();
        
        XYZ p = new SimpleXYZ(1.0f, 2.0f, 3.0f);

        Vertex v = new SimpleVertex();
        
        v.setPosition(p);
        v.setNormal(ImmutableXYZ.XYZ_ZERO);
        
        int expResult = 0;
        int result = instance.size();
        assertEquals(expResult, result);
        
        instance.add(v);
        assertEquals(1, instance.size());

        instance.clear();
        assertEquals(0, instance.size());
        assertEquals(0, instance.getPositionBuffer().limit());
        assertEquals(0, instance.getNormalBuffer().limit());
        assertEquals(0, instance.getColourBuffer().limit());
        assertEquals(0, instance.getEmissiveBuffer().limit());
   }

    /**
     * Test of getBuffer method, of class com.alvermont.fracplanet.geom.VertexBufferArray.
     */
    public void testGetBuffer()
    {
        System.out.println("getBuffer");
        
        VertexBufferArray instance = new VertexBufferArray();
        
        Buffer result = instance.getBuffer();
        
        assertNotNull(result);
        
        //assertEquals(0, result.positionlimit());
        assertEquals(0, instance.size());

        XYZ p = new SimpleXYZ(1.0f, 2.0f, 3.0f);

        Vertex v = new SimpleVertex();
        
        v.setPosition(p);
        v.setNormal(ImmutableXYZ.XYZ_ZERO);
        
        instance.add(v);
        result = instance.getBuffer();
        
        //assertEquals(instance.getElementSize(), result.limit());
        assertEquals(1, instance.size());
        assertEquals(true, result.isReadOnly());
    }

    /**
     * Test of getElementSize method, of class com.alvermont.fracplanet.geom.VertexBufferArray.
     */
    public void testGetElementSize()
    {
        System.out.println("getElementSize");
        
        VertexBufferArray instance = new VertexBufferArray();
        
        int expResult = 0;
        int result = instance.getElementSize();
        assertEquals(40, result);
    }
    
}
