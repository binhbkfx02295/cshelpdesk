package com.binhbkfx02295.cshelpdesk.testutil;

import com.binhbkfx02295.cshelpdesk.employee_management.employee.entity.Employee;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.entity.Status;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.entity.StatusLog;
import com.binhbkfx02295.cshelpdesk.employee_management.usergroup.UserGroup;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility tạo Map<String, Employee> cho cache.getAllEmployees() trong unit test.
 */
public final class DummyEmployees {

    private DummyEmployees() {}

    /** Map gồm <code>count</code> nhân viên ONLINE (key = username). */
    public static Map<String, Employee> onlineStaff(int count) {
        Map<String, Employee> map = new HashMap<>(count);
        for (int i = 1; i <= count; i++) {
            Employee e = new Employee();
            e.setUsername("staff" + i);
            StatusLog statusLog= new StatusLog();
            Status status = new Status();
            status.setId(1);
            statusLog.setStatus(status);
            e.getStatusLogs().add(statusLog);
            // Gắn trạng thái online tuỳ theo entity của bạn, ví dụ:
            // e.setLatestOnlineStatus(Employee.OnlineStatus.ONLINE);

            UserGroup gruop = new UserGroup();
            gruop.setCode("staff");
            e.setUserGroup(gruop);
            map.put(e.getUsername(), e);
        }
        return map;
    }

    /** Map rỗng: không có nhân viên online. */
    public static Map<String, Employee> noOnlineStaff() {
        return Collections.emptyMap();
    }
}
