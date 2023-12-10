package ra.academy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import ra.academy.model.Customer;
import ra.academy.service.ICustomerService;

@Controller
public class CustomerController {

    private final ICustomerService customerService;

    public CustomerController(ICustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/")
    public ModelAndView listCustomer() {
        ModelAndView modelAndView = new ModelAndView("/customer/list");
        modelAndView.addObject("customer", modelAndView);
        return modelAndView;
    }

    @GetMapping("/create")
    public String showFormCreate(Model model) {
        Customer customer = new Customer();
        model.addAttribute("customerForm", customer);
        return "customer/add";
    }

    @PostMapping("/create")
    public String createCustomer(@ModelAttribute("customerForm") Customer customer) {
        customerService.save(customer);
        return "redirect:/";
    }

    @GetMapping("/detail/{id}")
    public String showAddForm(@PathVariable("id") Long id, Model theModel) {
        Customer theCustomer = customerService.findById(id);
        theModel.addAttribute("customer", theCustomer);
        return "customer/detail";
    }

    @GetMapping("/delete/{id}")
    public ModelAndView showDeleteForm(@PathVariable("id") Long id) {
        Customer customer = customerService.findById(id);
        ModelAndView modelAndView = new ModelAndView("customer/delete");
        modelAndView.addObject("deleteForm", customer);
        return modelAndView;
    }

    @PostMapping("/delete")
    public String deleteById(@ModelAttribute("deleteForm") Customer customer) {
        customerService.deleteById(customer.getId());
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable("id") Long id) {
        Customer customer = customerService.findById(id);
        ModelAndView modelAndView = new ModelAndView("customer/edit");
        modelAndView.addObject("editForm", customer);
        return modelAndView;
    }

    @PostMapping("/edit/")
    public String editCustomer(@ModelAttribute("editForm") Customer customer) {
        customerService.save(customer);
        return "redirect:/";
    }
}
