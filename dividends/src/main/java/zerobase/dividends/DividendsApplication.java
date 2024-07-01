package zerobase.dividends;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import zerobase.dividends.scraper.Scraper;
import zerobase.dividends.scraper.YahooFinanceScraper;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class DividendsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DividendsApplication.class, args);

        //Scraper scrapper = new YahooFinanceScraper();
        //var result = scrapper.scrap(Company.builder().ticker("O").build());
        //var result = scrapper.scrapCompanyByTicker("MMM");
        //System.out.println(result);
    }
}
