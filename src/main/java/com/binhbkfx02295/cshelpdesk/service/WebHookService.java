package com.binhbkfx02295.cshelpdesk.service;

import com.binhbkfx02295.cshelpdesk.dto.WebHookEventDTO;

public interface WebHookService {
    void handleWebhook(WebHookEventDTO event);
}