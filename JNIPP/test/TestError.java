import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;


public class TestError {

	public static void main(String[] args) throws ClassNotFoundException {
		/*
		System.out.println(Modifier.PUBLIC);
		System.out.println(Modifier.PRIVATE);
		System.out.println(Modifier.PROTECTED);
		
		System.out.println((3 & Modifier.PUBLIC) == Modifier.PUBLIC);
		*/
		
		Set s = new HashSet();
		s.add("java.lang.Runnable");
		
		System.out.println(s.contains("java.lang.Runnable"));
		
		Method[] methods = Runnable.class.getMethods();
		System.out.println("Method " + methods[0].getName() + " é abstrato: " + Modifier.isAbstract(methods[0].getModifiers()));
	}
}
