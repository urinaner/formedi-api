package kr.co.wasp.api.data.application;


import com.deepl.api.TextResult;
import com.deepl.api.Translator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.wasp.api.hospital.entity.Hospital;
import kr.co.wasp.api.hospital.entity.Nation;
import kr.co.wasp.api.hospital.repository.NationRepository;
import kr.co.wasp.api.hospital.service.HospitalService;
import kr.co.wasp.api.medicine.entity.Medicine;
import kr.co.wasp.api.medicine.entity.MedicineCategory;
import kr.co.wasp.api.medicine.repository.MedicineCategoryRepository;
import kr.co.wasp.api.medicine.repository.MedicineRepository;
import kr.co.wasp.api.pharmacy.domain.PharmacyEntity;
import kr.co.wasp.api.pharmacy.inflastructure.PharmacyRepository;
import kr.co.wasp.api.pharmacy.inflastructure.datatool.ResponseDTO;
import kr.co.wasp.api.pharmacy.inflastructure.datatool.address.RootDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
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
        String filePath = "src/main/resources/hospital.csv";

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
                hospital.setHospital_category(hospitalCategory);
                hospital.setHospital_si(hospitalSi);
                hospital.setHospital_gu(hospitalGu);
                hospital.setHospital_dong(hospitalDong);
                hospital.setHospital_ceo(hospitalCeo);
                hospital.setHospital_name_en(translate(hospitalName, "en-Us"));
                hospital.setHospital_name_ch(translate(hospitalName, "ZH"));
                hospital.setHospital_name_ja(translate(hospitalName, "JA"));

                // 주소가 비어있는 경우 위도, 경도에 null 설정
                if (hospitalAddress.isEmpty()) {
                    hospital.setHospital_latitude(null);
                    hospital.setHospital_longitude(null);
                } else {
                    // 주소에서 큰따옴표가 있는 경우 처리
                    hospitalAddress = hospitalAddress.replaceAll("^\"|\"$", "");
                    hospital.setHospital_address(hospitalAddress);

                    // 주소 -> 위도, 경도
                    String encodedAddress = URLEncoder.encode(hospitalAddress, StandardCharsets.UTF_8);

                    String apiUrl = "http://15.164.137.89:8080/map/" + encodedAddress;
                    URI uri = URI.create(apiUrl);

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

                    RestTemplate restTemplate = new RestTemplate();
                    try {
                        ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), Map.class);
                        if (response.getStatusCode() == HttpStatus.OK) {
                            Map<String, Double> coordinates = response.getBody();
                            if (coordinates != null) {
                                double latitude = coordinates.get("latitude");
                                double longitude = coordinates.get("longitude");
                                hospital.setHospital_latitude(latitude);
                                hospital.setHospital_longitude(longitude);
                            } else {
                                System.out.println("Failed to retrieve coordinates for the given address. Hospital ID: " + hospitalKey + ", Address: " + hospitalAddress);
                                log.debug("Failed to retrieve coordinates for the given address. Hospital ID: {}, Address: {}", hospitalKey, hospitalAddress);
                            }
                        } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                            System.out.println("404 Error occurred for hospital: " + hospitalKey);
                            log.debug("404 Error occurred for hospital: {}", hospitalKey);
                            hospital.setHospital_latitude(null);
                            hospital.setHospital_longitude(null);
                        } else if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                            System.out.println("400 Error occurred for hospital: " + hospitalKey + ". Skipping this hospital.");
                            log.debug("400 Error occurred for hospital: {}. Skipping this hospital.", hospitalKey);
                            continue; // 400 에러가 발생한 병원은 무시하고 다음 병원으로 넘어감
                        } else {
                            System.out.println(response.getStatusCode() + " Error occurred for hospital: " + hospitalKey);
                            log.debug(response.getStatusCode() + " Error occurred for hospital: {}", hospitalKey);
                            continue; // 오류가 발생한 병원은 무시하고 다음 병원으로 넘어감
                        }
                    } catch (HttpClientErrorException.BadRequest e) {
                        System.out.println("400 Error occurred for hospital: " + hospitalKey + ". Skipping this hospital.");
                        log.debug("400 Error occurred for hospital: {}. Skipping this hospital.", hospitalKey);
                        continue; // 400 에러가 발생한 병원은 무시하고 다음 병원으로 넘어감
                    } catch (HttpServerErrorException e) {
                        System.out.println("500 Error occurred for hospital: " + hospitalKey);
                        log.debug("500 Error occurred for hospital: {}", hospitalKey);
                        continue; // 500 에러가 발생한 병원은 무시하고 다음 병원으로 넘어감
                    } catch (Exception e) {
                        System.out.println("Unexpected error occurred for hospital: " + hospitalKey);
                        log.debug("Unexpected error occurred for hospital: {}", hospitalKey);
                        continue; // 예상치 못한 오류가 발생한 병원은 무시하고 다음 병원으로 넘어감
                    }
                }

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
            log.error("CSV 파일을 읽는 중 오류가 발생했습니다: {}", e.getMessage());
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
                pharmacy.setDistrict(node.get("자치구").asText());
                pharmacyname = node.get("약국이름").asText();
                pharmacy.setName(pharmacyname);
                pharmacy.setPhar_name_en(translate(pharmacyname, "en-Us"));
                pharmacy.setPhar_name_ch(translate(pharmacyname, "ZH"));
                pharmacy.setPhar_name_ja(translate(pharmacyname, "JA"));
                pharmacy.setAddress(node.get("주소 (도로명)").asText());
                address = node.get("주소 (도로명)").asText();
                longtitude = generateCoordinate(address).getLongitude();
                latitude = generateCoordinate(address).getLatitude();
                pharmacy.setLongitude(longtitude);
                pharmacy.setLatitude(latitude);
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
