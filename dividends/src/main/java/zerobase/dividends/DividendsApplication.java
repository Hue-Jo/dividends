package zerobase.dividends;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zerobase.dividends.model.Company;
import zerobase.dividends.scrapper.YahooFinanceScrapper;

import java.io.IOException;

//@SpringBootApplication
public class DividendsApplication {

    public static void main(String[] args) {
        //SpringApplication.run(DividendsApplication.class, args);

        YahooFinanceScrapper scrapper = new YahooFinanceScrapper();
        var result = scrapper.scrap(Company.builder().ticker("O").build());
        System.out.println(result);
    }

}
