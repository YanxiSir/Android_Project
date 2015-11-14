package com.neu.tools;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

public class StreamTools {

	
	
	public static String readFromStream(InputStream is)throws Exception{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while((len = is.read(buffer))!=-1){
			baos.write(buffer,0,len);
		}
		is.close();
		String result = new String(baos.toByteArray(),"gbk");
		baos.close();
		return result;
	}
	 public static String getStreamAsString(InputStream stream, String charset) throws IOException {
	        try {
	            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset));
	            StringWriter writer = new StringWriter();

	            char[] chars = new char[256];
	            int count = 0;
	            while ((count = reader.read(chars)) > 0) {
	                writer.write(chars, 0, count);
	            }

	            return writer.toString();
	        } finally {
	            if (stream != null) {
	                stream.close();
	            }
	        }
	    }
}
