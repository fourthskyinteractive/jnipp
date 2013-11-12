package demo.chapters.san.rich;

import java.util.Collection;
import java.util.Iterator;

public class RichDemo
{
	public void printCollection(Collection coll)
	{
		Iterator it = coll.iterator();
		while ( it.hasNext() == true )
			System.out.println( (String) it.next() );
	}
}
