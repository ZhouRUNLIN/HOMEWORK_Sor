#ifndef SRC_CONNECTIONHANDLER_H_
#define SRC_CONNECTIONHANDLER_H_

#include "Socket.h"

namespace pr {

// une interface pour gerer la communication
class ConnectionHandler {
public:
	// gerer une conversation sur une socket
	virtual void handleConnection(Socket s) = 0;
	// une copie identique
	virtual ConnectionHandler * clone() const = 0;
	// pour virtual
	virtual ~ConnectionHandler() {}
};}
#endif /* SRC_CONNECTIONHANDLER_H_ */
