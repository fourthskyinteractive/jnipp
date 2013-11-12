#include <iostream.h>
#include "net/sourceforge/jnipp/JVM.h"
#include "net/sourceforge/jnipp/gui/AppProxy.h"
#include "java/lang/ThrowableProxy.h"
#include "net/sourceforge/jnipp/BaseException.h"
#include "net/sourceforge/jnipp/JStringHelperArray.h"
#include "net/sourceforge/jnipp/JStringHelper.h"

using namespace net::sourceforge::jnipp;
using namespace net::sourceforge::jnipp::gui;
using namespace java::lang;

int main(int argc, char** argv)
{
	try
	{
		std::string jarCP = getenv( "JNIPP_HOME" );
#ifdef WIN32
		JVM::appendClassPath( jarCP + "\\lib\\jnipp.jar" );
		JVM::load( SUN_JRE );
#else
		JVM::load( "libjvm.so" );
#endif
		try
		{
			JStringHelperArray args;
			args.addElement( JStringHelper( "" ) );
			AppProxy::main( args );
		}
		catch(jthrowable thr)
		{
			ThrowableProxy tp( thr );
			tp.printStackTrace();
		}
		
		JVM::unload();
	}
	catch(BaseException& ex)
	{
		cerr << "caught exception: " << ex.getMessage().c_str() << endl;
	}

	return 0;
}
