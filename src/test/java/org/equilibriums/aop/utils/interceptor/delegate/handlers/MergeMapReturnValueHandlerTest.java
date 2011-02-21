package org.equilibriums.aop.utils.interceptor.delegate.handlers;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.*;

import static org.junit.Assert.*;

public class MergeMapReturnValueHandlerTest {

    private MergeMapReturnValueHandler mergeMapReturnValueHandler = null;
	
	@Before
	public void initialize(){
		mergeMapReturnValueHandler = new MergeMapReturnValueHandler();
	}
	
	@Test
	public void testSupports_ObjectReturnType(){
		assertFalse( mergeMapReturnValueHandler.supports( Object.class, null, null ) );
	}
	
	@Test
	public void testSupports_MapReturnType(){
		assertTrue( mergeMapReturnValueHandler.supports( Map.class, null, null ) );
	}
	
	@Test
	public void testSupports_HashMapReturnType(){
		assertTrue( mergeMapReturnValueHandler.supports( HashMap.class, null, null ) );
	}
	
	@Test
	public void testSupports_CollectionReturnType(){
		assertFalse( mergeMapReturnValueHandler.supports( Collection.class, null, null ) );
	}
	
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_SingleEmptyMapReturnValue_NullProceededReturnValue(){
		List<Object> returnValues = new ArrayList<Object>();
		
		HashMap<Object, Object> o1 = new HashMap<Object, Object>();
		returnValues.add( o1 );
		
		HashMap<Object, Object> result = ( HashMap<Object, Object> )mergeMapReturnValueHandler.getReturnValue( Object.class, returnValues, null );
		
		assertEquals( result.size(), 0 );
	}
    
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_SingleEmptyMapReturnValue_EmptyProceededMapReturnValue(){
    	mergeMapReturnValueHandler.setMapClass( TreeMap.class );
    	List<Object> returnValues = new ArrayList<Object>();
		
		HashMap<Object, Object> o1 = new HashMap<Object, Object>();
		returnValues.add( o1 );
		
		HashMap<Object, Object> proceededReturnValue = new HashMap<Object, Object>();
		
		TreeMap<Object, Object> result = ( TreeMap<Object, Object> )mergeMapReturnValueHandler.getReturnValue( Object.class, returnValues, proceededReturnValue );
		
		assertEquals( result.size(), 0 );
	}
    
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_SingleEmptyMapReturnValue_PopulatedProceededMapReturnValue(){
    	mergeMapReturnValueHandler.setMapClass( TreeMap.class );
    	List<Object> returnValues = new ArrayList<Object>();
		
		HashMap<Object, Object> o1 = new HashMap<Object, Object>();
		returnValues.add( o1 );
		
		HashMap<Object, Object> proceededReturnValue = new HashMap<Object, Object>();
		Object proceededReturnValue1 = new Object();
		proceededReturnValue.put( "proceededReturnValue1", proceededReturnValue1 );
		
		TreeMap<Object, Object> result = ( TreeMap<Object, Object> )mergeMapReturnValueHandler.getReturnValue( Object.class, returnValues, proceededReturnValue );
		
		assertEquals( result.size(), 1 );
		assertEquals( result.get( "proceededReturnValue1" ), proceededReturnValue1 );
	}
    
    
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_SinglePopulatedMapReturnValue_NullProceededReturnValue(){
    	mergeMapReturnValueHandler.setMapClass( TreeMap.class );
    	
		List<Object> returnValues = new ArrayList<Object>();
		
		HashMap<Object, Object> o1 = new HashMap<Object, Object>();
		Object o11 = new Object();
		o1.put( "o11", o11 );
		returnValues.add( o1 );
		
		TreeMap<Object, Object> result = ( TreeMap<Object, Object> )mergeMapReturnValueHandler.getReturnValue( Object.class, returnValues, null );
		
		assertEquals( result.size(), 1 );
		assertEquals( result.get( "o11" ), o11 );
	}
    
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_SinglePopulatedMapReturnValue_EmptyProceededMapReturnValue(){
		List<Object> returnValues = new ArrayList<Object>();
		
		HashMap<Object, Object> o1 = new HashMap<Object, Object>();
		Object o11 = new Object();
		o1.put( "o11", o11 );
		returnValues.add( o1 );
		
		HashMap<Object, Object> proceededReturnValue = new HashMap<Object, Object>();
		
		HashMap<Object, Object> result = ( HashMap<Object, Object> )mergeMapReturnValueHandler.getReturnValue( Object.class, returnValues, proceededReturnValue );
		
		assertEquals( result.size(), 1 );
		assertEquals( result.get( "o11" ), o11 );
	}
    
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_SinglePopulatedMapReturnValue_PopulatedProceededMapReturnValue(){
		List<Object> returnValues = new ArrayList<Object>();
		
		HashMap<Object, Object> o1 = new HashMap<Object, Object>();
		Object o11 = new Object();
		o1.put( "o11", o11 );
		returnValues.add( o1 );
		
		HashMap<Object, Object> proceededReturnValue = new HashMap<Object, Object>();
		Object proceededReturnValue1 = new Object();
		proceededReturnValue.put( "proceededReturnValue1", proceededReturnValue1 );
		
		HashMap<Object, Object> result = ( HashMap<Object, Object> )mergeMapReturnValueHandler.getReturnValue( Object.class, returnValues, proceededReturnValue );
		
		assertEquals( result.size(), 2 );
		assertEquals( result.get( "o11" ), o11 );
		assertEquals( result.get( "proceededReturnValue1" ), proceededReturnValue1 );
	}
    
    
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_2EmptyMapReturnValue_NullProceededReturnValue(){
		List<Object> returnValues = new ArrayList<Object>();
		
		HashMap<Object, Object> o1 = new HashMap<Object, Object>();
		returnValues.add( o1 );
		
		HashMap<Object, Object> o2 = new HashMap<Object, Object>();
		returnValues.add( o2 );
		
		HashMap<Object, Object> result = ( HashMap<Object, Object> )mergeMapReturnValueHandler.getReturnValue( Object.class, returnValues, null );
		
		assertEquals( result.size(), 0 );
	}
    
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_2EmptyMapReturnValue_EmptyProceededMapReturnValue(){
    	mergeMapReturnValueHandler.setMapClass( TreeMap.class );
    	List<Object> returnValues = new ArrayList<Object>();
		
		HashMap<Object, Object> o1 = new HashMap<Object, Object>();
		returnValues.add( o1 );
		
		HashMap<Object, Object> o2 = new HashMap<Object, Object>();
		returnValues.add( o2 );
		
		HashMap<Object, Object> proceededReturnValue = new HashMap<Object, Object>();
		
		TreeMap<Object, Object> result = ( TreeMap<Object, Object> )mergeMapReturnValueHandler.getReturnValue( Object.class, returnValues, proceededReturnValue );
		
		assertEquals( result.size(), 0 );
	}
    
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_2EmptyMapReturnValue__null_PoulatedProceededMapReturnValue(){
    	List<Object> returnValues = new ArrayList<Object>();
		
		HashMap<Object, Object> o1 = new HashMap<Object, Object>();
		returnValues.add( o1 );
		
		HashMap<Object, Object> o2 = new HashMap<Object, Object>();
		returnValues.add( o2 );
		
		returnValues.add(null);
		
		HashMap<Object, Object> proceededReturnValue = new HashMap<Object, Object>();
		Object proceededReturnValue1 = new Object();
		proceededReturnValue.put( "proceededReturnValue1", proceededReturnValue1 );
		
		HashMap<Object, Object> result = ( HashMap<Object, Object> )mergeMapReturnValueHandler.getReturnValue( Object.class, returnValues, proceededReturnValue );
		
		assertEquals( result.size(), 1 );
		assertEquals( result.get("proceededReturnValue1"), proceededReturnValue1 );
	}
    
    
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_2PopulatedMapReturnValue_NullProceededReturnValue(){
		List<Object> returnValues = new ArrayList<Object>();
		
		HashMap<Object, Object> o1 = new HashMap<Object, Object>();
		Object o11 = new Object();
		o1.put( "o11", o11 );
		returnValues.add( o1 );
		
		HashMap<Object, Object> o2 = new HashMap<Object, Object>();
		Object o22 = new Object();
		o2.put( "o22", o22 );
		returnValues.add( o2 );
		
		HashMap<Object, Object> o3 = new HashMap<Object, Object>();
		returnValues.add( o3 );
		
		HashMap<Object, Object> result = ( HashMap<Object, Object> )mergeMapReturnValueHandler.getReturnValue( Object.class, returnValues, null );
		
		assertEquals( result.size(), 2 );
		assertEquals( result.get("o11"), o11 );
		assertEquals( result.get("o22"), o22 );
	}
    
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_2PopulatedMapReturnValue_EmptyProceededMapReturnValue(){
    	mergeMapReturnValueHandler.setMapClass( TreeMap.class );
    	List<Object> returnValues = new ArrayList<Object>();
		
    	HashMap<Object, Object> o1 = new HashMap<Object, Object>();
		Object o11 = new Object();
		o1.put( "o11", o11 );
		returnValues.add( o1 );
		
		HashMap<Object, Object> o2 = new HashMap<Object, Object>();
		Object o22 = new Object();
		o2.put( "o22", o22 );
		returnValues.add( o2 );
		
		HashMap<Object, Object> proceededReturnValue = new HashMap<Object, Object>();
		
		TreeMap<Object, Object> result = ( TreeMap<Object, Object> )mergeMapReturnValueHandler.getReturnValue( Object.class, returnValues, proceededReturnValue );
		
		assertEquals( result.size(), 2 );
		assertEquals( result.get("o11"), o11 );
		assertEquals( result.get("o22"), o22 );
	}
    
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_2PopulatedMapReturnValue_null_PoulatedProceededMapReturnValue(){
    	List<Object> returnValues = new ArrayList<Object>();
		
    	HashMap<Object, Object> o1 = new HashMap<Object, Object>();
		Object o11 = new Object();
		o1.put( "o11", o11 );
		returnValues.add( o1 );
		
		HashMap<Object, Object> o2 = new HashMap<Object, Object>();
		Object o22 = new Object();
		o2.put( "o22", o22 );
		returnValues.add( o2 );
		
		returnValues.add(null);
		
		HashMap<Object, Object> proceededReturnValue = new HashMap<Object, Object>();
		Object proceededReturnValue1 = new Object();
		proceededReturnValue.put( "proceededReturnValue1", proceededReturnValue1 );
		
		HashMap<Object, Object> result = ( HashMap<Object, Object> )mergeMapReturnValueHandler.getReturnValue( Object.class, returnValues, proceededReturnValue );
		
		assertEquals( result.size(), 3 );
		assertEquals( result.get("o11"), o11 );
		assertEquals( result.get("o22"), o22 );
		assertEquals( result.get("proceededReturnValue1"), proceededReturnValue1 );
	}
}
