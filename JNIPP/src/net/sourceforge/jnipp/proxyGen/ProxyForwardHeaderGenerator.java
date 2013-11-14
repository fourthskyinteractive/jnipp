package net.sourceforge.jnipp.proxyGen;

import net.sourceforge.jnipp.common.ClassNode;
import net.sourceforge.jnipp.project.ProxyGenSettings;

public interface ProxyForwardHeaderGenerator {

	void generate(ClassNode root, ProxyGenSettings proxyGenSettings) throws java.io.IOException;
}
