#include <iostream.h>
#include "net/sourceforge/jnipp/JVM.h"
#include "net/sourceforge/jnipp/JNIEnvHelper.h"
#include "demo/chapters/san/rich/RichDemoProxy.h"
#include "java/util/ArrayListProxy.h"
#include "java/util/CollectionProxy.h"
#include "java/lang/ObjectProxy.h"
#include "net/sourceforge/jnipp/BaseException.h"

using namespace net::sourceforge::jnipp;
using namespace demo::chapters::san::rich;
using namespace java::util;
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

		RichDemoProxy rdp;
		ArrayListProxy alp;
		
		alp.add( ObjectProxy( JStringHelper( "Come" ) ) );
		alp.add( ObjectProxy( JStringHelper( "up" ) ) );
		alp.add( ObjectProxy( JStringHelper( "and" ) ) );
		alp.add( ObjectProxy( JStringHelper( "C++" ) ) );
		alp.add( ObjectProxy( JStringHelper( "me" ) ) );
		alp.add( ObjectProxy( JStringHelper( "sometime" ) ) );

		// note the use of the "alternative to inheritance" -- instantiating a CollectionProxy with the
		// ArrayListProxy (rather than ArrayListProxy derived from CollectionProxy)
		rdp.printCollection( CollectionProxy( alp ) );
		JVM::unload();
	}
	catch(BaseException& ex)
	{
		cerr << "caught exception: " << ex.getMessage().c_str() << endl;
	}

	return 0;
}
