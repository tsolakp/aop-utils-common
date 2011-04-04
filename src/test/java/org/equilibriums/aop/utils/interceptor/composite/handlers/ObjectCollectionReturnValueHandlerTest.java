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
package org.equilibriums.aop.utils.interceptor.composite.handlers;

import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.*;
import static org.junit.Assert.*;

public class ObjectCollectionReturnValueHandlerTest {

    private ObjectCollectionReturnValueHandler objectCollectionReturnValueHandler = null;
	
	@Before
	public void initialize(){
		objectCollectionReturnValueHandler = new ObjectCollectionReturnValueHandler();
	}
	
	@Test
	public void testSupports_ObjectReturnType(){
		assertTrue( objectCollectionReturnValueHandler.supports( Object.class, null ) );
	}
	
	@Test
	public void testSupports_GenericCollectionReturnType(){
		assertFalse( objectCollectionReturnValueHandler.supports( Collection.class, null ) );
	}
	
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_SingleReturnValue(){
		Object o = new Object();
		
		Object[] returnValues = new Object[]{ o };
		
		List<Object> result = ( List<Object> )objectCollectionReturnValueHandler.getReturnValue( Object.class, returnValues );
		
		assertEquals( result.size(), 1 );
		assertEquals( result.get( 0 ), o );
	}
    
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_2ReturnValue(){
    	objectCollectionReturnValueHandler.setCollectionClass( HashSet.class );
    	
		Object o1 = new Object();
		Object o2 = new Object();
		
		Object[] returnValues = new Object[]{ o1, o2 };
		
		HashSet<Object> result = ( HashSet<Object> )objectCollectionReturnValueHandler.getReturnValue( Object.class, returnValues );
				
		assertEquals( result.size(), 2 );
		assertTrue( result.contains( o1 ) );
		assertTrue( result.contains( o2 ) );
	}
                
    
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_SingleListReturnValue(){
		List<Object> o = new ArrayList<Object>();
		
		Object[] returnValues = new Object[]{ o };
		
		List<Object> result = ( List<Object> )objectCollectionReturnValueHandler.getReturnValue( Object.class, returnValues );
		
		assertEquals( result.size(), 1 );
		assertEquals( result.get( 0 ), o );
	}
    
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_2ListReturnValue(){
		List<Object> o1 = new ArrayList<Object>();
		
		List<Object> o2 = new ArrayList<Object>();
		
		Object[] returnValues = new Object[]{ o1, o2 };
		
		List<Object> result = ( List<Object> )objectCollectionReturnValueHandler.getReturnValue( Object.class, returnValues );
		
		assertEquals( result.size(), 2 );
		assertEquals( result.get( 0 ), o1 );
		assertEquals( result.get( 1 ), o2 );
	}
}
