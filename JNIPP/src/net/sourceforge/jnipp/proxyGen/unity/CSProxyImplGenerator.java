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
			fullFileName = root.getCPPClassName() + ".cs";

		} else {
			fullFileName = root.getPackageName().replace( '.', '_' ) + '_' + root.getCPPClassName() + ".cs";

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

		writer.output( "public class " + root.getCPPClassName() );
		if ( proxyGenSettings.getUseInheritance() == true )
		{
			if ( root.isInterface() == false )
			{
				if ( root.getSuperClass() != null )
					writer.output( " : " + root.getSuperClass().getFullyQualifiedCSClassName() );
			}
			else
			{
				Iterator intfcs = root.getInterfaces();
				if ( intfcs.hasNext() == true )
				{
					writer.output( " : " + ((ClassNode) intfcs.next()).getFullyQualifiedCSClassName() );
					while ( intfcs.hasNext() == true )
						writer.output( ", " + ((ClassNode) intfcs.next()).getFullyQualifiedCPPClassName() );
				}
			}
		}

		writer.outputLine( "" );
		writer.outputLine( "{" );
		writer.incTabLevel();

		// Generate private fields
		writer.outputLine("private const string className = \"" +
				root.getFullyQualifiedClassName().replace('.', '/') + 
				"\";");
		writer.outputLine("private static IntPtr objectClass = IntPtr.Zero;");
		writer.outputLine("private IntPtr peerObject = IntPtr.Zero;");
		writer.newLine( 1 );
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

		writer.outputLine("public static string ObjectClass {");
		writer.incTabLevel();
		writer.outputLine("get {");
		writer.incTabLevel();
		writer.outputLine( "if ( objectClass == NULL )" );
		writer.incTabLevel();
		writer.outputLine( "objectClass = AndroidJNI.NewGlobalRef( AndroidJNI.FindClass( className ) );" );
		writer.decTabLevel();
		writer.outputLine("return objectClass;");
		writer.decTabLevel();
		writer.outputLine("}");
		writer.decTabLevel();
		writer.outputLine("}");
		writer.newLine( 1 );

		writer.outputLine("public static string PeerObject {");
		writer.incTabLevel();
		writer.outputLine("get {");
		writer.incTabLevel();
		writer.outputLine("return peerObject;");
		writer.decTabLevel();
		writer.outputLine("}");
		writer.decTabLevel();
		writer.outputLine("}");
		writer.newLine( 1 );

		// Generate constructors
		generateCtors( root, writer );

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

		writer.outputLine( root.getCPPClassName() + "(IntPtr obj)" );

		writer.outputLine( "{" );
		writer.incTabLevel();
		writer.outputLine( "peerObject = AndroidJNI.NewGlobalRef( obj );" );
		writer.decTabLevel();
		writer.outputLine( "}" );
		writer.newLine( 1 );

		// Iterate thought declared java constructors		
		Iterator it = root.getConstructors();
		while ( it.hasNext() )
		{
			int currentIndex = 0;
			MethodNode node = (MethodNode) it.next();
			writer.output( root.getCPPClassName() + "(" );
			Iterator params = node.getParameterList();
			for ( int count = 0; params.hasNext() == true; ++count )
			{
				ClassNode currentParam = (ClassNode) params.next();
				if ( currentParam.getFullyQualifiedClassName().equals( root.getFullyQualifiedClassName() ) == true && 
					 count == 0 && 
					 params.hasNext() == false ) {
					writer.output( currentParam.getJNITypeName( proxyGenSettings.getProject().getUsePartialSpec() ) + "& p" + currentIndex++ );
					
				} else {
					writer.output( currentParam.getJNITypeName( proxyGenSettings.getProject().getUsePartialSpec() ) + " p" + currentIndex++ );
					
				}

				if ( params.hasNext() == true )
					writer.output( ", " );
			}
			writer.outputLine( ")" );
			
			writer.outputLine( "{" );
			writer.incTabLevel();
			writer.output( "IntPtr mid = AndroidJNI.GetMethodID( this.ObjectClass, " );
			writer.outputLine( "\"" + node.getJNIName() + "\", \"" + node.getJNISignature() + "\" );");
			writer.output( "peerObject = AndroidJNI.NewGlobalRef( AndroidJNI.NewObject( this.ObjectClass, mid" );
			currentIndex = 0;
			params = node.getParameterList();
			while ( params.hasNext() == true )
			{
				ClassNode currentParam = (ClassNode) params.next();
				writer.output( ", (" + currentParam.getPlainJNITypeName() + ") p" + currentIndex++ + " )" );
			}
			writer.outputLine( " );" );
			writer.decTabLevel();
			writer.outputLine("}");
		}

	}

	public void generateGetters(ClassNode root, FormattedFileWriter writer) throws IOException {
		// TODO Auto-generated method stub

	}

	public void generateSetters(ClassNode root, FormattedFileWriter writer) throws IOException {
		// TODO Auto-generated method stub

	}

	public void generateMethods(ClassNode root, FormattedFileWriter writer) throws IOException {
		// TODO Auto-generated method stub

	}

}
