/*
 * ImmutableXYZTest.java
 * JUnit based test
 *
 * Created on January 2, 2006, 4:51 PM
 */

package com.alvermont.terraj.fracplanet.geom;

import junit.framework.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Test cases for ImmutableXYZ
 *
 * @author martin
 * @version $Id: ImmutableXYZTest.java,v 1.2 2006/04/24 14:58:50 martin Exp $
 */
public class ImmutableXYZTest extends TestCase
{
    
    public ImmutableXYZTest(String testName)
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
        TestSuite suite = new TestSuite(ImmutableXYZTest.class);
        
        return suite;
    }
    
    /**
     * Test of setZ method, of class com.alvermont.fracplanet.geom.ImmutableXYZ.
     */
    public void testSetZ()
    {
        System.out.println("setZ");
        
        float z = 0.0F;
        ImmutableXYZ instance = (ImmutableXYZ) ImmutableXYZ.XYZ_ZERO;
        
        try
        {
            instance.setZ(z);
            
            fail("Did not throw expected exception");
        }
        catch (UnsupportedOperationException uoe)
        {
            // we expect this exception, don't worry about it
        }
    }
    
    /**
     * Test of setY method, of class com.alvermont.fracplanet.geom.ImmutableXYZ.
     */
    public void testSetY()
    {
        System.out.println("setY");
        
        float z = 0.0F;
        ImmutableXYZ instance = (ImmutableXYZ) ImmutableXYZ.XYZ_ZERO;
        
        try
        {
            instance.setY(z);
            
            fail("Did not throw expected exception");
        }
        catch (UnsupportedOperationException uoe)
        {
            // we expect this exception, don't worry about it
        }
    }
    
    /**
     * Test of setX method, of class com.alvermont.fracplanet.geom.ImmutableXYZ.
     */
    public void testSetX()
    {
        System.out.println("setX");
        
        float z = 0.0F;
        ImmutableXYZ instance = (ImmutableXYZ) ImmutableXYZ.XYZ_ZERO;
        
        try
        {
            instance.setX(z);
            
            fail("Did not throw expected exception");
        }
        catch (UnsupportedOperationException uoe)
        {
            // we expect this exception, don't worry about it
        }
    }
    
    /**
     * Test of opSubtractAssign method, of class com.alvermont.fracplanet.geom.ImmutableXYZ.
     */
    public void testOpSubtractAssign()
    {
        System.out.println("opSubtractAssign");
        
        ImmutableXYZ instance = (ImmutableXYZ) ImmutableXYZ.XYZ_ZERO;
        XYZ smeg = new SimpleXYZ(1.0f, 2.0f, 3.0f);
        
        try
        {
            instance.opSubtractAssign(smeg);
            
            fail("Did not throw expected exception");
        }
        catch (UnsupportedOperationException uoe)
        {
            // we expect this exception, don't worry about it
        }
    }
    
    /**
     * Test of opMultiplyAssign method, of class com.alvermont.fracplanet.geom.ImmutableXYZ.
     */
    public void testOpMultiplyAssign()
    {
        System.out.println("opMultiplyAssign");
        
        ImmutableXYZ instance = (ImmutableXYZ) ImmutableXYZ.XYZ_ZERO;
        float smeg = 1.2f;
        
        try
        {
            instance.opMultiplyAssign(smeg);
            
            fail("Did not throw expected exception");
        }
        catch (UnsupportedOperationException uoe)
        {
            // we expect this exception, don't worry about it
        }
    }
    
    /**
     * Test of opDivideAssign method, of class com.alvermont.fracplanet.geom.ImmutableXYZ.
     */
    public void testOpDivideAssign()
    {
        System.out.println("opDivideAssign");
        
        ImmutableXYZ instance = (ImmutableXYZ) ImmutableXYZ.XYZ_ZERO;
        float smeg = 1.2f;
        
        try
        {
            instance.opDivideAssign(smeg);
            
            fail("Did not throw expected exception");
        }
        catch (UnsupportedOperationException uoe)
        {
            // we expect this exception, don't worry about it
        }
    }
    
    /**
     * Test of opAssign method, of class com.alvermont.fracplanet.geom.ImmutableXYZ.
     */
    public void testOpAssign()
    {
        System.out.println("opAssign");
        
        ImmutableXYZ instance = (ImmutableXYZ) ImmutableXYZ.XYZ_ZERO;
        XYZ smeg = new SimpleXYZ(1.0f, 2.0f, 3.0f);
        
        try
        {
            instance.opAssign(smeg);
            
            fail("Did not throw expected exception");
        }
        catch (UnsupportedOperationException uoe)
        {
            // we expect this exception, don't worry about it
        }
    }
    
    /**
     * Test of opAddAssign method, of class com.alvermont.fracplanet.geom.ImmutableXYZ.
     */
    public void testOpAddAssign()
    {
        System.out.println("opAddAssign");
        
        ImmutableXYZ instance = (ImmutableXYZ) ImmutableXYZ.XYZ_ZERO;
        XYZ smeg = new SimpleXYZ(1.0f, 2.0f, 3.0f);
        
        try
        {
            instance.opAddAssign(smeg);
            
            fail("Did not throw expected exception");
        }
        catch (UnsupportedOperationException uoe)
        {
            // we expect this exception, don't worry about it
        }
    }
    
    /**
     * Test of normalise method, of class com.alvermont.fracplanet.geom.ImmutableXYZ.
     */
    public void testNormalise()
    {
        System.out.println("normalise");
        
        ImmutableXYZ instance = (ImmutableXYZ) ImmutableXYZ.XYZ_ZERO;
        
        try
        {
            instance.normalise();
            
            fail("Did not throw expected exception");
        }
        catch (UnsupportedOperationException uoe)
        {
            // we expect this exception, don't worry about it
        }
    }
    
}
