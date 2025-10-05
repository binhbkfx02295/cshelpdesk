package com.binhbkfx02295.cshelpdesk.websocket.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.binhbkfx02295.cshelpdesk.dto.EmployeeDTO;

@Getter
@RequiredArgsConstructor
public class EmployeeEvent {
    public enum Action {CREATED, UPDATED}
    private final Action action;
    private final EmployeeDTO employeeDTO;
}