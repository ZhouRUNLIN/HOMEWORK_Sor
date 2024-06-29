#ifndef SRC_MTCHATROOM_H_
#define SRC_MTCHATROOM_H_

#include "IChatRoom.h"
#include <memory>
#include <mutex>

namespace pr {

class MTChatRoom : public IChatRoom {
	IChatRoom * deco;
	mutable std::mutex mut;
public :
	MTChatRoom(IChatRoom * cr) : deco(cr) {};
	std::string getSubject() const {
		std::unique_lock<std::mutex> l(mut);
		return deco->getSubject();
	}
	std::vector<ChatMessage> getHistory() const {
		std::unique_lock<std::mutex> l(mut);
		return deco->getHistory();
	}
	bool posterMessage(const ChatMessage & msg) {
		std::unique_lock<std::mutex> l(mut);
		return deco->posterMessage(msg);
	}
	bool joinChatRoom (IChatter * chatter) {
		std::unique_lock<std::mutex> l(mut);
		return deco->joinChatRoom(chatter);
	}
	bool leaveChatRoom (IChatter * chatter) {
		std::unique_lock<std::mutex> l(mut);
		return deco->leaveChatRoom(chatter);
	}
	virtual size_t nbParticipants() const {
		std::unique_lock<std::mutex> l(mut);
		return deco->nbParticipants();
	}
};

}


#endif /* SRC_MTCHATROOM_H_ */
