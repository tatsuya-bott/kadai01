package com.japan.compass.annotation.controller.user;

import com.japan.compass.annotation.domain.AnnotationImage;
import com.japan.compass.annotation.domain.AnnotationModel;
import com.japan.compass.annotation.domain.ProjectList;
import com.japan.compass.annotation.domain.QuestionList;
import com.japan.compass.annotation.domain.entity.User;
import com.japan.compass.annotation.exception.ApplicationException;
import com.japan.compass.annotation.exception.Errors;
import com.japan.compass.annotation.exception.SystemException;
import com.japan.compass.annotation.service.user.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;

@Validated
@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    /*
     * 回答者入力画面
     */
    @GetMapping("/answerer")
    public String getAnswerer() {
        return "user/answerer";
    }

    @PostMapping("/answerer")
    public String postAnswerer(
            HttpServletRequest request,
            @AuthenticationPrincipal User user,
            @Email(regexp = ".+@.+") @RequestParam(value = "mail") String mail
    ) throws ApplicationException {
        try {
            userService.registerUserInfo(mail);

            // ログアウト
            if (user != null) {
                log.info("user: {}", user);
                SecurityContextHolder.clearContext();
            }

            request.login(mail, User.RAW_USER_PASSWORD);
        } catch (ApplicationException | SystemException e) {
            throw e;
        } catch (Exception ex) {
            log.warn("user login error.", ex);
            throw new ApplicationException(Errors.INTERNAL_SERVER_ERROR);
        }

        return "redirect:/user/project";
    }

    /*
     * プロジェクト選択画面
     */
    @GetMapping("/project")
    public String getProject(Model model) {
        ProjectList projects = userService.getEnabledProjects();
        model.addAttribute("projects", projects);
        return "user/project";
    }

    /*
     * 質問選択画面
     */
    @GetMapping("/question")
    public String getQuestion(
            @Min(1) @RequestParam(value = "project_id", required = true) int projectId,
            Model model
    ) {
        QuestionList questions = userService.getEnabledQuestionsByProjectId(projectId);
        model.addAttribute("questions", questions);
        return "user/question";
    }

    /*
     * アノテーション画面
     */
    @GetMapping("/annotation")
    public String getAnnotation(
            @Min(1) @RequestParam(value = "question_id", required = true) int questionId,
            Model model
    ) throws ApplicationException {

        AnnotationImage annotationImage = userService.getAnnotationImage(questionId);
        model.addAttribute("annotationImage", annotationImage);

        return "user/annotation_type" + annotationImage.numberOfType();
    }

    @PostMapping("/annotation")
    public String postAnnotation(
            @AuthenticationPrincipal User user,
            @Validated @ModelAttribute AnnotationModel annotationModel,
            BindingResult bindingResult,
            Model model
    ) throws ApplicationException {

        if (bindingResult.hasErrors()) {
            log.info("binding error." + bindingResult.getAllErrors());
            throw new ApplicationException(Errors.INVALID_REQUEST);
        }

        userService.registerAnnotation(user, annotationModel);

        return "redirect:/user/annotation?question_id=" + annotationModel.getQuestionId();
    }
}
