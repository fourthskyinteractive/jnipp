#include <iostream.h>
#include "net/sourceforge/jnipp/JVM.h"
#include "net/sourceforge/jnipp/JNIEnvHelper.h"
#include "net/sourceforge/jnipp/JStringHelperArray.h"
#include "net/sourceforge/jnipp/main/MainProxy.h"
#include "net/sourceforge/jnipp/BaseException.h"

using namespace net::sourceforge::jnipp;
using namespace net::sourceforge::jnipp::main;

int main(int argc, char** argv)
{
	try
	{
		char* jnippHome = getenv( "JNIPP_HOME" );
		if ( jnippHome == NULL )
		{
			cerr << "Please set the JNIPP_HOME environment variable before launching." << endl;
			return 1;
		}
		
		if ( getenv( "JVM_HOME" ) == NULL )
		{
			cerr << "Please set the JVM_HOME environment variable before launching." << endl;
			return 2;
		}

		std::string jnippJar = jnippHome;
		jnippJar += "/lib/jnipp.jar";
		JVM::appendClassPath( jnippJar );
		JVM::load();
		JStringHelperArray<1> args;
		for ( int i = 0; i < argc; ++i )
			args.addElement( JStringHelper( argv[i] ) );
		MainProxy::main( args );
		JVM::unload();
	}
	catch(BaseException& ex)
	{
		cerr << "caught exception: " << ex.getMessage().c_str() << endl;
	}

	return 0;
}
