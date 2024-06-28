package zerobase.dividends.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import zerobase.dividends.model.Company;
import zerobase.dividends.model.Dividend;
import zerobase.dividends.model.ScrapedResult;
import zerobase.dividends.persist.CompanyRepository;
import zerobase.dividends.persist.DividendRepository;
import zerobase.dividends.persist.entity.CompanyEntity;
import zerobase.dividends.persist.entity.DividendEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FinanceService {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    public ScrapedResult getDividendByCompanyName(String companyName) {

        // 1. 회사명을 기준으로 회사정보 조회
        CompanyEntity company = this.companyRepository
                                        .findByName(companyName)
                                        .orElseThrow(() -> new RuntimeException("존재하지 않는 회사명입니다"));

        // 2. 조회된 회사 아이디로 배당금 조회
        List<DividendEntity> dividendEntities = this.dividendRepository
                                                        .findAllByCompanyId(company.getId());

        // 3. scrapedResult로 결과 반환
        List<Dividend> dividends = new ArrayList<>();
        for (var entity : dividendEntities) {
            dividends.add(Dividend.builder()
                                    .date(entity.getDate())
                                    .dividend(entity.getDividend())
                                    .build());

        }

//        List<Dividend> dividends = dividendEntities
//                                    .stream()
//                                        .map(e -> Dividend.builder()
//                                        .date(e.getDate())
//                                        .dividend(e.getDividend())
//                                        .build())
//                                    .collect(Collectors.toList());

        return new ScrapedResult(Company.builder()
                                        .ticker(company.getTicker())
                                        .name(company.getName())
                                        .build(),
                                dividends);
    }
}
