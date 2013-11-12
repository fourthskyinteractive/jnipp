package demo.chapters.ichi.rawExample;

public class Simple
{
	static
	{
		System.loadLibrary( "Simple" );
	}

	private long handle = 0;
	
	protected native void init();
	protected native void uninit();
	public native void loadByID(String id);
	public native String getLegacyData();
	
	public Simple()
	{
		init();
	}

	protected void finalize()
	{
		uninit();
	}
		
	public static void main(String[] args)
	{
		try
		{
			Simple s1 = new Simple();
			Simple s2 = new Simple();
			s1.loadByID( "s1ID" );
			s2.loadByID( "s2ID" );
			System.out.println( "s1.getLegacyData() == \"" + s1.getLegacyData() + "\"" );
			System.out.println( "s2.getLegacyData() == \"" + s2.getLegacyData() + "\"" );
			s1.loadByID( null );
		}
		catch(Exception ex)
		{
			System.out.println( "Caught exception:" );
			ex.printStackTrace();
		}
	}
}
