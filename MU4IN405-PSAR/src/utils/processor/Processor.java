package utils.processor;

import utils.channel.Channel;
import utils.exception.ServerException;
import utils.message.Message;

import java.io.IOException;
import java.net.Socket;

public interface Processor {
    void process(Channel channel, String id,Message message) throws IOException, ClassNotFoundException, ServerException, InterruptedException;
}
