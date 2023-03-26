package ch.hegarc.guestbook;

import ch.hegarc.guestbook.models.Entry;
import ch.hegarc.guestbook.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    @Autowired
    private GuestbookFirestoreService guestbookFirestoreService;

    @Autowired
    private GuestbookListService guestbookListService;

    @Autowired
    private GuestbookSqlService guestbookSqlService;

    @Autowired
    private Environment env;

    private GuestbookService getGuestbookService() {
        String guestbookServiceClass = env.getProperty("guestbook.service", "list");
        if (guestbookServiceClass.equals("firestore")) {
            return guestbookFirestoreService;
        }
        if (guestbookServiceClass.equals("sql")) {
            return guestbookSqlService;
        }
        return guestbookListService;
    }


	@GetMapping("/")
	public String greeting(Model model) {
		model.addAttribute("entries", getGuestbookService().select());
        model.addAttribute("service", getGuestbookService().getClass().getName());
		return "index";
	}

    @GetMapping("/sign")
    public String sign(Model model) {
        model.addAttribute("entry", new Entry());
        return "sign";
    }

    @PostMapping( "/sign")
    public String signProcess(@ModelAttribute Entry entry) {
        getGuestbookService().insert(entry);
        return "redirect:/";
    }

}
