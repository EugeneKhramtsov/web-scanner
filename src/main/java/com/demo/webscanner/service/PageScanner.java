package com.demo.webscanner.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class PageScanner implements Runnable {

    private LinkedBlockingQueue<String> pagesQueue;
    private Map<String, Integer> scannedPages;
    private String lookingFor;
    private String processingPage;
    private int maxPages;

    public PageScanner(LinkedBlockingQueue<String> pagesQueue, Map<String, Integer> scannedPages,
            final String lookingFor, final int maxPages)
    {
        this.pagesQueue = pagesQueue;
        this.scannedPages = scannedPages;
        this.lookingFor = lookingFor;
        this.maxPages = maxPages;

        try {
            processingPage = this.pagesQueue.take();
        } catch (InterruptedException e) {
            log.error("Thread {} was interrupted {}", Thread.currentThread(), e.getMessage());
        }
    }

    @Override
    public void run() {
        System.out.println("Thread " + Thread.currentThread().getName() + " Scanning page " + processingPage);
        if (!isCorrectWebLink(processingPage)) {
            return;
        }
        Set<String> foundPages = findPages();
        int textMatches = wordMatches();
        scannedPages.put(processingPage, textMatches);
        foundPages.removeAll(scannedPages.keySet());
        if (scannedPages.size() + pagesQueue.size() < maxPages) {
            pagesQueue.addAll(foundPages);
        }
    }

    private boolean isCorrectWebLink(final String link) {
        try {
            if (link == null || link.isEmpty()) {
                return false;
            }
            Jsoup.connect(link).get();
            return true;
        } catch (IOException e) {
            log.error(String.format("URL %s processed with error %s", link, e.getMessage()));
            return false;
        }
    }

    private Set<String> findPages() {
        final Document doc;
        try {
            doc = Jsoup.connect(processingPage).get();
        } catch (IOException e) {
            log.error(e.getMessage());
            return Collections.emptySet();
        }
        final Elements links = doc.select("body a");
        Set<String> urls = new HashSet<>();
        for(Element element : links) {
            String link = element.attributes().get("href");
            if (isLinkStartWithProtocol(link)) {
                urls.add(link);
            } else {
                urls.add(processingPage + link);
            }
        }
        return urls;
    }

    private boolean isLinkStartWithProtocol(final String link) {
        return link.startsWith("http://") || link.startsWith("https://") || link.startsWith("file://");
    }

    private int wordMatches() {
        final URL url;
        try {
            url = new URL(processingPage);
        } catch (MalformedURLException e) {
            log.error(e.getMessage());
            return 0;
        }
        int matches = 0;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                if (inputLine.contains(lookingFor)) {
                    matches++;
                }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return matches;
    }
}
