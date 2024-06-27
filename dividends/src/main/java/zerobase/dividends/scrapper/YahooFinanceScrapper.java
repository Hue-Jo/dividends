package zerobase.dividends.scrapper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import zerobase.dividends.model.Company;
import zerobase.dividends.model.Dividend;
import zerobase.dividends.model.ScrapedResult;
import zerobase.dividends.model.constants.Month;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class YahooFinanceScrapper {
    private static final String STATISTIC_URL = "https://finance.yahoo.com/quote/%s/history/?frequency=1mo&period1=%d&period2=%d";
    private static final long START_TIME = 86400; // 60 * 60 * 24

    public ScrapedResult scrap(Company company) {
        var scrapResult = new ScrapedResult();
        scrapResult.setCompany(company);

        try {
            long now = System.currentTimeMillis() / 1000;

            String url = String.format(STATISTIC_URL, company.getTicker(), START_TIME, now);
            Connection connection = Jsoup.connect(url);
            Document document = connection.get();

            Elements parsingDivs = document.getElementsByClass("table svelte-ewueuo");
            Element tableEle = parsingDivs.get(0); // table 전체

            Element tbody = tableEle.getElementsByTag("tbody").first();

            List<Dividend> dividends = new ArrayList<>();
            for (Element e : tbody.children()) {
                String txt = e.text();
                if (!txt.endsWith("Dividend")) {
                    continue;
                }

                String[] splits = txt.split(" ");
                int month = Month.strToNumber(splits[0]);
                int day = Integer.valueOf(splits[1].replace(",", ""));
                int year = Integer.valueOf(splits[2]);
                String dividend = splits[3];

                if (month < 0) {
                    throw new RuntimeException("Unexpected Month enum value => " + splits[0]);
                }

                dividends.add(Dividend.builder()
                                    .date(LocalDateTime.of(year, month, day, 0, 0))
                                    .dividend(dividend)
                                    .build()
                );

                //System.out.println(year + "/" + month + "/" + day + " -> " + dividend);
            }
            scrapResult.setDividendEntities(dividends);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return scrapResult;
    }

    public Company scrapCompanyByTicker(String ticker) {
        return null;
    }
}
