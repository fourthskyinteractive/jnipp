package demo.chapters.ichi.jnippExample;

import demo.chapters.ichi.jnippExample.Simple;

public class SimpleProxy
	implements Simple
{
	private long peerPtr = 0;

	private static native void init();
	private native void releasePeer();

	protected void finalize()
		throws Throwable
	{
		releasePeer();
	}

	// methods
	public native String getData();
	public native void loadByID(String p0);


	static
	{
		System.loadLibrary( "Simple" );
		init();
	}

	public static void main(String[] args)
	{
		try
		{
			SimpleProxy s1 = new SimpleProxy();
			SimpleProxy s2 = new SimpleProxy();
			s1.loadByID( "s1ID" );
			s2.loadByID( "s2ID" );
			System.out.println( "s1.getData() == \"" + s1.getData() + "\"" );
			System.out.println( "s2.getData() == \"" + s2.getData() + "\"" );
			s1.loadByID( null );
		}
		catch(Exception ex)
		{
			System.out.println( "Caught exception:" );
			ex.printStackTrace();
		}
	}
}
