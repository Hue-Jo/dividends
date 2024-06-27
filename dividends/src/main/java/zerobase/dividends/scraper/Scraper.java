package zerobase.dividends.scraper;

import zerobase.dividends.model.Company;
import zerobase.dividends.model.ScrapedResult;

public interface Scraper {
    Company scrapCompanyByTicker(String ticker);
    ScrapedResult scrap(Company company);
}
