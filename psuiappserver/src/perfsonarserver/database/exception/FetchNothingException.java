package perfsonarserver.database.exception;

public class FetchNothingException extends RuntimeException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7681737597637084014L;
	
	public FetchNothingException(String message)
	{
		super(message);
	}
	
	public FetchNothingException()
	{
		super();
	}
}
