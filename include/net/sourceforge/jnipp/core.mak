# Microsoft Developer Studio Generated NMAKE File, Based on core.dsp
!IF "$(CFG)" == ""
CFG=core - Win32 Debug
!MESSAGE No configuration specified. Defaulting to core - Win32 Debug.
!ENDIF 

!IF "$(CFG)" != "core - Win32 Release" && "$(CFG)" != "core - Win32 Debug"
!MESSAGE Invalid configuration "$(CFG)" specified.
!MESSAGE You can specify a configuration when running NMAKE
!MESSAGE by defining the macro CFG on the command line. For example:
!MESSAGE 
!MESSAGE NMAKE /f "core.mak" CFG="core - Win32 Debug"
!MESSAGE 
!MESSAGE Possible choices for configuration are:
!MESSAGE 
!MESSAGE "core - Win32 Release" (based on "Win32 (x86) Dynamic-Link Library")
!MESSAGE "core - Win32 Debug" (based on "Win32 (x86) Dynamic-Link Library")
!MESSAGE 
!ERROR An invalid configuration is specified.
!ENDIF 

!IF "$(OS)" == "Windows_NT"
NULL=
!ELSE 
NULL=nul
!ENDIF 

!IF  "$(CFG)" == "core - Win32 Release"

OUTDIR=.\Release
INTDIR=.\Release
# Begin Custom Macros
OutDir=.\Release
# End Custom Macros

ALL : "$(OUTDIR)\libJNIPPCore.dll"


CLEAN :
	-@erase "$(INTDIR)\JNIEnvHelper.obj"
	-@erase "$(INTDIR)\JStringHelper.obj"
	-@erase "$(INTDIR)\JVM.obj"
	-@erase "$(INTDIR)\SharedLibraryHelper.obj"
	-@erase "$(INTDIR)\vc60.idb"
	-@erase "$(OUTDIR)\libJNIPPCore.dll"
	-@erase "$(OUTDIR)\libJNIPPCore.exp"
	-@erase "$(OUTDIR)\libJNIPPCore.lib"

"$(OUTDIR)" :
    if not exist "$(OUTDIR)/$(NULL)" mkdir "$(OUTDIR)"

CPP=cl.exe
CPP_PROJ=/nologo /MD /W3 /GR /GX /O2 /I "../.." /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_MBCS" /D "_USRDLL" /D "__IncludedFromCore" /Fp"$(INTDIR)\core.pch" /YX /Fo"$(INTDIR)\\" /Fd"$(INTDIR)\\" /FD /c 

.c{$(INTDIR)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cpp{$(INTDIR)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cxx{$(INTDIR)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.c{$(INTDIR)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cpp{$(INTDIR)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cxx{$(INTDIR)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

MTL=midl.exe
MTL_PROJ=/nologo /D "NDEBUG" /mktyplib203 /win32 
RSC=rc.exe
BSC32=bscmake.exe
BSC32_FLAGS=/nologo /o"$(OUTDIR)\core.bsc" 
BSC32_SBRS= \
	
LINK32=link.exe
LINK32_FLAGS=advapi32.lib /nologo /dll /incremental:no /pdb:"$(OUTDIR)\libJNIPPCore.pdb" /machine:I386 /out:"$(OUTDIR)\libJNIPPCore.dll" /implib:"$(OUTDIR)\libJNIPPCore.lib" 
LINK32_OBJS= \
	"$(INTDIR)\JNIEnvHelper.obj" \
	"$(INTDIR)\JStringHelper.obj" \
	"$(INTDIR)\JVM.obj" \
	"$(INTDIR)\SharedLibraryHelper.obj"

"$(OUTDIR)\libJNIPPCore.dll" : "$(OUTDIR)" $(DEF_FILE) $(LINK32_OBJS)
    $(LINK32) @<<
  $(LINK32_FLAGS) $(LINK32_OBJS)
<<

!ELSEIF  "$(CFG)" == "core - Win32 Debug"

OUTDIR=.\Debug
INTDIR=.\Debug
# Begin Custom Macros
OutDir=.\Debug
# End Custom Macros

ALL : "$(OUTDIR)\libJNIPPCore_d.dll"


CLEAN :
	-@erase "$(INTDIR)\JNIEnvHelper.obj"
	-@erase "$(INTDIR)\JStringHelper.obj"
	-@erase "$(INTDIR)\JVM.obj"
	-@erase "$(INTDIR)\SharedLibraryHelper.obj"
	-@erase "$(INTDIR)\vc60.idb"
	-@erase "$(INTDIR)\vc60.pdb"
	-@erase "$(OUTDIR)\libJNIPPCore_d.dll"
	-@erase "$(OUTDIR)\libJNIPPCore_d.exp"
	-@erase "$(OUTDIR)\libJNIPPCore_d.ilk"
	-@erase "$(OUTDIR)\libJNIPPCore_d.lib"
	-@erase "$(OUTDIR)\libJNIPPCore_d.pdb"

"$(OUTDIR)" :
    if not exist "$(OUTDIR)/$(NULL)" mkdir "$(OUTDIR)"

CPP=cl.exe
CPP_PROJ=/nologo /MDd /W3 /Gm /GR /GX /ZI /Od /I "../.." /D "WIN32" /D "_DEBUG" /D "_WINDOWS" /D "_MBCS" /D "_USRDLL" /D "__IncludedFromCore" /Fp"$(INTDIR)\core.pch" /YX /Fo"$(INTDIR)\\" /Fd"$(INTDIR)\\" /FD /GZ /c 

.c{$(INTDIR)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cpp{$(INTDIR)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cxx{$(INTDIR)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.c{$(INTDIR)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cpp{$(INTDIR)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cxx{$(INTDIR)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

MTL=midl.exe
MTL_PROJ=/nologo /D "_DEBUG" /mktyplib203 /win32 
RSC=rc.exe
BSC32=bscmake.exe
BSC32_FLAGS=/nologo /o"$(OUTDIR)\core.bsc" 
BSC32_SBRS= \
	
LINK32=link.exe
LINK32_FLAGS=advapi32.lib /nologo /dll /incremental:yes /pdb:"$(OUTDIR)\libJNIPPCore_d.pdb" /debug /machine:I386 /out:"$(OUTDIR)\libJNIPPCore_d.dll" /implib:"$(OUTDIR)\libJNIPPCore_d.lib" /pdbtype:sept 
LINK32_OBJS= \
	"$(INTDIR)\JNIEnvHelper.obj" \
	"$(INTDIR)\JStringHelper.obj" \
	"$(INTDIR)\JVM.obj" \
	"$(INTDIR)\SharedLibraryHelper.obj"

"$(OUTDIR)\libJNIPPCore_d.dll" : "$(OUTDIR)" $(DEF_FILE) $(LINK32_OBJS)
    $(LINK32) @<<
  $(LINK32_FLAGS) $(LINK32_OBJS)
<<

!ENDIF 


!IF "$(NO_EXTERNAL_DEPS)" != "1"
!IF EXISTS("core.dep")
!INCLUDE "core.dep"
!ELSE 
!MESSAGE Warning: cannot find "core.dep"
!ENDIF 
!ENDIF 


!IF "$(CFG)" == "core - Win32 Release" || "$(CFG)" == "core - Win32 Debug"

SOURCE=.\JNIEnvHelper.cpp

"$(INTDIR)\JNIEnvHelper.obj" : $(SOURCE) "$(INTDIR)"


SOURCE=.\JStringHelper.cpp

"$(INTDIR)\JStringHelper.obj" : $(SOURCE) "$(INTDIR)"


SOURCE=.\JVM.cpp

"$(INTDIR)\JVM.obj" : $(SOURCE) "$(INTDIR)"


SOURCE=.\SharedLibraryHelper.cpp

"$(INTDIR)\SharedLibraryHelper.obj" : $(SOURCE) "$(INTDIR)"



!ENDIF 

