package es.andujar.campamento.exception;

import java.util.Map;
import java.util.TreeMap;

import lombok.Data;
@Data
public class UserException extends Exception
{
	/**Codigo de error de la excepcion */
	private int code;
	
	/**Mensaje de error */
	private String message;
	
	/**Causa externa */
	private Exception exception;

	
	public UserException(int code, String message, Exception exception) 
	{
		super(message,exception);
		this.code = code;
		this.message = message;
		this.exception = exception;
	}


	public UserException(String message, int code) 
	{
		super(message);
		this.code = code;
		this.message = message;
	}
	
	
	public Map<String,Object>toMap()
	{
		Map<String,Object> exceptionMap = new TreeMap<String, Object>();
		
		exceptionMap.put("codigo", this.code);
		exceptionMap.put("mensaje", this.message);
		if(this.exception!=null)
		{
			exceptionMap.put("causa", this.exception);
		}
		
		return exceptionMap;
	} 
	
}
