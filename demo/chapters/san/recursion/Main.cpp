#include <iostream.h>
#include "net/sourceforge/jnipp/JVM.h"
#include "net/sourceforge/jnipp/JNIEnvHelper.h"
#include "demo/chapters/san/recursion/RecursionDemoProxy.h"
#include "java/lang/IntegerProxy.h"
#include "java/lang/DoubleProxy.h"
#include "net/sourceforge/jnipp/BaseException.h"

using namespace net::sourceforge::jnipp;
using namespace demo::chapters::san::recursion;
using namespace java::lang;

int main(int argc, char** argv)
{
	try
	{
		/*
		 * Note that we are relying on the JVM class to resolve the location of the JVM through the
		 * JVM_HOME environment variable.  An exception will be thrown if it is not set.
		 */
		JVM::load();

		RecursionDemoProxy rdp;
		rdp.printValue( DoubleProxy( 123.456 ) );
		rdp.printValue1( IntegerProxy( 123 ) );
		
		JVM::unload();
	}
	catch(BaseException& ex)
	{
		cerr << "caught exception: " << ex.getMessage().c_str() << endl;
	}

	return 0;
}
