package com.Nteam.backend.formediapi.medicine.service;
import com.Nteam.backend.formediapi.medicine.dto.MedicineElementDto;
import com.Nteam.backend.formediapi.medicine.entity.Medicine;
import com.Nteam.backend.formediapi.medicine.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicineService {
    @Autowired
    MedicineRepository medicineRepository;

    @Transactional(readOnly = true)
    public List<MedicineElementDto> getMedicineListByCategory(Long categoryId) {
        List<Medicine> medicines = medicineRepository.findAll();

        return medicines.stream()
                .filter(medicine -> medicine.getMedicineCategory().getCategory_id().equals(categoryId))
                .map(MedicineElementDto::new)
                .collect(Collectors.toList());
    }

}
