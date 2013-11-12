#include "net/sourceforge/jnipp/JNIEnvHelper.h"
#include "MyCoolClass.h"

// includes for parameter and return type proxy classes

using namespace net::sourceforge::jnipp;
using namespace demo::proxygen::tutorial;


std::string MyCoolClass::className = "demo/proxygen/tutorial/MyCoolClass";
jclass MyCoolClass::objectClass = NULL;

jclass MyCoolClass::_getObjectClass()
{
	if ( objectClass == NULL )
		objectClass = static_cast<jclass>( JNIEnvHelper::NewGlobalRef( JNIEnvHelper::FindClass( className.c_str() ) ) );

	return objectClass;
}

MyCoolClass::MyCoolClass(void* unused)
{
}

jobject MyCoolClass::_getPeerObject() const
{
	return peerObject;
}

jclass MyCoolClass::getObjectClass()
{
	return _getObjectClass();
}

MyCoolClass::operator jobject()
{
	return _getPeerObject();
}

// constructors
MyCoolClass::MyCoolClass(jobject obj)
{
	peerObject = JNIEnvHelper::NewGlobalRef( obj );
}

MyCoolClass::MyCoolClass()
{
	jmethodID mid = JNIEnvHelper::GetMethodID( _getObjectClass(), "<init>", "()V" );
	peerObject = JNIEnvHelper::NewGlobalRef( JNIEnvHelper::NewObject( _getObjectClass(), mid ) );
}

MyCoolClass::MyCoolClass(::net::sourceforge::jnipp::JStringHelper p0)
{
	jmethodID mid = JNIEnvHelper::GetMethodID( _getObjectClass(), "<init>", "(Ljava/lang/String;)V" );
	peerObject = JNIEnvHelper::NewGlobalRef( JNIEnvHelper::NewObject( _getObjectClass(), mid, static_cast<jstring>( p0 ) ) );
}

MyCoolClass::~MyCoolClass()
{
	JNIEnvHelper::DeleteGlobalRef( peerObject );
}

MyCoolClass& MyCoolClass::operator=(const MyCoolClass& rhs)
{
	JNIEnvHelper::DeleteGlobalRef( peerObject );
	peerObject = JNIEnvHelper::NewGlobalRef( rhs.peerObject );
	return *this;
}

// attribute setters
void MyCoolClass::setSomeData(::net::sourceforge::jnipp::JStringHelper someData)
{
	jfieldID fid = JNIEnvHelper::GetFieldID( _getObjectClass(), "someData", "Ljava/lang/String;" );
	JNIEnvHelper::SetObjectField( _getPeerObject(), fid, someData );
}

void MyCoolClass::setSomeStaticData(::net::sourceforge::jnipp::JStringHelper someStaticData)
{
	jfieldID fid = JNIEnvHelper::GetStaticFieldID( _getObjectClass(), "someStaticData", "Ljava/lang/String;" );
	JNIEnvHelper::SetStaticObjectField( _getObjectClass(), fid, someStaticData );
}

// methods
::net::sourceforge::jnipp::JStringHelper MyCoolClass::getSomeData()
{
	static jmethodID mid = NULL;
	if ( mid == NULL )
		mid = JNIEnvHelper::GetMethodID( _getObjectClass(), "getSomeData", "()Ljava/lang/String;" );
	return ::net::sourceforge::jnipp::JStringHelper( JNIEnvHelper::CallObjectMethod( _getPeerObject(), mid )  );
}

::net::sourceforge::jnipp::JStringHelper MyCoolClass::getSomeStaticData()
{
	static jmethodID mid = NULL;
	if ( mid == NULL )
		mid = JNIEnvHelper::GetStaticMethodID( _getObjectClass(), "getSomeStaticData", "()Ljava/lang/String;" );
	return ::net::sourceforge::jnipp::JStringHelper( JNIEnvHelper::CallStaticObjectMethod( _getObjectClass(), mid )  );
}

void MyCoolClass::printContents(::demo::proxygen::tutorial::MyCoolClass p0)
{
	static jmethodID mid = NULL;
	if ( mid == NULL )
		mid = JNIEnvHelper::GetStaticMethodID( _getObjectClass(), "printContents", "(Ldemo/proxygen/tutorial/MyCoolClass;)V" );
	JNIEnvHelper::CallStaticVoidMethod( _getObjectClass(), mid, static_cast<jobject>( p0 ) );
}

