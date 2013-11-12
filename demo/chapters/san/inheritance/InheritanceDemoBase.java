package demo.chapters.san.inheritance;

public class InheritanceDemoBase
{
	public void printBaseMessage()
	{
		System.out.println( "Hello from InheritanceDemoBase" );
	}
	
	public void printMessage()
	{
		printBaseMessage();
	}
}
