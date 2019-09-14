package it.valeriovaudi.familybudget.accountservice.web.endpoint;

import it.valeriovaudi.familybudget.accountservice.web.security.GlobalFrontChannelLogoutProvider;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OICDGlobalFrontChannelLogout {

    private final GlobalFrontChannelLogoutProvider globalFrontChannelLogoutProvider;

    public OICDGlobalFrontChannelLogout(GlobalFrontChannelLogoutProvider globalFrontChannelLogoutProvider) {
        this.globalFrontChannelLogoutProvider = globalFrontChannelLogoutProvider;
    }

    @GetMapping(value = "/oidc_logout.html")
    public String logout(Model model) {
        String logoutUrl = globalFrontChannelLogoutProvider.getLogoutUrl();
        model.addAttribute("logoutUrl", logoutUrl);
        return "redirect:" + logoutUrl;
    }
}
