#include "SimplePeer.h"
#include "java/lang/IllegalArgumentExceptionProxy.h"
#include "net/sourceforge/jnipp/JStringHelper.h"

using namespace demo::chapters::ichi::jnippExample;
using namespace java::lang;
using namespace net::sourceforge::jnipp;

SimplePeer::SimplePeer()
{
}

// methods
jstring SimplePeer::getData(JNIEnv* env, jobject obj)
{
	// TODO: Fill in your implementation here
	return JStringHelper( delegate.getData().c_str() );
}

void SimplePeer::loadByID(JNIEnv* env, jobject obj, jstring p0)
{
	// TODO: Fill in your implementation here
	if ( p0 == NULL )
	{
		throw static_cast<jthrowable>(IllegalArgumentExceptionProxy( JStringHelper( "id parameter cannot be NULL" ) ) );
	}
	
	delegate.loadByID( static_cast<const char*>(JStringHelper( p0 )) );
}

