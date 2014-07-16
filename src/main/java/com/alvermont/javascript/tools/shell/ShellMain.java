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
 * The Original Code is Rhino code, released
 * May 6, 1998.
 *
 * The Initial Developer of the Original Code is Netscape
 * Communications Corporation.  Portions created by Netscape are
 * Copyright (C) 1997-1999 Netscape Communications Corporation. All
 * Rights Reserved.
 *
 * Contributor(s):
 * Patrick Beard
 * Norris Boyd
 * Igor Bukanov
 * Rob Ginda
 * Kurt Westerfeld
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
 * NOTE THIS IS A MODIFIED VERSION FOR USE IN THIS PROGRAM UNDER THE GPL
 */
package com.alvermont.javascript.tools.shell;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.MalformedURLException;
import java.util.*;
import org.mozilla.javascript.*;
import org.mozilla.javascript.tools.ToolErrorReporter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The shell program.
 *
 * Can execute scripts interactively or in batch mode at the command line.
 * An example of controlling the JavaScript engine.
 *
 * @author Norris Boyd
 */
public class ShellMain
{
    /** Our logger object */
    private static Log log = LogFactory.getLog(ShellMain.class);

    // RequireThis OFF: log
    private static ShellContextFactory shellContextFactory =
        new ShellContextFactory();

    // RequireThis OFF: shellContextFactory
    public static void setShellContextFactory(
        ShellContextFactory newShellContextFactory)
    {
        shellContextFactory = newShellContextFactory;
    }

    protected ShellMain()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Proxy class to avoid proliferation of anonymous classes.
     */
    private static class IProxy implements ContextAction
    {
        private static final int PROCESS_FILES = 1;
        private static final int EVAL_INLINE_SCRIPT = 2;
        private int type;
        private String[] args;
        private String scriptText;

        IProxy(int type)
        {
            this.type = type;
        }

        public Object run(Context cx)
        {
            if (type == PROCESS_FILES)
            {
                processFiles(cx, args);
            }
            else if (type == EVAL_INLINE_SCRIPT)
            {
                final Script script =
                    loadScriptFromSource(cx, scriptText, "<command>", 1, null);

                if (script != null)
                {
                    evaluateScript(script, cx, getGlobal());
                }
            }
            else
            {
                throw Kit.codeBug();
            }

            return null;
        }
    }

    /**
     * ShellMain entry point.
     *
     * Process arguments as would a normal Java program. Also
     * create a new Context and associate it with the current thread.
     * Then set up the execution environment and begin to
     * execute scripts.
     */
    public static void main(String[] args)
    {
        try
        {
            if (Boolean.getBoolean("rhino.use_java_policy_security"))
            {
                initJavaPolicySecuritySupport();
            }
        }
        catch (SecurityException ex)
        {
            ex.printStackTrace(System.err);
        }

        final int result = exec(args);

        if (result != 0)
        {
            System.exit(result);
        }
    }

    /**
     *  Execute the given arguments, but don't System.exit at the end.
     */
    public static int exec(String[] origArgs)
    {
        setErrorReporter(new ToolErrorReporter(false, GLOBAL.getErr()));
        shellContextFactory.setErrorReporter(getErrorReporter());

        final String[] args = processOptions(origArgs);

        if (processStdin)
        {
            getFileList()
                .add(null);
        }

        if (!GLOBAL.isInitialized())
        {
            GLOBAL.init(shellContextFactory);
        }

        final IProxy iproxy = new IProxy(IProxy.PROCESS_FILES);
        iproxy.args = args;
        shellContextFactory.call(iproxy);

        return getExitCode();
    }

    static void processFiles(Context cx, String[] args)
    {
        // define "arguments" array in the top-level object:
        // need to allocate new array since newArray requires instances
        // of exactly Object[], not ObjectSubclass[]
        final Object[] array = new Object[args.length];
        System.arraycopy(args, 0, array, 0, args.length);

        final Scriptable argsObj = cx.newArray(GLOBAL, array);
        GLOBAL.defineProperty("arguments", argsObj, ScriptableObject.DONTENUM);

        for (int i = 0; i < getFileList()
                .size(); ++i)
        {
            processSource(cx, (String) getFileList().get(i));
        }
    }

    public static ShellGlobal getGlobal()
    {
        return GLOBAL;
    }

    /**
     * Parse arguments.
     */
    public static String[] processOptions(String[] args)
    {
        String usageError;

goodUsage: 

        for (int i = 0;; ++i)
        {
            if (i == args.length)
            {
                return new String[0];
            }

            final String arg = args[i];

            if (!arg.startsWith("-"))
            {
                processStdin = false;
                getFileList()
                    .add(arg);

                final String[] result = new String[args.length - i - 1];
                System.arraycopy(args, i + 1, result, 0, args.length - i - 1);

                return result;
            }

            if (arg.equals("-version"))
            {
                if (++i == args.length)
                {
                    usageError = arg;

                    break goodUsage;
                }

                int version;

                try
                {
                    version = Integer.parseInt(args[i]);
                }
                catch (NumberFormatException ex)
                {
                    usageError = args[i];

                    break goodUsage;
                }

                if (!Context.isValidLanguageVersion(version))
                {
                    usageError = args[i];

                    break goodUsage;
                }

                shellContextFactory.setLanguageVersion(version);

                continue;
            }

            if (arg.equals("-opt") || arg.equals("-O"))
            {
                if (++i == args.length)
                {
                    usageError = arg;

                    break goodUsage;
                }

                int opt;

                try
                {
                    opt = Integer.parseInt(args[i]);
                }
                catch (NumberFormatException ex)
                {
                    usageError = args[i];

                    break goodUsage;
                }

                if (opt == -2)
                {
                    // Compatibility with Cocoon Rhino fork
                    opt = -1;
                }
                else if (!Context.isValidOptimizationLevel(opt))
                {
                    usageError = args[i];

                    break goodUsage;
                }

                shellContextFactory.setOptimizationLevel(opt);

                continue;
            }

            if (arg.equals("-strict"))
            {
                shellContextFactory.setStrictMode(true);

                continue;
            }

            if (arg.equals("-e"))
            {
                processStdin = false;

                if (++i == args.length)
                {
                    usageError = arg;

                    break goodUsage;
                }

                if (!GLOBAL.isInitialized())
                {
                    GLOBAL.init(shellContextFactory);
                }

                final IProxy iproxy = new IProxy(IProxy.EVAL_INLINE_SCRIPT);
                iproxy.scriptText = args[i];
                shellContextFactory.call(iproxy);

                continue;
            }

            if (arg.equals("-w"))
            {
                getErrorReporter()
                    .setIsReportingWarnings(true);

                continue;
            }

            if (arg.equals("-f"))
            {
                processStdin = false;

                if (++i == args.length)
                {
                    usageError = arg;

                    break goodUsage;
                }

                if (args[i] != null)
                {
                    getFileList()
                        .add(args[i]);
                }
                else
                {
                    getFileList()
                        .add(null);
                }

                continue;
            }

            if (arg.equals("-sealedlib"))
            {
                GLOBAL.setSealedStdLib(true);

                continue;
            }

            usageError = arg;

            break goodUsage;
        }

        // print usage message
        GLOBAL.getOut()
            .println(
                ToolErrorReporter.getMessage("msg.shell.usage", usageError));
        System.exit(1);

        return null;
    }

    private static void initJavaPolicySecuritySupport()
    {
        Throwable exObj;

        try
        {
            final Class cl =
                Class.forName(
                    "org.mozilla.javascript.tools.shell.JavaPolicySecurity");
            securityImpl = (ShellSecurityProxy) cl.newInstance();
            SecurityController.initGlobal(securityImpl);

            return;
        }
        catch (ClassNotFoundException ex)
        {
            exObj = ex;
        }
        catch (IllegalAccessException ex)
        {
            exObj = ex;
        }
        catch (InstantiationException ex)
        {
            exObj = ex;
        }
        catch (LinkageError ex)
        {
            exObj = ex;
        }

        throw Kit.initCause(
            new IllegalStateException(
                "Can not load security support: " + exObj), exObj);
    }

    /**
     * Evaluate JavaScript source.
     *
     * @param cx the current context
     * @param filename the name of the file to compile, or null
     *                 for interactive mode.
     */
    public static void processSource(Context cx, String filename)
    {
        if (filename == null || filename.equals("-"))
        {
            final PrintStream ps = GLOBAL.getErr();

            if (filename == null)
            {
                // print implementation version
                ps.println(cx.getImplementationVersion());
            }

            // Use the interpreter for interactive input
            cx.setOptimizationLevel(-1);

            final BufferedReader in =
                new BufferedReader(new InputStreamReader(GLOBAL.getIn()));

            int lineno = 1;
            boolean hitEOF = false;

            while (!hitEOF)
            {
                final int startline = lineno;

                if (filename == null)
                {
                    ps.print("js> ");
                }

                ps.flush();

                String source = "";

                // Collect lines of source to compile.
                while (true)
                {
                    String newline;

                    try
                    {
                        newline = in.readLine();
                    }
                    catch (IOException ioe)
                    {
                        ps.println(ioe.toString());

                        break;
                    }

                    if (newline == null)
                    {
                        hitEOF = true;

                        break;
                    }

                    source = source + newline + "\n";
                    ++lineno;

                    if (cx.stringIsCompilableUnit(source))
                    {
                        break;
                    }
                }

                final Script script =
                    loadScriptFromSource(cx, source, "<stdin>", lineno, null);

                if (script != null)
                {
                    final Object result = evaluateScript(script, cx, GLOBAL);

                    if (result != Context.getUndefinedValue())
                    {
                        try
                        {
                            ps.println(Context.toString(result));
                        }
                        catch (RhinoException rex)
                        {
                            ToolErrorReporter.reportException(
                                cx.getErrorReporter(), rex);
                        }
                    }

                    final NativeArray h = GLOBAL.getHistory();
                    h.put((int) h.getLength(), h, source);
                }
            }

            ps.println();
        }
        else
        {
            processFile(cx, GLOBAL, filename);
        }

        System.gc();
    }

    public static void processFile(
        Context cx, Scriptable scope, String filename)
    {
        if (securityImpl == null)
        {
            processFileSecure(cx, scope, filename, null);
        }
        else
        {
            securityImpl.callProcessFileSecure(cx, scope, filename);
        }
    }

    static void processFileSecure(
        Context cx, Scriptable scope, String path, Object securityDomain)
    {
        Script script;

        if (path.endsWith(".class"))
        {
            script = loadCompiledScript(cx, path, securityDomain);
        }
        else
        {
            String source = (String) readFileOrUrl(path, true);

            if (source == null)
            {
                exitCode = EXITCODE_FILE_NOT_FOUND;

                return;
            }

            // Support the executable script #! syntax:  If
            // the first line begins with a '#', treat the whole
            // line as a comment.
            if (source.length() > 0 && source.charAt(0) == '#')
            {
                for (int i = 1; i != source.length(); ++i)
                {
                    final int c = source.charAt(i);

                    if (c == '\n' || c == '\r')
                    {
                        source = source.substring(i);

                        break;
                    }
                }
            }

            script = loadScriptFromSource(cx, source, path, 1, securityDomain);
        }

        if (script != null)
        {
            evaluateScript(script, cx, scope);
        }
    }

    public static Script loadScriptFromSource(
        Context cx, String scriptSource, String path, int lineno,
        Object securityDomain)
    {
        try
        {
            return cx.compileString(scriptSource, path, lineno, securityDomain);
        }
        catch (EvaluatorException ee)
        {
            // Already printed message.
            exitCode = EXITCODE_RUNTIME_ERROR;
        }
        catch (RhinoException rex)
        {
            ToolErrorReporter.reportException(cx.getErrorReporter(), rex);
            exitCode = EXITCODE_RUNTIME_ERROR;
        }
        catch (VirtualMachineError ex)
        {
            // Treat StackOverflow and OutOfMemory as runtime errors
            ex.printStackTrace();

            final String msg =
                ToolErrorReporter.getMessage(
                    "msg.uncaughtJSException", ex.toString());
            exitCode = EXITCODE_RUNTIME_ERROR;
            Context.reportError(msg);
        }

        return null;
    }

    private static Script loadCompiledScript(
        Context cx, String path, Object securityDomain)
    {
        final byte[] data = (byte[]) readFileOrUrl(path, false);

        if (data == null)
        {
            exitCode = EXITCODE_FILE_NOT_FOUND;

            return null;
        }

        // XXX: For now extract class name of compiled Script from path
        // instead of parsing class bytes
        int nameStart = path.lastIndexOf('/');

        if (nameStart < 0)
        {
            nameStart = 0;
        }
        else
        {
            ++nameStart;
        }

        int nameEnd = path.lastIndexOf('.');

        if (nameEnd < nameStart)
        {
            // '.' does not exist in path (nameEnd < 0)
            // or it comes before nameStart
            nameEnd = path.length();
        }

        final String name = path.substring(nameStart, nameEnd);

        try
        {
            final GeneratedClassLoader loader =
                SecurityController.createLoader(
                    cx.getApplicationClassLoader(), securityDomain);
            final Class clazz = loader.defineClass(name, data);
            loader.linkClass(clazz);

            if (!Script.class.isAssignableFrom(clazz))
            {
                throw Context.reportRuntimeError("msg.must.implement.Script");
            }

            return (Script) clazz.newInstance();
        }
        catch (RhinoException rex)
        {
            ToolErrorReporter.reportException(cx.getErrorReporter(), rex);
            exitCode = EXITCODE_RUNTIME_ERROR;
        }
        catch (IllegalAccessException iaex)
        {
            exitCode = EXITCODE_RUNTIME_ERROR;
            Context.reportError(iaex.toString());
        }
        catch (InstantiationException inex)
        {
            exitCode = EXITCODE_RUNTIME_ERROR;
            Context.reportError(inex.toString());
        }

        return null;
    }

    public static Object evaluateScript(
        Script script, Context cx, Scriptable scope)
    {
        try
        {
            return script.exec(cx, scope);
        }
        catch (RhinoException rex)
        {
            ToolErrorReporter.reportException(cx.getErrorReporter(), rex);
            exitCode = EXITCODE_RUNTIME_ERROR;
        }
        catch (VirtualMachineError ex)
        {
            // Treat StackOverflow and OutOfMemory as runtime errors
            ex.printStackTrace();

            final String msg =
                ToolErrorReporter.getMessage(
                    "msg.uncaughtJSException", ex.toString());
            exitCode = EXITCODE_RUNTIME_ERROR;
            Context.reportError(msg);
        }

        return Context.getUndefinedValue();
    }

    public static InputStream getIn()
    {
        return getGlobal()
            .getIn();
    }

    public static void setIn(InputStream in)
    {
        getGlobal()
            .setIn(in);
    }

    public static PrintStream getOut()
    {
        return getGlobal()
            .getOut();
    }

    public static void setOut(PrintStream out)
    {
        getGlobal()
            .setOut(out);
    }

    public static PrintStream getErr()
    {
        return getGlobal()
            .getErr();
    }

    public static void setErr(PrintStream err)
    {
        getGlobal()
            .setErr(err);
    }

    /**
     * Read file or url specified by <tt>path</tt>.
     * @return file or url content as <tt>byte[]</tt> or as <tt>String</tt> if
     * <tt>convertToString</tt> is true.
     */
    private static Object readFileOrUrl(String path, boolean convertToString)
    {
        URL url = null;

        // Assume path is URL if it contains dot and there are at least
        // 2 characters in the protocol part. The later allows under Windows
        // to interpret paths with driver letter as file, not URL.
        if (path.indexOf(':') >= 2)
        {
            try
            {
                url = new URL(path);
            }
            catch (MalformedURLException ex)
            {
                log.debug("MalformedURLException in readFileOrUrl", ex);
            }
        }

        InputStream is = null;
        int capacityHint = 0;

        if (url == null)
        {
            final File file = new File(path);
            capacityHint = (int) file.length();

            try
            {
                is = new FileInputStream(file);
            }
            catch (IOException ex)
            {
                Context.reportError(
                    ToolErrorReporter.getMessage("msg.couldnt.open", path));

                return null;
            }
        }
        else
        {
            try
            {
                final URLConnection uc = url.openConnection();
                is = uc.getInputStream();
                capacityHint = uc.getContentLength();

                // Ignore insane values for Content-Length
                if (capacityHint > (1 << 20))
                {
                    capacityHint = -1;
                }
            }
            catch (IOException ex)
            {
                Context.reportError(
                    ToolErrorReporter.getMessage(
                        "msg.couldnt.open.url", url.toString(), ex.toString()));

                return null;
            }
        }

        if (capacityHint <= 0)
        {
            capacityHint = 4096;
        }

        byte[] data;

        try
        {
            try
            {
                data = Kit.readStream(is, capacityHint);
            }
            finally
            {
                is.close();
            }
        }
        catch (IOException ex)
        {
            Context.reportError(ex.toString());

            return null;
        }

        Object result;

        if (!convertToString)
        {
            result = data;
        }
        else
        {
            // Convert to String using the default encoding
            // XXX: Use 'charset=' argument of Content-Type if URL?
            result = new String(data);
        }

        return result;
    }

    /** The global object */
    protected static final ShellGlobal GLOBAL = new ShellGlobal();
    private static ToolErrorReporter errorReporter;
    private static int exitCode = 0;
    private static final int EXITCODE_RUNTIME_ERROR = 3;
    private static final int EXITCODE_FILE_NOT_FOUND = 4;
    static boolean processStdin = true;
    private static List<String> fileList = new ArrayList<String>(5);
    private static ShellSecurityProxy securityImpl;

    public static ToolErrorReporter getErrorReporter()
    {
        return errorReporter;
    }

    public static void setErrorReporter(ToolErrorReporter aErrorReporter)
    {
        errorReporter = aErrorReporter;
    }

    public static int getExitCode()
    {
        return exitCode;
    }

    public static List<String> getFileList()
    {
        return fileList;
    }
}
