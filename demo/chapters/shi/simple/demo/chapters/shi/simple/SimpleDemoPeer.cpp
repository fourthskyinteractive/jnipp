#include "SimpleDemoPeer.h"
#include "net/sourceforge/jnipp/JStringHelper.h"
#include <iostream.h>

using namespace demo::chapters::shi::simple;
using namespace net::sourceforge::jnipp;

SimpleDemoPeer::SimpleDemoPeer()
{
}

// methods
void SimpleDemoPeer::printMessage(JNIEnv* env, jobject obj, jstring p0)
{
	// TODO: Fill in your implementation here
	cout << "The following message was printed from the C++ peer: " << JStringHelper( p0 ) << endl;
}

