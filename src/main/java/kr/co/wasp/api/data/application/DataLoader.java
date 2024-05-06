package kr.co.wasp.api.data.application;

import org.springframework.stereotype.Component;

@Component
public class DataLoader implements org.springframework.boot.CommandLineRunner {
    private final DataLoadService dataLoadService;

    public DataLoader(DataLoadService dataLoadService) {
        this.dataLoadService = dataLoadService;
    }

    @Override
    public void run(String... args) throws Exception{
        dataLoadService.getMedicine();
//        dataLoadService.getHospital();
        dataLoadService.getPharmacy();

    }
}
