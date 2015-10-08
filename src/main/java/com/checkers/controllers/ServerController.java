package com.checkers.controllers;

import com.checkers.domain.server.GameThread;
import com.checkers.domain.server.Server;
import com.checkers.services.IServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by KutsykV on 08.10.2015.
 */
@Controller
public class ServerController {

    @Autowired
    private IServerService serverService;

    @RequestMapping(value = {"/server"})
    public String desktop(Model model) {
        if(!serverService.serverStarted())
            serverService.startServer();
        model.addAttribute("gamesAmount", serverService.getGamesAmount());
        return "main/server";
    }

    @RequestMapping(value = "/games", method = RequestMethod.GET)
    public
    @ResponseBody
    List<GameThread> categories() {
        return serverService.getGames();
    }
}
