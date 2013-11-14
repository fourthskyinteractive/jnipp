#include <iostream.h>
#include "net/sourceforge/jnipp/JVM.h"
#include "net/sourceforge/jnipp/JNIEnvHelper.h"
#include "demo/chapters/san/simple/SimpleDemo.h"
#include "net/sourceforge/jnipp/BaseException.h"

using namespace net::sourceforge::jnipp;
using namespace demo::chapters::san::simple;

int main(int argc, char** argv)
{
	try
	{
		/*
		 * Note that we are relying on the JVM class to resolve the location of the JVM through the
		 * JVM_HOME environment variable.  An exception will be thrown if it is not set.
		 */
		JVM::load();

		SimpleDemo sdp;
		
		jclass cls = static_cast<jclass>( JNIEnvHelper::NewGlobalRef( JNIEnvHelper::FindClass( "java/lang/Integer" ) ) );
		jmethodID mid = JNIEnvHelper::GetMethodID( cls, "<init>", "(I)V" );
		jobject integerParam = JNIEnvHelper::NewGlobalRef( JNIEnvHelper::NewObject( cls, mid, 42 ) );
		
		sdp.printValue( integerParam );
		JVM::unload();
	}
	catch(BaseException& ex)
	{
		cerr << "caught exception: " << ex.getMessage().c_str() << endl;
	}

	return 0;
}
