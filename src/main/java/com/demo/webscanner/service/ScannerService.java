package com.demo.webscanner.service;

import com.demo.webscanner.model.ScannerInputTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Service
public class ScannerService {

    private int maxPages;
    private String lookingFor;
    private Map<String, Integer> scannedPages;
    private LinkedBlockingQueue<String> pagesQueue;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    public Map<String, Integer> scan(final ScannerInputTo scannerInputTo) {
        initialize(scannerInputTo);
        while (scannedPages.size() < maxPages) {
            if (needToWait()) {
                waitExecutorToProceed();
            } else {
                startScanner(new PageScanner(pagesQueue, scannedPages, lookingFor, maxPages));
            }
        }
        pagesQueue.clear();
        return scannedPages;
    }

    public void stop() {
        taskExecutor.shutdown();
        if (pagesQueue != null) pagesQueue.clear();
    }

    private void initialize(final ScannerInputTo scannerInputTo) {
        maxPages = scannerInputTo.getMaxNumberOfPagesToScan();
        lookingFor = scannerInputTo.getLookingFor();
        scannedPages = new ConcurrentHashMap<>(maxPages);
        pagesQueue = new LinkedBlockingQueue<>();
        pagesQueue.add(scannerInputTo.getUrl());
    }

    private boolean needToWait() {
        return (pagesQueue.isEmpty() && areBusyThreads()) || (scannedPages.size() + taskExecutor.getThreadPoolExecutor().getQueue().size() >= maxPages);
    }

    private void startScanner(final PageScanner pageScanner) {
        try {
            taskExecutor.execute(pageScanner);
        } catch (TaskRejectedException e) {
            log.error("Executor stopped working. ", e.getMessage());
        }
    }

    private boolean areBusyThreads() {
        return taskExecutor.getActiveCount() > 0;
    }

    private void waitExecutorToProceed() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) { }
    }
}
