package com.carlos.certification_nlw.modules.students.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carlos.certification_nlw.modules.students.dto.StudentCertificationAnswerDTO;
import com.carlos.certification_nlw.modules.students.dto.VerifyHasCertificationDTO;
import com.carlos.certification_nlw.modules.students.useCases.StudentCertificationAnswerUseCase;
import com.carlos.certification_nlw.modules.students.useCases.VerifyIfHasCertificationUseCase;

@RestController
@RequestMapping("/students")
public class StudentController {
    

    @Autowired
    private StudentCertificationAnswerUseCase studentCertificationAnswerUseCase;
    @Autowired
    private VerifyIfHasCertificationUseCase verifyIfHasCertificationUseCase;
    
    @PostMapping("/verifyHasCertification")
    public String verifyIfHasCertification(@RequestBody VerifyHasCertificationDTO verifyHasCertificationDTO){


        var result = this.verifyIfHasCertificationUseCase.execute(verifyHasCertificationDTO);
        if (result) {
            return "The user took the test";
        }

        return "User can take the test";
    
    }

        @PostMapping("/certification/answer")
        public ResponseEntity<Object> certificationAnswer(@RequestBody StudentCertificationAnswerDTO studentCertificationAnswerDTO) throws Exception {
           
           try{
            var result = studentCertificationAnswerUseCase.execute(studentCertificationAnswerDTO);
            return ResponseEntity.ok().body(result);
           }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage()); 

           }
        
        }
}
