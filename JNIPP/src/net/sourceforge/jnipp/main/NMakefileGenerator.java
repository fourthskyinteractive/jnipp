package net.sourceforge.jnipp.main;

import net.sourceforge.jnipp.common.FormattedFileWriter;
import java.io.File;
import net.sourceforge.jnipp.project.NMakefileSettings;
import java.util.Collection;
import net.sourceforge.jnipp.common.ClassNode;
import java.util.Iterator;

/**
 * NMAKE makefile generator.
 *
 * This class is utilized to generate an NMAKE makefile for a target
 * <code>Project</code>.
 *
 * @author $Author: ptrewhella $
 * @version $Revision: 1.16 $
 */

public class NMakefileGenerator 
{
   /**
    * Generate makefile for target <code>Project</code>.
    *
    * This method is the public entry point called by the <code>Main</code> class
    * to invoke the code generator.  It will utilize the settings specified in
    * the <code>NMakefileSettings</code> parameter to control the output.
    *
    * @param nMakefileSettings The settings controlling the code generation.
    * @param peerGenDependencies Collection of <code>DependencyData</code> instances
    * for which code was generated by the C++ Peer Generator.
    * @param proxyGenDependencies Collection of <code>DependencyData</code> instances
    * for which code was generated by the C++ Proxy Generator.
    * @exception java.io.IOException
    * @see net.sourceforge.jnipp.main.Main
    * @see net.sourceforge.jnipp.common.ClassNode
    */
   public static void generate(NMakefileSettings nMakefileSettings, Collection peerGenDependencies, Collection proxyGenDependencies)
      throws java.io.IOException
   {
		System.out.println( "generating NMakefile for project " + nMakefileSettings.getProject().getName() + " ..." );
      FormattedFileWriter writer = new FormattedFileWriter( nMakefileSettings.getProject().getCPPOutputDir() + File.separator + nMakefileSettings.getName() );

      writer.outputLine( "!IF \"$(CFG)\" != \"Release\" && \"$(CFG)\" != \"Debug\"" );
      writer.outputLine( "!MESSAGE You must specify a configuration by defining the macro CFG on the command line. For example:" );
      writer.outputLine( "!MESSAGE" );
      writer.output( "!MESSAGE NMAKE /f " );
      writer.outputLine( nMakefileSettings.getName() + " CFG=\"Debug\"" );
      writer.outputLine( "!MESSAGE" );
      writer.outputLine( "!MESSAGE Possible choices for configuration are:" );
      writer.outputLine( "!MESSAGE" );
      writer.outputLine( "!MESSAGE \"Release\"" );
      writer.outputLine( "!MESSAGE \"Debug\"" );
      writer.outputLine( "!MESSAGE" );
      writer.outputLine( "!ERROR An invalid configuration is specified." );
      writer.outputLine( "!ENDIF" );
      writer.outputLine( "" );
      writer.outputLine( "!IF \"$(OS)\" == \"Windows_NT\"" );
      writer.outputLine( "NULL=" );
      writer.outputLine( "!ELSE" );
      writer.outputLine( "NULL=nul" );
      writer.outputLine( "!ENDIF" );
      writer.outputLine( "" );

      writer.output( "TARGETTYPE=" );
      writer.outputLine( nMakefileSettings.getProject().getTargetType().equals( "exe" ) == true ? "CONSOLEAPP" : "DLL" );

      writer.output( "SOURCES=" );
      Iterator depIter = peerGenDependencies.iterator();
      while ( depIter.hasNext() == true )
      {
         DependencyData current = (DependencyData) depIter.next();
         writer.output( " " + nMakefileSettings.getProject().getCPPOutputDir() + File.separator + current.getFullCPPFileName() );
      }
      depIter = proxyGenDependencies.iterator();
      while ( depIter.hasNext() == true )
      {
         DependencyData current = (DependencyData) depIter.next();
         writer.output( " " + nMakefileSettings.getProject().getCPPOutputDir() + File.separator + current.getFullCPPFileName() );
      }
      writer.newLine( 1 );
      writer.outputLine( "OBJS=$(SOURCES:.cpp=.obj)" );
      writer.outputLine( "" );
      writer.outputLine( "CPP=cl.exe" );
      writer.outputLine( "INC=/I .\\ /I \"$(JAVA_HOME)/include\" /I \"$(JAVA_HOME)/include/win32\" /I \"$(JNIPP_HOME)/include\"" );
		if ( nMakefileSettings.getProject().getUsePartialSpec() == false )
      	writer.outputLine( "CPPFLAGS=/nologo /GX /W3 /FD /D \"WIN32\" /D \"_WINDOWS\" /D \"_MBCS\" /D \"__NoPartialSpec\" $(INC) /c" );
		else
      	writer.outputLine( "CPPFLAGS=/nologo /GX /W3 /FD /D \"WIN32\" /D \"_WINDOWS\" /D \"_MBCS\" $(INC) /c" );
		
      writer.outputLine( "LINK=link.exe" );
      writer.outputLine( "LINKFLAGS=/nologo /machine:IX86 /libpath:\"$(JNIPP_HOME)/lib\"" );
      writer.outputLine( "" );
      writer.outputLine( "!IF \"$(TARGETTYPE)\" == \"DLL\"" );
      writer.outputLine( "CPPFLAGS=$(CPPFLAGS) /D \"_USRDLL\"" );
      writer.outputLine( "EXT=\"dll\"" );
      writer.outputLine( "LINKFLAGS=$(LINKFLAGS) /incremental:no /dll" );
      writer.outputLine( "!ELSEIF \"$(TARGETTYPE)\" == \"CONSOLEAPP\"" );
      writer.outputLine( "EXT=\"exe\"" );
      writer.outputLine( "LINKFLAGS=$(LINKFLAGS) /subsystem:console /incremental:no" );
      writer.outputLine( "!ENDIF" );
      writer.outputLine( "" );
      writer.outputLine( "!IF \"$(CFG)\" == \"Debug\"" );
      writer.outputLine( "" );
      writer.outputLine( "CPPFLAGS=$(CPPFLAGS) /MDd /Zi /Od /D \"_DEBUG\"" );
      writer.outputLine( "OUTDIR=Debug" );
      writer.outputLine( "LINKFLAGS=$(LINKFLAGS) /debug" );
      writer.outputLine( "LINKOBJS=$(OBJS:.obj=_d.obj)" );
      writer.output( "LINKLIBS=libJNIPPCore_d.lib" );
      writer.newLine( 1 );
      writer.outputLine( "TARGETNAME=" + nMakefileSettings.getProject().getTargetName() + "_d" );
      writer.outputLine( "" );
      writer.outputLine( "\"$(OUTDIR)\":" );
      writer.incTabLevel();
      writer.outputLine( "if not exist \"$(OUTDIR)\\$(NULL)\" mkdir \"$(OUTDIR)\"" );
      writer.decTabLevel();
      writer.outputLine( "" );
      writer.outputLine( ".cpp.obj:" );
      writer.incTabLevel();
      writer.outputLine( "$(CPP) @<< $(CPPFLAGS) /Fo$(<:.cpp=_d.obj) $<" );
      writer.decTabLevel();
      writer.outputLine( "<<" );
      writer.outputLine( "" );
      writer.outputLine( "!ELSE" );
      writer.outputLine( "" );
      writer.outputLine( "CPPFLAGS=$(CPPFLAGS) /MD /O2 /D \"NDEBUG\"" );
      writer.outputLine( "OUTDIR=Release" );
      writer.outputLine( "LINKOBJS=$(OBJS)" );
      writer.output( "LINKLIBS=libJNIPPCore.lib" );
      writer.newLine( 1 );
      writer.outputLine( "TARGETNAME=" + nMakefileSettings.getProject().getTargetName() );
      writer.outputLine( "" );
      writer.outputLine( "\"$(OUTDIR)\":" );
      writer.incTabLevel();
      writer.outputLine( "if not exist \"$(OUTDIR)\\$(NULL)\" mkdir \"$(OUTDIR)\"" );
      writer.decTabLevel();
      writer.outputLine( "" );
      writer.outputLine( ".cpp.obj:" );
      writer.incTabLevel();
      writer.outputLine( "$(CPP) @<< $(CPPFLAGS) /Fo$(<:.cpp=.obj) $<" );
      writer.decTabLevel();
      writer.outputLine( "<<" );
      writer.outputLine( "" );
      writer.outputLine( "!ENDIF" );
      writer.outputLine( "" );
      writer.outputLine( "\"$(OUTDIR)\\$(TARGETNAME).$(EXT)\" : \"$(OUTDIR)\" $(OBJS)" );
      writer.incTabLevel();
      writer.outputLine( "$(LINK) @<< $(LINKFLAGS) /out:\"$(OUTDIR)\\$(TARGETNAME).$(EXT)\" $(LINKOBJS) $(LINKLIBS)" );
      writer.decTabLevel();
      writer.outputLine( "<<" );
      writer.outputLine( "" );
      writer.outputLine( "all : \"$(OUTDIR)\\$(TARGETNAME).$(EXT)\"" );
      writer.outputLine( "" );
      writer.outputLine( "clean::" );
      writer.incTabLevel();
      writer.outputLine( "-@del $(LINKOBJS:/=\\)" );
      writer.outputLine( "-@rmdir /s /q $(OUTDIR)" );
      writer.decTabLevel();
      writer.outputLine( "" );
      writer.outputLine( "rebuild : clean all" );

      writer.flush();
      writer.close();
   }
}
