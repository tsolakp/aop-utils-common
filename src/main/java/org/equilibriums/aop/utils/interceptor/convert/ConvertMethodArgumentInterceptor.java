package org.equilibriums.aop.utils.interceptor.convert;

import org.aopalliance.intercept.MethodInvocation;
import org.aopalliance.intercept.MethodInterceptor;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.beanutils.PropertyUtils;

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
