package zerobase.dividends.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam String keyWord) {
        return null;
    }

    @GetMapping
    public ResponseEntity<?> searchCompany() {
        return null;
    }

    @PostMapping
    public ResponseEntity<?> addCompany() {
        return null;
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCompany() {
        return null;
    }
}
