INCLUDES= -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/$(OS)
DBGCPPFLAGS= -O0 -g
RELCPPFLAGS= -O3
DBGLIBS= -lstdc++ -ldl -lpthread
RELLIBS= -lstdc++ -ldl -lpthread
ifeq ($(OS), linux)
	SHLIBCMD=-shared
else
	SHLIBCMD=-G
endif

Debug/%.o:	%.cpp
		g++ $(DBGFLAGS) -fPIC -c -Wall $< -o $@

Release/%.o:	%.cpp
		g++ $(RELFLAGS) -fPIC -c -Wall $< -o $@

SRCS=$(wildcard *.cpp)
DBGOBJS=$(patsubst %.cpp, Debug/%.o, $(SRCS))
RELOBJS=$(patsubst %.cpp, Release/%.o, $(SRCS))

all:		Debug Release

Debug:		dirs $(DBGOBJS)
		g++ $(SHLIBCMD) -o Debug/libJNIPPCore_d.so $(DBGOBJS) $(DBGLIBS)

Release:	dirs $(RELOBJS)
		g++ $(SHLIBCMD) -o Release/libJNIPPCore.so $(RELOBJS) $(RELLIBS)

dirs:		$(dummy)
		@mkdir -p Debug
		@mkdir -p Release

clean:		$(dummy)
		@rm -rf Debug
		@rm -fr Release

rebuild:	clean dirs Debug Release
