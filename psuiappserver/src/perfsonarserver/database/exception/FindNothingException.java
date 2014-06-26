package perfsonarserver.database.exception;

public class FindNothingException extends RuntimeException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2309212865987134983L;

	public FindNothingException(String message)
	{
		super(message);
	}
	
	public FindNothingException()
	{
		super();
	}
}
