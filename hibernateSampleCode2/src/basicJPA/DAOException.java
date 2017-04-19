package basicJPA;

@SuppressWarnings("serial")
public class DAOException extends Exception
{

	public DAOException(String msg)
	{
		super(msg);
	}

	public DAOException(String msg, Throwable ex)
	{
		super(msg, ex);
	}

}
