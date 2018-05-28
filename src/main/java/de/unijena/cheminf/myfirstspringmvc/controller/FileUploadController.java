package de.unijena.cheminf.myfirstspringmvc.controller;

import java.io.IOException;
import java.util.stream.Collectors;
import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.unijena.cheminf.myfirstspringmvc.storage.StorageFileNotFoundException;
import de.unijena.cheminf.myfirstspringmvc.storage.StorageService;


import de.unijena.cheminf.myfirstspringmvc.nplikeness.misc.InputParser;

@Controller
public class FileUploadController {


    private final StorageService storageService;
    private boolean returnedResults = false;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
        System.out.println("\n\n I'm in File Upload Controller!");
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

        return "index";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    /*@PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }*/


   @PostMapping("/")
    public String launchNPScorerOnFile(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        System.out.println( "\n" + file.getOriginalFilename() + "\n");
       storageService.store(file);
        redirectAttributes.addFlashAttribute("uploadSuccessful",
                "Working on scoring file! " + file.getOriginalFilename() + "!");


        String loadedFile = "upload-dir/"+ file.getOriginalFilename();

        InputParser inparser = new InputParser(loadedFile);
        //TODO replace by NPWorker
       redirectAttributes.addFlashAttribute("workerStarted",
               "NP-likeness scorer started");
        inparser.doWorkForSpring();
        //TODO erase the message workerStarted

       //TODO print the results to the page /




        //Do stuff with my file

        return "redirect:/";//redirect to results page when finished //TODO later
    }

    @GetMapping("/wait")
    public String letsWait(RedirectAttributes redirectAttributes){

        System.out.println("In wait 1");

       System.out.println("\n\n WAITING \n\n\n");
       // redirectAttributes.addFlashAttribute("wait",
         //       "Waiting for the results!");

       //DO STUFF HERE
        System.out.println("In wait");

       return "redirect:/results";
    }

    @RequestMapping("/results")
    @ResponseBody
    public String postResults(RedirectAttributes redirectAttributes){
            System.out.println("\n\n Results \n\n\n");
            redirectAttributes.addFlashAttribute("result",
                    "Here will be results");
            System.out.println("In results");
            return "redirect:/results";

    }



    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    //Cast multipart spring file to conventional file
    public File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException
    {
        File convFile = new File( multipart.getOriginalFilename());
        multipart.transferTo(convFile);
        return convFile;
    }
}
