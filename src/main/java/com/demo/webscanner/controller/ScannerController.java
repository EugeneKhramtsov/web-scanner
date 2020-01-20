package com.demo.webscanner.controller;

import com.demo.webscanner.model.ScannerInputTo;
import com.demo.webscanner.service.ScannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
public class ScannerController {

    @Autowired
    private ScannerService scannerService;

    @GetMapping("/scanner")
    public String scannerForm(Model model) {
        model.addAttribute("scannerInputTo", new ScannerInputTo());
        return "scanner";
    }

    @PostMapping("/scanner")
    public String scannerSubmit(@ModelAttribute ScannerInputTo scannerInputTo, Model model) {
        final Map<String, Integer> resultTo = scannerService.scan(scannerInputTo);
        model.addAttribute("scannerResultTo", resultTo);
        return "result";
    }

    @PostMapping("/scanner/stop")
    public String scannerStop() {
        scannerService.stop();
        return "result";
    }
}
