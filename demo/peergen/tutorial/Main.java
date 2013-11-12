package demo.peergen.tutorial;

public class Main
{
	public static void main(String[] args)
	{
		NetAdapterServices nas = new NetAdapterServicesProxy();
		String[] adapterNames = nas.getInterfaceList();
		if ( adapterNames == null )
		{
			System.out.println( "NULL return value" );
			return;
		}
		for ( int i = 0; i < adapterNames.length; ++i )
			System.out.println( "Adapter: " + adapterNames[i] + " has MAC address = " + nas.getMACAddress( adapterNames[i] ) );
	}
}
