package net.sourceforge.jnipp.proxyGen;

import net.sourceforge.jnipp.common.ClassNode;
import net.sourceforge.jnipp.common.FormattedFileWriter;
import net.sourceforge.jnipp.project.ProxyGenSettings;

public interface ProxyHeaderGenerator {

	void generate(ClassNode root, ProxyGenSettings proxyGenSettings) throws java.io.IOException;
	
	void generateIncludes(ClassNode root, FormattedFileWriter writer) throws java.io.IOException;
	
	void generateCtors(ClassNode root, FormattedFileWriter writer) throws java.io.IOException;
	
	void generateGetters(ClassNode root, FormattedFileWriter writer) throws java.io.IOException;
	
	void generateSetters(ClassNode root, FormattedFileWriter writer) throws java.io.IOException;
	
	void generateMethods(ClassNode root, FormattedFileWriter writer) throws java.io.IOException;
}
