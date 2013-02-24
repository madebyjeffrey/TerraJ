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
 * The Original Code is Rhino JavaScript Debugger code, released
 * November 21, 2000.
 *
 * The Initial Developer of the Original Code is See Beyond Corporation.

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
 */
package com.alvermont.javascript.tools.shell;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.Document;
import javax.swing.text.Segment;

class ConsoleWrite implements Runnable
{
    private ShellConsoleTextArea textArea;
    private String str;

    public ConsoleWrite(ShellConsoleTextArea textArea, String str)
    {
        this.textArea = textArea;
        this.str = str;
    }

    public void run()
    {
        this.textArea.write(this.str);
    }
}
;

class ConsoleWriter extends java.io.OutputStream
{
    private ShellConsoleTextArea textArea;
    private StringBuffer buffer;

    public ConsoleWriter(ShellConsoleTextArea textArea)
    {
        this.textArea = textArea;
        this.buffer = new StringBuffer();
    }

    public synchronized void write(int ch)
    {
        this.buffer.append((char) ch);

        if (ch == '\n')
        {
            flushBuffer();
        }
    }

    public synchronized void write(char[] data, int off, int len)
    {
        for (int i = off; i < len; ++i)
        {
            this.buffer.append(data[i]);

            if (data[i] == '\n')
            {
                flushBuffer();
            }
        }
    }

    public synchronized void flush()
    {
        if (this.buffer.length() > 0)
        {
            flushBuffer();
        }
    }

    public void close()
    {
        flush();
    }

    private void flushBuffer()
    {
        final String str = this.buffer.toString();
        this.buffer.setLength(0);
        SwingUtilities.invokeLater(new ConsoleWrite(this.textArea, str));
    }
}
;

/**
 * Class that provides the extended text area for the shell
 */
public class ShellConsoleTextArea extends JTextArea implements KeyListener,
    DocumentListener
{
    static final long serialVersionUID = 8557083244830872961L;
    private ConsoleWriter console1;
    private ConsoleWriter console2;
    private PrintStream out;
    private PrintStream err;
    private PrintWriter inPipe;
    private PipedInputStream in;
    private java.util.List<String> history;
    private int historyIndex = -1;
    private int outputMark = 0;

    /** Font size for the text area */
    private static final int MONOSPACED_FONT_SIZE = 12;

    // RequireThis OFF: MONOSPACED_FONT_SIZE
    private static final int BACKSPACE_CHAR = 0x8;

    // RequireThis OFF: BACKSPACE_CHAR

    /**
     * Selects a range of text
     *
     * @param start The start index for the selection
     * @param end The end index for the selection
     */
    public void select(int start, int end)
    {
        requestFocus();
        super.select(start, end);
    }

    /**
     * Create a new instance of ShellConsoleTextArea
     *
     * @param argv The arguments for this shell
     */
    public ShellConsoleTextArea(String[] argv)
    {
        super();
        this.history = new java.util.ArrayList<String>();
        this.console1 = new ConsoleWriter(this);
        this.console2 = new ConsoleWriter(this);
        this.out = new PrintStream(this.console1);
        this.err = new PrintStream(this.console2);

        final PipedOutputStream outPipe = new PipedOutputStream();
        this.inPipe = new PrintWriter(outPipe);
        this.in = new PipedInputStream();

        try
        {
            outPipe.connect(this.in);
        }
        catch (IOException exc)
        {
            exc.printStackTrace();
        }

        getDocument()
            .addDocumentListener(this);
        addKeyListener(this);
        setLineWrap(true);
        setFont(new Font("Monospaced", 0, MONOSPACED_FONT_SIZE));
    }

    synchronized void returnPressed()
    {
        final Document doc = getDocument();
        final int len = doc.getLength();
        final Segment segment = new Segment();

        try
        {
            doc.getText(this.outputMark, len - this.outputMark, segment);
        }
        catch (javax.swing.text.BadLocationException ignored)
        {
            ignored.printStackTrace();
        }

        if (segment.count > 0)
        {
            this.history.add(segment.toString());
        }

        this.historyIndex = this.history.size();
        this.inPipe.write(segment.array, segment.offset, segment.count);
        append("\n");
        this.outputMark = doc.getLength();
        this.inPipe.write("\n");
        this.inPipe.flush();
        this.console1.flush();
    }

    /**
     * Evaluate a string by wrting it to the input pipe
     *
     * @param str The string to be evaluated
     */
    public void eval(String str)
    {
        this.inPipe.write(str);
        this.inPipe.write("\n");
        this.inPipe.flush();
        this.console1.flush();
    }

    /**
     * Called to process a key event when a key has been pressed
     *
     * @param e The event describing the key press
     */
    public void keyPressed(KeyEvent e)
    {
        final int code = e.getKeyCode();

        if (code == KeyEvent.VK_BACK_SPACE || code == KeyEvent.VK_LEFT)
        {
            if (this.outputMark == getCaretPosition())
            {
                e.consume();
            }
        }
        else if (code == KeyEvent.VK_HOME)
        {
            final int caretPos = getCaretPosition();

            if (caretPos == this.outputMark)
            {
                e.consume();
            }
            else if (caretPos > this.outputMark)
            {
                if (!e.isControlDown())
                {
                    if (e.isShiftDown())
                    {
                        moveCaretPosition(this.outputMark);
                    }
                    else
                    {
                        setCaretPosition(this.outputMark);
                    }

                    e.consume();
                }
            }
        }
        else if (code == KeyEvent.VK_ENTER)
        {
            returnPressed();
            e.consume();
        }
        else if (code == KeyEvent.VK_UP)
        {
            --this.historyIndex;

            if (this.historyIndex >= 0)
            {
                if (this.historyIndex >= this.history.size())
                {
                    this.historyIndex = this.history.size() - 1;
                }

                if (this.historyIndex >= 0)
                {
                    final String str =
                        (String) this.history.get(this.historyIndex);
                    final int len = getDocument()
                            .getLength();
                    replaceRange(str, this.outputMark, len);

                    final int caretPos = this.outputMark + str.length();
                    select(caretPos, caretPos);
                }
                else
                {
                    ++this.historyIndex;
                }
            }
            else
            {
                ++this.historyIndex;
            }

            e.consume();
        }
        else if (code == KeyEvent.VK_DOWN)
        {
            int caretPos = this.outputMark;

            if (this.history.size() > 0)
            {
                ++this.historyIndex;

                if (this.historyIndex < 0)
                {
                    this.historyIndex = 0;
                }

                final int len = getDocument()
                        .getLength();

                if (this.historyIndex < this.history.size())
                {
                    final String str =
                        (String) this.history.get(this.historyIndex);
                    replaceRange(str, this.outputMark, len);
                    caretPos = this.outputMark + str.length();
                }
                else
                {
                    this.historyIndex = this.history.size();
                    replaceRange("", this.outputMark, len);
                }
            }

            select(caretPos, caretPos);
            e.consume();
        }
    }

    /**
     * Called to process a key event when a key has been typed. Note this is
     * different from a key press. Some key presses do not result in characters
     * being fed to the application.
     *
     * @param e The event describing the key that has been typed
     */
    public void keyTyped(KeyEvent e)
    {
        final int keyChar = e.getKeyChar();

        if (keyChar == BACKSPACE_CHAR /* KeyEvent.VK_BACK_SPACE */)
        {
            if (this.outputMark == getCaretPosition())
            {
                e.consume();
            }
        }
        else if (getCaretPosition() < this.outputMark)
        {
            setCaretPosition(this.outputMark);
        }
    }

    /**
     * Called to process a key event when a key has been released
     *
     * @param e The event describing the key release
     */
    public synchronized void keyReleased(KeyEvent e)
    {
    }

    /**
     * Writes a string to this text area
     *
     * @param str The string to be written
     */
    public synchronized void write(String str)
    {
        insert(str, this.outputMark);

        final int len = str.length();
        this.outputMark += len;
        select(this.outputMark, this.outputMark);
    }

    /**
     * Called to process an insert operation in response to an event
     *
     * @param e The document event that has occurred
     */
    public synchronized void insertUpdate(DocumentEvent e)
    {
        final int len = e.getLength();
        final int off = e.getOffset();

        if (this.outputMark > off)
        {
            this.outputMark += len;
        }
    }

    /**
     * Called to process a remove operation in response to an event
     *
     * @param e The document event that has occurred
     */
    public synchronized void removeUpdate(DocumentEvent e)
    {
        final int len = e.getLength();
        final int off = e.getOffset();

        if (this.outputMark > off)
        {
            if (this.outputMark >= off + len)
            {
                this.outputMark -= len;
            }
            else
            {
                this.outputMark = off;
            }
        }
    }

    /**
     * Called after the UI has been updated
     */
    public synchronized void postUpdateUI()
    {
        // this attempts to cleanup the damage done by updateComponentTreeUI
        requestFocus();
        setCaret(getCaret());
        select(this.outputMark, this.outputMark);
    }

    /**
     * Called to process a change operation in response to an event
     *
     * @param e The document event that has occurred
     */
    public synchronized void changedUpdate(DocumentEvent e)
    {
    }

    /**
     * Get the input stream for this object
     *
     * @return The input stream for the text area
     */
    public InputStream getIn()
    {
        return this.in;
    }

    /**
     * Get the output stream for this object
     *
     * @return The output stream for this text area
     */
    public PrintStream getOut()
    {
        return this.out;
    }

    /**
     * Get the error stream for this object
     *
     * @return The error stream for this text area
     */
    public PrintStream getErr()
    {
        return this.err;
    }
}
