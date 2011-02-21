package org.equilibriums.aop.utils.interceptor.convert;

import java.util.*;

import org.junit.*;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import org.aopalliance.intercept.MethodInvocation;

public class ConvertMethodReturnValueInterceptorTest {
 
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
	
	private ConvertMethodReturnValueInterceptor convertMethodReturnValueInterceptor = null;
	
	@Before
	public void initialize(){
		convertMethodReturnValueInterceptor = new ConvertMethodReturnValueInterceptor();
	}
	
	@Test
	public void testWith_VoidMethod() throws Throwable {	
		MethodInvocation methodInvocation = createMock(MethodInvocation.class);
		
		expect( methodInvocation.proceed() ).andReturn(null);
		
		Converter converter = createMock(Converter.class);
		convertMethodReturnValueInterceptor.setConverter( converter );		
		expect( converter.convert( null ) ).andReturn( "convertedArg1" );
		
		replay(methodInvocation, converter);
		assertEquals( "convertedArg1", convertMethodReturnValueInterceptor.invoke( methodInvocation ) );
		verify(methodInvocation, converter);
	}
	
	
	@Test
	public void testWith_BadFromPropertyPath() throws Throwable {	
		convertMethodReturnValueInterceptor.setFromPropertyPath( "bad" );
			
		MethodInvocation methodInvocation = createMock(MethodInvocation.class);
		expect( methodInvocation.proceed() ).andReturn("proceededReturnValue");
		
		replay(methodInvocation);
		try{ convertMethodReturnValueInterceptor.invoke( methodInvocation ); fail(); }
		catch(RuntimeException e){ assertTrue( e.getCause() instanceof NoSuchMethodException ); }
		verify(methodInvocation);
	}
	
	@Test
	public void testWith_BadToPropertyPath() throws Throwable {	
		convertMethodReturnValueInterceptor.setToPropertyPath( "bad" );
			
		MethodInvocation methodInvocation = createMock(MethodInvocation.class);
		expect( methodInvocation.proceed() ).andReturn("proceededReturnValue");
		
		Converter converter = createMock(Converter.class);
		convertMethodReturnValueInterceptor.setConverter( converter );		
		expect( converter.convert( "proceededReturnValue" ) ).andReturn( "convertedProceededReturnValue" );
		
		replay(methodInvocation, converter);
		try{ convertMethodReturnValueInterceptor.invoke( methodInvocation ); fail(); }
		catch(RuntimeException e){ assertTrue( e.getCause() instanceof NoSuchMethodException ); }
		verify(methodInvocation, converter);
	}
	
	
	@Test
	public void testWith_StringReturnValueMethod() throws Throwable {	
			
		MethodInvocation methodInvocation = createMock(MethodInvocation.class);
		expect( methodInvocation.proceed() ).andReturn("proceededReturnValue");
		
		Converter converter = createMock(Converter.class);
		convertMethodReturnValueInterceptor.setConverter( converter );		
		expect( converter.convert( "proceededReturnValue" ) ).andReturn( "convertedProceededReturnValue" );
		
		replay(methodInvocation, converter);
		assertEquals( "convertedProceededReturnValue", convertMethodReturnValueInterceptor.invoke( methodInvocation ) );		
		verify(methodInvocation, converter);
	}
	
	
    @Test
	@SuppressWarnings( "unchecked" )
	public void testWith_StringMapReturnValueMethod() throws Throwable {	
		
		Map<String, String> returnValue = new HashMap<String, String>();
		returnValue.put("mrvk1", "mrvv1" );
		
		convertMethodReturnValueInterceptor.setFromPropertyPath( "mrvk1" );
		convertMethodReturnValueInterceptor.setToPropertyPath( "mrvk1" );
		
		MethodInvocation methodInvocation = createMock(MethodInvocation.class);
		expect( methodInvocation.proceed() ).andReturn(returnValue);
		
		Converter converter = createMock(Converter.class);
		convertMethodReturnValueInterceptor.setConverter( converter );		
		expect( converter.convert( "mrvv1" ) ).andReturn( "convertedMrvv1" );
		
		replay(methodInvocation, converter);
		Map<String, String> result = (Map<String, String>)convertMethodReturnValueInterceptor.invoke( methodInvocation );	
		verify(methodInvocation, converter);
		
		assertSame(returnValue, result);
		assertEquals(1, result.size() );
		assertEquals("convertedMrvv1", result.get("mrvk1") );
	}
    
    
    @Test
	public void testWith_ComplexObjectReturnValueMethod_StringListPropertyToStringMapProperty() throws Throwable {	
		
    	ComplexObject complexObjectReturnValue = new ComplexObject();
    	
    	List<String> stringListProperty = Arrays.<String>asList( new String[]{"slp1", "slp2"} );
    	complexObjectReturnValue.setStringListProperty( stringListProperty );
    	
		Map<String, String> stringMapProperty = new HashMap<String, String>();
		stringMapProperty.put("mrvk1", "mrvv1" );
		stringMapProperty.put("mrvk2", "mrvv2" );
		complexObjectReturnValue.setStringMapProperty( stringMapProperty );
		
		convertMethodReturnValueInterceptor.setFromPropertyPath( "stringListProperty[1]" );
		convertMethodReturnValueInterceptor.setToPropertyPath( "stringMapProperty.mrvk2" );
		
		MethodInvocation methodInvocation = createMock(MethodInvocation.class);
		expect( methodInvocation.proceed() ).andReturn(complexObjectReturnValue);
		
		Converter converter = createMock(Converter.class);
		convertMethodReturnValueInterceptor.setConverter( converter );		
		expect( converter.convert( "slp2" ) ).andReturn( "convertedSlp2" );
		
		replay(methodInvocation, converter);
		ComplexObject result = (ComplexObject)convertMethodReturnValueInterceptor.invoke( methodInvocation );	
		verify(methodInvocation, converter);
		
		assertSame(complexObjectReturnValue, result);
		
		List<String> resultStringListProperty = result.getStringListProperty();
		assertEquals( 2, resultStringListProperty.size() );
		assertEquals( "slp1", resultStringListProperty.get(0) );
		assertEquals( "slp2", resultStringListProperty.get(1) );
		
		Map<String, String> resultStringMapProperty = result.getStringMapProperty();
		assertEquals( 2, resultStringMapProperty.size() );
		assertEquals("mrvv1", resultStringMapProperty.get("mrvk1") );
		assertEquals("convertedSlp2", resultStringMapProperty.get("mrvk2") );
	}
}
