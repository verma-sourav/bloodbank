package org.verma.javatar.blood.resources;

import java.io.IOException;

import javax.json.stream.JsonGenerationException;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utility {

	public static String convertObjectTOJSONString(Object object){
		   if(object == null){
		      return null;
		   }
		   ObjectMapper mapper = new ObjectMapper();
		   try {
		       return mapper.writeValueAsString(object);
		   } catch (JsonGenerationException jgexp) {
		     System.out.println(" JsonGenerationException ::: "+jgexp);
		   } catch (JsonMappingException jmexp) {
		     System.out.println(" JsonMappingException ::: "+jmexp);
		   } catch (IOException ioexp) {
		      System.out.println(" IOException ::: "+ioexp);
		   }
		   return null;
		}
	
}
