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
 * The Original Code is Rhino JavaScript Debugger code, released
 * November 21, 2000.
 *
 * The Initial Developer of the Original Code is See Beyond Corporation.
 *
 * Portions created by See Beyond are
 * Copyright (C) 2000 See Beyond Communications Corporation. All
 * Rights Reserved.
 *
 * Contributor(s):
 * Christopher Oliver
 *
 * Alternatively, the contents of this file may be used under the
 * terms of the GNU Public License (the "GPL"), in which case the
 * provisions of the GPL are applicable instead of those above.
 * If you wish to allow use of your version of this file only
 * under the terms of the GPL and not to allow others to use your
 * version of this file under the NPL, indicate your decision by
 * deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL.  If you do not delete
 * the provisions above, a recipient may use your version of this
 * file under either the NPL or the GPL.
 *
 * NOTE: THIS IS A MODIFIED VERSION FOR USE WITH THIS PROGRAM UNDER THE
 * GPL
 */
package com.alvermont.javascript.tools.shell;

import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import org.mozilla.javascript.tools.shell.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ShellJSConsole extends JFrame implements ActionListener
{
    /** Our logger object */
    private static Log log = LogFactory.getLog(ShellJSConsole.class);

    // RequireThis OFF: log
    private Thread shellThread;
    private String[] args;
    static final long serialVersionUID = 2551225560631876300L;
    private File cwd;
    private JFileChooser dlg;
    private ShellConsoleTextArea consoleTextArea;

    public String chooseFile()
    {
        if (this.cwd == null)
        {
            final String dir = System.getProperty("user.dir");

            if (dir != null)
            {
                this.cwd = new File(dir);
            }
        }

        if (this.cwd != null)
        {
            this.dlg.setCurrentDirectory(this.cwd);
        }

        this.dlg.setDialogTitle("Select a file to load");

        final int returnVal = this.dlg.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            final String result = this.dlg.getSelectedFile()
                    .getPath();
            this.cwd = new File(this.dlg.getSelectedFile().getParent());

            return result;
        }

        return null;
    }

    private class ShellRunnable implements Runnable
    {
        private String[] args;

        public ShellRunnable(String[] args)
        {
            this.args = args;
        }

        public void run()
        {
            ShellMain.exec(this.args);
        }
    }

    public static void main(String[] args)
    {
        final ShellJSConsole console = new ShellJSConsole(args);
    }

    /**
     * Start the shell execution thread
     */
    public void startShell()
    {
        this.shellThread = new Thread(
                new ShellRunnable(this.args), "JSShellExecutionThread");
        this.shellThread.setDaemon(true);
        this.shellThread.start();
    }

    /**
     * Interrupt the shell execution thread
     *
     * Note I don't think this will work because the thread is blocked
     * in readLine() and can't be interrupted. This method is here in case
     * it ever gets switched over to nio using interruptible channels
     */
    public void stopShell()
    {
        this.shellThread.interrupt();
    }

    public void createFileChooser()
    {
        this.dlg = new JFileChooser();

        javax.swing.filechooser.FileFilter filter =
            new javax.swing.filechooser.FileFilter()
            {
                public boolean accept(File f)
                {
                    boolean accepted = false;

                    if (f.isDirectory())
                    {
                        accepted = true;
                    }
                    else
                    {
                        final String name = f.getName();

                        if (name.toLowerCase()
                                .endsWith(".js"))
                        {
                            accepted = true;
                        }
                    }

                    return accepted;
                }

                public String getDescription()
                {
                    return "JavaScript Files (*.js)";
                }
            };

        this.dlg.addChoosableFileFilter(filter);
    }

    public ShellJSConsole(String[] args)
    {
        super("Rhino JavaScript Console");

        final JMenuBar menubar = new JMenuBar();
        createFileChooser();

        final String[] fileItems = { "Load...", "Close" };
        final String[] fileCmds = { "Load", "Close" };
        final char[] fileShortCuts = { 'L', 'X' };
        final String[] editItems = { "Cut", "Copy", "Paste" };
        final char[] editShortCuts = { 'T', 'C', 'P' };
        final JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');

        final JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic('E');

        for (int i = 0; i < fileItems.length; ++i)
        {
            final JMenuItem item =
                new JMenuItem(fileItems[i], fileShortCuts[i]);
            item.setActionCommand(fileCmds[i]);
            item.addActionListener(this);
            fileMenu.add(item);
        }

        for (int i = 0; i < editItems.length; ++i)
        {
            final JMenuItem item =
                new JMenuItem(editItems[i], editShortCuts[i]);
            item.addActionListener(this);
            editMenu.add(item);
        }

        final ButtonGroup group = new ButtonGroup();
        menubar.add(fileMenu);
        menubar.add(editMenu);
        setJMenuBar(menubar);
        this.consoleTextArea = new ShellConsoleTextArea(args);

        final JScrollPane scroller = new JScrollPane(this.consoleTextArea);
        setContentPane(scroller);
        this.consoleTextArea.setRows(24);
        this.consoleTextArea.setColumns(80);
        addWindowListener(
            new WindowAdapter()
            {
                public void windowClosing(WindowEvent e)
                {
                    setVisible(false);
                }
            });
        pack();
        setVisible(true);
        // System.setIn(consoleTextArea.getIn());
        // System.setOut(consoleTextArea.getOut());
        // System.setErr(consoleTextArea.getErr());
        ShellMain.setIn(this.consoleTextArea.getIn());
        ShellMain.setOut(this.consoleTextArea.getOut());
        ShellMain.setErr(this.consoleTextArea.getErr());

        // we don't do this here as we want to separate construction from
        // the run thread
        this.args = args;

        //ShellMain.exec(args);
    }

    public void actionPerformed(ActionEvent e)
    {
        final String cmd = e.getActionCommand();

        if (cmd.equals("Load"))
        {
            String f = chooseFile();

            if (f != null)
            {
                f = f.replace('\\', '/');
                this.consoleTextArea.eval("load(\"" + f + "\");");
            }
        }
        else if (cmd.equals("Close"))
        {
            log.debug("Closing shell");

            setVisible(false);
        }
        else if (cmd.equals("Cut"))
        {
            this.consoleTextArea.cut();
        }
        else if (cmd.equals("Copy"))
        {
            this.consoleTextArea.copy();
        }
        else if (cmd.equals("Paste"))
        {
            this.consoleTextArea.paste();
        }
        else
        {
            // add new commands here
            log.debug("Unexpected actionPerformed() : " + cmd);
        }
    }
}
