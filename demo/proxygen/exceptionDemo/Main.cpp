#include <iostream.h>
#include "net/sourceforge/jnipp/JVM.h"
#include "demo/proxygen/exceptionDemo/ExceptionDemoProxy.h"
#include "java/lang/ThrowableProxy.h"
#include "net/sourceforge/jnipp/BaseException.h"
#include "net/sourceforge/jnipp/JStringHelper.h"

using namespace net::sourceforge::jnipp;
using namespace demo::proxygen::exceptionDemo;
using namespace java::lang;

int main(int argc, char** argv)
{
	try
	{
#ifdef WIN32
		JVM::load( SUN_JRE );
#else
		JVM::load( "libjvm.so" );
#endif
		ExceptionDemoProxy edp;
		try
		{
			edp.throwAnException();
		}
		catch(jthrowable thr)
		{
			cerr << "caught java.lang.Throwable -- using proxy class to print message and stack trace" << endl;
			ThrowableProxy tp( thr );
			JStringHelper msg( tp.getMessage() );
			cerr << "Message: " << msg << endl;
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
