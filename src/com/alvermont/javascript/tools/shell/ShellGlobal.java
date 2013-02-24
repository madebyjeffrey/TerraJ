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

/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * The contents of this file are subject to the Netscape Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/NPL
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
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
 * Igor Bukanov
 * Norris Boyd
 * Rob Ginda
 * Kurt Westerfeld
 * Matthias Radestock
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
import java.net.*;
import java.lang.reflect.*;
import org.mozilla.javascript.*;
import org.mozilla.javascript.tools.ToolErrorReporter;
import org.mozilla.javascript.serialize.*;

/**
 * This class provides for sharing functions across multiple threads.
 * This is of particular interest to server applications.
 *
 * @author Norris Boyd
 */
public class ShellGlobal extends ImporterTopLevel
{
    static final long serialVersionUID = 4029130780977538005L;

    /** Creates a new instance of ShellGlobal */
    public ShellGlobal()
    {
    }

    /**
     * Creates a new instance of ShellGlobal
     *
     * @param cx The context to be used
     */
    public ShellGlobal(Context cx)
    {
        init(cx);
    }

    /**
     * Initlialize the object using a context factory
     *
     * @param factory The factory to be used
     */
    public void init(ContextFactory factory)
    {
        factory.call(
            new ContextAction()
            {
                public Object run(Context cx)
                {
                    init(cx);

                    return null;
                }
            });
    }

    /**
     * Initialize the object using a context
     *
     * @param cx The context to be used
     */
    public void init(Context cx)
    {
        // Define some global functions particular to the shell. Note
        // that these functions are not part of ECMA.
        initStandardObjects(cx, sealedStdLib);

        final String[] names =
            {
                "print", "quit", "version", "load", "help", "loadClass",
                "defineClass", "spawn", "sync", "serialize", "deserialize",
                "runCommand", "seal", "readFile", "readUrl"
            };
        defineFunctionProperties(
            names, ShellGlobal.class, ScriptableObject.DONTENUM);

        // Set up "environment" in the global scope to provide access to the
        // System environment variables.
        ShellEnvironment.defineClass(this);

        final ShellEnvironment environment = new ShellEnvironment(this);
        defineProperty("environment", environment, ScriptableObject.DONTENUM);

        history = (NativeArray) cx.newArray(this, 0);
        defineProperty("history", getHistory(), ScriptableObject.DONTENUM);
        initialized = true;
    }

    /**
     * Print a help message.
     *
     * This method is defined as a JavaScript function.
     *
     * @param cx The context to be used
     * @param thisObj The current scriptable object
     * @param args The arguments
     * @param funObj The function object
     */
    public static void help(
        Context cx, Scriptable thisObj, Object[] args, Function funObj)
    {
        final PrintStream out = getInstance(funObj)
                .getOut();
        out.println(ToolErrorReporter.getMessage("msg.help"));
    }

    /**
     * Print the string values of its arguments.
     *
     * This method is defined as a JavaScript function.
     * Note that its arguments are of the "varargs" form, which
     * allows it to handle an arbitrary number of arguments
     * supplied to the JavaScript function.
     *
     */
    public static Object print(
        Context cx, Scriptable thisObj, Object[] args, Function funObj)
    {
        final PrintStream out = getInstance(funObj)
                .getOut();

        for (int i = 0; i < args.length; ++i)
        {
            if (i > 0)
            {
                out.print(" ");
            }

            // Convert the arbitrary JavaScript value into a string form.
            final String s = Context.toString(args[i]);

            out.print(s);
        }

        out.println();

        return Context.getUndefinedValue();
    }

    /**
     * Quit the shell.
     *
     * This only affects the interactive mode.
     *
     * This method is defined as a JavaScript function.
     */
    public static void quit(
        Context cx, Scriptable thisObj, Object[] args, Function funObj)
    {
        int exitCode = 0;

        if (args.length > 0)
        {
            exitCode = (int) Context.toNumber(args[0]);
        }

        System.exit(exitCode);
    }

    /**
     * Get and set the language version.
     *
     * This method is defined as a JavaScript function.
     */
    public static double version(
        Context cx, Scriptable thisObj, Object[] args, Function funObj)
    {
        final double result = (double) cx.getLanguageVersion();

        if (args.length > 0)
        {
            final double d = Context.toNumber(args[0]);
            cx.setLanguageVersion((int) d);
        }

        return result;
    }

    /**
     * Load and execute a set of JavaScript source files.
     *
     * This method is defined as a JavaScript function.
     *
     */
    public static void load(
        Context cx, Scriptable thisObj, Object[] args, Function funObj)
    {
        for (int i = 0; i < args.length; ++i)
        {
            ShellMain.processFile(cx, thisObj, Context.toString(args[i]));
        }
    }

    /**
     * Load a Java class that defines a JavaScript object using the
     * conventions outlined in ScriptableObject.defineClass.
     * <p>
     * This method is defined as a JavaScript function.
     * @exception IllegalAccessException if access is not available
     *            to a reflected class member
     * @exception InstantiationException if unable to instantiate
     *            the named class
     * @exception InvocationTargetException if an exception is thrown
     *            during execution of methods of the named class
     * @exception ClassDefinitionException if the format of the
     *            class causes this exception in ScriptableObject.defineClass
     * @see org.mozilla.javascript.ScriptableObject#defineClass
     */
    public static void defineClass(
        Context cx, Scriptable thisObj, Object[] args, Function funObj)
        throws IllegalAccessException, InstantiationException, 
            InvocationTargetException
    {
        final Class clazz = getClass(args);
        ScriptableObject.defineClass(thisObj, clazz);
    }

    /**
     * Load and execute a script compiled to a class file.
     * <p>
     * This method is defined as a JavaScript function.
     * When called as a JavaScript function, a single argument is
     * expected. This argument should be the name of a class that
     * implements the Script interface, as will any script
     * compiled by jsc.
     *
     * @exception IllegalAccessException if access is not available
     *            to the class
     * @exception InstantiationException if unable to instantiate
     *            the named class
     * @exception InvocationTargetException if an exception is thrown
     *            during execution of methods of the named class
     * @see org.mozilla.javascript.ScriptableObject#defineClass
     */
    public static void loadClass(
        Context cx, Scriptable thisObj, Object[] args, Function funObj)
        throws IllegalAccessException, InstantiationException, 
            InvocationTargetException
    {
        final Class clazz = getClass(args);

        if (!Script.class.isAssignableFrom(clazz))
        {
            throw reportRuntimeError("msg.must.implement.Script");
        }

        final Script script = (Script) clazz.newInstance();
        script.exec(cx, thisObj);
    }

    private static Class getClass(Object[] args)
        throws IllegalAccessException, InstantiationException, 
            InvocationTargetException
    {
        if (args.length == 0)
        {
            throw reportRuntimeError("msg.expected.string.arg");
        }

        final Object arg0 = args[0];

        if (arg0 instanceof Wrapper)
        {
            final Object wrapped = ((Wrapper) arg0).unwrap();

            if (wrapped instanceof Class)
            {
                return (Class) wrapped;
            }
        }

        final String className = Context.toString(args[0]);

        try
        {
            return Class.forName(className);
        }
        catch (ClassNotFoundException cnfe)
        {
            throw reportRuntimeError("msg.class.not.found", className);
        }
    }

    public static void serialize(
        Context cx, Scriptable thisObj, Object[] args, Function funObj)
        throws IOException
    {
        if (args.length < 2)
        {
            throw Context.reportRuntimeError(
                "Expected an object to serialize and a filename to write " +
                "the serialization to");
        }

        final Object obj = args[0];
        final String filename = Context.toString(args[1]);
        final FileOutputStream fos = new FileOutputStream(filename);
        final Scriptable scope = ScriptableObject.getTopLevelScope(thisObj);
        final ScriptableOutputStream out =
            new ScriptableOutputStream(fos, scope);
        out.writeObject(obj);
        out.close();
    }

    public static Object deserialize(
        Context cx, Scriptable thisObj, Object[] args, Function funObj)
        throws IOException, ClassNotFoundException
    {
        if (args.length < 1)
        {
            throw Context.reportRuntimeError(
                "Expected a filename to read the serialization from");
        }

        final String filename = Context.toString(args[0]);
        final FileInputStream fis = new FileInputStream(filename);
        final Scriptable scope = ScriptableObject.getTopLevelScope(thisObj);
        final ObjectInputStream in = new ScriptableInputStream(fis, scope);
        final Object deserialized = in.readObject();
        in.close();

        return Context.toObject(deserialized, scope);
    }

    /**
     * The spawn function runs a given function or script in a different
     * thread.
     *
     * js> function g() { a = 7; }
     * js> a = 3;
     * 3
     * js> spawn(g)
     * Thread[Thread-1,5,main]
     * js> a
     * 3
     */
    public static Object spawn(
        Context cx, Scriptable thisObj, Object[] args, Function funObj)
    {
        final Scriptable scope = funObj.getParentScope();
        Runner runner;

        if (args.length != 0 && args[0] instanceof Function)
        {
            Object[] newArgs = null;

            if (args.length > 1 && args[1] instanceof Scriptable)
            {
                newArgs = cx.getElements((Scriptable) args[1]);
            }

            if (newArgs == null)
            {
                newArgs = ScriptRuntime.emptyArgs;
            }

            runner = new Runner(scope, (Function) args[0], newArgs);
        }
        else if (args.length != 0 && args[0] instanceof Script)
        {
            runner = new Runner(scope, (Script) args[0]);
        }
        else
        {
            throw reportRuntimeError("msg.spawn.args");
        }

        runner.setFactory(cx.getFactory());

        final Thread thread = new Thread(runner);
        thread.start();

        return thread;
    }

    /**
     * The sync function creates a synchronized function (in the sense
     * of a Java synchronized method) from an existing function. The
     * new function synchronizes on the <code>this</code> object of
     * its invocation.
     * js> var o = { f : sync(function(x) {
     *       print("entry");
     *       Packages.java.lang.Thread.sleep(x*1000);
     *       print("exit");
     *     })};
     * js> spawn(function() {o.f(5);});
     * Thread[Thread-0,5,main]
     * entry
     * js> spawn(function() {o.f(5);});
     * Thread[Thread-1,5,main]
     * js>
     * exit
     * entry
     * exit
     */
    public static Object sync(
        Context cx, Scriptable thisObj, Object[] args, Function funObj)
    {
        if (args.length == 1 && args[0] instanceof Function)
        {
            return new Synchronizer((Function) args[0]);
        }
        else
        {
            throw reportRuntimeError("msg.sync.args");
        }
    }

    /**
     * Execute the specified command with the given argument and options
     * as a separate process and return the exit status of the process.
     * <p>
     * Usage:
     * <pre>
     * runCommand(command)
     * runCommand(command, arg1, ..., argN)
     * runCommand(command, arg1, ..., argN, options)
     * </pre>
     * All except the last arguments to runCommand are converted to strings
     * and denote command name and its arguments. If the last argument is a
     * JavaScript object, it is an option object. Otherwise it is converted to
     * string denoting the last argument and options objects assumed to be
     * empty.
     * Te following properties of the option object are processed:
     * <ul>
     * <li><tt>args</tt> - provides an array of additional command arguments
     * <li><tt>env</tt> - explicit environment object. All its enumeratable
     *   properties define the corresponding environment variable names.
     * <li><tt>input</tt> - the process input. If it is not
     *   java.io.InputStream, it is converted to string and sent to the process
     *   as its input. If not specified, no input is provided to the process.
     * <li><tt>output</tt> - the process output instead of
     *   java.lang.System.out. If it is not instance of java.io.OutputStream,
     *   the process output is read, converted to a string, appended to the
     *   output property value converted to string and put as the new value of
     *   the output property.
     * <li><tt>err</tt> - the process error output instead of
     *   java.lang.System.err. If it is not instance of java.io.OutputStream,
     *   the process error output is read, converted to a string, appended to
     *   the err property value converted to string and put as the new
     *   value of the err property.
     * </ul>
     */
    public static Object runCommand(
        Context cx, Scriptable thisObj, Object[] args, Function funObj)
        throws IOException
    {
        int l = args.length;

        if (l == 0 || (l == 1 && args[0] instanceof Scriptable))
        {
            throw reportRuntimeError("msg.runCommand.bad.args");
        }

        InputStream in = null;
        OutputStream out = null;
        OutputStream err = null;
        ByteArrayOutputStream outBytes = null;
        ByteArrayOutputStream errBytes = null;
        Object outObj = null;
        Object errObj = null;
        String[] environment = null;
        Scriptable params = null;
        Object[] addArgs = null;

        if (args[l - 1] instanceof Scriptable)
        {
            params = (Scriptable) args[l - 1];
            --l;

            final Object envObj = ScriptableObject.getProperty(params, "env");

            if (envObj != Scriptable.NOT_FOUND)
            {
                if (envObj == null)
                {
                    environment = new String[0];
                }
                else
                {
                    if (!(envObj instanceof Scriptable))
                    {
                        throw reportRuntimeError("msg.runCommand.bad.env");
                    }

                    final Scriptable envHash = (Scriptable) envObj;
                    final Object[] ids =
                        ScriptableObject.getPropertyIds(envHash);
                    environment = new String[ids.length];

                    for (int i = 0; i != ids.length; ++i)
                    {
                        final Object keyObj = ids[i];
                        Object val;
                        String key;

                        if (keyObj instanceof String)
                        {
                            key = (String) keyObj;
                            val = ScriptableObject.getProperty(envHash, key);
                        }
                        else
                        {
                            final int ikey = ((Number) keyObj).intValue();
                            key = Integer.toString(ikey);
                            val = ScriptableObject.getProperty(envHash, ikey);
                        }

                        if (val == ScriptableObject.NOT_FOUND)
                        {
                            val = Undefined.instance;
                        }

                        environment[i] = key + '=' +
                            ScriptRuntime.toString(val);
                    }
                }
            }

            final Object inObj = ScriptableObject.getProperty(params, "input");

            if (inObj != Scriptable.NOT_FOUND)
            {
                in = toInputStream(inObj);
            }

            outObj = ScriptableObject.getProperty(params, "output");

            if (outObj != Scriptable.NOT_FOUND)
            {
                out = toOutputStream(outObj);

                if (out == null)
                {
                    outBytes = new ByteArrayOutputStream();
                    out = outBytes;
                }
            }

            errObj = ScriptableObject.getProperty(params, "err");

            if (errObj != Scriptable.NOT_FOUND)
            {
                err = toOutputStream(errObj);

                if (err == null)
                {
                    errBytes = new ByteArrayOutputStream();
                    err = errBytes;
                }
            }

            final Object addArgsObj =
                ScriptableObject.getProperty(params, "args");

            if (addArgsObj != Scriptable.NOT_FOUND)
            {
                final Scriptable s =
                    Context.toObject(addArgsObj, getTopLevelScope(thisObj));
                addArgs = cx.getElements(s);
            }
        }

        final ShellGlobal global = getInstance(funObj);

        if (out == null)
        {
            if (global != null)
            {
                out = global.getOut();
            }
            else
            {
                out = System.out;
            }
        }

        if (err == null)
        {
            if (global != null)
            {
                err = global.getErr();
            }
            else
            {
                err = System.err;
            }
        }

        // If no explicit input stream, do not send any input to process,
        // in particular, do not use System.in to avoid deadlocks
        // when waiting for user input to send to process which is already
        // terminated as it is not always possible to interrupt read method.
        final String[] cmd =
            new String[(addArgs == null) ? l : l + addArgs.length];

        for (int i = 0; i != l; ++i)
        {
            cmd[i] = ScriptRuntime.toString(args[i]);
        }

        if (addArgs != null)
        {
            for (int i = 0; i != addArgs.length; ++i)
            {
                cmd[l + i] = ScriptRuntime.toString(addArgs[i]);
            }
        }

        final int exitCode = runProcess(cmd, environment, in, out, err);

        if (outBytes != null)
        {
            final String s =
                ScriptRuntime.toString(outObj) + outBytes.toString();
            ScriptableObject.putProperty(params, "output", s);
        }

        if (errBytes != null)
        {
            final String s =
                ScriptRuntime.toString(errObj) + errBytes.toString();
            ScriptableObject.putProperty(params, "err", s);
        }

        return new Integer(exitCode);
    }

    /**
     * The seal function seals all supplied arguments.
     */
    public static void seal(
        Context cx, Scriptable thisObj, Object[] args, Function funObj)
    {
        for (int i = 0; i != args.length; ++i)
        {
            final Object arg = args[i];

            if (!(arg instanceof ScriptableObject) ||
                    arg == Undefined.instance)
            {
                if (!(arg instanceof Scriptable) || arg == Undefined.instance)
                {
                    throw reportRuntimeError("msg.shell.seal.not.object");
                }
                else
                {
                    throw reportRuntimeError("msg.shell.seal.not.scriptable");
                }
            }
        }

        for (int i = 0; i != args.length; ++i)
        {
            final Object arg = args[i];
            ((ScriptableObject) arg).sealObject();
        }
    }

    /**
     * The readFile reads the given file context and convert it to a string
     * using the specified character coding or default character coding if
     * explicit coding argument is not given.
     * <p>
     * Usage:
     * <pre>
     * readFile(filePath)
     * readFile(filePath, charCoding)
     * </pre>
     * The first form converts file's context to string using the default
     * character coding.
     */
    public static Object readFile(
        Context cx, Scriptable thisObj, Object[] args, Function funObj)
        throws IOException
    {
        if (args.length == 0)
        {
            throw reportRuntimeError("msg.shell.readFile.bad.args");
        }

        final String path = ScriptRuntime.toString(args[0]);
        String charCoding = null;

        if (args.length >= 2)
        {
            charCoding = ScriptRuntime.toString(args[1]);
        }

        return readUrl(path, charCoding, true);
    }

    /**
     * The readUrl opens connection to the given URL, read all its data
     * and converts them to a string
     * using the specified character coding or default character coding if
     * explicit coding argument is not given.
     * <p>
     * Usage:
     * <pre>
     * readUrl(url)
     * readUrl(url, charCoding)
     * </pre>
     * The first form converts file's context to string using the default
     * charCoding.
     */
    public static Object readUrl(
        Context cx, Scriptable thisObj, Object[] args, Function funObj)
        throws IOException
    {
        if (args.length == 0)
        {
            throw reportRuntimeError("msg.shell.readUrl.bad.args");
        }

        final String url = ScriptRuntime.toString(args[0]);
        String charCoding = null;

        if (args.length >= 2)
        {
            charCoding = ScriptRuntime.toString(args[1]);
        }

        return readUrl(url, charCoding, false);
    }

    public InputStream getIn()
    {
        return inStream == null ? System.in : inStream;
    }

    public void setIn(InputStream in)
    {
        inStream = in;
    }

    public PrintStream getOut()
    {
        return outStream == null ? System.out : outStream;
    }

    public void setOut(PrintStream out)
    {
        outStream = out;
    }

    public PrintStream getErr()
    {
        return errStream == null ? System.err : errStream;
    }

    public void setErr(PrintStream err)
    {
        errStream = err;
    }

    public void setSealedStdLib(boolean value)
    {
        sealedStdLib = value;
    }

    private static ShellGlobal getInstance(Function function)
    {
        final Scriptable scope = function.getParentScope();

        if (!(scope instanceof ShellGlobal))
        {
            throw reportRuntimeError(
                "msg.bad.shell.function.scope", String.valueOf(scope));
        }

        return (ShellGlobal) scope;
    }

    /**
     * If any of in, out, err is null, the corresponding process stream will
     * be closed immediately, otherwise it will be closed as soon as
     * all data will be read from/written to process
     */
    private static int runProcess(
        String[] cmd, String[] environment, InputStream in, OutputStream out,
        OutputStream err) throws IOException
    {
        Process p;

        if (environment == null)
        {
            p = Runtime.getRuntime()
                    .exec(cmd);
        }
        else
        {
            p = Runtime.getRuntime()
                    .exec(cmd, environment);
        }

        PipeThread inThread = null;
        PipeThread errThread = null;

        try
        {
            InputStream errProcess = null;

            try
            {
                if (err != null)
                {
                    errProcess = p.getErrorStream();
                }
                else
                {
                    p.getErrorStream()
                        .close();
                }

                InputStream outProcess = null;

                try
                {
                    if (out != null)
                    {
                        outProcess = p.getInputStream();
                    }
                    else
                    {
                        p.getInputStream()
                            .close();
                    }

                    OutputStream inProcess = null;

                    try
                    {
                        if (in != null)
                        {
                            inProcess = p.getOutputStream();
                        }
                        else
                        {
                            p.getOutputStream()
                                .close();
                        }

                        if (out != null)
                        {
                            // Read process output on this thread
                            if (err != null)
                            {
                                errThread = new PipeThread(
                                        true, errProcess, err);
                                errThread.start();
                            }

                            if (in != null)
                            {
                                inThread = new PipeThread(false, in, inProcess);
                                inThread.start();
                            }

                            pipe(true, outProcess, out);
                        }
                        else if (in != null)
                        {
                            // No output, read process input on this thread
                            if (err != null)
                            {
                                errThread = new PipeThread(
                                        true, errProcess, err);
                                errThread.start();
                            }

                            pipe(false, in, inProcess);
                            in.close();
                        }
                        else if (err != null)
                        {
                            // No output or input, read process err
                            // on this thread
                            pipe(true, errProcess, err);
                            errProcess.close();
                            errProcess = null;
                        }

                        // wait for process completion
                        for (;;)
                        {
                            try
                            {
                                p.waitFor();

                                break;
                            }
                            catch (InterruptedException ex)
                            {
                            }
                        }

                        return p.exitValue();
                    }
                    finally
                    {
                        // pipe will close stream as well, but for reliability
                        // duplicate it in any case
                        if (inProcess != null)
                        {
                            inProcess.close();
                        }
                    }
                }
                finally
                {
                    if (outProcess != null)
                    {
                        outProcess.close();
                    }
                }
            }
            finally
            {
                if (errProcess != null)
                {
                    errProcess.close();
                }
            }
        }
        finally
        {
            p.destroy();

            if (inThread != null)
            {
                for (;;)
                {
                    try
                    {
                        inThread.join();

                        break;
                    }
                    catch (InterruptedException ex)
                    {
                    }
                }
            }

            if (errThread != null)
            {
                for (;;)
                {
                    try
                    {
                        errThread.join();

                        break;
                    }
                    catch (InterruptedException ex)
                    {
                    }
                }
            }
        }
    }

    static void pipe(boolean fromProcess, InputStream from, OutputStream to)
        throws IOException
    {
        try
        {
            final int size = 4096;
            final byte[] buffer = new byte[size];

            for (;;)
            {
                int n;

                if (!fromProcess)
                {
                    n = from.read(buffer, 0, size);
                }
                else
                {
                    try
                    {
                        n = from.read(buffer, 0, size);
                    }
                    catch (IOException ex)
                    {
                        // Ignore exception as it can be cause by closed pipe
                        break;
                    }
                }

                if (n < 0)
                {
                    break;
                }

                if (fromProcess)
                {
                    to.write(buffer, 0, n);
                    to.flush();
                }
                else
                {
                    try
                    {
                        to.write(buffer, 0, n);
                        to.flush();
                    }
                    catch (IOException ex)
                    {
                        // Ignore exception as it can be cause by closed pipe
                        break;
                    }
                }
            }
        }
        finally
        {
            try
            {
                if (fromProcess)
                {
                    from.close();
                }
                else
                {
                    to.close();
                }
            }
            catch (IOException ex)
            {
                // Ignore errors on close. On Windows JVM may throw invalid
                // refrence exception if process terminates too fast.
            }
        }
    }

    private static InputStream toInputStream(Object value)
        throws IOException
    {
        InputStream is = null;
        String s = null;

        if (value instanceof Wrapper)
        {
            final Object unwrapped = ((Wrapper) value).unwrap();

            if (unwrapped instanceof InputStream)
            {
                is = (InputStream) unwrapped;
            }
            else if (unwrapped instanceof byte[])
            {
                is = new ByteArrayInputStream((byte[]) unwrapped);
            }
            else if (unwrapped instanceof Reader)
            {
                s = readReader((Reader) unwrapped);
            }
            else if (unwrapped instanceof char[])
            {
                s = new String((char[]) unwrapped);
            }
        }

        if (is == null)
        {
            if (s == null)
            {
                s = ScriptRuntime.toString(value);
            }

            is = new ByteArrayInputStream(s.getBytes());
        }

        return is;
    }

    private static OutputStream toOutputStream(Object value)
    {
        OutputStream os = null;

        if (value instanceof Wrapper)
        {
            final Object unwrapped = ((Wrapper) value).unwrap();

            if (unwrapped instanceof OutputStream)
            {
                os = (OutputStream) unwrapped;
            }
        }

        return os;
    }

    private static String readUrl(
        String filePath, String charCoding, boolean urlIsFile)
        throws IOException
    {
        int chunkLength;
        InputStream is = null;

        String cCoding = charCoding;

        try
        {
            if (!urlIsFile)
            {
                final URL urlObj = new URL(filePath);
                final URLConnection uc = urlObj.openConnection();
                is = uc.getInputStream();
                chunkLength = uc.getContentLength();

                if (chunkLength <= 0)
                {
                    chunkLength = 1024;
                }

                if (cCoding == null)
                {
                    final String type = uc.getContentType();

                    if (type != null)
                    {
                        cCoding = getCharCodingFromType(type);
                    }
                }
            }
            else
            {
                final File f = new File(filePath);

                final long length = f.length();
                chunkLength = (int) length;

                if (chunkLength != length)
                {
                    throw new IOException("File size is too large: " + length);
                }

                if (chunkLength == 0)
                {
                    return "";
                }

                is = new FileInputStream(f);
            }

            Reader r;

            if (cCoding == null)
            {
                r = new InputStreamReader(is);
            }
            else
            {
                r = new InputStreamReader(is, cCoding);
            }

            return readReader(r, chunkLength);
        }
        finally
        {
            if (is != null)
            {
                is.close();
            }
        }
    }

    private static String getCharCodingFromType(String type)
    {
        int i = type.indexOf(';');

        if (i >= 0)
        {
            int end = type.length();
            ++i;

            while (i != end && type.charAt(i) <= ' ')
            {
                ++i;
            }

            final String charset = "charset";

            if (charset.regionMatches(true, 0, type, i, charset.length()))
            {
                i += charset.length();

                while (i != end && type.charAt(i) <= ' ')
                {
                    ++i;
                }

                if (i != end && type.charAt(i) == '=')
                {
                    ++i;

                    while (i != end && type.charAt(i) <= ' ')
                    {
                        ++i;
                    }

                    if (i != end)
                    {
                        // i is at the start of non-empty
                        // charCoding spec
                        while (type.charAt(end - 1) <= ' ')
                        {
                            --end;
                        }

                        return type.substring(i, end);
                    }
                }
            }
        }

        return null;
    }

    private static String readReader(Reader reader) throws IOException
    {
        return readReader(reader, 4096);
    }

    private static String readReader(Reader reader, int initialBufferSize)
        throws IOException
    {
        char[] buffer = new char[initialBufferSize];
        int offset = 0;

        for (;;)
        {
            final int n = reader.read(buffer, offset, buffer.length - offset);

            if (n < 0)
            {
                break;
            }

            offset += n;

            if (offset == buffer.length)
            {
                final char[] tmp = new char[buffer.length * 2];
                System.arraycopy(buffer, 0, tmp, 0, offset);
                buffer = tmp;
            }
        }

        return new String(buffer, 0, offset);
    }

    static RuntimeException reportRuntimeError(String msgId)
    {
        final String message = ToolErrorReporter.getMessage(msgId);

        return Context.reportRuntimeError(message);
    }

    static RuntimeException reportRuntimeError(String msgId, String msgArg)
    {
        final String message = ToolErrorReporter.getMessage(msgId, msgArg);

        return Context.reportRuntimeError(message);
    }

    private NativeArray history;
    private InputStream inStream;
    private PrintStream outStream;
    private PrintStream errStream;
    private boolean sealedStdLib = false;
    private boolean initialized;

    public NativeArray getHistory()
    {
        return this.history;
    }

    public boolean isInitialized()
    {
        return this.initialized;
    }
}


class Runner implements Runnable, ContextAction
{
    Runner(Scriptable scope, Function func, Object[] args)
    {
        this.scope = scope;
        f = func;
        this.args = args;
    }

    Runner(Scriptable scope, Script script)
    {
        this.scope = scope;
        this.s = script;
    }

    public void run()
    {
        getFactory()
            .call(this);
    }

    public Object run(Context cx)
    {
        if (f != null)
        {
            return f.call(cx, scope, scope, args);
        }
        else
        {
            return s.exec(cx, scope);
        }
    }

    private ContextFactory factory;
    private Scriptable scope;
    private Function f;
    private Script s;
    private Object[] args;

    public ContextFactory getFactory()
    {
        return this.factory;
    }

    public void setFactory(ContextFactory factory)
    {
        this.factory = factory;
    }
}


class PipeThread extends Thread
{
    PipeThread(boolean fromProcess, InputStream from, OutputStream to)
    {
        setDaemon(true);
        this.fromProcess = fromProcess;
        this.from = from;
        this.to = to;
    }

    public void run()
    {
        try
        {
            ShellGlobal.pipe(fromProcess, from, to);
        }
        catch (IOException ex)
        {
            throw Context.throwAsScriptRuntimeEx(ex);
        }
    }

    private boolean fromProcess;
    private InputStream from;
    private OutputStream to;
}
