package com.binhbkfx02295.cshelpdesk.ticket_management.report.service;

import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;
import com.binhbkfx02295.cshelpdesk.ticket_management.report.model.Report;

public interface ReportService {

    Report fetchHourlyReport(long fromTime, long toTime, String type, String label, boolean main, String timezone);
    Report fetchWeekdayReport(long fromTime, long toTime, String type, String label, boolean main, String timezone);
    Report fetchDayInMonthReport(long fromTime, long toTime, String type, String label, boolean main, String timezone);
}
