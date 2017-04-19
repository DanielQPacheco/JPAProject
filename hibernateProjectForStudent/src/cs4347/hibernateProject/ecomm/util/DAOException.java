package cs4347.hibernateProject.ecomm.util;

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
