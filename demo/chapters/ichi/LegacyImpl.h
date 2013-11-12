#ifndef __LegacyImpl_H
#define __LegacyImpl_H

#include <string>

class LegacyImpl
{
private:
	std::string data;
	
public:
	LegacyImpl()
		: data( "" )
	{
	}

	void loadByID(const std::string& id)
	{
		data = "LegacyImpl ID( " + id + " )";
	}

	std::string getData()
	{
		return data;
	}
};

#endif

