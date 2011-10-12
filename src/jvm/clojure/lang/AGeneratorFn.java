package clojure.lang;

import kilim.Fiber;
import kilim.Generator;
import kilim.Pausable;

/**
 * User: antonio
 * Date: 25/09/2011
 * Time: 13:47
 */
public abstract class AGeneratorFn extends Generator implements IFn, ITaskFn, IGeneratorFn {

public Object call() {
	return invoke();
}

public void run(){
	try
		{
		invoke();
		}
	catch(Exception e)
		{
		throw Util.runtimeException(e);
		}
}

public Object invoke() {
	return throwArity(0);
}

public Object invoke(Object arg1) {
	return throwArity(1);
}


public Object invoke(Object arg1, Object arg2) {
	return throwArity(2);
}

public Object invoke(Object arg1, Object arg2, Object arg3) {
	return throwArity(3);
}

public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4) {
	return throwArity(4);
}

public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5) {
	return throwArity(5);
}

public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6) {
	return throwArity(6);
}

public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7)
		{
	return throwArity(7);
}

public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8) {
	return throwArity(8);
}

public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9) {
	return throwArity(9);
}

public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9, Object arg10) {
	return throwArity(10);
}

public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9, Object arg10, Object arg11) {
	return throwArity(11);
}

public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9, Object arg10, Object arg11, Object arg12) {
	return throwArity(12);
}

public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13)
		{
	return throwArity(13);
}

public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14)
		{
	return throwArity(14);
}

public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                     Object arg15) {
	return throwArity(15);
}

public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                     Object arg15, Object arg16) {
	return throwArity(16);
}

public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                     Object arg15, Object arg16, Object arg17) {
	return throwArity(17);
}

public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                     Object arg15, Object arg16, Object arg17, Object arg18) {
	return throwArity(18);
}

public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                     Object arg15, Object arg16, Object arg17, Object arg18, Object arg19) {
	return throwArity(19);
}

public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                     Object arg15, Object arg16, Object arg17, Object arg18, Object arg19, Object arg20)
		{
	return throwArity(20);
}


public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                     Object arg15, Object arg16, Object arg17, Object arg18, Object arg19, Object arg20,
                     Object... args)
		{
	return throwArity(21);
}

// Throwin Pausable
public Object invokeTask() throws Pausable {
	return throwArity(0);
}

public Object invokeTask(Fiber fiber) throws Pausable {
	return throwArity(0);
}
public Object invokeTask(Object arg1) throws Pausable {
	return throwArity(1);
}

public Object invokeTask(Object arg1, Fiber fiber) throws Pausable{
	return throwArity(1);
}

public Object invokeTask(Object arg1, Object arg2) throws Pausable {
	return throwArity(2);
}

public Object invokeTask(Object arg1, Object arg2, Object arg3) throws Pausable {
	return throwArity(3);
}

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4) throws Pausable {
	return throwArity(4);
}

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5) throws Pausable {
	return throwArity(5);
}

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6) throws Pausable {
	return throwArity(6);
}

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7) throws Pausable
		{
	return throwArity(7);
}

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8) throws Pausable {
	return throwArity(8);
}

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9) throws Pausable {
	return throwArity(9);
}

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9, Object arg10) throws Pausable {
	return throwArity(10);
}

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9, Object arg10, Object arg11) throws Pausable {
	return throwArity(11);
}

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9, Object arg10, Object arg11, Object arg12) throws Pausable {
	return throwArity(12);
}

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13) throws Pausable
		{
	return throwArity(13);
}

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14) throws Pausable
		{
	return throwArity(14);
}

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                     Object arg15) throws Pausable {
	return throwArity(15);
}

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                     Object arg15, Object arg16) throws Pausable {
	return throwArity(16);
}

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                     Object arg15, Object arg16, Object arg17) throws Pausable {
	return throwArity(17);
}

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                     Object arg15, Object arg16, Object arg17, Object arg18) throws Pausable {
	return throwArity(18);
}

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                     Object arg15, Object arg16, Object arg17, Object arg18, Object arg19) throws Pausable {
	return throwArity(19);
}

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                     Object arg15, Object arg16, Object arg17, Object arg18, Object arg19, Object arg20) throws Pausable
		{
	return throwArity(20);
}

// from here

public Object invokeTask(Object arg1, Object arg2, Fiber fiber) throws Pausable {
	return throwArity(2);
}

public Object invokeTask(Object arg1, Object arg2, Object arg3, Fiber fiber) throws Pausable {
	return throwArity(3);
}

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Fiber fiber) throws Pausable {
	return throwArity(4);
}

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Fiber fiber) throws Pausable {
	return throwArity(5);
}

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Fiber fiber) throws Pausable {
	return throwArity(6);
}

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Fiber fiber) throws Pausable
		{
	return throwArity(7);
}

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Fiber fiber) throws Pausable {
	return throwArity(8);
}

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9, Fiber fiber) throws Pausable {
	return throwArity(9);
}

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9, Object arg10, Fiber fiber) throws Pausable {
	return throwArity(10);
}

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9, Object arg10, Object arg11, Fiber fiber) throws Pausable {
	return throwArity(11);
}

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Fiber fiber) throws Pausable {
	return throwArity(12);
}

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Fiber fiber) throws Pausable
		{
	return throwArity(13);
}

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14, Fiber fiber) throws Pausable
		{
	return throwArity(14);
}

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                     Object arg15, Fiber fiber) throws Pausable {
	return throwArity(15);
}

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                     Object arg15, Object arg16, Fiber fiber) throws Pausable {
	return throwArity(16);
}

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                     Object arg15, Object arg16, Object arg17, Fiber fiber) throws Pausable {
	return throwArity(17);
}

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                     Object arg15, Object arg16, Object arg17, Object arg18, Fiber fiber) throws Pausable {
	return throwArity(18);
}

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                     Object arg15, Object arg16, Object arg17, Object arg18, Object arg19, Fiber fiber) throws Pausable {
	return throwArity(19);
}

public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                     Object arg15, Object arg16, Object arg17, Object arg18, Object arg19, Object arg20, Fiber fiber) throws Pausable
		{
	return throwArity(20);
}

// @todo
// this is not supported
public Object invokeTask(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                     Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                     Object arg15, Object arg16, Object arg17, Object arg18, Object arg19, Object arg20,
                     Object... args) throws Pausable
		{
	return throwArity(21);
}

public Object applyTo(ISeq arglist) {
	return applyToHelper(this, Util.ret1(arglist, arglist = null));
}

static public Object applyToHelper(IFn ifn, ISeq arglist) {
	switch(RT.boundedLength(arglist, 20))
		{
		case 0:
			arglist = null;
			return ifn.invoke();
		case 1:
			return ifn.invoke(Util.ret1(arglist.first(), arglist = null));
		case 2:
			return ifn.invoke(arglist.first()
					, Util.ret1((arglist = arglist.next()).first(), arglist = null)
			);
		case 3:
			return ifn.invoke(arglist.first()
					, (arglist = arglist.next()).first()
					, Util.ret1((arglist = arglist.next()).first(), arglist = null)
			);
		case 4:
			return ifn.invoke(arglist.first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, Util.ret1((arglist = arglist.next()).first(), arglist = null)
			);
		case 5:
			return ifn.invoke(arglist.first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, Util.ret1((arglist = arglist.next()).first(), arglist = null)
			);
		case 6:
			return ifn.invoke(arglist.first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, Util.ret1((arglist = arglist.next()).first(), arglist = null)
			);
		case 7:
			return ifn.invoke(arglist.first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, Util.ret1((arglist = arglist.next()).first(), arglist = null)
			);
		case 8:
			return ifn.invoke(arglist.first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, Util.ret1((arglist = arglist.next()).first(), arglist = null)
			);
		case 9:
			return ifn.invoke(arglist.first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, Util.ret1((arglist = arglist.next()).first(), arglist = null)
			);
		case 10:
			return ifn.invoke(arglist.first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, Util.ret1((arglist = arglist.next()).first(), arglist = null)
			);
		case 11:
			return ifn.invoke(arglist.first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, Util.ret1((arglist = arglist.next()).first(), arglist = null)
			);
		case 12:
			return ifn.invoke(arglist.first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, Util.ret1((arglist = arglist.next()).first(), arglist = null)
			);
		case 13:
			return ifn.invoke(arglist.first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, Util.ret1((arglist = arglist.next()).first(), arglist = null)
			);
		case 14:
			return ifn.invoke(arglist.first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, Util.ret1((arglist = arglist.next()).first(), arglist = null)
			);
		case 15:
			return ifn.invoke(arglist.first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, Util.ret1((arglist = arglist.next()).first(), arglist = null)
			);
		case 16:
			return ifn.invoke(arglist.first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, Util.ret1((arglist = arglist.next()).first(), arglist = null)
			);
		case 17:
			return ifn.invoke(arglist.first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, Util.ret1((arglist = arglist.next()).first(), arglist = null)
			);
		case 18:
			return ifn.invoke(arglist.first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, Util.ret1((arglist = arglist.next()).first(), arglist = null)
			);
		case 19:
			return ifn.invoke(arglist.first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, Util.ret1((arglist = arglist.next()).first(), arglist = null)
			);
		case 20:
			return ifn.invoke(arglist.first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, Util.ret1((arglist = arglist.next()).first(), arglist = null)
			);
		default:
			return ifn.invoke(arglist.first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, (arglist = arglist.next()).first()
					, RT.seqToArray(Util.ret1(arglist.next(), arglist = null)));
		}
}

public Object generate(Object self) throws Pausable {
    return throwArity(1);
}

public void runGenerator() throws Pausable {
	try
		{
		generate(this);
		}
	catch(Exception e)
		{
		throw Util.runtimeException(e);
		}
}
public Object throwArity(int n){
	String name = getClass().getSimpleName();
	int suffix = name.lastIndexOf("__");
	throw new ArityException(n, (suffix == -1 ? name : name.substring(0, suffix)).replace('_', '-'));
}
}
