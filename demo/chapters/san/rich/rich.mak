!IF "$(CFG)" != "Release" && "$(CFG)" != "Debug"
!MESSAGE You must specify a configuration by defining the macro CFG on the command line. For example:
!MESSAGE
!MESSAGE NMAKE /f rich.mak CFG="Debug"
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
SOURCES= .\java\net\ContentHandlerProxy.cpp .\java\util\Hashtable_EmptyIteratorProxy.cpp .\java\util\ComparatorProxy.cpp .\java\net\ContentHandlerFactoryProxy.cpp .\demo\chapters\san\rich\RichDemoProxy.cpp .\java\util\Hashtable_EntryProxy.cpp .\java\util\StackProxy.cpp .\java\io\ObjectInputStreamProxy.cpp .\java\security\cert\CertificateProxy.cpp .\java\net\URLProxy.cpp .\java\net\URLStreamHandlerFactoryProxy.cpp .\java\io\FileSystemProxy.cpp .\java\io\OutputStreamWriterProxy.cpp .\java\io\DataOutputStreamProxy.cpp .\java\lang\SecurityManagerProxy.cpp .\java\lang\PackageProxy.cpp .\java\io\WriterProxy.cpp .\java\security\AccessControlContextProxy.cpp .\sun\misc\ResourceProxy.cpp .\java\awt\AWTPermissionProxy.cpp .\java\io\ObjectInputValidationProxy.cpp .\java\io\DataInputProxy.cpp .\java\security\ProtectionDomainProxy.cpp .\java\io\FilenameFilterProxy.cpp .\java\lang\ClassLoaderProxy.cpp .\java\lang\reflect\ConstructorProxy.cpp .\java\lang\ObjectProxy.cpp .\java\net\URLConnectionProxy.cpp .\java\security\AllPermissionProxy.cpp .\java\lang\reflect\FieldProxy.cpp .\java\lang\ThreadGroupProxy.cpp .\sun\io\CharToByteConverterProxy.cpp .\sun\misc\URLClassPath_LoaderProxy.cpp .\java\io\ObjectStreamClassProxy.cpp .\java\lang\RunnableProxy.cpp .\java\util\HashMapProxy.cpp .\java\util\HashMap_EmptyHashIteratorProxy.cpp .\java\io\BufferedWriterProxy.cpp .\java\io\DataOutputProxy.cpp .\java\io\FileDescriptorProxy.cpp .\java\io\ObjectOutputStreamProxy.cpp .\java\io\FileProxy.cpp .\java\io\DataInputStreamProxy.cpp .\java\util\Hashtable_EmptyEnumeratorProxy.cpp .\java\security\DomainCombinerProxy.cpp .\java\net\InetAddressImplProxy.cpp .\java\util\HashMap_EntryProxy.cpp .\java\util\jar\Manifest_FastInputStreamProxy.cpp .\java\io\ObjectOutputStream_ReplaceTableProxy.cpp .\java\lang\ThrowableProxy.cpp .\java\lang\Package_1Proxy.cpp .\java\lang\ThreadProxy.cpp .\java\io\OutputStreamProxy.cpp .\java\io\PrintWriterProxy.cpp .\java\util\jar\AttributesProxy.cpp .\java\net\InetAddressProxy.cpp .\java\net\NetPermissionProxy.cpp .\java\io\InputStreamProxy.cpp .\java\util\CollectionProxy.cpp .\java\io\ObjectOutputStream_PutFieldProxy.cpp .\java\util\jar\Attributes_NameProxy.cpp .\java\net\SocketPermissionProxy.cpp .\java\util\jar\ManifestProxy.cpp .\sun\security\util\DebugProxy.cpp .\java\io\IOExceptionProxy.cpp .\java\util\ListIteratorProxy.cpp .\java\security\PublicKeyProxy.cpp .\java\net\URLStreamHandlerProxy.cpp .\java\util\EnumerationProxy.cpp .\java\lang\ClassNotFoundExceptionProxy.cpp .\java\io\ObjectInputStream_GetFieldProxy.cpp .\java\io\ObjectStreamClass_ObjectStreamClassEntryProxy.cpp .\java\util\ArrayListProxy.cpp .\sun\misc\URLClassPathProxy.cpp .\java\lang\ClassProxy.cpp .\java\io\ObjectStreamFieldProxy.cpp .\java\security\PermissionCollectionProxy.cpp .\java\util\VectorProxy.cpp .\java\io\ObjectOutputStream_HandleTableProxy.cpp .\java\lang\StringBufferProxy.cpp .\java\util\MapProxy.cpp .\java\io\ObjectOutputProxy.cpp .\java\security\CodeSourceProxy.cpp .\java\io\ObjectOutputStream_StackProxy.cpp .\java\io\FileFilterProxy.cpp .\java\util\HashtableProxy.cpp .\java\lang\reflect\MethodProxy.cpp .\java\lang\RuntimePermissionProxy.cpp .\java\io\ObjectOutputStream_1Proxy.cpp .\java\util\ListProxy.cpp .\java\util\IteratorProxy.cpp .\java\net\FileNameMapProxy.cpp .\java\io\PrintStreamProxy.cpp .\java\security\PermissionProxy.cpp .\java\util\SetProxy.cpp
SOURCES=$(SOURCES) Main.cpp

OBJS=$(SOURCES:.cpp=.obj)

CPP=cl.exe
INC=/I .\ /I "$(JAVA_HOME)/include" /I "$(JAVA_HOME)/include/win32" /I "$(JNIPP_HOME)/include"
CPPFLAGS=/nologo /GX /W3 /FD /D "WIN32" /D "_WINDOWS" /D "_MBCS" /D "__NoPartialSpec" $(INC) /c
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

TARGETNAME=richDemo_d

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

TARGETNAME=richDemo

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
