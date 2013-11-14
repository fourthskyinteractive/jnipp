package net.sourceforge.jnipp.main;


/**
 * Helper class for makefile generators.
 *
 *
 * @author $Author: ptrewhella $
 * @version $Revision: 1.2 $
 */

public class DependencyData 
{
   private String path = null;
   private String headerFileName = null;
   private String implFileName = null;
   
   public DependencyData(String path, String headerFileName, String implFileName)
   {
      this.path = path;
      this.headerFileName = headerFileName;
      this.implFileName = implFileName;
   }
   
   public String getPath()
   {
      return path;
   }
   
   public String getHeaderFileName()
   {
      return headerFileName;
   }
   
   public String getImplFileName()
   {
      return implFileName;
   }
   
   public String getFullHeaderFileName()
   {
      if ( path == null || path.equals( "" ) == true )
         return headerFileName;
      return path + /*File.separatorChar*/ '/' + headerFileName;
   }
   
   public String getFullImplFileName()
   {
      if ( path == null || path.equals( "" ) == true )
         return implFileName;
      return path + /*File.separatorChar*/ '/' + implFileName;
   }
}
