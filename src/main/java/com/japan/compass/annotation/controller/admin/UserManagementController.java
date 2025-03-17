package com.japan.compass.annotation.controller.admin;

import javax.validation.constraints.Min;

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

import com.japan.compass.annotation.domain.UserList;
import com.japan.compass.annotation.domain.UserModel;
import com.japan.compass.annotation.domain.entity.User;
import com.japan.compass.annotation.exception.ApplicationException;
import com.japan.compass.annotation.exception.Errors;
import com.japan.compass.annotation.service.admin.UserManagementService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@AllArgsConstructor
@Slf4j
@Validated
@Controller
@RequestMapping("/admin/user")
public class UserManagementController {

    private final UserManagementService userManagementService;
    
    /*
     * ユーザー管理画面
     */
    @GetMapping("")
    public String getUser(
            @Min(1) @RequestParam(value = "page", defaultValue = "1", required = true) int page,
            Model model
    ) {
        UserList userList = userManagementService.getUserList(
                UserList.LIMIT.getValue(),
                UserList.LIMIT.getValue() * (page-1));
        model.addAttribute("userList", userList);
        model.addAttribute("currentPage", page);
        
        return "admin/user";
    }

    @GetMapping("/edit/{id}")
    public String editUser(
            @Min(1) @PathVariable(value = "id", required = true) int id,
            Model model
    ) {
        User user = userManagementService.getUser(id);
        model.addAttribute("user", user);
        return "admin/user_edit";
    }

    @PostMapping("/update")
    public String updateUser(
            @Validated @ModelAttribute UserModel userModel,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) throws ApplicationException {

        if (bindingResult.hasErrors()) {
            log.info("binding error." + bindingResult.getAllErrors());
            throw new ApplicationException(Errors.INVALID_REQUEST);
        }

        userManagementService.updateUser(userModel);
        redirectAttributes.addFlashAttribute("updated", true);
        return "redirect:/admin/user/edit/" + userModel.getId();
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(
            @Min(1) @PathVariable(value = "id", required = true) int id
    ) {
        userManagementService.deleteUser(id);
        return "redirect:/admin/user";
    }
}
