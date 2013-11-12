package java.lang;

import java.lang.Runnable;

public class RunnableProxy
	implements Runnable
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
	public native void run();


	static
	{
		System.loadLibrary( "PeerTutorial" );
		init();
	}
}
