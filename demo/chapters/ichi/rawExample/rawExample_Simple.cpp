#include "rawExample_Simple.h"
#include "../LegacyImpl.h"
#include <map>

/*
 * Class:     demo_chapters_ichi_rawExample_Simple
 * Method:    init
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_demo_chapters_ichi_rawExample_Simple_init(JNIEnv* env, jobject obj)
{
	jclass cls = env->GetObjectClass( obj );
	jfieldID fid = env->GetFieldID( cls, "handle", "J" );
	LegacyImpl* peer = new LegacyImpl;
	env->SetLongField( obj, fid, reinterpret_cast<jlong>(peer) );
}

/*
 * Class:     demo_chapters_ichi_rawExample_Simple
 * Method:    uninit
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_demo_chapters_ichi_rawExample_Simple_uninit(JNIEnv *env, jobject obj)
{
	jclass cls = env->GetObjectClass( obj );
	jfieldID fid = env->GetFieldID( cls, "handle", "J" );
	jlong handle = env->GetLongField( obj, fid );
	
	LegacyImpl* peer = reinterpret_cast<LegacyImpl*>( handle );
	delete peer;
}

/*
 * Class:     demo_chapters_ichi_rawExample_Simple
 * Method:    loadByID
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_demo_chapters_ichi_rawExample_Simple_loadByID(JNIEnv* env, jobject obj, jstring str)
{
	if ( str == NULL )
	{
		jclass cls = env->FindClass( "java/lang/IllegalArgumentException" );
		env->ThrowNew( cls, "id parameter cannot be NULL" );
		return;
	}
	
	const char* id = env->GetStringUTFChars( str, NULL );
	jclass cls = env->GetObjectClass( obj );
	jfieldID fid = env->GetFieldID( cls, "handle", "J" );
	jlong handle = env->GetLongField( obj, fid );
	
	LegacyImpl* peer = reinterpret_cast<LegacyImpl*>( handle );
	peer->loadByID( id );
	env->ReleaseStringUTFChars( str, id );
}

/*
 * Class:     demo_chapters_ichi_rawExample_Simple
 * Method:    getLegacyData
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_demo_chapters_ichi_rawExample_Simple_getLegacyData(JNIEnv* env, jobject obj)
{
	jclass cls = env->GetObjectClass( obj );
	jfieldID fid = env->GetFieldID( cls, "handle", "J" );
	jlong handle = env->GetLongField( obj, fid );
	
	LegacyImpl* peer = reinterpret_cast<LegacyImpl*>( handle );
		
	return env->NewStringUTF( peer->getData().c_str() );
}

