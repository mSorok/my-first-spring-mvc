package de.unijena.cheminf.myfirstspringmvc.controller;


/**
 * @author mSorok
 */

import de.unijena.cheminf.myfirstspringmvc.nplikeness.NplService;
import de.unijena.cheminf.myfirstspringmvc.nplikeness.misc.InputParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


//@Controller
public class NPScoreCalculatorClontroller {
    private final NplService nplService;

    //@Autowired
    public NPScoreCalculatorClontroller(NplService nplService) {
        this.nplService = nplService;
        System.out.println("\n\n I'm in NPScoreCalculatorClontroller!");

        //Launch computations from here

        // "calculation in process page"

        // results page
    }

    //do stuff here


   //@PostMapping("/waitForResults")
    public String launchNPScorerOnFile(RedirectAttributes redirectAttributes) {



        redirectAttributes.addFlashAttribute("wait",
                "Please wait for your results");




        //Do stuff with my file

        return "redirect:/results";//redirect to results page when finished
    }

}
