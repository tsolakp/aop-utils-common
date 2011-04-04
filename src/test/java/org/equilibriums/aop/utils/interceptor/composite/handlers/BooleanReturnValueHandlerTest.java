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
		assertFalse( booleanReturnValueHandler.supports( String.class, null ) );
	}
	
	@Test
	public void testSupports_BooleanReturnType(){
		assertTrue( booleanReturnValueHandler.supports( Boolean.class, null ) );
	}
	
	@Test
	public void testSupports_BooleanTypeReturnType(){
		assertTrue( booleanReturnValueHandler.supports( Boolean.TYPE, null ) );
	}
	
	
	@Test
	public void testGetReturnValue_SingleBooleanReturnValue_OROperator(){
		Object[] returnValues = new Object[]{ Boolean.TRUE };
		
		booleanReturnValueHandler.setBooleanOperator( BooleanReturnValueHandler.BooleanOperator.OR );
		assertTrue( (Boolean)booleanReturnValueHandler.getReturnValue( Boolean.class, returnValues ) );
	}
	
	@Test
	public void testGetReturnValue_SingleBooleanReturnValue_ANDOperator(){
		Object[] returnValues = new Object[]{ Boolean.TRUE };
		
		booleanReturnValueHandler.setBooleanOperator( BooleanReturnValueHandler.BooleanOperator.AND );
		assertTrue( (Boolean)booleanReturnValueHandler.getReturnValue( Boolean.class, returnValues ) );
	}
	
	
	@Test
	public void testGetReturnValue_2BooleanReturnValue_OROperator(){
		Object[] returnValues = new Object[]{ Boolean.TRUE, Boolean.FALSE };
		
		booleanReturnValueHandler.setBooleanOperator( BooleanReturnValueHandler.BooleanOperator.OR );
		assertTrue( (Boolean)booleanReturnValueHandler.getReturnValue( Boolean.class, returnValues ) );
	}
	
	@Test
	public void testGetReturnValue_2BooleanReturnValue_ANDOperator(){
		Object[] returnValues = new Object[]{ Boolean.TRUE, Boolean.FALSE };
		
		booleanReturnValueHandler.setBooleanOperator( BooleanReturnValueHandler.BooleanOperator.AND );
		assertFalse( (Boolean)booleanReturnValueHandler.getReturnValue( Boolean.class, returnValues ) );
	}	
}
