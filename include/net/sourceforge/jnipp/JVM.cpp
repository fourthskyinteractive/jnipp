#include "JVM.h"
#include "JNIEnvHelper.h"
#include "InitializationException.h"
#include "SharedLibraryHelper.h"

using namespace net::sourceforge::jnipp;

#ifdef WIN32
const char JVM::pathSeparator = ';';
#else
const char JVM::pathSeparator = ':';
#endif

JavaVM* JVM::javaVM = NULL;
std::string JVM::classPath;
std::map<std::string, std::string> JVM::defines;
std::vector<NativeMethodData*> JVM::natives;

void JVM::load()
{
#if defined(_VISION_ANDROID)
	// Specific initialization for Havok Vision for Android
	javaVM = AndroidApplication->activity->vm;
	
#else
	// Initialization for any desktop platforms
	char* jvmHome = getenv( "JVM_HOME" );
	if ( jvmHome == NULL )
		throw InitializationException( "JVM_HOME not set" );
	load( jvmHome );
	
#endif	
}

void JVM::load(const std::string& jvmLibraryPath)
{
#if defined(_VISION_ANDROID)
	// Specific initialization for Havok Vision for Android
	javaVM = AndroidApplication->activity->vm;
	
#else
	JavaVMInitArgs vmArgs;
	vmArgs.version = JNI_VERSION_1_2;

	SharedLibraryHelper shLib;
	shLib.load( jvmLibraryPath.c_str() );

	jint (JNICALL *pJNI_GetDefaultJavaVMInitArgs)(void*) = (jint (JNICALL *)(void*)) shLib.resolveSymbol( "JNI_GetDefaultJavaVMInitArgs" );

	jint res = pJNI_GetDefaultJavaVMInitArgs( reinterpret_cast<void*>( &vmArgs ) );

	int numOptions = defines.size() + 1;	// add 1 for the classpath
	JavaVMOption* options = new JavaVMOption[numOptions];
	std::map<std::string, std::string>::iterator it = defines.begin();

	// set the classpath
	std::string* optionStrings = new std::string[numOptions];
	if ( classPath == "" )
	{
		char* cp = getenv( "CLASSPATH" );
		classPath = (cp == NULL ? "" : cp);
	}
	optionStrings[0] = ("-Djava.class.path=" + classPath).c_str();
	options[0].optionString = const_cast<char *>( optionStrings[0].c_str() );

	// set the defines
	for ( int i = 1; it != defines.end(); ++it, ++i )
	{
		optionStrings[i] = ("-D" + it->first + "=" + it->second).c_str();
		options[i].optionString = const_cast<char *>( optionStrings[i].c_str() );
	}

	vmArgs.options = options;
	vmArgs.nOptions = numOptions;

	JNIEnv* jniEnv = NULL;

	jint (JNICALL *pJNI_CreateJavaVM)(JavaVM**, void**, void*) = (jint (JNICALL *)(JavaVM**, void**, void*)) shLib.resolveSymbol( "JNI_CreateJavaVM" );

	res = pJNI_CreateJavaVM( &javaVM, reinterpret_cast<void**>( &jniEnv ), &vmArgs );

	JNIEnvHelper::init( jniEnv );

	std::vector<NativeMethodData*>::iterator it2 = natives.begin();
	for ( ; it2 != natives.end(); ++it2 )
	{
		jclass cls = reinterpret_cast<jclass>( JNIEnvHelper::NewGlobalRef( JNIEnvHelper::FindClass( (*it2)->className.c_str() ) ) );
		JNIEnvHelper::RegisterNatives( cls, (*it2)->methods, (*it2)->numMethods );
		JNIEnvHelper::DeleteGlobalRef( reinterpret_cast<jobject>(cls) );
	}

	delete[] optionStrings;
#endif
}


void JVM::unload()
{
#if defined(_VISION_ANDROID)
	javaVM = null;
	
#else
	JNIEnvHelper::uninit();
	javaVM->DestroyJavaVM();
	std::vector<NativeMethodData*>::iterator it = natives.begin();
	for ( ; it != natives.end(); ++it )
		delete *it;
		
#endif
}

void JVM::appendClassPath(const std::string& classPath)
{
#if !defined(_VISION_ANDROID)
	if ( JVM::classPath == "" )
	{
		char* cp = getenv( "CLASSPATH" );
		JVM::classPath = (cp == NULL ? "" : cp);
	}

	JVM::classPath += pathSeparator;
	JVM::classPath += classPath;
#endif
}

void JVM::clearClassPath()
{
	classPath = "";
}

void JVM::addDefine(const std::string& key, const std::string& value)
{
	defines.insert( std::pair<std::string, std::string>( key, value ) );
}

void JVM::clearDefines()
{
	defines.clear();
}

jint JVM::attachCurrentThread()
{
	JNIEnv* jniEnv = NULL;
	jint retVal = javaVM->AttachCurrentThread( reinterpret_cast<void**>( &jniEnv ), NULL );
	JNIEnvHelper::init( jniEnv );
	return retVal;
}

jint JVM::detachCurrentThread()
{
	JNIEnvHelper::uninit();
	return javaVM->DetachCurrentThread();
}

void JVM::registerNatives(const std::string& className, JNINativeMethod nativeMethods[], int numMethods)
{
	natives.push_back( new NativeMethodData( className, nativeMethods, numMethods ) );
}
