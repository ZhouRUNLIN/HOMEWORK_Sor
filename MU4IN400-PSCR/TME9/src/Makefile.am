# noinst = no install, on peut utiliser bin_PROGRAMS sinon
noinst_PROGRAMS = server client textcr server2 client2

INC_DIR = $(top_builddir)/include
SRC_DIR = $(top_builddir)/src

# Flags to compiler
AM_CPPFLAGS =  -I $(INC_DIR) -g -Wall -std=c++1y

# global LDFLAG  = Linker flags
AM_LDFLAGS = -pthread -lprotobuf

chat.pb.cc : chat.proto
	protoc chat.proto --cpp_out ./

chat.pb.h : chat.proto
	protoc chat.proto --cpp_out ./

client2_SOURCES = Socket.h Socket.cpp ChatRoomProxy.h ChatRoomProxy2.cpp chat.pb.cc chat.pb.h clientchat.cpp
 
server2_SOURCES = TCPServer.h TCPServer.cpp ServerSocket.h ServerSocket.cpp chat.pb.cc chat.pb.h Socket.h Socket.cpp serverchat.cpp ChatServer.h ChatServer2.cpp

client_SOURCES = Socket.h Socket.cpp ChatRoomProxy.h ChatRoomProxy.cpp  clientchat.cpp
 
server_SOURCES = TCPServer.h TCPServer.cpp ServerSocket.h ServerSocket.cpp Socket.h Socket.cpp serverchat.cpp ChatServer.h ChatServer.cpp

textcr_SOURCES = IChatRoom.h TextChatRoom.h chatbasic.cpp



