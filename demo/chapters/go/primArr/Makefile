INCLUDES= -I. -I$(JNIPP_HOME)/include -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/$(OS)
TARGETTYPE= shlib
TARGETNAME= libPrimArrDemo

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

SRCS=  ./demo/chapters/go/primArr/PrimArrDemoPeer.cpp ./demo/chapters/go/primArr/PrimArrDemoMapping.cpp
SRCS+= ./demo/chapters/go/primArr/PrimArrDemoImpl.cpp

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
