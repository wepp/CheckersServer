package com.checkers.controllers;

import com.checkers.domain.vo.Game;
import com.checkers.services.IServerService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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
        if(serverService.serverStarted())
            model.addAttribute("gamesAmount", serverService.getGamesAmount());
        return "main/server";
    }

    @RequestMapping(value = {"/stop"})
    public String stop() {
        serverService.stopServer();
        return "redirect:/";
    }

    @RequestMapping(value = "/amount", method = RequestMethod.GET)
    public
    @ResponseBody
    int amount() {
        if(serverService.serverStarted())
            return serverService.getGamesAmount();
        return 0;
    }

    @RequestMapping(value = "/games", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Game> games() {
        if(serverService.serverStarted())
            return serverService.getGames();
        return Lists.newArrayList();
    }

    @RequestMapping(value = "/{gameId}/field/{fieldNum}", method = RequestMethod.GET)
    public
    @ResponseBody
    Game field(
            @PathVariable String gameId,
            @PathVariable String fieldNum,
            Model model) {
        model.addAttribute("gameId", gameId);
        return serverService.getGame(gameId, fieldNum);
    }


    @RequestMapping(value = "/finished/{gameId}", method = RequestMethod.GET)
    public
    @ResponseBody
    boolean finished(
            @PathVariable String gameId, Model model) {
        model.addAttribute("gameId", gameId);
        return serverService.finished(gameId);
    }


    @RequestMapping(value = "/winner/{gameId}", method = RequestMethod.GET)
    public
    @ResponseBody
    String getWinner(
            @PathVariable String gameId, Model model) {
        model.addAttribute("gameId", gameId);
        return serverService.getWinner(gameId);
    }

    @RequestMapping(value = "/game/{gameId}", method = RequestMethod.GET)
    public
    String games(
            @PathVariable String gameId, Model model) {
        model.addAttribute("gameId", gameId);
        return "main/gamePage";
    }
}
