package com.japan.compass.annotation.controller.admin;

import com.japan.compass.annotation.domain.ProjectList;
import com.japan.compass.annotation.domain.ProjectModel;
import com.japan.compass.annotation.domain.entity.Project;
import com.japan.compass.annotation.exception.ApplicationException;
import com.japan.compass.annotation.exception.Errors;
import com.japan.compass.annotation.service.admin.ProjectManagementService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.constraints.Min;

@AllArgsConstructor
@Slf4j
@Validated
@Controller
@RequestMapping("/admin/project")
public class ProjectManagementController {

    private final ProjectManagementService projectManagementService;

    /*
     * プロジェクト管理画面
     */
    @GetMapping("")
    public String getProject(
            Model model
    ) {
        ProjectList projectList = projectManagementService.getProjectList();
        model.addAttribute("projectList", projectList);
        return "admin/project";
    }

    @GetMapping("/new")
    public String getNewProject() {
        return "admin/project_new";
    }

    @PostMapping("/new")
    public String postNewProject(
            @Validated @ModelAttribute ProjectModel projectModel,
            BindingResult bindingResult
    ) throws ApplicationException {

        if (bindingResult.hasErrors()) {
            log.info("binding error." + bindingResult.getAllErrors());
            throw new ApplicationException(Errors.INVALID_REQUEST);
        }

        projectManagementService.createNewProject(projectModel);
        return "redirect:/admin/project";
    }

    @GetMapping("/edit/{id}")
    public String editProject(
            @Min(1) @PathVariable(value = "id", required = true) int id,
            Model model
    ) {
        Project project = projectManagementService.getProject(id);
        model.addAttribute("project", project);
        return "admin/project_edit";
    }

    @PostMapping("/update")
    public String updateProject(
            @Validated @ModelAttribute ProjectModel projectModel,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) throws ApplicationException {

        if (bindingResult.hasErrors()) {
            log.info("binding error." + bindingResult.getAllErrors());
            throw new ApplicationException(Errors.INVALID_REQUEST);
        }

        projectManagementService.update(projectModel);
        redirectAttributes.addFlashAttribute("updated", true);
        return "redirect:/admin/project/edit/" + projectModel.getId();
    }

    @PostMapping("/delete/{id}")
    public String deleteProject(
            @Min(1) @PathVariable(value = "id", required = true) int id
    ) {
        projectManagementService.delete(id);
        return "redirect:/admin/project";
    }
}
