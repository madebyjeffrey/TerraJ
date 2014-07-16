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
 * JNLPFileChooser.java
 *
 * Created on 08 March 2006, 09:55
 */
package com.alvermont.terraj.util.ui;

import com.alvermont.terraj.fracplanet.io.FileUtils;
import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
//import javax.jnlp.FileContents;
//import javax.jnlp.FileOpenService;
//import javax.jnlp.JNLPRandomAccessFile;
//import javax.jnlp.ServiceManager;
//import javax.jnlp.UnavailableServiceException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Attempt to encapsulate a file chooser (which doesn't work in JNLP)
 * and hide the fact that we're not running as a normal app. Let's hope
 * it works. It's really annoying that an application is expected to use
 * completely different APIs when running under Java Web Start.
 *
 * It more or less works but runs up against problems and design silliness in
 * the JNLP facilities.
 *
 * Note: I have currently disabled JNLP support in this file as I doubt that 
 * the JNLP jar file can be distributed in a manner compatible with a GPL
 * application. As this jar file is required to build with JNLP I wanted to
 * bundle it with the source. The application as supplied did not make use of 
 * JNLP so this has not currently affected any functionality. I may review this
 * in future.
 *
 * @author  martin
 * @version $Id: JNLPFileChooser.java,v 1.7 2006/07/06 06:58:34 martin Exp $
 */
public class JNLPFileChooser
{
    /** Our logger object */
    private static Log log = LogFactory.getLog(JNLPFileChooser.class);
    
    /** Our file chooser object */
    protected JFileChooser chooser;
    /** Our file contents object */
    
    // protected FileContents fileContents;
    
    protected MyFileContents fileContents;

    /**
     * Dummy implementation of FileContents wrapping a file to try and
     * unify the JNLP approach with standard file handling.
     */
    //public class MyFileContents implements FileContents
    public class MyFileContents 
    {
        /** The file that this object is wrapping */
        protected File file;

        /**
         * Create a new instance of MyFileContents
         * 
         * @param file The file object to be wrapped
         */
        public MyFileContents(File file)
        {
            this.file = file;
        }

        /**
         * Get the file name of the file contents object
         * 
         * @throws java.io.IOException If there is an error getting the name
         *
         * @return The name of the file
         */
        public String getName() throws IOException
        {
            return file.getName();
        }

        /**
         * Get an input stream to the file wrapped by this object
         * 
         * @throws java.io.IOException If there is an error getting the stream
         *
         * @return An <code>InputStream</code> to the wrapped file
         */
        public InputStream getInputStream() throws IOException
        {
            return new FileInputStream(file);
        }

        /**
         * Get an output stream to the wrapped file
         * 
         * @param b If <pre>true</pre> then the file should be overwritten if it exists
         * @throws java.io.IOException If there is an error getting the stream
         * @return An <code>OutputStream</code> to the wrapped file
         */
        public OutputStream getOutputStream(boolean b)
            throws IOException
        {
            return new FileOutputStream(file);
        }

        /**
         * Get the length of the wrapped file 
         *
         * @throws java.io.IOException If there is an error getting the information
         * @return The length of the file in bytes
         */
        public long getLength() throws IOException
        {
            return new RandomAccessFile(file, "r").length();
        }

        /**
         * Test whether the wrapped file exists. This isn't part of the JNLP
         * interface, which is another right royal pain. 
         *
         * @throws java.io.IOException If there is an error getting the information
         * @return <pre>true</pre> if the file exists, otherwise false
         */
        public boolean exists() throws IOException
        {
            return file.exists();
        }

        /**
         * Tests whether the file can be read 
         *
         * @throws java.io.IOException If there is an error getting the information
         * @return <pre>true</pre> if the file can be read, otherwise false
         */
        public boolean canRead() throws IOException
        {
            return file.canRead();
        }

        /**
         * Tests whether the file can be written 
         *
         * @throws java.io.IOException If there is an error getting the information
         * @return <pre>true</pre> if the file can be written, otherwise false
         */
        public boolean canWrite() throws IOException
        {
            return file.canWrite();
        }

//        /**
//         * Get a random access file to the underlying file. Not currently supported
//         * 
//         * @param string The requested access mode to the file
//         * @throws java.io.IOException If there is an error getting the information
//         * @return A new random access file object
//         */
//        public JNLPRandomAccessFile getRandomAccessFile(String string)
//            throws IOException
//        {
//            throw new UnsupportedOperationException(
//                "Random access not currently supported");
//        }

        /**
         * Get the maximum length that can be written 
         *
         * @throws java.io.IOException If there is an error getting the information
         * @return The maximum number of bytes that can be written to the file
         */
        public long getMaxLength() throws IOException
        {
            return Integer.MAX_VALUE;
        }

        /**
         * Set the maximum number of bytes that can be written 
         *
         * @param l The requested maximum size in bytes
         * @throws java.io.IOException If there is an error getting the information
         * @return The new allowed maximum size in bytes
         */
        public long setMaxLength(long l) throws IOException
        {
            // we ignore this as we're using this object outside a JNLP
            // context. IMHO they should have tried to integrate JNLP file
            // access with normal file access by popping up a different
            // file selector without the app having to jump through hoops
            return Integer.MAX_VALUE;
        }
    }

    private void init()
    {
        try
        {
            chooser = new JFileChooser();
        }
        catch (SecurityException se)
        {
            log.error(
                "SecurityException creating JFileChooser assuming JNLP", se);
        }
    }

    /**
     * Creates a new instance of JNLPFileChooser
     *
     * @param defaultExtensions The list of extensions to be accepted
     */
    public JNLPFileChooser(List<String> defaultExtensions)
    {
        this.defaultExtensions = defaultExtensions;

        init();
    }

    /**
     * Creates a new instance of JNLPFileChooser
     *
     * @param defaultExtension The default extension for the chooser
     */
    public JNLPFileChooser(String defaultExtension)
    {
        defaultExtensions.add(defaultExtension);

        init();
    }

    /**
     * Add a new choosable file filter to this object 
     *
     * @param filter The new filter to be added
     */
    public void addChoosableFileFilter(FileFilter filter)
    {
        if (chooser != null)
        {
            chooser.addChoosableFileFilter(filter);
        }
    }

    /**
     * Add an extension to a file, we use the first default one we have
     * 
     * @param f The file to add an extension to
     * @return A new file object with a possibly different extension
     */
    protected File addExtension(File f)
    {
        if (defaultExtensions.size() > 0)
        {
            final String extension = defaultExtensions.get(0);

            return FileUtils.addExtension(f, extension);
        }
        else
        {
            // no default, leave it alone
            return f;
        }
    }

    /**
     * Carry out the file open dialog
     * 
     * @param parent The parent for the dialog
     * @return The result of the user selection
     */
    public int showOpenDialog(Component parent)
    {
//        if (chooser != null)
//        {
            int option = chooser.showOpenDialog(parent);

            if (option == JFileChooser.APPROVE_OPTION)
            {
                File target = addExtension(chooser.getSelectedFile());

                fileContents = new MyFileContents(target);
            }
            else
            {
                fileContents = null;
            }
//        }
//        else
//        {
//            FileOpenService fos;
//
//            try
//            {
//                fos = (FileOpenService) ServiceManager.lookup(
//                        "javax.jnlp.FileOpenService");
//
//                try
//                {
//                    String[] extensions = new String[defaultExtensions.size()];
//
//                    defaultExtensions.toArray(extensions);
//
//                    fileContents = fos.openFileDialog(null, extensions);
//                }
//                catch (IOException ex)
//                {
//                    log.error("IOException in open file selection", ex);
//
//                    return JFileChooser.ERROR_OPTION;
//                }
//            }
//            catch (UnavailableServiceException e)
//            {
//                // we assume that JNLP is not available and some other security manager
//                // related reason applies
//                log.error(
//                    "Couldn't create a file selector or lookup JNLP service", e);
//
//                throw new SecurityException(
//                    "Could not create file selector or use JNLP service", e);
//            }
//        }

        if (fileContents != null)
        {
            return JFileChooser.APPROVE_OPTION;
        }
        else
        {
            return JFileChooser.CANCEL_OPTION;
        }
    }

    /**
     * Carry out the file save dialog
     * 
     * @param parent The parent for the dialog
     * @return The result of the user selection
     */
    public int showSaveDialog(Component parent)
    {
//        if (chooser != null)
//        {
            int option = chooser.showSaveDialog(parent);

            if (option == JFileChooser.APPROVE_OPTION)
            {
                File target = addExtension(chooser.getSelectedFile());

                fileContents = new MyFileContents(target);
            }
            else
            {
                fileContents = null;
            }
//        }
//        else
//        {
//            // NOTE: we use the FileOpenService here instead of FileSaveService
//            // because I couldn't get the latter to work. I just got
//            // StreamClosed exceptions thrown all the time.
//            FileOpenService fos;
//
//            try
//            {
//                fos = (FileOpenService) ServiceManager.lookup(
//                        "javax.jnlp.FileOpenService");
//
//                try
//                {
//                    String[] extensions = new String[defaultExtensions.size()];
//
//                    defaultExtensions.toArray(extensions);
//
//                    fileContents = fos.openFileDialog(null, extensions);
//                }
//                catch (IOException ex)
//                {
//                    log.error("IOException in open file selection", ex);
//
//                    return JFileChooser.ERROR_OPTION;
//                }
//            }
//            catch (UnavailableServiceException e)
//            {
//                // we assume that JNLP is not available and some other security manager
//                // related reason applies
//                log.error(
//                    "Couldn't create a file selector or lookup JNLP service", e);
//
//                throw new SecurityException(
//                    "Could not create file selector or use JNLP service", e);
//            }
//        }

        if (fileContents != null)
        {
            return JFileChooser.APPROVE_OPTION;
        }
        else
        {
            return JFileChooser.CANCEL_OPTION;
        }
    }

    /**
     * Get the file contents object resulting from the user selection
     * 
     * @return The file contents object for the file the user selected
     */
    public MyFileContents getFileContents()
    {
        //if (chooser != null)
        //{
            return new MyFileContents(chooser.getSelectedFile());
        //}
        //else
        //{
        //    return fileContents;
        //}
    }

    /**
     * Holds value of property defaultExtensions.
     */
    private java.util.List<String> defaultExtensions = new ArrayList<String>();

    /**
     * Getter for property defaultExtension.
     * @return Value of property defaultExtension.
     */
    public java.util.List<String> getDefaultExtensions()
    {
        return this.defaultExtensions;
    }
}
