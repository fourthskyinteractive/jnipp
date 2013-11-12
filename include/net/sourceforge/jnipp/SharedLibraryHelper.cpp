#include "SharedLibraryHelper.h"
#include "LibraryLoadException.h"
#include "LibraryNotLoadedException.h"
#include "ResolveSymbolException.h"

using namespace net::sourceforge::jnipp;

SharedLibraryHelper::SharedLibraryHelper()
	: handle( NULL )
{
}

SharedLibraryHelper::~SharedLibraryHelper()
{
}

void SharedLibraryHelper::load(const std::string& libName)
{
	this->libName = libName;
#ifdef WIN32
	handle = LoadLibraryA( libName.c_str() );
#elif MACOSX
	NSObjectFileImage image;
	if ( NSCreateObjectFileImageFromFile == NSCreateObjectFileImageFromFile( libName.c_str(), &image ) )
		handle = NSLinkModule( image, libName.c_str(), NSLINKMODULE_OPTION_BINDNOW );
#else
	handle = dlopen( libName.c_str(), RTLD_NOW | RTLD_GLOBAL );
#endif
	if ( handle == NULL )
		throw LibraryLoadException( libName );
}

void* SharedLibraryHelper::resolveSymbol(const std::string& symbolName)
{
	if ( handle == NULL )
		throw LibraryNotLoadedException();

	void* symbolAddress = NULL;	
#ifdef WIN32
	symbolAddress = GetProcAddress( handle, symbolName.c_str() );
#elif MACOSX
	NSSymbol symbol = NSLookupSymbolInModule( handle, symbolName.c_str() );
	symbolAddress = NSAddressOfSymbol( symbol );
#else
	symbolAddress = dlsym( handle, symbolName.c_str() );
#endif
	if ( symbolAddress == NULL )
		throw ResolveSymbolException( symbolName );

	return symbolAddress;
}

void SharedLibraryHelper::unload()
{
	if ( handle == NULL )
		return;
#ifdef WIN32
	FreeLibrary( handle );
#elif MACOSX
	NSUnlinkModule( handle, NSUNLINKMODULE_OPTION_NONE );
#else
	dlclose( handle );
#endif
}
