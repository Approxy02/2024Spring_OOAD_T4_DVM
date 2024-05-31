package org.DVM.Control.Communication;

import java.util.HashMap;

public class Message {
    public MessageType msg_type ;
    public String src_id;
    public String dst_id;
    public HashMap<String, String> msg_content ;

    public Message(MessageType type, String src_id, String dst_id, HashMap<String, String> content) {
        this.msg_type  = type;
        this.src_id = src_id;
        this.dst_id = dst_id;
        this.msg_content  = content;
    }
}
