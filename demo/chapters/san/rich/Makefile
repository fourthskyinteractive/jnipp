INCLUDES= -I. -I$(JNIPP_HOME)/include -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/$(OS)
TARGETTYPE= consoleapp
TARGETNAME= richDemo

ifeq ($(TARGETTYPE), shlib)
DBGCPPFLAGS= -O0 -g -fPIC -c -Wall $(INCLUDES)
RELCPPFLAGS= -O0 -fPIC -c -Wall $(INCLUDES)
else
DBGCPPFLAGS= -O0 -g -c -Wall $(INCLUDES)
RELCPPFLAGS= -O0 -c -Wall $(INCLUDES)
endif

LIBPATH= -L$(JNIPP_HOME)/lib
DBGLIBS= $(LIBPATH) -lJNIPPCore_d
RELLIBS= $(LIBPATH) -lJNIPPCore

LINKFLAGS=
EXT=
ifeq ($(TARGETTYPE), shlib)
ifeq ($(OS), linux)
LINKFLAGS=-shared
EXT=.so
else
SHLIBCMD=-G
EXT=.so
endif
endif

%_d.o:	%.cpp
			g++ $(DBGCPPFLAGS) $< -o $@

%.o:	%.cpp
			g++ $(RELCPPFLAGS) $< -o $@

SRCS=  ./java/net/ContentHandlerProxy.cpp ./java/util/Hashtable_EmptyIteratorProxy.cpp ./java/util/ComparatorProxy.cpp ./java/net/ContentHandlerFactoryProxy.cpp ./demo/chapters/san/rich/RichDemoProxy.cpp ./java/util/Hashtable_EntryProxy.cpp ./java/util/StackProxy.cpp ./java/io/ObjectInputStreamProxy.cpp ./java/security/cert/CertificateProxy.cpp ./java/net/URLProxy.cpp ./java/net/URLStreamHandlerFactoryProxy.cpp ./java/io/FileSystemProxy.cpp ./java/io/OutputStreamWriterProxy.cpp ./java/io/DataOutputStreamProxy.cpp ./java/lang/SecurityManagerProxy.cpp ./java/lang/PackageProxy.cpp ./java/io/WriterProxy.cpp ./java/security/AccessControlContextProxy.cpp ./sun/misc/ResourceProxy.cpp ./java/awt/AWTPermissionProxy.cpp ./java/io/ObjectInputValidationProxy.cpp ./java/io/DataInputProxy.cpp ./java/security/ProtectionDomainProxy.cpp ./java/io/FilenameFilterProxy.cpp ./java/lang/ClassLoaderProxy.cpp ./java/lang/reflect/ConstructorProxy.cpp ./java/lang/ObjectProxy.cpp ./java/net/URLConnectionProxy.cpp ./java/security/AllPermissionProxy.cpp ./java/lang/reflect/FieldProxy.cpp ./java/lang/ThreadGroupProxy.cpp ./sun/io/CharToByteConverterProxy.cpp ./sun/misc/URLClassPath_LoaderProxy.cpp ./java/io/ObjectStreamClassProxy.cpp ./java/lang/RunnableProxy.cpp ./java/util/HashMapProxy.cpp ./java/util/HashMap_EmptyHashIteratorProxy.cpp ./java/io/BufferedWriterProxy.cpp ./java/io/DataOutputProxy.cpp ./java/io/FileDescriptorProxy.cpp ./java/io/ObjectOutputStreamProxy.cpp ./java/io/FileProxy.cpp ./java/io/DataInputStreamProxy.cpp ./java/util/Hashtable_EmptyEnumeratorProxy.cpp ./java/security/DomainCombinerProxy.cpp ./java/net/InetAddressImplProxy.cpp ./java/util/HashMap_EntryProxy.cpp ./java/util/jar/Manifest_FastInputStreamProxy.cpp ./java/io/ObjectOutputStream_ReplaceTableProxy.cpp ./java/lang/ThrowableProxy.cpp ./java/lang/Package_1Proxy.cpp ./java/lang/ThreadProxy.cpp ./java/io/OutputStreamProxy.cpp ./java/io/PrintWriterProxy.cpp ./java/util/jar/AttributesProxy.cpp ./java/net/InetAddressProxy.cpp ./java/net/NetPermissionProxy.cpp ./java/io/InputStreamProxy.cpp ./java/util/CollectionProxy.cpp ./java/io/ObjectOutputStream_PutFieldProxy.cpp ./java/util/jar/Attributes_NameProxy.cpp ./java/net/SocketPermissionProxy.cpp ./java/util/jar/ManifestProxy.cpp ./sun/security/util/DebugProxy.cpp ./java/io/IOExceptionProxy.cpp ./java/util/ListIteratorProxy.cpp ./java/security/PublicKeyProxy.cpp ./java/net/URLStreamHandlerProxy.cpp ./java/util/EnumerationProxy.cpp ./java/lang/ClassNotFoundExceptionProxy.cpp ./java/io/ObjectInputStream_GetFieldProxy.cpp ./java/io/ObjectStreamClass_ObjectStreamClassEntryProxy.cpp ./java/util/ArrayListProxy.cpp ./sun/misc/URLClassPathProxy.cpp ./java/lang/ClassProxy.cpp ./java/io/ObjectStreamFieldProxy.cpp ./java/security/PermissionCollectionProxy.cpp ./java/util/VectorProxy.cpp ./java/io/ObjectOutputStream_HandleTableProxy.cpp ./java/lang/StringBufferProxy.cpp ./java/util/MapProxy.cpp ./java/io/ObjectOutputProxy.cpp ./java/security/CodeSourceProxy.cpp ./java/io/ObjectOutputStream_StackProxy.cpp ./java/io/FileFilterProxy.cpp ./java/util/HashtableProxy.cpp ./java/lang/reflect/MethodProxy.cpp ./java/lang/RuntimePermissionProxy.cpp ./java/io/ObjectOutputStream_1Proxy.cpp ./java/util/ListProxy.cpp ./java/util/IteratorProxy.cpp ./java/net/FileNameMapProxy.cpp ./java/io/PrintStreamProxy.cpp ./java/security/PermissionProxy.cpp ./java/util/SetProxy.cpp
SRCS+= Main.cpp

DBGOBJS=$(patsubst %.cpp, %_d.o, $(SRCS))
RELOBJS=$(patsubst %.cpp, %.o, $(SRCS))

all:		Debug Release

Debug:	dirs $(DBGOBJS)
			g++ $(LINKFLAGS) -o Debug/$(TARGETNAME)_d$(EXT) $(DBGOBJS) $(DBGLIBS)

Release:	dirs $(RELOBJS)
			g++ $(LINKFLAGS) -o Release/$(TARGETNAME)$(EXT) $(RELOBJS) $(RELLIBS)

dirs:	$(dummy)
		@mkdir -p Debug
		@mkdir -p Release

clean:	$(dummy)
			@rm -rf Debug
			@rm -rf Release

rebuild:	clean Debug Release
