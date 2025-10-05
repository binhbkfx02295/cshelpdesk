package com.binhbkfx02295.cshelpdesk.openai.service;

import com.binhbkfx02295.cshelpdesk.entity.Message;
import com.binhbkfx02295.cshelpdesk.openai.model.GPTResult;
import com.binhbkfx02295.cshelpdesk.entity.Ticket;
import java.util.List;
public interface GPTTicketService {

    public GPTResult analyze(Ticket ticket);

    public GPTResult analyze(List<Message> messages);
}
