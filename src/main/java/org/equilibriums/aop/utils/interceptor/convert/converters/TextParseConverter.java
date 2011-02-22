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
