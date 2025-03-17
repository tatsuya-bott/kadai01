package com.japan.compass.annotation.controller.admin;

import com.japan.compass.annotation.domain.ProjectList;
import com.japan.compass.annotation.domain.QuestionList;
import com.japan.compass.annotation.domain.QuestionModel;
import com.japan.compass.annotation.domain.entity.Question;
import com.japan.compass.annotation.exception.ApplicationException;
import com.japan.compass.annotation.exception.Errors;
import com.japan.compass.annotation.service.admin.ProjectManagementService;
import com.japan.compass.annotation.service.admin.QuestionManagementService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.constraints.Min;

@AllArgsConstructor
@Slf4j
@Validated
@Controller
@RequestMapping("/admin/question")
public class QuestionManagementController {

    private final ProjectManagementService projectManagementService;
    private final QuestionManagementService questionManagementService;

    /*
     * 質問管理画面
     */
    @GetMapping("")
    public String getQuestion(
            @Min(0) @RequestParam(value = "project_id", defaultValue = "0", required = true) int project_id,
            Model model
    ) {
        QuestionList questionList = questionManagementService.getQuestionList();
        model.addAttribute("questionList", questionList);
        if (project_id != 0) {
            model.addAttribute("questionList", questionList.getTargetList(project_id));
            model.addAttribute("projectId", project_id);
        }

        ProjectList projectList = projectManagementService.getProjectList();
        model.addAttribute("projectList", projectList);

        return "admin/question";
    }

    @GetMapping("/new")
    public String getNewQuestion(
            Model model
    ) {
        ProjectList projectList = projectManagementService.getProjectList();
        model.addAttribute("projectList", projectList);
        return "admin/question_new";
    }

    @PostMapping("/new")
    public String postNewQuestion(
            @Validated @ModelAttribute QuestionModel questionModel,
            BindingResult bindingResult
    ) throws ApplicationException {

        if (bindingResult.hasErrors()) {
            log.info("binding error." + bindingResult.getAllErrors());
            throw new ApplicationException(Errors.INVALID_REQUEST);
        }

        questionManagementService.createNewQuestion(questionModel);
        return "redirect:/admin/question?project_id=" + questionModel.getProjectId();
    }

    @GetMapping("/edit/{id}")
    public String editQuestion(
            @Min(1) @PathVariable(value = "id", required = true) int id,
            Model model
    ) {
        Question question = questionManagementService.getQuestion(id);
        model.addAttribute("question", question);
        return "admin/question_edit";
    }

    @PostMapping("/update")
    public String updateQuestion(
            @Validated @ModelAttribute QuestionModel questionModel,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) throws ApplicationException {

        if (bindingResult.hasErrors()) {
            log.info("binding error." + bindingResult.getAllErrors());
            throw new ApplicationException(Errors.INVALID_REQUEST);
        }

        questionManagementService.update(questionModel);
        redirectAttributes.addFlashAttribute("updated", true);
        return "redirect:/admin/question/edit/" + questionModel.getQuestionId();
    }

    @PostMapping("/delete/{id}")
    public String deleteQuestion(
            @Min(1) @PathVariable(value = "id", required = true) int questionId,
            @Min(1) @RequestParam(value = "projectId", required = true) int projectId
    ) {
        questionManagementService.delete(questionId);
        return "redirect:/admin/question?project_id=" + projectId;
    }
}
