package org.equilibriums.aop.utils.interceptor.delegate.handlers;

import java.util.List;
import java.util.ArrayList;

import org.junit.*;
import static org.junit.Assert.*;

public class BooleanReturnValueHandlerTest {

	private BooleanReturnValueHandler booleanReturnValueHandler = null;
	
	@Before
	public void initialize(){
		booleanReturnValueHandler = new BooleanReturnValueHandler();
	}
	
	@Test
	public void testSupports_NonBooleanReturnType(){
		assertFalse( booleanReturnValueHandler.supports( String.class, null, null ) );
	}
	
	@Test
	public void testSupports_BooleanReturnType(){
		assertTrue( booleanReturnValueHandler.supports( Boolean.class, null, null ) );
	}
	
	@Test
	public void testSupports_BooleanTypeReturnType(){
		assertTrue( booleanReturnValueHandler.supports( Boolean.TYPE, null, null ) );
	}
	
	
	@Test
	public void testGetReturnValue_SingleBooleanReturnValue_NullProceededReturnValue_OROperator(){
		List<Object> returnValues = new ArrayList<Object>();
		returnValues.add( Boolean.TRUE );
		
		booleanReturnValueHandler.setBooleanOperator( BooleanReturnValueHandler.BooleanOperator.OR );
		assertTrue( (Boolean)booleanReturnValueHandler.getReturnValue( Boolean.class, returnValues, null ) );
	}
	
	@Test
	public void testGetReturnValue_SingleBooleanReturnValue_NullProceededReturnValue_ANDOperator(){
		List<Object> returnValues = new ArrayList<Object>();
		returnValues.add( Boolean.TRUE );
		
		booleanReturnValueHandler.setBooleanOperator( BooleanReturnValueHandler.BooleanOperator.AND );
		assertTrue( (Boolean)booleanReturnValueHandler.getReturnValue( Boolean.class, returnValues, null ) );
	}
	
	
	@Test
	public void testGetReturnValue_SingleBooleanReturnValue_ProceededReturnValue_OROperator(){
		List<Object> returnValues = new ArrayList<Object>();
		returnValues.add( Boolean.TRUE );
		
		booleanReturnValueHandler.setBooleanOperator( BooleanReturnValueHandler.BooleanOperator.OR );
		assertTrue( (Boolean)booleanReturnValueHandler.getReturnValue( Boolean.class, returnValues, Boolean.TRUE ) );
	}
	
	@Test
	public void testGetReturnValue_SingleBooleanReturnValue_ProceededReturnValue_ANDOperator(){
		List<Object> returnValues = new ArrayList<Object>();
		returnValues.add( Boolean.TRUE );
		
		booleanReturnValueHandler.setBooleanOperator( BooleanReturnValueHandler.BooleanOperator.AND );
		assertFalse( (Boolean)booleanReturnValueHandler.getReturnValue( Boolean.class, returnValues, Boolean.FALSE ) );
	}
	
	
	@Test
	public void testGetReturnValue_2BooleanReturnValue_NullProceededReturnValue_OROperator(){
		List<Object> returnValues = new ArrayList<Object>();
		returnValues.add( Boolean.TRUE );
		returnValues.add( Boolean.FALSE );
		
		booleanReturnValueHandler.setBooleanOperator( BooleanReturnValueHandler.BooleanOperator.OR );
		assertTrue( (Boolean)booleanReturnValueHandler.getReturnValue( Boolean.class, returnValues, null ) );
	}
	
	@Test
	public void testGetReturnValue_2BooleanReturnValue_NullProceededReturnValue_ANDOperator(){
		List<Object> returnValues = new ArrayList<Object>();
		returnValues.add( Boolean.TRUE );
		returnValues.add( Boolean.FALSE );
		
		booleanReturnValueHandler.setBooleanOperator( BooleanReturnValueHandler.BooleanOperator.AND );
		assertFalse( (Boolean)booleanReturnValueHandler.getReturnValue( Boolean.class, returnValues, null ) );
	}
	
	
	@Test
	public void testGetReturnValue_2BooleanReturnValue_ProceededReturnValue_OROperator(){
		List<Object> returnValues = new ArrayList<Object>();
		returnValues.add( Boolean.TRUE );
		returnValues.add( Boolean.FALSE );
		returnValues.add( null );
		
		booleanReturnValueHandler.setBooleanOperator( BooleanReturnValueHandler.BooleanOperator.OR );
		assertTrue( (Boolean)booleanReturnValueHandler.getReturnValue( Boolean.class, returnValues, Boolean.TRUE ) );
	}
	
	@Test
	public void testGetReturnValue_2BooleanReturnValue_ProceededReturnValue_ANDOperator(){
		List<Object> returnValues = new ArrayList<Object>();
		returnValues.add( Boolean.TRUE );
		returnValues.add( Boolean.TRUE );
		returnValues.add( null );
		
		booleanReturnValueHandler.setBooleanOperator( BooleanReturnValueHandler.BooleanOperator.AND );
		assertTrue( (Boolean)booleanReturnValueHandler.getReturnValue( Boolean.class, returnValues, Boolean.TRUE ) );
	}
	
}
