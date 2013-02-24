/*
 * FileUtilsTest.java
 * JUnit based test
 *
 * Created on 10 January 2006, 17:27
 */

package com.alvermont.terraj.fracplanet.io;

import junit.framework.*;
import java.io.File;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Test cases for FileUtils
 *
 * @author martin
 * @version $Id: FileUtilsTest.java,v 1.1 2006/01/28 19:05:08 martin Exp $
 */
public class FileUtilsTest extends TestCase
{
    
    public FileUtilsTest(String testName)
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
        TestSuite suite = new TestSuite(FileUtilsTest.class);
        
        return suite;
    }

    /**
     * Test of addExtension method, of class com.alvermont.fracplanet.io.FileUtils.
     */
    public void testAddExtension()
    {
        System.out.println("addExtension");
        
        File f = new File("/temp", "smeg");
        String newExt = ".git";
        
        File result = FileUtils.addExtension(f, newExt);

        assertEquals(f.getParent(), result.getParent());
        assertEquals("smeg", f.getName());
        assertEquals("smeg.git", result.getName());

        result = FileUtils.addExtension(f, "git");

        assertEquals(f.getParent(), result.getParent());
        assertEquals("smeg", f.getName());
        assertEquals("smeg.git", result.getName());

        File f2 = new File("/temp", "smeg.toss");

        result = FileUtils.addExtension(f2, newExt);

        assertEquals(f2.getParent(), result.getParent());
        assertEquals("smeg.toss", f2.getName());
        assertEquals("smeg.toss", result.getName());
    }

    /**
     * Test of changeExtension method, of class com.alvermont.fracplanet.io.FileUtils.
     */
    public void testChangeExtension()
    {
        System.out.println("changeExtension");
        
        File f = new File("/temp", "smeg");
        String newExt = ".git";
        
        File result = FileUtils.changeExtension(f, newExt);

        assertEquals(f.getParent(), result.getParent());
        assertEquals("smeg", f.getName());
        assertEquals("smeg.git", result.getName());

        File f2 = new File("/temp", "smeg.toss");

        result = FileUtils.changeExtension(f2, newExt);

        assertEquals(f2.getParent(), result.getParent());
        assertEquals("smeg.toss", f2.getName());
        assertEquals("smeg.git", result.getName());

        newExt = "git";
        result = FileUtils.changeExtension(f2, newExt);

        assertEquals(f2.getParent(), result.getParent());
        assertEquals("smeg.toss", f2.getName());
        assertEquals("smeg.git", result.getName());
    }
    
}
