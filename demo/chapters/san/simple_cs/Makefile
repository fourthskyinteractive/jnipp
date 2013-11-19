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

SRCS=  .\Enumeration.cs .\InputStream.cs .\CodeSigner.cs .\Constructor.cs .\ContentHandler.cs .\URLConnection.cs .\Map.cs .\Proxy.cs .\Method.cs .\Field.cs .\Key.cs .\URLStreamHandler.cs .\GenericDeclaration.cs .\ListIterator.cs .\CodeSource.cs .\Certificate.cs .\CertPath.cs .\Class.cs .\Set.cs .\Timestamp.cs .\Serializable.cs .\Collection.cs .\FileNameMap.cs .\Object.cs .\List.cs .\Proxy_Type.cs .\SocketAddress.cs .\Package.cs .\URLStreamHandlerFactory.cs .\Permission.cs .\URI.cs .\Date.cs .\PublicKey.cs .\ClassLoader.cs .\Annotation.cs .\TypeVariable.cs .\Type.cs .\Iterator.cs .\ProtectionDomain.cs .\URL.cs .\Iterable.cs .\Principal.cs .\OutputStream.cs .\PermissionCollection.cs .\ContentHandlerFactory.cs .\AccessibleObject.cs .\Enum.cs

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
