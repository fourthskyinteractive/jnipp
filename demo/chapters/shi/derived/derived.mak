!IF "$(CFG)" != "Release" && "$(CFG)" != "Debug"
!MESSAGE You must specify a configuration by defining the macro CFG on the command line. For example:
!MESSAGE
!MESSAGE NMAKE /f derived.mak CFG="Debug"
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

TARGETTYPE=DLL
SOURCES= .\demo\chapters\shi\derived\DerivedDemoPeer.cpp .\demo\chapters\shi\derived\DerivedDemoMapping.cpp
SOURCES=$(SOURCES) .\demo\chapters\shi\derived\DerivedDemoPeerImpl.cpp

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

TARGETNAME=DerivedDemo_d

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

TARGETNAME=DerivedDemo

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
