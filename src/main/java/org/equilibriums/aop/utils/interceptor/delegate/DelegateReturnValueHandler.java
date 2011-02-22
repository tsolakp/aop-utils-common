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
package org.equilibriums.aop.utils.interceptor.delegate;

import java.util.List;

/**
 * <p>Strategy interface to be implemented to do custom handling of return values from calls to delegates.</p>
 * <p>See actual implementation as an examples.</p>
 * 
 * @author Tsolak Petrosian
 */
public interface DelegateReturnValueHandler {

	/**
	 * Called by {@link DelegateInterceptor} after all delegates are invoked with their retun values.
	 * 
	 * @param returnType
	 * @param returnValues
	 * @return If true than {@link getReturnValue} will be called with same returnType and returnValues.
	 */
	public boolean supports( Class<? extends Object> returnType, List< Object > returnValues );
	
	public Object getReturnValue( Class<? extends Object> returnType, List< Object > returnValues );
}
