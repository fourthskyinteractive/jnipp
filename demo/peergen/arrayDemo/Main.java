package demo.peergen.arrayDemo;

public class Main
{
	public static void main(String[] args)
	{
		ArrayDemo ad = new ArrayDemoProxy();
		int[][][] arr = ad.getArray();
		System.out.println( "Contents of C++ array:" );
		for ( int i = 0; i < 3; ++i )
		{
			for ( int j = 0; j < 3; ++j )
			{
				for ( int k = 0; k < 3; ++k )
					System.out.print( arr[i][j][k] + " " );
				System.out.println();
			}
			System.out.println();
		}

		ad.showArray( arr );
	}
}
