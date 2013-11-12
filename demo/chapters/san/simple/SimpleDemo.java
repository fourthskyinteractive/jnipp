package demo.chapters.san.simple;

public class SimpleDemo
{
	private String value;
	
	public SimpleDemo(String value) {
		this.value = value;
	}

	public void printValue(String value) 
	{
		System.out.println( "value is " + value );
	}

	public void printValue(Integer value)
	{
		System.out.println( "value is " + value );
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
}
