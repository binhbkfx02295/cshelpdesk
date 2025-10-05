package com.binhbkfx02295.cshelpdesk.dto;

import lombok.Data;

@Data

public class TicketPerformanceDTO {
    private EmployeeTicketDTO assignee;
    private SatisfactionDTO satisfactionDTO;
    private Long firstResponseRate;
    private Long overallResponseRate;
    private Long resolutionRate;
}
