package org.equilibriums.aop.utils.interceptor.convert.converters;

import java.text.Format;
import java.text.ParseException;

import org.equilibriums.aop.utils.interceptor.convert.Converter;

public class TextParseConverter implements Converter {

	private Format format = null;
		
	public Format getFormat() {
    	return format;
    }

	public void setFormat( Format format ) {
    	this.format = format;
    }

	public Object convert(Object source){
		if (source == null) return null;
		try{ return format.parseObject( source.toString() ); }
		catch (ParseException e) { throw new RuntimeException(e); }
	}
}
