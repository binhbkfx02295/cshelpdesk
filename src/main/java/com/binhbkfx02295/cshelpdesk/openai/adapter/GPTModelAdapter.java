package com.binhbkfx02295.cshelpdesk.openai.adapter;

import com.binhbkfx02295.cshelpdesk.entity.Message;
import com.binhbkfx02295.cshelpdesk.openai.model.GPTResult;

import java.util.List;


public interface GPTModelAdapter {
    public GPTResult analyze(List<Message> messages);
}