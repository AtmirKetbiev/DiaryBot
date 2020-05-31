package ru.bot.extension;

import ru.bot.objects.Progress;

import java.util.List;
import java.util.Map;

public class ReportExcel {

    private Map<Long, List<Progress>> studentProgress;

    public ReportExcel(Map<Long, List<Progress>> studentProgress) {
        this.studentProgress = studentProgress;
    }

    public void getReportFile() {

    }

}
