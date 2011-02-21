package org.equilibriums.aop.utils.interceptor.stub;

import java.io.IOException;

import org.junit.*;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import org.aopalliance.intercept.MethodInvocation;

public class StubWithThrowableInterceptorTest {

    private static interface ObjectWithoutDeclaredExceptionMethod {
		
		public void method1(String arg1);		
		
	}
    
    private static interface ObjectWithDeclaredExceptionMethod {
		
		public void method2(String arg1) throws IOException;		
		
	}    
    
	private StubWithThrowableInterceptor noOpWithThrowableInterceptor = null;
		
	@Before
	public void initialize(){
		noOpWithThrowableInterceptor = new StubWithThrowableInterceptor();
	}
	
	@Test
	public void testWith_ObjectWithoutDeclaredExceptionMethod_NullThrowable() throws Throwable {			
		noOpWithThrowableInterceptor.setThrowable(null);
		
		MethodInvocation methodInvocation = createMock(MethodInvocation.class);
		
		expect( methodInvocation.getMethod() ).andReturn( ObjectWithoutDeclaredExceptionMethod.class.getMethod("method1", new Class[]{String.class} ) );
		
		replay(methodInvocation);
		try{ noOpWithThrowableInterceptor.invoke(methodInvocation);	fail(); }	
		catch(IllegalArgumentException e){ assertEquals( e.getMessage(), "Throwable property cannot be null"); }
		verify(methodInvocation);
	}
	
	@Test
	public void testWith_ObjectWithoutDeclaredExceptionMethod_CheckedException() throws Throwable {
		Exception ex = new Exception();
		noOpWithThrowableInterceptor.setThrowable(ex);
		
		MethodInvocation methodInvocation = createMock(MethodInvocation.class);
		
		expect( methodInvocation.getMethod() ).andReturn( ObjectWithoutDeclaredExceptionMethod.class.getMethod("method1", new Class[]{String.class} ) );
		
		replay(methodInvocation);
		try{ noOpWithThrowableInterceptor.invoke(methodInvocation);	fail(); }	
		catch(IllegalArgumentException e){ assertEquals( e.getMessage(), 
		String.format( "The method %s does not declare any checked axception and throwable %s is checked exception", "method1", ex ) ); }
		verify(methodInvocation);
	}
	
	
	@Test
	public void testWith_ObjectWithDeclaredExceptionMethod_RuntimeException() throws Throwable {
		UnsupportedOperationException ex = new UnsupportedOperationException();
		noOpWithThrowableInterceptor.setThrowable(ex);
		
		MethodInvocation methodInvocation = createMock(MethodInvocation.class);
		
		expect( methodInvocation.getMethod() ).andReturn( ObjectWithDeclaredExceptionMethod.class.getMethod("method2", new Class[]{String.class} ) );
		
		replay(methodInvocation);
		try{ noOpWithThrowableInterceptor.invoke(methodInvocation);	fail(); }	
		catch(UnsupportedOperationException e){ assertEquals(e, ex); }
		verify(methodInvocation);
	}
			
	@Test
	public void testWith_ObjectWithDeclaredExceptionMethod_NotAssignableCheckedException() throws Throwable {
		Exception ex = new Exception();
		noOpWithThrowableInterceptor.setThrowable(ex);
		
		MethodInvocation methodInvocation = createMock(MethodInvocation.class);
		
		expect( methodInvocation.getMethod() ).andReturn( ObjectWithDeclaredExceptionMethod.class.getMethod("method2", new Class[]{String.class} ) );
		
		replay(methodInvocation);
		try{ noOpWithThrowableInterceptor.invoke(methodInvocation);	fail(); }	
		catch(IllegalArgumentException e){ assertEquals( e.getMessage(), 
		String.format( "Throwable is not assignable from any of methods declared exceptions for method %s and throwable %s", "method2", ex ) ); }
		verify(methodInvocation);
	}
	
	@Test
	public void testWith_ObjectWithDeclaredExceptionMethod_CheckedException() throws Throwable {
		IOException ex = new IOException();
		noOpWithThrowableInterceptor.setThrowable(ex);
		
		MethodInvocation methodInvocation = createMock(MethodInvocation.class);
		
		expect( methodInvocation.getMethod() ).andReturn( ObjectWithDeclaredExceptionMethod.class.getMethod("method2", new Class[]{String.class} ) );
		
		replay(methodInvocation);
		try{ noOpWithThrowableInterceptor.invoke(methodInvocation);	fail(); }	
		catch(Exception e){ assertEquals(e, ex); }
		verify(methodInvocation);
	}
}
