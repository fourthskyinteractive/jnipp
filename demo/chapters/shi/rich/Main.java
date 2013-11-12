package demo.chapters.shi.rich;

import java.util.Iterator;

public class Main
{
	public static void main(String[] args)
	{
		RichDemo rd = new RichDemoProxy();
		Iterator it = rd.getCollection();
		
		System.out.println( "Collection Contents:" );
		while ( it.hasNext() == true )
			System.out.println( (String) it.next() );
	}
}
