package com.therap.supply.chain.user.atest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        BooksCreationDto booksForm = new BooksCreationDto();

        for (int i = 1; i <= 3; i++) {
            booksForm.addBook(new Book());
        }

        model.addAttribute("form", booksForm);
        return "test/createBooksForm";
    }

    @PostMapping("/save")
    @ResponseBody
    public BooksCreationDto saveBooks(@ModelAttribute BooksCreationDto form, Model model) {

        return form;
    }

}
