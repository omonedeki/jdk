/*
 * Copyright (c) 2001, 2025, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package nsk.jdi.ObjectReference.invokeMethod;

import nsk.share.*;
import nsk.share.jpda.*;
import nsk.share.jdi.*;


/**
 * This class is used as debuggee application for the invokemethod001 JDI test.
 */

public class invokemethod001a {

    //----------------------------------------------------- templete section

    static final int PASSED = 0;
    static final int FAILED = 2;
    static final int PASS_BASE = 95;

    //--------------------------------------------------   log procedures

    static boolean verbMode = false;

    private static void log1(String message) {
        if (verbMode)
            System.err.println("**>  debuggee: " + message);
    }

    public static void log2(String message) {
        if (verbMode)
            System.err.println("**> " + message);
    }

    private static void logErr(String message) {
        if (verbMode)
            System.err.println("!!**>  debuggee: " + message);
    }

    //====================================================== test program

    static Threadinvokemethod001a test_thread = null;

    //----------------------------------------------------   main method

    public static void main (String argv[]) {

        for (int i=0; i<argv.length; i++) {
            if ( argv[i].equals("-vbs") || argv[i].equals("-verbose") ) {
                verbMode = true;
                break;
            }
        }
        log1("debuggee started!");

        // informing a debugger of readyness
        ArgumentHandler argHandler = new ArgumentHandler(argv);
        IOPipe pipe = argHandler.createDebugeeIOPipe();
        pipe.println("ready");


        int exitCode = PASSED;
        for (int i = 0; ; i++) {

            String instruction;

            log1("waiting for an instruction from the debugger ...");
            instruction = pipe.readln();
            if (instruction.equals("quit")) {
                log1("'quit' recieved");
                break ;

            } else if (instruction.equals("newcheck")) {
                switch (i) {

    //------------------------------------------------------  section tested

                case 0:
                         test_thread = new Threadinvokemethod001a("testedThread");
                         log1("       thread2 is created");

                         label:
                         synchronized (Threadinvokemethod001a.lockingObject) {
                             synchronized (Threadinvokemethod001a.waitnotifyObj) {
                                 log1("       synchronized (waitnotifyObj) { enter");
                                 log1("       before: test_thread.start()");
                                 test_thread.start();

                                 try {
                                     log1("       before:   waitnotifyObj.wait();");
                                     Threadinvokemethod001a.waitnotifyObj.wait();
                                     log1("       after:    waitnotifyObj.wait();");
                                     pipe.println("checkready");
                                     instruction = pipe.readln();
                                     if (!instruction.equals("continue")) {
                                         logErr("ERROR: unexpected instruction: " + instruction);
                                         exitCode = FAILED;
                                         break label;
                                     }
                                     pipe.println("docontinue");
                                 } catch ( Exception e2) {
                                     log1("       Exception e2 exception: " + e2 );
                                     pipe.println("waitnotifyerr");
                                 }
                             }
                         }
                         log1("mainThread is out of: synchronized (lockingObject) {");

                         break ;

    //-------------------------------------------------    standard end section

                default:
                                pipe.println("checkend");
                                break ;
                }

            } else {
                logErr("ERRROR: unexpected instruction: " + instruction);
                exitCode = FAILED;
                break ;
            }
        }

        System.exit(exitCode + PASS_BASE);
    }
}

class Threadinvokemethod001a extends Thread {

    public Threadinvokemethod001a(String threadName) {
        super(threadName);
    }

    public static Object waitnotifyObj = new Object();
    public static Object lockingObject = new Object();

    public void run() {
        log("method 'run' enter");
        synchronized (waitnotifyObj)                                    {
            log("entered into block:  synchronized (waitnotifyObj)");
            waitnotifyObj.notify();                                     }
        log("exited from block:  synchronized (waitnotifyObj)");
        synchronized (lockingObject)                                    {
            log("entered into block:  synchronized (lockingObject)");   }
        log("exited from block:  synchronized (lockingObject)");
        log("call to the method 'runt1'");
        runt1();
        log("returned from the method 'runt1'");
        log("method 'run' exit");
        return;
    }

    public void runt1() {

        int i0 = 0;
        log("method 'runt1': enter");
        i0 = 1;
        log("method 'runt1': body: i0 == " + i0);
        log("method 'runt1': exit");
        return;
    }

    public static final int breakpointLineNumber1 = 3;


    void log(String str) {
        invokemethod001a.log2("thread2: " + str);
    }

    public static boolean blValue() {
        return true;
    }
    public static byte btValue() {
        return 1;
    }
    public static char chValue() {
        return 1;
    }
    public static double dbValue() {
        return 1.0d;
    }
    public static float flValue() {
        return 1.0f;
    }
    public static int inValue() {
        return 1;
    }
    public static long lnValue() {
        return 1;
    }
    public static short shValue() {
        return 1;
    }
    public static void vdValue() {
        return ;
    }

}
