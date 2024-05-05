package com.Nteam.backend.formediapi.medicine.controller;

import com.Nteam.backend.formediapi.medicine.entity.Medicine;
import com.Nteam.backend.formediapi.medicine.repository.MedicineRepository;
import com.Nteam.backend.formediapi.medicine.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/medicines")
public class MedicineController {

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private MedicineService medicineService;

    // 상비약 전체 조회
    @GetMapping("")
    public List<Medicine> getAllMedicines() {
        return medicineRepository.findAll();
    }

    // 특정 상비약 정보 조회
    @GetMapping("/{medicine_id}")
    public Optional<Medicine> getMedicine(@PathVariable Long medicine_id) {
        return medicineRepository.findById(medicine_id);
    }
//
//    @GetMapping("/category/{category_id}")
//    public List<MedicineElementDto> getMedicineListByCategory(@PathVariable Long category_id) {
//        return medicineService.getMedicineListByCategory(category_id);
//    }


}