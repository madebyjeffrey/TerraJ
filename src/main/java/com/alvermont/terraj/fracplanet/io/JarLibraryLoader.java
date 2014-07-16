/*
 * Java Terrain and Stellar System Ports
 *
 * Copyright (C) 2006 Martin H. Smith based on work by original
 * authors.
 *
 * Released under the terms of the GNU General Public License
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 * Linking TerraJ statically or dynamically with other modules is making a
 * combined work based on TerraJ. Thus, the terms and conditions of the
 * GNU General Public License cover the whole combination.
 *
 * In addition, as a special exception, the copyright holders of TerraJ
 * give you permission to combine this program with free software programs
 * or libraries that are released under the GNU LGPL and with code included
 * in the standard release of JOGL, Java Getopt and FreeMarker under the BSD
 * license (or modified versions of such code, with unchanged license) and with
 * Apache Commons and Log4J libraries under the Apache license (or modified versions
 * of such code. You may copy and distribute such a system following the terms
 * of the GNU GPL for TerraJ and the licenses of the other code concerned,
 * provided that you include the source code of that other code when and as the
 * GNU GPL requires distribution of source code.
 *
 * Note that people who make modified versions of TerraJ are not obligated to grant
 * this special exception for their modified versions; it is their choice whether
 * to do so. The GNU General Public License gives permission to release a modified
 * version without this exception; this exception also makes it possible to release
 * a modified version which carries forward this exception.
 */

/*
 * JarLibraryLoader.java
 *
 * Created on 26 April 2006, 09:05
 */

package com.alvermont.terraj.fracplanet.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Attempt to load native libraries from Jar files. Some of the code for this
 * came from:
 *
 * http://forum.java.sun.com/thread.jspa?forumID=406&messageID=2102477&threadID=439060
 *
 * I've modified it not to be specific to Windows. It's not a brilliant solution
 * but there seems to be little better that can be done given the difficulty
 * of platform independent native library loading.
 *
 * @author  martin
 * @version $Id: JarLibraryLoader.java,v 1.9 2006/07/06 07:46:59 martin Exp $
 */
public class JarLibraryLoader
{
    /** Our logger object */
    private static Log log = LogFactory.getLog(JarLibraryLoader.class);
    
    /** Creates a new instance of JarLibraryLoader */
    public JarLibraryLoader()
    {
    }
    
    /**
     * Get the name of a library file on this system given a base name.
     * 
     * @param name The base name of the library eg jogl
     * @return The name of the corresponding library file eg. libjogl.so on 
     * Linux
     */

    private static String getLibraryName(String name)
    {
        String platform = System.getProperty("os.name");

        String libname = "";
        
        if (platform.startsWith("Windows"))
        {
            // no prefix
        }
        else if (platform.equalsIgnoreCase("Mac OS"))
        {
            libname = "lib";
        }
        else
        {
            libname = "lib";
        }        
        
        return libname + name + getLibraryNameSuffix();
    }
    
    /**
     * Return the suffix (extension) of a library file on this system
     *
     * @return The name of a library file on this system e.g ".so" on Unix or
     * ".dll" on Windows.
     */
    
    private static String getLibraryNameSuffix()
    {
        String platform = System.getProperty("os.name");

        String suffix = "";
        
        if (platform.startsWith("Windows"))
        {
            suffix = ".dll";
        }
        else if (platform.equalsIgnoreCase("Mac OS"))
        {
            suffix = ".jnlib";
        }
        else
        {
            suffix = ".so";
        }        
        
        return suffix;
    }
    
    /**
     * Adds our extraction path to the system library path.
     *
     * @throws NoSuchFieldException if there is a reflection error
     * @throws IllegalAccessException if there is an access error during reflection
     */    
    public static void setupPath() throws NoSuchFieldException, IllegalAccessException
    {
        String dir = System.getProperty("user.home");

        dir = new File(dir).getPath();

        String newLibPath = dir + File.pathSeparator +
        System.getProperty("java.library.path");
        System.setProperty("java.library.path", newLibPath);

        Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
        fieldSysPath.setAccessible(true);
        if (fieldSysPath != null) 
        {
            fieldSysPath.set(System.class.getClassLoader(), null);
        }

        log.debug("Lib path: " + newLibPath);
    }
    
    /**
     * Get the name of a library resource for this platform
     * 
     * @param name The name of the library
     * @return The resource name to use to look up the library
     */
    private static String getLibraryResourceName(String name)
    {
        String platform = System.getProperty("os.name");
        
        log.debug("Platform is: " + platform);
        
        String path = "/lib/natives_";
        
        if (platform.startsWith("Windows"))
        {
            path += "win32/" + name + ".dll";
        }
        else if (platform.equalsIgnoreCase("Mac OS"))
        {
            path += "macos/lib" + name + ".jnlib";
        }
        else
        {
            path += platform.toLowerCase() + "/lib" + name + ".so";
        }
        
        log.debug("Path is: " + path);

        return path;
    }
    
    /**
     * Extracts a file from the jar so it can be found as a library. There
     * is no way to load a library directly from the jar file. Uses the 
     * users current directory to write the jar file, which might not work,
     * should probably fall back to the home dir. We can't use createTempFile
     * for this as we have to preserve the library filename so that dependencies
     * can be correctly resolved. The extracted file is marked for deletion on
     * exit.
     *
     * @param name The name of the file to be extracted
     * @throws IOException if there is an error extracting the library.
     */    
    public static File extractLibrary(String name) throws IOException
    {
        try
        {
            // Gets a stream to the library file

            File temporaryDll = null;
            
            InputStream inputStream = JarLibraryLoader.class.
                    getResourceAsStream(getLibraryResourceName(name));

            if (inputStream != null)
            {
                temporaryDll = new File(System.getProperty("user.home") + 
                        File.separator + name + getLibraryNameSuffix());

                String dir = System.getProperty("user.home");

                temporaryDll = new File(dir, getLibraryName(name));
                temporaryDll.deleteOnExit();

                FileOutputStream outputStream = new FileOutputStream(temporaryDll);

                byte[] array = new byte[8192];

                for (int i = inputStream.read(array); i != -1; i = inputStream.read(array))
                {
                    outputStream.write(array, 0, i);
                }

                outputStream.close();
            }

            return temporaryDll;
        }
        catch (IOException ioe)
        {
            log.error("IOException loading library", ioe);
            
            throw ioe;
        }        
    }
    
    /**
     * Loads a native library. The native library is assumed
     * to be in a platform specific subdirectory of the lib directory in the jar. 
     * The library is writen to a local temporary directory and (hopefully) 
     * deleted at JVM exit.
     *
     * Dll extraction code taken from Gabriele Piero Nizzoli
     * http://www.nizzoli.net/index.php?itemid=15
     *
     * @param name The base name of the library to be loaded
     * @throws IOException if there is a problem loading the library
     */
    public static void loadLibrary(String name) throws IOException
    {
        try
        {
            File libFile = extractLibrary(name);
            
            if (libFile != null)
            {
                log.debug("Calling System.load on: " + libFile.getPath());

                System.load(libFile.getPath());
            }
        }
        catch (IOException ioe)
        {
            log.error("IOException loading library", ioe);
            
            throw ioe;
        }        
    }
}
