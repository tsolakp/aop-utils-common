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
 * <p>Simply retuns element from returnValues at specified index.</p>
 * <p>{@link #supports} method will always return true.</p>
 * 
 * @author Tsolak Petrosian
 */
public class IndexedReturnValueHandler implements ReturnValueHandler {

	private Integer index = 0;
	
	public Integer getIndex() {
    	return index;
    }

	public void setIndex( Integer index ) {
    	this.index = index;
    }

	@Override
	public boolean supports( Class<? extends Object> returnType, Object[] returnValues ){
		return true;
	}
	
	@Override
	public Object getReturnValue( Class<? extends Object> returnType, Object[] returnValues ){
		return returnValues[index];
	}
}
