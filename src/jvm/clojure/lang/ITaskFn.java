/**
 *   Copyright (c) Rich Hickey. All rights reserved.
 *   The use and distribution terms for this software are covered by the
 *   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
 *   which can be found in the file epl-v10.html at the root of this distribution.
 *   By using this software in any fashion, you are agreeing to be bound by
 * 	 the terms of this license.
 *   You must not remove this notice, or any other, from this software.
 **/

/* rich Mar 25, 2006 3:54:03 PM */

package clojure.lang;

import kilim.Fiber;
import kilim.Pausable;

import java.util.concurrent.Callable;

public interface ITaskFn extends Callable, Runnable{

public Object invokeTask() throws Pausable;
public Object invokeTask(Fiber fiber) throws Pausable;

public Object invokeTask(Object arg1) throws Pausable ;
public Object invokeTask(Object arg1, Fiber fiber) throws Pausable;

public Object invokeTask(Object arg1, Object arg2) throws Pausable ;
public Object invokeTask(Object arg1, Object arg2, Fiber fiber) throws Pausable ;

public Object invokeTask(Object arg1, Object arg2, Object arg3) throws Pausable ;
public Object invokeTask(Object arg1, Object arg2, Object arg3, Fiber fiber) throws Pausable ;

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4) throws Pausable ;
public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Fiber fiber) throws Pausable ;

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5) throws Pausable ;
public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Fiber fiber) throws Pausable ;

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6) throws Pausable ;
public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Fiber fiber) throws Pausable ;

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7) throws Pausable;
public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Fiber fiber) throws Pausable;

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8) throws Pausable ;
public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Fiber fiber) throws Pausable ;

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9) throws Pausable ;

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9, Object arg10) throws Pausable ;

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9, Object arg10, Object arg11) throws Pausable ;

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9, Object arg10, Object arg11, Object arg12) throws Pausable ;

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13) throws Pausable ;

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14) throws Pausable
		;

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                         Object arg15) throws Pausable ;

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                         Object arg15, Object arg16) throws Pausable ;

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                         Object arg15, Object arg16, Object arg17) throws Pausable ;

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                         Object arg15, Object arg16, Object arg17, Object arg18) throws Pausable ;

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                         Object arg15, Object arg16, Object arg17, Object arg18, Object arg19) throws Pausable ;

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                         Object arg15, Object arg16, Object arg17, Object arg18, Object arg19, Object arg20) throws Pausable
		;


public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9, Fiber fiber) throws Pausable ;

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9, Object arg10, Fiber fiber) throws Pausable ;

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9, Object arg10, Object arg11, Fiber fiber) throws Pausable ;

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Fiber fiber) throws Pausable ;

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Fiber fiber) throws Pausable ;

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14, Fiber fiber) throws Pausable
		;

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                         Object arg15, Fiber fiber) throws Pausable ;

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                         Object arg15, Object arg16, Fiber fiber) throws Pausable ;

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                         Object arg15, Object arg16, Object arg17, Fiber fiber) throws Pausable ;

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                         Object arg15, Object arg16, Object arg17, Object arg18, Fiber fiber) throws Pausable ;

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                         Object arg15, Object arg16, Object arg17, Object arg18, Object arg19, Fiber fiber) throws Pausable ;

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                         Object arg15, Object arg16, Object arg17, Object arg18, Object arg19, Object arg20, Fiber fiber) throws Pausable
		;

// @todo
// this is not supported
public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                         Object arg15, Object arg16, Object arg17, Object arg18, Object arg19, Object arg20,
                         Object... args) throws Pausable
		;
}
