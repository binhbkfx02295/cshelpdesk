package com.binhbkfx02295.cshelpdesk.testutil;

import java.lang.reflect.Method;

/**
 * Helper gọi private-method qua Reflection cho unit-test.
 */
public final class TestInvoke {

    private TestInvoke() {}

    @SuppressWarnings("unchecked")
    public static <T, S> T callPrivate(S target, String methodName, Class<?>[] paramTypes, Object... args) {
        try {
            Method m = target.getClass().getDeclaredMethod(methodName, paramTypes);
            m.setAccessible(true);
            return (T) m.invoke(target, args);
        } catch (Exception e) {
            throw new RuntimeException("Cannot invoke " + methodName, e);
        }
    }

    /* Shortcut cho case getOrCreateFacebookUser(String) */
    public static com.binhbkfx02295.cshelpdesk.facebookuser.dto.FacebookUserDetailDTO
    callGetOrCreateFacebookUser(com.binhbkfx02295.cshelpdesk.webhook.service.WebHookServiceImpl service,
                                String facebookId) {
        return callPrivate(service,
                "getOrCreateFacebookUser",
                new Class[]{String.class},
                facebookId);
    }
}
