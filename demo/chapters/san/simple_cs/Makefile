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

SRCS=  .\InputStream.cs .\Attributes_Name.cs .\Proxy.cs .\Field.cs .\URLStreamHandler.cs .\GenericDeclaration.cs .\CodeSource.cs .\Certificate.cs .\Class.cs .\Timestamp.cs .\Serializable.cs .\Object.cs .\Proxy_Type.cs .\List.cs .\URLStreamHandlerFactory.cs .\Package.cs .\Integer.cs .\Permission.cs .\PublicKey.cs .\Attributes.cs .\Number.cs .\Annotation.cs .\TypeVariable.cs .\Type.cs .\Iterator.cs .\Iterable.cs .\Principal.cs .\ContentHandlerFactory.cs .\Void.cs .\PermissionCollection.cs .\AccessibleObject.cs .\Enumeration.cs .\ContentHandler.cs .\Constructor.cs .\CodeSigner.cs .\Map.cs .\URLConnection.cs .\Package_1.cs .\Method.cs .\Key.cs .\ListIterator.cs .\CertPath.cs .\Set.cs .\Collection.cs .\FileNameMap.cs .\SocketAddress.cs .\URI.cs .\Manifest.cs .\Date.cs .\ClassLoader.cs .\SimpleDemo.cs .\URL.cs .\ProtectionDomain.cs .\OutputStream.cs .\Enum.cs

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
