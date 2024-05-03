package kr.co.wasp.api.job.infrastructure;

import kr.co.wasp.api.job.infrastructure.datatool.NaverJobBoardClient;
import kr.co.wasp.api.job.infrastructure.datatool.NaverJobBoardResponse;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
class NaverJobBoardClientTest {
    @Autowired
    NaverJobBoardClient naverJobBoardClient;

    @Test
    void loadJobList() {
        int currentIdx = 0;
        NaverJobBoardResponse res = null;
        try {
            List<NaverJobBoardResponse> result = Lists.newArrayList();
            res = naverJobBoardClient.getJobList(currentIdx);
            result.add(res);
            currentIdx += res.getList().size();
            while (currentIdx < res.getTotalSize()) {
                res = naverJobBoardClient.getJobList(currentIdx);
                result.add(res);
                currentIdx += res.getList().size();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(currentIdx, (int) res.getTotalSize());
    }
}