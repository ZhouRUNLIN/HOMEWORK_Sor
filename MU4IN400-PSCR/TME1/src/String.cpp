#include "String.h"

size_t String::length(const char* s)
{
    size_t i = 0;
    while (s[i]) {
        i++;
    }
    return i;
}

char *String::newcopy(const char* s)
{
    char* str2 = new char[length(s) + 1];
    for (int i = 0; i <= length(s); i++)
    {
        str2[i] = s[i];
    }

    return str2;
}



String::String(const char *s)
{
    str = newcopy(s);
}

String::~String()
{
    delete[] str;
}

String::String(const String &other)
{
    str = newcopy(other.str);
}

String &String::operator=(const String &other)
{
    if (this != &other)
    {
        delete[] str;
        str = newcopy(other.str);
    }
    return *this;
}



std::ostream &operator<<(std::ostream &os, const String &s)
{
    os << "String Data : ";
    os << s.str << std::endl;
    return os;
}



int String::compare(const String *other) const
{
    const char* s1 = this->str;
    const char* s2 = other->str;

    while (*s1 != '\0' && (*s1 == *s2)) {
        ++s1;
        ++s2;
    }

    return (*s1 - *s2);
}

bool String::operator<(const String &b) const
{
    return  this->compare(&b) < 0;
}

bool operator==(const String &a, const String &b)
{
    return a.compare(&b) == 0;
}
