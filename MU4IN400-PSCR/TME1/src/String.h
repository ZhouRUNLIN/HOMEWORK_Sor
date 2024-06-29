#ifndef SRC_STRING_H
#define SRC_STRING_H

#include <cstddef>
#include <string>
#include <ostream>

class String{
private :
    char* str;

public :
    String(const char* s);
    ~String();

    size_t length(const char* s);

    char* newcopy(const char* s);


    String(const String& other);

    String& operator=(const String& other);

    friend std::ostream& operator<<(std::ostream& os, const String& s);

    int compare(const String* other) const;

    bool operator<(const String& b) const;



};

bool operator==(const String& a, const String& b);




#endif /* SRC_STRING_H_ */
