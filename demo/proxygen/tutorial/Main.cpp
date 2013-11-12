#include <iostream.h>
#include "net/sourceforge/jnipp/JVM.h"
#include "demo/proxygen/tutorial/MyCoolClassProxy.h"
#include "net/sourceforge/jnipp/BaseException.h"
#include "net/sourceforge/jnipp/JStringHelper.h"

using namespace net::sourceforge::jnipp;
using namespace demo::proxygen::tutorial;

int main(int argc, char** argv)
{
	try
	{
#ifdef WIN32
		JVM::load( SUN_JRE );
#else
		JVM::load( "libjvm.so" );
#endif
		MyCoolClassProxy p1;
		p1.setSomeData( JStringHelper( "some instance data" ) );
		MyCoolClassProxy::setSomeStaticData( JStringHelper( "some static data" ) );
		MyCoolClassProxy::printContents( p1 );
		JVM::unload();
	}
	catch(BaseException& ex)
	{
		cerr << "caught exception: " << ex.getMessage().c_str() << endl;
	}

	return 0;
}
