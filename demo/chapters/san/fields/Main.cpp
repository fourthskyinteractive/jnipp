#include <iostream.h>
#include "net/sourceforge/jnipp/JVM.h"
#include "net/sourceforge/jnipp/JNIEnvHelper.h"
#include "net/sourceforge/jnipp/JStringHelper.h"
#include "demo/chapters/san/fields/FieldsDemoProxy.h"
#include "net/sourceforge/jnipp/BaseException.h"

using namespace net::sourceforge::jnipp;
using namespace demo::chapters::san::fields;

int main(int argc, char** argv)
{
	try
	{
		/*
		 * Note that we are relying on the JVM class to resolve the location of the JVM through the
		 * JVM_HOME environment variable.  An exception will be thrown if it is not set.
		 */
		JVM::load();

		FieldsDemoProxy fdp( static_cast<jstring>( JStringHelper( "Initial Value" ) ) );
		cout << "Before modifying, the value is \"" << JStringHelper( fdp.getStringField() ) << "\"" << endl;
		fdp.setStringField( JStringHelper( "New Value" ) );
		cout << "After modifying, the value is \"" << JStringHelper( fdp.getStringField() ) << "\"" << endl;
		
		JVM::unload();
	}
	catch(BaseException& ex)
	{
		cerr << "caught exception: " << ex.getMessage().c_str() << endl;
	}

	return 0;
}
