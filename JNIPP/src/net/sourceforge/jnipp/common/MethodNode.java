package net.sourceforge.jnipp.common;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import net.sourceforge.jnipp.project.ProxyGenSettings;

/**
 * Node representing a method of a Java interface or class.
 *
 * @author Philllip E. Trewhella
 * @version 1.0
 */

public class MethodNode
{
	private String methodName = "";
	private String uniqueCppName = null;
	private String jniName = "";
	private List parameterList = new ArrayList();
	private ClassNode returnType = null;
	private boolean ctor = false;
	private String jniMethodCall = "";
	private String jniSignature = null;
	private boolean staticMethod = false;
	private boolean abstractMethod = false;
	private ClassNode parent = null;

	public MethodNode(ClassNode parent, Constructor ctor) throws ClassNotFoundException
	{
		this.parent = parent;
		this.ctor = true;
		int lastDot = ctor.getName().lastIndexOf( '.' ) + 1;
		methodName = ctor.getName().substring( lastDot );
		jniName = "<init>";

		Class[] parameterTypes = ctor.getParameterTypes();
		for ( int i = 0; i < parameterTypes.length; ++i )
			parameterList.add( ClassNode.getClassNode( parameterTypes[i].getName() ) );
	}

	public MethodNode(ClassNode parent, Method method) throws ClassNotFoundException
	{
		this.parent = parent;
		methodName = method.getName();
		jniName = methodName;
		staticMethod = Modifier.isStatic( method.getModifiers() );
		abstractMethod = Modifier.isAbstract( method.getModifiers() );
		returnType = ClassNode.getClassNode( method.getReturnType().getName() );
		Class[] parameterTypes = method.getParameterTypes();
		for ( int i = 0; i < parameterTypes.length; ++i )
			parameterList.add( ClassNode.getClassNode( parameterTypes[i].getName() ) );
	}

	public String getName()
	{
		return methodName;
	}
	
	public String getCPPName()
	{
		return Util.getCPPIdentifier( methodName );
	}
	
	public String getCSName()
	{
		return Util.getCSIdentifier( methodName );
	}

	/**
	 * Returns a unique C++ name for this method. If the name of the
	 * method is not initially unique, an integer suffix is incremented
	 * until a unique name is found.
	 *
	 * @return a unique C++ method name.
	 */
	public String getUniqueCPPName()
	{
		if ( uniqueCppName == null )
		{
			String baseName = Util.getCPPIdentifier( methodName );
			String cppName = baseName;
			int methodCounter = 1;
			while ( parent.getCPPMethodNames().contains( cppName ) )
			{
				cppName = baseName + Integer.toString( methodCounter );
				++methodCounter;
			}
			parent.getCPPMethodNames().add( cppName );
			uniqueCppName = cppName;
		}
		return uniqueCppName;
 	}

	public String getJNIName()
	{
		return jniName;
	}

	public String getJNISignature()
	{
		if (jniSignature == null) {
			jniSignature = "(";
			Iterator it = parameterList.iterator();
			while ( it.hasNext() == true )
				jniSignature += ((ClassNode) it.next()).getJNIString();
			jniSignature += ")" + (returnType == null ? "V" : returnType.getJNIString() );
		}
		
		return jniSignature;
	}
	
	public int getParameterCount() {
		return parameterList.size();
	}

	public Iterator getParameterList()
	{
		return parameterList.iterator();
	}

	public ClassNode getReturnType()
	{
		return returnType;
	}	
	
	public ClassNode getParent() 
	{
		return parent;
	}
	
	public String getJNIMethodCall()
	{
		return getJNIMethodCall(ProxyGenSettings.LANGUAGE_CPP);
	}
	
	public String getJNIMethodCall(String language) {
		if (ProxyGenSettings.LANGUAGE_CPP.equals(language))
			return getJNIMethodCallForCPP();
		else if (ProxyGenSettings.LANGUAGE_CS.equals(language))
			return getJNIMethodCallForCS();
		else
			throw new RuntimeException("language not supported");
	}
	
	private String getJNIMethodCallForCPP()
	{
		String jniMethodCall = "";
		if ( returnType == null || returnType.getClassName().equals( "void" ) == true )
		{
			jniMethodCall = "JNIEnvHelper::Call";
			jniMethodCall += (staticMethod == true ? "StaticVoidMethod(" : "VoidMethod(");
		}
		else
		if ( returnType.isPrimitive() == false )
		{
			jniMethodCall = "JNIEnvHelper::Call";
			jniMethodCall += (staticMethod == true ? "StaticObjectMethod(" : "ObjectMethod(");
		}
		else
		{
			jniMethodCall = "JNIEnvHelper::Call";
			if ( staticMethod == true )
				jniMethodCall += "Static";
			jniMethodCall += Character.toUpperCase( returnType.getClassName().charAt( 0 ) ) + returnType.getClassName().substring( 1 );
			jniMethodCall += "Method(";
		}
		return jniMethodCall;
	}

	private String getJNIMethodCallForCS()
	{
		String jniMethodCall = "";
		if ( returnType == null || returnType.getClassName().equals( "void" ) == true )
		{
			jniMethodCall = "AndroidJNI.Call";
			jniMethodCall += (staticMethod == true ? "StaticVoidMethod(" : "VoidMethod(");
		}
		else if ( returnType.getFullyQualifiedClassName().equals( "java.lang.String" ) ) 
		{
			jniMethodCall = "AndroidJNI.Call";
			jniMethodCall += (staticMethod == true ? "StaticStringMethod(" : "StringMethod(");
		}
		else if ( returnType.isPrimitive() == false )
		{
			jniMethodCall = "AndroidJNI.Call";
			jniMethodCall += (staticMethod == true ? "StaticObjectMethod(" : "ObjectMethod(");
		}
		else
		{
			jniMethodCall = "AndroidJNI.Call";
			if ( staticMethod == true )
				jniMethodCall += "Static";
			jniMethodCall += Character.toUpperCase( returnType.getClassName().charAt( 0 ) ) + returnType.getClassName().substring( 1 );
			jniMethodCall += "Method(";
		}
		return jniMethodCall;
	}
	
	public boolean isStatic()
	{
		return staticMethod;
	}
	
	public boolean isAbstract() 
	{
		return abstractMethod;	
	}
	
	public String getJavaSignature()
	{
		String signature = "";
		if ( returnType != null )
			signature += returnType.getClassName() + " ";
		else
		if ( ctor == false )
			signature += "void ";
		signature += methodName + "(";
		Iterator it = parameterList.iterator();
		while ( it.hasNext() == true )
			signature += ((ClassNode) it.next()).getClassName() + (it.hasNext() == true ? ", " : "");
		signature += ");";
		return signature;
	}
	
}
