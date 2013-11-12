#ifndef __demo_proxygen_tutorial_MyCoolClassProxy_H
#define __demo_proxygen_tutorial_MyCoolClassProxy_H


#include <jni.h>
#include <string>

#include "net/sourceforge/jnipp/JBooleanArrayHelper.h"
#include "net/sourceforge/jnipp/JByteArrayHelper.h"
#include "net/sourceforge/jnipp/JCharArrayHelper.h"
#include "net/sourceforge/jnipp/JDoubleArrayHelper.h"
#include "net/sourceforge/jnipp/JFloatArrayHelper.h"
#include "net/sourceforge/jnipp/JIntArrayHelper.h"
#include "net/sourceforge/jnipp/JLongArrayHelper.h"
#include "net/sourceforge/jnipp/JShortArrayHelper.h"
#include "net/sourceforge/jnipp/JStringHelper.h"
#include "net/sourceforge/jnipp/JStringHelperArray.h"
#include "net/sourceforge/jnipp/ProxyArray.h"

// includes for parameter and return type proxy classes

namespace demo
{
	namespace proxygen
	{
		namespace tutorial
		{
			class MyCoolClass
			{
			private:
				static std::string className;
				static jclass objectClass;
				jobject peerObject;

			protected:
				MyCoolClass(void* unused);
				virtual jobject _getPeerObject() const;

			public:
				static jclass _getObjectClass();
				static inline std::string _getClassName()
				{
					return className;
				}

				jclass getObjectClass();
				operator jobject();
				// constructors
				MyCoolClass(jobject obj);
				MyCoolClass();
				MyCoolClass(::net::sourceforge::jnipp::JStringHelper p0);

				virtual ~MyCoolClass();
				MyCoolClass& operator=(const MyCoolClass& rhs);

				// attribute setters
				void setSomeData(::net::sourceforge::jnipp::JStringHelper someData);
				static void setSomeStaticData(::net::sourceforge::jnipp::JStringHelper someStaticData);

				// methods
				/*
				 * String getSomeData();
				 */
				::net::sourceforge::jnipp::JStringHelper getSomeData();
				/*
				 * static String getSomeStaticData();
				 */
				static ::net::sourceforge::jnipp::JStringHelper getSomeStaticData();
				/*
				 * static void printContents(MyCoolClass);
				 */
				static void printContents(::demo::proxygen::tutorial::MyCoolClass p0);

			};
		}
	}
}


#endif
