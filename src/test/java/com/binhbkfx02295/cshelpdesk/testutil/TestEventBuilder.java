package com.binhbkfx02295.cshelpdesk.testutil;

import com.binhbkfx02295.cshelpdesk.webhook.dto.WebHookEventDTO;

import java.util.Collections;
import java.util.List;

/**
 * Builder utility tạo một {@link WebHookEventDTO} tối thiểu cho unit test
 * – phù hợp 100 % với định nghĩa lớp WebHookEventDTO hiện tại.
 */
public final class TestEventBuilder {

    private TestEventBuilder() {}

    /**
     * @param senderId    ID của người gửi (customer ID hoặc PAGE_ID nếu nhân viên gửi)
     * @param recipientId ID của người nhận (thường là PAGE_ID hoặc customer ID)
     * @param text        Nội dung message (text) – có thể null nếu test attachment
     */
    public static WebHookEventDTO build(String senderId, String recipientId, String text) {

        /* ===== Messaging.User objects ===== */
        WebHookEventDTO.Messaging.User sender = new WebHookEventDTO.Messaging.User();
        sender.setId(senderId);

        WebHookEventDTO.Messaging.User recipient = new WebHookEventDTO.Messaging.User();
        recipient.setId(recipientId);

        /* ===== Message ===== */
        WebHookEventDTO.Messaging.Message message = new WebHookEventDTO.Messaging.Message();
        message.setText(text);
        // Có thể set thêm attachments, stickerId, quickReply… tùy test-case.

        /* ===== Messaging ===== */
        WebHookEventDTO.Messaging messaging = new WebHookEventDTO.Messaging();
        messaging.setSender(sender);
        messaging.setRecipient(recipient);
        messaging.setTimestamp(System.currentTimeMillis());
        messaging.setMessage(message);

        /* ===== Entry ===== */
        WebHookEventDTO.Entry entry = new WebHookEventDTO.Entry();
        entry.setId("ENTRY_ID_DUMMY");
        entry.setTime(System.currentTimeMillis());
        entry.setMessaging(List.of(messaging));

        /* ===== Root event ===== */
        WebHookEventDTO event = new WebHookEventDTO();
        event.setObject("page");
        event.setEntry(Collections.singletonList(entry));

        return event;
    }
}
