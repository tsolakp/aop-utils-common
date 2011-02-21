package org.equilibriums.aop.utils.interceptor.convert;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

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
