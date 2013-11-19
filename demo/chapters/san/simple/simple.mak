!IF "$(CFG)" != "Release" && "$(CFG)" != "Debug"
!MESSAGE You must specify a configuration by defining the macro CFG on the command line. For example:
!MESSAGE
!MESSAGE NMAKE /f simple.mak CFG="Debug"
!MESSAGE
!MESSAGE Possible choices for configuration are:
!MESSAGE
!MESSAGE "Release"
!MESSAGE "Debug"
!MESSAGE
!ERROR An invalid configuration is specified.
!ENDIF

!IF "$(OS)" == "Windows_NT"
NULL=
!ELSE
NULL=nul
!ENDIF

TARGETTYPE=CONSOLEAPP
SOURCES= .\java/io/InputStream.cpp .\java/net/Proxy.cpp .\java/lang/reflect/Field.cpp .\java/net/URLStreamHandler.cpp .\java/lang/reflect/GenericDeclaration.cpp .\java/security/CodeSource.cpp .\java/security/cert/Certificate.cpp .\java/lang/Class.cpp .\java/security/Timestamp.cpp .\java/io/Serializable.cpp .\java/lang/Object.cpp .\java/net/Proxy_Type.cpp .\java/util/List.cpp .\java/net/URLStreamHandlerFactory.cpp .\java/lang/Package.cpp .\java/lang/Integer.cpp .\java/security/Permission.cpp .\java/security/PublicKey.cpp .\java/lang/Number.cpp .\java/lang/annotation/Annotation.cpp .\java/lang/reflect/TypeVariable.cpp .\java/lang/reflect/Type.cpp .\java/util/Iterator.cpp .\java/lang/Iterable.cpp .\java/security/Principal.cpp .\java/net/ContentHandlerFactory.cpp .\java/security/PermissionCollection.cpp .\java/lang/reflect/AccessibleObject.cpp .\java/util/Enumeration.cpp .\java/net/ContentHandler.cpp .\java/lang/reflect/Constructor.cpp .\java/security/CodeSigner.cpp .\java/util/Map.cpp .\java/net/URLConnection.cpp .\java/lang/reflect/Method.cpp .\java/security/Key.cpp .\java/util/ListIterator.cpp .\java/security/cert/CertPath.cpp .\java/util/Set.cpp .\java/util/Collection.cpp .\java/net/FileNameMap.cpp .\java/net/SocketAddress.cpp .\java/net/URI.cpp .\java/util/Date.cpp .\java/lang/ClassLoader.cpp .\demo/chapters/san/simple/SimpleDemo.cpp .\java/net/URL.cpp .\java/security/ProtectionDomain.cpp .\java/io/OutputStream.cpp .\java/lang/Enum.cpp

OBJS=$(SOURCES:.cpp=.obj)

CPP=cl.exe
INC=/I .\ /I "$(JAVA_HOME)/include" /I "$(JAVA_HOME)/include/win32" /I "$(JNIPP_HOME)/include"
CPPFLAGS=/nologo /GX /W3 /FD /D "WIN32" /D "_WINDOWS" /D "_MBCS" $(INC) /c
LINK=link.exe
LINKFLAGS=/nologo /machine:IX86 /libpath:"$(JNIPP_HOME)/lib"

!IF "$(TARGETTYPE)" == "DLL"
CPPFLAGS=$(CPPFLAGS) /D "_USRDLL"
EXT="dll"
LINKFLAGS=$(LINKFLAGS) /incremental:no /dll
!ELSEIF "$(TARGETTYPE)" == "CONSOLEAPP"
EXT="exe"
LINKFLAGS=$(LINKFLAGS) /subsystem:console /incremental:no
!ENDIF

!IF "$(CFG)" == "Debug"

CPPFLAGS=$(CPPFLAGS) /MDd /Zi /Od /D "_DEBUG"
OUTDIR=Debug
LINKFLAGS=$(LINKFLAGS) /debug
LINKOBJS=$(OBJS:.obj=_d.obj)
LINKLIBS=libJNIPPCore_d.lib

TARGETNAME=simpleDemo_d

"$(OUTDIR)":
	if not exist "$(OUTDIR)\$(NULL)" mkdir "$(OUTDIR)"

.cpp.obj:
	$(CPP) @<< $(CPPFLAGS) /Fo$(<:.cpp=_d.obj) $<
<<

!ELSE

CPPFLAGS=$(CPPFLAGS) /MD /O2 /D "NDEBUG"
OUTDIR=Release
LINKOBJS=$(OBJS)
LINKLIBS=libJNIPPCore.lib

TARGETNAME=simpleDemo

"$(OUTDIR)":
	if not exist "$(OUTDIR)\$(NULL)" mkdir "$(OUTDIR)"

.cpp.obj:
	$(CPP) @<< $(CPPFLAGS) /Fo$(<:.cpp=.obj) $<
<<

!ENDIF

"$(OUTDIR)\$(TARGETNAME).$(EXT)" : "$(OUTDIR)" $(OBJS)
	$(LINK) @<< $(LINKFLAGS) /out:"$(OUTDIR)\$(TARGETNAME).$(EXT)" $(LINKOBJS) $(LINKLIBS)
<<

all : "$(OUTDIR)\$(TARGETNAME).$(EXT)"

clean::
	-@del $(LINKOBJS:/=\)
	-@rmdir /s /q $(OUTDIR)

rebuild : clean all
