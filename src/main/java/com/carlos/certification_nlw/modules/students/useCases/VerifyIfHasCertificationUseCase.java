package com.carlos.certification_nlw.modules.students.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carlos.certification_nlw.modules.students.dto.VerifyHasCertificationDTO;
import com.carlos.certification_nlw.modules.students.repositories.CertificationStudentRepository;

@Service
public class VerifyIfHasCertificationUseCase {
    

    @Autowired
    private CertificationStudentRepository certificationStudentRepositoy;

    public boolean execute(VerifyHasCertificationDTO dto){
        var result = this.certificationStudentRepositoy.findByStudentEmailAndTechnology(dto.getEmail(), dto.getTechnology());
        
        if(!result.isEmpty()){
            return true;
        }
        return false;

    }


}
