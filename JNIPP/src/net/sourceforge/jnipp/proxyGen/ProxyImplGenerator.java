package net.sourceforge.jnipp.proxyGen;

import net.sourceforge.jnipp.common.ClassNode;
import net.sourceforge.jnipp.common.FormattedFileWriter;
import net.sourceforge.jnipp.common.MethodNode;
import net.sourceforge.jnipp.project.ProxyGenSettings;

public interface ProxyImplGenerator {

void generate(ClassNode root, ProxyGenSettings proxyGenSettings) throws java.io.IOException;
	
	void generateIncludes(ClassNode root, FormattedFileWriter writer) throws java.io.IOException;
	
	void generateUsing(ClassNode root, FormattedFileWriter writer) throws java.io.IOException;
	
	void generateCtors(ClassNode root, FormattedFileWriter writer) throws java.io.IOException;
	
	void generateGetters(ClassNode root, FormattedFileWriter writer) throws java.io.IOException;
	
	void generateSetters(ClassNode root, FormattedFileWriter writer) throws java.io.IOException;
	
	void generateMethods(ClassNode root, FormattedFileWriter writer) throws java.io.IOException;
	
	String getClassName(ClassNode root);
	
	String getFullyQualifiedClassName(ClassNode root);
	
	String getJNITypeName(ClassNode root, boolean usePartialSpec);
	
	String getPlainJNITypeName(ClassNode root);
	
	String getJNIMethodCall(MethodNode node);
	
	String getMethodName(MethodNode node);
}
