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

import org.equilibriums.aop.utils.interceptor.composite.ReturnValueHandler;

/**
 * <p>Assumes returnType is of {@link Boolean} type.</p>
 * <p>Depending on BooleanOperator will return result of only "AND" or "OR" operation across all return values.</p>
 * 
 * @author Tsolak Petrosian
 */
public class BooleanReturnValueHandler implements ReturnValueHandler {

	public enum BooleanOperator{AND, OR};
	
	private BooleanOperator booleanOperator = BooleanOperator.AND; 
	
	public BooleanOperator getBooleanOperator() {
    	return booleanOperator;
    }

	public void setBooleanOperator( BooleanOperator booleanOperator ) {
    	this.booleanOperator = booleanOperator;
    }

	@Override
	public boolean supports( Class<? extends Object> returnType, Object[] returnValues ){
		return returnType.equals( Boolean.class ) || returnType.equals( Boolean.TYPE );
	}
	
	@Override
	public Object getReturnValue( Class<? extends Object> returnType, Object[] returnValues ){
		Boolean result = null;			
		for ( Object o:returnValues ) if (o != null) result = ( result == null ? (Boolean)o : 
		eval( result, (Boolean)o, booleanOperator ) );		
		return result;
	}
	
	private Boolean eval(Boolean b1, Boolean b2, BooleanOperator booleanOperator){
		if ( booleanOperator == BooleanOperator.AND ) return b1 && b2;
		else return b1 || b2;
	}
}
