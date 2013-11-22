package net.sourceforge.jnipp.proxyGen.unity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.jnipp.common.ClassNode;
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

		boolean isNotObjectClass = root.getFullyQualifiedClassName().equals("java.lang.Object") == false;
		
		this.proxyGenSettings = proxyGenSettings;

		String fullFileName = proxyGenSettings.getProject().getCPPOutputDir() + /*File.separatorChar*/ '/';
		if ( root.getPackageName().equals( "" ) == true ) {
			fullFileName = getClassName(root) + ".cs";

		} else {
			fullFileName = root.getPackageName().replace( '.', '_' ) + '_' + getClassName(root) + ".cs";

		}

		FormattedFileWriter writer = new FormattedFileWriter( fullFileName, true );

		generateIncludes( root, writer );

		Iterator it = root.getNamespaceElements();
		while ( it.hasNext() == true )
		{
			String current = (String) it.next();
			writer.outputLine( "namespace " + current );
			writer.outputLine( "{" );
			writer.incTabLevel();
		}

		
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
		writer.outputLine( "if ( objectClass == null )" );
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
			writer.outputLine("public string PeerObject {");
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
		
		writer.outputLine("public static explicit operator IntPtr(" + getClassName(root) + " obj) {");
		writer.incTabLevel();
		writer.outputLine("return obj.PeerObject;");
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

		for ( it = root.getNamespaceElements(); it.hasNext() == true; it.next() )
		{
			writer.decTabLevel();
			writer.outputLine( "}" );
		}

		writer.flush();
		writer.close();
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

		/*
		if ( proxyGenSettings.getUseRichTypes() == true )
		{
			writer.newLine( 1 );
			ArrayList arrayIncludes = new ArrayList();
			ArrayList otherIncludes = new ArrayList();
			ArrayList componentTypeIncludes = new ArrayList();
			writer.outputLine( "// includes for parameter and return type proxy classes" );
			Iterator it = root.getDependencies();
			while ( it.hasNext() == true )
			{
				ClassNode cn = (ClassNode) it.next();
				if ( alreadyGenerated.get( cn.getFullyQualifiedCPPClassName() ) == null )
					if ( cn.isArray() == true )
					{
						if ( cn.getComponentType().needsProxy() == true )
						{
							String headerFile = cn.getComponentType().getPackageName().replace( '.', '_' );
							if ( headerFile.equals( "" ) == false ) {
								headerFile += '_';

							}
							headerFile += cn.getComponentType().getCPPClassName() + ".h";
							componentTypeIncludes.add( headerFile );
							alreadyGenerated.put( cn.getComponentType().getFullyQualifiedCPPClassName(), cn.getComponentType() );
						}
					}
					else
						if ( cn.needsProxy() == true )
						{
							String headerFile = cn.getPackageName().replace( '.', '_' );
							if ( headerFile.equals( "" ) == false ) {
								headerFile += '_';

							}
							headerFile += cn.getCPPClassName() + (cn.needsProxy() == true ? "Forward.h" : ".h");
							otherIncludes.add( headerFile );
							alreadyGenerated.put( cn.getFullyQualifiedCPPClassName(), cn );
						}
			}

			it = arrayIncludes.iterator();
			while ( it.hasNext() == true )
				writer.outputLine( "#include \"" + (String) it.next() + "\"" );
			it = otherIncludes.iterator();
			while ( it.hasNext() == true )
				writer.outputLine( "#include \"" + (String) it.next() + "\"" );
			it = componentTypeIncludes.iterator();
			while ( it.hasNext() == true )
				writer.outputLine( "#include \"" + (String) it.next() + "\"" );
		}
		 */

		Iterator<String> it = alreadyGenerated.iterator();
		while(it.hasNext()) {
			writer.outputLine("using " + it.next() + ";");
		}
		writer.newLine( 1 );
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
		
//		boolean hasDefaultConstructor = false;

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

			
			// Mark class as having a default constructor
//			if (node.getParameterCount() == 0)
//				hasDefaultConstructor = true;
		}
		
		// If class don't have a default constructor, create one as "protected",
		// used only by C#
//		if (!hasDefaultConstructor) {
//			writer.outputLine( "protected " + root.getCPPClassName() + "()" );
//			writer.outputLine( "{" );
//			writer.outputLine( "}" );
//			writer.newLine( 1 );
//			
//		}
		
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
		
			int currentIndex = 0;
			String midName = node.getCSName() + String.valueOf(node.getJNISignature().hashCode()).replace('-', '_') + "_mid";
			writer.outputLine("private static IntPtr " + midName + " = null;");
			
			writer.output( "public " + getJNITypeName(node.getReturnType(), proxyGenSettings.getProject().getUsePartialSpec() ) + " " );
			writer.output( node.getCPPName() + "(" );

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
				writer.outputLine( "if ( " + midName + " == NULL )" );
				writer.incTabLevel();
				writer.output( midName + " = AndroidJNI.Get" );
				if ( node.isStatic() == true )
					writer.output( "Static" );
				writer.output( "MethodID( " + root.getFullyQualifiedClassName().replace('$', '_') + ".ObjectClass, " );
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
				writer.output( root.getFullyQualifiedClassName().replace('$', '_')  + ".ObjectClass, " );
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
		if ( root.isArray() == true && root.getComponentType().isPrimitive() == true )
			return "J" + 
					Character.toUpperCase( root.getComponentType().getClassName().charAt( 0 ) ) + 
					root.getComponentType().getClassName().substring( 1 ) + 
					"ArrayHelper";

		// TODO Handle array for C#
		if ( root.isArray() == true && root.getComponentType().needsProxy() == true )
			return "JObjectArrayHelper<" + getJNITypeName(root.getComponentType(), usePartialSpec) + ">";

		if ( root.isArray() == true && root.getComponentType().getFullyQualifiedClassName().equals( "java.lang.String" ) == true )
			return "string[]";

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

		return "IntPtr";
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
		return Util.getCSIdentifier( node.getName() );
	}

}
