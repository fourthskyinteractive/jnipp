#include <iostream.h>
#include "net/sourceforge/jnipp/JVM.h"
#include "net/sourceforge/jnipp/JNIEnvHelper.h"
#include "demo/chapters/san/inheritance/InheritanceDemoProxy.h"
#include "net/sourceforge/jnipp/BaseException.h"
#include "net/sourceforge/jnipp/JStringHelper.h"

using namespace net::sourceforge::jnipp;
using namespace demo::chapters::san::inheritance;

int main(int argc, char** argv)
{
	try
	{
		/*
		 * Note that we are relying on the JVM class to resolve the location of the JVM through the
		 * JVM_HOME environment variable.  An exception will be thrown if it is not set.
		 */
		JVM::load();

		InheritanceDemoProxy derivedInstance;
		derivedInstance.printMessage();
		
		// the printBaseMessage() is inherited
		derivedInstance.printBaseMessage();
		
		// the toString() method is inherited also
		cerr << JStringHelper( derivedInstance.toString() ) << endl;
		
		InheritanceDemoBaseProxy idbp( derivedInstance );
		derivedInstance.printMessage();
		
		InheritanceDemoBaseProxy baseInstance;
		baseInstance.printMessage();

		JVM::unload();
	}
	catch(BaseException& ex)
	{
		cerr << "caught exception: " << ex.getMessage().c_str() << endl;
	}

	return 0;
}
