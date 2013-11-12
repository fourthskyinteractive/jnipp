import java.lang.reflect.Modifier;


public class TestError {

	public static void main(String[] args) throws ClassNotFoundException {
		System.out.println(Modifier.PUBLIC);
		System.out.println(Modifier.PRIVATE);
		System.out.println(Modifier.PROTECTED);
		
		System.out.println((3 & Modifier.PUBLIC) == Modifier.PUBLIC);
	}
}
