package org.equilibriums.aop.utils.interceptor.delegate.handlers;

import java.util.Map;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.*;

import static org.junit.Assert.*;

public class MergeCollectionReturnValueHandlerTest {

    private MergeCollectionReturnValueHandler mergeCollectionReturnValueHandler = null;
	
	@Before
	public void initialize(){
		mergeCollectionReturnValueHandler = new MergeCollectionReturnValueHandler();
	}
	
	@Test
	public void testSupports_ObjectReturnType(){
		assertFalse( mergeCollectionReturnValueHandler.supports( Object.class, null, null ) );
	}
	
	@Test
	public void testSupports_CollectionReturnType(){
		assertTrue( mergeCollectionReturnValueHandler.supports( Collection.class, null, null ) );
	}
	
	@Test
	public void testSupports_ArrayListReturnType(){
		assertTrue( mergeCollectionReturnValueHandler.supports( ArrayList.class, null, null ) );
	}
	
	@Test
	public void testSupports_MapReturnType(){
		assertFalse( mergeCollectionReturnValueHandler.supports( Map.class, null, null ) );
	}
	
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_SingleEmptyListReturnValue_NullProceededReturnValue(){
		List<Object> returnValues = new ArrayList<Object>();
		
		List<Object> o1 = new ArrayList<Object>();
		returnValues.add( o1 );
		
		List<Object> result = ( List<Object> )mergeCollectionReturnValueHandler.getReturnValue( Object.class, returnValues, null );
		
		assertEquals( result.size(), 0 );
	}
    
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_SingleEmptyListReturnValue_EmptyProceededListReturnValue(){
    	mergeCollectionReturnValueHandler.setCollectionClass( HashSet.class );
    	List<Object> returnValues = new ArrayList<Object>();
		
		List<Object> o1 = new ArrayList<Object>();
		returnValues.add( o1 );
		
		List<Object> proceededReturnValue = new ArrayList<Object>();
		
		HashSet<Object> result = ( HashSet<Object> )mergeCollectionReturnValueHandler.getReturnValue( Object.class, returnValues, proceededReturnValue );
		
		assertEquals( result.size(), 0 );
	}
    
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_SingleEmptyListReturnValue_PopulatedProceededListReturnValue(){
    	mergeCollectionReturnValueHandler.setCollectionClass( HashSet.class );
    	List<Object> returnValues = new ArrayList<Object>();
		
		List<Object> o1 = new ArrayList<Object>();
		returnValues.add( o1 );
		
		List<Object> proceededReturnValue = new ArrayList<Object>();
		Object proceededReturnValue1 = new Object();
		proceededReturnValue.add( proceededReturnValue1 );
		
		HashSet<Object> result = ( HashSet<Object> )mergeCollectionReturnValueHandler.getReturnValue( Object.class, returnValues, proceededReturnValue );
		
		assertEquals( result.size(), 1 );
		assertTrue( result.contains( proceededReturnValue1 ) );
	}
    
    
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_SinglePopulatedListReturnValue_NullProceededReturnValue(){
    	mergeCollectionReturnValueHandler.setCollectionClass( HashSet.class );
    	
		List<Object> returnValues = new ArrayList<Object>();
		
		List<Object> o1 = new ArrayList<Object>();
		Object o11 = new Object();
		o1.add( o11 );
		returnValues.add( o1 );
		
		HashSet<Object> result = ( HashSet<Object> )mergeCollectionReturnValueHandler.getReturnValue( Object.class, returnValues, null );
		
		assertEquals( result.size(), 1 );
		assertTrue( result.contains( o11 ) );
	}
    
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_SinglePopulatedListReturnValue_EmptyProceededListReturnValue(){
		List<Object> returnValues = new ArrayList<Object>();
		
		List<Object> o1 = new ArrayList<Object>();
		Object o11 = new Object();
		o1.add( o11 );
		returnValues.add( o1 );
		
		List<Object> proceededReturnValue = new ArrayList<Object>();
		
		List<Object> result = ( List<Object> )mergeCollectionReturnValueHandler.getReturnValue( Object.class, returnValues, proceededReturnValue );
		
		assertEquals( result.size(), 1 );
		assertTrue( result.contains( o11 ) );;
	}
    
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_SinglePopulatedListReturnValue_PopulatedProceededListReturnValue(){
		List<Object> returnValues = new ArrayList<Object>();
		
		List<Object> o1 = new ArrayList<Object>();
		Object o11 = new Object();
		o1.add( o11 );
		returnValues.add( o1 );
		
		List<Object> proceededReturnValue = new ArrayList<Object>();
		Object proceededReturnValue1 = new Object();
		proceededReturnValue.add( proceededReturnValue1 );
		
		List<Object> result = ( List<Object> )mergeCollectionReturnValueHandler.getReturnValue( Object.class, returnValues, proceededReturnValue );
		
		assertEquals( result.size(), 2 );
		assertTrue( result.contains( o11 ) );
		assertTrue( result.contains( proceededReturnValue1 ) );
	}
    
    
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_2EmptyListReturnValue_NullProceededReturnValue(){
		List<Object> returnValues = new ArrayList<Object>();
		
		List<Object> o1 = new ArrayList<Object>();
		returnValues.add( o1 );
		
		List<Object> o2 = new ArrayList<Object>();
		returnValues.add( o2 );
		
		List<Object> result = ( List<Object> )mergeCollectionReturnValueHandler.getReturnValue( Object.class, returnValues, null );
		
		assertEquals( result.size(), 0 );
	}
    
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_2EmptyListReturnValue_EmptyProceededListReturnValue(){
    	mergeCollectionReturnValueHandler.setCollectionClass( HashSet.class );
    	List<Object> returnValues = new ArrayList<Object>();
		
		List<Object> o1 = new ArrayList<Object>();
		returnValues.add( o1 );
		
		List<Object> o2 = new ArrayList<Object>();
		returnValues.add( o2 );
		
		List<Object> proceededReturnValue = new ArrayList<Object>();
		
		HashSet<Object> result = ( HashSet<Object> )mergeCollectionReturnValueHandler.getReturnValue( Object.class, returnValues, proceededReturnValue );
		
		assertEquals( result.size(), 0 );
	}
    
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_2EmptyListReturnValue__null_PoulatedProceededListReturnValue(){
    	List<Object> returnValues = new ArrayList<Object>();
		
		List<Object> o1 = new ArrayList<Object>();
		returnValues.add( o1 );
		
		List<Object> o2 = new ArrayList<Object>();
		returnValues.add( o2 );
		
		returnValues.add(null);
		
		List<Object> proceededReturnValue = new ArrayList<Object>();
		Object proceededReturnValue1 = new Object();
		proceededReturnValue.add( proceededReturnValue1 );
		
		List<Object> result = ( List<Object> )mergeCollectionReturnValueHandler.getReturnValue( Object.class, returnValues, proceededReturnValue );
		
		assertEquals( result.size(), 1 );
		assertEquals( result.get(0), proceededReturnValue1 );
	}
    
    
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_2PopulatedListReturnValue_NullProceededReturnValue(){
		List<Object> returnValues = new ArrayList<Object>();
		
		List<Object> o1 = new ArrayList<Object>();
		Object o11 = new Object();
		o1.add( o11 );
		returnValues.add( o1 );
		
		List<Object> o2 = new ArrayList<Object>();
		Object o22 = new Object();
		o2.add( o22 );
		returnValues.add( o2 );
		
		List<Object> o3 = new ArrayList<Object>();
		returnValues.add( o3 );
		
		List<Object> result = ( List<Object> )mergeCollectionReturnValueHandler.getReturnValue( Object.class, returnValues, null );
		
		assertEquals( result.size(), 2 );
		assertEquals( result.get(0), o11 );
		assertEquals( result.get(1), o22 );
	}
    
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_2PopulatedListReturnValue_EmptyProceededListReturnValue(){
    	mergeCollectionReturnValueHandler.setCollectionClass( HashSet.class );
    	List<Object> returnValues = new ArrayList<Object>();
		
		List<Object> o1 = new ArrayList<Object>();
		Object o11 = new Object();
		o1.add( o11 );
		returnValues.add( o1 );
		
		List<Object> o2 = new ArrayList<Object>();
		Object o22 = new Object();
		o2.add( o22 );
		returnValues.add( o2 );
		
		List<Object> proceededReturnValue = new ArrayList<Object>();
		
		HashSet<Object> result = ( HashSet<Object> )mergeCollectionReturnValueHandler.getReturnValue( Object.class, returnValues, proceededReturnValue );
		
		assertEquals( result.size(), 2 );
		assertTrue( result.contains( o11 ) );
		assertTrue( result.contains( o22 ) );
	}
    
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_2PopulatedListReturnValue_null_PoulatedProceededListReturnValue(){
    	List<Object> returnValues = new ArrayList<Object>();
		
		List<Object> o1 = new ArrayList<Object>();
		Object o11 = new Object();
		o1.add( o11 );
		returnValues.add( o1 );
		
		List<Object> o2 = new ArrayList<Object>();
		Object o22 = new Object();
		o2.add( o22 );
		returnValues.add( o2 );
		
		returnValues.add(null);
		
		List<Object> proceededReturnValue = new ArrayList<Object>();
		Object proceededReturnValue1 = new Object();
		proceededReturnValue.add( proceededReturnValue1 );
		
		List<Object> result = ( List<Object> )mergeCollectionReturnValueHandler.getReturnValue( Object.class, returnValues, proceededReturnValue );
		
		assertEquals( result.size(), 3 );
		assertEquals( result.get(0), o11 );
		assertEquals( result.get(1), o22 );
		assertEquals( result.get(2), proceededReturnValue1 );
	}
}
