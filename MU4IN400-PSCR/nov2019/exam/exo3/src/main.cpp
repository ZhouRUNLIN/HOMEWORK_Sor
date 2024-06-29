#include "concat.h"
#include <vector>
#include <iostream>
#include <string>

using namespace std;
using namespace pr;

int main () {
	vector<string> v1;
	v1.push_back("abc"); v1.push_back("def");

	vector<string> v2;
	v2.push_back("ghi"); v2.push_back("klm");

	// sans faire de copies !
	concat conc = concat(v1,v2);
	for (const string & s : conc) {
		cout << s << ":";
	}
	cout << endl;
	return 0;
}
