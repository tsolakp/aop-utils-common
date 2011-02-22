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

import org.aopalliance.intercept.MethodInvocation;
import org.aopalliance.intercept.MethodInterceptor;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.beanutils.PropertyUtils;

/**
 * <p>
 * Will take value from the property specified by {@link #getFromArgumentIndex} and {@link #getFromPropertyPath}, 
 * convert it using specified {@link Converter} and replace property specified by 
 * {@link #getToArgumentIndex} and {@link #getToPropertyPath}.
 * </p>
 * 
 * <p>The paths are defines according to {@link org.apache.commons.lang.StringUtils#getProperty} or {@link org.apache.commons.lang.StringUtils#setProperty} documentation.
 * 
 * <p>For example given method "public String method1(String arg1, List arg2, Map arg3);</p>
 * 
 * <ul>
 * <li>
 * To convert arg1 use "0" as {@link #getFromArgumentIndex}, null as {@link #getFromPropertyPath},
 * "0" as {@link #getToArgumentIndex} and null as {@link #getToPropertyPath}.
 * </li>
 * 
 * <li>
 * To convert first value in arg2 use "1" as {@link #getFromArgumentIndex}, [0] as {@link #getFromPropertyPath},
 * "1" as {@link #getToArgumentIndex} and [0] as {@link #getToPropertyPath}.
 * </li>
 * 
 *  <li>
 * To convert first value in arg2 and put it in arg3 with key "key1" use "1" as {@link #getFromArgumentIndex}, [0] as {@link #getFromPropertyPath},
 * "2" as {@link #getToArgumentIndex} and [key1] as {@link #getToPropertyPath}.
 * </li>
 * 
 * </ul>
 * </p>
 * 
 * <p><u>For more on how to use this interceptor see Overview section of this Javadoc.</u></p>
 * 
 * @author Tsolak Petrosian
 * 
 */
public class ConvertMethodArgumentInterceptor implements MethodInterceptor {

	private Converter converter = null;
	
	private int fromArgumentIndex = 0;
	private String fromPropertyPath = null;

	private int toArgumentIndex = 0;
	private String toPropertyPath = null;
		
	public Converter getConverter() {
    	return converter;
    }

	public void setConverter( Converter converter ) {
    	this.converter = converter;
    }
	
	
    public int getFromArgumentIndex() {
    	return fromArgumentIndex;
    }

	public void setFromArgumentIndex( int fromArgumentIndex ) {
    	this.fromArgumentIndex = fromArgumentIndex;
    }

	public String getFromPropertyPath() {
    	return fromPropertyPath;
    }

	public void setFromPropertyPath( String fromPropertyPath ) {
    	this.fromPropertyPath = fromPropertyPath;
    }
	

	public int getToArgumentIndex() {
    	return toArgumentIndex;
    }

	public void setToArgumentIndex( int toArgumentIndex ) {
    	this.toArgumentIndex = toArgumentIndex;
    }

	public String getToPropertyPath() {
    	return toPropertyPath;
    }

	public void setToPropertyPath( String toPropertyPath ) {
    	this.toPropertyPath = toPropertyPath;
    }
	
	@Override
	public final Object invoke( MethodInvocation invocation ) throws Throwable {
		Object[] args = invocation.getArguments();
		
		if ( fromArgumentIndex < 0 || fromArgumentIndex >= args.length ) throw new IllegalArgumentException( String.format( 
		"fromArgumentIndex %s is out of bounds with arguments count of %s", fromArgumentIndex, args.length ) ); 
		
		if ( toArgumentIndex < 0 || toArgumentIndex >= args.length ) throw new IllegalArgumentException( String.format( 
		"toArgumentIndex %s is out of bounds with arguments count of %s", toArgumentIndex, args.length ) ); 
		
		convert(args, fromArgumentIndex, fromPropertyPath, toArgumentIndex, toPropertyPath);
				
		return invocation.proceed();
	}	
		
	private void convert(Object[] args, Integer fromArgumentIndex, String fromPropertyPath, Integer toArgumentIndex, String toPropertyPath){
		Object convertedValue = getConvertedValue(args, fromArgumentIndex, fromPropertyPath);
		
		if ( StringUtils.isBlank( toPropertyPath ) ){
			args[toArgumentIndex] = convertedValue;
		}else{		
		    try{ PropertyUtils.setProperty( args[toArgumentIndex], toPropertyPath, convertedValue ); }
		    catch(Exception e){throw new RuntimeException(e);}	
		}
    }   
	
	private Object getConvertedValue(Object[] args, Integer fromArgumentIndex, String fromPropertyPath){
		if ( StringUtils.isBlank( fromPropertyPath ) ) return converter.convert( args[fromArgumentIndex] );
		
		Object fromPropertyValue = null;
		try{ fromPropertyValue = PropertyUtils.getProperty( args[fromArgumentIndex], fromPropertyPath ); }
		catch(Exception e){throw new RuntimeException(e);}		
		
		return converter.convert(fromPropertyValue);
   } 
}
