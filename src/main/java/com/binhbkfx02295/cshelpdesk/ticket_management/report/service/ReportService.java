package com.binhbkfx02295.cshelpdesk.ticket_management.report.service;

import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;
import com.binhbkfx02295.cshelpdesk.ticket_management.report.model.Report;

public interface ReportService {
    String MSG_SUCCESS_CREATE_REPORT = "Tạo báo cáo thành công";
    String MSG_FAILED_CREATE_REPORT = "Lỗi hệ thống khi tạo báo cáo hourly";
    String MSG_FAILED_TICKET_FAIL = "Không thể tạo báo cáo";

    APIResultSet<Report> fetchHourlyReport(long fromTime, long toTime, String type, String label, boolean main, String timezone);
    APIResultSet<Report> fetchWeekdayReport(long fromTime, long toTime, String type, String label, boolean main, String timezone);
    APIResultSet<Report> fetchDayInMonthReport(long fromTime, long toTime, String type, String label, boolean main, String timezone);
}
