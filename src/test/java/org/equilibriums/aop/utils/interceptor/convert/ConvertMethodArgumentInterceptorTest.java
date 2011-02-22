/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.equilibriums.aop.utils.interceptor.convert;

import java.util.*;

import org.junit.*;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import org.aopalliance.intercept.MethodInvocation;

public class ConvertMethodArgumentInterceptorTest {
 
	public static class ComplexObject{		
	
		private List<String> stringListProperty = null;
		
		private Map<String, String> stringMapProperty = null;

		public List< String > getStringListProperty() {
        	return stringListProperty;
        }

		public void setStringListProperty( List< String > stringListProperty ) {
        	this.stringListProperty = stringListProperty;
        }

		public Map< String, String > getStringMapProperty() {
        	return stringMapProperty;
        }

		public void setStringMapProperty( Map< String, String > stringMapProperty ) {
        	this.stringMapProperty = stringMapProperty;
        }
	}
	
	private ConvertMethodArgumentInterceptor convertMethodArgumentInterceptor = null;
	
	@Before
	public void initialize(){
		convertMethodArgumentInterceptor = new ConvertMethodArgumentInterceptor();
	}
	
	@Test
	public void testWith_VoidMethod() throws Throwable {		
		MethodInvocation methodInvocation = createMock(MethodInvocation.class);
		
		expect( methodInvocation.getArguments() ).andReturn( new Object[]{} );
		
		replay(methodInvocation);
		try{ convertMethodArgumentInterceptor.invoke( methodInvocation ); fail(); }
		catch(IllegalArgumentException e){ assertEquals( "fromArgumentIndex 0 is out of bounds with arguments count of 0", e.getMessage() ); }
		verify(methodInvocation);
	}
	
	
	@Test
	public void testWith_SingleArgumentMethod_NegativeFromArgumentIndex() throws Throwable {	
		convertMethodArgumentInterceptor.setFromArgumentIndex( -1 );
		
		MethodInvocation methodInvocation = createMock(MethodInvocation.class);
		
		expect( methodInvocation.getArguments() ).andReturn( new Object[]{"arg1"} );
		
		replay(methodInvocation);
		try{ convertMethodArgumentInterceptor.invoke( methodInvocation ); fail(); }
		catch(IllegalArgumentException e){ assertEquals( "fromArgumentIndex -1 is out of bounds with arguments count of 1", e.getMessage() ); }
		verify(methodInvocation);
	}
	
	@Test
	public void testWith_SingleArgumentMethod_NegativeToArgumentIndex() throws Throwable {	
		convertMethodArgumentInterceptor.setToArgumentIndex( -1 );
		
		MethodInvocation methodInvocation = createMock(MethodInvocation.class);
		
		expect( methodInvocation.getArguments() ).andReturn( new Object[]{"arg1"} );
		
		replay(methodInvocation);
		try{ convertMethodArgumentInterceptor.invoke( methodInvocation ); fail(); }
		catch(IllegalArgumentException e){ assertEquals( "toArgumentIndex -1 is out of bounds with arguments count of 1", e.getMessage() ); }
		verify(methodInvocation);
	}
	
	
	@Test
	public void testWith_SingleArgumentMethod_BiggerFromArgumentIndex() throws Throwable {	
		convertMethodArgumentInterceptor.setFromArgumentIndex( 2 );
		
		MethodInvocation methodInvocation = createMock(MethodInvocation.class);
		
		expect( methodInvocation.getArguments() ).andReturn( new Object[]{"arg1"} );
		
		replay(methodInvocation);
		try{ convertMethodArgumentInterceptor.invoke( methodInvocation ); fail(); }
		catch(IllegalArgumentException e){ assertEquals( "fromArgumentIndex 2 is out of bounds with arguments count of 1",  e.getMessage() ); }
		verify(methodInvocation);
	}
	
	@Test
	public void testWith_SingleArgumentMethod_BiggerToArgumentIndex() throws Throwable {	
		convertMethodArgumentInterceptor.setToArgumentIndex( 2 );
		
		MethodInvocation methodInvocation = createMock(MethodInvocation.class);
		
		expect( methodInvocation.getArguments() ).andReturn( new Object[]{"arg1"} );
		
		replay(methodInvocation);
		try{ convertMethodArgumentInterceptor.invoke( methodInvocation ); fail(); }
		catch(IllegalArgumentException e){ assertEquals( "toArgumentIndex 2 is out of bounds with arguments count of 1", e.getMessage() ); }
		verify(methodInvocation);
	}
	
	
	@Test
	public void testWith_SingleArgumentMethod_BadFromPropertyPath() throws Throwable {	
		convertMethodArgumentInterceptor.setToArgumentIndex( 0 );
		convertMethodArgumentInterceptor.setFromPropertyPath( "bad" );
		
		MethodInvocation methodInvocation = createMock(MethodInvocation.class);
		
		expect( methodInvocation.getArguments() ).andReturn( new Object[]{"arg1"} );
		
		replay(methodInvocation);
		try{ convertMethodArgumentInterceptor.invoke( methodInvocation ); fail(); }
		catch(RuntimeException e){ assertTrue( e.getCause() instanceof NoSuchMethodException ); }
		verify(methodInvocation);
	}
	
	@Test
	public void testWith_SingleArgumentMethod_BadToPropertyPath() throws Throwable {	
		convertMethodArgumentInterceptor.setFromArgumentIndex( 0 );
		
		convertMethodArgumentInterceptor.setToArgumentIndex( 0 );
		convertMethodArgumentInterceptor.setToPropertyPath( "bad" );
		
		MethodInvocation methodInvocation = createMock(MethodInvocation.class);
		Object[] args = new Object[]{"arg1"};
		
		expect( methodInvocation.getArguments() ).andReturn( args );
		
		Converter converter = createMock(Converter.class);
		convertMethodArgumentInterceptor.setConverter( converter );		
		expect( converter.convert( "arg1" ) ).andReturn( "convertedArg1" );
		
		replay(methodInvocation, converter);
		try{ convertMethodArgumentInterceptor.invoke( methodInvocation ); fail(); }
		catch(RuntimeException e){ assertTrue( e.getCause() instanceof NoSuchMethodException ); }
		verify(methodInvocation, converter);
	}
	
	
	@Test
	public void testWith_SingleArgumentMethod() throws Throwable {			
		MethodInvocation methodInvocation = createMock(MethodInvocation.class);
		
		Object[] args = new Object[]{"arg1"};
		
		expect( methodInvocation.getArguments() ).andReturn(args);		
		expect( methodInvocation.proceed() ).andReturn( "proceededReturnValue" );
				
		Converter converter = createMock(Converter.class);
		convertMethodArgumentInterceptor.setConverter( converter );		
		expect( converter.convert( "arg1" ) ).andReturn( "convertedArg1" );
		
		replay(methodInvocation, converter);
		assertEquals( "proceededReturnValue", convertMethodArgumentInterceptor.invoke( methodInvocation ) );
		verify(methodInvocation, converter);
		
		assertEquals( 1, args.length );
		assertEquals( "convertedArg1", args[0] );
	}
	
	
	@Test
	public void testWith_MultyArgumentMethod_StringArgument() throws Throwable {			
		MethodInvocation methodInvocation = createMock(MethodInvocation.class);
				
		Object[] args = new Object[]{"arg1", null, null, null, null};
		
		expect( methodInvocation.getArguments() ).andReturn(args);		
		expect( methodInvocation.proceed() ).andReturn( "proceededReturnValue" );
				
		Converter converter = createMock(Converter.class);
		convertMethodArgumentInterceptor.setConverter( converter );		
		expect( converter.convert( "arg1" ) ).andReturn( "convertedArg1" );
		
		replay(methodInvocation, converter);
		assertEquals( "proceededReturnValue", convertMethodArgumentInterceptor.invoke( methodInvocation ) );
		verify(methodInvocation, converter);
		
		assertEquals( 5, args.length );
		assertEquals( "convertedArg1", args[0] );
		assertNull( args[1] );
		assertNull( args[2] );
		assertNull( args[3] );
		assertNull( args[4] );
	}
	
	
	@Test
	public void testWith_MultyArgumentMethod_StringArrayToObjectList() throws Throwable {			
		MethodInvocation methodInvocation = createMock(MethodInvocation.class);
				
		String[] stringArrayArg = new String[]{"sta1", "st2"};
		
		List<Object> objectListArg = Arrays.<Object>asList( new Object[]{"ol1", "ol2"} );
		
		Object[] args = new Object[]{null, stringArrayArg, objectListArg, null, null};
		
		convertMethodArgumentInterceptor.setFromArgumentIndex( 1 );
		convertMethodArgumentInterceptor.setFromPropertyPath( "[1]" );
		
		convertMethodArgumentInterceptor.setToArgumentIndex( 2 );
		convertMethodArgumentInterceptor.setToPropertyPath( "[0]" );
				
		expect( methodInvocation.getArguments() ).andReturn(args);		
		expect( methodInvocation.proceed() ).andReturn( "proceededReturnValue" );
				
		Converter converter = createMock(Converter.class);
		convertMethodArgumentInterceptor.setConverter( converter );		
		expect( converter.convert( "st2" ) ).andReturn( "convertedArg1" );
		
		replay(methodInvocation, converter);
		assertEquals( "proceededReturnValue", convertMethodArgumentInterceptor.invoke( methodInvocation ) );
		verify(methodInvocation, converter);
		
		assertEquals( 5, args.length );
		assertNull( args[0] );
		assertSame( stringArrayArg, args[1] );
		assertEquals( Arrays.<Object>asList( new Object[]{"convertedArg1", "ol2"} ), args[2] );
		assertNull( args[3] );
		assertNull( args[4] );
	}
	
	
	@Test
	public void testWith_MultyArgumentMethod_ObjectMapToItself() throws Throwable {			
		MethodInvocation methodInvocation = createMock(MethodInvocation.class);
				
		Map<Object, Object> objectMapArg = new HashMap<Object, Object>();
		objectMapArg.put( "omk1", "omv1" );
		objectMapArg.put( "omk2", "omv2" );
						
		Object[] args = new Object[]{null, null, null, objectMapArg, null};
		
		convertMethodArgumentInterceptor.setFromArgumentIndex( 3 );
		convertMethodArgumentInterceptor.setFromPropertyPath( "omk1" );
		
		convertMethodArgumentInterceptor.setToArgumentIndex( 3 );
		convertMethodArgumentInterceptor.setToPropertyPath( "omk1" );
				
		expect( methodInvocation.getArguments() ).andReturn(args);		
		expect( methodInvocation.proceed() ).andReturn( "proceededReturnValue" );
				
		Converter converter = createMock(Converter.class);
		convertMethodArgumentInterceptor.setConverter( converter );		
		expect( converter.convert( "omv1" ) ).andReturn( "convertedOmv1" );
		
		replay(methodInvocation, converter);
		assertEquals( "proceededReturnValue", convertMethodArgumentInterceptor.invoke( methodInvocation ) );
		verify(methodInvocation, converter);
		
		assertEquals( 5, args.length );
		assertNull( args[0] );
		assertNull( args[1] );
		assertNull( args[2] );
		assertSame( objectMapArg, args[3] );
		assertEquals( objectMapArg.size(), 2 );
		assertEquals( objectMapArg.get("omk1"), "convertedOmv1" );
		assertEquals( objectMapArg.get("omk2"), "omv2" );
		assertNull( args[4] );
	}
	
	
	@Test
	public void testWith_MultyArgumentMethod_ObjectMapToComplexObjectListProperty() throws Throwable {			
		MethodInvocation methodInvocation = createMock(MethodInvocation.class);
				
		Map<Object, Object> objectMapArg = new HashMap<Object, Object>();
		objectMapArg.put( "omk1", "omv1" );
		objectMapArg.put( "omk2", "omv2" );
		
		ComplexObject complexObjectArg = new ComplexObject();
		List<String> stringListProperty = Arrays.<String>asList( new String[]{"cosl1", "cosl2"} );
		complexObjectArg.setStringListProperty( stringListProperty );
		
		Object[] args = new Object[]{null, null, null, objectMapArg, complexObjectArg};
		
		convertMethodArgumentInterceptor.setFromArgumentIndex( 3 );
		convertMethodArgumentInterceptor.setFromPropertyPath( "omk1" );
		
		convertMethodArgumentInterceptor.setToArgumentIndex( 4 );
		convertMethodArgumentInterceptor.setToPropertyPath( "stringListProperty[1]" );
				
		expect( methodInvocation.getArguments() ).andReturn(args);		
		expect( methodInvocation.proceed() ).andReturn( "proceededReturnValue" );
				
		Converter converter = createMock(Converter.class);
		convertMethodArgumentInterceptor.setConverter( converter );		
		expect( converter.convert( "omv1" ) ).andReturn( "convertedOmv1" );
		
		replay(methodInvocation, converter);
		assertEquals( "proceededReturnValue", convertMethodArgumentInterceptor.invoke( methodInvocation ) );
		verify(methodInvocation, converter);
		
		assertEquals( 5, args.length );
		assertNull( args[0] );
		assertNull( args[1] );
		assertNull( args[2] );
		assertSame( objectMapArg, args[3] );
		assertEquals( Arrays.<Object>asList( new Object[]{"cosl1", "convertedOmv1"} ), ( (ComplexObject)args[4] ).getStringListProperty() );
	}
	
	
	@Test
	public void testWith_MultyArgumentMethod_ComplexObjectStringMapPropertyToString() throws Throwable {			
		MethodInvocation methodInvocation = createMock(MethodInvocation.class);
				
		ComplexObject complexObjectArg = new ComplexObject();
		Map<String, String> stringMapProperty = new HashMap<String, String>();
		stringMapProperty.put( "smpk1", "smpv1" );
		stringMapProperty.put( "smpk2", "smpv2" );
		complexObjectArg.setStringMapProperty( stringMapProperty );
				
		Object[] args = new Object[]{"stringArg", null, null, null, complexObjectArg};
		
		convertMethodArgumentInterceptor.setFromArgumentIndex( 4 );
		convertMethodArgumentInterceptor.setFromPropertyPath( "stringMapProperty.smpk1" );
		
		convertMethodArgumentInterceptor.setToArgumentIndex( 0 );
				
		expect( methodInvocation.getArguments() ).andReturn(args);		
		expect( methodInvocation.proceed() ).andReturn( "proceededReturnValue" );
				
		Converter converter = createMock(Converter.class);
		convertMethodArgumentInterceptor.setConverter( converter );		
		expect( converter.convert( "smpv1" ) ).andReturn( "convertedSmpv1" );
		
		replay(methodInvocation, converter);
		assertEquals( "proceededReturnValue", convertMethodArgumentInterceptor.invoke( methodInvocation ) );
		verify(methodInvocation, converter);
		
		assertEquals( 5, args.length );
		assertEquals( "convertedSmpv1", args[0] );
		assertNull( args[1] );
		assertNull( args[2] );
		assertNull( args[3] );
		assertSame( complexObjectArg, args[4] );
		assertSame( stringMapProperty, complexObjectArg.getStringMapProperty() );
		assertEquals( complexObjectArg.getStringMapProperty().size(), 2 );
		assertEquals( complexObjectArg.getStringMapProperty().get("smpk1"), "smpv1" );
		assertEquals( complexObjectArg.getStringMapProperty().get("smpk2"), "smpv2" );
	}
}
