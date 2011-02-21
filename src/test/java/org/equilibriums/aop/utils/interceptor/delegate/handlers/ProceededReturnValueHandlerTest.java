package org.equilibriums.aop.utils.interceptor.delegate.handlers;

import java.util.List;
import java.util.ArrayList;

import org.junit.*;
import static org.junit.Assert.*;

public class ProceededReturnValueHandlerTest {

    private ProceededReturnValueHandler proceededReturnValueHandler = null;
	
	@Before
	public void initialize(){
		proceededReturnValueHandler = new ProceededReturnValueHandler();
	}
	
	@Test
	public void testSupports(){
		assertTrue( proceededReturnValueHandler.supports( Object.class, null, null ) );
	}
	
	@Test
	public void testGetReturnValue_SingleTarget_NullProceededReturnValue(){
		List<Object> returnValues = new ArrayList<Object>();
		returnValues.add( new Object() );
		
		assertNull( proceededReturnValueHandler.getReturnValue( Object.class, returnValues, null ) );
	}
	
	@Test
	public void testGetReturnValue_SingleTarget_ProceededReturnValue(){
		List<Object> returnValues = new ArrayList<Object>();
		returnValues.add( new Object() );
		
		Object proceededReturnValue = new Object();
		
		assertSame( proceededReturnValueHandler.getReturnValue( Object.class, returnValues, proceededReturnValue ), proceededReturnValue );
	}
	
	
	@Test
	public void testGetReturnValue_2Target_NullProceededReturnValue(){
		List<Object> returnValues = new ArrayList<Object>();
		returnValues.add( new Object() );
		returnValues.add( new Object() );
				
		assertNull( proceededReturnValueHandler.getReturnValue( Object.class, returnValues, null ) );
	}
	
	@Test
	public void testGetReturnValue_2Target_ProceededReturnValue(){
		List<Object> returnValues = new ArrayList<Object>();
		returnValues.add( new Object() );
		returnValues.add( new Object() );
		
		Object proceededReturnValue = new Object();
		
		assertSame( proceededReturnValueHandler.getReturnValue( Object.class, returnValues, proceededReturnValue ), proceededReturnValue );
	}
}
