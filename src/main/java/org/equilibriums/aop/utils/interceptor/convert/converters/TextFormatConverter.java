package org.equilibriums.aop.utils.interceptor.convert.converters;

import java.text.Format;

import org.equilibriums.aop.utils.interceptor.convert.Converter;

public class TextFormatConverter implements Converter {

	private Format format = null;
		
	public Format getFormat() {
    	return format;
    }

	public void setFormat( Format format ) {
    	this.format = format;
    }

	public Object convert(Object source){
		return format.format( source );
	}
}
