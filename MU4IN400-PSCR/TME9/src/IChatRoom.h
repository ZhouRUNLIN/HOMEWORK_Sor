#ifndef SRC_ICHATROOM_H_
#define SRC_ICHATROOM_H_

#include <string>
#include <vector>

namespace pr {

class ChatMessage {
	std::string author;
	std::string message;
public :
	ChatMessage (const std::string & author, const std::string & msg):author(author),message(msg) {};
	const std::string & getAuthor() const {return author; }
	const std::string & getMessage() const {return message; }
};

class IChatter {
public :
	virtual std::string getName() const  = 0;
	virtual void messageReceived (ChatMessage  msg) = 0;
	virtual ~IChatter() {}
};

class IChatRoom {
public :
	virtual std::string getSubject() const = 0;
	virtual std::vector<ChatMessage> getHistory() const = 0;
	virtual bool posterMessage(const ChatMessage & msg) = 0;
	virtual bool joinChatRoom (IChatter * chatter) = 0;
	virtual bool leaveChatRoom (IChatter * chatter) = 0;
	virtual size_t nbParticipants() const =0;
	virtual ~IChatRoom() {}
};

}

#endif /* SRC_ICHATROOM_H_ */
