package com.Nteam.backend.formediapi.data.application;
import com.Nteam.backend.formediapi.hospital.entity.Hospital;
import com.Nteam.backend.formediapi.hospital.entity.Nation;
import com.Nteam.backend.formediapi.hospital.repository.NationRepository;
import com.Nteam.backend.formediapi.hospital.service.HospitalService;
import com.Nteam.backend.formediapi.medicine.entity.Medicine;
import com.Nteam.backend.formediapi.medicine.entity.MedicineCategory;
import com.Nteam.backend.formediapi.medicine.repository.MedicineCategoryRepository;
import com.Nteam.backend.formediapi.medicine.repository.MedicineRepository;
import com.Nteam.backend.formediapi.pharmacy.domain.PharmacyEntity;
import com.Nteam.backend.formediapi.pharmacy.inflastructure.PharmacyRepository;
import com.Nteam.backend.formediapi.pharmacy.inflastructure.datatool.ResponseDTO;
import com.Nteam.backend.formediapi.pharmacy.inflastructure.datatool.address.RootDto;
import com.deepl.api.TextResult;
import com.deepl.api.Translator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DataLoadService {

    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private MedicineCategoryRepository medicineCategoryRepository;
    @Autowired
    private PharmacyRepository pharmacyRepository;
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private NationRepository nationRepository;

    public void getMedicine() {
        String medicineAPIPath = "MedicineAPI.json";
        String mediName;

        try {
            // Medicine 저장
            InputStream medicineInputStream = new ClassPathResource(medicineAPIPath).getInputStream();
            ObjectMapper medicineObjectMapper = new ObjectMapper();
            JsonNode medicineRootNode = medicineObjectMapper.readTree(medicineInputStream);
            JsonNode medicinesNode = medicineRootNode.isArray() ? medicineRootNode : medicineRootNode.get("medicines");
            if (medicinesNode != null && medicinesNode.isArray()) {
                for (JsonNode medicineNode : medicinesNode) {
                    Medicine medicine = new Medicine();
                    medicine.setMedicine_id(medicineNode.get("medicine_id").asLong());
                    medicine.setMedicine_name(medicineNode.get("medicine_name").asText());
                    mediName = medicineNode.get("medicine_name").asText();
                    medicine.setMedicine_name_en(translate(mediName, "en-Us"));
                    medicine.setMedicine_name_ch(translate(mediName, "ZH"));
                    medicine.setMedicine_name_ja(translate(mediName, "JA"));
                    medicine.setMedicine_image(medicineNode.get("medicine_image").asText());
                    medicine.setMedicine_efficacy(medicineNode.get("medicine_efficacy").asText());
                    medicine.setInstructions(medicineNode.get("instructions").asText());
                    medicine.setMedicine_caution(medicineNode.get("medicine_caution").asText());
                    medicine.setMedicine_side_effect(medicineNode.get("medicine_side_effect").asText());

                    // MedicineCategory 참조 설정
                    JsonNode categoryNode = medicineNode.get("medicineCategory");
                    if (categoryNode != null) {
                        String categoryName = categoryNode.get("category_name").asText();
                        MedicineCategory category = medicineCategoryRepository.findByCategoryName(categoryName);
                        if (category == null) {
                            // 카테고리가 존재하지 않으면 새로운 카테고리를 생성하여 저장
                            category = new MedicineCategory();
                            category.setCategoryName(categoryName);
                            category = medicineCategoryRepository.save(category);
                        }
                        medicine.setMedicineCategory(category);
                    }

                    medicineRepository.save(medicine);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void getHospital() {
        String filePath = "src/main/resources/hospital_test.csv";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // 헤더 처리

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                String hospitalKey = data[0];
                String hospitalRegisterNum = data[1];
                String hospitalName = data[2];
                String hospitalCategory = data[3];
                String hospitalSi = data[4];
                String hospitalGu = data[5];
                String hospitalDong = data[6];
                String hospitalCeo = data[7];
                String hospitalAddress = data[8];

                Hospital hospital = new Hospital();
                hospital.setHospital_key(hospitalKey);
                hospital.setHospital_register_num(hospitalRegisterNum);
                hospital.setHospital_name(hospitalName);
                hospital.setHospital_name_en(translate(hospitalName, "en-Us"));
                hospital.setHospital_name_ch(translate(hospitalName, "ZH"));
                hospital.setHospital_name_ja(translate(hospitalName, "JA"));
                hospital.setHospital_category(hospitalCategory);
                hospital.setHospital_si(hospitalSi);
                hospital.setHospital_gu(hospitalGu);
                hospital.setHospital_dong(hospitalDong);
                hospital.setHospital_ceo(hospitalCeo);

                // 주소에서 큰따옴표가 있는 경우 처리
                hospitalAddress = hospitalAddress.replaceAll("^\"|\"$", "");

                hospital.setHospital_address(hospitalAddress);

                // 국가 정보가 있는 경우 처리
                if (data.length > 9) {
                    String[] nationNames = data[9].split(",");

                    // 마지막 요소가 공백인 경우 제거
                    if (nationNames.length > 0) {
                        nationNames = Arrays.copyOf(nationNames, nationNames.length - 1);
                    }

                    for (String nationName : nationNames) {
                        // 큰따옴표 제거
                        nationName = nationName.replaceAll("^\"|\"$", "").trim();
                        Nation nation = nationRepository.findByName(nationName);
                        if (nation == null) {
                            // 미리 정의된 국가에 존재하지 않는 경우 새로 생성하여 저장
                            nation = new Nation();
                            nation.setName(nationName);
                            nation = nationRepository.save(nation);
                        }
                        hospital.getHospital_nations().add(nation);
                    }
                }


                hospitalService.saveHospitalAndNations(hospital);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public void getPharmacy() {
        ObjectMapper mapper = new ObjectMapper();
        String address;
        double longtitude;
        double latitude;
        String pharmacyname;

        try {
            ClassPathResource resource = new ClassPathResource("pharmacyList_test.json");
            JsonNode root = mapper.readTree(resource.getInputStream()); // JSON 데이터를 JsonNode로 읽음

            for (JsonNode node : root) { //돌면서 저장
                PharmacyEntity pharmacy = new PharmacyEntity();
                pharmacy.setPhar_id(node.get("연번").asLong());
                pharmacy.setPhar_gu(node.get("자치구").asText());
                pharmacyname = node.get("약국이름").asText();
                pharmacy.setPhar_name(pharmacyname);
                pharmacy.setPhar_name_en(translate(pharmacyname, "en-Us"));
                pharmacy.setPhar_name_ch(translate(pharmacyname, "ZH"));
                pharmacy.setPhar_name_ja(translate(pharmacyname, "JA"));
                pharmacy.setAddress(node.get("주소 (도로명)").asText());
                address = node.get("주소 (도로명)").asText();
//                longtitude = generateCoordinate(address).getLongitude();
//                latitude = generateCoordinate(address).getLatitude();
//                pharmacy.setLongitude(longtitude);
//                pharmacy.setLatitude(latitude);
                pharmacy.setPhone(node.get("전화번호").asText());
                pharmacy.setEnglish(node.has("영어") && "○".equals(node.get("영어").asText()));
                pharmacy.setChinese(node.has("중국어") && "○".equals(node.get("중국어").asText()));
                pharmacy.setJapanese(node.has("일본어") && "○".equals(node.get("일본어").asText()));
                pharmacyRepository.save(pharmacy);
            }
        }catch (IOException e){
            System.out.print(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseDTO generateCoordinate(String address) throws UnsupportedEncodingException { //주소 -> 위,경도 생성
        ResponseEntity<RootDto> responseEntity = requestCoordinate("6697ce651492e186db0ea6d0c9dc850a", address); //요청 api, 주소

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            System.out.print(responseEntity.getBody());
            RootDto rootDto = responseEntity.getBody();
            if(rootDto.isDocumentsEmpty()){ //검색이 되지않는다면..
                ResponseDTO responseDTO = new ResponseDTO(37.5, 37.5);
                return responseDTO;
            }
            ResponseDTO responseDTO = new ResponseDTO(rootDto.getDocuments().get(0).getX(), rootDto.getDocuments().get(0).getY()); //추가
            return responseDTO;
        }

        return null;
    }

    private ResponseEntity<RootDto> requestCoordinate(String apiKey, String address) throws UnsupportedEncodingException {
        String encodedAddress = URLEncoder.encode(address, "UTF-8");

        String apiUrl = "https://dapi.kakao.com/v2/local/search/address.json";
        URI uri = UriComponentsBuilder //url생성
                .fromUriString(apiUrl)
                .queryParam("query", address)
                .build()
                .encode()
                .toUri();

        HttpHeaders headers = new HttpHeaders(); //헤더 생성
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "KakaoAK " + apiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<RootDto> response = restTemplate.exchange(uri, HttpMethod.GET, entity, RootDto.class); //url + 헤더 + get요청 후 Dto변환
        return response;
        }

    public String translate(String sentence, String language) throws Exception { //번역할문장 , 언어 -> 번역
        Translator translator;
        String authKey = "300146b9-57bd-413d-9693-610ccdc57af3:fx";  // Replace with your key
        translator = new Translator(authKey);
        TextResult result =
                translator.translateText(sentence, null, language);
        return result.getText();
    }
}
