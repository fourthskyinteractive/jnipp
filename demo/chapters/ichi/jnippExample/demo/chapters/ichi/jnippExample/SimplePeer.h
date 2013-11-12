#ifndef __demo_chapters_ichi_jnippExample_SimplePeer_H
#define __demo_chapters_ichi_jnippExample_SimplePeer_H


#include <jni.h>
#include "../../../../../LegacyImpl.h"

namespace demo
{
	namespace chapters
	{
		namespace ichi
		{
			namespace jnippExample
			{
				class SimplePeer
				{
				private:
					LegacyImpl delegate;

				public:
					SimplePeer();

					// methods
					jstring getData(JNIEnv*, jobject);
					void loadByID(JNIEnv*, jobject, jstring);

				};
			};
		};
	};
};


#endif
