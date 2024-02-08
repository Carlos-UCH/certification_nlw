package com.carlos.certification_nlw.modules.certifications.usecases;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carlos.certification_nlw.modules.students.entities.CertificationStudentEntity;
import com.carlos.certification_nlw.modules.students.repositories.CertificationStudentRepository;

@Service
public class Top10RankingUseCase {
    
    @Autowired
    private CertificationStudentRepository certificationStudentRepository;


    public List<CertificationStudentEntity> execute(){
        var result =  certificationStudentRepository.findTop10ByOrderByGradeDesc();
        return result; 
    }

}
