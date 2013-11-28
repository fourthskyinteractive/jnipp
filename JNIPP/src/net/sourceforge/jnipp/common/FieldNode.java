package net.sourceforge.jnipp.common;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author Philllip E. Trewhella
 * @version 1.0
 */

public class FieldNode
{
	private String name = "";
	private String jniGetFieldCall = null;
	private String jniSetFieldCall = null;
	private ClassNode type = null;
	private boolean staticField = false;
	private String value = null;
	

	public FieldNode(Field field)
		throws ClassNotFoundException
	{
		name = field.getName();
		type = ClassNode.getClassNode( field.getType().getName() );
		staticField = Modifier.isStatic( field.getModifiers() );
		
		// If field is static and final, store value
		try {
			if ( staticField && Modifier.isFinal( field.getModifiers()) ) {				
				if ( field.getType() == int.class ) {
					value = String.valueOf(field.getInt(null));				
							
				} else if ( field.getType() == String.class ) {
					value = field.get(null).toString();
					
				} else if ( field.getType() == long.class ) {
					value = String.valueOf(field.getLong(null));
					
				} else if ( field.getType() == short.class ) {
					value = String.valueOf(field.getShort(null));
					
				} else if ( field.getType() == byte.class ) {
					value = String.valueOf(field.getByte(null));
					
				} else if ( field.getType() == float.class ) {
					value = String.valueOf(field.getFloat(null));
					
				} else if ( field.getType() == double.class ) {
					value = String.valueOf(field.getDouble(null));
					
				} else if ( field.getType() == boolean.class ) {
					value = String.valueOf(field.getBoolean(null));
					
				} else if ( field.getType() == char.class ) {
					value = String.valueOf(field.getChar(null));
					
				}
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		buildJNIGetFieldCall();
		buildJNISetFieldCall();
	}
	
	private void buildJNIGetFieldCall()
	{
		if ( type.isPrimitive() == false )
		{
			jniGetFieldCall = "JNIEnvHelper::Get";
			jniGetFieldCall += (staticField == true ? "StaticObjectField(" : "ObjectField(");
		}
		else
		{
			jniGetFieldCall = "JNIEnvHelper::Get";
			if ( staticField == true )
				jniGetFieldCall += "Static";
			jniGetFieldCall += Character.toUpperCase( type.getClassName().charAt( 0 ) ) + type.getClassName().substring( 1 );
			jniGetFieldCall += "Field(";
		}
	}

	private void buildJNISetFieldCall()
	{
		if ( type.isPrimitive() == false )
		{
			jniSetFieldCall = "JNIEnvHelper::Set";
			jniSetFieldCall += (staticField == true ? "StaticObjectField(" : "ObjectField(");
		}
		else
		{
			jniSetFieldCall = "JNIEnvHelper::Set";
			if ( staticField == true )
				jniSetFieldCall += "Static";
			jniSetFieldCall += Character.toUpperCase( type.getClassName().charAt( 0 ) ) + type.getClassName().substring( 1 );
			jniSetFieldCall += "Field(";
		}
	}

	public String getName()
	{
		return name;
	}

	public String getCPPName()
	{
		return Util.getCPPIdentifier( name );
	}
	
	public ClassNode getType()
	{
		return type;
	}

	public String getJNIGetFieldCall()
	{
		return jniGetFieldCall;
	}

	public String getJNISetFieldCall()
	{
		return jniSetFieldCall;
	}
	
	public boolean isStatic()
	{
		return staticField;
	}

	public boolean isConstant() {
		return isStatic() && value != null;
	}

	public String getValue() {
		return value;
	}
}
