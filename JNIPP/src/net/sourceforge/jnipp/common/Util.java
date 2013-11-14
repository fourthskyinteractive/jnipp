package net.sourceforge.jnipp.common;

import java.util.HashMap;

public class Util
{
	private static HashMap cppKeywordMap = new HashMap();
	
	static
	{
		cppKeywordMap.put( "asm", "_asm" );
		cppKeywordMap.put( "auto", "_auto" );
		cppKeywordMap.put( "bool", "_bool" );
		cppKeywordMap.put( "break", "_break" );
		cppKeywordMap.put( "case", "_case" );
		cppKeywordMap.put( "catch", "_catch" );
		cppKeywordMap.put( "char", "_char" );
		cppKeywordMap.put( "class", "_class" );
		cppKeywordMap.put( "const", "_const" );
		cppKeywordMap.put( "const_cast", "_const_cast" );
		cppKeywordMap.put( "continue", "_continue" );
		cppKeywordMap.put( "default", "_default" );
		cppKeywordMap.put( "delete", "_delete" );
		cppKeywordMap.put( "do", "_do" );
		cppKeywordMap.put( "double", "_double" );
		cppKeywordMap.put( "dynamic_cast", "_dynamic_cast" );
		cppKeywordMap.put( "else", "_else" );
		cppKeywordMap.put( "enum", "_enum" );
		cppKeywordMap.put( "explicit", "_explicit" );
		cppKeywordMap.put( "export", "_export" );
		cppKeywordMap.put( "extern", "_extern" );
		cppKeywordMap.put( "false", "_false" );
		cppKeywordMap.put( "float", "_float" );
		cppKeywordMap.put( "for", "_for" );
		cppKeywordMap.put( "friend", "_friend" );
		cppKeywordMap.put( "goto", "_goto" );
		cppKeywordMap.put( "if", "_if" );
		cppKeywordMap.put( "inline", "_inline" );
		cppKeywordMap.put( "int", "_int" );
		cppKeywordMap.put( "long", "_long" );
		cppKeywordMap.put( "mutable", "_mutable" );
		cppKeywordMap.put( "namespace", "_namespace" );
		cppKeywordMap.put( "new", "_new" );
		cppKeywordMap.put( "operator", "_operator" );
		cppKeywordMap.put( "private", "_private" );
		cppKeywordMap.put( "protected", "_protected" );
		cppKeywordMap.put( "public", "_public" );
		cppKeywordMap.put( "register", "_register" );
		cppKeywordMap.put( "reinterpret_cast", "_reinterpret_cast" );
		cppKeywordMap.put( "return", "_return" );
		cppKeywordMap.put( "short", "_short" );
		cppKeywordMap.put( "signed", "_signed" );
		cppKeywordMap.put( "sizeof", "_sizeof" );
		cppKeywordMap.put( "static", "_static" );
		cppKeywordMap.put( "static_cast", "_static_cast" );
		cppKeywordMap.put( "struct", "_struct" );
		cppKeywordMap.put( "switch", "_switch" );
		cppKeywordMap.put( "template", "_template" );
		cppKeywordMap.put( "this", "_this" );
		cppKeywordMap.put( "throw", "_throw" );
		cppKeywordMap.put( "true", "_true" );
		cppKeywordMap.put( "try", "_try" );
		cppKeywordMap.put( "typedef", "_typedef" );
		cppKeywordMap.put( "typeid", "_typeid" );
		cppKeywordMap.put( "typename", "_typename" );
		cppKeywordMap.put( "union", "_union" );
		cppKeywordMap.put( "unsigned", "_unsigned" );
		cppKeywordMap.put( "using", "_using" );
		cppKeywordMap.put( "virtual", "_virtual" );
		cppKeywordMap.put( "void", "_void" );
		cppKeywordMap.put( "volatile", "_volatile" );
		cppKeywordMap.put( "wchar_t", "_wchar_t" );
		cppKeywordMap.put( "while", "_while" );
		
		// reserved words
		cppKeywordMap.put( "and", "_and" );
		cppKeywordMap.put( "and_eq", "_and_eq" );
		cppKeywordMap.put( "bitand", "_bitand" );
		cppKeywordMap.put( "compl", "_compl" );
		cppKeywordMap.put( "not", "_not" );
		cppKeywordMap.put( "not_eq", "_not_eq" );
		cppKeywordMap.put( "or", "_or" );
		cppKeywordMap.put( "or_eq", "_or_eq" );
		cppKeywordMap.put( "xor", "_xor" );
		cppKeywordMap.put( "xor_eq", "_xor_eq" );
		
		// others that cause problems
		cppKeywordMap.put( "NULL", "_NULL" );
	}
	
	public static String getCPPIdentifier(String identifier)
	{
		if ( cppKeywordMap.containsKey( identifier ) )
			return (String) cppKeywordMap.get( identifier );
		
		return identifier;
	}
	
	private static HashMap csKeywordMap = new HashMap();
	
	static
	{
		csKeywordMap.put( "asm", "_asm" );
		csKeywordMap.put( "auto", "_auto" );
		csKeywordMap.put( "bool", "_bool" );
		csKeywordMap.put( "break", "_break" );
		csKeywordMap.put( "case", "_case" );
		csKeywordMap.put( "catch", "_catch" );
		csKeywordMap.put( "char", "_char" );
		csKeywordMap.put( "class", "_class" );
		csKeywordMap.put( "const", "_const" );
		csKeywordMap.put( "const_cast", "_const_cast" );
		csKeywordMap.put( "continue", "_continue" );
		csKeywordMap.put( "default", "_default" );
		csKeywordMap.put( "delete", "_delete" );
		csKeywordMap.put( "do", "_do" );
		csKeywordMap.put( "double", "_double" );
		csKeywordMap.put( "dynamic_cast", "_dynamic_cast" );
		csKeywordMap.put( "else", "_else" );
		csKeywordMap.put( "enum", "_enum" );
		csKeywordMap.put( "explicit", "_explicit" );
		csKeywordMap.put( "export", "_export" );
		csKeywordMap.put( "extern", "_extern" );
		csKeywordMap.put( "false", "_false" );
		csKeywordMap.put( "float", "_float" );
		csKeywordMap.put( "for", "_for" );
		csKeywordMap.put( "friend", "_friend" );
		csKeywordMap.put( "goto", "_goto" );
		csKeywordMap.put( "if", "_if" );
		csKeywordMap.put( "inline", "_inline" );
		csKeywordMap.put( "int", "_int" );
		csKeywordMap.put( "long", "_long" );
		csKeywordMap.put( "mutable", "_mutable" );
		csKeywordMap.put( "namespace", "_namespace" );
		csKeywordMap.put( "new", "_new" );
		csKeywordMap.put( "operator", "_operator" );
		csKeywordMap.put( "private", "_private" );
		csKeywordMap.put( "protected", "_protected" );
		csKeywordMap.put( "public", "_public" );
		csKeywordMap.put( "register", "_register" );
		csKeywordMap.put( "reinterpret_cast", "_reinterpret_cast" );
		csKeywordMap.put( "return", "_return" );
		csKeywordMap.put( "short", "_short" );
		csKeywordMap.put( "signed", "_signed" );
		csKeywordMap.put( "sizeof", "_sizeof" );
		csKeywordMap.put( "static", "_static" );
		csKeywordMap.put( "static_cast", "_static_cast" );
		csKeywordMap.put( "struct", "_struct" );
		csKeywordMap.put( "switch", "_switch" );
		csKeywordMap.put( "template", "_template" );
		csKeywordMap.put( "this", "_this" );
		csKeywordMap.put( "throw", "_throw" );
		csKeywordMap.put( "true", "_true" );
		csKeywordMap.put( "try", "_try" );
		csKeywordMap.put( "typedef", "_typedef" );
		csKeywordMap.put( "typeid", "_typeid" );
		csKeywordMap.put( "typename", "_typename" );
		csKeywordMap.put( "union", "_union" );
		csKeywordMap.put( "unsigned", "_unsigned" );
		csKeywordMap.put( "using", "_using" );
		csKeywordMap.put( "virtual", "_virtual" );
		csKeywordMap.put( "void", "_void" );
		csKeywordMap.put( "volatile", "_volatile" );
		csKeywordMap.put( "wchar_t", "_wchar_t" );
		csKeywordMap.put( "while", "_while" );
		
		// reserved words
		csKeywordMap.put( "and", "_and" );
		csKeywordMap.put( "and_eq", "_and_eq" );
		csKeywordMap.put( "bitand", "_bitand" );
		csKeywordMap.put( "compl", "_compl" );
		csKeywordMap.put( "not", "_not" );
		csKeywordMap.put( "not_eq", "_not_eq" );
		csKeywordMap.put( "or", "_or" );
		csKeywordMap.put( "or_eq", "_or_eq" );
		csKeywordMap.put( "xor", "_xor" );
		csKeywordMap.put( "xor_eq", "_xor_eq" );
		
		// others that cause problems
		csKeywordMap.put( "NULL", "_NULL" );
	}
	
	public static String getCSIdentifier(String identifier)
	{
		if ( csKeywordMap.containsKey( identifier ) )
			return (String) csKeywordMap.get( identifier );
		
		return identifier;
	}
}
