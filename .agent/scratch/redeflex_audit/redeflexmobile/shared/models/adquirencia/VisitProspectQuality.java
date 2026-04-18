package com.axys.redeflexmobile.shared.models.adquirencia;

import com.annimon.stream.Stream;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Rogério Massa on 25/01/19.
 */

public class VisitProspectQuality {

    private Integer id;
    private Long idVisit;
    private String answers;
    private String questions;

    public VisitProspectQuality() {
    }

    public VisitProspectQuality(Integer id, Long idVisit, String answers, String questions) {
        this.id = id;
        this.idVisit = idVisit;
        this.answers = answers;
        this.questions = questions;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getIdVisit() {
        return idVisit;
    }

    public void setIdVisit(Long idVisit) {
        this.idVisit = idVisit;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public VisitProspectQualityRequest toRequest() {
        Gson gson = new Gson();
        VisitProspectQualityQuestion[] answers = gson.fromJson(this.answers, VisitProspectQualityQuestion[].class);
        VisitProspectQualityQuestion[] questions = gson.fromJson(this.questions, VisitProspectQualityQuestion[].class);

        return new VisitProspectQualityRequest(id,
                idVisit,
                Stream.ofNullable(answers).map(VisitProspectQualityQuestion::getId).toList(),
                Stream.ofNullable(questions).map(VisitProspectQualityQuestion::getId).toList());
    }

    public static class VisitProspectQualityRequest {

        @SerializedName("idAppMobile") public Integer id;
        @SerializedName("idVisita") Long idVisit;
        @SerializedName("validado") List<Integer> answers;
        @SerializedName("naovalidado") List<Integer> questions;

        VisitProspectQualityRequest(Integer id,
                                    Long idVisit,
                                    List<Integer> answers,
                                    List<Integer> questions) {
            this.id = id;
            this.idVisit = idVisit;
            this.answers = answers;
            this.questions = questions;
        }
    }
}
