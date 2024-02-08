package com.carlos.certification_nlw.modules.students.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import com.carlos.certification_nlw.modules.questions.repositories.QuestionRepository;
import com.carlos.certification_nlw.modules.students.dto.StudentCertificationAnswerDTO;
import com.carlos.certification_nlw.modules.students.dto.VerifyHasCertificationDTO;
import com.carlos.certification_nlw.modules.students.entities.AnswersCetificationsEntity;
import com.carlos.certification_nlw.modules.students.entities.CertificationStudentEntity;
import com.carlos.certification_nlw.modules.students.entities.StudentEntity;
import com.carlos.certification_nlw.modules.students.repositories.CertificationStudentRepository;
import com.carlos.certification_nlw.modules.students.repositories.StudentRepository;
import com.carlos.certification_nlw.modules.questions.entities.QuestionEntity;  

@Service
public class StudentCertificationAnswerUseCase {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired 
    private CertificationStudentRepository certificationStudentRepositoy;

    @Autowired
    private VerifyIfHasCertificationUseCase verifyIfHasCertificationUseCase;

    public CertificationStudentEntity execute(StudentCertificationAnswerDTO dto) throws Exception {
        
      var hasCertification =  this.verifyIfHasCertificationUseCase.execute(new VerifyHasCertificationDTO(dto.getEmail(), dto.getTechnology()));

        if (hasCertification) {
            throw new Exception("Certification already obtained");
        }

       List<QuestionEntity>  questionsEntity =  questionRepository.findByTechnology(dto.getTechnology());
       List<AnswersCetificationsEntity> answersCetifications = new ArrayList<>();
        
       AtomicInteger correctAnswers = new AtomicInteger(0);
       
       dto.getQuestionsAnswers().stream().forEach(questionAnswer -> {
           var question = questionsEntity.stream().filter(q -> q.getId().equals(questionAnswer.getQuestionID())).findFirst().get();
            
          var findCorrectAlternative =  question.getAlternatives().stream().filter(alternative -> alternative.isCorrect()).findFirst().get();
            if (findCorrectAlternative.getId().equals(questionAnswer.getAlternativeID())) {
                questionAnswer.setCorrect(true);
                correctAnswers .incrementAndGet();

            }else{
                questionAnswer.setCorrect(false);
            }
            var answerrsCertificationEntity = AnswersCetificationsEntity.builder()
            .answerID(questionAnswer.getAlternativeID())
            .questionID(questionAnswer.getQuestionID())
            .isCorrect(questionAnswer.isCorrect()).build();
            
            answersCetifications.add(answerrsCertificationEntity);
        });

        var student = studentRepository.findByEmail(dto.getEmail());
        UUID studentID;

        if (student.isEmpty()) {
           var studentCreated = StudentEntity.builder().email(dto.getEmail()).build();
            studentCreated = studentRepository.save(studentCreated);
            studentID = studentCreated.getId();

        }else{
            studentID = student.get().getId();
        }


        CertificationStudentEntity certificationStudentEntity = CertificationStudentEntity.builder()
        .technology(dto.getTechnology())
        .studentID(studentID)
        .grade(correctAnswers.get())
        .build(); 


      var certificatioStudentCreated =   certificationStudentRepositoy.save(certificationStudentEntity);
       
      answersCetifications.stream().forEach(answerCetification -> {
        answerCetification.setCertificationID(certificationStudentEntity.getId());
        answerCetification.setCertificationStudentEntity(certificationStudentEntity);
      });

      certificationStudentEntity.setAnswersCetificationsEntities(answersCetifications);
      certificationStudentRepositoy.save(certificationStudentEntity);
      return certificatioStudentCreated;
    }

}
