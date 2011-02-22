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
package org.equilibriums.aop.utils.interceptor.convert;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * Will take return value from {@link org.aopalliance.intercept.MethodInvocation#proceed()} get the property specified by {@link #getFromPropertyPath}, 
 * convert it using specified {@link Converter} and replace property specified by 
 * {@link #getToPropertyPath}.
 * </p>
 * 
 * <p>The paths are defines according to {@link org.apache.commons.lang.StringUtils#getProperty} or {@link org.apache.commons.lang.StringUtils#setProperty} documentation.
 * 
 * <p>For example given method "public String method1();
 * To convert return String value null as {@link #getFromPropertyPath}
 * and null as {@link #getToPropertyPath}.
 * </p>
 *  
 * <p>For example given method "public Map method1();
 * To convert value defined by key "key1" use [key1] as {@link #getFromPropertyPath}
 * and [key1] as {@link #getToPropertyPath}.
 * </p>
 * 
 * </p>
 * 
 * <p><u>For more on how to use this interceptor see Overview section of this Javadoc.</u></p>
 * 
 * @author Tsolak Petrosian
 * 
 */
public class ConvertMethodReturnValueInterceptor implements MethodInterceptor{
	
	private Converter converter = null;
	
	private String fromPropertyPath = null;
	private String toPropertyPath = null;
			
	public Converter getConverter() {
    	return converter;
    }

	public void setConverter( Converter converter ) {
    	this.converter = converter;
    }

	
	public String getFromPropertyPath() {
    	return fromPropertyPath;
    }

	public void setFromPropertyPath( String fromPropertyPath ) {
    	this.fromPropertyPath = fromPropertyPath;
    }

	public String getToPropertyPath() {
    	return toPropertyPath;
    }

	public void setToPropertyPath( String toPropertyPath ) {
    	this.toPropertyPath = toPropertyPath;
    }
	
	@Override
	public final Object invoke( MethodInvocation invocation ) throws Throwable {
		Object returnValue = invocation.proceed();
		returnValue = convert(returnValue, fromPropertyPath, toPropertyPath);
		return returnValue;
	}
	
	private Object convert(Object returnValue, String fromPropertyPath, String toPropertyPath){
		Object convertedValue = getConvertedValue(returnValue, fromPropertyPath);
		
		if ( StringUtils.isBlank( toPropertyPath ) ) return convertedValue;
		
		try{ PropertyUtils.setProperty( returnValue, toPropertyPath, convertedValue ); }
		catch(Exception e){throw new RuntimeException(e);}	
		
		return returnValue;
    }   
	
	private Object getConvertedValue(Object returnValue, String fromPropertyPath){
		if ( StringUtils.isBlank( fromPropertyPath ) ) return converter.convert( returnValue );
		
		Object fromPropertyValue = null;
		try{ fromPropertyValue = PropertyUtils.getProperty( returnValue, fromPropertyPath ); }
		catch(Exception e){throw new RuntimeException(e);}		
		
		return converter.convert(fromPropertyValue);
   } 
}
