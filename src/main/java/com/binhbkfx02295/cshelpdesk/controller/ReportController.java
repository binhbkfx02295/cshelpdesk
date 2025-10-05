package com.binhbkfx02295.cshelpdesk.controller;

import com.binhbkfx02295.cshelpdesk.entity.Report;
import com.binhbkfx02295.cshelpdesk.service.ReportServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
@Slf4j
public class ReportController {

    private final ReportServiceImpl reportService;

    @GetMapping(value = "/ticket-by-hour", params = {"fromTime", "toTime", "type", "label", "timezone"})
    public ResponseEntity<Report> getHourlyReport(
            long fromTime,
            long toTime,
            String type,
            String label,
            String timezone,
            @RequestParam(value = "main", defaultValue = "false") boolean main) {
        return ResponseEntity.ok(reportService.fetchHourlyReport(fromTime, toTime, type, label, main, timezone));
    }

    @GetMapping(value = "/ticket-by-weekday", params = {"fromTime", "toTime", "type", "label", "timezone"})
    public ResponseEntity<Report> getWeekdayReport(
            long fromTime,
            long toTime,
            String type,
            String label,
            String timezone,
            @RequestParam(value = "main", defaultValue = "false") boolean main) {
        return ResponseEntity.ok(reportService.fetchWeekdayReport(fromTime, toTime, type, label, main, timezone));
    }

    @GetMapping(value = "/ticket-by-day", params = {"fromTime", "toTime", "type", "label", "timezone"})
    public ResponseEntity<Report> getDayInMonthReport(
            long fromTime,
            long toTime,
            String type,
            String label,
            String timezone,
            @RequestParam(value = "main", defaultValue = "false") boolean main) {
        return ResponseEntity.ok(reportService.fetchDayInMonthReport(fromTime, toTime, type, label, main, timezone));
    }

}
