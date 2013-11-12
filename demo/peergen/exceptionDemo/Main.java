package demo.peergen.exceptionDemo;

public class Main
{
	public static void main(String[] args)
	{
		ExceptionDemo ed = new ExceptionDemoProxy();
		
		try
		{
			ed.throwAnException();
		}
		catch(Exception ex)
		{
			System.out.println( ex );
			ex.printStackTrace();
		}
	}
}
