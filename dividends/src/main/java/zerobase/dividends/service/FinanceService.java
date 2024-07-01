package zerobase.dividends.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import zerobase.dividends.model.Company;
import zerobase.dividends.model.Dividend;
import zerobase.dividends.model.ScrapedResult;
import zerobase.dividends.model.constants.CacheKey;
import zerobase.dividends.persist.CompanyRepository;
import zerobase.dividends.persist.DividendRepository;
import zerobase.dividends.persist.entity.CompanyEntity;
import zerobase.dividends.persist.entity.DividendEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@EnableCaching
@AllArgsConstructor
public class FinanceService {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    // 특정 데이터를 캐싱하는 게 적합한지를 아는 방법
    //   1. 요청이 자주 들어오는가?
    //   2. 자주 변경되는 데이터인가?
    @Cacheable(key = "#companyName", value = CacheKey.KEY_FINANCE)
    public ScrapedResult getDividendByCompanyName(String companyName) {
        log.info("search company -> " + companyName);

        // 1. 회사명을 기준으로 회사정보 조회
        CompanyEntity company = this.companyRepository
                .findByName(companyName)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회사명입니다"));

        // 2. 조회된 회사 아이디로 배당금 조회
        List<DividendEntity> dividendEntities = this.dividendRepository
                .findAllByCompanyId(company.getId());

        // 3. scrapedResult로 결과 반환
//        List<Dividend> dividends = new ArrayList<>();
//        for (var entity : dividendEntities) {
//            dividends.add(Dividend.builder()
//                                    .date(entity.getDate())
//                                    .dividend(entity.getDividend())
//                                    .build());
//        }
        List<Dividend> dividends = dividendEntities
                .stream()
                .map(e -> new Dividend(e.getDate(), e.getDividend()))
                .collect(Collectors.toList());

        return new ScrapedResult(
                new Company(company.getTicker(), company.getName()), dividends);
    }
}
