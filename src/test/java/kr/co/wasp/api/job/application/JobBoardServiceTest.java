package kr.co.wasp.api.job.application;

import kr.co.wasp.api.job.interfaces.response.JobBoardDetailResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class JobBoardServiceTest {
    @Autowired
    JobBoardService jobBoardService;

    @Test
    void getJobBoardList() {
        int size = jobBoardService.getJobBoardList().size();
        int size1 = jobBoardService.getJobBoardList().size();
        int size2 = jobBoardService.getJobBoardList().size();
        assertEquals(size, size1, size2);
    }

    @Test
    void getJobBoardDetail() {
        JobBoardDetailResponse jobBoardDetail = jobBoardService.getJobBoardDetail("30001966");
        System.out.println();
    }
}