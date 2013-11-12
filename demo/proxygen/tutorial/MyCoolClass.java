package demo.proxygen.tutorial;

public class MyCoolClass
{
	private static String someStaticData = "uninitialized";
	private String someData = "uninitialized";

	public MyCoolClass()
	{
	}

	public MyCoolClass(String someData)
	{
		this.someData = someData;
	}

	public String getSomeData()
	{
		return someData;
	}

	public static String getSomeStaticData()
	{
		return someStaticData;
	}

	public static void printContents(MyCoolClass mcc)
	{
		System.out.println( "someStaticData = " + someStaticData + ", mcc.someData = " + mcc.someData );
	}
}
