package srcs.chat.implem;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.google.protobuf.BoolValue;
import com.google.protobuf.Empty;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import srcs.chat.implem.ChatGrpc.ChatImplBase;

public class ChatImpl extends ChatImplBase {
	Map<String, InetSocketAddress> utils = new HashMap<>();
	
    /**
     */
    public void subscribe(Messages.SubM request,
        io.grpc.stub.StreamObserver<com.google.protobuf.BoolValue> responseObserver) {
    	
    	boolean exists;
    	synchronized (utils) {
    		exists = (utils.containsKey(request.getPseudo()));
    	}
    	
    	responseObserver.onNext(BoolValue.of(exists));
    	responseObserver.onCompleted();
    	
    }

    /**
     */
    public void send(Messages.SendM request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Int32Value> responseObserver) {
    	for (String user : utils.keySet()) {
    		InetSocketAddress	address = utils.get(user);
    		ManagedChannel 		chan = ManagedChannelBuilder.forAddress(address.getAddress().toString(), address.getPort()).build();
    		
    		MessageReceiverGrpc.MessageReceiverStub m = MessageReceiverGrpc.newStub(chan);
    		m.newMessage(request, new StreamObserver<Empty>() {
				
				@Override
				public void onNext(Empty arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onError(Throwable arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onCompleted() {
					// TODO Auto-generated method stub
					
				}
			});
    	}
    	
    	responseObserver.onNext(Int32Value.of(utils.keySet().size()));
    	responseObserver.onCompleted();
    }

    /**
     */
    public void listChatter(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<com.google.protobuf.StringValue> responseObserver) {
    	for (String user : utils.keySet()) {
    		responseObserver.onNext(StringValue.of(user));
    	}
    	responseObserver.onCompleted();
    }

    /**
     */
    public void unsubscribe(com.google.protobuf.StringValue request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
    	synchronized (utils) {
    		utils.remove(request.getValue());
    	}
    	
    	responseObserver.onNext(Empty.getDefaultInstance());
    	responseObserver.onCompleted();
    }

}
