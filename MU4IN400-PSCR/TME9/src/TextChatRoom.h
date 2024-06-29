#ifndef SRC_TEXTCHAT_H_
#define SRC_TEXTCHAT_H_

#include "IChatRoom.h"

#include <iostream>
#include <algorithm>

namespace pr {

class TextChatter : public IChatter {
	std::string name;
public :
	TextChatter (const std::string & name):name(name){}
	std::string getName() const  { return name ; }
	void messageReceived (ChatMessage  msg) { std::cout << "(chez " << name << ") de : " << msg.getAuthor() << " > " << msg.getMessage() << std::endl; }
};

class TextChatRoom : public IChatRoom {
	std::string subject;
	std::vector<ChatMessage> history;
	std::vector<IChatter *> participants;
public :
	 TextChatRoom(const std::string & subject) : subject(subject) {}
	 std::string getSubject() const  { return subject; }
	 std::vector<ChatMessage> getHistory() const { return history ; }
	 bool posterMessage(const ChatMessage & msg) {
		 history.push_back(msg);
		 for (auto c : participants) {
			 c->messageReceived(msg);
		 }
		 return true;
	 }
	 bool joinChatRoom (IChatter * chatter) {
		 participants.push_back(chatter);
		 return true;
	 }
	 bool leaveChatRoom (IChatter * chatter) {
		 auto it = std::find(participants.begin(),participants.end(),chatter);
		 if (it != participants.end()) {
			 participants.erase(it);
			 return true;
		 }
		 return false;
	 }
	 size_t nbParticipants() const { return participants.size(); }
};

}

#endif
