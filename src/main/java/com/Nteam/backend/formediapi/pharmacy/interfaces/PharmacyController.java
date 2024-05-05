package com.Nteam.backend.formediapi.pharmacy.interfaces;
import com.Nteam.backend.formediapi.pharmacy.application.PharmacyService;
import com.Nteam.backend.formediapi.pharmacy.domain.PharmacyEntity;
import com.Nteam.backend.formediapi.pharmacy.inflastructure.datatool.PharmacyDTO;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pharmacies") //전체약국조회
public class PharmacyController {

    private final PharmacyService pharmacyService;

    public PharmacyController(PharmacyService pharmacyService){
        this.pharmacyService = pharmacyService;
    }

    @GetMapping()
    public List<PharmacyDTO> getAllPharmacies(){
        return pharmacyService.getPharmacyList();

    }
    @GetMapping("/{id}") //아이디조회
    public PharmacyDTO getPharmacy(@PathVariable(value = "id") Long id){
        System.out.print(id);
        return pharmacyService.getPharmacy(id);

    }
    @GetMapping("/language/{langId}")
    public List<PharmacyDTO> getPharmaciesByLanguage(@PathVariable(value = "langId") int langId) {
        return pharmacyService.getPharmaciesByLanguage(langId);
    }

//    @GetMapping("/name/{name}")
//    public List<PharmacyDTO> getPharmaciesByName(@PathVariable(value = "name") String name) {
//        return pharmacyService.getPharmaciesByName(name);
//    }
//
//    @GetMapping("/gu/{gu}")
//    public List<PharmacyDTO> getPharmaciesByDistrict(@PathVariable(value = "gu") String gu) {
//        return pharmacyService.getPharmaciesByDistrict(gu);
//    }

//    @GetMapping("/gu/{gu}") // 구조회
//    public List<PharmacyDTO> getHospitalsByGu(@PathVariable(value = "gu") String gu) {
//        return pharmacyService.getHospitalsByGu(gu);
//    }

    @GetMapping("/nearest") //근처 약국 조회 요청값 위도, 경도, 언어 가능유무
    public List<PharmacyEntity> getNearestPharmacies(

            @RequestParam("lat") double lat,
            @RequestParam("lon") double lon,
            @RequestParam(name = "english", required = false, defaultValue = "true") boolean english,
            @RequestParam(name = "chinese", required = false, defaultValue = "false") boolean chinese,
            @RequestParam(name = "japanese", required = false, defaultValue = "false") boolean japanese)
    {
        return pharmacyService.findNearestPharmacies(lat, lon, english, chinese, japanese);
    }


}
