INCLUDES= -I. -I$(JNIPP_HOME)/include -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/$(OS)
TARGETTYPE= consoleapp
TARGETNAME= simpleDemo

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

SRCS=  .\java/io/InputStream.cpp .\java/net/Proxy.cpp .\java/lang/reflect/Field.cpp .\java/net/URLStreamHandler.cpp .\java/lang/reflect/GenericDeclaration.cpp .\java/security/CodeSource.cpp .\java/security/cert/Certificate.cpp .\java/lang/Class.cpp .\java/security/Timestamp.cpp .\java/io/Serializable.cpp .\java/lang/Object.cpp .\java/net/Proxy_Type.cpp .\java/util/List.cpp .\java/net/URLStreamHandlerFactory.cpp .\java/lang/Package.cpp .\java/lang/Integer.cpp .\java/security/Permission.cpp .\java/security/PublicKey.cpp .\java/lang/Number.cpp .\java/lang/annotation/Annotation.cpp .\java/lang/reflect/TypeVariable.cpp .\java/lang/reflect/Type.cpp .\java/util/Iterator.cpp .\java/lang/Iterable.cpp .\java/security/Principal.cpp .\java/net/ContentHandlerFactory.cpp .\java/security/PermissionCollection.cpp .\java/lang/reflect/AccessibleObject.cpp .\java/util/Enumeration.cpp .\java/net/ContentHandler.cpp .\java/lang/reflect/Constructor.cpp .\java/security/CodeSigner.cpp .\java/util/Map.cpp .\java/net/URLConnection.cpp .\java/lang/reflect/Method.cpp .\java/security/Key.cpp .\java/util/ListIterator.cpp .\java/security/cert/CertPath.cpp .\java/util/Set.cpp .\java/util/Collection.cpp .\java/net/FileNameMap.cpp .\java/net/SocketAddress.cpp .\java/net/URI.cpp .\java/util/Date.cpp .\java/lang/ClassLoader.cpp .\demo/chapters/san/simple/SimpleDemo.cpp .\java/net/URL.cpp .\java/security/ProtectionDomain.cpp .\java/io/OutputStream.cpp .\java/lang/Enum.cpp

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
