package net.sourceforge.jnipp.proxyGen.unity;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.jnipp.common.ClassNode;
import net.sourceforge.jnipp.common.FieldNode;
import net.sourceforge.jnipp.common.FormattedFileWriter;
import net.sourceforge.jnipp.common.MethodNode;
import net.sourceforge.jnipp.common.Util;
import net.sourceforge.jnipp.project.ProxyGenSettings;
import net.sourceforge.jnipp.proxyGen.ProxyImplGenerator;

public class CSProxyImplGenerator implements ProxyImplGenerator {

	/**
	 * Settings specifying the code generation options.
	 *
	 * This reference points to the <code>ProxyGenSettings</code> instance passed
	 * into the <code>generate()</code> method and is used by various methods to
	 * guide the code generation.
	 *
	 * @see #generate
	 */
	private ProxyGenSettings proxyGenSettings = null;

	/**
	 * Defualt constructor.
	 */
	
	public CSProxyImplGenerator()
	{
	}

	public void generate(ClassNode root, ProxyGenSettings proxyGenSettings) throws IOException {
		if ( root.isPrimitive() == true || root.needsProxy() == false )
			return;
		
		this.proxyGenSettings = proxyGenSettings;

		String fullFileName = proxyGenSettings.getProject().getCPPOutputDir() + /*File.separatorChar*/ '/';
		if ( root.getPackageName().equals( "" ) == true ) {
			fullFileName = getClassName(root) + ".cs";

		} else {
			fullFileName = root.getPackageName().replace( '.', '_' ) + '_' + getClassName(root) + ".cs";

		}

		FormattedFileWriter writer = new FormattedFileWriter( fullFileName, true );

		generateIncludes( root, writer );

		// Open namespace declaration
		Iterator it = root.getNamespaceElements();
		while ( it.hasNext() == true )
		{
			String current = (String) it.next();
			writer.outputLine( "namespace " + current );
			writer.outputLine( "{" );
			writer.incTabLevel();
		}

		// Generate class as proxy
		if (proxyGenSettings.isGenerateAsPeer(root.getFullyQualifiedClassName()))
			generatePeer(root, proxyGenSettings, writer);
		else
			generateProxy(root, proxyGenSettings, writer);

		// Close namespace declaration
		for ( it = root.getNamespaceElements(); it.hasNext() == true; it.next() )
		{
			writer.decTabLevel();
			writer.outputLine( "}" );
		}

		writer.flush();
		writer.close();
	}
	
	public void generatePeer(ClassNode root, ProxyGenSettings proxyGenSettings, FormattedFileWriter writer) throws IOException {
		// Class definition
		writer.output( "public abstract class " + getClassName(root) );
		if (root.isInterface())
			writer.output(" : AndroidJavaProxy");
		else
			writer.output(" : AndroidJavaAbstractProxy");
		
		writer.outputLine( "" );
		writer.outputLine( "{" );
		writer.incTabLevel();

		// Generate private fields
		writer.outputLine("private static readonly string className = \"" +
							root.getFullyQualifiedClassName().replace('.', '/') + 
							"\";");
		writer.outputLine("public static string ClassName {");
		writer.incTabLevel();
		writer.outputLine("get {");
		writer.incTabLevel();
		writer.outputLine("return className;");
		writer.decTabLevel();
		writer.outputLine("}");
		writer.decTabLevel();
		writer.outputLine("}");
		writer.newLine( 1 );
   		
		writer.outputLine("private static IntPtr objectClass = IntPtr.Zero;");
		writer.outputLine("public static IntPtr ObjectClass {");
		writer.incTabLevel();
		writer.outputLine("get {");
		writer.incTabLevel();
		writer.outputLine( "if ( objectClass == IntPtr.Zero )" );
		writer.incTabLevel();
		writer.outputLine( "objectClass = AndroidJNI.NewGlobalRef( AndroidJNI.FindClass( className ) );" );
		writer.decTabLevel();
		writer.outputLine("return objectClass;");
		writer.decTabLevel();
		writer.outputLine("}");
		writer.decTabLevel();
		writer.outputLine("}");
		writer.newLine( 1 );

		writer.outputLine("protected IntPtr peerObject = IntPtr.Zero;");
		writer.outputLine("public IntPtr PeerObject {");
		writer.incTabLevel();
		writer.outputLine("get {");
		writer.incTabLevel();
		writer.outputLine("return peerObject;");
		writer.decTabLevel();
		writer.outputLine("}");
		writer.decTabLevel();
		writer.outputLine("}");
		writer.newLine( 1 );
		
		// Cast operator to IntPtr
		writer.outputLine("public static explicit operator IntPtr(" + root.getFullyQualifiedClassName().replace('$', '_') + " obj) {");
		writer.incTabLevel();
		writer.outputLine("return obj.PeerObject;");
		writer.decTabLevel();
		writer.outputLine("}");
		writer.newLine( 1 );

		// Generate constructor
		writer.outputLine( "// constructors" );
		
		writer.outputLine( "public " + getClassName(root) + "()" );
		writer.incTabLevel();
		writer.output( ": base( " + getClassName(root) + ".className )" );
		writer.decTabLevel();
		writer.newLine( 1 );

		writer.outputLine( "{" );
		writer.incTabLevel();
		writer.outputLine( "peerObject = AndroidJNIHelper.CreateJavaProxy ( this );" );
		writer.decTabLevel();
		writer.outputLine( "}" );
		writer.newLine( 1 );
		
		// Generate destructor
		writer.outputLine( "~" + getClassName(root) + "()" );
		writer.outputLine("{");
		writer.incTabLevel();
		writer.outputLine("if ( peerObject != IntPtr.Zero ) {");
		writer.incTabLevel();
		writer.outputLine("AndroidJNI.DeleteGlobalRef( peerObject );");
		writer.decTabLevel();
		writer.outputLine("}");
		writer.outputLine("peerObject = IntPtr.Zero;");
		writer.decTabLevel();
		writer.outputLine("}");
		writer.newLine( 1 );

		// Generate abstract methods
		generateMethods(root, writer);		

		writer.decTabLevel();
		writer.outputLine( "}" );
	}
	
	public void generateProxy(ClassNode root, ProxyGenSettings proxyGenSettings, FormattedFileWriter writer) throws IOException {
		boolean isNotObjectClass = root.getFullyQualifiedClassName().equals("java.lang.Object") == false;
		
		writer.output( "public class " + getClassName(root) );
		if ( proxyGenSettings.getUseInheritance() == true )
		{
			if ( root.isInterface() == false )
			{
				if ( root.getSuperClass() != null )
					writer.output( " : " + getFullyQualifiedClassName(root.getSuperClass()) );
				
			}
			else
			{
				Iterator intfcs = root.getInterfaces();
				if ( intfcs.hasNext() == true )
				{
					writer.output( " : " + getFullyQualifiedClassName((ClassNode) intfcs.next()));
					while ( intfcs.hasNext() == true )
						writer.output( ", " + getFullyQualifiedClassName((ClassNode) intfcs.next()) );
				}
				else
					writer.output(" : java.lang.Object");
			}
		}

		writer.outputLine( "" );
		writer.outputLine( "{" );
		writer.incTabLevel();
		
		// Generate constants
		generateConstants(root, writer);

		// Generate private fields
		writer.outputLine("private static readonly string className = \"" +
							root.getFullyQualifiedClassName().replace('.', '/') + 
							"\";");
		if (isNotObjectClass)
			writer.outputLine("public static new string ClassName {");
		else
			writer.outputLine("public static string ClassName {");
		writer.incTabLevel();
		writer.outputLine("get {");
		writer.incTabLevel();
		writer.outputLine("return className;");
		writer.decTabLevel();
		writer.outputLine("}");
		writer.decTabLevel();
		writer.outputLine("}");
		writer.newLine( 1 );
   		
		
		writer.outputLine("private static IntPtr objectClass = IntPtr.Zero;");
		if (isNotObjectClass)
			writer.outputLine("public static new IntPtr ObjectClass {");
		else
			writer.outputLine("public static IntPtr ObjectClass {");
		writer.incTabLevel();
		writer.outputLine("get {");
		writer.incTabLevel();
		writer.outputLine( "if ( objectClass == IntPtr.Zero )" );
		writer.incTabLevel();
		writer.outputLine( "objectClass = AndroidJNI.NewGlobalRef( AndroidJNI.FindClass( className ) );" );
		writer.decTabLevel();
		writer.outputLine("return objectClass;");
		writer.decTabLevel();
		writer.outputLine("}");
		writer.decTabLevel();
		writer.outputLine("}");
		writer.newLine( 1 );

		// Only include these fields for 
		if ( !isNotObjectClass || 
				( root.isInterface() && root.getSuperClass() != null ) ) {
			writer.outputLine("protected IntPtr peerObject = IntPtr.Zero;");
			writer.outputLine("public IntPtr PeerObject {");
			writer.incTabLevel();
			writer.outputLine("get {");
			writer.incTabLevel();
			writer.outputLine("return peerObject;");
			writer.decTabLevel();
			writer.outputLine("}");
			writer.decTabLevel();
			writer.outputLine("}");
			writer.newLine( 1 );
		}
		
		// Cast operator to IntPtr
		writer.outputLine("public static explicit operator IntPtr(" + root.getFullyQualifiedClassName().replace('$', '_') + " obj) {");
		writer.incTabLevel();
		writer.outputLine("return obj.PeerObject;");
		writer.decTabLevel();
		writer.outputLine("}");
		writer.newLine( 1 );
		
		// Cast operator from IntPtr
		writer.outputLine("public static explicit operator " + root.getFullyQualifiedClassName().replace('$', '_') + "(IntPtr ptr) {");
		writer.incTabLevel();
		writer.outputLine("return new " + root.getFullyQualifiedClassName().replace('$', '_') + "(ptr);");
		writer.decTabLevel();
		writer.outputLine("}");
		writer.newLine( 1 );

		// Generate constructors
		generateCtors( root, writer );
		
		// Generate destructor
		writer.outputLine( "~" + getClassName(root) + "()" );
		writer.outputLine("{");
		writer.incTabLevel();
		writer.outputLine("if ( peerObject != IntPtr.Zero ) {");
		writer.incTabLevel();
		writer.outputLine("AndroidJNI.DeleteGlobalRef( peerObject );");
		writer.decTabLevel();
		writer.outputLine("}");
		writer.outputLine("peerObject = IntPtr.Zero;");
		writer.decTabLevel();
		writer.outputLine("}");
		writer.newLine( 1 );

		// Generate methods
		generateMethods(root, writer);

		writer.decTabLevel();
		writer.outputLine( "}" );
	}

	public void generateIncludes(ClassNode root, FormattedFileWriter writer) throws IOException {
		Set<String> alreadyGenerated = new TreeSet<String>();

		writer.outputLine( "using System;" );
		writer.outputLine( "using UnityEngine;" );
		writer.output("using net.sourceforge.jnipp;");
		writer.newLine(1);

		if ( proxyGenSettings.getUseInheritance() == true )
		{
			if ( root.isInterface() == false )
			{
				if ( root.getSuperClass() != null )
				{
					String namespace = root.getSuperClass().getPackageName();
					if (!root.getPackageName().equals(namespace))
						alreadyGenerated.add(namespace);
				}
			}
			else
			{
				Iterator it = root.getInterfaces();
				while ( it.hasNext() == true )
				{
					ClassNode cn = (ClassNode) it.next();
					ClassNode superClass = root.getSuperClass(); 
					if (superClass != null) {
						String namespace = superClass.getPackageName();
						if (!root.getPackageName().equals(namespace))
							alreadyGenerated.add(namespace);

					}
				}
			}
		}

		Iterator<String> it = alreadyGenerated.iterator();
		while(it.hasNext()) {
			writer.outputLine("using " + it.next() + ";");
		}
		writer.newLine( 1 );
	}
	
	public void generateConstants(ClassNode root, FormattedFileWriter writer) throws IOException {
		
		Iterator it = root.getFields();
		while(it.hasNext()) {
			FieldNode node = (FieldNode) it.next();
			System.out.println("Found const " + node.getName() + " with value " + node.getValue());
			if (node.isConstant()) {
				writer.outputLine("public static readonly " + getJNITypeName(node.getType(), false) + " " + node.getName() + " = " + node.getValue());
			}
		}		
		
		writer.newLine(1);
	}

	public void generateUsing(ClassNode root, FormattedFileWriter writer) throws IOException {
		/*
		HashMap alreadyGenerated = new HashMap();
		if ( root.getNamespace().equals( "" ) == false )
		{
			writer.outputLine( "using namespace " + root.getNamespace().replaceAll("::", ".") + ";" );
			alreadyGenerated.put( root.getNamespace(), root );
		}

		writer.newLine( 2 );
		 */
	}

	public void generateCtors(ClassNode root, FormattedFileWriter writer) throws IOException {
		writer.outputLine( "// constructors" );
		
		writer.outputLine( "public " + getClassName(root) + "(IntPtr obj)" );
		
		// Call base constructor
		if ( proxyGenSettings.getUseInheritance() == true )
		{
			if ( root.isInterface() == false )
			{
				if ( root.getSuperClass() != null )
				{
					writer.incTabLevel();
					writer.output( ": base( IntPtr.Zero )" );
					writer.decTabLevel();
				}
			}
			else
			{
				Iterator intfcs = root.getInterfaces();
				if ( intfcs.hasNext() == true )
				{
					writer.incTabLevel();
					writer.output( ": base( IntPtr.Zero )" );
					writer.decTabLevel();
				}
			}
			writer.newLine( 1 );
		}
		writer.outputLine( "{" );
		writer.incTabLevel();
		writer.outputLine("if ( obj != IntPtr.Zero ) {");
		writer.incTabLevel();
		writer.outputLine( "peerObject = AndroidJNI.NewGlobalRef( obj );" );
		writer.decTabLevel();
		writer.outputLine("}");
		writer.decTabLevel();
		writer.outputLine( "}" );
		writer.newLine( 1 );

		// Iterate thought declared java constructors		
		Iterator it = root.getConstructors();
		while ( it.hasNext() )
		{
			MethodNode node = (MethodNode) it.next();
			
			int currentIndex = 0;
			writer.output( "public " + getClassName(root) + "(" );
			Iterator params = node.getParameterList();
			for ( int count = 0; params.hasNext() == true; ++count )
			{
				ClassNode currentParam = (ClassNode) params.next();
				writer.output( getJNITypeName(currentParam, proxyGenSettings.getProject().getUsePartialSpec() ) + " p" + currentIndex++ );
					
				if ( params.hasNext() == true )
					writer.output( ", " );
			}
			writer.outputLine( ")" );
		
			// Call base constructor
			if ( proxyGenSettings.getUseInheritance() == true )
			{
				if ( root.isInterface() == false )
				{
					if ( root.getSuperClass() != null )
					{
						writer.incTabLevel();
						writer.output( ": base( IntPtr.Zero )" );
						writer.decTabLevel();
					}
				}
				else
				{
					Iterator intfcs = root.getInterfaces();
					if ( intfcs.hasNext() == true )
					{
						writer.incTabLevel();
						writer.output( ": base( IntPtr.Zero )" );
						writer.decTabLevel();
					}
				}
				writer.newLine( 1 );
			}
			
			writer.outputLine( "{" );
			writer.incTabLevel();
			writer.output( "IntPtr mid = AndroidJNI.GetMethodID( " + root.getFullyQualifiedClassName().replace('$', '_') + ".ObjectClass, " );
			writer.outputLine( "\"" + node.getJNIName() + "\", \"" + node.getJNISignature() + "\" );");
			writer.output( "peerObject = AndroidJNI.NewGlobalRef( AndroidJNI.NewObject( " + root.getFullyQualifiedClassName().replace('$', '_') + ".ObjectClass, mid" );
			currentIndex = 0;
			
			// Add parameters for constructor
			if (node.getParameterCount() > 0) {
				writer.output(", AndroidJNIHelper.CreateJNIArgArray( new object[] { ");
				
				params = node.getParameterList();
				while ( params.hasNext() == true )
				{
					ClassNode currentParam = (ClassNode) params.next();
					if (currentParam.getFullyQualifiedClassName().equals( "java.lang.String" )) {
						writer.output(" p" + currentIndex++ );
						
					} else if ( currentParam.isPrimitive() == false ) {
						writer.output( " (IntPtr) p" + currentIndex++ );
						
					} else {
						writer.output( " p" + currentIndex++ );
						
					}
					
					if (params.hasNext()) 
						writer.output(", ");
				}
				
				writer.output(" } )");
			}
			else
			{
				writer.output(", new jvalue[]{} ");
			}
			
			writer.outputLine( " ) );" );
			writer.decTabLevel();
			writer.outputLine("}");
			writer.newLine(1);

		}
				
	}

	public void generateGetters(ClassNode root, FormattedFileWriter writer) throws IOException {
		// TODO Auto-generated method stub

	}

	public void generateSetters(ClassNode root, FormattedFileWriter writer) throws IOException {
		// TODO Auto-generated method stub

	}

	public void generateMethods(ClassNode root, FormattedFileWriter writer) throws IOException {
		writer.outputLine( "// methods" );
		Iterator it = root.getMethods();
		while ( it.hasNext() )
		{
			MethodNode node = (MethodNode) it.next();

			// Check if method is implemented in superclass or interfaces implemented
			if (root.isImplemented(node))
				continue;
		
			if (node.isAbstract()) 
				generateAbstractMethod(node, writer);
			else
				generateMethod(node, writer);
						
		}

	}
	
	public void generateMethod(MethodNode node, FormattedFileWriter writer) throws IOException {
		int currentIndex = 0;
		String midName = node.getCSName() + String.valueOf(node.getJNISignature().hashCode()).replace('-', '_') + "_mid";
		writer.outputLine("private static IntPtr " + midName + " = IntPtr.Zero;");
		
		if ( node.isStatic() == true )
			writer.output( "public static " );
		else
			writer.output( "public " );
		
		writer.output( getJNITypeName(node.getReturnType(), proxyGenSettings.getProject().getUsePartialSpec() ) + " " );
		writer.output( getMethodName(node, true) + "(" );

		Iterator params = node.getParameterList();
		while ( params.hasNext() == true )
		{
			ClassNode currentParam = (ClassNode) params.next();
			writer.output( getJNITypeName(currentParam, proxyGenSettings.getProject().getUsePartialSpec() ) + " p" + currentIndex++ );
			
			if ( params.hasNext() == true )
				writer.output( ", " );
		}
		writer.outputLine( ")" );

		// Obtaining method id block
		{
			writer.outputLine( "{" );
			writer.incTabLevel();
			writer.outputLine( "if ( " + midName + " == IntPtr.Zero )" );
			writer.incTabLevel();
			writer.output( midName + " = AndroidJNI.Get" );
			if ( node.isStatic() == true )
				writer.output( "Static" );
			writer.output( "MethodID( " + node.getParent().getFullyQualifiedClassName().replace('$', '_') + ".ObjectClass, " );
			writer.outputLine( "\"" + node.getJNIName() + "\", \"" + node.getJNISignature() + "\" );");
			writer.decTabLevel();
		}

		String returnTypeName = getJNITypeName(node.getReturnType(), proxyGenSettings.getProject().getUsePartialSpec() );
		if ( returnTypeName.equals( "void" ) == false )
		{
			writer.output( "return " );
			if ( node.getReturnType().needsProxy() == true  ) 
			{
				writer.output( "new " + returnTypeName + "( " );
				
			}
			else if ( node.getReturnType().isArray() ) 
			{
				writer.output( "new " + getJNITypeName(node.getReturnType(), proxyGenSettings.getProject().getUsePartialSpec() ) + "( " );
			}
			else if ( node.getReturnType().isString() )
				writer.output("");
			else if ( node.getReturnType().isPrimitive() == false )
				writer.output("(IntPtr)");
		}

		writer.output( getJNIMethodCall(node) );

		if ( node.isStatic() == true )
			writer.output( node.getParent().getFullyQualifiedClassName().replace('$', '_')  + ".ObjectClass, " );
		else
			writer.output( " this.PeerObject, " );
		
		writer.output( midName );
		
		if (node.getParameterCount() > 0) {
			writer.output(", AndroidJNIHelper.CreateJNIArgArray( new object[] { ");
			
			currentIndex = 0;
			params = node.getParameterList();
			while ( params.hasNext() == true )
			{
				ClassNode currentParam = (ClassNode) params.next();
				if (currentParam.getFullyQualifiedClassName().equals( "java.lang.String" )) {
					writer.output(" p" + currentIndex++ );
					
				} else if ( currentParam.isPrimitive() == false ) {
					writer.output( "(IntPtr) p" + currentIndex++ );
					
				} else {
					writer.output( "p" + currentIndex++ );
					
				}
				
				if (params.hasNext())
					writer.output(", ");
			}
			
			writer.output(" } )");
		}
		else
		{
			writer.output(", new jvalue[]{} ");
		}
		
		
		if ( returnTypeName.equals( "void" ) == false && 
			 node.getReturnType().isPrimitive() == false &&
			 node.getReturnType().isString() == false )
			writer.output( " ) " );

		writer.outputLine( " );" );
		
		writer.decTabLevel();
		writer.outputLine( "}" );
		writer.newLine( 1 );
	}
	
	public void generateAbstractMethod(MethodNode node, FormattedFileWriter writer) throws IOException {			
		int currentIndex = 0;
		
		// First, generate abstract method that will be implemented
		writer.output( "public abstract " + getJNITypeName(node.getReturnType(), proxyGenSettings.getProject().getUsePartialSpec() ) + " " );
		writer.output( getMethodName(node, true) + "(" );

		Iterator params = node.getParameterList();
		while ( params.hasNext() == true )
		{
			ClassNode currentParam = (ClassNode) params.next();
			writer.output( getJNITypeName(currentParam, proxyGenSettings.getProject().getUsePartialSpec() ) + " p" + currentIndex++ );
			
			if ( params.hasNext() == true )
				writer.output( ", " );
		}
		writer.outputLine( ");" );
		writer.newLine(1);
		
		
		currentIndex = 0;
		
		// Now, generate internal method that will be called from Unity internal
		writer.output( getJNITypeName(node.getReturnType(), proxyGenSettings.getProject().getUsePartialSpec() ) + 
					   " " + getMethodName(node) + "(" );
		params = node.getParameterList();
		while ( params.hasNext() == true )
		{
			ClassNode currentParam = (ClassNode) params.next();
			writer.output( getPlainJNITypeName(currentParam) + " p" + currentIndex++);
			
			if ( params.hasNext() == true )
				writer.output( ", " );
		}
		writer.outputLine( ")" );
		writer.outputLine("{");
		writer.incTabLevel();
		
		// Call abstract method
		String returnTypeName = getJNITypeName(node.getReturnType(), proxyGenSettings.getProject().getUsePartialSpec() );
		if ( returnTypeName.equals( "void" ) == false )
			writer.output( "return " );
			
		writer.output( getMethodName(node, true) + "(" ); 
		
		params = node.getParameterList();
		while ( params.hasNext() == true )
		{
			ClassNode currentParam = (ClassNode) params.next();
			writer.output( "(" + getJNITypeName( currentParam, proxyGenSettings.getProject().getUsePartialSpec() ) + ")" + " p" + currentIndex++);
			if (getPlainJNITypeName(currentParam).equals("AndroidJavaObject"))
				writer.output(".getRawObject()");
			
			if ( params.hasNext() == true )
				writer.output( ", " );
		}
		
		writer.outputLine( ");" );
		
		writer.decTabLevel();
		writer.outputLine("}");
		writer.newLine(1);
	}

	public String getClassName(ClassNode root) {
		String csName = Util.getCSIdentifier( root.getClassName() );
		return csName.replace( '$', '_' );
	}

	public String getFullyQualifiedClassName(ClassNode root) {
		if ( root.getNamespace().equals( "" ) == true )
			return getClassName(root);

		return (root.getNamespace() + "::" + getClassName(root)).replaceAll("::", ".");
	}

	public String getJNITypeName(ClassNode root, boolean usePartialSpec) {
		if ( root.needsProxy() == true )
			return (root.getNamespace() + "." + getClassName(root)).replaceAll("::", ".");

		if ( root.getFullyQualifiedClassName().equals( "java.lang.String" ) == true ) 
		{
			return "string";
		}

		// TODO handle array of objects
		if (root.isArray()) {
			if ( root.getComponentType().isPrimitive() )
				return "J" + 
						Character.toUpperCase( root.getComponentType().getClassName().charAt( 0 ) ) + 
						root.getComponentType().getClassName().substring( 1 ) + 
						"ArrayHelper";
	
			// TODO Handle array for C#
			if ( root.getComponentType().needsProxy() )
				return "JObjectArrayHelper<" + getJNITypeName(root.getComponentType(), usePartialSpec) + ">";
	
			if ( root.getComponentType().getFullyQualifiedClassName().equals( "java.lang.String" ) )
				return "JStringArrayHelper";
		
		}

		return getPlainJNITypeName(root);
	}

	public String getPlainJNITypeName(ClassNode root) {
		if ( root.isPrimitive() )
			if ( getClassName(root).equals( "void" ) )
				return "void";
			else {
				if ( getClassName(root).equals( "boolean" ) )
					return "bool";
				else
					return root.getClassName();
				
			}

		if ( root.isArray() == true )
			if ( root.getComponentType().getFullyQualifiedClassName().equals( "java.lang.String" ) == true ||
				 root.getComponentType().getFullyQualifiedClassName().equals( "java.lang.Class" ) == true ||
				 root.getArrayDims() > 1 )
				return "jobjectArray";
			else
				return getPlainJNITypeName(root.getComponentType()) + "[]";

		if ( root.getFullyQualifiedClassName().equals( "java.lang.String" ) == true )
			return "string";

		if ( root.getFullyQualifiedClassName().equals( "java.lang.Class" ) == true )
			return "IntPtr";

		return "AndroidJavaObject";
	}

	public String getJNIMethodCall(MethodNode node) {
		String jniMethodCall = "";
		ClassNode returnType = node.getReturnType();
		boolean staticMethod = node.isStatic();
		
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

	public String getMethodName(MethodNode node) {
		return getMethodName(node, false);
	}
	
	public String getMethodName(MethodNode node, boolean firstLetterUpper) {
		String name = Util.getCSIdentifier( node.getName() );
		
		if (firstLetterUpper) {
			name = name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
		}
		
		return name;
	}

}
